/*
 * Copyright (C) 2017 Inera AB (http://www.inera.se)
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

angular.module('ts-bas').factory('ts-bas.viewConfigFactory', [
    '$log', '$filter',
    function($log, $filter) {
        'use strict';

        var viewConfig = [
            {
                type: 'uv-kategori',
                labelKey: 'KAT_99.RBK',
                components: [
                    {
                        type: 'uv-fraga',
                        labelKey: 'FRG_1.RBK',
                        components: [{
                            type: 'uv-del-fraga',
                            labelKey: 'FRG_1.2.RBK',
                            components: [{
                                type: 'uv-list',
                                labelKey: 'KORKORT_{var}.RBK',
                                listKey: 'type',
                                modelProp: 'intygAvser.korkortstyp'
                            }]
                        }]
                    }
                ]
            },
            {
                type: 'uv-kategori',
                labelKey: 'KAT_100.RBK',
                components: [{
                    type: 'uv-fraga',
                    components: [{
                        type: 'uv-kodverk-value',
                        kvModelProps: ['vardkontakt.idkontroll'],
                        kvLabelKeys: ['IDENTITET_{var}.RBK']
                    }]
                }]
            },
            {
                type: 'uv-kategori',
                labelKey: 'KAT_1.RBK',
                components: [
                    {
                        type: 'uv-fraga',
                        labelKey: 'FRG_3.RBK',
                        components: [{
                            type: 'uv-del-fraga',
                            components: [{
                                type: 'uv-boolean-value',
                                modelProp: 'syn.synfaltsdefekter'
                            }]
                        }]
                    },
                    {
                        type: 'uv-fraga',
                        labelKey: 'FRG_4.RBK',
                        components: [{
                            type: 'uv-del-fraga',
                            components: [{
                                type: 'uv-boolean-value',
                                modelProp: 'syn.nattblindhet'
                            }]
                        }]
                    },
                    {
                        type: 'uv-fraga',
                        labelKey: 'FRG_5.RBK',
                        components: [{
                            type: 'uv-del-fraga',
                            components: [{
                                type: 'uv-boolean-value',
                                modelProp: 'syn.progressivOgonsjukdom'
                            }]
                        }]
                    },
                    {
                        type: 'uv-alert-value',
                        labelKey: 'FRG_3-5.INF',
                        alertLevel: 'info'
                    },
                    {
                        type: 'uv-fraga',
                        labelKey: 'FRG_6.RBK',
                        components: [{
                            type: 'uv-del-fraga',
                            components: [{
                                type: 'uv-boolean-value',
                                modelProp: 'syn.diplopi'
                            }]
                        }]
                    },
                    {
                        type: 'uv-fraga',
                        labelKey: 'FRG_7.RBK',
                        components: [{
                            type: 'uv-del-fraga',
                            components: [{
                                type: 'uv-boolean-value',
                                modelProp: 'syn.nystagmus'
                            }]
                        }]
                    },
                    {
                        type: 'uv-fraga',
                        labelKey: 'FRG_8.RBK', // TODO: syn behöver nog egen komponent
                        components: [
                            {
                                type: 'uv-del-fraga',
                                components: [{
                                    type: 'uv-table',
                                    modelProp: 'syn',
                                    colProps: ['hogerOga', 'vansterOga', 'binokulart'],
                                    headers: ['', 'ts-bas.label.syn.utankorrektion', 'ts-bas.label.syn.medkorrektion',
                                        'ts-bas.label.syn.kontaktlins'],
                                    valueProps: [
                                        function(model, rowIndex, colIndex, colProp) {
                                            var message = 'ts-bas.label.syn.' + colProp.toLowerCase();
                                            return message;
                                        },
                                        function(model) {
                                            return model.utanKorrektion;
                                        },
                                        function(model) {
                                            return model.medKorrektion;
                                        },
                                        function(model) {
                                            return $filter('uvBoolFilter')(model.kontaktlins);
                                        }
                                    ]
                                }]
                            }
                        ]
                    },
                    {
                        type: 'uv-fraga',
                        labelKey: 'FRG_9.RBK',
                        components: [{
                            type: 'uv-del-fraga',
                            components: [{
                                type: 'uv-boolean-value',
                                modelProp: 'syn.korrektionsglasensStyrka'
                            }]
                        }]
                    },
                    {
                        type: 'uv-alert-value',
                        labelKey: 'FRG_9.INF',
                        alertLevel: 'info'
                    }
                ]
            },
            {
                type: 'uv-kategori',
                labelKey: 'KAT_2.RBK',
                components: [
                    {
                        type: 'uv-fraga',
                        labelKey: 'FRG_10.RBK',
                        components: [{
                            type: 'uv-del-fraga',
                            components: [{
                                type: 'uv-boolean-value',
                                modelProp: 'horselBalans.balansrubbningar'
                            }]
                        }]
                    },
                    {
                        type: 'uv-fraga',
                        labelKey: 'FRG_11.RBK',
                        components: [{
                            type: 'uv-del-fraga',
                            components: [{
                                type: 'uv-boolean-value',
                                modelProp: 'horselBalans.svartUppfattaSamtal4Meter'
                            }]
                        }]
                    }
                ]
            },
            {
                type: 'uv-kategori',
                labelKey: 'KAT_3.RBK',
                components: [
                    {
                        type: 'uv-fraga',
                        labelKey: 'FRG_12.RBK',
                        components: [{
                            type: 'uv-del-fraga',
                            components: [{
                                type: 'uv-boolean-value',
                                modelProp: 'funktionsnedsattning.funktionsnedsattning'
                            }]
                        }]
                    },
                    {
                        type: 'uv-del-fraga',
                        labelKey: 'DFR_12.2.RBK',
                        components: [{
                            type: 'uv-simple-value',
                            modelProp: 'funktionsnedsattning.beskrivning'
                        }]
                    },
                    {
                        type: 'uv-fraga',
                        labelKey: 'FRG_13.RBK',
                        components: [{
                            type: 'uv-del-fraga',
                            components: [{
                                type: 'uv-boolean-value',
                                modelProp: 'funktionsnedsattning.otillrackligRorelseformaga'
                            }]
                        }]
                    }
                ]
            },
            {
                type: 'uv-kategori',
                labelKey: 'KAT_4.RBK',
                components: [
                    {
                        type: 'uv-fraga',
                        labelKey: 'FRG_14.RBK',
                        components: [{
                            type: 'uv-del-fraga',
                            components: [{
                                type: 'uv-boolean-value',
                                modelProp: 'hjartKarl.hjartKarlSjukdom'
                            }]
                        }]
                    },
                    {
                        type: 'uv-fraga',
                        labelKey: 'FRG_15.RBK',
                        components: [{
                            type: 'uv-del-fraga',
                            components: [{
                                type: 'uv-boolean-value',
                                modelProp: 'hjartKarl.hjarnskadaEfterTrauma'
                            }]
                        }]
                    },
                    {
                        type: 'uv-fraga',
                        labelKey: 'FRG_16.RBK',
                        components: [{
                            type: 'uv-del-fraga',
                            components: [{
                                type: 'uv-boolean-value',
                                modelProp: 'funktionsnedsattning.riskfaktorerStroke'
                            }]
                        }]
                    },
                    {
                        type: 'uv-del-fraga',
                        labelKey: 'DFR_16.2.RBK',
                        components: [{
                            type: 'uv-simple-value',
                            modelProp: 'hjartKarl.beskrivningRiskfaktorer'
                        }]
                    }
                ]
            },
            {
                type: 'uv-kategori',
                labelKey: 'KAT_5.RBK',
                components: [
                    {
                        type: 'uv-fraga',
                        labelKey: 'FRG_17.RBK',
                        components: [{
                            type: 'uv-del-fraga',
                            components: [{
                                type: 'uv-boolean-value',
                                modelProp: 'hjartKarl.hjartKarlSjukdom'
                            }]
                        }]
                    },
                    {
                        type: 'uv-fraga',
                        labelKey: 'FRG_18.RBK',
                        components: [{
                            type: 'uv-del-fraga',
                            components: [{
                                type: 'uv-simple-value',
                                modelProp: 'diabetes.diabetesTyp'
                            }]
                        }]
                    },
                    /*                { // TODO: needs special treatment?
                     type: 'uv-fraga',
                     labelKey: 'FRG_19.RBK',
                     components: [{
                     type: 'uv-del-fraga',
                     components: [{
                     type: 'uv-boolean-value',
                     modelProp: 'funktionsnedsattning.riskfaktorerStroke'
                     }]
                     }]
                     },*/
                    {
                        type: 'uv-alert-value',
                        labelKey: 'DFR_19.3.INF',
                        alertLevel: 'info'
                    }
                ]
            },
            {
                type: 'uv-kategori',
                labelKey: 'KAT_6.RBK',
                components: [
                    {
                        type: 'uv-fraga',
                        labelKey: 'FRG_20.RBK',
                        components: [{
                            type: 'uv-del-fraga',
                            components: [{
                                type: 'uv-boolean-value',
                                modelProp: 'neurologi.neurologiskSjukdom'
                            }]
                        }]
                    }
                ]
            },
            {
                type: 'uv-kategori',
                labelKey: 'KAT_7.RBK',
                components: [
                    {
                        type: 'uv-fraga',
                        labelKey: 'FRG_21.RBK',
                        components: [
                            {
                                type: 'uv-simple-value',
                                modelProp: 'medvetandestorning.medventandestorning'
                            },
                            {
                                type: 'uv-del-fraga',
                                labelKey: 'DFR_21.2.RBK',
                                components: [{
                                    type: 'uv-simple-value',
                                    modelProp: 'medvetandestorning.beskrivning'
                                }]
                            }]
                    }
                ]
            },
            {
                type: 'uv-kategori',
                labelKey: 'KAT_8.RBK',
                components: [
                    {
                        type: 'uv-fraga',
                        labelKey: 'FRG_22.RBK',
                        components: [
                            {
                                type: 'uv-del-fraga',
                                components: [{
                                    type: 'uv-boolean-value',
                                    modelProp: 'njurar.nedsattNjurfunktion'
                                }]
                            }]
                    }
                ]
            },
            {
                type: 'uv-kategori',
                labelKey: 'KAT_9.RBK',
                components: [
                    {
                        type: 'uv-fraga',
                        labelKey: 'FRG_23.RBK',
                        components: [
                            {
                                type: 'uv-del-fraga',
                                components: [{
                                    type: 'uv-boolean-value',
                                    modelProp: 'kognitivt.sviktandeKognitivFunktion'
                                }]
                            }]
                    }
                ]
            },
            {
                type: 'uv-kategori',
                labelKey: 'KAT_10.RBK',
                components: [
                    {
                        type: 'uv-fraga',
                        labelKey: 'FRG_24.RBK',
                        components: [
                            {
                                type: 'uv-del-fraga',
                                components: [{
                                    type: 'uv-boolean-value',
                                    modelProp: 'somnVakenhet.teckenSomnstorningar'
                                }]
                            }]
                    }
                ]
            },
            {
                type: 'uv-kategori',
                labelKey: 'KAT_11.RBK',
                components: [
                    {
                        type: 'uv-fraga',
                        components: [
                            {
                                type: 'uv-del-fraga',
                                labelKey: 'DFR_25.1.RBK',
                                components: [{
                                    type: 'uv-boolean-value',
                                    modelProp: 'somnVakenhet.teckenMissbruk'
                                }]
                            }]
                    },
                    {
                        type: 'uv-fraga',
                        components: [
                            {
                                type: 'uv-del-fraga',
                                labelKey: 'DFR_25.2.RBK',
                                components: [{
                                    type: 'uv-boolean-value',
                                    modelProp: 'somnVakenhet.foremalForVardinsats'
                                }]
                            }]
                    },
                    {
                        type: 'uv-fraga',
                        components: [
                            {
                                type: 'uv-del-fraga',
                                labelKey: 'DFR_25.3.RBK',
                                components: [{
                                    type: 'uv-boolean-value',
                                    modelProp: 'somnVakenhet.foremalForVardinsats'
                                }]
                            }]
                    },
                    {
                        type: 'uv-alert-value',
                        labelKey: 'DFR_25.3.INF',
                        alertLevel: 'info'
                    },
                    {
                        type: 'uv-fraga',
                        labelKey: 'FRG_26.RBK',
                        components: [
                            {
                                type: 'uv-simple-value',
                                modelProp: 'narkotikaLakemedel.lakarordineratLakemedelsbruk'
                            },
                            {
                                type: 'uv-del-fraga',
                                labelKey: 'DFR_26.1.RBK',
                                components: [{
                                    type: 'uv-boolean-value',
                                    modelProp: 'narkotikaLakemedel.lakemedelOchDos'
                                }]
                            }]
                    }
                ]
            },
            {
                type: 'uv-kategori',
                labelKey: 'KAT_12.RBK',
                components: [
                    {
                        type: 'uv-fraga',
                        labelKey: 'FRG_27.RBK',
                        components: [
                            {
                                type: 'uv-del-fraga',
                                components: [{
                                    type: 'uv-boolean-value',
                                    modelProp: 'psykiskt.psykiskSjukdom'
                                }]
                            }]
                    }
                ]
            },
            {
                type: 'uv-kategori',
                labelKey: 'KAT_13.RBK',
                components: [
                    {
                        type: 'uv-fraga',
                        labelKey: 'FRG_28.RBK',
                        components: [
                            {
                                type: 'uv-del-fraga',
                                components: [{
                                    type: 'uv-boolean-value',
                                    modelProp: 'utvecklingsstorning.psykiskUtvecklingsstorning'
                                }]
                            }]
                    },
                    {
                        type: 'uv-fraga',
                        labelKey: 'FRG_29.RBK',
                        components: [
                            {
                                type: 'uv-del-fraga',
                                components: [{
                                    type: 'uv-boolean-value',
                                    modelProp: 'utvecklingsstorning.harSyndrom'
                                }]
                            }]
                    }
                ]
            },
            {
                type: 'uv-kategori',
                labelKey: 'KAT_14.RBK',
                components: [
                    {
                        type: 'uv-fraga',
                        labelKey: 'FRG_30.RBK',
                        components: [
                            {
                                type: 'uv-simple-value',
                                modelProp: 'sjukhusvard.sjukhusEllerLakarkontakt'
                            },
                            {
                                type: 'uv-del-fraga',
                                labelKey: 'DFR_30.2.RBK',
                                components: [{
                                    type: 'uv-simple-value',
                                    modelProp: 'sjukhusvard.tidpunkt'
                                }]
                            },
                            {
                                type: 'uv-del-fraga',
                                labelKey: 'DFR_30.3.RBK',
                                components: [{
                                    type: 'uv-simple-value',
                                    modelProp: 'sjukhusvard.vardinrattning'
                                }]
                            },
                            {
                                type: 'uv-del-fraga',
                                labelKey: 'DFR_30.4.RBK',
                                components: [{
                                    type: 'uv-simple-value',
                                    modelProp: 'sjukhusvard.anledning'
                                }]
                            }
                        ]
                    }
                ]
            },
            {
                type: 'uv-kategori',
                labelKey: 'KAT_15.RBK',
                components: [
                    {
                        type: 'uv-fraga',
                        labelKey: 'FRG_31.RBK',
                        components: [
                            {
                                type: 'uv-simple-value',
                                modelProp: 'medicinering.stadigvarandeMedicinering'
                            },
                            {
                                type: 'uv-del-fraga',
                                labelKey: 'DFR_31.2.RBK',
                                components: [{
                                    type: 'uv-simple-value',
                                    modelProp: 'medicinering.beskrivning'
                                }]
                            }
                        ]
                    }
                ]
            },
            {
                type: 'uv-kategori',
                labelKey: 'KAT_16.RBK',
                components: [
                    {
                        type: 'uv-fraga',
                        labelKey: 'FRG_32.RBK',
                        components: [
                            {
                                type: 'uv-simple-value',
                                modelProp: 'kommentar'
                            }
                        ]
                    }
                ]
            },
            {
                type: 'uv-kategori',
                labelKey: 'KAT_101.RBK',
                components: [
                    {
                        type: 'uv-fraga',
                        labelKey: 'FRG_33.RBK',
                        components: [
                            {
                                type: 'uv-simple-value', // TODO: this aint right
                                modelProp: 'bedomning.kanInteTaStallning'
                            },
                            {
                                type: 'uv-del-fraga',
                                labelKey: 'DFR_33.2.RBK',
                                components: [{
                                    type: 'uv-simple-value',
                                    modelProp: 'medicinering.beskrivning'
                                }]
                            }
                        ]
                    },
                    {
                        type: 'uv-fraga',
                        labelKey: 'FRG_34.RBK',
                        components: [
                            {
                                type: 'uv-simple-value', // TODO: this aint right
                                modelProp: 'bedomning.lakareSpecialKompetens'
                            }
                        ]
                    }
                ]
            },
            {
                type: 'uv-skapad-av',
                modelProp: 'grundData.skapadAv'
            }];

        return {
            getViewConfig: function() {
                return angular.copy(viewConfig);
            }
        };
    }]);
