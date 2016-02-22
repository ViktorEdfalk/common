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

package se.inera.certificate.modules.sjukpenning_utokad.validator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import se.inera.certificate.modules.sjukpenning_utokad.model.internal.Diagnos;
import se.inera.certificate.modules.sjukpenning_utokad.model.internal.SjukpenningUtokadUtlatande;
import se.inera.intyg.common.support.model.InternalLocalDateInterval;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationStatus;
import se.inera.intyg.common.support.validate.StringValidator;

public class InternalDraftValidator {

    private static final Logger LOG = LoggerFactory.getLogger(InternalDraftValidator.class);

    @Autowired(required = false)
    private WebcertModuleService moduleService;

    private static final StringValidator STRING_VALIDATOR = new StringValidator();

    private static final int MIN_SIZE_PSYKISK_DIAGNOS = 4;
    private static final int MIN_SIZE_DIAGNOS = 3;

    public ValidateDraftResponse validateDraft(SjukpenningUtokadUtlatande utlatande) {
        List<ValidationMessage> validationMessages = new ArrayList<>();

        // Kategori 1 – Grund för medicinskt underlag
        validateGrundForMU(utlatande, validationMessages);
        // Kategori 4 – Diagnos
        validateDiagnose(utlatande, validationMessages);
        // Kategori 6 – Aktivitetsbegränsning
        validateAktivitetsbegransning(utlatande, validationMessages);
        // Kategori 7 – Medicinska behandlingar/åtgärder
        // Kategori 9 – Övrigt
        // Kategori 10 – Kontakt
        // vårdenhet
        validateVardenhet(utlatande, validationMessages);

        return new ValidateDraftResponse(getValidationStatus(validationMessages), validationMessages);
    }

    private void validateGrundForMU(SjukpenningUtokadUtlatande utlatande, List<ValidationMessage> validationMessages) {

//        if (utlatande.getUndersokningAvPatienten() == null && utlatande.getJournaluppgifter() == null
//                && utlatande.getAnhorigsBeskrivningAvPatienten() == null && utlatande.getAnnatGrundForMU() == null) {
//            addValidationError(validationMessages, "grundformu", ValidationMessageType.EMPTY,
//                    "sjukersattning.validation.grund-for-mu.missing");
//        }
//
//        if (utlatande.getUndersokningAvPatienten() != null && !utlatande.getUndersokningAvPatienten().isValidDate()) {
//            addValidationError(validationMessages, "grundformu.undersokning", ValidationMessageType.INVALID_FORMAT,
//                    "sjukersattning.validation.grund-for-mu.undersokning.incorrect_format");
//        }
//        if (utlatande.getJournaluppgifter() != null && !utlatande.getJournaluppgifter().isValidDate()) {
//            addValidationError(validationMessages, "grundformu.journaluppgifter", ValidationMessageType.INVALID_FORMAT,
//                    "sjukersattning.validation.grund-for-mu.journaluppgifter.incorrect_format");
//        }
//        if (utlatande.getAnhorigsBeskrivningAvPatienten() != null && !utlatande.getAnhorigsBeskrivningAvPatienten().isValidDate()) {
//            addValidationError(validationMessages, "grundformu.anhorigsbeskrivning", ValidationMessageType.INVALID_FORMAT,
//                    "sjukersattning.validation.grund-for-mu.anhorigsbeskrivning.incorrect_format");
//        }
//        if (utlatande.getAnnatGrundForMU() != null && !utlatande.getAnnatGrundForMU().isValidDate()) {
//            addValidationError(validationMessages, "grundformu.annat", ValidationMessageType.INVALID_FORMAT,
//                    "sjukersattning.validation.grund-for-mu.annat.incorrect_format");
//        }
//        if (utlatande.getAnnatGrundForMU() != null && StringUtils.isBlank(utlatande.getAnnatGrundForMUBeskrivning())) {
//            addValidationError(validationMessages, "grundformu.annat", ValidationMessageType.EMPTY,
//                    "sjukersattning.validation.grund-for-mu.annat.missing");
//        }

    }

