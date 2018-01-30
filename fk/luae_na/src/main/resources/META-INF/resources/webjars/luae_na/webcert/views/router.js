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
angular.module('luae_na').config(function($stateProvider) {
    'use strict';

    var commonPath = '/web/webjars/common/webcert/';

    $stateProvider.
        state('luae_na-edit', {
            data: { defaultActive : 'index', intygType: 'luae_na' },
            url : '/luae_na/edit/:certificateId/:focusOn',
            views : {
                'content@' : {
                    templateUrl: commonPath + 'utkast/smiUtkast.html',
                    controller: 'smi.EditCertCtrl',
                    resolve: {
                        ViewState: 'luae_na.EditCertCtrl.ViewStateService',
                        FormFactory: 'luae_na.FormFactory',
                        supportPanelConfigFactory: 'luae_na.supportPanelConfigFactory'
                    }
                },

                'header@' : {
                    templateUrl: commonPath + 'components/headers/wcHeader.partial.html'
                },

                'header@luae_na-edit' : {
                    templateUrl: commonPath + 'utkast/utkast-header/utkastHeader.html',
                    controller: 'common.UtkastHeader'
                },

                'footer@luae_na-edit' : {
                    templateUrl: commonPath + 'utkast/utkast-footer/utkastFooter.html',
                    controller: 'common.UtkastFooter'
                },

                'formly@luae_na-edit' : {
                    templateUrl: commonPath + 'utkast/smiUtkastFormly.html',
                    controller: 'smi.EditCert.FormlyCtrl',
                    resolve: {
                        ViewState: 'luae_na.EditCertCtrl.ViewStateService',
                        FormFactory: 'luae_na.FormFactory'
                    }
                }
            }
        }).
        state('webcert.intyg.fk.luae_na', {
            data: { defaultActive : 'index', intygType: 'luae_na' },
            url:'/intyg/luae_na/:certificateId/:focusOn',
            views: {
                'intyg@webcert.intyg.fk' : {
                    templateUrl: commonPath + 'intyg/smiIntygUv.html',
                    controller: 'smi.ViewCertCtrlUv',
                    resolve: {
                        ViewState: 'luae_na.IntygController.ViewStateService',
                        ViewConfigFactory: 'luae_na.viewConfigFactory',
                        supportPanelConfigFactory: 'luae_na.supportPanelConfigFactory'
                    }
                },
                'header@webcert.intyg.fk.luae_na' : {
                    templateUrl: commonPath + 'intyg/intygHeader/intygHeader.html',
                    controller: 'common.IntygHeader'
                }
            }
        }).
        state('webcert.fragasvar.luae_na', {
            data: { defaultActive : 'enhet-arenden', intygType: 'luae_na' },
            url: '/fragasvar/luae_na/:certificateId',
            views: {
                'intyg@webcert.fragasvar' : {
                    templateUrl: commonPath + 'intyg/smiIntygUv.html',
                    controller: 'smi.ViewCertCtrlUv',
                    resolve: {
                        ViewState: 'luae_na.IntygController.ViewStateService',
                        ViewConfigFactory: 'luae_na.viewConfigFactory',
                        supportPanelConfigFactory: 'luae_na.supportPanelConfigFactory'
                    }
                },
                'header@webcert.fragasvar.luae_na' : {
                    templateUrl: commonPath + 'intyg/intygHeader/intygHeader.html',
                    controller: 'common.IntygHeader'
                }
            }
        });
});
