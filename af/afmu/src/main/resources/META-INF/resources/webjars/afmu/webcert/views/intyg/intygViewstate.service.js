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
angular.module('afmu').service('afmu.IntygController.ViewStateService',
    ['$log', 'common.IntygViewStateService', 'common.messageService',
        function($log, CommonViewState, messageService) {
            'use strict';

            this.common = CommonViewState;
            this.intygModel = {};

            this.reset = function() {
                this.common.reset();
                this.common.intygProperties.type = 'afmu';
            };

            /**
                Lägg på text om observandum ska visas och returnera hela modellen
             */
            this.getSendContent = function(intygType) {

                var sendContentModel = {
                    observandumId: this.getObservandumId(),
                    bodyText: messageService.getProperty(intygType + '.label.send.body')
                };

                if(sendContentModel.observandumId) {
                    sendContentModel.bodyText = messageService.getProperty('common.label.send.body') + messageService.getProperty(intygType + '.label.send.body');
                }

                return sendContentModel;
            };

            this.reset();
        }]);