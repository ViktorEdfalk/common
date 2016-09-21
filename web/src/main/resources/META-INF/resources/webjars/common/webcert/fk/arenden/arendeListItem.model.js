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
 * Created by BESA on 2015-02-09.
 */

angular.module('common').factory('common.ArendeListItemModel',
    ['$log', 'common.UserModel', 'common.ObjectHelper', 'common.messageService',
        function($log, UserModel, ObjectHelper, messageService) {
        'use strict';

        /**
         * Constructor
         */
        function ArendeListItemModel(arendeModel, extraKompletteringarArende) {
            this.answerDisabled = false;
            this.answerDisabledReason = '';
            this.svaraMedNyttIntygDisabled = false;
            this.svaraMedNyttIntygDisabledReason = '';
            this.atgardMessageId = '';
            this.arende = arendeModel; // ArendeModel from backend
            this.kompletteringar = []; // this is created in updateArendeListItem since dynamic text ids needs to be created from arende.fraga.kompletteringar
            this.extraKompletteringarArenden = [];
            if (extraKompletteringarArende) {
                this.extraKompletteringarArenden = extraKompletteringarArende.map(function(extraKompletteringarArende) {
                    var newItem = ArendeListItemModel.build(extraKompletteringarArende);
                    newItem.parentListItem = this;
                    newItem.updateArendeListItem();
                    return newItem;
                }, this);
            }
            this.updateArendeListItem();
        }

        function _isKomplettering(amne) {
            return amne === 'KOMPLETTERING_AV_LAKARINTYG' || amne === 'KOMPLT';
        }

        function _isPaminnelse(amne) {
            return amne === 'PAMINNELSE' || amne === 'PAMINN';
        }

        ArendeListItemModel.build = function(arendeModel, extraKompletteringarArende) {
            return new ArendeListItemModel(arendeModel, extraKompletteringarArende);
        };

        ArendeListItemModel.prototype.updateArendeListItem = function () {

            if (!ObjectHelper.isDefined(this.arende.svar)) {
                this.arende.svar = {
                    meddelande: ''
                };
            }

            this._updateListItemState();
            this._updateAtgardMessage();
            this._updateKompletteringar();
        };

        ArendeListItemModel.prototype._updateListItemState = function() {
            if (this.arende.fraga.status === 'CLOSED') {
                this.answerDisabled = true;
                this.answerDisabledReason = undefined; // En avslutat konversation kan inte besvaras
            } else if (_isPaminnelse(this.arende.fraga.amne)) {
                // RE-020 Påminnelser is never
                // answerable
                this.answerDisabled = true;
                this.answerDisabledReason = undefined; // Påminnelser kan inte besvaras men det behöver vi inte säga
            } else if (this.arende.fraga.status !== 'CLOSED' &&
                _isKomplettering(this.arende.fraga.amne) &&
                !UserModel.hasPrivilege(UserModel.privileges.BESVARA_KOMPLETTERINGSFRAGA)) {
                // RE-005, RE-006
                this.answerDisabled = true;
                this.answerDisabledReason = messageService.getProperty('common.arende.komplettering.disabled.onlydoctor');
            } else {
                this.answerDisabled = false;
                this.answerDisabledReason = undefined;
            }

            if (_isKomplettering(this.arende.fraga.amne) &&
                UserModel.hasRequestOrigin(UserModel.requestOrigins.UTHOPP)) {
                this.svaraMedNyttIntygDisabled = true;
                this.svaraMedNyttIntygDisabledReason = messageService.getProperty('common.arende.komplettering.disabled.svaramedintyg.uthopp');
            } else {
                this.svaraMedNyttIntygDisabled = false;
            }
        };

        ArendeListItemModel.prototype._updateAtgardMessage = function() {
            if (this.arende.fraga.status === 'CLOSED') {
                this.atgardMessageId = 'handled';
            } else if (this._isUnhandledForDecoration()) {
                this.atgardMessageId = 'markhandled';
            } else if (_isKomplettering(this.arende.fraga.amne)) {
                this.atgardMessageId = 'komplettering';
            } else {
                if (this.arende.fraga.status === 'PENDING_INTERNAL_ACTION') {
                    this.atgardMessageId = 'svarfranvarden';
                } else if (this.arende.fraga.status === 'PENDING_EXTERNAL_ACTION') {
                    this.atgardMessageId = 'svarfranfk';
                } else {
                    this.atgardMessageId = '';
                    $log.debug('warning: undefined status');
                }
            }
        };

        ArendeListItemModel.prototype._updateKompletteringar = function() {
            if (ObjectHelper.isDefined(this.arende.fraga.kompletteringar)) {
                this.kompletteringar = [];
                angular.forEach(this.arende.fraga.kompletteringar, function(komplettering){

                    var newKompletteringListItem = {
                        frgId: 'FRG_' + komplettering.frageId + '.RBK',
                        text: komplettering.text,
                        modelName: komplettering.jsonPropertyHandle,
                        id: komplettering.frageId
                    };

                    if (komplettering.jsonPropertyHandle === 'tillaggsfragor') {
                        newKompletteringListItem.frgId = 'DFR_' + komplettering.frageId + '.1.RBK';
                    }

                    this.push(newKompletteringListItem);
                }, this.kompletteringar);
            }
        };

        ArendeListItemModel.prototype._isUnhandledForDecoration = function(){
            return this.arende.fraga.status === 'ANSWERED' || this.arende.fraga.amne === 'MAKULERING' || _isPaminnelse(this.arende.fraga.amne);
        };

        return ArendeListItemModel;
    }
]);
