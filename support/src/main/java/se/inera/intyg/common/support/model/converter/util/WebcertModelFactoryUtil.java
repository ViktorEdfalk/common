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
package se.inera.intyg.common.support.model.converter.util;

import java.time.LocalDateTime;

import org.apache.commons.lang3.StringUtils;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.intyg.common.support.modules.support.api.dto.CreateNewDraftHolder;
import se.inera.intyg.common.support.modules.support.api.dto.Personnummer;

public final class WebcertModelFactoryUtil {

    private WebcertModelFactoryUtil() {
    }

    public static void updateSkapadAv(Utlatande utlatande, HoSPersonal hosPerson, LocalDateTime signeringsdatum) {
        utlatande.getGrundData().setSkapadAv(hosPerson);
        utlatande.getGrundData().setSigneringsdatum(signeringsdatum);
    }

    public static void populateGrunddataFromCreateDraftCopyHolder(GrundData grundData, CreateDraftCopyHolder copyData) throws ConverterException {
        validateRequest(copyData.getSkapadAv());

        if (grundData.getSkapadAv().getVardenhet().getEnhetsid().equals(copyData.getSkapadAv().getVardenhet().getEnhetsid())) {
            // grundData is the copied information that we received from the original certificate. This object contains
            // information that we want to preserve if the enhet is the same as in the logged in user. See INTYG-2835.
            populateWithMissingInfo(copyData.getSkapadAv().getVardenhet(), grundData.getSkapadAv().getVardenhet());
        }

        grundData.setSkapadAv(copyData.getSkapadAv());
        grundData.setRelation(copyData.getRelation());

        if (copyData.hasPatient()) {
            populateWithPatientInfo(grundData, copyData.getPatient());
        }

        if (copyData.hasNewPersonnummer()) {
            grundData.getPatient().setPersonId(copyData.getNewPersonnummer());
        }
    }

    public static void populateGrunddataFromCreateNewDraftHolder(GrundData grundData, CreateNewDraftHolder newDraftData) throws ConverterException {
        validateRequest(newDraftData.getSkapadAv());
        grundData.setSkapadAv(newDraftData.getSkapadAv());
        populateWithPatientInfo(grundData, newDraftData.getPatient());
    }

    public static void populateWithPatientInfo(GrundData grundData, Patient patient) throws ConverterException {
        if (patient == null) {
            throw new ConverterException("Got null while trying to populateWithPatientInfo");
        }
        grundData.setPatient(patient);
    }

    /**
     * Create a new effective Patient model based on supplied patient and existing patient, following the pattern that
     * only valid new patient details override existing ones.
     *
     * Special logic is applies to personId, where only valid personnr and samordningsnummer are valid override values.
     *
     * @param existingPatient
     * @param newPatient
     * @return
     */
    public static Patient buildNewEffectivePatient(Patient existingPatient, Patient newPatient) {
        Patient mergedPatient = new Patient();

        // Only accept valid personnr or samordningsnummer as new personId
        if (newPatient.getPersonId() != null && (Personnummer.createValidatedPersonnummerWithDash(newPatient.getPersonId()).isPresent()
                || newPatient.getPersonId().isSamordningsNummer())) {
            mergedPatient.setPersonId(newPatient.getPersonId());
        } else {
            mergedPatient.setPersonId(existingPatient.getPersonId());
        }

        if (StringUtils.isNotBlank(newPatient.getFornamn())) {
            mergedPatient.setFornamn(newPatient.getFornamn());
        } else {
            mergedPatient.setFornamn(existingPatient.getFornamn());
        }

        // Name
        if (StringUtils.isNotBlank(newPatient.getMellannamn())) {
            mergedPatient.setMellannamn(newPatient.getMellannamn());
        } else {
            mergedPatient.setMellannamn(existingPatient.getMellannamn());
        }

        if (StringUtils.isNotBlank(newPatient.getEfternamn())) {
            mergedPatient.setEfternamn(newPatient.getEfternamn());
        } else {
            mergedPatient.setEfternamn(existingPatient.getEfternamn());
        }

        if (StringUtils.isNotBlank(newPatient.getFullstandigtNamn())) {
            mergedPatient.setFullstandigtNamn(newPatient.getFullstandigtNamn());
        } else {
            mergedPatient.setFullstandigtNamn(existingPatient.getFullstandigtNamn());
        }

        // Address
        if (StringUtils.isNotBlank(newPatient.getPostadress())) {
            mergedPatient.setPostadress(newPatient.getPostadress());
        } else {
            mergedPatient.setPostadress(existingPatient.getPostadress());
        }
        if (StringUtils.isNotBlank(newPatient.getPostnummer())) {
            mergedPatient.setPostnummer(newPatient.getPostnummer());
        } else {
            mergedPatient.setPostnummer(existingPatient.getPostnummer());
        }
        if (StringUtils.isNotBlank(newPatient.getPostort())) {
            mergedPatient.setPostort(newPatient.getPostort());
        } else {
            mergedPatient.setPostort(existingPatient.getPostort());
        }

        return mergedPatient;
    }

    private static void populateWithMissingInfo(Vardenhet target, Vardenhet source) {
        if (StringUtils.isBlank(target.getPostadress())) {
            target.setPostadress(source.getPostadress());
        }
        if (StringUtils.isBlank(target.getPostnummer())) {
            target.setPostnummer(source.getPostnummer());
        }
        if (StringUtils.isBlank(target.getPostort())) {
            target.setPostort(source.getPostort());
        }
        if (StringUtils.isBlank(target.getTelefonnummer())) {
            target.setTelefonnummer(source.getTelefonnummer());
        }

    }

    private static void validateRequest(HoSPersonal skapadAv) throws ConverterException {
        if (skapadAv == null) {
            throw new ConverterException("Got null while trying to populateWithSkapadAv");
        }
    }

}
