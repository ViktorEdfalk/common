describe('ts-bas.Utkast.Form4Controller', function() {
    'use strict';

    var ManageCertView;
    var User;
    var wcFocus;
    var utkastNotifyService;
    var viewState;
    var anchorScrollService;


    beforeEach(angular.mock.module('common','ts-bas', function($provide) {
        ManageCertView = jasmine.createSpyObj('common.ManageCertView', [ 'load' ]);
        User = {};
        wcFocus = {};
        $provide.value('common.ManageCertView', ManageCertView);
        $provide.value('common.UserModel', User);
        $provide.value('common.wcFocus', wcFocus);
        $provide.value('common.utkastNotifyService', utkastNotifyService);
        $provide.value('common.anchorScrollService', anchorScrollService);
    }));

    var $scope, ctrl;

    beforeEach(angular.mock.inject([
        '$controller',
        '$rootScope',
        'ts-bas.UtkastController.ViewStateService',
        function($controller, $rootScope, _viewState_) {
        $scope = $rootScope.$new();
        viewState = _viewState_;

        ctrl = $controller('ts-bas.Utkast.Form4Controller', { $scope: $scope });

            var cert = testData.cert;

        spyOn(viewState, 'setDraftModel');
        spyOn(viewState, 'intygModel').and.returnValue(cert);

        $scope.$digest();
    }]));

    // --- form4
    it('should reset hidden fields when "riskfaktorerStroke" is set to false', function() {
        $scope.cert.hjartKarl.riskfaktorerStroke = true;
        $scope.$digest();

        $scope.cert.hjartKarl.beskrivningRiskfaktorer = 'Hello';
        $scope.cert.hjartKarl.riskfaktorerStroke = false;
        $scope.$digest();

        expect($scope.cert.hjartKarl.beskrivningRiskfaktorer).toBe('');

        // Attic
        $scope.cert.hjartKarl.riskfaktorerStroke = true;
        $scope.$digest();

        expect($scope.cert.hjartKarl.beskrivningRiskfaktorer).toBe('Hello');
    });
    // --- form4
});
