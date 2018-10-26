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
angular.module('doi').service('doi.EditCertCtrl.ViewStateService.v1',
    ['$log', '$state', 'common.UtkastViewStateService', 'doi.Domain.IntygModel.v1',
        function($log, $state, CommonViewState, IntygModel) {
            'use strict';

            this.common = CommonViewState;

            this.intygModel = undefined;
            this.draftModel = undefined;

            this.setDraftModel = function(draftModel){
                this.draftModel = draftModel;
                this.intygModel = draftModel.content;
            };

            this.inputLimits = {
                arbetsformagaPrognos: 600,
                nuvarandeArbetsuppgifter: 120,
                atgardInomSjukvarden: 66,
                annanAtgard: 66,
                aktivitetsbegransning: 1100,
                funktionsnedsattning: 450,
                sjukdomsforlopp: 270,
                diagnosBeskrivning :220,
                ovrigt: 360,
                finishedBehandling: 999,
                pagaendeBehandling: 999,
                planeradBehandling: 999
            };

            this.reset = function() {
                CommonViewState.reset();
                CommonViewState.intyg.type = 'doi';
                this.setDraftModel(IntygModel._members.build());
                return this;
            };

            this.clearModel = function(){
                this.intygModel = undefined;
                this.draftModel = undefined;
            };

            this.getCopyDraftAlert = function() {
                var intygsTyp = 'doi';

                var previousIntyg = this.common.previousIntyg[intygsTyp];
                var previousUtkast = this.common.previousUtkast[intygsTyp];

                if (previousUtkast && !previousUtkast.sameVardgivare) {
                    return intygsTyp + '.warn.previousdraft.differentvg';
                }

                if (previousIntyg && !previousIntyg.sameVardgivare) {
                    return intygsTyp + '.warn.previouscertificate.differentvg';
                }

                return null;
            };

            this.getLockedDraftAlert = function() {
                var intygsTyp = 'doi';

                var previousIntyg = this.common.previousIntyg[intygsTyp];
                var previousUtkast = this.common.previousUtkast[intygsTyp];

                if (previousUtkast && previousUtkast.sameVardgivare) {
                    if (!previousUtkast.sameEnhet) {
                        return [{
                            severity: 'info',
                            id: 'intyg-previousutkast-warning-' + intygsTyp,
                            text: intygsTyp + '.warn.previousdraft.samevg.differentenhet',
                            params: {
                                enhetName: previousUtkast.enhetName
                            }
                        }];
                    }

                    return [{
                        severity: 'info',
                        id: 'intyg-previousutkast-warning-' + intygsTyp,
                        text: intygsTyp + '.warn.previousdraft.samevg'
                    }];
                }

                if (previousIntyg && previousIntyg.sameVardgivare) {
                    if (!previousIntyg.sameEnhet) {
                        return [{
                            severity: 'info',
                            id: 'intyg-previousintyg-warning-' + intygsTyp,
                            text: intygsTyp + '.warn.previouscertificate.samevg.differentenhet',
                            params: {
                                enhetName: previousIntyg.enhetName
                            }
                        }];
                    }

                    return [{
                        severity: 'info',
                        id: 'intyg-previousintyg-warning-' + intygsTyp,
                        text: intygsTyp + '.warn.previouscertificate.samevg'
                    }];
                }

                return [];
            };

            this.reset();
        }]);