angular.module('fk7263').controller('fk7263.EditCert.Form4Ctrl',
    ['$log', 'fk7263.Domain.IntygModel', '$scope', 'fk7263.EditCertCtrl.ViewStateService',
        function($log, model, $scope, viewState) {
            'use strict';
            $scope.model = model;
            $scope.viewState = viewState;
            $scope.inputLimits = {
                funktionsnedsattning: 450};
        }]);