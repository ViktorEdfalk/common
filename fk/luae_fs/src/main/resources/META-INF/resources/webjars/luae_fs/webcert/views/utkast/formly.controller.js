angular.module('luae_fs').controller('luae_fs.EditCert.FormlyCtrl',
    ['$scope', 'luae_fs.EditCertCtrl.ViewStateService', 'luae_fs.FormFactory', 'common.TillaggsfragorHelper', 'common.ArendeListViewStateService',
        function FormlyCtrl($scope, viewState, formFactory, tillaggsfragorHelper, ArendeListViewState) {
            'use strict';

            $scope.viewState = viewState;

            $scope.model = viewState.intygModel;

            // hasKompletteringar needs to be here since a formly wrapper (validationGroup.formly.js) currently can not have a controller
            $scope.options = {
                formState:{viewState:viewState, hasKompletteringar:ArendeListViewState.hasKompletteringar.bind(ArendeListViewState)}
            };

            $scope.formFields = formFactory.getFormFields();

            tillaggsfragorHelper.buildTillaggsFragor($scope.formFields, viewState.intygModel, $scope.formFields.length - 1);
            $scope.$on('intyg.loaded', function () {
                tillaggsfragorHelper.buildTillaggsFragor($scope.formFields, viewState.intygModel, $scope.formFields.length - 1);
            });
        }
    ]);