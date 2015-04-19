angular.module('common').factory('common.Domain.GrundDataModel',
    [ 'common.Domain.SkapadAvModel',
        'common.Domain.PatientModel',  function(SkapadAvModel, PatientModel) {
        'use strict';

        /**
         * Constructor, with class name
         */
        function GrundDataModel() {
            this.skapadAv = SkapadAvModel.build();
            this.patient = PatientModel.build();
        }

        

        GrundDataModel.prototype.update = function (grundData) {
            // refresh the model data
            if(grundData === undefined) {
                return;
            }
            this.skapadAv.update(grundData.skapadAv);
            this.patient.update(grundData.patient);
        };

        GrundDataModel.build = function() {
            return new GrundDataModel();
        };

        /**
         * Return the constructor function GrundDataModel
         */
        return GrundDataModel;

    }]);