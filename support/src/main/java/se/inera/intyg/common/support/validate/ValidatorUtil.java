/*
 * Copyright (C) 2017 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.support.validate;

import com.google.common.base.Strings;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Ints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.InternalLocalDateInterval;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Common utils used for validation.
 *
 * @author Gustav Norbäcker, R2M
 */
public final class ValidatorUtil {

    private static final Logger LOG = LoggerFactory.getLogger(ValidatorUtil.class);
    private static final StringValidator STRING_VALIDATOR = new StringValidator();

    private static final int BASE_10 = 10;

    private ValidatorUtil() {
    }

    /**
     * Calculates the modulo 10 checksum of a numeric string (the luhn algorithm).
     *
     * @param number A numeric string (in order to support leading zeroes).
     * @return The modulo 10 checksum.
     */
    public static int calculateMod10(String number) {
        int cs = 0;
        int multiple = 2;
        for (int i = 0; i < number.length(); i++) {
            int code = Integer.parseInt(number.substring(i, i + 1));
            int pos = multiple * code;
            cs += pos % BASE_10 + pos / BASE_10;
            multiple = (multiple == 1 ? 2 : 1);
        }

        // Subtract the sum modulo 10 from 10.
        // The remainder becomes the checksum. If the remainder is 10 the
        // checksum i 0.
        return (BASE_10 - (cs % BASE_10)) % BASE_10;
    }

    /**
     * Validates that the supplied date is parsable and reasonable. Reasonable is defined as somewhere between
     * {@link InternalDate#MIN_DATE} and {@link InternalDate#MAX_DATE}, typically 1900-01-01 to 2099-12-31.
     *
     * @param date
     *      Date to validate.
     * @param validationMessages
     *      List of validationMessages. Any validation errors and/or warnings are added to this list.
     * @param field
     *      Field identifier, used if a validation entry has to be added.
     * @param message
     *      Special message for field, null if it does not exist.
     * @return
     *      True if valid, false otherwise.
     */
    public static boolean validateDate(InternalDate date, List<ValidationMessage> validationMessages, String field, String message) {

        if (date == null) {
            addValidationError(validationMessages, field, ValidationMessageType.EMPTY);
            return false;
        }

        if (!date.isValidDate()) {
            if (date.isCorrectFormat()) {
                addValidationError(validationMessages, field, ValidationMessageType.INVALID_FORMAT, "common.validation.date_invalid");
            } else if (message != null) {
                addValidationError(validationMessages, field, ValidationMessageType.INVALID_FORMAT, message);
            } else {
                addValidationError(validationMessages, field, ValidationMessageType.INVALID_FORMAT);
            }
            return false;
        }

        if (!date.isReasonable()) {
            addValidationError(validationMessages, field, ValidationMessageType.INVALID_FORMAT,
                    "common.validation.date_out_of_range");
            return false;
        }
        return true;
    }

    /**
     * Validates that the supplied interval is parsable and reasonable. Reasonable is defined as somewhere between
     * {@link InternalDate#MIN_DATE} and {@link InternalDate#MAX_DATE}, typically 1900-01-01 to 2099-12-31.
     *
     * @param interval
     *      InteralLocalDateInterval to validate.
     * @param validationMessages
     *      List of validationMessages. Any validation errors and/or warnings are added to this list.
     * @param field
     *      Field identifier, used if a validation entry has to be added.
     * @param message
     *      Special message for field, null if it does not exist.
     * @return
     *      True if valid, false otherwise.
     */
    public static boolean validateInternalDateInterval(InternalLocalDateInterval interval,
                                                       List<ValidationMessage> validationMessages,
                                                       String field,
                                                       String message) {
        if (interval == null || interval.getTom() == null || interval.getFrom() == null) {
            addValidationError(validationMessages, field, ValidationMessageType.EMPTY);
            return false;
        }
        return validateDate(interval.getFrom(), validationMessages, field, message)
                && validateDate(interval.getTom(), validationMessages, field, message);
    }

