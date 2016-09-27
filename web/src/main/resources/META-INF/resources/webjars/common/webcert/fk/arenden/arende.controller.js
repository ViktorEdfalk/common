angular.module('common').controller('common.ArendeCtrl',
    ['$log', '$rootScope', '$state', '$stateParams', '$scope', '$timeout', '$window', '$filter',
        'common.dialogService', 'common.ObjectHelper', 'common.ErrorHelper',
        'common.ArendeProxy', 'common.ArendenViewStateService', 'common.ArendeHelper', 'common.statService',
        function ($log, $rootScope, $state, $stateParams, $scope, $timeout, $window, $filter,
                  dialogService, ObjectHelper, ErrorHelper,
                  ArendeProxy, ArendenViewState, ArendeHelper, statService) {
            'use strict';

            ArendenViewState.reset();
            ArendenViewState.setIntygType($state.current.data.intygType);
            $scope.viewState = ArendenViewState;
            $scope.arendeList = [];

            // Load

            function filterKompletteringar(arendeList, intygProperties) {

                var isAnyKompletteringarNotHandled = false;

                // Filter out the komplettering the utkast was based on and only that one.
                var filteredList = arendeList.filter(function(arendeListItem) {

                    var isKompletteringFraga = arendeListItem.fraga.amne === 'KOMPLETTERING_AV_LAKARINTYG' || arendeListItem.fraga.amne === 'KOMPLT';

                    // Check if this komplettering isn't handled. Used to show sign if there are no more unhandled kompletteringar
                    if(!isAnyKompletteringarNotHandled){
                        isAnyKompletteringarNotHandled = (isKompletteringFraga && arendeListItem.fraga.status !== 'CLOSED');
                    }

                    return isKompletteringFraga;
                });

                // If there aren't any kompletteringar that aren't handled already, we can show the sign that all kompletteringar are handled.
                ArendenViewState.showAllKompletteringarHandled = !isAnyKompletteringarNotHandled;
                return filteredList;
            }

            this.filterKompletteringar = filterKompletteringar;


            function fetchArenden(intygId, intygProperties) {
                ArendeProxy.getArenden(intygId, intygProperties.type, function (result) {
                    $log.debug('getArendeForCertificate:success data:' + result);
                    ArendenViewState.doneLoading = true;
                    ArendenViewState.activeErrorMessageKey = null;
                    ArendenViewState.showAllKompletteringarHandled = false;

                    // If kompletteringsmode, only show kompletteringsissues
                    if(ObjectHelper.isDefined(intygProperties) && intygProperties.kompletteringOnly) {
                        result = filterKompletteringar(result, intygProperties);
                    }

                    $scope.arendeList = ArendeHelper.createListItemsFromArenden(result);

                }, function (errorData) {
                    // show error view
                    ArendenViewState.doneLoading = true;

                    ArendenViewState.activeErrorMessageKey = ErrorHelper.safeGetError(errorData);
                });
            }

            var unbindFastEvent = $rootScope.$on('ViewCertCtrl.load', function (event, intyg, intygProperties) {

                // IMPORTANT!! DON'T LET fetchArenden DEPEND ON THE INTYG LOAD EVENT (intyg) in this case!
                // Messages needs to be loaded separately from the intyg as user should be able to see messages even if intyg didn't load.
                // Used when coming from Intyg page.
                ArendenViewState.intyg = intyg;
                if (ObjectHelper.isDefined(ArendenViewState.intyg) && ObjectHelper.isDefined(ArendenViewState.intygProperties)) {

                    ArendenViewState.intygProperties = intygProperties;
                    ArendenViewState.intygProperties.isLoaded = true;
                    var intygId = $stateParams.certificateId;
                    if (intygProperties.forceUseProvidedIntyg) {
                        // Used for utkast page. In this case we must use the id from intyg because $stateParams.certificateId is the id of the utkast, not the parentIntyg
                        intygId = intyg.id;
                    }
                    fetchArenden(intygId, ArendenViewState.intygProperties);

                } else if (ObjectHelper.isDefined($stateParams.certificateId)) {
                    fetchArenden($stateParams.certificateId, {
                        type:$state.current.data.intygType
                    });
                }
            });
            $scope.$on('$destroy', unbindFastEvent);

            // listeners - interscope communication
            var unbindmarkAnsweredAsHandledEvent = $scope.$on('markAnsweredAsHandledEvent', function ($event, deferred, unhandledQas) {
                _updateAnsweredAsHandled(deferred, unhandledQas);
                deferred.resolve();
            });
            $scope.$on('$destroy', unbindmarkAnsweredAsHandledEvent);

            var unbindHasUnhandledQasEvent = $scope.$on('hasUnhandledQasEvent', function ($event, deferred) {
                deferred.resolve(ArendeHelper.getUnhandledArenden($scope.arendeList));
            });
            $scope.$on('$destroy', unbindHasUnhandledQasEvent);

            function _updateAnsweredAsHandled(deferred, unhandledarendes) {
                if (unhandledarendes === undefined || unhandledarendes.length === 0) {
                    return;
                }
                ArendeProxy.closeAllAsHandled(unhandledarendes, ArendenViewState.common.intygProperties.type,
                    function(arendes) {
                        if (arendes) {
                            angular.forEach(arendes, function(arende) {
                                angular.forEach($scope.arendeList, function(arendeListItem) {
                                    if (arende.fraga.internReferens === arendeListItem.arende.fraga.internReferens) {
                                        angular.copy(arende, arendeListItem.arende);
                                        arendeListItem.updateArendeListItem();
                                    }
                                });
                            });
                            statService.refreshStat();
                        }
                        $window.doneLoading = true;
                        if (deferred) {
                            deferred.resolve();
                        }
                    }, function() { // unused parameter: errorData
                        // show error view
                        $window.doneLoading = true;
                        if (deferred) {
                            deferred.resolve();
                        }
                    }
                );
            };

            // Scope interactions

            $scope.dismissSentMessage = function() {
                $scope.viewState.sentMessage = false;
            };

            $scope.openArendenFilter = function (arendeListItem) {
                return arendeListItem.arende.fraga.status !== 'CLOSED';
            };

            $scope.closedArendenFilter = function (arendeListItem) {
                return arendeListItem.arende.fraga.status === 'CLOSED';
            };
        }]);
