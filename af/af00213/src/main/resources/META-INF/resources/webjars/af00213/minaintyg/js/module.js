/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
angular.module('af00213', ['ui.bootstrap', 'ngCookies', 'ui.router', 'ngSanitize', 'common']);

angular.module('af00213').config(function($stateProvider) {
    'use strict';

    $stateProvider.state('af00213-view', {
        url: '/af00213/:intygTypeVersion/view/:certificateId',
        templateUrl: '/web/webjars/af00213/minaintyg/views/view-cert.html',
        controller: 'common.ViewCertCtrl',
        resolve: {
            viewConfigFactory: function(factoryResolverHelper, $stateParams) {
              return factoryResolverHelper.resolve('af00213.viewConfigFactory', $stateParams);
            },
            viewFactory: function(factoryResolverHelper, $stateParams) {
                return factoryResolverHelper.resolve('af00213.viewFactory', $stateParams);
            }
        },
        data: {
            title: 'Läkarintyg för sjukpenning',
            keepInboxTabActive: true,
            breadcrumb: ['inkorg', 'intyg']
        }
    });
});

// Inject language resources
angular.module('af00213').run(['common.messageService', 'af00213.messages', function(messageService, af00213Messages) {
    'use strict';

    messageService.addResources(af00213Messages);
}]);
