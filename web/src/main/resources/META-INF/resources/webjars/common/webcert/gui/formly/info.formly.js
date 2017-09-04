angular.module('common').run(function(formlyConfig) {
    'use strict';

    formlyConfig.setType({
        name: 'info',
        defaultOptions: {
            className: 'fold-animation'
        },
        templateUrl: '/web/webjars/common/webcert/gui/formly/info.formly.html'
    });

});
