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
 * wcField directive. Used to abstract common layout for full-layout form fields in intyg modules
 */
angular.module('common').directive('wcIntygField',
    [ 'common.messageService', 'common.dynamicLabelService', 'common.IntygViewStateService',
        function(messageService, dynamicLabelService, IntygViewStateService) {
            'use strict';

            return {
                restrict: 'A',
                transclude: true,
                replace: true,
                templateUrl: '/web/webjars/common/webcert/intyg/fk/wcIntygField.directive.html',
                scope: {
                    categoryNumber: '@',
                    fieldLabel: '@',
                    fieldDynamicLabel: '@',
                    filled: '@?'
                },
                controller: function($scope) {
                    $scope.viewState = IntygViewStateService;
                    $scope.fieldHasKomplettering = false;

                    if ($scope.filled === undefined) {
                        $scope.filled = 'true';
                    }

                    $scope.$watch('viewState.arende.status', function() {
                        var arende = $scope.viewState.arende;

                        if ($scope.categoryNumber !== undefined && arende !== undefined && arende.amne === 'KOMPLT') {

                            angular.forEach(arende.kompletteringar, function(komplettering) {
                                if ($scope.viewState.fcMap) {
                                    var key = komplettering.jsonPropertyHandle;
                                    var map  = $scope.viewState.fcMap;
                                    var status = arende.status;

                                    if (map.has(key) && map.get(key).toString() === $scope.categoryNumber) {
                                        $scope.fieldHasKomplettering = status === 'PENDING_INTERNAL_ACTION';
                                    }
                                }
                            });
                        }
                    });

                }
            };
        }]);
