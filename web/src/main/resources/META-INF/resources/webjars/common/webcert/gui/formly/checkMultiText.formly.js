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

angular.module('common').run(function(formlyConfig) {
    'use strict';

    formlyConfig.setType({
        name: 'check-multi-text',
        templateUrl: '/web/webjars/common/webcert/gui/formly/checkMultiText.formly.html',
        controller: ['$scope', 'common.ObjectHelper', 'common.AtticHelper', function($scope, ObjectHelper, AtticHelper) {
            if (!$scope.to.labelColSize) {
                $scope.to.labelColSize = 5;
            }
            if (!$scope.to.indent) {
                $scope.to.indent = false;
            }

            $scope.to.open = false;

            function loadModel(params) {
                // Restore data model value form attic if exists
                AtticHelper.restoreFromAttic($scope.model, $scope.options.key);

                if ($scope.model[$scope.options.key]) {
                    $scope.to.open = true;
                } else {
                    $scope.to.open = false;
                }
            }

            function updateModel(open) {
                if (open) {
                    // Restore data model value form attic if exists
                    AtticHelper.restoreFromAttic($scope.model, $scope.options.key);
                } else {
                    // Clear attic model and destroy watch on scope destroy
                    AtticHelper.updateToAttic($scope, $scope.model, $scope.options.key);
                }
            }

            $scope.$watch('to.open', function(newVal) {
                updateModel(newVal);
            });

            loadModel();

            // Clear attic model and destroy watch on scope destroy
            AtticHelper.updateToAttic($scope, $scope.model, $scope.options.key);
        }]
    });
});