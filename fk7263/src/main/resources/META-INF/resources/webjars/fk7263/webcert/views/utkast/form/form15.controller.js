/*
 * Copyright (C) 2015 Inera AB (http://www.inera.se)
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

angular.module('fk7263').controller('fk7263.EditCert.Form15Ctrl',
    ['$scope', '$log', 'fk7263.EditCertCtrl.ViewStateService',
        function($scope, $log, viewState) {
            'use strict';
            $scope.model = viewState.intygModel;
            $scope.viewState = viewState;

            $scope.$watch('viewState.common.doneLoading', function ( newVal, oldVal ) {
                if(newVal && (newVal === oldVal) ){
                    return;
                }

                var doesntHaveVardenhet = !$scope.model.grundData || !$scope.model.grundData.skapadAv ||
                    !$scope.model.grundData.skapadAv.vardenhet;

                    // check if all info is available from HSA. If not, display the info message that someone needs to update it
                $scope.viewState.common.hsaInfoMissing =
                    doesntHaveVardenhet || $scope.model.grundData.skapadAv.vardenhet.isMissingInfo();

            });
        }]);
