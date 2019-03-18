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

angular.module('ag7804').factory('ag7804.UtkastConfigFactory.v1',
    ['$log', '$timeout',
        'common.DateUtilsService', 'common.ueFactoryTemplatesHelper', 'common.ObjectHelper',
        function($log, $timeout, DateUtils, ueFactoryTemplates, ObjectHelper) {
            'use strict';

            function _getCategoryIds() {
                return {
                    1: 'grundformu',
                    2: 'sysselsattning',
                    3: 'diagnos',
                    4: 'funktionsnedsattning',
                    5: 'medicinskaBehandlingar',
                    6: 'bedomning',
                    7: 'atgarder',
                    8: 'ovrigt',
                    9: 'kontakt',
                    10: 'smittbararpenning'
                };
            }
            function isDiagnoseRequired(model) {
                return model.onskarFormedlaDiagnos === true && !ObjectHelper.deepGet(model, 'diagnoser[0].diagnosKod');
            }
            function isFunktionsNedsattningRequired(model) {
                return model.onskarFormedlaFunktionsnedsattning === true && !model.funktionsnedsattning;
            }

            function _getConfig(viewState) {
                var categoryIds = _getCategoryIds();
                console.log(viewState);

                var kategori = ueFactoryTemplates.kategori;
                var fraga = ueFactoryTemplates.fraga;
                var today = moment().format('YYYY-MM-DD');

                var config = [

                    kategori(categoryIds[10], 'KAT_10.RBK', 'KAT_10.HLP', {signingDoctor: true}, [
                        fraga(27, '', '', {}, [{
                            type: 'ue-checkbox',
                            modelProp: 'avstangningSmittskydd',
                            label: {
                                key: 'FRG_27.RBK',
                                helpKey: 'FRG_27.HLP'
                            }
                        }])
                    ]),

                    kategori(categoryIds[1], 'KAT_1.RBK', 'KAT_1.HLP', { hideExpression: 'model.avstangningSmittskydd' }, [
                        fraga(1, 'FRG_1.RBK', 'FRG_1.HLP',
                            {
                                validationContext: {
                                    key: 'baseratPa',
                                    type: 'ue-checkgroup'
                                },
                                required: true,
                                requiredProp: ['undersokningAvPatienten', 'telefonkontaktMedPatienten', 'journaluppgifter', 'annatGrundForMU']
                            },
                            [{
                                type: 'ue-grid',
                                independentRowValidation: true,
                                components: [[{
                                    label: {
                                        key: 'KV_FKMU_0001.UNDERSOKNING.RBK',
                                        helpKey: 'KV_FKMU_0001.UNDERSOKNING.HLP'
                                    },
                                    type: 'ue-checkbox-date',
                                    modelProp: 'undersokningAvPatienten',
                                    maxDate: today
                                }], [{
                                    label: {
                                        key: 'KV_FKMU_0001.TELEFONKONTAKT.RBK',
                                        helpKey: 'KV_FKMU_0001.TELEFONKONTAKT.HLP'
                                    },
                                    type: 'ue-checkbox-date',
                                    modelProp: 'telefonkontaktMedPatienten',
                                    maxDate: today
                                }], [{
                                    label: {
                                        key: 'KV_FKMU_0001.JOURNALUPPGIFTER.RBK',
                                        helpKey: 'KV_FKMU_0001.JOURNALUPPGIFTER.HLP'
                                    },
                                    type: 'ue-checkbox-date',
                                    modelProp: 'journaluppgifter',
                                    maxDate: today
                                }], [{
                                    label: {
                                        key: 'KV_FKMU_0001.ANNAT.RBK',
                                        helpKey: 'KV_FKMU_0001.ANNAT.HLP'
                                    },
                                    type: 'ue-checkbox-date',
                                    modelProp: 'annatGrundForMU',
                                    maxDate: today
                                }], [{
                                    label: {
                                        key: 'DFR_1.3.RBK',
                                        helpKey: 'DFR_1.3.HLP',
                                        required: true,
                                        requiredProp: 'annatGrundForMUBeskrivning'
                                    },
                                    type: 'ue-textarea',
                                    hideExpression: '!model.annatGrundForMU',
                                    modelProp: 'annatGrundForMUBeskrivning'
                                }]]
                            }]
                        )
                    ]),

                    kategori(categoryIds[2], 'KAT_2.RBK', 'KAT_2.HLP', { hideExpression: 'model.avstangningSmittskydd' }, [
                        fraga(28, 'FRG_28.RBK', 'FRG_28.HLP', { required: true, requiredProp: ['sysselsattning["NUVARANDE_ARBETE"]',
                            'sysselsattning["ARBETSSOKANDE"]', 'sysselsattning["FORALDRALEDIG"]','sysselsattning["STUDIER"]']}, [{
                            type: 'ue-checkgroup',
                            modelProp: 'sysselsattning',
                            code: 'KV_FKMU_0002',
                            choices: ['NUVARANDE_ARBETE',
                                'ARBETSSOKANDE',
                                'FORALDRALEDIG',
                                'STUDIER'
                            ]
                        }]), fraga(29, 'FRG_29.RBK', 'FRG_29.HLP', { required: true, hideExpression: '!model.sysselsattning["NUVARANDE_ARBETE"]',
                            requiredProp: 'nuvarandeArbete'}, [{
                            type: 'ue-textarea',
                            modelProp: 'nuvarandeArbete'
                        }])
                    ]),

                    kategori(categoryIds[3], 'KAT_3.RBK', 'KAT_3.HLP', {}, [
                        fraga(100, 'FRG_100.RBK', 'FRG_100.HLP', { required: true, requiredProp: 'onskarFormedlaDiagnos', cssClass: 'yellow-banner'}, [{
                            type: 'ue-radio',
                            modelProp: 'onskarFormedlaDiagnos'
                        }]),
                        fraga(6, 'FRG_6.RBK', 'FRG_6.HLP', {
                            required: true,
                            requiredProp: isDiagnoseRequired,
                            hideExpression: 'model.onskarFormedlaDiagnos === false',
                            hideHelpExpression: 'model.onskarFormedlaDiagnos !== true'
                        }, [{
                            type: 'ue-diagnos',
                            modelProp: 'diagnoser',
                            defaultKodSystem: 'ICD_10_SE',
                            disabledFunc: function(model) {
                                return ObjectHelper.isEmpty(model.onskarFormedlaDiagnos);
                            },
                            diagnosBeskrivningLabel: 'DFR_6.1.RBK',
                            diagnosBeskrivningHelp: 'DFR_6.1.HLP',
                            diagnosKodLabel: 'DFR_6.2.RBK',
                            diagnosKodHelp: 'DFR_6.2.HLP'
                        }])
                    ]),

                    kategori(categoryIds[4], 'KAT_4.RBK', 'KAT_4.HLP', { hideExpression: 'model.avstangningSmittskydd' }, [
                        fraga(101, 'FRG_101.RBK', 'FRG_101.HLP', { required: true, requiredProp: 'onskarFormedlaFunktionsnedsattning', cssClass: 'yellow-banner'}, [{
                            type: 'ue-radio',
                            modelProp: 'onskarFormedlaFunktionsnedsattning'
                        }]),
                        fraga(35, 'FRG_35.RBK', 'FRG_35.HLP', {
                            hideExpression: 'model.onskarFormedlaFunktionsnedsattning === false'
                        }, [
                            {   type: 'ue-form-label',
                                key: 'DFR_35.1.RBK',
                                helpKey: 'DFR_35.1.HLP',
                                required: true,
                                requiredProp: isFunktionsNedsattningRequired,
                                hideHelpExpression: 'model.onskarFormedlaFunktionsnedsattning !== true'
                             },
                             { type: 'ue-textarea',
                                 modelProp: 'funktionsnedsattning',
                                 disabledFunc: function(model) {
                                    return ObjectHelper.isEmpty(model.onskarFormedlaFunktionsnedsattning);
                                 }
                             }
                        ]),
                        fraga(17, 'FRG_17.RBK', 'FRG_17.HLP', {}, [{
                            type: 'ue-textarea',
                            modelProp: 'aktivitetsbegransning',
                            label: {
                                key: 'DFR_17.1.RBK',
                                helpKey: 'DFR_17.1.HLP',
                                required: true,
                                requiredProp: 'aktivitetsbegransning'
                            }
                        }])
                    ]),

                    kategori(categoryIds[5], 'KAT_5.RBK', 'KAT_5.HLP', { hideExpression: 'model.avstangningSmittskydd' }, [
                        fraga(19, 'FRG_19.RBK', 'FRG_19.HLP', {}, [{
                            type: 'ue-textarea',
                            modelProp: 'pagaendeBehandling',
                            label: {
                                key: 'DFR_19.1.RBK',
                                helpKey: 'DFR_19.1.HLP'
                            }
                        }]),
                        fraga(20, 'FRG_20.RBK', 'FRG_20.HLP', {}, [{
                            type: 'ue-textarea',
                            modelProp: 'planeradBehandling',
                            label: {
                                key: 'DFR_20.1.RBK',
                                helpKey: 'DFR_20.1.HLP'
                            }
                        }])
                    ]),

                    kategori(categoryIds[6], 'KAT_6.RBK', 'KAT_6.HLP', {}, [
                        fraga(32, 'FRG_32.RBK', 'FRG_32.HLP', { required: true, requiredProp: ['sjukskrivningar["EN_FJARDEDEL"].period.from', 'sjukskrivningar["HALFTEN"].period.from',
                            'sjukskrivningar["TRE_FJARDEDEL"].period.from', 'sjukskrivningar["HELT_NEDSATT"].period.from'] }, [{
                            type: 'ue-sjukskrivningar',
                            modelProp: 'sjukskrivningar',
                            hoursPerWeek: {
                                labelkey1: 'FRG_32.IFS1.RBK',
                                labelkey2: 'FRG_32.IFS2.RBK',
                                hlpKey: 'FRG_32.IFS.HLP'
                            },
                            code: 'KV_FKMU_0003',
                            fields: [
                                'EN_FJARDEDEL',
                                'HALFTEN',
                                'TRE_FJARDEDEL',
                                'HELT_NEDSATT'
                            ]
                        }]),
                        fraga(37, 'FRG_37.RBK', 'FRG_37.HLP', { hideExpression: 'model.avstangningSmittskydd' }, [{
                            type: 'ue-textarea',
                            modelProp: 'forsakringsmedicinsktBeslutsstod'
                        }]),
                        fraga(33, '', '', { }, [{
                            type: 'ue-radio',
                            modelProp: 'arbetstidsforlaggning',
                            htmlClass: 'more-padding',
                            label: {
                                key: 'FRG_33.RBK',
                                helpKey: 'FRG_33.HLP',
                                labelType: 'h4',
                                required: true,
                                requiredProp: 'arbetstidsforlaggning'
                            },
                            hideExpression: function(scope) {

                                if (scope.model.avstangningSmittskydd) {
                                    return true;
                                }

                                var sjukskrivningar = scope.model.sjukskrivningar;

                                var nedsatt75under = false;
                                angular.forEach(sjukskrivningar, function(item, key) {
                                    if (!nedsatt75under && key !== 'HELT_NEDSATT') {
                                        if (item.period && DateUtils.isDate(item.period.from) &&
                                            DateUtils.isDate(item.period.tom)) {
                                            nedsatt75under = true;
                                        }
                                    }
                                });

                                return !nedsatt75under;
                            }
                        }, {
                            type: 'ue-textarea',
                            modelProp: 'arbetstidsforlaggningMotivering',
                            label: {
                                key: 'DFR_33.2.RBK',
                                helpKey: 'DFR_33.2.HLP',
                                labelType: 'h4',
                                required: true,
                                requiredProp: 'arbetstidsforlaggningMotivering'
                            },
                            hideExpression: function(scope) {
                                if (scope.model.avstangningSmittskydd) {
                                    return true;
                                }
                                return scope.model.arbetstidsforlaggning !== true;
                            }
                        }]),
                        fraga(34, '', '', { hideExpression: 'model.avstangningSmittskydd' }, [{
                            type: 'ue-checkbox',
                            modelProp: 'arbetsresor',
                            label: {
                                key: 'FRG_34.RBK',
                                helpKey: 'FRG_34.HLP'
                            }
                        }]),
                        fraga(39, 'FRG_39.RBK', 'FRG_39.HLP', { required: true, requiredProp: 'prognos.typ', hideExpression: 'model.avstangningSmittskydd' }, [{
                            type: 'ue-prognos',
                            modelProp: 'prognos',
                            code: 'KV_FKMU_0006',
                            choices: [{id: 'STOR_SANNOLIKHET', showDropDown: false},
                                {id: 'ATER_X_ANTAL_DGR', showDropDown: true},
                                {id: 'SANNOLIKT_INTE', showDropDown: false},
                                {id: 'PROGNOS_OKLAR', showDropDown: false}
                            ],
                            prognosDagarTillArbeteCode: 'KV_FKMU_0007',
                            prognosDagarTillArbeteTyper: ['TRETTIO_DGR',
                                'SEXTIO_DGR',
                                'NITTIO_DGR',
                                'HUNDRAATTIO_DAGAR',
                                'TREHUNDRASEXTIOFEM_DAGAR'
                            ]
                        }])
                    ]),

                    kategori(categoryIds[7], 'KAT_7.RBK', 'KAT_7.HLP', { hideExpression: 'model.avstangningSmittskydd' }, [
                        fraga(40, 'FRG_40.RBK', 'FRG_40.HLP', { required: true, requiredProp: ['arbetslivsinriktadeAtgarder["EJ_AKTUELLT"]', 'arbetslivsinriktadeAtgarder["ARBETSTRANING"]',
                            'arbetslivsinriktadeAtgarder["ARBETSANPASSNING"]', 'arbetslivsinriktadeAtgarder["BESOK_ARBETSPLATS"]',
                            'arbetslivsinriktadeAtgarder["ERGONOMISK"]', 'arbetslivsinriktadeAtgarder["HJALPMEDEL"]',
                            'arbetslivsinriktadeAtgarder["KONTAKT_FHV"]', 'arbetslivsinriktadeAtgarder["OMFORDELNING"]', 'arbetslivsinriktadeAtgarder["OVRIGA_ATGARDER"]']}, [{
                            type: 'ue-checkgroup',
                            modelProp: 'arbetslivsinriktadeAtgarder',
                            code: 'KV_FKMU_0004',
                            choices: ['EJ_AKTUELLT',
                                'ARBETSTRANING',
                                'ARBETSANPASSNING',
                                'BESOK_ARBETSPLATS',
                                'ERGONOMISK',
                                'HJALPMEDEL',
                                'KONTAKT_FHV',
                                'OMFORDELNING',
                                'OVRIGA_ATGARDER'
                            ],
                            watcher: [{
                                type: '$watch',
                                watchDeep: true,
                                expression: 'model.arbetslivsinriktadeAtgarder',
                                listener: function(newValue, oldValue, scope) {
                                    if (oldValue && newValue !== oldValue) {
                                        if (newValue.EJ_AKTUELLT !== oldValue.EJ_AKTUELLT) {
                                            if (newValue.EJ_AKTUELLT) {
                                                angular.forEach(scope.model.arbetslivsinriktadeAtgarder,
                                                    function(atgard, key) {
                                                        if (key !== 'EJ_AKTUELLT') {
                                                            scope.model.arbetslivsinriktadeAtgarder[key] = undefined;
                                                        }
                                                    });
                                            }
                                        } else {
                                            angular.forEach(scope.model.arbetslivsinriktadeAtgarder,
                                                function(atgard, key) {
                                                    if (key !== 'EJ_AKTUELLT' && atgard) {
                                                        scope.model.arbetslivsinriktadeAtgarder.EJ_AKTUELLT = undefined;
                                                    }
                                                });
                                        }
                                    }
                                }
                            }]
                        }]),
                        fraga(44, 'FRG_44.RBK', 'FRG_44.HLP', { hideExpression: function(scope) {
                            var hide = true;
                            angular.forEach(scope.model.arbetslivsinriktadeAtgarder, function(atgard, key) {
                                if (atgard === true && key !== 'EJ_AKTUELLT') {
                                    hide = false;
                                    return;
                                }
                            });
                            return hide;
                        }}, [{
                            modelProp: 'arbetslivsinriktadeAtgarderBeskrivning',
                            type: 'ue-textarea'
                        }])
                    ]),

                    kategori(categoryIds[8], 'KAT_8.RBK', 'KAT_8.HLP', {}, [
                        fraga(25, 'FRG_25.RBK', '', { }, [{
                            modelProp: 'ovrigt',
                            type: 'ue-textarea'
                        }])
                    ]),

                    kategori(categoryIds[9], 'KAT_9.RBK', 'KAT_9.HLP', { hideExpression: 'model.avstangningSmittskydd' }, [
                        fraga(103, 'FRG_103.RBK', 'FRG_103.HLP', { }, [{
                            type: 'ue-checkbox',
                            modelProp: 'kontaktMedAg',
                            label: {
                                key: 'DFR_103.1.RBK',
                                helpKey: 'DFR_103.1.HLP'
                            }
                        },{
                            type: 'ue-textarea',
                            modelProp: 'anledningTillKontakt',
                            hideExpression: '!model.kontaktMedAg',
                            label: {
                                labelType: 'h5',
                                key: 'DFR_103.2.RBK',
                                helpKey: 'DFR_103.2.HLP'
                            }
                        }])
                    ]),

                    {
                        type: 'ue-tillaggsfragor'
                    },

                    ueFactoryTemplates.vardenhet
                ];

                return config;
            }


            return {
                getConfig: _getConfig,
                getCategoryIds: _getCategoryIds
            };
        }]);
