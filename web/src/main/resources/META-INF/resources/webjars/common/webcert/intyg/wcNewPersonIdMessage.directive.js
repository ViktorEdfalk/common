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

/**
 * Show patient has new id message if it differs from the one from the intyg.
 * Broadcast a intyg.loaded event on rootscope when the intyg is loaded to update the message.
 */
angular.module('common').directive('wcNewPersonIdMessage', [
    '$stateParams', 'common.PersonIdValidatorService', 'common.messageService',
    function($stateParams, personIdValidator, messageService) {
        'use strict';

        return {
            restrict: 'A',
            replace: true,
            scope: true,
            controller: function($scope) {

                $scope.show = false; // Flag to control visibility
                $scope.message = ''; // Text to be shown

                function showPersonnummerMessage(number) {
                    $scope.show = true;
                    var messageId = 'common.alert.newpersonid';
                    $scope.message = messageService.getProperty(messageId, {person: number}, messageId);
                }

                function showReservnummerMessage(number) {
                    $scope.show = true;
                    var messageId = 'common.alert.newreserveid';
                    $scope.message = messageService.getProperty(messageId, {reserve: number}, messageId);
                }

                function isDefined(data) {
                    return typeof data !== 'undefined' && data !== '';
                }

                function modelHasValidPatient(intygModel) {
                    return intygModel && intygModel.grundData && intygModel.grundData.patient;
                }

                function decideMessageToShow(intygPersonnummer, alternatePatientSSn) {
                    // 1. intygets personnummer validerar som personnummer
                    // = visa nuvarande skylt om nytt personnummer om alternatePatientSSn skiljer sig från detta.
                    var result = personIdValidator.validatePersonnummer(intygPersonnummer);
                    if(personIdValidator.validResult(result)){
                        if(intygPersonnummer !== alternatePatientSSn) {
                            showPersonnummerMessage(alternatePatientSSn);
                        }
                    } else {
                        //2 intygets personnummer är ett samordningsnummer (dagsiffra > 31)
                        result = personIdValidator.validateSamordningsnummer(intygPersonnummer);
                        if(personIdValidator.validResult(result)) {

                            //2.2 om alternatePatientSSn validerar som personnummer
                            //    = visa nuvarande meddelande om nytt personnummer.
                            result = personIdValidator.validatePersonnummer(alternatePatientSSn);
                            if(personIdValidator.validResult(result)) {
                                showPersonnummerMessage(alternatePatientSSn);
                            } else {
                                //2.1 om alternatePatientSSn inte validerar som personnummer
                                //    = visa istället meddelande "Patienten har samordningsnummer kopplat till reservnummer: alternatePatientSSn"
                                showReservnummerMessage(alternatePatientSSn);
                            }
                        }
                    }
                }

                var updateShowFlag = function() {
                    $scope.show = false;
                    if (isDefined($stateParams.patientId) &&
                        modelHasValidPatient($scope.viewState.intygModel)) {

                        var intygPersonnummer = $scope.viewState.intygModel.grundData.patient.personId;
                        var alternatePatientSSn = $stateParams.patientId;
                        decideMessageToShow(intygPersonnummer, alternatePatientSSn);
                    }
                };

                // intyg data may be loaded now, or it may be loaded later.
                updateShowFlag();
                $scope.$watch('viewState.intygModel.grundData.patient.personId', updateShowFlag);
            },
            templateUrl: '/web/webjars/common/webcert/intyg/wcNewPersonIdMessage.directive.html'
        };
    }]);
