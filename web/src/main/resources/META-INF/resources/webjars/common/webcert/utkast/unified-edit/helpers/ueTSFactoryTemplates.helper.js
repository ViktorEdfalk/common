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
angular.module('common').factory('common.ueTSFactoryTemplatesHelper', [
    '$log', 'common.ObjectHelper', 'common.UserModel', 'common.ueFactoryTemplatesHelper', 'common.UtilsService',
    function($log, ObjectHelper, UserModel, ueFactoryTemplates, UtilsService) {
        'use strict';

        function _patient(viewState) {
            function _shouldDisableAddressInputWhen(model) {
                return UserModel.isDjupintegration() && viewState.common.validPatientAddressAquiredFromPU;
            }
            return ueFactoryTemplates.patient(_shouldDisableAddressInputWhen, true, true);
        }

        function _getBedomningListenerConfig(modelProp, kanInteTaStallningType) {
            return [{
                type: '$watch',
                watchDeep: true,
                expression: 'model.bedomning.' + modelProp,
                /*jshint maxcomplexity:11*/
                listener: function(newValue, oldValue, scope) {

                    if (oldValue && newValue !== oldValue) {

                        var kanInteTaStallningIndex = UtilsService.findIndexWithPropertyValue(newValue, 'type', kanInteTaStallningType),
                            kanInteTaStallningChanged = oldValue[kanInteTaStallningIndex].selected !== newValue[kanInteTaStallningIndex].selected;

                        if(kanInteTaStallningChanged) {
                            // enable or disable all but "Kan inte ta ställning"
                            var kanInteTaStallningSelected = newValue[kanInteTaStallningIndex].selected;
                            for(var i = 0; i < scope.model.bedomning[modelProp].length; i++) {
                                if(kanInteTaStallningIndex === i) {
                                    continue;
                                }
                                scope.model.bedomning[modelProp][i].disabled = kanInteTaStallningSelected;
                                if(kanInteTaStallningSelected) {
                                    scope.model.bedomning[modelProp][i].selected = false;
                                }
                            }
                            return;
                        }

                        // something else has changed
                        var selectedChoices = newValue.filter(function(choice) {
                            return choice.type !== kanInteTaStallningType && choice.selected;
                        });

                        scope.model.bedomning[modelProp][kanInteTaStallningIndex].disabled = selectedChoices.length > 0;
                    }
                }
            }];
        }

        function _getBedomningGroupListenerConfig(modelProp) {
            return [{
                type: '$watch',
                watchDeep: true,
                expression: 'model.intygetAvserBehorigheter.' + modelProp, // this way or $parse the string everywhere
                /*jshint maxcomplexity:11*/
                listener: function(newValue, oldValue, scope) {

                    if (oldValue && newValue !== oldValue) {

                        function checkUncheckOther(targetName) {
                            var targetIndex = UtilsService.findIndexWithPropertyValue(newValue, 'type', targetName);
                            var targetChanged = oldValue[targetIndex].selected !== newValue[targetIndex].selected;

                            if(targetChanged) {
                                // enable or disable all but "Kan inte ta ställning"
                                var targetSelected = newValue[targetIndex].selected;
                                for(var i = 0; i < scope.model.intygetAvserBehorigheter[modelProp].length; i++) {
                                    if(targetIndex === i) {
                                        continue;
                                    }
                                    scope.model.intygetAvserBehorigheter[modelProp][i].disabled = targetSelected;
                                    if(targetSelected) {
                                        scope.model.intygetAvserBehorigheter[modelProp][i].selected = false;
                                    }
                                }
                                return;
                            }
                        }

                        // R2	Om "Behörigheter som avses (Delsvar)" (DFR 1.1) besvaras med "SVAR_ALLA.RBK" ska inga andra svarsalternativ för frågan kunna väljas samtidigt. 	1.1	-
                        checkUncheckOther('ALLA')

                        // R8	Om "Behörigheter som avses (Delsvar)" besvaras med "SVAR_KANINTETASTALLNING.RBK" ska inga andra svarsalternativ för frågan kunna väljas samtidigt. 	1.1
                        // R10	Om frågan "Behörigheter som avses (Delsvar)" (DFR 1.1) besvaras med något annat värde än "SVAR_KANINTETASTALLNING.RBK" kan värdet "SVAR_KANINTETASTALLNING.RBK" inte också anges. 	1.1
                        checkUncheckOther('KANINTETASTALLNING')

                    }
                }
            }];
        }

        return {
            patient: _patient,
            getBedomningListenerConfig: _getBedomningListenerConfig,
            getBedomningGroupListenerConfig: _getBedomningGroupListenerConfig
        };
    }]);
