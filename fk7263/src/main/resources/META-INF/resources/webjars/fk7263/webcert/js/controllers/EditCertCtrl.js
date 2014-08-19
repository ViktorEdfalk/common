angular.module('fk7263').controller('fk7263.EditCertCtrl',
    [ '$anchorScroll', '$filter', '$location', '$scope', '$log', 'common.CertificateService',
        'common.ManageCertView', 'common.User',
        function($anchorScroll, $filter, $location, $scope, $log, CertificateService, ManageCertView, User) {
            'use strict';

            $scope.cert = {};
            $scope.hasSavedThisSession = false;
            $scope.messages = [];
            $scope.isComplete = false;
            $scope.isSigned = false;
            $scope.user = User;

            // init state
            $scope.widgetState = {
                doneLoading: false,
                activeErrorMessageKey: null,
                hasError: false,
                showComplete: false,
                collapsedHeader: false
            };

            // Keeps track of which nedsatt fields that are invalid from onChange checks
            $scope.nedsattInvalid = {
                nedsattMed25from: false, nedsattMed25tom: false,
                nedsattMed50from: false, nedsattMed50tom: false,
                nedsattMed75from: false, nedsattMed75tom: false,
                nedsattMed100from: false, nedsattMed100tom: false
            };

            $scope.today = new Date();
            $scope.today.setHours(0, 0, 0, 0); // reset time to
            // increase comparison accuracy (using new Date() also sets time)

            $scope.toggleHeader = function() {
                $scope.widgetState.collapsedHeader = !$scope.widgetState.collapsedHeader;
            };

            $scope.toggleShowComplete = function() {
                $scope.widgetState.showComplete = !$scope.widgetState.showComplete;
                if ($scope.widgetState.showComplete) {

                    var old = $location.hash();
                    $location.hash('top');
                    $anchorScroll();
                    // reset to old to keep any additional routing logic from kicking in
                    $location.hash(old);
                }
            };

            $scope.form = {
                'identity': {
                    'ID-kort': 'ID_KORT',
                    'Företagskort eller tjänstekort': 'FORETAG_ELLER_TJANSTEKORT',
                    'Körkort': 'KORKORT',
                    'Personlig kännedom': 'PERS_KANNEDOM',
                    'Försäkran enligt 18 kap. 4§': 'FORSAKRAN_KAP18',
                    'Pass': 'PASS'
                },
                'korkortd': false,
                'behorighet': true,
                'arbete': 'NUVARANDE',
                'rehab': 'NEJ'
            };

            $scope.testerror = false;

            // Input limit handling
            $scope.inputLimits = {
                diagnosBeskrivning: 100,
                sjukdomsforlopp: 270,
                funktionsnedsattning: 560,
                aktivitetsbegransning: 1100,
                nuvarandeArbetsuppgifter: 120,
                arbetsformagaPrognos: 600,
                atgardInomSjukvarden: 66,
                annanAtgard: 66,
                ovrigt: 420
                // 420 = combined field 13 (and dependencies) limit
            };

            // Watch Fält 4b -> Annat and update backend model when view changes.
            $scope.$watchCollection('[cert.otherData.baseradPaAnnat, basedOnState.check.annanReferens]',
                function() {
                    if ($scope.cert.otherData !== undefined) {
                        if (!$scope.cert.otherData.baseradPaAnnat ||
                            $scope.cert.otherData.baseradPaAnnat === '' || !$scope.basedOnState.check.annanReferens) {
                            $scope.cert.annanReferensBeskrivning = null;
                            return;
                        }
                        $scope.cert.annanReferensBeskrivning = $scope.cert.otherData.baseradPaAnnat;
                    }
                });

            // Watch Fält 10 -> Går ej att bedöma and update backend model when view changes.
            $scope.$watchCollection('[cert.otherData.prognosisClarification, cert.prognosis]',
                function() {
                    if ($scope.cert.otherData !== undefined) {
                        if (!$scope.cert.otherData.prognosisClarification ||
                            $scope.cert.otherData.prognosisClarification === '' ||
                            $scope.cert.prognosis !== 'UNKNOWN') {
                            $scope.cert.arbetsformagaPrognosGarInteAttBedomBeskrivning = null;
                            return;
                        }
                        $scope.cert.arbetsformagaPrognosGarInteAttBedomBeskrivning =
                            $scope.cert.otherData.prognosisClarification;
                    }
                });

            $scope.limitFieldLength = function(field) {
                $scope.cert[field] = $scope.cert[field].substr(0, $scope.inputLimits[field]);
            };

            $scope.limitOtherField = function(field) {
                function limitOvrigtLength(val) {
                    var totalOvrigtLength = $scope.getTotalOvrigtLength();
                    if (totalOvrigtLength > $scope.inputLimits.ovrigt) {
                        // Remove characters over limit from current field
                        return val.substr(0, val.length - (totalOvrigtLength - $scope.inputLimits.ovrigt));
                    }
                    return val;
                }

                $scope.cert[field] = limitOvrigtLength($scope.cert[field]);
            };

            $scope.getTotalOvrigtLength = function() {
                function getLengthOrZero(value) {
                    if (value === undefined) {
                        return 0;
                    } else {
                        return value.length;
                    }
                }

                var totalOvrigtLength = getLengthOrZero($scope.cert.kommentar);

                if ($scope.cert.otherData !== undefined) {
                    totalOvrigtLength = getLengthOrZero($scope.cert.otherData.baseradPaAnnat) +
                        getLengthOrZero($scope.cert.otherData.workingHours25) +
                        getLengthOrZero($scope.cert.otherData.workingHours50) +
                        getLengthOrZero($scope.cert.otherData.workingHours75) +
                        getLengthOrZero($scope.cert.otherData.workingHours100) +
                        getLengthOrZero($scope.cert.otherData.prognosisClarification);
                }
                if ($scope.cert.otherData !== undefined) {
                    if ($scope.cert.otherData.rehabWhen instanceof Date) {
                        totalOvrigtLength += ($filter('date')
                        ($scope.cert.otherData.rehabWhen, 'yyyy-MM-dd')).length;
                    }
                }
                // NOTE: this date (rehabWhen) will probably
                // need a label and therefore
                // use more than the length of the date when
                // merged with the other
                // fields.
                // Remember to add length for the label as well
                // (probably applies to all
                // in cert.otherData)
                return totalOvrigtLength;
            };

            // Based on handling (4b)
            $scope.basedOnState = {
                check: {
                    undersokningAvPatienten: false,
                    telefonkontaktMedPatienten: false,
                    journaluppgifter: false,
                    annanReferens: false
                }
            };

            $scope.autoEnterDate = function(modelName) {

                // Set todays date when a baserat pa field is checked
                if ($scope.basedOnState.check[modelName]) {
                    if ($scope.cert[modelName] === undefined || $scope.cert[modelName] === '') {
                        $scope.cert[modelName] = $filter('date')($scope.today, 'yyyy-MM-dd');
                    }
                } else {

                    // Clear date if check is unchecked
                    $scope.cert[modelName] = '';
                }
            };

            $scope.onChangeBaserasPaDate = function(baserasPaType) {
                if ($scope.cert[baserasPaType] !== undefined && isDate($scope.cert[baserasPaType])) {
                    $scope.basedOnState.check[baserasPaType] = true;
                }
            };

            // Diagnose handling (2)
            $scope.diagnoseCodes = [
                {
                    value: 'J44.0',
                    label: 'J44.0 Kroniskt obstruktiv lungsjukdom med akut nedre luftvägsinfektion'
                },
                {
                    value: 'K92.2',
                    label: 'K92.2 Gastrointestinal blödning, ospecificerad'
                }
            ];

            $scope.diagnoses = [
                {
                    value: 'Kroniskt obstruktiv lungsjukdom med akut nedre luftvägsinfektion',
                    label: 'J44.0 Kroniskt obstruktiv lungsjukdom med akut nedre luftvägsinfektion'
                },
                {
                    value: 'Gastrointestinal blödning, ospecificerad',
                    label: 'K92.2 Gastrointestinal blödning, ospecificerad'
                }
            ];

            // Arbetsförmåga handling (8b)
            $scope.workState = {
                nedsattMed25: false,
                nedsattMed50: false,
                nedsattMed75: false,
                nedsattMed100: false
            };

            /**
             * 8b: Called when checks or dates for Arbetsförmåga are changed. Update dependency controls here
             */
            function onArbetsformagaDatesUpdated() {
                $scope.updateTotalCertDays();

                var rangeDates = findStartEndDates();
                checkArbetsformagaDatesRange(rangeDates.minDate);

                var periodDates = findStartEndDates();
                checkArbetsformagaDatesPeriodLength(periodDates.minDate, periodDates.maxDate);
            }

            /**
             * 8b: Check that the earliest startdate in arbetsförmåga is no more than a week before today and no more than 6 months in the future
             * @type {boolean}
             */
            $scope.datesOutOfRange = false;
            function checkArbetsformagaDatesRange(startDate) {
                if (!startDate) {
                    $scope.datesOutOfRange = false;
                    return;
                }

                var olderThanAWeek = moment(startDate).isBefore(moment().subtract('days', 7));
                var moreThanSixMonthsInFuture = moment(startDate).isAfter(moment().add('months', 6));
                $scope.datesOutOfRange = (olderThanAWeek || moreThanSixMonthsInFuture);
            }

            /**
             * 8b: Check that the period between the earliest startdate and the latest end date is no more than 6 months in the future
             * @type {boolean}
             */
            $scope.datesPeriodTooLong = false;
            function checkArbetsformagaDatesPeriodLength(startDate, endDate) {
                if (!startDate || !endDate) {
                    $scope.datesPeriodTooLong = false;
                    return;
                }

                $scope.datesPeriodTooLong = (Math.abs(moment(startDate).diff(endDate, 'months')) >= 6);
            }

            /**
             * 8b: find earliest and latest dates for arbetsförmåga
             * @returns {{minDate: null, maxDate: null}}
             */
            function findStartEndDates() {
                var dates = {
                    minDate: null,
                    maxDate: null
                };
                var startDates = [];
                var endDates = [];

                if ($scope.cert.nedsattMed25) {
                    startDates.push($scope.cert.nedsattMed25.from);
                    endDates.push($scope.cert.nedsattMed25.tom);
                }
                if ($scope.cert.nedsattMed50) {
                    startDates.push($scope.cert.nedsattMed50.from);
                    endDates.push($scope.cert.nedsattMed50.tom);
                }
                if ($scope.cert.nedsattMed75) {
                    startDates.push($scope.cert.nedsattMed75.from);
                    endDates.push($scope.cert.nedsattMed75.tom);
                }
                if ($scope.cert.nedsattMed100) {
                    startDates.push($scope.cert.nedsattMed100.from);
                    endDates.push($scope.cert.nedsattMed100.tom);
                }

                dates.minDate = getMinMaxDate('min', startDates);
                dates.maxDate = getMinMaxDate('max', endDates);
                return dates;
            }

            /**
             * Does supplied date look like an iso date XXXX-XX-XX (not a complete validation)?
             * @param date
             * @returns {*}
             */
            var ISODATE_REGEXP = /^\d{4}-\d{2}-\d{2}$/;
            function isDate(date) {
                var validDateFormat = ISODATE_REGEXP.test(date);
                return validDateFormat;
            }

            /**
             * Get earliest or latest date in a list of dates
             * @param comparisonType
             * @param dates
             * @returns {boolean}
             */
            function getMinMaxDate(comparisonType, dates) {

                var compareDate = false;
                for (var i = 0; i < dates.length; i++) {
                    if (isDate(dates[i])) {
                        if (!compareDate || // no valid date found yet
                            (comparisonType === 'min' && dates[i] < compareDate) || // looking for min date
                            (comparisonType === 'max' && dates[i] > compareDate)) { // looking for max date
                            compareDate = dates[i];
                        }
                    }
                }

                // if no valid dates, compareDate is still false, otherwise contains the lowest/highest date
                // sent depending on comparisonType
                return compareDate;
            }

            /**
             * Convert a date into time ms since 1970-01-01
             * @param date
             * @returns {number}
             */
            function convertDateToTime(date) {
                var splitDate = date.split('-');
                var time = (new Date(splitDate[0], splitDate[1], splitDate[2])).getTime();
                return time;
            }

            /**
             * Calculate total days between the earliest and the latest dates supplied in the 8b controls
             * @type {boolean}
             */
            $scope.totalCertDays = false;
            $scope.updateTotalCertDays = function() {
                var oneDay = 24 * 60 * 60 * 1000;
                var dates = findStartEndDates();
                if (!dates.minDate || !dates.maxDate) {
                    // return if there's no valid range span yet
                    $scope.totalCertDays = false;
                    return $scope.totalCertDays;
                }

                dates.minDate = convertDateToTime(dates.minDate);
                dates.maxDate = convertDateToTime(dates.maxDate);
                $scope.totalCertDays = Math.round(Math.abs((dates.minDate - dates.maxDate) / (oneDay))) + 1;
            };

            /**
             * Watches for rehab
             */

                // Rekommendationer 6a, 7, 11
            $scope.$watch('cert.rehabNow', function(newVal) {
                if ($scope.cert.otherData !== undefined) {
                    if (newVal === 'LATER' && $scope.cert.otherData.rehabWhen === '') {
                        $scope.cert.otherData.rehabWhen = $scope.today;
                    } else if (newVal === 'NOW') {
                        $scope.cert.otherData.rehabWhen = '';
                    }
                }
            });

            $scope.$watch('cert.otherData.rehabWhen', function(newVal) {
                if (isDate(newVal)) {
                    $scope.cert.rehabNow = 'LATER';
                }
            });

            /**
             * Set a default value to listed properties on an object
             * @param list
             * @param propertyNames
             * @param defaultValue
             */
            function setPropertyDefaults(list, propertyNames, defaultValue) {
                for (var i = 0; i < propertyNames.length; i++) {
                    if (list[propertyNames[i]] === undefined) {
                        list[propertyNames[i]] = defaultValue;
                    }
                }
            }

            function convertCertToForm($scope) {

                // Set blank defaults
                var propertyNames = ['diagnosBeskrivning', 'wcDiagnosBeskrivning1', 'wcDiagnosKod2',
                    'wcDiagnosBeskrivning2', 'wcDiagnosKod3', 'wcDiagnosBeskrivning3' ];
                setPropertyDefaults($scope.cert, propertyNames, '');

                // Set prognosis default value
                if ($scope.cert.prognosis === undefined) {
                    $scope.cert.prognosis = 'YES';
                }

                // Arbete radio conversions
                if($scope.cert.arbetsloshet) {
                    $scope.form.arbete = 'ARBETSLOSHET';
                }
                else if($scope.cert.foraldrarledighet) {
                    $scope.form.arbete = 'FORALDRALEDIGHET';
                }

                // Rehab radio conversions
                if ($scope.cert.rehabiliteringAktuell) {
                    $scope.form.rehab = 'JA';
                }
                else if ($scope.cert.rehabiliteringEjAktuell) {
                    $scope.form.rehab = 'NEJ';
                }
                else if ($scope.cert.rehabiliteringGarInteAttBedoma) {
                    $scope.form.rehab = 'GAREJ';
                }
            }

            function convertFormToCert($scope) {

                // Arbete radio conversions
                $scope.cert.arbetsloshet = false;
                $scope.cert.foraldrarledighet = false;

                switch($scope.form.arbete) {
                    case 'ARBETSLOSHET': $scope.cert.arbetsloshet = true; break;
                    case 'FORALDRALEDIGHET': $scope.cert.foraldrarledighet = true; break;
                }

                // Rehab radio conversions
                $scope.cert.rehabiliteringAktuell = false;
                $scope.cert.rehabiliteringEjAktuell = false;
                $scope.cert.rehabiliteringGarInteAttBedoma = false;

                switch($scope.form.rehab) {
                    case 'JA': $scope.cert.rehabiliteringAktuell = true; break;
                    case 'NEJ': $scope.cert.rehabiliteringEjAktuell = true; break;
                    case 'GAREJ': $scope.cert.rehabiliteringGarInteAttBedoma = true; break;
                }
            }

            // Get the certificate draft from the server.
            ManageCertView.load($scope, function(cert) {
                // Decorate intygspecific default data
                $scope.cert = cert;
                convertCertToForm($scope);
            });

            /**
             * Action to save the certificate draft to the server.
             */
            $scope.save = function() {
                $scope.hasSavedThisSession = true;
                convertFormToCert($scope);
                ManageCertView.save($scope);
            };

            /**
             * Action to discard the certificate draft and return to WebCert again.
             */
            $scope.discard = function() {
                ManageCertView.discard($scope);
            };

            /**
             * Action to sign the certificate draft and return to Webcert again.
             */
            $scope.sign = function() {
                ManageCertView.signera($scope, 'fk7263');
            };

            /**
             * Print draft
             */
            $scope.print = function() {
                ManageCertView.printDraft($scope.cert.id);
            };

            /**
             * Update arbetsformaga dates when checkbox is updated
             * @param nedsattModelName
             */
            $scope.onChangeWorkStateCheck = function(nedsattModelName) {
                if ($scope.cert !== undefined) {
                    if ($scope.workState[nedsattModelName]) {

                        // Set suggested dates
                        var workstate = $scope.cert[nedsattModelName];
                        if (!workstate) {
                            workstate = $scope.cert[nedsattModelName] = {};
                        }

                        // Set from date
                        var dates = findStartEndDates();
                        if (!workstate.from || !isDate(workstate.from)) {

                            // find highest max date
                            if(!dates.maxDate) {
                                // if no maxdate is available, use today
                                var today = ($filter('date')($scope.today, 'yyyy-MM-dd'));
                                workstate.from = today;
                            } else {
                                workstate.from = moment(dates.maxDate).add('days', 1).format('YYYY-MM-DD');
                            }
                        }

                        // Set tom date
                        if (!workstate.tom || !isDate(workstate.tom)) {
                            workstate.tom = moment(workstate.from).add('days', 7).format('YYYY-MM-DD');
                        }
                    } else {

                        // Remove dates
                        delete $scope.cert[nedsattModelName];
                    }

                    onArbetsformagaDatesUpdated();
                }
            };

            /**
             * Set checkbox and non-selected date for arbetsformaga % when a date is changed
             * @param nedsattModelName
             * @param fromTom
             */
            $scope.onChangeNedsattMed = function(nedsattModelName, fromTom) {

                // Bail out if model hasn't been loaded yet
                var nedsattModel = $scope.cert[nedsattModelName];
                if (nedsattModel === undefined) {
                    return;
                }

                var dateField = $scope.cert[nedsattModelName][fromTom];
                if (dateField === undefined) {
                    return;
                }

                // if a valid date has been set
                if (dateField !== undefined && isDate(dateField)) {

                    // Check checkbox
                    $scope.workState[nedsattModelName] = true;

                    // If non-changed date for same % is still invalid, set that as well
                    if (fromTom === 'from' && !isDate(nedsattModel.tom)) {
                        nedsattModel.tom = nedsattModel.from;
                    } else if (fromTom === 'tom' && !isDate(nedsattModel.from)) {
                        nedsattModel.from = nedsattModel.tom;
                    }

                    // Set invalid if from dates are after tom dates
                    var groups = ['nedsattMed25','nedsattMed50','nedsattMed75','nedsattMed100'];
                    for(var i = 0; i < groups.length; i++) {
                        if($scope.cert[groups[i]] && $scope.cert[groups[i]].from && $scope.cert[groups[i]].tom) {
                            if(moment($scope.cert[groups[i]].from).isAfter(moment($scope.cert[groups[i]].tom))) {
                                $scope.nedsattInvalid[groups[i]+'from'] = true;
                                $scope.nedsattInvalid[groups[i]+'tom'] = true;
                            } else {
                                $scope.nedsattInvalid[groups[i]+'from'] = false;
                                $scope.nedsattInvalid[groups[i]+'tom'] = false;
                            }
                        } else {
                            $scope.nedsattInvalid[groups[i]+'from'] = false;
                            $scope.nedsattInvalid[groups[i]+'tom'] = false;
                        }
                    }

                    // Set invalid if date periods overlap
                    for(var i = 0; i < groups.length; i++) {
                        // for every nedsatt group
                        var nedsatt = $scope.cert[groups[i]];

                        // where group is used, set and not already marked as invalid
                        if(nedsatt && nedsatt.from && nedsatt.tom && !($scope.nedsattInvalid[groups[i] + 'from'] && $scope.nedsattInvalid[groups[i] + 'tom'])) {

                            // check with all other period groups after nedsatt period if periods overlap
                            for (var j = i+1; j < groups.length; j++) {
                                var nedsattCompare = $scope.cert[groups[j]];

                                // dont check against unused dates and already invalid dates
                                if (nedsattCompare && nedsattCompare.from && nedsattCompare.tom && !($scope.nedsattInvalid[groups[j] + 'from'] && $scope.nedsattInvalid[groups[j] + 'tom'])) {

                                    if(moment(nedsatt.from).isSame(nedsattCompare.from)) {
                                        $scope.nedsattInvalid[groups[i] + 'from'] = true;
                                        $scope.nedsattInvalid[groups[j] + 'from'] = true;
                                    }
                                    if(moment(nedsatt.tom).isSame(nedsattCompare.from)) {
                                        $scope.nedsattInvalid[groups[i] + 'tom'] = true;
                                        $scope.nedsattInvalid[groups[j] + 'from'] = true;
                                    }
                                    if(moment(nedsatt.from).isSame(nedsattCompare.tom)) {
                                        $scope.nedsattInvalid[groups[i] + 'from'] = true;
                                        $scope.nedsattInvalid[groups[j] + 'tom'] = true;
                                    }
                                    if(moment(nedsatt.tom).isSame(nedsattCompare.tom)) {
                                        $scope.nedsattInvalid[groups[i] + 'tom'] = true;
                                        $scope.nedsattInvalid[groups[j] + 'tom'] = true;
                                    }

                                    if((moment(nedsatt.tom).isAfter(nedsattCompare.from) && moment(nedsatt.from).isBefore(nedsattCompare.from)) // first group overlaps in front
                                       || (moment(nedsatt.from).isBefore(nedsattCompare.tom) && moment(nedsatt.tom).isAfter(nedsattCompare.tom))  // first group overlaps behind
                                       || (moment(nedsatt.from).isBefore(nedsattCompare.from) && moment(nedsatt.tom).isAfter(nedsattCompare.tom)) // first group wraps second group
                                       || (moment(nedsatt.from).isAfter(nedsattCompare.from) && moment(nedsatt.tom).isBefore(nedsattCompare.tom))) { // second group wraps first group
                                        $scope.nedsattInvalid[groups[i] + 'from'] = true;
                                        $scope.nedsattInvalid[groups[i] + 'tom'] = true;
                                        $scope.nedsattInvalid[groups[j] + 'from'] = true;
                                        $scope.nedsattInvalid[groups[j] + 'tom'] = true;
                                    }
                                }
                            }
                        }
                    }

                }

                onArbetsformagaDatesUpdated();
            };
        }]);
