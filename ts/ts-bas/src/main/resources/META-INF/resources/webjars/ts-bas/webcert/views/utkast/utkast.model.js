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
angular.module('ts-bas').factory('ts-bas.Domain.IntygModel',
    ['common.Domain.GrundDataModel', 'common.Domain.DraftModel', 'common.domain.ModelAttr',
        'common.domain.BaseAtticModel', 'common.domain.ModelTransformService',
        function(GrundData, DraftModel, ModelAttr, BaseAtticModel, ModelTransform) {
        'use strict';

        var bedomningFromTransform = function(backendBedomning) {

            var korkortstyp = backendBedomning.korkortstyp;
            korkortstyp.push({type:'KAN_INTE_TA_STALLNING', selected: !angular.isDefined(backendBedomning.kanInteTaStallning) ? false : backendBedomning.kanInteTaStallning});

            var frontendObject = {
                korkortstyp: korkortstyp,
                lakareSpecialKompetens: backendBedomning.lakareSpecialKompetens
            }

            return frontendObject;
        };

        var bedomningToTransform = function(frontendObject) {

            function findWithAttr(array, attr, value) {
                for(var i = array.length - 1; i >= 0; i--) {
                    if(array[i][attr] === value) {
                        return i;
                    }
                }
                return -1;
            }

            var transformedFrontendObject = angular.copy(frontendObject);
            var index = findWithAttr(transformedFrontendObject.korkortstyp, 'type', 'KAN_INTE_TA_STALLNING');
            var kanInteTaStallning = undefined;
            if(index !== -1) {
                kanInteTaStallning = transformedFrontendObject.korkortstyp[index].selected;
                transformedFrontendObject.korkortstyp.splice(index);
            }

            var backendBedomning = {
                korkortstyp: transformedFrontendObject.korkortstyp,
                kanInteTaStallning: kanInteTaStallning,
                lakareSpecialKompetens: transformedFrontendObject.lakareSpecialKompetens
            };
            return backendBedomning;
        };

        /**
         * Constructor, with class name
         */
        var TsBasModel = BaseAtticModel._extend({
            init: function init() {
                var grundData = GrundData.build();
                init._super.call(this, 'TsBasModel', {
                    id: undefined,
                    typ: undefined,
                    kommentar: undefined,
                    grundData: grundData,
                    textVersion: undefined,
                    vardkontakt: {
                        typ: undefined,
                        idkontroll: undefined
                    },
                    intygAvser: {
                        'korkortstyp': new ModelAttr('korkortstyp',
                            {defaultValue:[
                                {'type': 'C1', 'selected': false},
                                {'type': 'C1E', 'selected': false},
                                {'type': 'C', 'selected': false},
                                {'type': 'CE', 'selected': false},
                                {'type': 'D1', 'selected': false},
                                {'type': 'D1E', 'selected': false},
                                {'type': 'D', 'selected': false},
                                {'type': 'DE', 'selected': false},
                                {'type': 'TAXI', 'selected': false},
                                {'type': 'ANNAT', 'selected': false}
                        ]})
                    },
                    syn: {
                        'synfaltsdefekter': undefined,
                        'nattblindhet': undefined,
                        'progressivOgonsjukdom': undefined,
                        'diplopi': undefined,
                        'nystagmus': undefined,
                        'hogerOga': {
                            'utanKorrektion': undefined,
                            'medKorrektion': undefined,
                            'kontaktlins': false
                        },

                        'vansterOga': {
                            'utanKorrektion': undefined,
                            'medKorrektion': undefined,
                            'kontaktlins': false
                        },

                        'binokulart': {
                            'utanKorrektion': undefined,
                            'medKorrektion': undefined
                        },
                        'korrektionsglasensStyrka': false
                    },
                    horselBalans: {
                        'balansrubbningar': undefined,
                        'svartUppfattaSamtal4Meter': undefined
                    },
                    funktionsnedsattning: {
                        'funktionsnedsattning': undefined,
                        'otillrackligRorelseformaga': undefined,
                        'beskrivning': undefined
                    },
                    hjartKarl: {
                        'hjartKarlSjukdom': undefined,
                        'hjarnskadaEfterTrauma': undefined,
                        'riskfaktorerStroke': undefined,
                        'beskrivningRiskfaktorer': undefined
                    },
                    diabetes: {
                        'harDiabetes': undefined,
                        'diabetesTyp': undefined,
                        'tabletter' : false,
                        'insulin' : false,
                        'kost': false
                    },
                    neurologi: {
                        'neurologiskSjukdom': undefined
                    },
                    medvetandestorning: {
                        'medvetandestorning': undefined,
                        'beskrivning': undefined
                    },
                    njurar: {
                        'nedsattNjurfunktion': undefined
                    },
                    kognitivt: {
                        'sviktandeKognitivFunktion': undefined
                    },
                    somnVakenhet: {
                        'teckenSomnstorningar': undefined
                    },
                    narkotikaLakemedel: {
                        'teckenMissbruk': undefined,
                        'foremalForVardinsats': undefined,
                        'lakarordineratLakemedelsbruk': undefined,
                        'lakemedelOchDos': undefined,
                        'provtagningBehovs': undefined
                    },
                    psykiskt: {
                        'psykiskSjukdom': undefined
                    },
                    utvecklingsstorning: {
                        'psykiskUtvecklingsstorning': undefined,
                        'harSyndrom': undefined
                    },
                    sjukhusvard: {
                        'sjukhusEllerLakarkontakt': undefined,
                        'tidpunkt': undefined,
                        'vardinrattning': undefined,
                        'anledning': undefined
                    },
                    medicinering: {
                        'stadigvarandeMedicinering': undefined,
                        'beskrivning': undefined
                    },
                    bedomning: new ModelAttr('bedomning', {
                        toTransform: bedomningToTransform,
                        fromTransform: bedomningFromTransform,
                    }),
                });
            },
            update: function update(content, parent) {
                if (parent) {
                    parent.content = this;
                }
                update._super.call(this, content);
            }
        }, {
            build : function(){
                return new DraftModel(new TsBasModel());
            }
        });

        /**
         * Return the constructor function IntygModel
         */
        return TsBasModel;
    }]);