    /**
     * Performs the normal date validation {@link ValidatorUtil#validateDate(InternalDate, List, String, String)} as well as
     * checking if the supplied date is in the future. If future, a {@link ValidationMessage} of type {@link ValidationMessageType#WARN}
     * is added to the supplied list of validationMessages.
     *
     * @param date
     *      Date to validate.
     * @param validationMessages
     *      List of validationMessages. Any validation errors and/or warnings are added to this list.
     * @param field
     *      Field identifier, used if a validation entry has to be added.
     * @return
     *      True if date was valid according to {@link ValidatorUtil#validateDate(InternalDate, List, String, String)}.
     */
    public static boolean validateDateAndWarnIfFuture(InternalDate date, List<ValidationMessage> validationMessages, String field) {
        boolean isValid = validateDate(date, validationMessages, field, null);


        // For structurally valid dates, check if it is a future date
        // Note that being in the future doesn't make it invalid per se, only WARN
        if (date.isValidDate() && date.asLocalDate().isAfter(LocalDate.now())) {
            ValidatorUtil.addValidationError(validationMessages, field, ValidationMessageType.WARN,
                    "common.validation.future.datum");
        }
        return isValid;
    }

    public static void validateVardenhet(GrundData grundData, List<ValidationMessage> validationMessages) {
        if (Strings.nullToEmpty(grundData.getSkapadAv().getVardenhet().getPostadress()).trim().isEmpty()) {
            addValidationError(validationMessages, "vardenhet.grunddata.skapadAv.vardenhet.postadress", ValidationMessageType.EMPTY);
        }

        if (Strings.nullToEmpty(grundData.getSkapadAv().getVardenhet().getPostnummer()).trim().isEmpty()) {
            addValidationError(validationMessages, "vardenhet.grunddata.skapadAv.vardenhet.postnummer", ValidationMessageType.EMPTY);
        } else if (!STRING_VALIDATOR.validateStringAsPostalCode(grundData.getSkapadAv().getVardenhet().getPostnummer())) {
            addValidationError(validationMessages,
                    "vardenhet.grunddata.skapadAv.vardenhet.postnummer", ValidationMessageType.INVALID_FORMAT,
                    "common.validation.postnummer.incorrect-format");
        }

        if (Strings.nullToEmpty(grundData.getSkapadAv().getVardenhet().getPostort()).trim().isEmpty()) {
            addValidationError(validationMessages, "vardenhet.grunddata.skapadAv.vardenhet.postort", ValidationMessageType.EMPTY);
        }

        if (Strings.nullToEmpty(grundData.getSkapadAv().getVardenhet().getTelefonnummer()).trim().isEmpty()) {
            addValidationError(validationMessages,
                    "vardenhet.grunddata.skapadAv.vardenhet.telefonnummer", ValidationMessageType.EMPTY);
        }
    }

    public static boolean isBlankButNotNull(String stringFromField) {
        return !Strings.isNullOrEmpty(stringFromField) && stringFromField.trim().isEmpty();
    }

    /**
     * Check if there are validation errors.
     *
     */
    public static ValidationStatus getValidationStatus(List<ValidationMessage> validationMessages) {
        return validationMessages.isEmpty() ? ValidationStatus.VALID : ValidationStatus.INVALID;
    }

    public static void addValidationError(List<ValidationMessage> validationMessages, String field,
                                          ValidationMessageType type, String msg, String dynamicLabel) {

        // Bit of a hack - but make sure no WARN types are added to the ERROR list.
        if (type == ValidationMessageType.WARN) {
            return;
        }
        validationMessages.add(new ValidationMessage(field, type, msg, dynamicLabel));
        LOG.debug(field + " " + msg);
    }

