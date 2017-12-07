angular.module('common.formlyBaseTypes').run(function(formlyConfig) {
    'use strict';

    formlyConfig.setType({
        name: 'validation-on-change',
        controller: ['$scope', '$timeout', 'common.UtkastValidationService', function($scope, $timeout, UtkastValidationService) {
            $scope.onChange = function() {
                // $timeout is needed to allow for the attic functionality to clear the model value for hidden fields
                $timeout(function() {
                    UtkastValidationService.validate($scope.model);
                });
            };
        }],
        defaultOptions:  {
            templateOptions: {
                onChange: 'onChange()'
            }
        }
    });
});