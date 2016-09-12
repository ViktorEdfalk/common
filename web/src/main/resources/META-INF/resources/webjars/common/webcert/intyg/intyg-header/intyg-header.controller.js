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

angular.module('common').controller('common.IntygHeader',
    ['$rootScope', '$scope', '$log', '$state', '$stateParams', 'common.messageService', 'common.PrintService',
    'common.IntygCopyRequestModel', 'common.IntygFornyaRequestModel', 'common.User', 'common.UserModel',
        'common.IntygSend', 'common.IntygCopyFornya', 'common.IntygMakulera', 'common.IntygViewStateService', 'common.statService', 'common.ObjectHelper',
        function($rootScope, $scope, $log, $state, $stateParams, messageService, PrintService, IntygCopyRequestModel,
            IntygFornyaRequestModel, User, UserModel, IntygSend, IntygCopyFornya, IntygMakulera, CommonViewState, statService, ObjectHelper) {
            'use strict';

            var intygType = $state.current.data.intygType;

            $scope.user = UserModel;
            $scope.intygstyp = intygType;
            $scope.copyBtnTooltipText = messageService.getProperty($scope.intygstyp+'.label.kopiera.text');
            $scope.fornyaBtnTooltipText = messageService.getProperty($scope.intygstyp+'.label.fornya.text');


            $scope.makuleratIntyg = function(){
                return $scope.viewState.common.intygProperties.isRevoked || $scope.viewState.common.isIntygOnRevokeQueue;
            };

            $scope.visaSkickaKnappen = function(){
                return !$scope.viewState.common.intygProperties.isSent &&
                  !$scope.viewState.common.isIntygOnSendQueue &&
                  !$scope.viewState.common.intygProperties.isRevoked &&
                  !$scope.viewState.common.isIntygOnRevokeQueue;
            };

            $scope.send = function() {
                IntygSend.send($scope.viewState.intygModel.id, intygType, CommonViewState.defaultRecipient,
                        intygType+'.label.send', intygType+'.label.send.body', function() {
                        // After a send request we shouldn't reload right away due to async reasons.
                        // Instead, we show an info message stating 'Intyget has skickats till mottagaren'
                        $scope.viewState.common.isIntygOnSendQueue = true;
                });
            };

            $scope.makulera = function(intyg) {
                var confirmationMessage = messageService.getProperty(intygType+'.label.makulera.confirmation', {
                    namn: intyg.grundData.patient.fullstandigtNamn,
                    personnummer: intyg.grundData.patient.personId
                });
                intyg.intygType = intygType;
                IntygMakulera.makulera( intyg, confirmationMessage, function() {
                    $scope.viewState.common.isIntygOnRevokeQueue = true;
                    $scope.viewState.common.intygProperties.isRevoked = true;
                    $rootScope.$emit('ViewCertCtrl.load', intyg, $scope.viewState.common.intygProperties);
                });
            };

            function fornyaOrCopy (intyg, intygServiceMethod, buildIntygRequestModel) {
                if (intyg === undefined || intyg.grundData === undefined) {
                    $log.debug('intyg or intyg.grundData is undefined. Aborting fornya.');
                    return;
                }
                var isOtherCareUnit = User.getValdVardenhet().id !== intyg.grundData.skapadAv.vardenhet.enhetsid;
                intygServiceMethod($scope.viewState,
                    buildIntygRequestModel({
                        intygId: intyg.id,
                        intygType: intygType,
                        patientPersonnummer: intyg.grundData.patient.personId,
                        nyttPatientPersonnummer: $stateParams.patientId,
                        fornamn: $stateParams.fornamn,
                        efternamn: $stateParams.efternamn,
                        mellannamn: $stateParams.mellannamn,
                        postadress: $stateParams.postadress,
                        postnummer: $stateParams.postnummer,
                        postort: $stateParams.postort,
                        coherentJournaling: $stateParams.sjf
                    }),
                    isOtherCareUnit
                );
            }

            $scope.fornya = function(intyg) {
                return fornyaOrCopy(intyg, IntygCopyFornya.fornya, IntygFornyaRequestModel.build);
            };

            $scope.copy = function(intyg) {
                return fornyaOrCopy(intyg, IntygCopyFornya.copy, IntygCopyRequestModel.build);
            };

            $scope.print = function(intyg, isEmployeeCopy) {
                if (CommonViewState.intygProperties.isRevoked) {
                    var customHeader = intyg.grundData.patient.fullstandigtNamn + ' - ' + intyg.grundData.patient.personId;
                    PrintService.printWebPageWithCustomTitle(intyg.id, intygType, customHeader);
                } else if (isEmployeeCopy) {
                    window.open($scope.pdfUrl + '/arbetsgivarutskrift', '_blank');
                } else {
                    window.open($scope.pdfUrl, '_blank');
                }
            };
        }
    ]
);
