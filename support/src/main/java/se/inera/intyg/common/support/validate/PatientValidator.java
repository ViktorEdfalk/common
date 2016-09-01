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

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;

/**
 * Created by BESA on 2016-08-26.
 */
public final class PatientValidator {

    private PatientValidator() {
    }

    public static void validate(Patient patient, List<ValidationMessage> validationMessages) {
        if (patient == null) {
            throw new RuntimeException("No Patient found when attempting to validate");
        }
        validateString(validationMessages, patient.getPostadress(), "patient.postadress", "common.validation.patient.postadress.missing");
        validateString(validationMessages, patient.getPostnummer(), "patient.postnummer", "common.validation.patient.postnummer.missing");
        validateString(validationMessages, patient.getPostort(), "patient.postort", "common.validation.patient.postort.missing");
    }

    private static void validateString(List<ValidationMessage> validationMessages, String text, String field, String msg) {
        if (StringUtils.isBlank(text)) {
            validationMessages.add(new ValidationMessage(field, ValidationMessageType.EMPTY, msg));
        }
    }
}
