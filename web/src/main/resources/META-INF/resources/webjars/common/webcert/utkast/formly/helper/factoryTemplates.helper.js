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

angular.module('common').factory('common.FactoryTemplatesHelper', [
    'common.ObjectHelper', 'common.UserModel', 'common.DateUtilsService',
    function(ObjectHelper, UserModel, dateUtils) {
        'use strict';

        var singleTextAdressLabelColSize = 2;
        var singleTextLabelColSize = 2;

        return {

            kategori: function kategori(id, name, fragor, wrapperOptions) {

                if(!wrapperOptions){
                    wrapperOptions = {};
                }

                if(!wrapperOptions.required){
                    wrapperOptions.required = null;
                }
                if(!wrapperOptions.hideExpression){
                    wrapperOptions.hideExpression = 'false';
                }

                return {
                    wrapper: 'wc-kategori',
                    templateOptions: {category: id, categoryName: name, required: wrapperOptions.required},
                    hideExpression: wrapperOptions.hideExpression,
                    fieldGroup: fragor
                };
            },

            fraga: function fraga(id, components) {
                return {
                    wrapper: 'wc-fraga',
                    templateOptions: {frageId: id},
                    fieldGroup: components
                };
            },

            adress: {
                wrapper: 'wc-field-static',
                className: 'no-wcFormLabelPadding',
                templateOptions: {staticLabel: 'common.intyg.patientadress', categoryName: 'patient'},
                fieldGroup: [
                    {
                        key: 'grundData.patient.postadress',
                        type: 'single-text',
                        templateOptions: {
                            staticLabel: 'Postadress',
                            disabled: UserModel.isDjupintegration(),
                            size: 'full',
                            labelColSize: singleTextAdressLabelColSize,
                            formType: 'horizontal',
                            htmlMaxlength: 500,
                            required: true
                        }
                    },
                    {
                        key: 'grundData.patient.postnummer',
                        type: 'single-text',
                        templateOptions: {
                            staticLabel: 'Postnummer',
                            disabled: UserModel.isDjupintegration(),
                            size: '5',
                            labelColSize: singleTextAdressLabelColSize,
                            formType: 'horizontal',
                            htmlMaxlength: 6,
                            required: true
                        }
                    },
                    {
                        key: 'grundData.patient.postort',
                        type: 'single-text',
                        templateOptions: {
                            staticLabel: 'Postort',
                            disabled: UserModel.isDjupintegration(),
                            labelColSize: singleTextAdressLabelColSize,
                            formType: 'horizontal',
                            htmlMaxlength: 100,
                            required: true
                        }
                    },
                    {
                        type: 'patient-address-updater',
                        hideExpression: function() {
                            return UserModel.isDjupintegration();
                        },
                        templateOptions: {
                            formType: 'horizontal',
                            labelColSize: singleTextAdressLabelColSize,
                            hideFromSigned: true
                        }
                    }
                ]
            },
            vardenhet: {
                wrapper: 'wc-field-static',
                className: 'no-wcFormLabelPadding',
                templateOptions: {staticLabel: 'common.label.vardenhet', categoryName: 'vardenhet'},
                fieldGroup: [
                    {
                        type: 'label-vardenhet'
                    },
                    {
                        key: 'grundData.skapadAv.vardenhet.postadress',
                        type: 'single-text',
                        templateOptions: {
                            staticLabel: 'Postadress',
                            size: 'full',
                            labelColSize: singleTextLabelColSize,
                            formType: 'horizontal',
                            htmlMaxlength: 500,
                            required: true
                        }
                    },
                    {
                        key: 'grundData.skapadAv.vardenhet.postnummer',
                        type: 'single-text',
                        templateOptions: {
                            staticLabel: 'Postnummer',
                            size: '5',
                            labelColSize: singleTextLabelColSize,
                            formType: 'horizontal',
                            htmlMaxlength: 6,
                            numbersOnly: true,
                            required: true
                        }
                    },
                    {
                        key: 'grundData.skapadAv.vardenhet.postort',
                        type: 'single-text',
                        templateOptions: {
                            staticLabel: 'Postort',
                            labelColSize: singleTextLabelColSize,
                            formType: 'horizontal',
                            required: true
                        }
                    },
                    {
                        key: 'grundData.skapadAv.vardenhet.telefonnummer',
                        type: 'single-text',
                        templateOptions: {
                            staticLabel: 'Telefonnummer',
                            labelColSize: singleTextLabelColSize,
                            formType: 'horizontal',
                            htmlMaxlength: 100,
                            required: true
                        }
                    }
                ]
            },
            grundForMU: {
                wrapper: 'validationGroup', templateOptions: {
                    type: 'check-group',
                    validationGroup: 'baserasPa',
                    kompletteringGroup: 'baseratPa'
                },
                fieldGroup: [
                    {
                        key: 'undersokningAvPatienten', type: 'date', className: 'small-gap', templateOptions: {
                        label: 'KV_FKMU_0001.UNDERSOKNING',
                        maxDate: dateUtils.todayAsYYYYMMDD()
                    }
                    }, {
                        key: 'journaluppgifter', type: 'date', className: 'small-gap', templateOptions: {
                            label: 'KV_FKMU_0001.JOURNALUPPGIFTER',
                            maxDate: dateUtils.todayAsYYYYMMDD()
                        }
                    }, {
                        key: 'anhorigsBeskrivningAvPatienten', type: 'date', className: 'small-gap', templateOptions: {
                            label: 'KV_FKMU_0001.ANHORIG',
                            maxDate: dateUtils.todayAsYYYYMMDD()
                        }
                    }, {
                        key: 'annatGrundForMU', type: 'date', templateOptions: {
                            label: 'KV_FKMU_0001.ANNAT',
                            maxDate: dateUtils.todayAsYYYYMMDD()
                        }
                    }
                ]
            },
            annatGrundForMUBeskrivning: {
                key: 'annatGrundForMUBeskrivning',
                type: 'single-text-vertical',
                hideExpression: '!model.annatGrundForMU',
                templateOptions: {
                    label: 'DFR_1.3',
                    help: 'DFR_1.3',
                    required: true,
                    size: 'full'
                }
            },
            annatGrundForMUBeskrivningNoLine: {
                key: 'annatGrundForMUBeskrivning',
                type: 'single-text-vertical',
                hideExpression: '!model.annatGrundForMU',
                templateOptions: {
                    label: 'DFR_1.3',
                    help: 'DFR_1.3',
                    required: true,
                    size: 'full'
                }
            }
        };
    }]);