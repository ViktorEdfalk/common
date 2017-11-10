/*
 * Copyright (C) 2017 Inera AB (http://www.inera.se)
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
 * Position help buttons with FMB, SRS, etc
 */
angular.module('common').directive('wcHelpWrapper', ['common.ObjectHelper', 'common.fmbService', 'common.fmbViewState', '$rootScope',
    function(ObjectHelper, fmbService, fmbViewState, $rootScope) {
        'use strict';

        return {
            restrict: 'E',
            transclude: true,
            scope: {
                fmbId: '@',
                fmbFieldName: '@',
                srsId: '@',
                pushContent: '@'
            },
            link: function(scope, element, attrs) {

                if(!ObjectHelper.isDefined(scope.pushContent)){
                    scope.pushContent = true;
                } else {
                    scope.pushContent = scope.pushContent === 'true';
                }

                scope.fmbAvailable = true;
                function updateFMBAvailable() {
                    if(ObjectHelper.isDefined(scope.fmbFieldName) && ObjectHelper.isDefined(scope.fmbId)){
                        scope.fmbAvailable = fmbService.isAnyFMBDataAvailable(fmbViewState);
                    } else {
                        scope.fmbAvailable = false;
                    }
                }

                scope.fmbViewState = fmbViewState;
                scope.$watch('fmbViewState', function(newVal, oldVal) {
                    updateFMBAvailable();
                }, true);

                updateFMBAvailable();

                scope.shouldPushContent = function(){
                    return scope.pushContent === true && (scope.fmbAvailable || scope.srsId);
                };
            },
            templateUrl: '/web/webjars/common/webcert/fmb/wcHelpWrapper.directive.html'
        };
    }]);
