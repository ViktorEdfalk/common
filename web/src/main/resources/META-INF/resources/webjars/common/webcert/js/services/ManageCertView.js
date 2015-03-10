/**
 * Common certificate management methods between certificate modules
 */
angular.module('common').factory('common.ManageCertView',
    ['$rootScope', '$document', '$log', '$location', '$stateParams', '$timeout', '$window', '$q',
        'common.CertificateService', 'common.dialogService', 'common.messageService', 'common.statService',
        'common.User', 'common.CertViewState',
        function($rootScope, $document, $log, $location, $stateParams, $timeout, $window, $q,
            CertificateService, dialogService, messageService, statService, User, CertViewState) {
            'use strict';

            /**
             * Load draft to webcert
             * @param $scope
             * @private
             */
            function _load($scope, intygsTyp, onSuccess) {
                $scope.viewState.doneLoading = false;
                CertificateService.getDraft($stateParams.certificateId, intygsTyp, function(data) {
                    $scope.viewState.doneLoading = true;
                    $scope.viewState.activeErrorMessageKey = null;
                    $scope.cert = data.content;
                    $scope.certMeta.intygId = data.content.id;
                    $scope.certMeta.vidarebefordrad = data.vidarebefordrad;
                    $scope.isSigned = data.status === 'SIGNED';
                    $scope.isComplete = $scope.isSigned || data.status === 'DRAFT_COMPLETE';
                    if (onSuccess !== undefined) {
                        onSuccess(data.content);
                    }
                }, function(error) {
                    $scope.viewState.doneLoading = true;
                    $scope.viewState.activeErrorMessageKey = checkSetError(error.errorCode);
                });
            }

            function checkSetErrorSave(errorCode) {
                var model = 'common.error.save.unknown';
                if (errorCode !== undefined && errorCode !== null) {
                    model = ('common.error.save.' + errorCode).toLowerCase();
                }

                return model;
            }

            /**
             * Action to save the certificate draft to the server.
             */
            function _save(autoSave) {
                if (autoSave && CertificateService.isSaveDraftInProgress()) {
                    return false;
                }

                var deferred = $q.defer();
                $rootScope.$emit('saveRequest',deferred);
                deferred.promise.then(function(saveIntygModel) {
                    CertificateService.saveDraft(saveIntygModel.intygsId, saveIntygModel.intygsTyp, saveIntygModel.cert, autoSave,
                        function(data) {

                            var result = {};
                            result.validationMessagesGrouped = {};
                            result.validationMessages = [];

                            if (data.status === 'COMPLETE') {
                                CertViewState.viewState.intyg.isComplete = true;
                            } else {
                                CertViewState.viewState.intyg.isComplete = false;

                                if (!CertViewState.viewState.showComplete) {
                                    result.validationMessages = data.messages.filter(function(message) {
                                        return (message.type !== 'EMPTY');
                                    });
                                }
                                else {
                                    result.validationMessages = data.messages;
                                }

                                angular.forEach(result.validationMessages, function(message) {
                                    var field = message.field;
                                    var parts = field.split('.');
                                    var section;
                                    if (parts.length > 0) {
                                        section = parts[0].toLowerCase();

                                        if (result.validationMessagesGrouped[section]) {
                                            result.validationMessagesGrouped[section].push(message);
                                        } else {
                                            result.validationMessagesGrouped[section] = [message];
                                        }
                                    }
                                });

                                saveIntygModel.saveComplete.resolve(result);
                            }
                        }, function(error) {
                            // Show error message if save fails
                            var result = {
                                errorMessageKey : checkSetErrorSave(error.errorCode)
                            };
                            saveIntygModel.saveComplete.reject(result);
                        }
                    );
                });
                return true;
            };

            function checkSetError(errorCode) {
                var model = 'common.error.unknown';
                if (errorCode !== undefined && errorCode !== null) {
                    model = ('common.error.' + errorCode).toLowerCase();
                }

                return model;
            }

            function signera($scope, intygsTyp) {
                if (User.userContext.authenticationScheme === 'urn:inera:webcert:fake') {
                    return _signeraServer($scope, intygsTyp, $stateParams.certificateId);
                } else {
                    return _signeraKlient($scope, intygsTyp, $stateParams.certificateId);
                }
            }

            function _signeraServer($scope, intygsTyp, intygsId) {
                var bodyText = 'Är du säker på att du vill signera intyget?';
                var confirmDialog = dialogService.showDialog($scope, {
                    dialogId: 'confirm-sign',
                    titleId: 'common.modal.label.confirm_sign',
                    bodyText: bodyText,
                    autoClose: false,
                    button1id: 'confirm-signera-utkast-button',

                    button1click: function() {
                        _confirmSignera($scope, intygsTyp, intygsId, confirmDialog);
                    },
                    button1text: 'common.sign',
                    button2click: function() {
                        if ($scope._timer) {
                            $timeout.cancel($scope._timer);
                        }
                        $scope.dialog.acceptprogressdone = true;
                    },
                    button2text: 'common.cancel'
                });
            }

            function _signeraKlient($scope, intygsTyp, intygsId) {
                    $scope.signingWithSITHSInProgress = true;
                    CertificateService.getSigneringshash(intygsId, intygsTyp, function(ticket) {
                    _openNetIdPlugin(ticket.hash, function(signatur) {
                        CertificateService.signeraUtkastWithSignatur(ticket.id, intygsTyp, signatur, function(ticket) {

                            if (ticket.status === 'SIGNERAD') {
                                _showIntygAfterSignering($scope, intygsTyp, intygsId);
                            } else {
                                _waitForSigneringsstatusSigneradAndClose($scope, intygsTyp, intygsId, ticket);
                            }

                        }, function(error) {
                            _showSigneringsError($scope, error);
                        });
                    }, function(error) {
                        _showSigneringsError($scope, error);
                    });
                }, function(error) {
                    _showSigneringsError($scope, error);
                });
            }

            function _confirmSignera($scope, intygsTyp, intygsId, confirmDialog) {
                $scope.dialog.acceptprogressdone = false;
                $scope.dialog.showerror = false;
                CertificateService.signeraUtkast(intygsId, intygsTyp, function(ticket) {
                    _waitForSigneringsstatusSigneradAndClose($scope, intygsTyp, intygsId, ticket,
                        confirmDialog);
                }, function(error) {
                    _showSigneringsError($scope, error);
                });
            }

            function _openNetIdPlugin(hash, onSuccess, onError) {
                $timeout(function() {
                    iid_SetProperty('Base64', 'true');
                    iid_SetProperty('DataToBeSigned', hash);
                    iid_SetProperty('URLEncode', 'false');
                    var resultCode = iid_Invoke('Sign');

                    if (resultCode === 0) {
                        onSuccess(iid_GetProperty('Signature'));
                    } else {
                        var message = 'Signeringen avbröts med kod: ' + resultCode;
                        $log.info(message);
                        onError({ errorCode: 'SIGN_NETID_ERROR'});
                    }
                });
            }

            function _waitForSigneringsstatusSigneradAndClose($scope, intygsTyp, intygsId, ticket, dialog) {

                function getSigneringsstatus() {
                    CertificateService.getSigneringsstatus(ticket.id, intygsTyp, function(ticket) {
                        if ('BEARBETAR' === ticket.status) {
                            $scope._timer = $timeout(getSigneringsstatus, 1000);
                        } else if ('SIGNERAD' === ticket.status) {
                            _showIntygAfterSignering($scope, intygsTyp, intygsId, dialog);
                        } else {
                            _showSigneringsError($scope, {errorCode: 'SIGNERROR'});
                        }
                    });
                }

                getSigneringsstatus();
            }

            function _showIntygAfterSignering($scope, intygsTyp, intygsId, dialog) {
                if (dialog) {
                    dialog.close();
                }
                $scope.signingWithSITHSInProgress = false;

                $location.replace();
                $location.path('/intyg/' + intygsTyp + '/' + intygsId);
                statService.refreshStat();
            }

            function _setErrorMessageId(error) {
                var messageId = '';

                if (error === undefined) {
                    $log.debug('_setErrorMessageId: Error is not defined.');
                    messageId = 'common.error.signerror';
                }
                else {
                    if (error.errorCode === 'DATA_NOT_FOUND') {
                        messageId = 'common.error.certificatenotfound';
                    } else if (error.errorCode === 'INVALID_STATE') {
                        messageId = 'common.error.certificateinvalidstate';
                    } else if (error.errorCode === 'SIGN_NETID_ERROR') {
                        messageId = 'common.error.signerrornetid';
                    } else if (error === '') {
                        messageId = 'common.error.cantconnect';
                    } else {
                        messageId = 'common.error.signerror';
                    }
                }
                return messageId;
            }

            function _showSigneringsError($scope, error) {
                if ($scope.dialog) {
                    $scope.dialog.acceptprogressdone = true;
                    $scope.dialog.showerror = true;
                    $scope.dialog.errormessageid = _setErrorMessageId(error);
                } else {
                    var sithssignerrormessageid = _setErrorMessageId(error);
                    var errorMessage = messageService.getProperty(sithssignerrormessageid, null, sithssignerrormessageid);
                    dialogService.showErrorMessageDialog(errorMessage);
                    $scope.signingWithSITHSInProgress = false;
                }
            }

            function _isSentToTarget(statusArr, target) {
                if (statusArr) {
                    for (var i = 0; i < statusArr.length; i++) {
                        if (statusArr[i].target === target && statusArr[i].type === 'SENT') {
                            return true;
                        }
                    }
                }
                return false;
            }

            function _isRevoked(statusArr) {
                if (statusArr) {
                    for (var i = 0; i < statusArr.length; i++) {
                        if (statusArr[i].type === 'CANCELLED') {
                            return true;
                        }
                    }
                }
                return false;
            }

            function _printDraft(intygsId, intygsTyp) {
                $window.print();
                CertificateService.logPrint(intygsId, intygsTyp, function(data) {
                    $log.debug('_logPrint, success: ' + data);
                });
            }


            // Return public API for the service
            return {
                load: _load,
                save: _save,
                signera: signera,
                isRevoked: _isRevoked,
                isSentToTarget: _isSentToTarget,
                printDraft: _printDraft,

                __test__: {
                    confirmSignera: _confirmSignera
                }
            };
        }]);
