/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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
angular.module('ts-bas').controller('ts-bas.IntygController',
    [ '$log', '$rootScope', '$stateParams', '$scope',
        'common.IntygProxy', 'common.User',
        'ViewState', 'common.dynamicLabelService', 'ViewConfigFactory',
        'supportPanelConfigFactory',
        function($log, $rootScope, $stateParams, $scope, IntygProxy,
            User, ViewState, DynamicLabelService, viewConfigFactory, supportPanelConfigFactory) {
            'use strict';

            /*********************************************************************
             * Page state
             *********************************************************************/

            ViewState.reset();
            $scope.viewState = ViewState;
            $scope.cert = undefined;

            $scope.uvConfig = viewConfigFactory.getViewConfig(true);

            $scope.user = { lakare: User.getUser().lakare };

            // expose calculated static link for pdf download
            $scope.downloadAsPdfLink = '/moduleapi/intyg/ts-bas/' + $stateParams.certificateId + '/pdf';

            // Decide if helptext related to field 1.a) - 1.c)
            $scope.achelptext = false;

            /*********************************************************************
             * Private support functions
             *********************************************************************/

            function createKorkortstypListString(list) {

                var tempList = [];
                angular.forEach(list, function(key) {
                    if (key.selected) {
                        this.push(key);
                    }
                }, tempList);

                var resultString = '';
                for (var i = 0; i < tempList.length; i++) {
                    if (i < tempList.length - 1) {
                        resultString += tempList[i].type + (', ');
                    } else {
                        resultString += tempList[i].type;
                    }
                }

                return resultString;
            }

            function loadIntyg() {
                IntygProxy.getIntyg($stateParams.certificateId, ViewState.common.intygProperties.type, function(result) {
                    ViewState.common.doneLoading = true;
                    if (result !== null && result !== '') {
                        ViewState.intygModel = result.contents;

                        DynamicLabelService.updateDynamicLabels(ViewState.common.intygProperties.type, ViewState.intygModel.textVersion);

                        if (ViewState.intygModel.syn.synfaltsdefekter === true || ViewState.intygModel.syn.nattblindhet === true ||
                            ViewState.intygModel.syn.progressivOgonsjukdom === true) {
                            $scope.achelptext = true;
                        }
                        ViewState.intygAvser = createKorkortstypListString(ViewState.intygModel.intygAvser.korkortstyp);
                        ViewState.bedomning = createKorkortstypListString(ViewState.intygModel.bedomning.korkortstyp);

                        ViewState.relations = result.relations;

                        if(ViewState.intygModel !== undefined && ViewState.intygModel.grundData !== undefined){
                            ViewState.enhetsId = ViewState.intygModel.grundData.skapadAv.vardenhet.enhetsid;
                        }

                        ViewState.common.updateIntygProperties(result, ViewState.intygModel.id);
                        $scope.cert = result.contents;

                        $scope.supportPanelConfig = supportPanelConfigFactory.getConfig($stateParams.certificateId, ViewState.intygModel.textVersion, true);
                        $rootScope.$emit('ViewCertCtrl.load', ViewState.intygModel);
                        $rootScope.$broadcast('intyg.loaded', ViewState.intygModel, ViewState.common.intygProperties);

                    } else {
                        $log.debug('Got error while loading intyg - invalid data');
                        $rootScope.$emit('ViewCertCtrl.load', null, null);
                        $rootScope.$broadcast('intyg.loaded', null);
                        ViewState.common.activeErrorMessageKey = 'common.error.data_not_found';
                    }
                }, function(error) {
                    $rootScope.$emit('ViewCertCtrl.load', null, null);
                    $rootScope.$broadcast('intyg.loaded', null);
                    ViewState.common.doneLoading = true;
                    ViewState.common.updateActiveError(error, $stateParams.signed);
                });
            }

            /*********************************************************************
             * Exposed scope interaction functions
             *********************************************************************/

            /*********************************************************************
             * Page load
             *********************************************************************/
            loadIntyg();

            $scope.$on('loadCertificate', loadIntyg);
        }]);
