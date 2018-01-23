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
angular.module('common').directive('wcUtkastErrorSummary',
    [ 'common.anchorScrollService', 'common.UtkastViewStateService', 'common.UtkastValidationViewState',
        function(anchorScrollService, CommonViewState, utkastValidationViewState) {
            'use strict';

            return {
                restrict: 'E',
                templateUrl: '/web/webjars/common/webcert/utkast/wcUtkastErrorSummary/wcUtkastErrorSummary.directive.html',
                scope: true,
                controller: function($scope) {
                    $scope.scrollTo = function(message) {
                        anchorScrollService.scrollIntygContainerTo('anchor-' + message);
                    };

                    $scope.hideMissing = function(){
                        CommonViewState.setShowComplete(false);
                        utkastValidationViewState.reset();
                    };
                }
            };
        }]);
