/**
 * Listen to intyg loaded event and present a message that the user is marked for secrecy (sekretessmarkerad) if he is.
 */
angular.module('common').directive('wcSecrecyMarkMessage', [
    'common.PatientProxy', 'common.ViewStateService',
    function(PatientProxy, ViewStateService) {
        'use strict';

        return {
            restrict: 'A',
            replace: true,
            scope: true,
            controller: function($scope) {

                $scope.viewstate = ViewStateService;

                /*
                 * Lookup patient to check for sekretessmarkering
                 */
                if ($scope.cert) {
                    lookupPatient($scope.cert);
                }
                $scope.$on('intyg.loaded', function(event, content) {
                    lookupPatient(content);
                });
                function lookupPatient(content) {
                    ViewStateService.sekretessmarkering = false;
                    ViewStateService.sekretessmarkeringError = false;

                    var onSuccess = function(resultPatient) {
                        ViewStateService.sekretessmarkering = resultPatient.sekretessmarkering;
                    };

                    var onNotFound = function() {
                        ViewStateService.sekretessmarkeringError = true;
                    };

                    var onError = function() {
                        ViewStateService.sekretessmarkeringError = true;
                    };

                    if (content.grundData && content.grundData.patient && content.grundData.patient.personId) {
                        PatientProxy.getPatient(content.grundData.patient.personId, onSuccess, onNotFound, onError);
                    }
                }

            },
            templateUrl: '/web/webjars/common/webcert/patient/wcSecrecyMarkMessage.directive.html'
        };
    }]);
