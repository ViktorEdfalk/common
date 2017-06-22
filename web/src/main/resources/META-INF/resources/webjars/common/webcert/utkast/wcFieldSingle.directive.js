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
 * wcFieldSingle directive. Used to abstract common layout for single-line form fields in intyg modules
 */
angular.module('common').directive('wcFieldSingle', ['common.messageService',
    function(messageService) {
        'use strict';

        return {
            restrict: 'A',
            transclude: true,
            replace: true,
            scope: {
                fieldNumber: '@',
                fieldHelpText: '@',
                fieldTooltipPlacement: '@'
            },
            controller: function($scope) {

                if ($scope.fieldNumber === null) {
                    $scope.fieldNumber = undefined;
                }

                if ($scope.fieldTooltipPlacement === undefined) {
                    $scope.placement = 'right';
                } else {
                    $scope.placement = $scope.fieldTooltipPlacement;
                }

                $scope.getMessage = function(key) {
                    return messageService.getProperty(key);
                };
            },
            template: '<div class="intyg-block"><div class="intyg-block-single__content clearfix">' +
                '<h4 class="intyg-field-number" ng-if="fieldNumber != undefined">' +
                '<span message key="modules.label.field"></span> {{fieldNumber}}</h4>' +
                '<span ng-transclude></span>' +
                '<span wc-help-chevron help-text-key="{{fieldHelpText}}"></span>' +
                '<span wc-help-chevron-text help-text-key="{{fieldHelpText}}"></span>' +
                '</div></div>'
        };
    }]);
