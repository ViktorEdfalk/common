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

angular.module('ts-diabetes').factory('ts-diabetes.UtkastConfigFactory',
    ['$log', '$timeout', 'common.ueFactoryTemplatesHelper',
        function($log, $timeout, ueFactoryTemplates) {
            'use strict';

            var categoryIds = {
                99: 'intygavser',
                100: 'identitet',
                1: 'diabetes',
                2: 'hypoglykemier',
                3: 'syn',
                4: 'bedomning'
            };

            var kategori = ueFactoryTemplates.kategori;
            var fraga = ueFactoryTemplates.fraga;

            var tomorrowDate = moment().format('YYYY-MM-DD');
            var minDate = moment().subtract(1, 'y').format('YYYY-MM-DD');

            function korkortHogreBehorighet(scope) {
                if (!scope.model.intygAvser || !scope.model.intygAvser.korkortstyp) {
                    return true;
                }
                var korkortstyp = scope.model.intygAvser.korkortstyp;
                var targetTypes = ['C1', 'C1E', 'C', 'CE', 'D1', 'D1E', 'D', 'DE', 'TAXI'];
                for (var i = 0; i < korkortstyp.length; i++) {
                    for (var j = 0; j < targetTypes.length; j++) {
                        if (korkortstyp[i].type === targetTypes[j] && korkortstyp[i].selected) {
                            return false;
                        }
                    }
                }
                return true;
            }

            var config = [

                ueFactoryTemplates.patient,

                // Intyget avser
                kategori(categoryIds[99], 'KAT_99.RBK', 'KAT_99.HLP', { required: true }, [
                    fraga(null, '', '', {}, [{
                        type: 'ue-checkgroup-ts',
                        modelProp: 'intygAvser.korkortstyp',
                        labelTemplate:'KORKORT_{0}.RBK'
                    }])
                ]),

                // Identitet styrkt genom
                kategori(categoryIds[100], 'KAT_100.RBK', 'KAT_100.HLP', { required: true }, [
                    fraga(null, '', '', {}, [{
                        type: 'ue-radiogroup',
                        modelProp: 'vardkontakt.idkontroll',
                        htmlClass: 'col-md-6',
                        paddingBottom: true,
                        choices: [
                            {label: 'ts-diabetes.label.identitet.id_kort', id: 'ID_KORT'},
                            {label: 'ts-diabetes.label.identitet.foretag_eller_tjanstekort', id: 'FORETAG_ELLER_TJANSTEKORT'},
                            {label: 'ts-diabetes.label.identitet.korkort', id: 'KORKORT'},
                            {label: 'ts-diabetes.label.identitet.pers_kannedom', id: 'PERS_KANNEDOM'},
                            {label: 'ts-diabetes.label.identitet.forsakran_kap18', id: 'FORSAKRAN_KAP18'},
                            {label: 'ts-diabetes.label.identitet.pass', id: 'PASS'}
                        ]
                    },{
                        type: 'ue-alert',
                        alertType: 'info',
                        key: 'FRM_1.RBK'
                    }])
                ]),

                // 1. Allmänt
                kategori(categoryIds[1], 'KAT_1.RBK', 'KAT_1.HLP', { }, [
                    fraga(35, 'FRG_35.RBK', 'FRG_35.HLP', { required:true }, [{
                        type: 'ue-year',
                        modelProp: 'diabetes.observationsperiod'
                    }]),
                    fraga(18, 'FRG_18.RBK', 'FRG_18.HLP', { required:true }, [{
                        type: 'ue-radiogroup',
                        modelProp: 'diabetes.diabetestyp',
                        choices: [
                            {label: 'ts-diabetes.label.diabets.typ1', id: 'DIABETES_TYP_1'},
                            {label: 'ts-diabetes.label.diabets.typ2', id: 'DIABETES_TYP_2'}
                        ]
                    }]),
                    fraga(19, 'FRG_19.RBK', 'FRG_19.HLP', {
                        validationContext: {key: 'diabetes.behandlingsTyp', type: 'checkgroup'},
                        required:true }, [{
                            type: 'ue-checkbox',
                            modelProp: 'diabetes.endastKost',
                            label: {
                                key: 'DFR_19.1.RBK'
                            },
                            paddingBottom: true
                        },{
                            type: 'ue-checkbox',
                            modelProp: 'diabetes.tabletter',
                            label: {
                                key: 'DFR_19.2.RBK'
                            },
                            paddingBottom: true
                        },{
                            type: 'ue-checkbox',
                            modelProp: 'diabetes.insulin',
                            label: {
                                key: 'DFR_19.3.RBK'
                            },
                            paddingBottom: true
                        },{
                            type: 'ue-year',
                            modelProp: 'diabetes.insulinBehandlingsperiod',
                            hideExpression: '!model.diabetes.insulin',
                            label: {
                                key: 'DFR_19.4.RBK',
                                required: true
                            },
                            paddingBottom: true
                        },{
                            type: 'ue-textfield',
                            modelProp: 'diabetes.annanBehandlingBeskrivning',
                            label: {
                                key: 'DFR_19.5.RBK'
                            }
                    }])
                ]),

                // 2. Hypoglykemier
                kategori(categoryIds[2], 'KAT_2.RBK', 'KAT_2.HLP', { }, [
                    fraga(36, 'FRG_36.RBK', 'FRG_36.HLP', { required:true }, [{
                        type: 'ue-radio',
                        modelProp: 'hypoglykemier.kunskapOmAtgarder'
                    }]),
                    fraga(37, 'FRG_37.RBK', 'FRG_37.HLP', { required:true }, [{
                        type: 'ue-radio',
                        modelProp: 'hypoglykemier.teckenNedsattHjarnfunktion'
                    }]),
                    fraga(38, 'FRG_38.RBK', 'FRG_38.HLP', { required:true, hideExpression:'!model.hypoglykemier.teckenNedsattHjarnfunktion' }, [{
                        type: 'ue-radio',
                        modelProp: 'hypoglykemier.saknarFormagaKannaVarningstecken'
                    }]),
                    fraga(39, 'FRG_39.RBK', 'FRG_39.HLP', { required:true, hideExpression:'!model.hypoglykemier.teckenNedsattHjarnfunktion' }, [{
                        type: 'ue-radio',
                        modelProp: 'hypoglykemier.allvarligForekomst'
                    },{
                        type: 'ue-textfield',
                        modelProp: 'hypoglykemier.allvarligForekomstBeskrivning',
                        hideExpression:'!model.hypoglykemier.allvarligForekomst',
                        label: {
                            key: 'DFR_39.2.RBK',
                            required: true
                        }
                    }]),
                    fraga(40, 'FRG_40.RBK', 'FRG_40.HLP', { required:true, hideExpression:'!model.hypoglykemier.teckenNedsattHjarnfunktion' }, [{
                        type: 'ue-radio',
                        modelProp: 'hypoglykemier.allvarligForekomstTrafiken'
                    },{
                        type: 'ue-textfield',
                        modelProp: 'hypoglykemier.allvarligForekomstTrafikBeskrivning',
                        hideExpression:'!model.hypoglykemier.allvarligForekomstTrafiken',
                        label: {
                            key: 'DFR_40.2.RBK',
                            required: true
                        }
                    }]), {
                        type: 'ue-group',
                        hideExpression: korkortHogreBehorighet,
                        components: [
                        fraga(null, 'ts-diabetes.helptext.hypoglykemier.korkortd', '', {required: true}, []),
                        fraga(41, 'FRG_41.RBK', 'FRG_41.HLP', {required: true}, [{
                            type: 'ue-radio',
                            modelProp: 'hypoglykemier.egenkontrollBlodsocker'
                        }]),
                        fraga(42, 'FRG_42.RBK', 'FRG_42.HLP', {required: true}, [{
                            type: 'ue-radio',
                            modelProp: 'hypoglykemier.allvarligForekomstVakenTid'
                        }, {
                            type: 'ue-date',
                            modelProp: 'hypoglykemier.allvarligForekomstVakenTidObservationstid',
                            hideExpression: '!model.hypoglykemier.allvarligForekomstVakenTid',
                            minDate: minDate,
                            maxDate: tomorrowDate,
                            label: {
                                key: 'DFR_42.2.RBK',
                                required: true
                            }
                        }, {
                            type: 'ue-text',
                            hideExpression: '!model.hypoglykemier.allvarligForekomstVakenTid',
                            label: {
                                key: 'ts-diabetes.helptext.hypoglykemier.date'
                            }
                        }])]
                    }
                ]),

                // 3. Syn
                kategori(categoryIds[3], 'KAT_3.RBK', 'KAT_3.HLP', { }, [
                    fraga(null, '', '', {}, [{
                        type: 'ue-text',
                        label: {
                            type: 'h5',
                            htmlClass: 'inline-heading',
                            key: 'ts-diabetes.helptext.syn.alt1.heading'
                        }
                    }, {
                        type: 'ue-text',
                        label: {
                            key: 'ts-diabetes.helptext.syn.alt1.text'
                        }
                    }]),
                    fraga(43, 'FRG_43.RBK', 'FRG_43.HLP', { required:true }, [{
                        type: 'ue-radio',
                        modelProp: 'syn.separatOgonlakarintyg'
                    }]), {
                        type: 'ue-group',
                        hideExpression: 'model.syn.separatOgonlakarintyg',
                        components: [
                        fraga(null, '', '', {}, [{
                            type: 'ue-text',
                            label: {
                                type: 'h5',
                                htmlClass: 'inline-heading',
                                key: 'ts-diabetes.helptext.syn.alt2.heading'
                            }
                        }, {
                            type: 'ue-text',
                            label: {
                                key: 'ts-diabetes.helptext.syn.alt2.text'
                            }
                        }]),
                        fraga(44, 'FRG_44.RBK', 'FRG_44.HLP', {required: true}, [{
                            type: 'ue-radio',
                            modelProp: 'syn.synfaltsprovningUtanAnmarkning'
                        }]),
                        fraga(8, 'FRG_8.RBK', 'FRG_8.HLP', { required: true}, [{
                            type: 'ue-grid',
                            components: [
                                // Row 1
                                [{
                                },{
                                    type: 'ue-form-label',
                                    key: 'ts-diabetes.label.syn.utankorrektion',
                                    helpKey: 'ts-diabetes.helptext.synfunktioner.utan-korrektion',
                                    required: true
                                },{
                                    type: 'ue-form-label',
                                    key: 'ts-diabetes.label.syn.medkorrektion',
                                    helpKey: 'ts-diabetes.helptext.synfunktioner.med-korrektion'
                                }],
                                // Row 2
                                [{
                                    type: 'ue-text',
                                    label: {
                                        key: 'ts-diabetes.label.syn.hogeroga'
                                    }
                                },{
                                    type: 'ue-synskarpa',
                                    modelProp: 'syn.hoger.utanKorrektion'
                                },{
                                    type: 'ue-synskarpa',
                                    modelProp: 'syn.hoger.medKorrektion'
                                }],
                                // Row 3
                                [{
                                    type: 'ue-text',
                                    label: {
                                        key: 'ts-diabetes.label.syn.vansteroga'
                                    }
                                },{
                                    type: 'ue-synskarpa',
                                    modelProp: 'syn.vanster.utanKorrektion'
                                },{
                                    type: 'ue-synskarpa',
                                    modelProp: 'syn.vanster.medKorrektion'
                                }],
                                // Row 4
                                [{
                                    type: 'ue-text',
                                    label: {
                                        key: 'ts-diabetes.label.syn.binokulart'
                                    }
                                },{
                                    type: 'ue-synskarpa',
                                    modelProp: 'syn.binokulart.utanKorrektion'
                                },{
                                    type: 'ue-synskarpa',
                                    modelProp: 'syn.binokulart.medKorrektion'
                                }]
                            ]

                        }]), fraga(6, 'FRG_6.RBK', 'FRG_6.HLP', {required: true}, [{
                            type: 'ue-radio',
                            modelProp: 'syn.diplopi'
                        }])]
                    }
                ]),

                // 4. Bedömning
                kategori(categoryIds[4], 'KAT_4.RBK', 'KAT_4.HLP', { }, [
                    fraga(33, 'FRG_33.RBK', 'FRG_33.HLP', { required:true }, [{
                        type: 'ue-korkort-bedomning',
                        modelProp: 'bedomning',
                        labelTemplate:'KORKORT_{0}.RBK'
                    }]),
                    fraga(45, 'FRG_45.RBK', 'FRG_45.HLP', { required:true, hideExpression: korkortHogreBehorighet }, [{
                        type: 'ue-radio',
                        modelProp: 'bedomning.lamplighetInnehaBehorighet'
                    }])
                ]),

                kategori(categoryIds[5], '', '', { }, [
                    fraga(32, 'FRG_32.RBK', 'FRG_32.HLP', {}, [{
                        type: 'ue-textarea',
                        modelProp: 'kommentar',
                        maxlength: 189
                    }])
                ]),

                kategori(categoryIds[6], '', '', { }, [
                    fraga(34, 'FRG_34.RBK', 'FRG_34.HLP', {}, [{
                        type: 'ue-textarea',
                        modelProp: 'bedomning.lakareSpecialKompetens',
                        maxlength: 71
                    }])
                ]),

                ueFactoryTemplates.vardenhet/*,

    Befattning and specialitet was present in code but not working in 5.4
                kategori(null, '', '', {}, [
                    fraga(null, '', '', {}, [{
                        type: 'ue-befattning-specialitet'
                    }])
                ])
*/
            ];

            return {
                getConfig: function() {
                    return angular.copy(config);
                },
                getCategoryNames: function() {
                    return angular.copy(categoryIds);
                },
                getCategoryIds: function() {
                    return angular.copy(categoryIds);
                }
            };
        }]);