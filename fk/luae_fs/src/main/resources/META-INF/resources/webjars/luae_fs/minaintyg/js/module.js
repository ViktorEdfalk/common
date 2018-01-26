/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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

angular.module('luae_fs', [ 'ui.bootstrap', 'ngCookies', 'ui.router', 'ngSanitize', 'common']);

angular.module('luae_fs').config(function($stateProvider) {
    'use strict';

    $stateProvider.
        state('luae_fs-view', {
            url :'/luae_fs/view/:certificateId',
            templateUrl: '/web/webjars/luae_fs/minaintyg/views/view-cert.html',
            controller: 'luae_fs.ViewCertCtrl',
            data : { title: 'Läkarutlåtande för aktivitetsersättning vid förlängd skolgång', keepInboxTabActive: true,
                breadcrumb: ['inkorg', 'intyg'] }
        }).
        state('luae_fs-fel', {
            url : '/luae_fs/fel/:errorCode',
            templateUrl: '/web/webjars/luae_fs/minaintyg/views/error.html',
            controller: 'luae_fs.ErrorCtrl',
            data : { title: 'Fel' }
        }).
        state('luae_fs-visafel', {
            url :'/luae_fs/visafel/:errorCode',
            templateUrl: '/web/webjars/luae_fs/minaintyg/views/error.html',
            controller: 'luae_fs.ErrorCtrl',
            data : { title: 'Fel',
                    backLink: '/web/start' }
        });
});

// Inject language resources
angular.module('luae_fs').run(['common.messageService', 'luae_fs.messages',
    function(messageService, luaeFsMessages) {
        'use strict';

        messageService.addResources(luaeFsMessages);
    }]);
