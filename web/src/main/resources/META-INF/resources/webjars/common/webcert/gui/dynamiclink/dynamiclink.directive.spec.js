describe('dynamiclink', function() {
    'use strict';

    beforeEach(angular.mock.module('common'));

    var $scope;
    var _links = {
        'somekey': {
            'text': 'real text',
            'url': 'http://some.url',
            'tooltip': 'My tooltip!',
            'target': '_blank'
        }
    };

    // Create a <p> with a dynamic link to test the validation directive on.
    beforeEach(angular.mock.inject(
        ['$compile', '$rootScope', 'dynamicLinkService', function($compile, $rootScope, dynamicLinkService) {
            dynamicLinkService.addLinks(_links);
            $scope = $rootScope.$new();
        }]));


    it('Should print an anchor when key exists', function($compile) {
        var el = angular.element('<p>This is text with <span dynamiclink key="somekey"></span></p>');
        $compile(el)($scope);
        $scope.$digest();

        expect(el.html()).toContain('href="http://some.url"');
        expect(el.html()).toContain('>real text<');
        expect(el.html()).toContain('title="My tooltip!"');
        expect(el.html()).toContain('target="_blank"');
    });

    it('Should print a warning anchor when key does not exist', function($compile) {
        var el = angular.element('<p>This is text with <span dynamiclink key="otherkey"></span></p>');
        $compile(el)($scope);
        $scope.$digest();

        expect(el.html()).toContain('href="#"');
        expect(el.html()).toContain('>WARNING: could not resolve dynamic link: otherkey');
    });
});