angular.module('lisu').controller('sjukpenning-utokad.EditCert.FormlyCtrl',
    ['$scope', 'sjukpenning-utokad.EditCertCtrl.ViewStateService', 'sjukpenning-utokad.FormFactory', 'common.TillaggsfragorHelper',
        function FormlyCtrl($scope, viewState, formFactory, tillaggsfragorHelper) {
            'use strict';

            $scope.viewState = viewState;

            $scope.model = viewState.intygModel;

            $scope.options = {
                formState:{viewState:viewState}
            };

            $scope.formFields = formFactory.getFormFields();

            tillaggsfragorHelper.buildTillaggsFragor($scope.formFields, viewState.intygModel, 9);
            $scope.$on('dynamicLabels.updated', function () {
                tillaggsfragorHelper.buildTillaggsFragor($scope.formFields, viewState.intygModel, 9);
            });

        }
    ]);