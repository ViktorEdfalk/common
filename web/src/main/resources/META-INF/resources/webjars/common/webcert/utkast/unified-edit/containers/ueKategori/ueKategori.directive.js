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
angular.module('common').directive('ueKategori', ['$parse',
    function($parse) {
        'use strict';

        return {
            restrict: 'E',
            scope: {
                form: '=',
                config: '=',
                model: '='
            },
            templateUrl: '/web/webjars/common/webcert/utkast/unified-edit/containers/ueKategori/ueKategori.directive.html',
            link: function($scope) {
                if ($scope.config.label.required) {
                    $scope.hasUnfilledRequirements = function() {
                        var reqProp = $scope.config.label.requiredProp;
                        if (reqProp) {
                            var req;
                            if (angular.isArray(reqProp)) {
                                for (var i = 0; i < reqProp.length; i++) {
                                    req = $parse(reqProp[i])($scope.model);
                                    if(!$scope.form[reqProp[i]] && (req === null || req === undefined || req === false)) {
                                        continue;
                                    }
                                    return false;
                                }
                                return true;
                            } else {
                                req = $parse(reqProp)($scope.model);
                                if(req === null || req === undefined || req === false) {
                                    return true;
                                }
                            }
                        } else {
                            return true;
                        }
                    };
                }
            }
        };
    }]);
