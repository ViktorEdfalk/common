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

angular.module('fk7263').controller('fk7263.EditCert.Form6a711Ctrl',
    ['$scope', '$log', 'fk7263.EditCertCtrl.ViewStateService',
        function($scope, $log, viewState) {
            'use strict';
            var model = viewState.intygModel;
            $scope.model = model;
            $scope.viewState = viewState;

            $scope.$watch('viewState.avstangningSmittskyddValue', function(newVal, oldVal) {
                if(newVal === oldVal){
                    return;
                }
                // only do this once the page is loaded and changes come from the gui!
                if(viewState.common.doneLoading) {
                    // Remove defaults not applicable when smittskydd is active
                    if (newVal === true) {
                        model.atticUpdateForm6a7();
                        model.clearForm6a7();
                    } else {
                        model.atticRestoreForm6a7();
                    }

                }
            });

            // once we've doneLoading we can set the radion buttons to the model state.
            $scope.$watch('viewState.common.doneLoading', function(newVal) {
                if(newVal) {
                    updateFormFromModel();
                }
            });

            function updateFormFromModel(){
                if(!model.rehabilitering){
                    model.rehabilitering = 'rehabiliteringEjAktuell';
                }

                if(!model.ressattTillArbeteAktuellt && !model.ressattTillArbeteEjAktuellt){
                    model.ressattTillArbeteAktuellt = false;
                    model.ressattTillArbeteEjAktuellt = true;
                }
            }

            $scope.onOvrigtChange = function(){
                if(!model.rekommendationOvrigtCheck){
                    model.updateToAttic(model.properties.form6a);
                    model.rekommendationOvrigt = undefined;
                } else {
                    model.updateToAttic(model.properties.form6a);
                    model.rekommendationOvrigtCheck = true;
                }
            };

        }]);
