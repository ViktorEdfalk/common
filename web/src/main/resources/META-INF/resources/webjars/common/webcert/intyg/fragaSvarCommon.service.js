/**
 * Common Fragasvar Module - Common services and controllers related to
 * FragaSvar functionality to be used in webcert and modules handling Fraga/svar
 * related to certificates. (As of this time, only fk7263 module)
 */
angular.module('common').factory('common.fragaSvarCommonService',
    ['$http', '$log', '$modal', '$window', 'common.dialogService', 'common.LocationUtilsService', 'common.featureService', 'common.UserModel',
        function($http, $log, $modal, $window, dialogService, LocationUtilsService, featureService, UserModel) {
            'use strict';

            /*
             * Toggle vidarebefordrad state of a fragasvar entity with given id
             */
            function _setVidareBefordradState(fragaSvarId, intygsTyp, isVidareBefordrad, callback) {
                $log.debug('_setVidareBefordradState');
                var restPath = '/moduleapi/fragasvar/' + intygsTyp + '/' + fragaSvarId + '/hanterad';
                $http.put(restPath, isVidareBefordrad.toString()).success(function(data) {
                    $log.debug('_setVidareBefordradState data:' + data);
                    callback(data);
                }).error(function(data, status) {
                    $log.error('error ' + status);
                    // Let calling code handle the error of no data response
                    callback(null);
                });
            }

            function _buildMailToLink(qa) {
                var baseURL = $window.location.protocol + '//' + $window.location.hostname +
                    ($window.location.port ? ':' + $window.location.port : '');
                var url = baseURL + '/webcert/web/user/certificate/' + qa.intygsReferens.intygsId + '/questions';

                var recipient = '';
                var subject = 'En fraga-svar ska besvaras i Webcert';
                if (qa.vardperson.enhetsnamn !== undefined) {
                    subject += ' pa enhet ' + qa.vardperson.enhetsnamn;
                    if (qa.vardperson.vardgivarnamn !== undefined) {
                        subject += ' for vardgivare ' + qa.vardperson.vardgivarnamn;
                    }
                }

                var body = 'Klicka pa lanktexten for att besvara fraga-svar:\n' + url;
                var link = 'mailto:' + recipient + '?subject=' + encodeURIComponent(subject) + '&body=' +
                    encodeURIComponent(body);
                $log.debug(link);
                return link;
            }

            function _setSkipVidareBefodradCookie() {
                var secsDays = 12 * 30 * 24 * 3600 * 1000; // 1 year
                var now = new Date();
                var expires = new Date(now.getTime() + secsDays);
                document.cookie = 'WCDontAskForVidareBefordradToggle=1; expires=' + expires.toUTCString();

            }

            function _isSkipVidareBefodradCookieSet() {
                return (document.cookie && document.cookie.indexOf('WCDontAskForVidareBefordradToggle=1') !== -1);
            }

            function _decorateSingleItem(qa) {
                if (qa.amne === 'PAMINNELSE') {
                    // RE-020 Påminnelser is never
                    // answerable
                    qa.answerDisabled = true;
                    qa.answerDisabledReason = undefined; // Påminnelser kan inte besvaras men det behöver vi inte säga
                } else if (qa.amne === 'KOMPLETTERING_AV_LAKARINTYG' && !UserModel.user.lakare) {
                    // RE-005, RE-006
                    qa.answerDisabled = true;
                    qa.answerDisabledReason = 'Kompletteringar kan endast besvaras av läkare.';
                } else {
                    qa.answerDisabled = false;
                    qa.answerDisabledReason = undefined;
                }
                _decorateSingleItemMeasure(qa);
            }

            function _decorateSingleItemMeasure(qa) {
                if (qa.status === 'CLOSED') {
                    qa.measureResKey = 'handled';
                } else if (_isUnhandledForDecoration(qa)) {
                    qa.measureResKey = 'markhandled';
                } else if (qa.amne === 'KOMPLETTERING_AV_LAKARINTYG') {
                    qa.measureResKey = 'komplettering';
                } else {
                    if (qa.status === 'PENDING_INTERNAL_ACTION') {
                        qa.measureResKey = 'svarfranvarden';
                    } else if (qa.status === 'PENDING_EXTERNAL_ACTION') {
                        qa.measureResKey = 'svarfranfk';
                    } else {
                        qa.measureResKey = '';
                        $log.debug('warning: undefined status');
                    }
                }
            }

            function _isUnhandledForDecoration(qa){
                if(!qa){
                    return false;
                }
                return qa.status === 'ANSWERED' || qa.amne === 'MAKULERING' || qa.amne === 'PAMINNELSE';
            }

            function _isUnhandled(qa){
                if( (qa.status === 'PENDING_INTERNAL_ACTION' && qa.amne === 'PAMINNELSE') || qa.status === 'ANSWERED') {
                    return true;
                } else {
                    return false;
                }
            }

            function _getUnhandledQas(qas){
                if(!qas || qas.length === 0){
                    return false;
                }
                var qasfiltered = [];
                for (var i = 0, len = qas.length; i < len; i++) {
                    var qa = qas[i];
                    var isUnhandled = _isUnhandled(qa);
                    var fromFk = _fromFk(qa);
                    if(qa.status === 'ANSWERED' || (isUnhandled && fromFk) ){
                        qasfiltered.push(qa);
                    }
                }
                return qasfiltered;
            }

            function _fromFk(qa){
                if(qa.frageStallare === 'FK'){
                    return true;
                }
                return false;
            }

            function _showVidarebefordradPreferenceDialog(title, bodyText, yesCallback, noCallback, noDontAskCallback,
                callback) {

                var DialogInstanceCtrl = function($scope, $modalInstance, title, bodyText, yesCallback, noCallback,
                    noDontAskCallback) {
                    $scope.title = title;
                    $scope.bodyText = bodyText;
                    $scope.noDontAskVisible = noDontAskCallback !== undefined;
                    $scope.yes = function(result) {
                        yesCallback();
                        $modalInstance.close(result);
                    };
                    $scope.no = function() {
                        noCallback();
                        $modalInstance.close('cancel');
                    };
                    $scope.noDontAsk = function() {
                        noDontAskCallback();
                        $modalInstance.close('cancel_dont_ask_again');
                    };
                };

                $window.dialogDoneLoading = false;

                var msgbox = $modal.open({
                    templateUrl: '/app/partials/preference-dialog.html',
                    controller: DialogInstanceCtrl,
                    resolve: {
                        title: function() {
                            return angular.copy(title);
                        },
                        bodyText: function() {
                            return angular.copy(bodyText);
                        },
                        yesCallback: function() {
                            return yesCallback;
                        },
                        noCallback: function() {
                            return noCallback;
                        },
                        noDontAskCallback: function() {
                            return noDontAskCallback;
                        }
                    }
                });

                msgbox.result.then(function(result) {
                    if (callback) {
                        callback(result);
                    }
                }, function() {
                });

                dialogService.runOnDialogDoneLoading(msgbox, function() {
                    $window.dialogDoneLoading = true;
                });
            }

            function _handleVidareBefodradToggle(qa, onYesCallback) {
                // Only ask about toggle if not already set AND not skipFlag cookie is
                // set
                if (!qa.vidarebefordrad && !_isSkipVidareBefodradCookieSet()) {
                    _showVidarebefordradPreferenceDialog(
                        'markforward',
                        'Det verkar som att du har informerat den som ska hantera ärendet. Vill du markera ärendet som vidarebefordrat?',
                        function() { // yes
                            $log.debug('yes');
                            qa.vidarebefordrad = true;
                            if (onYesCallback) {
                                // let calling scope handle yes answer
                                onYesCallback(qa);
                            }
                        },
                        function() { // no
                            $log.debug('no');
                            // Do nothing
                        },
                        function() {
                            $log.debug('no and dont ask');
                            // How can user reset this?
                            _setSkipVidareBefodradCookie();
                        }
                    );
                }
            }

            var QAdialog = null;
            var QAdialogConfirmed = false;
            function _checkQAonlyDialog($scope, $event, newUrl, currentUrl, unbindEvent) {
                // Check if the user used the special qa-link to get here.
                if (featureService.isFeatureActive('franJournalsystemQAOnly') &&
                    !QAdialog &&
                    !QAdialogConfirmed &&
                    newUrl.indexOf('#/fragasvar/') === -1 &&
                    newUrl.indexOf('#/unhandled-qa') === -1 &&
                    newUrl.indexOf('#/webcert/about') === -1 &&
                    newUrl.indexOf('#/support/about') === -1 &&
                    newUrl.indexOf('#/certificates/about') === -1 &&
                    newUrl.indexOf('#/faq/about') === -1 &&
                    newUrl.indexOf('#/cookies/about') === -1) {

                    $event.preventDefault();

                    QAdialog = dialogService.showDialog({
                        dialogId: 'qa-only-warning-dialog',
                        titleId: 'label.qaonlywarning',
                        bodyTextId: 'label.qaonlywarning.body',
                        templateUrl: '/app/partials/qa-only-warning-dialog.html',
                        button1click: function() {
                            QAdialogConfirmed = true;
                            // unbind the location change listener
                            unbindEvent();
                            LocationUtilsService.changeUrl(currentUrl, newUrl);
                        },
                        button1text: 'common.continue',
                        button1id: 'button1continue-dialog',
                        button2text: 'common.cancel',
                        autoClose: true
                    }).result.then(function() {
                        QAdialog = null; // Dialog closed
                    }, function() {
                        QAdialog = null; // Dialog dismissed
                    });

                }
                else {
                    // unbind the location change listener
                    unbindEvent();
                    if ($event.defaultPrevented) {
                        LocationUtilsService.changeUrl(currentUrl, newUrl);
                    }
                }
            }

            // Return public API for the service
            return {
                setVidareBefordradState: _setVidareBefordradState,
                handleVidareBefodradToggle: _handleVidareBefodradToggle,
                buildMailToLink: _buildMailToLink,
                decorateSingleItemMeasure: _decorateSingleItemMeasure,
                decorateSingleItem: _decorateSingleItem,
                isUnhandled: _isUnhandled,
                fromFk : _fromFk,
                checkQAonlyDialog: _checkQAonlyDialog,
                getUnhandledQas : _getUnhandledQas
            };
        }]);
