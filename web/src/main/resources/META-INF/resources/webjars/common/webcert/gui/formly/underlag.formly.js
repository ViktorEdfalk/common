angular.module('common').run(function(formlyConfig) {
    'use strict';

    formlyConfig.setType({
        name: 'underlag',
        templateUrl: '/web/webjars/common/webcert/gui/formly/underlag.formly.html',
        controller: ['$scope', 'common.dynamicLabelService', 'common.ObjectHelper', 'common.ArendeListViewStateService',
            'common.UtkastValidationService',
        function($scope, dynamicLabelService, objectHelper, ArendeListViewState, UtkastValidationService) {

            var chooseOption = {
                id: null,
                label: 'Välj...'
            };

            $scope.underlagOptions = [];
            var underlag = $scope.model[$scope.options.key];

            $scope.previousUnderlagIncomplete = function() {
                var prev = underlag[underlag.length - 1];
                return objectHelper.isEmpty(prev.typ) || objectHelper.isEmpty(prev.datum) || objectHelper.isEmpty(prev.hamtasFran);
            };

            $scope.hasKomplettering = function() {
                return ArendeListViewState.hasKompletteringar($scope.options.key);
            };

            $scope.hasValidationError = function(field, index) {
                return $scope.formState.viewState.common.validation.messagesByField &&
                    !!$scope.formState.viewState.common.validation.messagesByField['underlag.' + index + '.' + field];
            };

            $scope.validate = function() {
                UtkastValidationService.validate($scope.model);
            };

            $scope.$watch('formState.viewState.common.validation.messagesByField', function() {
                $scope.underlagValidations = [];
                angular.forEach($scope.formState.viewState.common.validation.messagesByField, function(validations, key) {
                    if (key.substr(0, $scope.options.key.length) === $scope.options.key.toLowerCase()) {
                        $scope.underlagValidations = $scope.underlagValidations.concat(validations);
                    }
                });
            });

            function updateUnderlag() {
                $scope.underlagOptions = [chooseOption];

                if ($scope.to.underlagsTyper) {
                    $scope.to.underlagsTyper.forEach(function (underlagsTyp) {
                        $scope.underlagOptions.push({
                            'id': underlagsTyp,
                            'label': dynamicLabelService.getProperty('KV_FKMU_0005.' + underlagsTyp + '.RBK')
                        });
                    });
                }
            }

            $scope.$on('dynamicLabels.updated', function() {
                updateUnderlag();
            });

            updateUnderlag();
        }]
    });

});
