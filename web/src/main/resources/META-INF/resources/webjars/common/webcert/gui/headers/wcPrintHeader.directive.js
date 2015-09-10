angular.module('common').directive('wcPrintHeader',
    [ 'common.User',
        function(User) {
            'use strict';

            return {
                restrict: 'A',
                replace: true,
                scope: {
                    titleId: '@',
                    printMessageId: '@',
                    intygsId: '='
                },
                controller: function($scope) {
                    $scope.today = new Date();
                    $scope.user = User.user;
                },
                templateUrl: '/web/webjars/common/webcert/gui/headers/wcPrintHeader.directive.html'
            };
        }]);
