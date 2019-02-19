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

    var commonPath = '/web/webjars/common/webcert/';

    var editViewState = function(factoryResolverHelper, $stateParams) {
        return factoryResolverHelper.resolve('tstrk1009.UtkastController.ViewStateService', $stateParams);
    };

    var utkastConfig = function(factoryResolverHelper, $stateParams) {
        return factoryResolverHelper.resolve('tstrk1009.UtkastConfigFactory', $stateParams);
    };

    var viewConfig = function(factoryResolverHelper, $stateParams) {
        return factoryResolverHelper.resolve('tstrk1009.viewConfigFactory', $stateParams);
    };

    var supportPanelConfig = function(factoryResolverHelper, $stateParams) {
        return factoryResolverHelper.resolve('tstrk1009.supportPanelConfigFactory', $stateParams);
    };

    $stateProvider.
        state('tstrk1009-edit', {
            data: { defaultActive : 'index', intygType: 'tstrk1009' },
            url: '/tstrk1009/:intygTypeVersion/edit/:certificateId/:focusOn',
            views : {
                'content@' : {
                    templateUrl: commonPath + 'utkast/smiUtkast.html',
                    controller: 'smi.EditCertCtrl',
                    resolve: {
                        ViewState: editViewState,
                        UtkastConfigFactory: utkastConfig,
                        supportPanelConfigFactory: supportPanelConfig
                    }
                },

                'header@' : {
                    templateUrl: commonPath + 'components/headers/wcHeader.partial.html'
                },

                'header@tstrk1009-edit' : {
                    templateUrl: commonPath + 'utkast/utkastHeader/utkastHeader.html',
                    controller: 'common.UtkastHeader',
                    resolve: {
                        ViewState: editViewState
                    }
                },

                'footer@tstrk1009-edit' : {
                    templateUrl: commonPath + 'utkast/utkast-footer/utkastFooter.html',
                    controller: 'common.UtkastFooter'
                },

                'utkast@tstrk1009-edit' : {
                    templateUrl: commonPath + 'utkast/smiUtkastUE.html',
                    controller: 'smi.EditCert.UECtrl',
                    resolve: {
                        ViewState: editViewState,
                        UtkastConfigFactory: utkastConfig
                    }
                }
            }
        }).
        state('webcert.intyg.tstrk1009', {
            data: { defaultActive: 'index', intygType: 'tstrk1009' },
            url: '/intyg/tstrk1009/:intygTypeVersion/:certificateId/:focusOn?:signed',
            views: {
                'intyg@webcert.intyg': {
                    templateUrl: commonPath + 'intyg/smiIntygUv.html',
                    controller: 'smi.ViewCertCtrlUv',
                    resolve: {
                        ViewState: 'tstrk1009.IntygController.ViewStateService',
                        ViewConfigFactory: viewConfig,
                        supportPanelConfigFactory: supportPanelConfig
                    }
                },
                'header@webcert.intyg.tstrk1009': {
                    templateUrl: commonPath + 'intyg/intygHeader/intygHeader.html',
                    controller: 'common.IntygHeader',
                    resolve: {
                        IntygViewState: 'tstrk1009.IntygController.ViewStateService'
                    }
                }
            }
        });
});
