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
angular.module('common').directive('wcFmbPanelTab', [ 'common.anchorScrollService', 'common.fmbService', 'common.fmbViewState',
    function(anchorScrollService, fmbService, fmbViewState) {
    'use strict';

    return {
        restrict: 'E',
        scope: {
            config: '='
        },
        templateUrl: '/web/webjars/common/webcert/components/wcSupportPanelManager/wcFmbPanelTab/wcFmbPanelTab.directive.html',
        link: function($scope) {
            $scope.fmb = fmbViewState;

            //Set initial viewmodel state
            $scope.vm = {
                activeDiagnose: null,
                noDataMessage: null,
                referensDescr: null,
                referensLink: null
            };

             /**
             * Determine info message to show
             */
            function getNoDataReasonMessage(diagnoseCount, hasDataCount) {
                //Case 1: No diagnose set at all
                if (diagnoseCount === 0) {
                    return {
                        warning: 'fmb.warn.no-diagnose-set'
                    };
                    //Case 2: 1 diagnose set, but no data for it
                } else if (diagnoseCount === 1 && hasDataCount === 0) {
                    return {
                        info: 'fmb.info.single-diagnose-no-data'
                    };
                    //Case 3: 2+ diagnose set, but no data for any of them
                } else if (diagnoseCount > 1 && hasDataCount === 0) {
                    return {
                        info: 'fmb.info.multiple-diagnose-no-data'
                    };
                } else {
                    return null;
                }

            }

            function _updateSectionDataForDiagnose(activeDiagnose) {
                //Init sections
                $scope.vm.sections = [ {
                    formId: 'ARBETSFORMAGA',
                    heading: 'BESLUTSUNDERLAG_TEXTUELLT',
                    data: null
                }, {
                    formId: 'FUNKTIONSNEDSATTNING',
                    heading: 'FUNKTIONSNEDSATTNING',
                    data: null
                }, {
                    formId: 'AKTIVITETSBEGRANSNING',
                    heading: 'AKTIVITETSBEGRANSNING',
                    data: null
                }, {
                    formId: 'DIAGNOS',
                    heading: 'GENERELL_INFO',
                    data: null
                }, {
                    formId: 'DIAGNOS',
                    heading: 'SYMPTOM_PROGNOS_BEHANDLING',
                    data: null
                } ];

                angular.forEach($scope.vm.sections, function(section) {
                    section.data = activeDiagnose ? activeDiagnose.getFormData(section.formId, section.heading) : null;
                    var reference = activeDiagnose ? activeDiagnose.getReference() : null;
                    if (reference) {
                        $scope.vm.referensDescr = reference.desc;
                        $scope.vm.referensLink = reference.link;
                    }
                });
            }

            function _updateState() {
                var oldActive = $scope.vm.activeDiagnose;
                var oldActiveValid = false;

                $scope.vm.activeDiagnose = null;
                $scope.vm.noDataMessage = null;

                var diagnosesEnteredCount = 0;
                var diagnosesWithDataCount = 0;

                angular.forEach($scope.fmb.diagnoses, function(d) {
                    diagnosesEnteredCount++;
                    if (fmbService.checkDiagnos(d)) {
                        diagnosesWithDataCount++;
                        if (oldActive === d) {
                            oldActiveValid = true;
                        }
                        // Default is to select first diagnose that has data data
                        if (!$scope.vm.activeDiagnose) {
                            $scope.vm.activeDiagnose = d;
                        }
                    }
                });

                //But if the previously selected diagnose still exist and has data - reselect the old one.
                if (oldActiveValid) {
                    $scope.vm.activeDiagnose = oldActive;
                }

                //Update data to display
                _updateSectionDataForDiagnose($scope.vm.activeDiagnose);

                //Update message to display
                $scope.vm.noDataMessage = getNoDataReasonMessage(diagnosesEnteredCount, diagnosesWithDataCount);
            }
            //Diagnoses were added/removed/changed
            $scope.$watchCollection('fmb.diagnoses', function() {
                _updateState();
            });
            $scope.$watch('vm.activeDiagnose.diagnosKod', function(newVal) {
                _updateState();
                if (newVal) {
                    angular.element('#fmb-panel-scrollable-body').scrollTop(0);
                }
            });
        }
    };
} ]);
