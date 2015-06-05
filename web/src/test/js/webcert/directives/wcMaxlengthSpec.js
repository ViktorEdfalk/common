describe('wcMaxlength', function() {
    'use strict';

    beforeEach(angular.mock.module('common'));

    var $scope, form;

    // Create a form to test the validation directive on.
    beforeEach(angular.mock.inject(function($compile, $rootScope) {
        $scope = $rootScope;
        $scope.model = {
            test: ''
        };

        var el = angular.element('<form name="form"><textarea wc-maxlength maxlength="20" rows="13" ng-model="model.test" name="test" id="test-{{1+1}}"></textarea></form>');

        form = $compile(el)($scope);
        $scope.$digest();
    }));

    it('should work with ngModel', function() {
        $scope.form.test.$setViewValue('This is a test');
        expect($scope.model.test).toEqual('This is a test');
    });
    it('should append number of characters left', function() {
        expect(form.html()).toContain('Tecken kvar: 20');
    });
    it('should update number of characters left', function() {
        $scope.form.test.$setViewValue('13 characters');
        $scope.$digest();
        expect(form.html()).toContain('Tecken kvar: 7'); // 20 - 13
    });
    it('should limit model to set limit', function() {
        $scope.form.test.$setViewValue('this text is very long, extremely long even'); // 44 characters
        $scope.$digest();
        expect(form.html()).toContain('Tecken kvar: 0'); // 20 - 13
        expect($scope.model.test).toEqual('this text is very lo'); // Break after limit
    });
    it('should accept stupid input', function() {
        $scope.form.test.$setViewValue('\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n');
        $scope.$digest();
        expect(form.html()).toContain('Tecken kvar: 0');
    });
    it('should accept unprintable input', function() {
        $scope.form.test.$setViewValue('\0\0\0\0');
        $scope.$digest();
        expect(form.html()).toContain('Tecken kvar: 16');
    });
});
