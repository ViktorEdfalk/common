angular.module('common').service('common.UtkastViewStateService',
    ['$stateParams', function($stateParams) {
        'use strict';

        this.reset = function() {
            this.error = {
                activeErrorMessageKey : null,
                saveErrorMessageKey : null
            };
            this.intyg = {
                isComplete : false,
                type : undefined
            };
            this.doneLoading = false;
            this.collapsedHeader = false;

            // TODO: should go into intyg
            this.showComplete = false;
            this.hsaInfoMissing = false;
            this.vidarebefordraInProgress = false;
            this.hospName = $stateParams.hospName;
            this.deleted = false;
            this.isSigned = false;
            this.validationMessages  = null;
            this.validationMessagesGrouped  = null;

            this.draftModel = undefined;
            this.intygModel = undefined;
        };

        this.update = function(draftModel, data) {
            if(draftModel){
                draftModel.update(data);
                this.error.activeErrorMessageKey = null;
                this.error.saveErrorMessageKey = null;

                this.isSigned = draftModel.isSigned();
                this.intyg.isComplete = draftModel.isSigned() || draftModel.isDraftComplete();

                // check if all info is available from HSA. If not, display the info message that someone needs to update it
                this.hsaInfoMissing = false;
                var vardenhetData = draftModel.content.grundData.skapadAv.vardenhet;
                var properties = ['postadress', 'postnummer', 'postort', 'telefonnummer'];
                for(var i = 0; i < properties.length; i++) {
                    var field = vardenhetData[properties[i]];
                    if(field === undefined || field === '') {
                        this.hsaInfoMissing = true;
                        break;
                    }
                }
            }
        };

        this.toggleShowComplete = function() {
            this.showComplete = !this.showComplete;
            return this.showComplete;
        };

        this.toggleCollapsedHeader = function() {
            this.collapsedHeader = !this.collapsedHeader;
        };

        this.reset();
    }
]);
