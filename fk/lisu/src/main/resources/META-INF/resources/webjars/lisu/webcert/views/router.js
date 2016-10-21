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

angular.module('lisu').config(function($stateProvider) {
    'use strict';

    var commonPath = '/web/webjars/common/webcert/';
    var intygsTypPath = '/web/webjars/lisu/webcert/';

    $stateProvider.
        state('lisu-edit', {
            data: { defaultActive : 'index', intygType: 'lisu' },
            url : '/lisu/edit/:certificateId?:patientId&:hospName&:fornamn&:efternamn&:mellannamn&:postadress&:postnummer&:postort&:sjf',
            views : {
                'content@' : {
                    templateUrl: intygsTypPath + 'views/utkast/utkast.html',
                    controller: 'lisu.EditCertCtrl'
                },

                'wcHeader@lisu-edit' : {
                    templateUrl: commonPath + 'gui/headers/wcHeader.partial.html',
                    controller: 'common.wcHeaderController'
                },

                'header@lisu-edit' : {
                    templateUrl: commonPath + 'utkast/utkast-header/utkastHeader.html',
                    controller: 'common.UtkastHeader'
                },

                'footer@lisu-edit' : {
                    templateUrl: commonPath + 'utkast/utkast-footer/utkastFooter.html',
                    controller: 'common.UtkastFooter'
                },

                'formly@lisu-edit' : {
                    templateUrl: intygsTypPath + 'views/utkast/formly.html',
                    controller: 'lisu.EditCert.FormlyCtrl'
                },

                'fragasvar@lisu-edit' : {
                    templateUrl: commonPath + 'fk/arenden/arendeListUtkast.html',
                    controller: 'common.ArendeListCtrl'
                }
            }
        }).
        state('webcert.intyg.fk.lisu', {
            data: { defaultActive : 'index', intygType: 'lisu' },
            url:'/intyg/lisu/:certificateId?:patientId&:hospName&:fornamn&:efternamn&:mellannamn&:postadress&:postnummer&:postort&:signed&:sjf',
            views: {
                'intyg@webcert.intyg.fk' : {
                    templateUrl: intygsTypPath + 'views/intyg/intyg.html',
                    controller: 'lisu.ViewCertCtrl'
                },
                'fragasvar@webcert.intyg.fk' : {
                    templateUrl: commonPath + 'fk/arenden/arendeList.html',
                    controller: 'common.ArendeListCtrl'
                },
                'header@webcert.intyg.fk.lisu' : {
                    templateUrl: commonPath + 'intyg/intyg-header/intyg-header.html',
                    controller: 'common.IntygHeader'
                }
            }
        }).
        state('webcert.fragasvar.lisu', {
            data: { defaultActive : 'unhandled-qa', intygType: 'lisu'  },
            url: '/fragasvar/lisu/:certificateId',
            views: {
                'intyg@webcert.fragasvar' : {
                    templateUrl: intygsTypPath + 'views/intyg/intyg.html',
                    controller: 'lisu.ViewCertCtrl'
                },
                'fragasvar@webcert.fragasvar' : {
                    templateUrl: commonPath + 'fk/arenden/arendeList.html',
                    controller: 'common.ArendeListCtrl'
                },
                'header@webcert.fragasvar.lisu' : {
                    templateUrl: commonPath + 'intyg/intyg-header/intyg-header.html',
                    controller: 'common.IntygHeader'
                }
            }
        });
});