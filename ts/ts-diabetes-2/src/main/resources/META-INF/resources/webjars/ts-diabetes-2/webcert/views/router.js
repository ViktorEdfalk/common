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
angular.module('ts-diabetes-2').config(function($stateProvider) {
    'use strict';

    var commonPath = '/web/webjars/common/webcert/';

    $stateProvider.
        state('ts-diabetes-2-edit', {
            data: { defaultActive : 'index', intygType: 'ts-diabetes-2', useFmb: false },
            url : '/ts-diabetes-2/edit/:certificateId/:focusOn',
                views : {
                'content@' : {
                    templateUrl: commonPath + 'utkast/smiUtkast.html',
                    controller: 'smi.EditCertCtrl',
                    resolve: {
                        ViewState: 'ts-diabetes-2.EditCertCtrl.ViewStateService',
                        UtkastConfigFactory: 'ts-diabetes-2.UtkastConfigFactory',
                        supportPanelConfigFactory: 'ts-diabetes-2.supportPanelConfigFactory'
                    }
                },

                'header@' : {
                    templateUrl: commonPath + 'components/headers/wcHeader.partial.html'
                },

                'header@ts-diabetes-2-edit' : {
                    templateUrl: commonPath + 'utkast/utkastHeader/utkastHeader.html',
                    controller: 'common.UtkastHeader',
                    resolve: {
                        ViewState: 'ts-diabetes-2.EditCertCtrl.ViewStateService'
                    }
                },

                'footer@ts-diabetes-2-edit' : {
                    templateUrl: commonPath + 'utkast/utkast-footer/utkastFooter.html',
                    controller: 'common.UtkastFooter'
                },

                'utkast@ts-diabetes-2-edit' : {
                    templateUrl: commonPath + 'utkast/smiUtkastUE.html',
                    controller: 'smi.EditCert.UECtrl',
                    resolve: {
                        ViewState: 'ts-diabetes-2.EditCertCtrl.ViewStateService',
                        UtkastConfigFactory: 'ts-diabetes-2.UtkastConfigFactory'
                    }
                 }
            }
        }).state('webcert.intyg.ts-diabetes-2', {
            data: { defaultActive : 'index', intygType: 'ts-diabetes-2' },
            url:'/intyg/ts-diabetes-2/:certificateId/:focusOn?:signed?:approvereceivers?',
            views: {
                'intyg@webcert.intyg' : {
                    templateUrl: commonPath + 'intyg/smiIntygUv.html',
                    controller: 'smi.ViewCertCtrlUv',
                    resolve: {
                        ViewState: 'ts-diabetes-2.IntygController.ViewStateService',
                        ViewConfigFactory: 'ts-diabetes-2.viewConfigFactory',
                        supportPanelConfigFactory: 'ts-diabetes-2.supportPanelConfigFactory'
                    }
                },
                'header@webcert.intyg.ts-diabetes-2' : {
                    templateUrl: commonPath + 'intyg/intygHeader/intygHeader.html',
                    controller: 'common.IntygHeader',
                    resolve: {
                        IntygViewState: 'ts-diabetes-2.IntygController.ViewStateService'
                    }
                }
            }
        });
});