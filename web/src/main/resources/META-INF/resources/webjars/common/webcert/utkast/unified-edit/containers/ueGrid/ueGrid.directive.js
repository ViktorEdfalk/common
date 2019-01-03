/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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

angular.module('common').directive('ueGrid', [ 'common.UtkastValidationViewState',
    function(UtkastValidationViewState) {
    'use strict';
    return {
        restrict: 'E',
        scope: {
            form: '=',
            config: '=',
            model: '='
        },
        templateUrl: '/web/webjars/common/webcert/utkast/unified-edit/containers/ueGrid/ueGrid.directive.html',
        link: function($scope) {
            $scope.validation = UtkastValidationViewState;
            $scope.getColSize = function(row, $index) {
                if ($scope.config.colSizes) {
                    return $scope.config.colSizes[$index];
                } else {
                    return 12 / row.length;
                }
            };

            $scope.useRowValidation = !!$scope.config.validationRows;
            $scope.getRowValidationKeys = function(row, $index) {
                return $scope.config.validationRows[$index];
            };

            if ($scope.config.validationContext) {
                $scope.validationKeys = [];
                $scope.validationKeys.push({key: $scope.config.validationContext.key.toLowerCase(), type: $scope.config.validationContext.type});
            }
        }
    };

}]);