    private void validateVardenhet(SjukpenningUtokadUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (StringUtils.isBlank(utlatande.getGrundData().getSkapadAv().getVardenhet().getPostadress())) {
            addValidationError(validationMessages, "vardenhet.adress", ValidationMessageType.EMPTY,
                    "sjukersattning.validation.vardenhet.postadress.missing");
        }

        if (StringUtils.isBlank(utlatande.getGrundData().getSkapadAv().getVardenhet().getPostnummer())) {
            addValidationError(validationMessages, "vardenhet.postnummer", ValidationMessageType.EMPTY,
                    "sjukersattning.validation.vardenhet.postnummer.missing");
        } else if (!STRING_VALIDATOR.validateStringAsPostalCode(utlatande.getGrundData().getSkapadAv().getVardenhet().getPostnummer())) {
            addValidationError(validationMessages, "vardenhet.postnummer", ValidationMessageType.EMPTY,
                    "sjukersattning.validation.vardenhet.postnummer.incorrect-format");
        }

        if (StringUtils.isBlank(utlatande.getGrundData().getSkapadAv().getVardenhet().getPostort())) {
            addValidationError(validationMessages, "vardenhet.postort", ValidationMessageType.EMPTY,
                    "sjukersattning.validation.vardenhet.postort.missing");
        }

        if (StringUtils.isBlank(utlatande.getGrundData().getSkapadAv().getVardenhet().getTelefonnummer())) {
            addValidationError(validationMessages, "vardenhet.telefonnummer", ValidationMessageType.EMPTY,
                    "sjukersattning.validation.vardenhet.telefonnummer.missing");
        }
    }

    private void validateAktivitetsbegransning(SjukpenningUtokadUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (StringUtils.isBlank(utlatande.getAktivitetsbegransning())) {
            addValidationError(validationMessages, "aktivitetsbegransning", ValidationMessageType.EMPTY,
                    "sjukersattning.validation.aktivitetsbegransning.missing");
        }
    }

    private void validateDiagnose(SjukpenningUtokadUtlatande utlatande, List<ValidationMessage> validationMessages) {

        if (utlatande.getDiagnoser().size() == 0) {
            addValidationError(validationMessages, "diagnos", ValidationMessageType.EMPTY,
                    "lisu.validation.diagnos.missing");
        }
        for (Diagnos diagnos : utlatande.getDiagnoser()) {

            /* R8 För delfråga 6.2 ska diagnoskod anges med så många positioner som möjligt, men minst tre positioner (t.ex. F32).
               R9 För delfråga 6.2 ska diagnoskod anges med minst fyra positioner då en psykisk diagnos anges.
               Med psykisk diagnos avses alla diagnoser som börjar med Z73 eller med F (dvs. som tillhör F-kapitlet i ICD-10). */
            if (StringUtils.isBlank(diagnos.getDiagnosKod())) {
                addValidationError(validationMessages, "diagnos", ValidationMessageType.EMPTY,
                        "lisu.validation.diagnos.missing");
            } else {
                String trimDiagnoskod = StringUtils.trim(diagnos.getDiagnosKod()).toUpperCase();
                if ((trimDiagnoskod.startsWith("Z73") || trimDiagnoskod.startsWith("F"))
                        && trimDiagnoskod.length() < MIN_SIZE_PSYKISK_DIAGNOS) {
                    addValidationError(validationMessages, "diagnos", ValidationMessageType.INVALID_FORMAT,
                            "lisu.validation.diagnos.psykisk.length-4");
                } else if (trimDiagnoskod.length() < MIN_SIZE_DIAGNOS) {
                    addValidationError(validationMessages, "diagnos", ValidationMessageType.INVALID_FORMAT,
                            "lisu.validation.diagnos.length-3");
                } else {
                    validateDiagnosKod(diagnos.getDiagnosKod(), diagnos.getDiagnosKodSystem(), "diagnos",
                            "lisu.validation.diagnos.invalid", validationMessages);
                }
            }
            if (StringUtils.isBlank(diagnos.getDiagnosBeskrivning())) {
                addValidationError(validationMessages, "diagnos", ValidationMessageType.EMPTY,
                        "lisu.validation.diagnos.description.missing");
            }
        }

    }

