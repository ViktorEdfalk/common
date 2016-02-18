/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
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

angular.module('luse').config(function($stateProvider) {
    'use strict';

    var commonPath = '/web/webjars/common/webcert/';
    var intygsTypPath = '/web/webjars/sjukersattning/webcert/';

    $stateProvider.
        state('luse-edit', {
            data: { defaultActive : 'index', intygType: 'luse' },
            url : '/luse/edit/:certificateId?:patientId&:hospName',
            views : {
                'content@' : {
                    templateUrl: intygsTypPath + 'views/utkast/utkast.html',
                    controller: 'sjukersattning.EditCertCtrl'
                },

                'wcHeader@luse-edit' : {
                    templateUrl: commonPath + 'gui/headers/wcHeader.partial.html',
                    controller: 'common.wcHeaderController'
                },

                'header@luse-edit' : {
                    templateUrl: commonPath + 'utkast/utkast-header/utkastHeader.html',
                    controller: 'common.UtkastHeader'
                },

                'footer@luse-edit' : {
                    templateUrl: commonPath + 'utkast/utkast-footer/utkastFooter.html',
                    controller: 'common.UtkastFooter'
                },

                'formly@luse-edit' : {
                    templateUrl: intygsTypPath + 'views/utkast/formly.html',
                    controller: 'sjukersattning.EditCert.FormlyCtrl'
                }
            }
        }).
        state('webcert.intyg.fk.luse', {
            data: { defaultActive : 'index', intygType: 'luse' },
            url:'/intyg/luse/:certificateId?:patientId&:hospName&:signed',
            views: {
                'intyg@webcert.intyg.fk' : {
                    templateUrl: intygsTypPath + 'views/intyg/intyg.html',
                    controller: 'luse.ViewCertCtrl'
                },
                'fragaSvar@webcert.intyg.fk' : {
                    templateUrl: commonPath + 'intyg/fk/fragasvar/fragasvar.html',
                    controller: 'common.QACtrl'
                },
                'header@webcert.intyg.fk.luse' : {
                    templateUrl: commonPath + 'intyg/intyg-header/intyg-header.html',
                    controller: 'common.IntygHeader'
                }
            }
        }).
        state('webcert.fragasvar.luse', {
            data: { defaultActive : 'unhandled-qa', intygType: 'luse' },
            url: '/fragasvar/luse/:certificateId',
            views: {
                'intyg@webcert.fragasvar' : {
                    templateUrl: intygsTypPath + 'views/intyg/intyg.html',
                    controller: 'luse.ViewCertCtrl'
                },
                'fragasvar@webcert.fragasvar' : {
                    templateUrl: commonPath + 'intyg/fk/fragasvar/fragasvar.html',
                    controller: 'common.QACtrl'
                },
                'header@webcert.fragasvar' : {
                    templateUrl: commonPath + 'intyg/intyg-header/intyg-header.html',
                    controller: 'common.IntygHeader'
                }
            }
        });
});