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
describe('ts-bas.Utkast.Form5Controller', function() {
    'use strict';

    beforeEach(angular.mock.module('common','ts-bas', function($provide) {
        $provide.value('ts-bas.UtkastController.ViewStateService', {});
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
            $controller('ts-bas.Utkast.Form5Controller', { $scope: $scope });
        }]));

    // --- form5
    it('should reset hidden fields when "harDiabetes" is set to false', function() {
        $scope.viewState.intygModel.diabetes.harDiabetes = true;
        $scope.$digest();

        $scope.viewState.intygModel.diabetes.diabetesTyp = 'DIABETES_TYP_1';
        $scope.viewState.intygModel.diabetes.harDiabetes = false;
        $scope.$digest();

        expect($scope.viewState.intygModel.diabetes.diabetesTyp).toBeUndefined();

        // Attic
        $scope.viewState.intygModel.diabetes.harDiabetes = true;
        $scope.$digest();

        expect($scope.viewState.intygModel.diabetes.diabetesTyp).toBe('DIABETES_TYP_1');
    });

    it('should reset hidden fields when "diabetesTyp" is not "DIABETES_TYP_2"', function() {
        $scope.viewState.intygModel.diabetes.diabetesTyp = 'DIABETES_TYP_2';
        $scope.$digest();

        $scope.viewState.intygModel.diabetes.kost = true;
        $scope.viewState.intygModel.diabetes.tabletter = true;
        $scope.viewState.intygModel.diabetes.insulin = true;
        $scope.viewState.intygModel.diabetes.diabetesTyp = 'DIABETES_TYP_1';
        $scope.$digest();

        expect($scope.viewState.intygModel.diabetes.kost).toBeUndefined();
        expect($scope.viewState.intygModel.diabetes.tabletter).toBeUndefined();
        expect($scope.viewState.intygModel.diabetes.insulin).toBeUndefined();

        // Attic
        $scope.viewState.intygModel.diabetes.diabetesTyp = 'DIABETES_TYP_2';
        $scope.$digest();

        expect($scope.viewState.intygModel.diabetes.kost).toBeTruthy();
        expect($scope.viewState.intygModel.diabetes.tabletter).toBeTruthy();
        expect($scope.viewState.intygModel.diabetes.insulin).toBeTruthy();
    });
    // --- form5
});