    /**
     * Create a ValidationMessage and add it to the list of messages.
     *
     * @param validationMessages
     *            list collection messages
     * @param field
     *            a String with the name of the field
     * @param msg
     *            a String with an error code for the front end implementation
     */
    public static void addValidationError(List<ValidationMessage> validationMessages, String field,
                                          ValidationMessageType type, String msg) {
        validationMessages.add(new ValidationMessage(field, type, msg));
        LOG.debug(field + " " + msg);
    }

    public static void addValidationError(List<ValidationMessage> validationMessages, String field, ValidationMessageType type) {
        validationMessages.add(new ValidationMessage(field, type));
        LOG.debug(field + " " + type.toString());
    }

    /**
     * @param intervals
     *            intervals
     * @return boolean
     */
    public static boolean allNulls(InternalLocalDateInterval[] intervals) {
        for (InternalLocalDateInterval interval : intervals) {
            if (interval != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check for null or empty String, if so add a validation error for field with errorCode.
     *
     * @param beskrivning
     *            the String to check
     * @param field
     *            the target field in the model
     * @param errorCode
     *            the errorCode to log in validation errors
     */
    public static AssertionResult assertDescriptionNotEmpty(List<ValidationMessage> validationMessages, String beskrivning,
                                                            String field, String errorCode) {
        if (beskrivning == null || beskrivning.isEmpty()) {
            addValidationError(validationMessages, field, ValidationMessageType.EMPTY, errorCode);
            LOG.debug(field + " " + errorCode);
            return AssertionResult.FAILURE;
        }
        return AssertionResult.SUCCESS;
    }

    public static AssertionResult assertDescriptionNotEmpty(List<ValidationMessage> validationMessages, String beskrivning, String field) {
        if (beskrivning == null || beskrivning.isEmpty()) {
            addValidationError(validationMessages, field, ValidationMessageType.EMPTY);
            return AssertionResult.FAILURE;
        }
        return AssertionResult.SUCCESS;
    }

    /**
     * Since the validator assertions doesn't throw exceptions on assertion failure, they instead return an assertion
     * result. This might be used to implement conditional logic based on if an assertion {@link #failed()} or was
     * {@link #success()}ful.
     */
    public enum AssertionResult {
        SUCCESS(true), FAILURE(false);

        private final boolean assertSuccessful;

        AssertionResult(boolean assertSuccessfull) {
            this.assertSuccessful = assertSuccessfull;
        }

        public boolean failed() {
            return !assertSuccessful;
        }

        public boolean success() {
            return assertSuccessful;
        }
    }

    public static boolean isNotNullTrue(Boolean bool) {
        return bool != null && bool;
    }

    public static boolean isNotNullFalse(Boolean bool) {
        return bool != null && !bool;
    }

    public static ValidateDraftResponse buildValidateDraftResponse(List<ValidationMessage> validationMessages) {
        List<ValidationMessage> errors = validationMessages.stream().filter(isValidationError()).collect(Collectors.toList());
        List<ValidationMessage> warnings = validationMessages.stream().filter(isValidationWarning()).collect(Collectors.toList());

        return new ValidateDraftResponse(getValidationStatus(errors), errors, warnings);
    }

    private static Predicate<ValidationMessage> isValidationWarning() {
        return vm -> vm.getType() == ValidationMessageType.WARN;
    }

    private static Predicate<ValidationMessage> isValidationError() {
        return vm -> vm.getType() != ValidationMessageType.WARN;
    }

    /**
     * Checks if the supplied string - if not null - is a parsable Integer or Float/Double.
     *
     * @param tjanstgoringstid
     *      A string that should represent a number such as 40 or 37.5.
     * @return
     *      true if invalid, false if valid.
     */
    public static boolean isInvalidTjanstgoringstid(String tjanstgoringstid) {
        return tjanstgoringstid != null && (Ints.tryParse(tjanstgoringstid) == null && Doubles.tryParse(tjanstgoringstid) == null);
    }
}
