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
/**
 * Created by stephenwhite on 05/03/15.
 */
angular.module('ts-tstrk1062').config(function($stateProvider) {
    'use strict';

    var commonPath = '/web/webjars/common/webcert/';
    //var intygsTypPath = '/web/webjars/ts-tstrk1062/webcert/';

    var editViewState = function(factoryResolverHelper, $stateParams) {
        return factoryResolverHelper.resolve('ts-tstrk1062.UtkastController.ViewStateService', $stateParams);
    };

    var utkastConfig = function(factoryResolverHelper, $stateParams) {
        return factoryResolverHelper.resolve('ts-tstrk1062.UtkastConfigFactory', $stateParams);
    };

    var viewConfig = function(factoryResolverHelper, $stateParams) {
        return factoryResolverHelper.resolve('ts-tstrk1062.viewConfigFactory', $stateParams);
    };

    $stateProvider.
        state('ts-tstrk1062-edit', {
            data: { defaultActive : 'index', intygType: 'ts-tstrk1062' },
            url: '/ts-tstrk1062/:intygTypeVersion/edit/:certificateId/:focusOn',
            views : {
                'content@' : {
                    templateUrl: commonPath + 'utkast/smiUtkast.html',
                    controller: 'smi.EditCertCtrl',
                    resolve: {
                        ViewState: editViewState,
                        UtkastConfigFactory: utkastConfig,
                        supportPanelConfigFactory: 'ts-tstrk1062.supportPanelConfigFactory'
                    }
                },

                'header@' : {
                    templateUrl: commonPath + 'components/headers/wcHeader.partial.html'
                },

                'header@ts-tstrk1062-edit' : {
                    templateUrl: commonPath + 'utkast/utkastHeader/utkastHeader.html',
                    controller: 'common.UtkastHeader',
                    resolve: {
                        ViewState: editViewState
                    }
                },

                'footer@ts-tstrk1062-edit' : {
                    templateUrl: commonPath + 'utkast/utkast-footer/utkastFooter.html',
                    controller: 'common.UtkastFooter'
                },

                'utkast@ts-tstrk1062-edit' : {
                    templateUrl: commonPath + 'utkast/smiUtkastUE.html',
                    controller: 'smi.EditCert.UECtrl',
                    resolve: {
                        ViewState: editViewState,
                        UtkastConfigFactory: utkastConfig
                    }
                }
            }
        }).
        state('webcert.intyg.ts-tstrk1062', {
            data: { defaultActive: 'index', intygType: 'ts-tstrk1062' },
            url: '/intyg/ts-tstrk1062/:intygTypeVersion/:certificateId/:focusOn?:signed',
            views: {
                'intyg@webcert.intyg': {
                    templateUrl: commonPath + 'intyg/smiIntygUv.html',
                    controller: 'smi.ViewCertCtrlUv',
                    resolve: {
                        ViewState: 'ts-tstrk1062.IntygController.ViewStateService',
                        ViewConfigFactory: viewConfig,
                        supportPanelConfigFactory: 'ts-tstrk1062.supportPanelConfigFactory'
                    }
                },
                'header@webcert.intyg.ts-tstrk1062': {
                    templateUrl: commonPath + 'intyg/intygHeader/intygHeader.html',
                    controller: 'common.IntygHeader',
                    resolve: {
                        IntygViewState: 'ts-tstrk1062.IntygController.ViewStateService'
                    }
                }
            }
        });
});
