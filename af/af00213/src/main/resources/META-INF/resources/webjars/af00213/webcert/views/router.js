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
angular.module('af00213').config(function($stateProvider) {
    'use strict';

    var commonPath = '/web/webjars/common/webcert/';

    $stateProvider.
        state('af00213-edit', {
            data: { defaultActive : 'index', intygType: 'af00213', useFmb: true },
            url : '/af00213/edit/:certificateId/:focusOn',
                views : {
                'content@' : {
                    templateUrl: commonPath + 'utkast/smiUtkast.html',
                    controller: 'smi.EditCertCtrl',
                    resolve: {
                        ViewState: 'af00213.EditCertCtrl.ViewStateService',
                        UtkastConfigFactory: 'af00213.UtkastConfigFactory',
                        supportPanelConfigFactory: 'af00213.supportPanelConfigFactory'
                    }
                },

                'header@' : {
                    templateUrl: commonPath + 'components/headers/wcHeader.partial.html'
                },

                'header@af00213-edit' : {
                    templateUrl: commonPath + 'utkast/utkastHeader/utkastHeader.html',
                    controller: 'common.UtkastHeader',
                    resolve: {
                        ViewState: 'af00213.EditCertCtrl.ViewStateService'
                    }
                },

                'footer@af00213-edit' : {
                    templateUrl: commonPath + 'utkast/utkast-footer/utkastFooter.html',
                    controller: 'common.UtkastFooter'
                },

                'utkast@af00213-edit' : {
                    templateUrl: commonPath + 'utkast/smiUtkastUE.html',
                    controller: 'smi.EditCert.UECtrl',
                    resolve: {
                        ViewState: 'af00213.EditCertCtrl.ViewStateService',
                        UtkastConfigFactory: 'af00213.UtkastConfigFactory'
                    }
                 }
            }
        }).state('webcert.intyg.af00213', {
            data: { defaultActive : 'index', intygType: 'af00213' },
            url:'/intyg/af00213/:certificateId/:focusOn?:signed?:approvereceivers?',
            views: {
                'intyg@webcert.intyg' : {
                    templateUrl: commonPath + 'intyg/smiIntygUv.html',
                    controller: 'smi.ViewCertCtrlUv',
                    resolve: {
                        ViewState: 'af00213.IntygController.ViewStateService',
                        ViewConfigFactory: 'af00213.viewConfigFactory',
                        supportPanelConfigFactory: 'af00213.supportPanelConfigFactory'
                    }
                },
                'header@webcert.intyg.af00213' : {
                    templateUrl: commonPath + 'intyg/intygHeader/intygHeader.html',
                    controller: 'common.IntygHeader',
                    resolve: {
                        IntygViewState: 'af00213.IntygController.ViewStateService'
                    }
                }
            }
        }).
        state('af00213-readonly', {
        url: '/intyg-read-only/af00213/:certificateId',
        views: {
            'content@': {
                templateUrl: commonPath + 'intyg/read-only-view/wcIntygReadOnlyView.template.html',
                controller: 'common.wcIntygReadOnlyViewController',
                resolve: {
                    intygsType: function() {
                        return 'af00213';
                    },
                    ViewConfigFactory: 'af00213.viewConfigFactory',
                    DiagnosExtractor: function() {
                        return function (af00213Model) {
                            return af00213Model.diagnoser[0].diagnosKod;
                        };
                    }
                }
            }
        }
    });
});