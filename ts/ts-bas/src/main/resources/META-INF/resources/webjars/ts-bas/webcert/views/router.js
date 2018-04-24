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
/**
 * Created by stephenwhite on 05/03/15.
 */
angular.module('ts-bas').config(function($stateProvider) {
    'use strict';

    var commonPath = '/web/webjars/common/webcert/';
    var intygsTypPath = '/web/webjars/ts-bas/webcert/';

    $stateProvider.
        state('ts-bas-edit', {
            data: { defaultActive : 'index', intygType: 'ts-bas' },
            url: '/ts-bas/edit/:certificateId/:focusOn',
            views : {
                'content@' : {
                    templateUrl: commonPath + 'utkast/smiUtkast.html',
                    controller: 'smi.EditCertCtrl',
                    resolve: {
                        ViewState: 'ts-bas.UtkastController.ViewStateService',
                        UtkastConfigFactory: 'ts-bas.UtkastConfigFactory',
                        supportPanelConfigFactory: 'ts-bas.supportPanelConfigFactory'
                    }
                },

                'header@' : {
                    templateUrl: commonPath + 'components/headers/wcHeader.partial.html'
                },

                'header@ts-bas-edit' : {
                    templateUrl: commonPath + 'utkast/utkastHeader/utkastHeader.html',
                    controller: 'common.UtkastHeader',
                    resolve: {
                        ViewState: 'ts-bas.UtkastController.ViewStateService'
                    }
                },

                'footer@ts-bas-edit' : {
                    templateUrl: commonPath + 'utkast/utkast-footer/utkastFooter.html',
                    controller: 'common.UtkastFooter'
                },

                'utkast@ts-bas-edit' : {
                    templateUrl: commonPath + 'utkast/smiUtkastUE.html',
                    controller: 'smi.EditCert.UECtrl',
                    resolve: {
                        ViewState: 'ts-bas.UtkastController.ViewStateService',
                        UtkastConfigFactory: 'ts-bas.UtkastConfigFactory'
                    }
                }
            }
        }).
        state('webcert.intyg.ts-bas', {
            data: { defaultActive: 'index', intygType: 'ts-bas' },
            url: '/intyg/ts-bas/:certificateId/:focusOn?:signed',
            views: {
                'intyg@webcert.intyg': {
                    templateUrl: intygsTypPath + 'views/intyg/intyg.html',
                    controller: 'ts-bas.IntygController',
                    resolve: {
                        supportPanelConfigFactory: 'ts-bas.supportPanelConfigFactory'
                    }
                },
                'header@webcert.intyg.ts-bas': {
                    templateUrl: commonPath + 'intyg/intygHeader/intygHeader.html',
                    controller: 'common.IntygHeader',
                    resolve: {
                        IntygViewState: 'ts-bas.IntygController.ViewStateService'
                    }
                }
            }
        });
});
