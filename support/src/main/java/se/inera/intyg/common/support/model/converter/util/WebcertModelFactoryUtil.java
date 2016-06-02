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

package se.inera.intyg.common.support.model.converter.util;

import org.joda.time.LocalDateTime;

import se.inera.intyg.common.support.model.common.internal.*;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.model.common.internal.Vardgivare;
import se.inera.intyg.common.support.modules.support.api.dto.*;

public final class WebcertModelFactoryUtil {

    private WebcertModelFactoryUtil() {
    }

    public static Patient convertPatientToEdit(se.inera.intyg.common.support.modules.support.api.dto.Patient patientInfo) {
        Patient patient = new Patient();
        patient.setFornamn(patientInfo.getFornamn());
        patient.setMellannamn(patientInfo.getMellannamn());
        patient.setEfternamn(patientInfo.getEfternamn());
        patient.setFullstandigtNamn(patientInfo.getFullstandigtNamn());
        patient.setPersonId(patientInfo.getPersonnummer());
        patient.setPostadress(patientInfo.getPostadress());
        patient.setPostnummer(patientInfo.getPostnummer());
        patient.setPostort(patientInfo.getPostort());

        return patient;
    }

    public static HoSPersonal convertHosPersonalToEdit(se.inera.intyg.common.support.modules.support.api.dto.HoSPersonal hosPers) {
        HoSPersonal hosPersonal = new HoSPersonal();

        hosPersonal.setPersonId(hosPers.getHsaId());
        hosPersonal.setForskrivarKod(hosPers.getForskrivarkod());
        hosPersonal.setFullstandigtNamn(hosPers.getNamn());

        if (hosPers.getBefattning() != null) {
            hosPersonal.getBefattningar().add(hosPers.getBefattning());
        }
        if (hosPers.getSpecialiseringar() != null) {
            hosPersonal.getSpecialiteter().addAll(hosPers.getSpecialiseringar());
        }

        if (hosPers.getVardenhet() != null) {
            Vardenhet vardenhet = convertVardenhetToEdit(hosPers.getVardenhet());
            hosPersonal.setVardenhet(vardenhet);
        }

        return hosPersonal;
    }

    public static void updateSkapadAv(Utlatande utlatande, se.inera.intyg.common.support.modules.support.api.dto.HoSPersonal hosPerson, LocalDateTime signeringsdatum) {
        utlatande.getGrundData().getSkapadAv().setPersonId(hosPerson.getHsaId());
        utlatande.getGrundData().getSkapadAv().setFullstandigtNamn(hosPerson.getNamn());
        utlatande.getGrundData().getSkapadAv().setForskrivarKod(hosPerson.getForskrivarkod());
        utlatande.getGrundData().setSigneringsdatum(signeringsdatum);
    }

    public static void populateGrunddataFromCreateDraftCopyHolder(GrundData grundData, CreateDraftCopyHolder copyData) throws ConverterException {
        populateWithSkapadAv(grundData, copyData.getSkapadAv());
        populateWithRelation(grundData, copyData.getRelation());

        if (copyData.hasPatient()) {
            populateWithPatientInfo(grundData, copyData.getPatient());
        }

        if (copyData.hasNewPersonnummer()) {
            populateWithNewPersonnummer(grundData, copyData.getNewPersonnummer());
        }
    }

    public static void populateGrunddataFromCreateNewDraftHolder(GrundData grundData, CreateNewDraftHolder newDraftData) throws ConverterException {
        populateWithSkapadAv(grundData, newDraftData.getSkapadAv());
        populateWithPatientInfo(grundData, newDraftData.getPatient());
    }

    private static void populateWithNewPersonnummer(GrundData grundData, Personnummer newPersonnummer) {
        grundData.getPatient().setPersonId(newPersonnummer);
    }

    private static void populateWithPatientInfo(GrundData grundData, se.inera.intyg.common.support.modules.support.api.dto.Patient patient) throws ConverterException {
        if (patient == null) {
            throw new ConverterException("Got null while trying to populateWithPatientInfo");
        }
        grundData.setPatient(convertPatientToEdit(patient));
    }

    private static void populateWithSkapadAv(GrundData grundData, se.inera.intyg.common.support.modules.support.api.dto.HoSPersonal hoSPersonal) throws ConverterException {
        if (hoSPersonal == null) {
            throw new ConverterException("Got null while trying to populateWithSkapadAv");
        }
        grundData.setSkapadAv(convertHosPersonalToEdit(hoSPersonal));
    }

    private static void populateWithRelation(GrundData grundData, Relation relation) {
        if (relation != null) {
            grundData.setRelation(relation);
        } else {
            grundData.setRelation(null);
        }
    }

    private static Vardenhet convertVardenhetToEdit(se.inera.intyg.common.support.modules.support.api.dto.Vardenhet vardenhetDto) {

        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setEnhetsid(vardenhetDto.getHsaId());
        vardenhet.setEnhetsnamn(vardenhetDto.getNamn());
        vardenhet.setVardgivare(convertVardgivareToEdit(vardenhetDto.getVardgivare()));

        vardenhet.setPostadress(vardenhetDto.getPostadress());
        vardenhet.setPostort(vardenhetDto.getPostort());
        vardenhet.setPostnummer(vardenhetDto.getPostnummer());
        vardenhet.setTelefonnummer(vardenhetDto.getTelefonnummer());
        vardenhet.setEpost(vardenhetDto.getEpost());
        vardenhet.setArbetsplatsKod(vardenhetDto.getArbetsplatskod());

        return vardenhet;
    }

    private static Vardgivare convertVardgivareToEdit(se.inera.intyg.common.support.modules.support.api.dto.Vardgivare vardgivareDto) {

        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setVardgivarid(vardgivareDto.getHsaId());
        vardgivare.setVardgivarnamn(vardgivareDto.getNamn());

        return vardgivare;
    }
}
