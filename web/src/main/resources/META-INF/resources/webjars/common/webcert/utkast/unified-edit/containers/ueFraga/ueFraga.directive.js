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
angular.module('common').directive('ueFraga', ['common.UtkastValidationViewState',
    function(UtkastValidationViewState) {
        'use strict';

        return {
            restrict: 'E',
            scope: {
                form: '=',
                config: '=',
                model: '='
            },
            templateUrl: '/web/webjars/common/webcert/utkast/unified-edit/containers/ueFraga/ueFraga.directive.html',
            link: function($scope) {
                $scope.validation = UtkastValidationViewState;

                //Returns an array with validationKeys for all child components by recursively iterating the config structure..
                //This only needs to be done once, since the config tree itself is static
                function _collectChildValidationKeys(arr, components) {
                    angular.forEach(components, function(c) {
                        //ue-grid has double array..
                        if (angular.isArray(c)) {
                            _collectChildValidationKeys(arr, c);
                        }
                        //Add itself...
                        if (c.modelProp) {
                            arr.push({key: c.modelProp.toLowerCase(), type: c.type });
                        }

                        //.. and any children
                        if (c.components) {
                           return _collectChildValidationKeys(arr, c.components);
                        }
                    });

                    return arr;
                }


                $scope.validationKeys = _collectChildValidationKeys([], $scope.config.components);

                //Also, add the validationKey for the actual fraga (for frage-level EMPTY-type validationmessages)
                if ($scope.config.validationContext) {
                    $scope.validationKeys.push({key: $scope.config.validationContext.key.toLowerCase(), type: $scope.config.validationContext.type});
                }
            }
        };
    }]);