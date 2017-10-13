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

angular.module('doi').factory('doi.FormFactory',
    ['$log', '$timeout',
        'common.DateUtilsService', 'common.ObjectHelper', 'common.UserModel',
        'common.FactoryTemplatesHelper', 'common.PersonIdValidatorService',
        function($log, $timeout,
            DateUtils, ObjectHelper, UserModel, FactoryTemplates, PersonIdValidator) {
            'use strict';


            var categoryNames = {
                1: 'personuppgifter',
                2: 'dodsdatumochdodsplats',
                3: 'barnsomavlidit',
                7: 'utlatandeorsak',
                8: 'operation',
                9: 'forgiftning',
                10: 'dodsorsakgrund'
            };

            var kategori = FactoryTemplates.kategori;
            var fraga = FactoryTemplates.fraga;

            var formFields = [
                FactoryTemplates.adress,
                kategori(1, categoryNames[1], [
                    fraga(1, [
                        {
                            key: 'identitetStyrkt',
                            type: 'single-text-vertical',
                            templateOptions: {label: 'DFR_1.1', required: true}
                        }
                    ]),
                    fraga(14, [
                        {
                            key: 'land',
                            type: 'single-text-vertical',
                            templateOptions: {label: 'DFR_14.1', required: true}
                        }
                    ])
                ]),
                kategori(2, categoryNames[2], [ // R1, R2, R3
                    fraga(2, [
                        {
                            key: 'dodsdatumSakert',
                            type: 'boolean',
                            templateOptions: {
                                label: 'FRG_2',
                                yesLabel: 'SVAR_SAKERT.RBK',
                                noLabel: 'SVAR_EJ_SAKERT.RBK',
                                required: true
                            }
                        },
                        {
                            key: 'dodsdatum',
                            type: 'singleDate',
                            hideExpression: 'model.dodsdatumSakert !== true',
                            templateOptions: {label: 'DFR_2.2', required: true}
                        },
                        {
                            key: 'dodsdatum',
                            type: 'vagueDate',
                            hideExpression: 'model.dodsdatumSakert !== false',
                            templateOptions: {label: 'DFR_2.2', required: true}
                        },
                        {
                            key: 'antraffatDodDatum',
                            type: 'singleDate',
                            hideExpression: 'model.dodsdatumSakert !== false',
                            templateOptions: {label: 'DFR_2.3', required: true}
                        }
                    ]),
                    fraga(3, [
                        {
                            key: 'dodsplatsKommun',
                            type: 'single-text-vertical',
                            templateOptions: {label: 'DFR_3.1', required: true}
                        },
                        {
                            key: 'dodsplatsBoende',
                            type: 'radio-group', // R4
                            templateOptions: {
                                label: 'DFR_3.2',
                                code: 'DODSPLATS_BOENDE',
                                choices: [
                                    'SJUKHUS',
                                    'ORDINART_BOENDE',
                                    'SARSKILT_BOENDE',
                                    'ANNAN'
                                ],
                                required: true
                            }
                        }
                    ])
                ]),
                kategori(3, categoryNames[3], [
                    fraga(4, [
                        {
                            key: 'barn',
                            type: 'boolean',
                            templateOptions: {label: 'DFR_4.1', required: true},
                            expressionProperties: {
                                'templateOptions.disabled': 'formState.barnForced'
                            },
                            watcher: {
                                expression: 'model.dodsdatumSakert ? model.dodsdatum : null',
                                listener: function _barnDodsDatumListener(field, newValue, oldValue, scope) {
                                    if (newValue !== oldValue) {
                                        var birthDate = DateUtils.toMomentStrict(PersonIdValidator.getBirthDate(scope.model.grundData.patient.personId));
                                        if (!birthDate) {
                                            $log.error('Invalid personnummer in _barnDodsDatumListener');
                                        }
                                        else {
                                            var barn28DagarDate = birthDate.add('days', 28);
                                            var dodsDatum = DateUtils.toMomentStrict(newValue);
                                            if (dodsDatum && dodsDatum.isValid() &&
                                                (dodsDatum.isBefore(barn28DagarDate) ||
                                                dodsDatum.isSame(barn28DagarDate))) {
                                                scope.model.barn = true;
                                                scope.options.formState.barnForced = true;
                                            } else if (dodsDatum && dodsDatum.isValid() &&
                                                dodsDatum.isAfter(barn28DagarDate)) {
                                                scope.model.barn = false;
                                                scope.options.formState.barnForced = true;
                                            } else {
                                                scope.model.barn = undefined;
                                                scope.options.formState.barnForced = false;
                                            }
                                        }
                                    }
                                }
                            }
                        },{
                            type: 'info',
                            hideExpression: '!formState.barnForced || !model.barn',
                            templateOptions: {label: 'doi.info.barn.forced.true'}
                        },{
                            type: 'info',
                            hideExpression: '!formState.barnForced || model.barn',
                            templateOptions: {label: 'doi.info.barn.forced.false'}
                        }
                    ])
                ]),
                kategori(7, categoryNames[7], [
                    fraga(8, [
                        {
                            type: 'headline',
                            templateOptions: {id: 'KAT_7.1', label: 'KAT_7.1'}
                        },
                        {
                            key: 'terminalDodsorsak',
                            type: 'dodsorsakTerminalFoljd', // R9
                            templateOptions: {
                                label: 'FRG_8',
                                orsaksTyper: [
                                    'UPPGIFT_SAKNAS',
                                    'KRONISK',
                                    'PLOTSLIG'
                                ],
                                letter: 'A',
                                required: true,
                                beskrivningLabel: 'DELAD_TEXT.1',
                                datumLabel: 'DELAD_TEXT.2',
                                orsakLabel: 'DELAD_TEXT.3',
                                foljd: {
                                    key: 'foljd',
                                    maxRows: 3,
                                    required: false,
                                    letter: ['B', 'C', 'D'],
                                    label: 'FRG_9',
                                    beskrivningLabel: 'DELAD_TEXT.1',
                                    datumLabel: 'DELAD_TEXT.2',
                                    orsakLabel: 'DELAD_TEXT.3',
                                    orsaksTyper: [
                                        'UPPGIFT_SAKNAS',
                                        'KRONISK',
                                        'PLOTSLIG'
                                    ]
                                }
                            }
                        }
                    ]),
                    fraga(10, [
                        {
                            type: 'headline',
                            templateOptions: {id: 'KAT_7.2', label: 'KAT_7.2', bold: true}
                        },
                        {
                            key: 'bidragandeSjukdomar',
                            type: 'dodsorsakMulti', // R11
                            templateOptions: {
                                maxRows: 8,
                                orsaksTyper: [
                                    'UPPGIFT_SAKNAS',
                                    'KRONISK',
                                    'PLOTSLIG'
                                ],
                                beskrivningLabel: 'DELAD_TEXT.1',
                                datumLabel: 'DELAD_TEXT.2',
                                orsakLabel: 'DELAD_TEXT.3'
                            }
                        }
                    ])
                ]),
                kategori(8, categoryNames[8], [
                    fraga(11, [
                        {
                            key: 'operation',
                            type: 'radio-group', // R12
                            templateOptions: {
                                label: 'DFR_11.1',
                                code: 'DFR_11.1',
                                choices: [
                                    'JA',
                                    'NEJ',
                                    'UPPGIFT_SAKNAS'
                                ],
                                required: true
                            }
                        },
                        {
                            key: 'operationDatum',
                            type: 'singleDate',
                            hideExpression: 'model.operation !== "JA"', // R13
                            templateOptions: {label: 'DFR_11.2', required: true}
                        },
                        {
                            key: 'operationAnledning',
                            type: 'single-text-vertical',
                            hideExpression: 'model.operation !== "JA"', // R13
                            templateOptions: {label: 'DFR_11.3', required: true}
                        }
                    ])
                ]),
                kategori(9, categoryNames[9], [
                    fraga(12, [
                        {
                            key: 'forgiftning',
                            type: 'boolean',
                            templateOptions: {
                                label: 'DFR_12.1',
                                required: true
                            }
                        },
                        {
                            key: 'forgiftningOrsak',
                            type: 'radio-group', // R15
                            hideExpression: 'model.forgiftning !== true', // R14
                            templateOptions: {
                                label: 'DFR_12.2',
                                code: 'ORSAK',
                                choices: [
                                    'OLYCKSFALL',
                                    'SJALVMORD',
                                    'AVSIKTLIGT_VALLAD',
                                    'OKLART'
                                ],
                                required: true
                            }
                        },
                        {
                            key: 'forgiftningDatum',
                            type: 'singleDate',
                            hideExpression: 'model.forgiftning !== true', // R16
                            templateOptions: {label: 'DFR_12.3', required: true}
                        },
                        {
                            key: 'forgiftningUppkommelse',
                            type: 'multi-text',
                            hideExpression: 'model.forgiftning !== true', // R17
                            templateOptions: {label: 'DFR_12.4', required: true}
                        }
                    ])
                ]),
                kategori(10, categoryNames[10], [
                    fraga(13, [
                        {
                            key: 'grunder',
                            type: 'check-group', // R18
                            templateOptions: {
                                label: 'DFR_13.1',
                                required: true,
                                descLabel: 'DFR_13.1',
                                code: 'DODSORSAKSUPPGIFTER',
                                choices: [
                                    'UNDERSOKNING_FORE_DODEN',
                                    'UNDERSOKNING_EFTER_DODEN',
                                    'KLINISK_OBDUKTION',
                                    'RATTSMEDICINSK_OBDUKTION',
                                    'RATTSMEDICINSK_BESIKTNING'
                                ]
                            }
                        }
                    ])
                ]),
                FactoryTemplates.vardenhet
            ];

            return {
                getFormFields: function() {
                    return angular.copy(formFields);
                },
                getCategoryNames: function() {
                    return angular.copy(categoryNames);
                }
            };
        }]);
