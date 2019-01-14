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
angular.module('tstrk1009').config(function($stateProvider) {
    'use strict';
    $stateProvider.
        state('tstrk1009-view', {
            url :'/tstrk1009/:intygTypeVersion/view/:certificateId',
            templateUrl: '/web/webjars/tstrk1009/minaintyg/views/view-cert.html',
            controller: 'tstrk1009.ViewCertCtrl',
            resolve: {
                viewConfigFactory: function(factoryResolverHelper, $stateParams) {
                    return factoryResolverHelper.resolve('tstrk1009.viewConfigFactory', $stateParams);
                }
            },
            data:{title: 'Läkarintyg Transportstyrelsen diabetes', keepInboxTabActive: true,
                breadcrumb: ['inkorg', 'intyg']}
        }).
        state('tstrk1009-visafel', {
            url :'/tstrk1009/visafel/:errorCode',
            templateUrl: '/web/webjars/tstrk1009/minaintyg/views/error.html',
            controller: 'tstrk1009.ErrorCtrl',
            data : { title: 'Fel', backLink: '/web/start' }
        });
});