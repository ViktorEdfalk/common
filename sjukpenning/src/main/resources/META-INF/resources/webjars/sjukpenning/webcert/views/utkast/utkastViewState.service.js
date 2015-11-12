angular.module('sjukpenning').service('sjukpenning.EditCertCtrl.ViewStateService',
    ['$log', 'common.UtkastViewStateService', 'sjukpenning.Domain.IntygModel', 'sjukpenning.EditCertCtrl.Helper',
        function($log, CommonViewState, IntygModel, helper) {
            'use strict';

            this.common = CommonViewState;

            this.intygModel = undefined;
            this.draftModel = undefined;

            this.setDraftModel = function(draftModel){
                this.draftModel = draftModel;
                this.intygModel = draftModel.content;
            };

            this.avstangningSmittskydd = function() {
                return this.intygModel.avstangningSmittskydd;
            };

            this.inputLimits = {
                arbetsformagaPrognos: 600,
                nuvarandeArbetsuppgifter: 120,
                atgardInomSjukvarden: 66,
                annanAtgard: 66,
                aktivitetsbegransning: 1100,
                funktionsnedsattning: 450,
                sjukdomsforlopp: 270,
                diagnosBeskrivning :220,
                ovrigt: 360
            };

            this.reset = function() {
                CommonViewState.reset();
                CommonViewState.intyg.type = 'sjukpenning';
                this.setDraftModel(IntygModel._members.build());
                return this;
            };

            this.clearModel = function(){
                this.intygModel = undefined;
                this.draftModel = undefined;
            };

            this.reset();
        }]);