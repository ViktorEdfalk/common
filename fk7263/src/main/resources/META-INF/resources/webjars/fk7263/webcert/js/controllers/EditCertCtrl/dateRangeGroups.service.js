angular.module('fk7263').factory('fk7263.EditCertCtrl.DateRangeGroupsService',
    ['common.DateUtilsService', 'common.UtilsService','fk7263.EditCertCtrl.DateRangeGroupModel', '$log', function( dateUtils, utils, DateRangeGroupModel, $log) {
        'use strict';

        /**
         * Constructor, with class name
         */
        // 8b ---------------------------------------------
        function DateRangeGroupsService(_$scope){
            this.totalCertDays = _$scope.totalCertDays;
            this.datesOutOfRange = _$scope.datesOutOfRange;
            this.datesPeriodTooLong = _$scope.datesPeriodTooLong;

            this.nedsattMed25 = DateRangeGroupModel.build(_$scope, _$scope.workState, _$scope.certForm, _$scope.nedsattInvalid, 'nedsattMed25', '25');
            this.nedsattMed50 = DateRangeGroupModel.build(_$scope, _$scope.workState, _$scope.certForm, _$scope.nedsattInvalid,  'nedsattMed50', '50');
            this.nedsattMed75 = DateRangeGroupModel.build(_$scope, _$scope.workState, _$scope.certForm, _$scope.nedsattInvalid,  'nedsattMed75', '75');
            this.nedsattMed100 = DateRangeGroupModel.build(_$scope, _$scope.workState, _$scope.certForm, _$scope.nedsattInvalid, 'nedsattMed100', '100');

            this.dateRangeGroups = [this.nedsattMed25,this.nedsattMed50,this.nedsattMed75,this.nedsattMed100];

            this._$scope = _$scope;

            // add the parser and formatter...
            this.addNedsattFormatters();

            this.validateDates();
            this.onArbetsformagaDatesUpdated();
        };

        DateRangeGroupsService.prototype.addNedsattFormatters = function addNedsattFormatters(){

            var self = this;

            function nedsattFormatter(dateRangeGroups, modelValue) {
                if (modelValue) {
                    dateRangeGroups.validateDates();
                    dateRangeGroups.onArbetsformagaDatesUpdated();
                }
                return modelValue;
            }

            angular.forEach(this.dateRangeGroups, function(dateRangeGroup){

                // Register parsers so we can follow changes in the date inputs
                var from = dateRangeGroup.nedsattFormFrom;
                var tom = dateRangeGroup.nedsattFormTom;

                if (from) {

                    // add formatters
                    if (from.$formatters.length > 0) {
                        from.$formatters.shift();
                    }
                    from.$formatters.push(function(modelValue) {
                        return nedsattFormatter(self, modelValue);
                    });

                }

                if (from) {

                    // add formatters
                    if (tom.$formatters.length > 0) {
                        tom.$formatters.shift();
                    }
                    tom.$formatters.push(function(modelValue) {
                        return nedsattFormatter(self, modelValue);
                    });
                }

            }, self);
        }

        /**
         * Revalidate 8b dates
         */
        DateRangeGroupsService.prototype.validateDates = function validateDates() {
            this.resetDateInvalidStates();
            this.validateDateRangeOrder(); // Set invalid if from dates are after tom dates
            this.validateDatePeriods(); // Set invalid if date periods overlap
        };

        DateRangeGroupsService.prototype.resetDateInvalidStates = function resetDateInvalidStates() {
            angular.forEach(this.dateRangeGroups, function(dateRangeGroup){
                dateRangeGroup.setDateInvalidState(false);
            });
        };

        DateRangeGroupsService.prototype.validateDateRangeOrder = function validateDateRangeOrder() {

            // Update others still marked as invalid as well if they previously conflicted with the changed one
            angular.forEach(this.dateRangeGroups, function(dateRangeGroup){
                // do the validity check
                dateRangeGroup.setDateValidity();
            });
        }

        /**
         * Validate 8b date periods so they don't overlap or wrap in any way
         */
        DateRangeGroupsService.prototype.validateDatePeriods = function validateDatePeriods() {
            var self = this;
            var compared = [];
            var ids = [];
            angular.forEach(self.dateRangeGroups, function(dateRangeGroup){

                // check with all other period groups after nedsatt period if periods overlap
                angular.forEach(self.dateRangeGroups, function(compareNedsattGroup){
                    ids[0] = dateRangeGroup.id + compareNedsattGroup.id;
                    ids[1] = compareNedsattGroup.id + dateRangeGroup.id;
                    // filter out comparison based on previous values
                    if(compared.indexOf(ids[0]) < 0 && compared.indexOf(ids[1]) < 0){
                        dateRangeGroup.markDateRangeValidity(compareNedsattGroup);
                        compared.push( dateRangeGroup.id+compareNedsattGroup.id );
                    }
                });
            });
        }

        /**
         * 8b: Called when checks or dates for Arbetsförmåga are changed. Update dependency controls here
         */
        DateRangeGroupsService.prototype.onArbetsformagaDatesUpdated = function onArbetsformagaDatesUpdated(useModelValue) {

            var startEndMoments = this.findStartEndMoments(useModelValue);

            this.updateTotalCertDays(startEndMoments);
            this.checkArbetsformagaDatesRange(startEndMoments.minMoment);
            this.checkArbetsformagaDatesPeriodLength(startEndMoments.minMoment, startEndMoments.maxMoment);

        };

        /**
         * 8b: find earliest and latest dates (as moment objects) for arbetsförmåga
         * @returns {{minMoment: null, maxMoment: null}}
         */
        DateRangeGroupsService.prototype.findStartEndMoments = function findStartEndMoments(useModelValue) {
            var moments = {
                minMoment: null,
                maxMoment: null
            };
            var startMoments = [];
            var endMoments = [];

            angular.forEach(this.dateRangeGroups, function(dateRangeGroup){
                var dateValue = dateRangeGroup.momentStrictFrom();
                if(dateValue){
                    startMoments.push(dateValue);
                }
                dateValue = dateRangeGroup.momentStrictTom();
                if(dateValue){
                    endMoments.push(dateValue);
                }
            });

            if (startMoments.length > 0) {
                moments.minMoment = moment.min(startMoments);
            }
            if (endMoments.length > 0) {
                moments.maxMoment = moment.max(endMoments);
            }
            return moments;
        };

        /**
         * Calculate total days between the earliest and the latest dates supplied in the 8b controls
         * @type {boolean}
         */
        DateRangeGroupsService.prototype.updateTotalCertDays = function(moments) {
            if(!moments){
                this.totalCertDays = false;
                return;
            }
            this.totalCertDays = dateUtils.daysBetween(moments.minMoment, moments.maxMoment);
        };

        /**
         * 8b: Check that the earliest startdate in arbetsförmåga is no more than a week before today and no more than 6 months in the future
         * @type {boolean}
         */
        DateRangeGroupsService.prototype.checkArbetsformagaDatesRange = function checkArbetsformagaDatesRange(startMoment) {
            this.datesOutOfRange = (dateUtils.olderThanAWeek(startMoment) || dateUtils.isDateOutOfRange(startMoment));
        }

        /**
         * 8b: Check that the period between the earliest startdate and the latest end date is no more than 6 months in the future
         * @type {boolean}
         */
        DateRangeGroupsService.prototype.checkArbetsformagaDatesPeriodLength = function checkArbetsformagaDatesPeriodLength(startMoment, endMoment) {
            this.datesPeriodTooLong = !dateUtils.areDatesWithinMonthRange(startMoment, endMoment);
        }

        // static build
        DateRangeGroupsService.build = function(_$scope){
            return new DateRangeGroupsService(_$scope);
        }

        /**
         * Return the constructor function DateRangeGroupModel
         */
        return DateRangeGroupsService;

    }]);