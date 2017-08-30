angular.module('db').service('db.IntygController.ViewStateService',
    ['$log', 'common.IntygViewStateService',
        function($log, CommonViewState) {
            'use strict';

            this.common = CommonViewState;

            this.reset = function() {
                this.common.reset();
                this.common.defaultRecipient = 'FKASSA';
                this.common.intygProperties.type = 'db';
            };

            this.reset();
        }]);