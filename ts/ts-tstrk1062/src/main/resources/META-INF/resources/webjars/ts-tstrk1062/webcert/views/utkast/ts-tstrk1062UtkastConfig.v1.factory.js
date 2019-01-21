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

angular.module('ts-tstrk1062').factory('ts-tstrk1062.UtkastConfigFactory.v1',
    ['$log', '$timeout', 'common.ueFactoryTemplatesHelper', 'common.ueTSFactoryTemplatesHelper',
        function($log, $timeout, ueFactoryTemplates, ueTSFactoryTemplates) {
            'use strict';

            function _getCategoryIds() {
                return {
                    1: 'intygavser',
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

                function requiredKorkortProperties(field, extraproperty) {
                    var antalKorkort = 16;
                    var korkortsarray = [];
                    for (var i = 0; i < antalKorkort; i++) {
                        korkortsarray.push( field + '.korkortstyp[' + i + '].selected');
                    }
                    korkortsarray.push(extraproperty);
                    return korkortsarray;
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
                    kategori(categoryIds[1], 'KAT_1.RBK', {}, {}, [
                        fraga(1, 'FRG_1.RBK', 'FRG_1.HLP', {required: true, requiredProp: requiredKorkortProperties('intygAvser')}, [{
                            type: 'ue-checkgroup-ts',
                            modelProp: 'intygAvser.korkortstyp',
                            labelTemplate:'KV_INTYGET_AVSER.{0}.RBK',
                            label: {}
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
