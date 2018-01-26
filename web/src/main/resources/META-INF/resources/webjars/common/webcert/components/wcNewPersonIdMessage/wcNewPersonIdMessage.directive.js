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
/**
 * Show patient has new id message if it differs from the one from the intyg.
 * Watches viewState.intygModel.grundData.patient.personId event on rootscope when the intyg is loaded to update the message.
 */
angular.module('common').directive('wcNewPersonIdMessage', [
    '$stateParams', 'common.PersonIdValidatorService', 'common.messageService', 'common.UserModel', 'common.ObjectHelper',
    function($stateParams, personIdValidator, messageService, UserModel, ObjectHelper) {
        'use strict';

        return {
            restrict: 'A',
            replace: true,
            scope: {
                patient: '=',
                isIntyg: '='
            },
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

                function decideMessageToShow(intygPersonnummer, alternatePatientSSn) {

                    var validatedAlternateSSn = personIdValidator.validate(alternatePatientSSn);

                    //If an alternatePatientSSn is given that differs from current..
                    if (intygPersonnummer !== validatedAlternateSSn) {
                        //.. and it's passes as a personnummer/samordningsnummer valid for future use (e.g in copy/renew)
                        if (personIdValidator.validResult(validatedAlternateSSn)) {
                            showPersonnummerMessage(alternatePatientSSn);
                        } else {
                            showReservnummerMessage(alternatePatientSSn);
                        }
                    }
                }

                if ($scope.isIntyg) {
                    var updateShowFlag = function(currentPatient) {
                        $scope.show = false;
                        var alternatePatientSSn = UserModel.getIntegrationParam('alternateSsn');
                        if (!ObjectHelper.isEmpty(alternatePatientSSn) && !ObjectHelper.isEmpty(currentPatient) &&
                            !ObjectHelper.isEmpty(currentPatient.personId)) {
                            var intygPersonnummer = currentPatient.personId;
                            decideMessageToShow(intygPersonnummer, alternatePatientSSn);
                        }
                    };
                    // Patient comes from intyg which is loaded async
                    $scope.$watch('patient', updateShowFlag, true);
                    updateShowFlag();
                }
                else {
                    $scope.show = false;
                    var alternatePatientSSn = UserModel.getIntegrationParam('alternateSsn');
                    var intygPersonnummer = UserModel.getIntegrationParam('beforeAlternateSsn');
                    if (!ObjectHelper.isEmpty(alternatePatientSSn) && !ObjectHelper.isEmpty(intygPersonnummer)) {
                        decideMessageToShow(intygPersonnummer, alternatePatientSSn);
                    }
                }
            },
            templateUrl: '/web/webjars/common/webcert/components/wcNewPersonIdMessage/wcNewPersonIdMessage.directive.html'
        };
    }]);
