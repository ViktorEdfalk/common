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
describe('ts-bas.Utkast.Form15Controller', function() {
    'use strict';

    beforeEach(angular.mock.module('common','ts-bas', function($provide) {
    }));

    var $scope;

    beforeEach(angular.mock.inject([
        '$controller',
        '$rootScope',
        'ts-bas.UtkastController.ViewStateService',
        'ts-bas.Domain.IntygModel',
        function($controller, $rootScope, viewState, IntygModel) {
            $scope = $rootScope.$new();
            var model = IntygModel._members.build();
            viewState.intygModel = model.content;
            $controller('ts-bas.Utkast.Form15Controller', { $scope: $scope });
        }]));

    // --- form15
    it('should reset hidden fields when "stadigvarandeMedicinering" is set to false', function() {
        $scope.viewState.intygModel.medicinering.stadigvarandeMedicinering = true;
        $scope.$digest();

        $scope.viewState.intygModel.medicinering.beskrivning = 'Hello';
        $scope.viewState.intygModel.medicinering.stadigvarandeMedicinering = false;
        $scope.$digest();

        expect($scope.viewState.intygModel.medicinering.beskrivning).toBe('');

        // Attic
        $scope.viewState.intygModel.medicinering.stadigvarandeMedicinering = true;
        $scope.$digest();

        expect($scope.viewState.intygModel.medicinering.beskrivning).toBe('Hello');
    });
    // --- form15
});
