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

angular.module('common').run(function(formlyConfig) {
    'use strict';

    formlyConfig.setType({
        name: 'dodsorsakMultirow',
        templateUrl: '/web/webjars/common/webcert/gui/formly/dodsorsakMultirow.formly.html',
        defaultOptions: {
            className: 'slide-animation'
        },
        controller: ['$scope',
        function($scope) {

            // Make sure the right amount of rows exist
            var emptyRow = {
                beskrivning: undefined,
                datum: '',
                specifikation: null
            };

            function setupRows(){
                var rows = $scope.to.maxRows;
                var modelArray = $scope.model[$scope.options.key];
                if(!modelArray || modelArray.length === 0){
                    if(rows > 1){
                        modelArray.push(angular.copy(emptyRow));
                    }
                }
            }

            $scope.$on('intyg.loaded', setupRows);
        }]
    });

});
