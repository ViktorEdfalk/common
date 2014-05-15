define([
    'angular',
    'ts-diabetes/webcert/js/controllers',
    'ts-diabetes/webcert/js/messages',
    'common/js/webcert/CertificateService',
    'common/js/webcert/ManageCertView',
    'common/js/webcert/eyeDecimal'
], function(angular, controllers, messages, wcCertificateService, wcManageCertView, eyeDecimal) {
    'use strict';

    var moduleName = 'ts-diabetes';

    var module = angular.module(moduleName, [controllers, wcCertificateService, wcManageCertView, eyeDecimal]);

    module.config(['$routeProvider', function($routeProvider) {
        $routeProvider.
            when('/ts-diabetes/edit/:certificateId', {
                templateUrl: '/web/webjars/ts-diabetes/webcert/views/edit-cert.html',
                controller: 'ts-diabetes.EditCertCtrl'
            }).
            when('/ts-diabetes/view/:certificateId', {
                templateUrl: '/web/webjars/ts-diabetes/common/views/view-cert.html',
                controller: 'ts-diabetes.ViewCertCtrl'
            });
    }]);

    // Inject language resources
    // NOTE: This only works since we always load webcert before the module, when the messageService
    // is moved to a commons project, make sure this is loaded for this module as well.
    module.run(['messageService',
        function(messageService) {
            messageService.addResources(messages);
        }
    ]);

    return moduleName;
});
