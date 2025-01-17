/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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
angular.module('fk7263').service('fk7263.EditCertCtrl.ViewStateService',
    ['$log', 'common.UtkastViewStateService', 'fk7263.Domain.IntygModel',
        function($log, CommonViewState, IntygModel) {
            'use strict';


            //We still need a barebone utkast viewstate to utilize utkastservice load functionality...
            this.common = CommonViewState;

            this.intygModel = undefined;
            this.draftModel = undefined;

            this.setDraftModel = function(draftModel){
                this.draftModel = draftModel;
                this.intygModel = draftModel.content;
            };


            this.reset = function() {
                CommonViewState.reset();
                CommonViewState.intyg.type = 'fk7263';
                this.setDraftModel(IntygModel._members.build());
                return this;
            };


            this.reset();
        }]);