    private void validateDiagnosKod(String diagnosKod, String kodsystem, String field, String msgKey, List<ValidationMessage> validationMessages) {
        // if moduleService is not available, skip this validation
        if (moduleService == null) {
            LOG.warn("Forced to skip validation of diagnosKod since an implementation of ModuleService is not available");
            return;
        }

        if (!moduleService.validateDiagnosisCode(diagnosKod, kodsystem)) {
            addValidationError(validationMessages, field, ValidationMessageType.INVALID_FORMAT, msgKey);
        }

    }

    /**
     * Check if there are validation errors.
     *
     */
    private ValidationStatus getValidationStatus(List<ValidationMessage> validationMessages) {
        return (validationMessages.isEmpty()) ? ValidationStatus.VALID : ValidationStatus.INVALID;
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
    private void addValidationError(List<ValidationMessage> validationMessages, String field, ValidationMessageType type, String msg) {
        validationMessages.add(new ValidationMessage(field, type, msg));
        LOG.debug(field + " " + msg);
    }

    /**
     *
     * @param validationMessages
     *            list collecting message
     * @param fieldId
     *            field id
     * @param intervals
     *            intervals
     * @return booleans
     */
    protected boolean validateIntervals(List<ValidationMessage> validationMessages, String fieldId, InternalLocalDateInterval... intervals) {
        if (intervals == null || allNulls(intervals)) {
            addValidationError(validationMessages, fieldId, ValidationMessageType.EMPTY,
                    "sjukersattning.validation.nedsattning.choose-at-least-one");
            return false;
        }

        for (int i = 0; i < intervals.length; i++) {
            if (intervals[i] != null) {
                Interval oneInterval = createInterval(intervals[i].fromAsLocalDate(), intervals[i].tomAsLocalDate());
                if (oneInterval == null) {
                    addValidationError(validationMessages, fieldId, ValidationMessageType.OTHER,
                            "sjukersattning.validation.nedsattning.incorrect-date-interval");
                    return false;
                }
                for (int j = i + 1; j < intervals.length; j++) {
                    if (intervals[j] != null) {
                        Interval anotherInterval = createInterval(intervals[j].fromAsLocalDate(), intervals[j].tomAsLocalDate());
                        if (anotherInterval == null) {
                            addValidationError(validationMessages, fieldId, ValidationMessageType.OTHER,
                                    "sjukersattning.validation.nedsattning.incorrect-date-interval");
                            return false;
                        }
                        // Overlap OR abuts(one intervals tom day== another's
                        // from day) is considered invalid
                        if (oneInterval.overlaps(anotherInterval) || oneInterval.abuts(anotherInterval)) {
                            addValidationError(validationMessages, fieldId, ValidationMessageType.OTHER,
                                    "sjukersattning.validation.nedsattning.overlapping-date-interval");
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * @param intervals
     *            intervals
     * @return boolean
     */
    private boolean allNulls(InternalLocalDateInterval[] intervals) {
        for (InternalLocalDateInterval interval : intervals) {
            if (interval != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param start
     *            start
     * @param end
     *            end
     * @return Interval
     */
    private Interval createInterval(LocalDate start, LocalDate end) {
        if ((start == null || end == null || start.isAfter(end))) {
            return null;
        } else {
            return new Interval(start.toDate().getTime(), end.toDate().getTime());
        }
    }

}
