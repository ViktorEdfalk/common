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

package se.inera.intyg.common.support.validate;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.InternalLocalDateInterval;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationStatus;

import java.util.List;

/**
 * Common utils used for validation.
 *
 * @author Gustav Norbäcker, R2M
 */
public final class ValidatorUtil {

    private static final Logger LOG = LoggerFactory.getLogger(ValidatorUtil.class);
    private static final StringValidator STRING_VALIDATOR = new StringValidator();

    public static final int BASE_10 = 10;

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

    public boolean validateDate(InternalDate date, List<ValidationMessage> validationMessages, String field) {
        boolean valid = true;
        if (!date.isValidDate()) {
            addValidationError(validationMessages, field, ValidationMessageType.INVALID_FORMAT);
            return false;
        }

        if (!date.isReasonable()) {
            addValidationError(validationMessages, field, ValidationMessageType.INVALID_FORMAT,
                    "common.validation.date_out_of_range");
            valid = false;
        }
        return valid;
    }

    public void validateVardenhet(GrundData grundData, List<ValidationMessage> validationMessages) {
        if (StringUtils.isBlank(grundData.getSkapadAv().getVardenhet().getPostadress())) {
            addValidationError(validationMessages, "vardenhet.grunddata.skapadAv.vardenhet.postadress", ValidationMessageType.EMPTY);
        }

        if (StringUtils.isBlank(grundData.getSkapadAv().getVardenhet().getPostnummer())) {
            addValidationError(validationMessages, "vardenhet.grunddata.skapadAv.vardenhet.postnummer", ValidationMessageType.EMPTY);
        } else if (!STRING_VALIDATOR.validateStringAsPostalCode(grundData.getSkapadAv().getVardenhet().getPostnummer())) {
            addValidationError(validationMessages, "vardenhet.grunddata.skapadAv.vardenhet.postnummer", ValidationMessageType.INVALID_FORMAT,
                    "common.validation.postnummer.incorrect-format");
        }

        if (StringUtils.isBlank(grundData.getSkapadAv().getVardenhet().getPostort())) {
            addValidationError(validationMessages, "vardenhet.grunddata.skapadAv.vardenhet.postort", ValidationMessageType.EMPTY);
        }

        if (StringUtils.isBlank(grundData.getSkapadAv().getVardenhet().getTelefonnummer())) {
            addValidationError(validationMessages, "vardenhet.grunddata.skapadAv.vardenhet.telefonnummer", ValidationMessageType.EMPTY);
        }
    }

    public boolean isBlankButNotNull(String stringFromField) {
        return (!StringUtils.isEmpty(stringFromField)) && StringUtils.isBlank(stringFromField);
    }

    /**
     * Check if there are validation errors.
     *
     */
    public ValidationStatus getValidationStatus(List<ValidationMessage> validationMessages) {
        return (validationMessages.isEmpty()) ? ValidationStatus.VALID : ValidationStatus.INVALID;
    }

    public void addValidationError(List<ValidationMessage> validationMessages, String field, ValidationMessageType type, String msg, String dynamicLabel) {
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
    public void addValidationError(List<ValidationMessage> validationMessages, String field, ValidationMessageType type, String msg) {
        validationMessages.add(new ValidationMessage(field, type, msg));
        LOG.debug(field + " " + msg);
    }

    public void addValidationError(List<ValidationMessage> validationMessages, String field, ValidationMessageType type) {
        validationMessages.add(new ValidationMessage(field, type));
        LOG.debug(field + " " + type.toString());
    }

    /**
     * @param intervals
     *            intervals
     * @return boolean
     */
    public boolean allNulls(InternalLocalDateInterval[] intervals) {
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
    public AssertionResult assertDescriptionNotEmpty(List<ValidationMessage> validationMessages, String beskrivning, String field, String errorCode) {
        if (beskrivning == null || beskrivning.isEmpty()) {
            addValidationError(validationMessages,field, ValidationMessageType.EMPTY, errorCode);
            LOG.debug(field + " " + errorCode);
            return AssertionResult.FAILURE;
        }
        return AssertionResult.SUCCESS;
    }

    public AssertionResult assertDescriptionNotEmpty(List<ValidationMessage> validationMessages, String beskrivning, String field) {
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

        AssertionResult(boolean assertSuccessfull) {
            this.assertSuccessful = assertSuccessfull;
        }

        private final boolean assertSuccessful;

        public boolean failed() {
            return !assertSuccessful;
        }

        public boolean success() {
            return assertSuccessful;
        }
    }

    public boolean isNotNullTrue(Boolean bool) {
        return bool != null && bool;
    }

    public boolean isNotNullFalse(Boolean bool) {
        return bool != null && !bool;
    }

}
