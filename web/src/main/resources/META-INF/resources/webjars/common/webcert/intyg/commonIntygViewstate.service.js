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

angular.module('common').service('common.IntygViewStateService',
    ['$log', 'common.ViewStateService', 'common.IntygHelper', 'common.ObjectHelper', function($log, commonViewStateService, IntygHelper, ObjectHelper) {
        'use strict';

        this.reset = function() {
            this.doneLoading = false;
            this.activeErrorMessageKey = null;
            this.inlineErrorMessageKey = null;
            this.showTemplate = true;
            this.isIntygOnSendQueue = false;
            this.isIntygOnRevokeQueue = false;
            this.deleted = false;

            this.intygProperties = {
                type: undefined,
                defaultRecipient: undefined,
                isSent: false,
                isRevoked: false,
                printStatus: 'notloaded', // used in intyg-header.html only and set in intyg view controllers: 'signed' or 'revoked'
                newPatientId: false // FK only for now. Consider making specific viewState services for each intyg as with utkast
            };

            this.common = commonViewStateService;
            this.common.reset();
        };

        this.updateIntygProperties = function(result) {

            var targetName;
            if(this.intygProperties.type === 'ts-bas' || this.intygProperties.type === 'ts-diabetes') {
                targetName = 'TS';
            } else {
                targetName = 'FK';
            }

            this.intygProperties.isSent = IntygHelper.isSentToTarget(result.statuses, targetName);
            this.intygProperties.isRevoked = IntygHelper.isRevoked(result.statuses);
            if (this.intygProperties.isRevoked) {
                this.intygProperties.printStatus = 'revoked';
            } else {
                this.intygProperties.printStatus = 'signed';
            }

            if (typeof result.relations !== 'undefined') {
                this.intygProperties.relations = result.relations;
            }
        };

        this.updateActiveError = function(error, signed) {
            $log.debug('Loading intyg - got error: ' + error.message);
            if (error.errorCode === 'DATA_NOT_FOUND') {
                this.activeErrorMessageKey = 'common.error.data_not_found';
            } else if (error.errorCode === 'AUTHORIZATION_PROBLEM') {
                this.activeErrorMessageKey = 'common.error.could_not_load_cert_not_auth';
            } else {
                if (signed) {
                    this.activeErrorMessageKey = 'common.error.sign.not_ready_yet';
                } else {
                    this.activeErrorMessageKey = 'common.error.could_not_load_cert';
                }
            }
        };

        /**
         * When a deep-integration user requests an intyg, the request (see stateParams) may contain name and address
         * as query parameters. This method matches the supplied stateParams (if applicable) with the patient name on
         * the requested certificate and returns true if the name has changed.
         */
        this.hasChangedName = function(intygModel, stateParams) {
            if (ObjectHelper.isDefined(stateParams.fornamn) && ObjectHelper.isDefined(stateParams.efternamn)) {
                return intygModel.grundData.patient.fornamn !== stateParams.fornamn ||
                    intygModel.grundData.patient.efternamn !== stateParams.efternamn;
            }
            return false;
        };

        /**
         * When a deep-integration user requests an intyg, the request (see stateParams) may contain name and address
         * as query parameters. This method matches the supplied stateParams (if applicable) with the patient address on
         * the requested certificate and returns true if the address has changed.
         */
        this.hasChangedAddress = function(intygModel, stateParams) {
            if (ObjectHelper.isDefined(stateParams.postort) && ObjectHelper.isDefined(stateParams.postadress) && ObjectHelper.isDefined(stateParams.postnummer)) {
                return intygModel.grundData.patient.postort !== stateParams.postort ||
                    intygModel.grundData.patient.postadress !== stateParams.postadress ||
                    intygModel.grundData.patient.postnummer !== stateParams.postnummer;
            }
            return false;
        };

        this.reset();
    }]
);
