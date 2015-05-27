/**
 * Person ID Validator Service
 * Service to check if a value is a valid personnummer or samordningsnummer. The validation follows the specification
 * in SKV 704 and SKV 707. The model holds the number in the format ååååMMdd-nnnn (or ååååMMnn-nnnn in the case of
 * samordningsnummer) but it allows the user to input the number in any of the valid formats.
 *
 * Created by bennysamuelsson on 27/05/15.
 */
angular.module('common').factory('common.PersonIdValidatorService',function() {
    'use strict';

    var PERSONNUMMER_REGEXP = /^(\d{2})?(\d{2})(\d{2})([0-3]\d)([-+]?)(\d{4})$/;
    var SAMORDNINGSNUMMER_REGEXP = /^(\d{2})?(\d{2})(\d{2})([6-9]\d)-?(\d{4})$/;

    var MAXIMUM_AGE = 125;

    var isCheckDigitValid = function(value) {

        // Remove separator.
        var cleanValue = value.replace(/[-+]/, '');

        // Multiply each of the digits with 2,1,2,1,...
        var digits = cleanValue.substring(0, cleanValue.length - 1).split('');
        var multipliers = [2, 1, 2, 1, 2, 1, 2, 1, 2];
        var digitsMultiplied = '';
        for (var i = 0; i < digits.length; i++) {
            digitsMultiplied += parseInt(digits[i], 10) * multipliers[i];
        }

        // Calculate the sum of all of the digits.
        digits = digitsMultiplied.split('');
        var sum = 0;
        for (i = 0; i < digits.length; i++) {
            sum += parseInt(digits[i], 10);
        }
        sum = sum % 10;

        // Get the specified check digit.
        var checkDigit = cleanValue.substring(cleanValue.length - 1);

        if (sum === 0 && checkDigit === '0') {
            return true;
        } else {
            return (10 - sum) === parseInt(checkDigit, 10);
        }
    };

    function isDateValid(dateStr) {
        // dateStr is on the format YYYY/MM/DD
        return moment(dateStr, 'YYYY/MM/DD').isValid();
    }

    function isDateInValidRange(dateStr) {
        // A date is not allowed to be greater than 125 years
        var yearsDiff = moment().diff(moment(dateStr, 'YYYY/MM/DD'), 'years');
        return yearsDiff < MAXIMUM_AGE + 1;
    }

    var formatPersonnummer = function(date, number) {
        return '' + date.getFullYear() + pad(date.getMonth() + 1) + pad(date.getDate()) + '-' + number;
    };

    var formatSamordningsnummer = function(date, number) {
        return '' + date.getFullYear() + pad(date.getMonth() + 1) + pad(date.getDate() + 60) + '-' + number;
    };

    function pad(number) {
        return number < 10 ? '0' + number : number;
    }

    function _validatePersonnummer(id){
        var date;
        var dateStr;

        // Try to match personnummer since that case is most common.
        var parts = PERSONNUMMER_REGEXP.exec(id);
        // Reset regexp. Apparently exec continues from last match rather than starting over in IE8.
        PERSONNUMMER_REGEXP.lastIndex = 0;
        if (parts) {

            // Parse with yyyy-MM-dd to make sure we get parse errors.
            // new Date('2010-02-41') is invalid but new Date(2010, 1, 41) is valid.

            if (parts[1]) {
                dateStr = parts[1] + parts[2] + '/' + parts[3] + '/' + parts[4];

                // Handle invalid dates.
                if ( !(isDateValid(dateStr) && isDateInValidRange(dateStr)) ) {
                    return undefined;
                }

                date = new Date(dateStr);

            } else {

                // Assume that the date is in 20xx and fix later.
                dateStr = (parseInt(parts[2], 10) + 2000) + '/' + parts[3] + '/' + parts[4];

                // Handle invalid dates.
                if (!isDateValid(dateStr)) {
                    return undefined;
                }

                date = new Date(dateStr); //<-- IE8 can't parse 2000-01-01 dates this way. Using '/' instead

                // Make sure the date is not in the future.
                if (date > new Date()) {
                    date.setFullYear(date.getFullYear() - 100);
                }

                // Handle persons older than 100 years.
                if (parts[5] === '+') {
                    date.setFullYear(date.getFullYear() - 100);
                }
            }

            if (isCheckDigitValid(parts[2] + parts[3] + parts[4] + parts[6])) {
                return formatPersonnummer(date, parts[6]);
            } else {
                return undefined;
            }
        }

        return null;
    }

    function _validateSamordningsnummer(id) {
        var date;
        var dateStr;

        var parts = SAMORDNINGSNUMMER_REGEXP.exec(id);
        // Reset regexp. Apparently exec continues from last match rather than starting over in IE8
        SAMORDNINGSNUMMER_REGEXP.lastIndex = 0;
        if (parts) {

            // Parse with yyyy-MM-dd to make sure we get parse errors.
            // new Date('2010-02-41') is invalid but new Date(2010, 1, 41) is valid.

            var day = parseInt(parts[4], 10) - 60; // 60 is the special number for samordningsnummer.

            if (parts[1]) {
                dateStr = parts[1] + parts[2] + '/' + parts[3] + '/' + pad(day);

                // Handle invalid dates.
                if ( !(isDateValid(dateStr) && isDateInValidRange(dateStr)) ) {
                    return undefined;
                }

                date = new Date(dateStr);

            } else {

                // Assume that the date is in 20xx and fix later.
                dateStr = (parseInt(parts[2], 10) + 2000) + '/' + parts[3] + '/' + pad(day);

                // Handle invalid dates.
                if ( !(isDateValid(dateStr) && isDateInValidRange(dateStr)) ) {
                    return undefined;
                }

                date = new Date(dateStr); //<-- IE8 can't parse Y-m-d these dates this way. Using '/' instead

                // Make sure the date is not in the future.
                if (date > new Date()) {
                    date.setFullYear(date.getFullYear() - 100);
                }
            }

            if (isCheckDigitValid(parts[2] + parts[3] + parts[4] + parts[5])) {
                return formatSamordningsnummer(date, parts[5]);
            } else {
                return undefined;
            }
        }

        return null;
    }

    function _validate(id) {

        // if the number is valid a number will be returned, otherwise undefined is returned.
        var result = _validatePersonnummer(id);

        if(result !== null) {
            // validatePersonnummer could process the id and result has the conclusion.
            // undefined === invalid personnummer, number === valid personnummer
            return result;
        } else {
            // Personnummer validation didn't apply (null), check if its a samordningsnummer instead.
            result = _validateSamordningsnummer(id);

            if(result !== null) {
                // validateSamordningsnummer could process the id and result has the conclusion.
                // undefined === invalid samordningsnummer, number === valid samordningsnummer
                return result;
            }
        }

        // Doesn't even match the regexps so it must be invalid.
        return undefined;
    }

    return {
        validate: _validate,
        validatePersonnummer: _validatePersonnummer,
        validateSamordningsnummer: _validateSamordningsnummer
    };
});
