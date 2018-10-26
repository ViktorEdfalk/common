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

angular.module('ts-bas').factory('ts-bas.UtkastConfigFactory.v6',
    ['$log', '$timeout', 'common.ueFactoryTemplatesHelper', 'common.ueTSFactoryTemplatesHelper', 'common.UtilsService',
        function($log, $timeout, ueFactoryTemplates, ueTSFactoryTemplates, u) {
            'use strict';

            function _getCategoryIds() {
                return {
                    99: 'intygavser',
                    100: 'identitet',
                    1: 'syn',
                    2: 'horselbalans',
                    3: 'funktionsnedsattning',
                    4: 'hjartkarl',
                    5: 'diabetes',
                    6: 'neurologi',
                    7: 'medvetandestorning',
                    8: 'njurar',
                    9: 'kognitivt',
                    10: 'somnvakenhet',
                    11: 'narkotikalakemedel',
                    12: 'psykiskt',
                    13: 'utvecklingsstorning',
                    14: 'sjukhusvard',
                    15: 'medicinering',
                    16: 'ovrigt',
                    101: 'bedomning'
                };
            }

            function _getConfig(viewState) {
                var categoryIds = _getCategoryIds();

                var kategori = ueFactoryTemplates.kategori;
                var fraga = ueFactoryTemplates.fraga;
                var patient = ueTSFactoryTemplates.patient(viewState);

                function korkortHogreBehorighet(scope) {
                    if (!scope.model.intygAvser || !scope.model.intygAvser.korkortstyp) {
                        return true;
                    }
                    var korkortstyp = scope.model.intygAvser.korkortstyp;
                    var targetTypes = ['D1', 'D1E', 'D', 'DE', 'TAXI'];
                    for (var i = 0; i < korkortstyp.length; i++) {
                        for (var j = 0; j < targetTypes.length; j++) {
                            if (korkortstyp[i].type === targetTypes[j] && korkortstyp[i].selected) {
                                return false;
                            }
                        }
                    }
                    return true;
                }

                function requiredKorkortProperties(field, extraproperty) {
                    var antalKorkort = 11;
                    var korkortsarray = [];
                    for (var i = 0; i < antalKorkort; i++) {
                        korkortsarray.push( field + '.korkortstyp[' + i + '].selected');
                    }
                    korkortsarray.push(extraproperty);
                    return korkortsarray;
                }

                function getNumberOfSelectedKortkortstyper(model) {
                    var numberOfSelected = 0;
                    for (var i = 0; i < model.intygAvser.korkortstyp.length; i++) {
                        if(model.intygAvser.korkortstyp[i].selected) {
                            numberOfSelected++;
                        }
                    }
                    return numberOfSelected;
                }
                function isAnnatSelected(model) {
                    for (var i = 0; i < model.intygAvser.korkortstyp.length; i++) {
                        if (model.intygAvser.korkortstyp[i].type === 'ANNAT' && model.intygAvser.korkortstyp[i].selected) {
                            return true;
                        }
                    }
                    return false;
                } 

                function korrektionRequired(model) {
                    var antalIntyg = getNumberOfSelectedKortkortstyper(model);

                    if((antalIntyg > 0 && !(antalIntyg === 1 && isAnnatSelected(model)) &&
                        ((model.syn.hogerOga.utanKorrektion < '0.8' &&
                        model.syn.vansterOga.utanKorrektion < '0.8') || 
                        (model.syn.hogerOga.utanKorrektion < '0.1' ||
                        model.syn.vansterOga.utanKorrektion < '0.1'))) ||
                        (antalIntyg > 0 && isAnnatSelected(model) &&
                        model.syn.binokulart.utanKorrektion < '0.5')) {
                        if (!model.syn.hogerOga.medKorrektion ||
                            !model.syn.vansterOga.medKorrektion ||
                            !model.syn.binokulart.medKorrektion ) {
                                return true;
                            }
                    }
                    return false;
                }

                var noKravYtterligareUnderlagFieldsFilledExpression = '!(' +
                    'model.syn.synfaltsdefekter === true || '+
                    'model.syn.nattblindhet === true || '+
                    'model.syn.progressivOgonsjukdom === true || '+
                    'model.syn.diplopi === true || '+
                    'model.syn.nystagmus === true || '+
                    'model.horselBalans.balansrubbningar === true || '+
                    'model.horselBalans.svartUppfattaSamtal4Meter === true || '+
                    'model.funktionsnedsattning.funktionsnedsattning === true || '+
                    'model.funktionsnedsattning.otillrackligRorelseformaga === true || '+
                    'model.hjartKarl.hjartKarlSjukdom === true || '+
                    'model.hjartKarl.hjarnskadaEfterTrauma === true || '+
                    'model.hjartKarl.riskfaktorerStroke === true || '+
                    'model.diabetes.harDiabetes === true ||  '+
                    'model.neurologi.neurologiskSjukdom === true || '+
                    'model.medvetandestorning.medvetandestorning === true || '+
                    'model.njurar.nedsattNjurfunktion === true || '+
                    'model.kognitivt.sviktandeKognitivFunktion === true || '+
                    'model.somnVakenhet.teckenSomnstorningar === true || '+
                    'model.narkotikaLakemedel.teckenMissbruk === true || '+
                    'model.narkotikaLakemedel.foremalForVardinsats === true || '+
                    'model.narkotikaLakemedel.provtagningBehovs === true || '+
                    'model.narkotikaLakemedel.lakarordineratLakemedelsbruk || '+
                    'model.psykiskt.psykiskSjukdom === true || '+
                    'model.utvecklingsstorning.psykiskUtvecklingsstorning === true || '+
                    'model.utvecklingsstorning.harSyndrom === true || '+
                    'model.sjukhusvard.sjukhusEllerLakarkontakt === true || '+
                    'model.medicinering.stadigvarandeMedicinering === true)';

                var config = [

                    patient,

                    // Intyget avser
                    kategori(categoryIds[99], 'KAT_99.RBK', 'KAT_99.HLP', {}, [
                        fraga(1, 'FRG_1.RBK', 'FRG_1.HLP', {required: true, requiredProp: requiredKorkortProperties('intygAvser')}, [{
                            type: 'ue-checkgroup-ts',
                            modelProp: 'intygAvser.korkortstyp',
                            labelTemplate:'KORKORT.{0}.RBK',
                            label: {
                                key: 'FRG_1.2.RBK'
                            }
                        }])
                    ]),

                    // Identitet styrkt genom
                    kategori(categoryIds[100], 'KAT_100.RBK', 'KAT_100.HLP', {}, [
                        fraga(2, 'FRG_2.RBK', 'FRG_2.HLP', {required: true, requiredProp: 'vardkontakt.idkontroll'}, [{
                            type: 'ue-radiogroup',
                            modelProp: 'vardkontakt.idkontroll',
                            htmlClass: 'col-md-6 no-padding',
                            paddingBottom: true,
                            choices: [
                                {label: 'ts-bas.label.identitet.id_kort', id: 'ID_KORT'},
                                {label: 'ts-bas.label.identitet.foretag_eller_tjanstekort', id: 'FORETAG_ELLER_TJANSTEKORT'},
                                {label: 'ts-bas.label.identitet.korkort', id: 'KORKORT'},
                                {label: 'ts-bas.label.identitet.pers_kannedom', id: 'PERS_KANNEDOM'},
                                {label: 'ts-bas.label.identitet.forsakran_kap18', id: 'FORSAKRAN_KAP18'},
                                {label: 'ts-bas.label.identitet.pass', id: 'PASS'}
                            ]
                        }])
                    ]),

                    // 1. Synfunktioner
                    kategori(categoryIds[1], 'KAT_1.RBK', 'KAT_1.HLP', { }, [
                        fraga(3, 'FRG_3.RBK', 'FRG_3.HLP', { required:true, requiredProp: 'syn.synfaltsdefekter' }, [{
                            type: 'ue-radio',
                            modelProp: 'syn.synfaltsdefekter'
                        }]),
                        fraga(4, 'FRG_4.RBK', 'FRG_4.HLP', { required:true, requiredProp: 'syn.nattblindhet' }, [{
                            type: 'ue-radio',
                            modelProp: 'syn.nattblindhet'
                        }]),
                        fraga(5, 'FRG_5.RBK', 'FRG_5.HLP', { required:true, requiredProp: 'syn.progressivOgonsjukdom' }, [{
                            type: 'ue-radio',
                            modelProp: 'syn.progressivOgonsjukdom'
                        }]),
                        fraga(null, '', '', {hideExpression: '!(model.syn.progressivOgonsjukdom || model.syn.nattblindhet || model.syn.synfaltsdefekter)'}, [{
                            type: 'ue-alert',
                            alertType: 'warning',
                            key: 'FRG_3-5.INF'
                        }]),
                        fraga(6, 'FRG_6.RBK', 'FRG_6.HLP', { required:true, requiredProp: 'syn.diplopi' }, [{
                            type: 'ue-radio',
                            modelProp: 'syn.diplopi'
                        }]),
                        fraga(7, 'FRG_7.RBK', 'FRG_7.HLP', { required:true, requiredProp: 'syn.nystagmus' }, [{
                            type: 'ue-radio',
                            modelProp: 'syn.nystagmus'
                        }]),
                        fraga(8, 'FRG_8.RBK', 'FRG_8.HLP', {}, [{
                            type: 'ue-grid',
                            components: [
                                // Row 1
                                [{
                                },{
                                    type: 'ue-form-label',
                                    key: 'ts-bas.label.syn.utankorrektion',
                                    required: true,
                                    requiredMode: 'AND',
                                    requiredProp: ['syn.hogerOga.utanKorrektion', 'syn.vansterOga.utanKorrektion', 'syn.binokulart.utanKorrektion']
                                },{
                                    type: 'ue-form-label',
                                    key: 'ts-bas.label.syn.medkorrektion',
                                    required: true,
                                    requiredProp: korrektionRequired
                                },{
                                    type: 'ue-form-label',
                                    key: 'ts-bas.label.syn.kontaktlinster'
                                }],
                                // Row 2
                                [{
                                    type: 'ue-text',
                                    label: {
                                        key: 'ts-bas.label.syn.hogeroga'
                                    }
                                },{
                                    type: 'ue-synskarpa',
                                    modelProp: 'syn.hogerOga.utanKorrektion'
                                },{
                                    type: 'ue-synskarpa',
                                    modelProp: 'syn.hogerOga.medKorrektion'
                                },{
                                    type: 'ue-checkbox',
                                    modelProp: 'syn.hogerOga.kontaktlins'
                                }],
                                // Row 3
                                [{
                                    type: 'ue-text',
                                    label: {
                                        key: 'ts-bas.label.syn.vansteroga'
                                    }
                                },{
                                    type: 'ue-synskarpa',
                                    modelProp: 'syn.vansterOga.utanKorrektion'
                                },{
                                    type: 'ue-synskarpa',
                                    modelProp: 'syn.vansterOga.medKorrektion'
                                },{
                                    type: 'ue-checkbox',
                                    modelProp: 'syn.vansterOga.kontaktlins'
                                }],
                                // Row 4
                                [{
                                    type: 'ue-text',
                                    label: {
                                        key: 'ts-bas.label.syn.binokulart'
                                    }
                                },{
                                    type: 'ue-synskarpa',
                                    modelProp: 'syn.binokulart.utanKorrektion'
                                },{
                                    type: 'ue-synskarpa',
                                    modelProp: 'syn.binokulart.medKorrektion'
                                },{
                                }]
                            ]
                        },{
                            type: 'ue-alert',
                            alertType: 'info',
                            key: 'FRG_8.INF'
                        }]),
                        fraga(9, 'FRG_9.RBK', '', { }, [{
                            type: 'ue-checkbox',
                            label: {
                                key: 'FRG_9.1.RBK',
                                helpKey: 'FRG_9.HLP'
                            },
                            modelProp: 'syn.korrektionsglasensStyrka',
                            paddingBottom: true
                        },{
                            type: 'ue-alert',
                            alertType: 'warning',
                            key: 'FRG_9.INF',
                            hideExpression: '!model.syn.korrektionsglasensStyrka'
                        }])
                    ]),

                    // 2. Hörsel och balanssinne
                    kategori(categoryIds[2], 'KAT_2.RBK', 'KAT_2.HLP', { }, [
                        fraga(10, 'FRG_10.RBK', 'FRG_10.HLP', { required:true, requiredProp: 'horselBalans.balansrubbningar' }, [{
                            type: 'ue-radio',
                            modelProp: 'horselBalans.balansrubbningar'
                        }]),
                        fraga(11, 'FRG_11.RBK', 'FRG_11.HLP', { required:true, hideExpression: korkortHogreBehorighet, requiredProp: 'horselBalans.svartUppfattaSamtal4Meter' }, [{
                            type: 'ue-radio',
                            modelProp: 'horselBalans.svartUppfattaSamtal4Meter'
                        }])
                    ]),

                    // 3. Rörelseorganens funktioner
                    kategori(categoryIds[3], 'KAT_3.RBK', 'KAT_3.HLP', { }, [
                        fraga(12, 'FRG_12.RBK', 'FRG_12.HLP', { required:true, requiredProp: 'funktionsnedsattning.funktionsnedsattning' }, [{
                            type: 'ue-radio',
                            modelProp: 'funktionsnedsattning.funktionsnedsattning'
                        },{
                            type: 'ue-textarea',
                            modelProp: 'funktionsnedsattning.beskrivning',
                            hideExpression: '!model.funktionsnedsattning.funktionsnedsattning',
                            htmlMaxlength: 180,
                            rows: 3,
                            label: {
                                key: 'DFR_12.2.RBK',
                                required: true,
                                requiredProp: 'funktionsnedsattning.beskrivning'
                            }
                        }]),
                        fraga(13, 'FRG_13.RBK', 'FRG_13.HLP', { required:true, hideExpression: korkortHogreBehorighet,
                            requiredProp: 'funktionsnedsattning.otillrackligRorelseformaga' }, [{
                            type: 'ue-radio',
                            modelProp: 'funktionsnedsattning.otillrackligRorelseformaga'
                        }])
                    ]),

                    // 4. Hjärt- och kärlsjukdomar
                    kategori(categoryIds[4], 'KAT_4.RBK', 'KAT_4.HLP', { }, [
                        fraga(14, 'FRG_14.RBK', 'FRG_14.HLP', { required:true, requiredProp: 'hjartKarl.hjartKarlSjukdom' }, [{
                            type: 'ue-radio',
                            modelProp: 'hjartKarl.hjartKarlSjukdom'
                        }]),
                        fraga(15, 'FRG_15.RBK', 'FRG_15.HLP', { required:true, requiredProp: 'hjartKarl.hjarnskadaEfterTrauma'}, [{
                            type: 'ue-radio',
                            modelProp: 'hjartKarl.hjarnskadaEfterTrauma'
                        }]),
                        fraga(16, 'FRG_16.RBK', 'FRG_16.HLP', { required:true, requiredProp: 'hjartKarl.riskfaktorerStroke' }, [{
                            type: 'ue-radio',
                            modelProp: 'hjartKarl.riskfaktorerStroke'
                        },{
                            type: 'ue-textarea',
                            modelProp: 'hjartKarl.beskrivningRiskfaktorer',
                            hideExpression: '!model.hjartKarl.riskfaktorerStroke',
                            htmlMaxlength: 180,
                            rows: 3,
                            label: {
                                key: 'DFR_16.2.RBK',
                                required: true,
                                requiredProp: 'hjartKarl.beskrivningRiskfaktorer'
                            }
                        }])
                    ]),

                    // 5. Diabetes
                    kategori(categoryIds[5], 'KAT_5.RBK', 'KAT_5.HLP', { }, [
                        fraga(17, 'FRG_17.RBK', 'FRG_17.HLP', { required:true, requiredProp: 'diabetes.harDiabetes' }, [{
                            type: 'ue-radio',
                            modelProp: 'diabetes.harDiabetes'
                        }]),
                        fraga(18, 'FRG_18.RBK', 'FRG_18.HLP', { required:true, hideExpression:'!model.diabetes.harDiabetes', requiredProp: 'diabetes.diabetesTyp' }, [{
                            type: 'ue-radiogroup',
                            modelProp: 'diabetes.diabetesTyp',
                            choices: [
                                {label: 'ts-bas.label.diabetes.diabetestyp.diabetes_typ_1', id: 'DIABETES_TYP_1'},
                                {label: 'ts-bas.label.diabetes.diabetestyp.diabetes_typ_2', id: 'DIABETES_TYP_2'}
                            ]
                        }]),
                        fraga(19, 'FRG_19.RBK', 'FRG_19.HLP', {
                                validationContext: {key: 'diabetes.behandlingsTyp', type: 'ue-checkgroup'},
                                required: true,
                                requiredProp: ['diabetes.kost', 'diabetes.tabletter', 'diabetes.insulin'],
                                hideExpression:'model.diabetes.diabetesTyp != "DIABETES_TYP_2"' },
                            [{
                                type: 'ue-checkbox',
                                modelProp: 'diabetes.kost',
                                label: {
                                    key: 'DFR_19.1.RBK'
                                }
                            },{
                                type: 'ue-checkbox',
                                modelProp: 'diabetes.tabletter',
                                label: {
                                    key: 'DFR_19.2.RBK'
                                }
                            },{
                                type: 'ue-checkbox',
                                modelProp: 'diabetes.insulin',
                                label: {
                                    key: 'DFR_19.3.RBK'
                                }
                            },{
                                type: 'ue-alert',
                                alertType: 'warning',
                                key: 'DFR_19.3.INF',
                                hideExpression: '!(model.diabetes.tabletter || model.diabetes.insulin)'
                            }])
                    ]),

                    // 6. Neurologiska sjukdomar
                    kategori(categoryIds[6], 'KAT_6.RBK', 'KAT_6.HLP', { }, [
                        fraga(20, 'FRG_20.RBK', 'FRG_20.HLP', { required:true, requiredProp: 'neurologi.neurologiskSjukdom' }, [{
                            type: 'ue-radio',
                            modelProp: 'neurologi.neurologiskSjukdom'
                        }])
                    ]),

                    // 7. Epilepsi, epileptiskt anfall och annan medvetandestörning
                    kategori(categoryIds[7], 'KAT_7.RBK', 'KAT_7.HLP', { }, [
                        fraga(21, 'FRG_21.RBK', 'FRG_21.HLP', { required:true, requiredProp: 'medvetandestorning.medvetandestorning' }, [{
                            type: 'ue-radio',
                            modelProp: 'medvetandestorning.medvetandestorning'
                        },{
                            type: 'ue-textarea',
                            modelProp: 'medvetandestorning.beskrivning',
                            hideExpression: '!model.medvetandestorning.medvetandestorning',
                            htmlMaxlength: 180,
                            rows: 3,
                            label: {
                                key: 'DFR_21.2.RBK'
                            }
                        }])
                    ]),

                    // 8. Njursjukdomar
                    kategori(categoryIds[8], 'KAT_8.RBK', 'KAT_8.HLP', { }, [
                        fraga(22, 'FRG_22.RBK', 'FRG_22.HLP', { required:true, requiredProp: 'njurar.nedsattNjurfunktion'}, [{
                            type: 'ue-radio',
                            modelProp: 'njurar.nedsattNjurfunktion'
                        }])
                    ]),

                    // 9. Demens och andra kognitiva störningar
                    kategori(categoryIds[9], 'KAT_9.RBK', 'KAT_9.HLP', { }, [
                        fraga(23, 'FRG_23.RBK', 'FRG_23.HLP', { required:true, requiredProp: 'kognitivt.sviktandeKognitivFunktion'}, [{
                            type: 'ue-radio',
                            modelProp: 'kognitivt.sviktandeKognitivFunktion'
                        }])
                    ]),

                    // 10. Sömn- och vakenhetsstörningar
                    kategori(categoryIds[10], 'KAT_10.RBK', 'KAT_10.HLP', { }, [
                        fraga(24, 'FRG_24.RBK', 'FRG_24.HLP', { required:true, requiredProp: 'somnVakenhet.teckenSomnstorningar' }, [{
                            type: 'ue-radio',
                            modelProp: 'somnVakenhet.teckenSomnstorningar'
                        }])
                    ]),

                    // 11. Alkohol, narkotika och läkemedel
                    kategori(categoryIds[11], 'KAT_11.RBK', 'KAT_11.HLP', { }, [
                        fraga(25, 'DFR_25.1.RBK', '', { required:true, requiredProp: 'narkotikaLakemedel.teckenMissbruk' }, [{
                            type: 'ue-radio',
                            modelProp: 'narkotikaLakemedel.teckenMissbruk'
                        }]),
                        fraga(25, 'DFR_25.2.RBK', '', { required:true, requiredProp: 'narkotikaLakemedel.foremalForVardinsats' }, [{
                            type: 'ue-radio',
                            modelProp: 'narkotikaLakemedel.foremalForVardinsats'
                        }]),
                        fraga(25, 'DFR_25.3.RBK', '', { required:true, hideExpression: '!(model.narkotikaLakemedel.teckenMissbruk || model.narkotikaLakemedel.foremalForVardinsats)',
                            requiredProp: 'narkotikaLakemedel.provtagningBehovs'}, [{
                            type: 'ue-radio',
                            modelProp: 'narkotikaLakemedel.provtagningBehovs',
                            paddingBottom: true
                        },{
                            type: 'ue-alert',
                            alertType: 'warning',
                            key: 'DFR_25.3.INF',
                            hideExpression: '!model.narkotikaLakemedel.provtagningBehovs'
                        }]),
                        fraga(26, 'FRG_26.RBK', 'FRG_26.HLP', { required:true, requiredProp: 'narkotikaLakemedel.lakarordineratLakemedelsbruk' }, [{
                            type: 'ue-radio',
                            modelProp: 'narkotikaLakemedel.lakarordineratLakemedelsbruk',
                            paddingBottom: true
                        },{
                            type: 'ue-textarea',
                            modelProp: 'narkotikaLakemedel.lakemedelOchDos',
                            hideExpression: '!model.narkotikaLakemedel.lakarordineratLakemedelsbruk',
                            htmlMaxlength: 180,
                            rows: 3,
                            label: {
                                key: 'DFR_26.1.RBK',
                                required: true,
                                requiredProp: 'narkotikaLakemedel.lakemedelOchDos'
                            }
                        }])
                    ]),

                    // 12. Psykiska sjukdomar och störningar
                    kategori(categoryIds[12], 'KAT_12.RBK', 'KAT_12.HLP', { }, [
                        fraga(27, 'FRG_27.RBK', 'FRG_27.HLP', { required:true, requiredProp: 'psykiskt.psykiskSjukdom' }, [{
                            type: 'ue-radio',
                            modelProp: 'psykiskt.psykiskSjukdom'
                        }])
                    ]),

                    // 13. ADHD, autismspektrumtillstånd och likartade tillstånd samt psykisk utvecklingsstörning
                    kategori(categoryIds[13], 'KAT_13.RBK', 'KAT_13.HLP', { }, [
                        fraga(28, 'FRG_28.RBK', 'FRG_28.HLP', { required:true, requiredProp: 'utvecklingsstorning.psykiskUtvecklingsstorning' }, [{
                            type: 'ue-radio',
                            modelProp: 'utvecklingsstorning.psykiskUtvecklingsstorning'
                        }]),
                        fraga(29, 'FRG_29.RBK', 'FRG_29.HLP', { required:true, requiredProp: 'utvecklingsstorning.harSyndrom' }, [{
                            type: 'ue-radio',
                            modelProp: 'utvecklingsstorning.harSyndrom'
                        }])
                    ]),

                    // 14. Sjukhusvård
                    kategori(categoryIds[14], 'KAT_14.RBK', 'KAT_14.HLP', { }, [
                        fraga(30, 'FRG_30.RBK', 'FRG_30.HLP', { required:true, requiredProp: 'sjukhusvard.sjukhusEllerLakarkontakt' }, [{
                            type: 'ue-radio',
                            modelProp: 'sjukhusvard.sjukhusEllerLakarkontakt'
                        }]),
                        fraga(30, '', '', { hideExpression: '!model.sjukhusvard.sjukhusEllerLakarkontakt' }, [{
                            type: 'ue-textfield',
                            modelProp: 'sjukhusvard.tidpunkt',
                            htmlMaxlength: 40,
                            label: {
                                key: 'DFR_30.2.RBK',
                                required: true,
                                requiredProp: 'sjukhusvard.tidpunkt'
                            }
                        }]),
                        fraga(30, '', '', { hideExpression: '!model.sjukhusvard.sjukhusEllerLakarkontakt' }, [{
                            type: 'ue-textfield',
                            modelProp: 'sjukhusvard.vardinrattning',
                            htmlMaxlength: 40,
                            label: {
                                key: 'DFR_30.3.RBK',
                                required: true,
                                requiredProp: 'sjukhusvard.vardinrattning'
                            }
                        }]),
                        fraga(30, '', '', { hideExpression: '!model.sjukhusvard.sjukhusEllerLakarkontakt' }, [{
                            type: 'ue-textfield',
                            modelProp: 'sjukhusvard.anledning',
                            htmlMaxlength: 50,
                            label: {
                                key: 'DFR_30.4.RBK',
                                required: true,
                                requiredProp: 'sjukhusvard.anledning'
                            }
                        }])
                    ]),

                    // 15. Övrig medicinering
                    kategori(categoryIds[15], 'KAT_15.RBK', 'KAT_15.HLP', { }, [
                        fraga(31, 'FRG_31.RBK', 'FRG_31.HLP', { required:true, requiredProp: 'medicinering.stadigvarandeMedicinering' }, [{
                            type: 'ue-radio',
                            modelProp: 'medicinering.stadigvarandeMedicinering'
                        },{
                            type: 'ue-textarea',
                            modelProp: 'medicinering.beskrivning',
                            hideExpression: '!model.medicinering.stadigvarandeMedicinering',
                            htmlMaxlength: 180,
                            rows: 3,
                            label: {
                                key: 'DFR_31.2.RBK',
                                required: true,
                                requiredProp: 'medicinering.beskrivning'
                            }
                        }])
                    ]),

                    // 16. Övrig kommentar
                    kategori(categoryIds[16], 'KAT_16.RBK', 'KAT_16.HLP', { }, [
                        fraga(32, 'FRG_32.RBK', 'FRG_32.HLP', { }, [{
                            type: 'ue-textarea',
                            modelProp: 'kommentar',
                            htmlMaxlength: 500,
                            rows: 5
                        }])
                    ]),
                    // Bedömning
                    kategori(categoryIds[101], 'KAT_101.RBK', 'KAT_101.HLP', { }, [
                        fraga(33, 'FRG_33.RBK', 'FRG_33.HLP', { required: true,
                            requiredProp: requiredKorkortProperties('bedomning')}, [{
                            labelTemplate:'KORKORT.{0}.RBK',
                            type: 'ue-checkgroup-ts',
                            modelProp: 'bedomning.korkortstyp',
                            watcher: [{
                                type: '$watch',
                                watchDeep: true,
                                expression: 'model.bedomning.korkortstyp',
                                /*jshint maxcomplexity:11*/
                                listener: function(newValue, oldValue, scope) {
                                    if (oldValue && newValue !== oldValue) {

                                        // find if a regular prop or kanintetastallning changed
                                        var kanInteTaStallningActivated = false;
                                        var otherActivated = false;
                                        for(var i = 0; i < newValue.length; i++){
                                            if(newValue[i].selected !== oldValue[i].selected && newValue[i].selected === true){
                                                if(!kanInteTaStallningActivated && newValue[i].type === 'KAN_INTE_TA_STALLNING'){
                                                    kanInteTaStallningActivated = true;
                                                } else if(!otherActivated) {
                                                    otherActivated = true;
                                                }
                                            }
                                        }

                                        // deselect all checks from the group not selected if something was selected
                                        var index = u.findIndexWithPropertyValue(newValue, 'type', 'KAN_INTE_TA_STALLNING');
                                        if (index !== -1) {
                                            if(kanInteTaStallningActivated) {
                                                for(var j = 0; j < scope.model.bedomning.korkortstyp.length; j++){
                                                    if(index !== j){
                                                        scope.model.bedomning.korkortstyp[j].selected = false;
                                                    }
                                                }
                                            } else if(otherActivated) {
                                               scope.model.bedomning.korkortstyp[index].selected = false;
                                            }
                                        }
                                    }
                                }
                            }]
                        }]),
                        fraga(33, '', '', { }, [{
                            type: 'ue-text',
                            label: {
                                key: 'ts-bas.helptext.bedomning.info'
                            },
                            hideExpression: noKravYtterligareUnderlagFieldsFilledExpression,
                            paddingBottom: true
                        },{
                            type: 'ue-text',
                            label: {
                                key: 'ts-bas.helptext.synfunktioner.8-dioptrier-valt'
                            },
                            hideExpression: '!model.syn.korrektionsglasensStyrka',
                            paddingBottom: true
                        },{
                            type: 'ue-text',
                            label: {
                                key: 'DFR_19.3.INF'
                            },
                            hideExpression: '!(model.diabetes.tabletter || model.diabetes.insulin)',
                            paddingBottom: true
                        }]),
                        fraga(34, 'FRG_34.RBK', 'FRG_34.HLP', { }, [{
                            type: 'ue-textarea',
                            modelProp: 'bedomning.lakareSpecialKompetens',
                            htmlMaxlength: 130,
                            rows: 3
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
                return config;
            }


            return {
                getConfig: _getConfig,
                getCategoryIds: _getCategoryIds
            };
        }]);