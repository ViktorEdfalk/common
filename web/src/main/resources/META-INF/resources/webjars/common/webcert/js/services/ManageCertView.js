/**
 * Common certificate management methods between certificate modules
 */
angular.module('common').factory('common.ManageCertView',
    ['$rootScope', '$document', '$log', '$location', '$stateParams', '$timeout', '$window', '$q',
        'common.CertificateService', 'common.dialogService', 'common.messageService', 'common.statService',
        'common.UserModel', 'common.UtkastViewStateService', 'common.wcFocus', 'common.utkastNotifyService',
        function($rootScope, $document, $log, $location, $stateParams, $timeout, $window, $q,
            CertificateService, dialogService, messageService, statService, UserModel, CommonViewState, wcFocus, utkastNotifyService) {
            'use strict';

            /**
             * Load draft to webcert
             * @param viewState
             * @private
             */
            function _load(viewState) {
                var intygsTyp = viewState.common.intyg.type;
                CommonViewState.doneLoading = false;
                CertificateService.getDraft($stateParams.certificateId, intygsTyp, function(data) {

                    viewState.common.update(viewState.draftModel, data);

                    // check that the certs status is not signed
                    if(viewState.draftModel.isSigned()){
                        // just change straight to the intyg
                        $location.url('/intyg/' + intygsTyp + '/' + viewState.draftModel.content.id);
                    }
                    else {
                        $timeout(function() {
                            wcFocus('firstInput');
                            $rootScope.$broadcast('intyg.loaded', viewState.draftModel.content);
                            $rootScope.$broadcast(intygsTyp + '.loaded', viewState.draftModel.content);
                            CommonViewState.doneLoading = true;
                        }, 10);
                    }

                }, function(error) {
                    CommonViewState.doneLoading = true;
                    CommonViewState.error.activeErrorMessageKey = checkSetError(error.errorCode);
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
             * Save draft to webcert
             * @param autoSave
             * @private
             */
            function _save(extras) {
                if (CertificateService.isSaveDraftInProgress()) {
                    return false;
                }

                var deferred = $q.defer();
                $rootScope.$broadcast('saveRequest', deferred);

                deferred.promise.then(function(intygState) {

                    var saveComplete = $q.defer();

                    var saveCompletePromise = saveComplete.promise.then(function(result) {
                        // save success
                        intygState.viewState.common.validationSections = result.validationSections;
                        intygState.viewState.common.validationMessages = result.validationMessages;
                        intygState.viewState.common.validationMessagesGrouped = result.validationMessagesGrouped;
                        intygState.viewState.common.error.saveErrorMessage = null;
                        intygState.viewState.common.error.saveErrorCode = null;
                        intygState.viewState.draftModel.version = result.version;

                    }, function(result) {
                        // save failed
                        intygState.formFail();
                        intygState.viewState.common.error.saveErrorMessage = result.errorMessage;
                        intygState.viewState.common.error.saveErrorCode = result.errorCode;
                    });

                    saveCompletePromise.finally(function(){
                        if(extras && extras.destroy ){
                            extras.destroy();
                        }
                    });

                    CertificateService.saveDraft( intygState.viewState.intygModel.id, intygState.viewState.common.intyg.type,
                            intygState.viewState.draftModel.version, intygState.viewState.intygModel.toSendModel(),
                        function(data) {

                                var result = {};
                                result.validationMessagesGrouped = {};
                                result.validationMessages = [];
                                result.validationSections = [];
                                result.version = data.version;

                                if (data.status === 'COMPLETE') {
                                    CommonViewState.intyg.isComplete = true;
                                    saveComplete.resolve(result);
                                } else {
                                    CommonViewState.intyg.isComplete = false;

                                    if (!CommonViewState.showComplete) {
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
                                            if(result.validationSections.indexOf(section) === -1){
                                                result.validationSections.push(section);
                                            }

                                            if (result.validationMessagesGrouped[section]) {
                                                result.validationMessagesGrouped[section].push(message);
                                            } else {
                                                result.validationMessagesGrouped[section] = [message];
                                            }
                                        }
                                    });
                                    saveComplete.resolve(result);
                                }
                            }, function(error) {
                                // Show error message if save fails

                                var errorMessage;
                                var variables = null;
                                if (error.errorCode === 'CONCURRENT_MODIFICATION') {
                                    // In the case of concurrent modification we should have the name of the user making trouble in the message.
                                    variables = {name: error.message};
                                }

                                var errorCode = error.errorCode;
                                if (typeof errorCode === 'undefined') {
                                    errorCode = 'unknown';
                                }
                                var errorMessageId = checkSetErrorSave(errorCode);
                                errorMessage = messageService.getProperty(errorMessageId, variables, errorMessageId);

                                var result = {
                                    errorMessage: errorMessage,
                                    errorCode: errorCode
                                };
                                saveComplete.reject(result);
                            }
                        );
            });
                return true;
            }

            function checkSetError(errorCode) {
                var model = 'common.error.unknown';
                if (errorCode !== undefined && errorCode !== null) {
                    model = ('common.error.' + errorCode).toLowerCase();
                }

                return model;
            }

            function signera(intygsTyp, version) {
                var deferred = $q.defer();
                if (UserModel.userContext.authenticationScheme === 'urn:inera:webcert:fake') {
                    return _signeraServer(intygsTyp, $stateParams.certificateId, version, deferred);
                } else {
                    return _signeraKlient(intygsTyp, $stateParams.certificateId, version, deferred);
                }
            }

            function _signeraServer(intygsTyp, intygsId, version, deferred) {
                var signModel = {};
                _confirmSignera(signModel, intygsTyp, intygsId, version, deferred);
                return deferred.promise;
            }

            function _signeraKlient(intygsTyp, intygsId, version, deferred) {
                var signModel = {
                    signingWithSITHSInProgress : true
                };
                CertificateService.getSigneringshash(intygsId, intygsTyp, version, function(ticket) {
                    _openNetIdPlugin(ticket.hash, function(signatur) {
                        CertificateService.signeraUtkastWithSignatur(ticket.id, intygsTyp, signatur, function(ticket) {

                            if (ticket.status === 'SIGNERAD') {
                                deferred.resolve({newVersion : ticket.version});
                                _showIntygAfterSignering(signModel, intygsTyp, intygsId);
                            } else {
                                _waitForSigneringsstatusSigneradAndClose(signModel, intygsTyp, intygsId, ticket, deferred);
                            }

                        }, function(error) {
                            deferred.resolve({newVersion : ticket.version});
                            _showSigneringsError(signModel, error);
                        });
                    }, function(error) {
                        deferred.resolve({newVersion : ticket.version});
                        _showSigneringsError(signModel, error);
                    });
                }, function(error) {
                    deferred.resolve({});
                    _showSigneringsError(signModel, error);
                });
                return deferred.promise;
            }

            function _confirmSignera(signModel, intygsTyp, intygsId, version, deferred) {
                CertificateService.signeraUtkast(intygsId, intygsTyp, version, function(ticket) {
                    _waitForSigneringsstatusSigneradAndClose(signModel, intygsTyp, intygsId, ticket, deferred);
                }, function(error) {
                    deferred.resolve({});
                    _showSigneringsError(signModel, error);
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

            function _waitForSigneringsstatusSigneradAndClose(signModel, intygsTyp, intygsId, ticket, deferred) {

                function getSigneringsstatus() {
                    CertificateService.getSigneringsstatus(ticket.id, intygsTyp, function(ticket) {
                        if ('BEARBETAR' === ticket.status) {
                            signModel._timer = $timeout(getSigneringsstatus, 1000);
                        } else if ('SIGNERAD' === ticket.status) {
                            deferred.resolve({newVersion : ticket.version});
                            _showIntygAfterSignering(signModel, intygsTyp, intygsId);
                        } else {
                            deferred.resolve({newVersion : ticket.version});
                            _showSigneringsError(signModel, {errorCode: 'SIGNERROR'});
                        }
                    });
                }

                getSigneringsstatus();
            }

            function _showIntygAfterSignering(signModel, intygsTyp, intygsId) {
                signModel.signingWithSITHSInProgress = false;

                $location.replace();
                $location.path('/intyg/' + intygsTyp + '/' + intygsId).search('signed', true);
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
                    } else if (error.errorCode === 'CONCURRENT_MODIFICATION') {
                        messageId = 'common.error.sign.concurrent_modification';
                    } else if (error === '') {
                        messageId = 'common.error.cantconnect';
                    } else {
                        messageId = 'common.error.signerror';
                    }
                }
                return messageId;
            }

            function _showSigneringsError(signModel, error) {
                var sithssignerrormessageid = _setErrorMessageId(error);

                var errorMessage;
                var variables = null;
                if (error.errorCode === 'CONCURRENT_MODIFICATION') {
                    // In the case of concurrent modification we should have the name of the user making trouble in the message.
                    variables = {name: error.message};
                }
                errorMessage = messageService.getProperty(sithssignerrormessageid, variables, sithssignerrormessageid);
                dialogService.showErrorMessageDialog(errorMessage);
                signModel.signingWithSITHSInProgress = false;
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

            function _notifyUtkast(intygId, intygType, utkast, updateState) {
                var utkastNotifyRequest = {
                    intygId: intygId,
                    intygType: intygType,
                    intygVersion: utkast.version,
                    vidarebefordrad: utkast.vidarebefordrad
                };
                utkastNotifyService.notifyUtkast(utkastNotifyRequest, updateState).then(function(vidarebefordradResult) {
                    onNotifyChangeSuccess(utkast, updateState, vidarebefordradResult);
                }, function(error) {
                    onNotifyChangeFail(utkast, updateState, error);
                });
            }

            function _onNotifyChange(intygId, intygType, utkast, updateState) {
                updateState.vidarebefordraInProgress = true;

                var utkastNotifyRequest = {
                    intygId: intygId,
                    intygType: intygType,
                    intygVersion: utkast.version,
                    vidarebefordrad: utkast.vidarebefordrad
                };

                var deferred = $q.defer();
                utkastNotifyService.onNotifyChange(utkastNotifyRequest, deferred).then(function(vidarebefordradResult) {
                    onNotifyChangeSuccess(utkast, updateState, vidarebefordradResult);
                }, function(error) {
                    utkast.vidarebefordrad = !utkast.vidarebefordrad;
                    onNotifyChangeFail(utkast, updateState, error);
                });
            }

            function onNotifyChangeSuccess(utkast, updateState, vidarebefordradResult) {
                updateState.vidarebefordraInProgress = false;
                if(vidarebefordradResult !== null) {
                    utkast.vidarebefordrad = vidarebefordradResult.vidarebefordrad;
                    utkast.version = vidarebefordradResult.version;
                }
            }

            function onNotifyChangeFail(utkast, updateState, error) {
                updateState.vidarebefordraInProgress = false;
                var errorMessage = 'Kunde inte markera/avmarkera intyget som ' +
                    'vidarebefordrat. Försök gärna igen för att se om felet är tillfälligt. Annars kan ' +
                    'du kontakta supporten. Läs mer under Om webcert | Support och kontaktinformation.';

                if (error && error.errorCode === 'CONCURRENT_MODIFICATION') {
                    var errorMessageId = 'common.error.save.concurrent_modification';
                    errorMessage = messageService.getProperty(errorMessageId, {name: error.message}, errorMessageId);
                }

                dialogService.showErrorMessageDialog(errorMessage);
            }

            // Return public API for the service
            return {
                load: _load,
                save: _save,
                signera: signera,
                isRevoked: _isRevoked,
                isSentToTarget: _isSentToTarget,
                printDraft: _printDraft,
                notifyUtkast: _notifyUtkast,
                onNotifyChange: _onNotifyChange,

                __test__: {
                    confirmSignera: _confirmSignera,
                    setErrorMessageId: _setErrorMessageId,
                    showSigneringsError: _showSigneringsError
                }
            };
        }]);
