/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

angular.module('common').factory('common.IntygService',
    [ '$log', '$timeout', '$state', '$stateParams', 'common.dialogService', 'common.IntygProxy', 'common.authorityService', 'common.ObjectHelper', 'common.UserModel', 'common.User',
        function($log, $timeout, $state, $stateParams, dialogService, IntygProxy, authorityService, ObjectHelper, UserModel, userService) {
            'use strict';

            var _COPY_DIALOG_PREFERENCE = 'wc.dontShowCopyDialog';
            var _FORNYA_DIALOG_PREFERENCE = 'wc.dontShowFornyaDialog';
            var copyDialogModel = {
                isOpen: false,
                dontShowInfo: null,
                otherCareUnit: null,
                patientId: null,
                deepIntegration: null,
                intygTyp: null,
                showerror: null,
                acceptprogressdone: null,
                errormessageid: 'error.failedtocopyintyg'
            };
            var fornyaDialogModel = angular.copy(copyDialogModel);
            fornyaDialogModel.errormessageid = 'error.failedtofornyaintyg';

            function goToDraft(type, intygId) {
                $state.go(type + '-edit', {
                    intygificateId: intygId
                });
            }

            function resetViewStateErrorKeys (viewState) {
                viewState.activeErrorMessageKey = null;
                viewState.inlineErrorMessageKey = null;
            }

            function hideCopyDialogError () {
                copyDialogModel.showerror = null;
            }

            function hideFornyaDialogError () {
                fornyaDialogModel.showerror = null;
            }

            function dialogButton1Click (options) {
                var requestType = options.requestType;
                var requestData = options.requestData;
                var requestFn = options.requestFn;
                var viewState = options.viewState;
                var closeDialog = options.closeDialog;
                var dialogModel = options.dialogModel;
                var dialogPreferenceKey = options.dialogPreferenceKey;

                $log.debug(requestType + ' intyg from dialog' + requestData);

                // Can't check directly on dialogModel.dontShowInfo, it may have false as its value...
                if (dialogPreferenceKey && (typeof dialogModel.dontShowInfo !== undefined) && dialogModel.dontShowInfo !== null) {
                    userService.storeAnvandarPreference(dialogPreferenceKey, dialogModel.dontShowInfo);
                }


                dialogModel.showerror = false;
                dialogModel.acceptprogressdone = false;
                requestFn(requestData, function(draftResponse) {
                    dialogModel.acceptprogressdone = true;
                    if(viewState && viewState.inlineErrorMessageKey) {
                        viewState.inlineErrorMessageKey = null;
                    }

                    var end = function() {
                        goToDraft(draftResponse.intygsTyp, draftResponse.intygsUtkastId);
                    };

                    closeDialog({direct:end});

                }, function(errorCode) {
                    if (errorCode === 'DATA_NOT_FOUND') {
                        dialogModel.errormessageid = 'error.failedto' + requestType + 'intyg.personidnotfound';
                    }
                    else {
                        dialogModel.errormessageid = 'error.failedto' + requestType + 'intyg';
                    }
                    dialogModel.acceptprogressdone = true;
                    dialogModel.showerror = true;
                });
            }

            function _copy(viewState, intygCopyRequest, isOtherCareUnit) {
                var copyDialog;
                // Create preference and model representative
                copyDialogModel.dontShowInfo = false;

                if (UserModel.getAnvandarPreference(_COPY_DIALOG_PREFERENCE) === undefined) {
                    UserModel.setAnvandarPreference(_COPY_DIALOG_PREFERENCE, copyDialogModel.dontShowInfo);
                }


                if (UserModel.getAnvandarPreference(_COPY_DIALOG_PREFERENCE) === true || UserModel.getAnvandarPreference(_COPY_DIALOG_PREFERENCE) === 'true') {
                    $log.debug('copy intyg without dialog' + intygCopyRequest);
                    resetViewStateErrorKeys(viewState);
                    _createCopyDraft(intygCopyRequest, function(draftResponse) {
                        goToDraft(draftResponse.intygsTyp, draftResponse.intygsUtkastId);
                    }, function(errorCode) {
                        if (errorCode === 'DATA_NOT_FOUND') {
                            viewState.inlineErrorMessageKey = 'error.failedtocopyintyg.personidnotfound';
                        }
                        else {
                            viewState.inlineErrorMessageKey = 'error.failedtocopyintyg';
                        }
                    });
                } else {

                    copyDialogModel.otherCareUnit = isOtherCareUnit;
                    copyDialogModel.patientId = $stateParams.patientId;
                    copyDialogModel.deepIntegration = !authorityService.isAuthorityActive({authority: 'HANTERA_PERSONUPPGIFTER'});
                    copyDialogModel.intygTyp = intygCopyRequest.intygType;

                    copyDialog = dialogService.showDialog({
                        dialogId: 'copy-dialog',
                        titleId: 'label.copyintyg',
                        templateUrl: '/app/partials/copy-dialog.html',
                        model: copyDialogModel,
                        button1click: function () {
                            dialogButton1Click({
                                requestType: 'copy',
                                requestData: intygCopyRequest,
                                requestFn: _createCopyDraft,
                                viewState: viewState,
                                dialogModel: copyDialogModel,
                                dialogPreferenceKey: _COPY_DIALOG_PREFERENCE,
                                closeDialog: function (result) {
                                    copyDialog.close(result);
                                }
                            });
                        },
                        button1text: 'common.copy',
                        button2text: 'common.cancel',
                        autoClose: false
                    });

                    copyDialog.opened.then(function() {
                        copyDialogModel.isOpen = true;
                    }, function() {
                        copyDialogModel.isOpen = false;
                    });

                    copyDialog.result.then(
                        hideCopyDialogError,
                        hideCopyDialogError
                    );

                    return copyDialog;
                }

                return null;
            }


            function _fornya(viewState, intygFornyaRequest, isOtherCareUnit) {
                var fornyaDialog;
                // Create preference and model representative
                fornyaDialogModel.dontShowInfo = false;

                if (UserModel.getAnvandarPreference(_FORNYA_DIALOG_PREFERENCE) === undefined) {
                    UserModel.setAnvandarPreference(_FORNYA_DIALOG_PREFERENCE, fornyaDialogModel.dontShowInfo);
                }

                if (UserModel.getAnvandarPreference(_FORNYA_DIALOG_PREFERENCE) === true || UserModel.getAnvandarPreference(_FORNYA_DIALOG_PREFERENCE) === 'true') {
                    $log.debug('copy intyg without dialog' + intygFornyaRequest);
                    resetViewStateErrorKeys(viewState);
                    _createFornyaDraft(intygFornyaRequest, function(draftResponse) {
                        goToDraft(draftResponse.intygsTyp, draftResponse.intygsUtkastId);
                    }, function(errorCode) {
                        if (errorCode === 'DATA_NOT_FOUND') {
                            viewState.inlineErrorMessageKey = 'error.failedtofornyaintyg.personidnotfound';
                        }
                        else {
                            viewState.inlineErrorMessageKey = 'error.failedtofornyaintyg';
                        }
                    });
                } else {

                    fornyaDialogModel.otherCareUnit = isOtherCareUnit;
                    fornyaDialogModel.patientId = $stateParams.patientId;
                    fornyaDialogModel.deepIntegration = !authorityService.isAuthorityActive({authority: 'HANTERA_PERSONUPPGIFTER'});
                    fornyaDialogModel.intygTyp = intygFornyaRequest.intygType;

                    fornyaDialog = dialogService.showDialog({
                        dialogId: 'fornya-dialog',
                        titleId: 'label.fornyaintyg',
                        templateUrl: '/app/partials/fornya-dialog.html',
                        model: fornyaDialogModel,
                        button1click: function () {
                            dialogButton1Click({
                                requestType: 'fornya',
                                requestData: intygFornyaRequest,
                                requestFn: _createFornyaDraft,
                                viewState: viewState,
                                dialogModel: fornyaDialogModel,
                                dialogPreferenceKey: _FORNYA_DIALOG_PREFERENCE,
                                closeDialog: function (result) {
                                    fornyaDialog.close(result);
                                }
                            });
                        },
                        button1text: 'common.fornya',
                        button2text: 'common.cancel',
                        autoClose: false
                    });

                    fornyaDialog.opened.then(function() {
                        fornyaDialogModel.isOpen = true;
                    }, function() {
                        fornyaDialogModel.isOpen = false;
                    });

                    fornyaDialog.result.then(
                        hideFornyaDialogError,
                        hideFornyaDialogError
                    );

                    return fornyaDialog;
                }

                return null;
            }

            function _createFornyaDraft(intygFornyaRequest, onSuccess, onError) {
                IntygProxy.fornyaIntyg(intygFornyaRequest, function(data) {
                    $log.debug('Successfully requested fornyad draft');
                    if(onSuccess) {
                        onSuccess(data);
                    }
                }, function(error) {
                    $log.debug('Create fornyad draft failed: ' + error.message);
                    if (onError) {
                        onError(error.errorCode);
                    }
                });
            }

            function _createCopyDraft(intygCopyRequest, onSuccess, onError) {
                IntygProxy.copyIntyg(intygCopyRequest, function(data) {
                    $log.debug('Successfully requested copy draft');
                    if(onSuccess) {
                        onSuccess(data);
                    }
                }, function(error) {
                    $log.debug('Create copy failed: ' + error.message);
                    if (onError) {
                        onError(error.errorCode);
                    }
                });
            }

            // Send dialog setup
            var sendDialog = {
                isOpen: false
            };

            function _sendSigneratIntyg(intygsId, intygsTyp, recipientId, dialogModel, sendDialog, onSuccess) {
                dialogModel.showerror = false;
                dialogModel.acceptprogressdone = false;
                IntygProxy.sendIntyg(intygsId, intygsTyp, recipientId, function(status) {
                    dialogModel.acceptprogressdone = true;
                    sendDialog.close();
                    onSuccess(status);
                }, function(error) {
                    $log.debug('Send intyg failed: ' + error);
                    dialogModel.acceptprogressdone = true;
                    dialogModel.showerror = true;
                });
            }

            function _send(intygId, intygType, recipientId, titleId, bodyTextId, onSuccess) {

                var dialogSendModel ={
                    acceptprogressdone: true,
                    focus: false,
                    errormessageid: 'error.failedtosendintyg',
                    showerror: false
                };

                sendDialog = dialogService.showDialog({
                    dialogId: 'send-dialog',
                    titleId: titleId,
                    bodyTextId: bodyTextId,
                    templateUrl: '/web/webjars/common/webintyg/intyg/intyg.send.dialog.html',
                    model: dialogSendModel,
                    button1click: function() {
                        $log.debug('send intyg from dialog. id:' + intygId + ', intygType:' + intygType + ', recipientId:' + recipientId);
                        _sendSigneratIntyg(intygId, intygType, recipientId, dialogSendModel,
                            sendDialog, onSuccess);
                    },
                    button1text: 'common.send',
                    button1id: 'button1send-dialog',
                    button2text: 'common.cancel',
                    autoClose: false
                });

                sendDialog.opened.then(function() {
                    sendDialog.isOpen = true;
                }, function() {
                    sendDialog.isOpen = false;
                });

                return sendDialog;
            }

            // Makulera dialog setup
            var makuleraDialog = {
                isOpen: false
            };

            function _revokeSigneratIntyg(intyg, dialogModel, makuleraDialog, onSuccess) {
                dialogModel.showerror = false;
                dialogModel.acceptprogressdone = false;
                IntygProxy.makuleraIntyg(intyg.id, intyg.intygType, function() {
                    dialogModel.acceptprogressdone = true;
                    makuleraDialog.close();
                    onSuccess();
                }, function(error) {
                    $log.debug('Revoke failed: ' + error);
                    dialogModel.acceptprogressdone = true;
                    dialogModel.showerror = true;
                });
            }

            function _revokeAndReplaceSigneratIntyg(intyg, dialogModel, makuleraDialog, onSuccess) {
                dialogModel.showerror = false;
                dialogModel.acceptprogressdone = false;
                IntygProxy.makuleraOchErsattIntyg(intyg.id, intyg.intygType, function() {
                    dialogModel.acceptprogressdone = true;
                    makuleraDialog.close();
                    onSuccess();
                }, function(error) {
                    $log.debug('Revoke failed: ' + error);
                    dialogModel.acceptprogressdone = true;
                    dialogModel.showerror = true;
                });
            }

            function _makulera( intyg, confirmationMessage, onSuccess) {

                function isMakuleraEnabled(model) {
                    return model.acceptprogressdone &&
                        (
                            ObjectHelper.isDefined(model.makuleraModel.reason) ||
                            (model.makuleraModel.reason === 'OVRIGT' &&
                            ObjectHelper.isDefined(model.makuleraModel.clarification))
                        );
                };

                var dialogMakuleraModel = {
                    isMakuleraEnabled: isMakuleraEnabled,
                    acceptprogressdone: true,
                    focus: false,
                    errormessageid: 'error.failedtomakuleraintyg',
                    showerror: false,
                    choices: [
                        {
                            label: 'Intyget har fyllts i felaktigt',
                            value: 'FELAKTIGT_INTYG',
                            placeholder: 'Förtydliga vid behov...'
                        },
                        {
                            label: 'Patienten har kommit med ny information som behöver tillföras',
                            value: 'PATIENT_NY_INFO',
                            placeholder: 'Förtydliga vid behov...'
                        },
                        {
                            label: 'Min bedömning i intyget har ändrats',
                            value: 'MIN_BEDOMNING_ANDRAD',
                            placeholder: 'Förtydliga vid behov...'
                        },
                        {
                            label: 'Övrigt',
                            value: 'OVRIGT',
                            placeholder: 'Ange orsak (obligatoriskt)...'
                        }
                    ],
                    makuleraModel: {
                        reason: undefined,
                        clarification: ''
                    }
                };

                makuleraDialog = dialogService.showDialog({
                    dialogId: 'makulera-dialog',
                    titleId: 'label.makulera',
                    templateUrl: '/app/partials/makulera-dialog.html',
                    model: dialogMakuleraModel,
                    button1click: function() {
                        $log.debug('revoking intyg from dialog' + intyg);
                        _revokeSigneratIntyg(intyg, dialogMakuleraModel, makuleraDialog, onSuccess);
                    },
                    button2click: function() {
                        $log.debug('revoking and replacing intyg from dialog' + intyg);
                        _revokeAndReplaceSigneratIntyg(intyg, dialogMakuleraModel, makuleraDialog, onSuccess);
                    },

                    button1text: 'common.revoke',
                    button1id: 'button1makulera-dialog',
                    button2text: 'common.revokeandreplace',
                    button2id: 'button2makulera-dialog',
                    button3text: 'common.canceldontrevoke',
                    button3id: 'button3makulera-dialog',
                    bodyTextId: 'label.makulera.body',
                    autoClose: false
                });

                return makuleraDialog;
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

             // Return public API for the service
            return {
                makulera: _makulera,
                send: _send,
                COPY_DIALOG_PREFERENCE: _COPY_DIALOG_PREFERENCE,
                FORNYA_DIALOG_PREFERENCE: _FORNYA_DIALOG_PREFERENCE,
                copy: _copy,
                fornya: _fornya,
                isRevoked: _isRevoked,
                isSentToTarget: _isSentToTarget,

                __test__: {
                    createCopyDraft: _createCopyDraft
                }
            };
        }]);
