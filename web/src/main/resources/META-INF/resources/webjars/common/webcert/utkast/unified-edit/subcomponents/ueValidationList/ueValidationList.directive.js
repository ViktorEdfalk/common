/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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
angular.module('common').directive('ueValidationList',
    [
        function() {
            'use strict';

            return {
                restrict: 'E',
                templateUrl: '/web/webjars/common/webcert/utkast/unified-edit/subcomponents/ueValidationList/ueValidationList.directive.html',
                scope: {
                    validations: '=',
                    warnings: '=',
                    fieldType: '@'
                },
                link: function(scope) {

                    // This is used to create keys for lookup
                    scope.getSection = function(message) {
                        var section = message.field.toLowerCase();
                        var i = message.field.indexOf('.');
                        if (i >= 0) {
                            section = message.field.substring(0, i).toLowerCase();
                        }
                        return section;
                    };
                }
            };
        }]);
