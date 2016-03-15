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

/**
 * Created by BESA on 2015-03-05.
 */

/**
 * qaPanel directive. Common directive for both unhandled and handled questions/answers
 */
angular.module('fk7263').directive('qaPanel',
    [ '$window', '$log', '$timeout', 'common.User', 'common.fragaSvarCommonService', 'fk7263.fragaSvarProxy',
        'common.statService', 'common.dialogService',
        function($window, $log, $timeout, User, fragaSvarCommonService, fragaSvarProxy, statService, dialogService) {
            'use strict';

            return {
                restrict: 'A',
                transclude: true,
                replace: true,
                templateUrl: '/web/webjars/fk7263/webcert/views/intyg/fragasvar/fragasvarPanel.directive.html',
                scope: {
                    panelId: '@',
                    qa: '=',
                    qaList: '=',
                    cert: '=',
                    certProperties: '='
                },
                controller: function($scope, $element, $attrs) {

                    $scope.cannotKomplettera = false;

//                    $scope.handledPanel = $attrs.type === 'handled';
                    $scope.handledFunction = function(newState) {
                        if (arguments.length) {
                            if (newState) {
                                $scope.updateAsHandled($scope.qa);
                            }
                            else {
                                $scope.updateAsUnHandled($scope.qa);
                            }
                        }
                        else {
                            return $scope.qa.status === 'CLOSED';
                        }
                    };
/*
                    function delayFindMessageAndAct(timeout, qaList, message, onFound) {
                        $timeout(function() {
                            var i;
                            for(i = 0; i < qaList.length; i++){
                                if(qaList[i].internReferens === message.id && qaList[i].proxyMessage !== undefined) {
                                    onFound(i);
                                    break;
                                }
                            }
                        }, timeout);

                        $log.debug('Message not found:' + message.id);
                    }

/*                    function addListMessage(qaList, qa, messageId) {
                        var messageProxy = {};
                        messageProxy.proxyMessage = messageId;
                        messageProxy.id = qa.internReferens;
                        messageProxy.internReferens = qa.internReferens;
                        messageProxy.senasteHandelseDatum = qa.senasteHandelseDatum;
                        messageProxy.messageStatus = qa.status;
                        qaList.push(messageProxy);

                        delayFindMessageAndAct(5000, qaList, messageProxy, function(index) {
                            qaList[index].messageStatus = 'HIDDEN';
                            delayFindMessageAndAct(2000, qaList, messageProxy, function(index) {
                                qaList.splice(index, 1);
                            });
                        });
                    }*/

                    $scope.sendAnswer = function sendAnswer(qa) {
                        qa.updateInProgress = true; // trigger local spinner

                        fragaSvarProxy.saveAnswer(qa, 'fk7263', function(result) {
                            $log.debug('Got saveAnswer result:' + result);
                            qa.updateInProgress = false;
                            qa.activeErrorMessageKey = null;
                            if (result !== null) {
                                fragaSvarCommonService.decorateSingleItem(result);
                                //addListMessage($scope.qaList, qa, 'fk7263.fragasvar.answer.is.sent');

                                // update real item
                                angular.copy(result, qa);
                                statService.refreshStat();
                            }
                        }, function(errorData) {
                            // show error view
                            qa.updateInProgress = false;
                            qa.activeErrorMessageKey = errorData.errorCode;
                        });
                    };

                    $scope.answerWithIntyg = function(qa, cert) {
                        qa.updateInProgress = true; // trigger local spinner
                        fragaSvarProxy.answerWithIntyg(qa, 'fk7263', function(result) {
                            qa.updateInProgress = false;
                            qa.activeErrorMessageKey = null;
                            statService.refreshStat();



                        }, function(errorData) {
                            // show error view
                            qa.updateInProgress = false;
                            qa.activeErrorMessageKey = errorData.errorCode;
                        });
                    };

                    $scope.onVidareBefordradChange = function(qa) {
                        qa.forwardInProgress = true;

                        fragaSvarCommonService.setVidareBefordradState(qa.internReferens, 'fk7263', qa.vidarebefordrad,
                            function(result) {
                                qa.forwardInProgress = false;

                                if (result !== null) {
                                    qa.vidarebefordrad = result.vidarebefordrad;
                                } else {
                                    qa.vidarebefordrad = !qa.vidarebefordrad;
                                    dialogService.showErrorMessageDialog('Kunde inte markera/avmarkera frågan som vidarebefordrad. ' +
                                        'Försök gärna igen för att se om felet är tillfälligt. Annars kan du kontakta supporten');
                                }
                            });
                    };

                    $scope.updateAnsweredAsHandled = function(deferred, unhandledQas){
                        if(unhandledQas === undefined || unhandledQas.length === 0 ){
                            return;
                        }
                        fragaSvarProxy.closeAllAsHandled(unhandledQas,
                            function(qas){
                                if(qas) {
                                    angular.forEach(qas, function(qa) { //unused parameter , key
                                        fragaSvarCommonService.decorateSingleItem(qa);
                                        //addListMessage(qas, qa, 'fk7263.fragasvar.marked.as.hanterad'); // TODOOOOOOOO TEST !!!!!!!!!!
                                    });
                                    statService.refreshStat();
                                }
                                $window.doneLoading = true;
                                if(deferred) {
                                    deferred.resolve();
                                }
                            },function() { // unused parameter: errorData
                                // show error view
                                $window.doneLoading = true;
                                if(deferred) {
                                    deferred.resolve();
                                }
                            });
                    };

                    $scope.hasUnhandledQas = function(){
                        if(!$scope.qaList || $scope.qaList.length === 0){
                            return false;
                        }
                        for (var i = 0, len = $scope.qaList.length; i < len; i++) {
                            var qa = $scope.qaList[i];
                            var isUnhandled = fragaSvarCommonService.isUnhandled(qa);
                            var fromFk = fragaSvarCommonService.fromFk(qa);
                            if(qa.status === 'ANSWERED' || (isUnhandled && fromFk)){
                                return true;
                            }
                        }
                        return false;
                    };

                    $scope.updateAsHandled = function(qa, deferred) {
                        $log.debug('updateAsHandled:' + qa);
                        qa.updateHandledStateInProgress = true;

                        fragaSvarProxy.closeAsHandled(qa.internReferens, 'fk7263', function(result) {
                            qa.activeErrorMessageKey = null;
                            qa.updateHandledStateInProgress = false;
                            if (result !== null) {
                                fragaSvarCommonService.decorateSingleItem(result);
                                //addListMessage($scope.qaList, qa, 'fk7263.fragasvar.marked.as.hanterad');

                                angular.copy(result, qa);
                                statService.refreshStat();
                            }
                            $window.doneLoading = true;
                            if(deferred) {
                                deferred.resolve();
                            }
                        }, function(errorData) {
                            // show error view
                            qa.updateHandledStateInProgress = false;
                            qa.activeErrorMessageKey = errorData.errorCode;
                            $window.doneLoading = true;
                            if(deferred) {
                                deferred.resolve();
                            }
                        });
                    };

                    $scope.updateAsUnHandled = function(qa) {
                        $log.debug('updateAsUnHandled:' + qa);
                        qa.updateHandledStateInProgress = true; // trigger local

                        fragaSvarProxy.openAsUnhandled(qa.internReferens, 'fk7263', function(result) {
                            $log.debug('Got openAsUnhandled result:' + result);
                            qa.activeErrorMessageKey = null;
                            qa.updateHandledStateInProgress = false;

                            if (result !== null) {
                                fragaSvarCommonService.decorateSingleItem(result);
                                //addListMessage($scope.qaList, qa, 'fk7263.fragasvar.marked.as.ohanterad');

                                angular.copy(result, qa);
                                statService.refreshStat();
                            }
                        }, function(errorData) {
                            // show error view
                            qa.updateHandledStateInProgress = false;
                            qa.activeErrorMessageKey = errorData.errorCode;
                        });
                    };

                    // Handle vidarebefordra dialog
                    $scope.openMailDialog = function(qa) {
                        // use timeout so that external mail client has a chance to start before showing dialog
                        $timeout(function() {
                            fragaSvarCommonService.handleVidareBefodradToggle(qa, $scope.onVidareBefordradChange);
                        }, 1000);
                        // Launch mail client
                        $window.location = fragaSvarCommonService.buildMailToLink(qa);
                    };

                    $scope.dismissProxy = function(qa) {
                        if (qa === undefined) {
                            $scope.widgetState.sentMessage = false;
                            return;
                        }
                        for (var i = 0; i < $scope.qaList.length; i++) {
                            if (qa.proxyMessage !== undefined && $scope.qaList[i].proxyMessage !== undefined &&
                                qa.internReferens === $scope.qaList[i].internReferens)
                            {
                                $scope.qaList.splice(i, 1);
                                return;
                            }
                        }
                    };

                    // listeners - interscope communication
                    var unbindmarkAnsweredAsHandledEvent = $scope.$on('markAnsweredAsHandledEvent', function($event, deferred, unhandledQas) {
                        $scope.updateAnsweredAsHandled(deferred, unhandledQas);
                    });
                    $scope.$on('$destroy', unbindmarkAnsweredAsHandledEvent);

                }
            };
        }]);
