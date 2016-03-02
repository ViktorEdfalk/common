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

package se.inera.certificate.modules.sjukersattning.model.converter;

import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.AKTIVITETSBEGRANSNING_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.AKTIVITETSFORMAGA_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.AKTIVITETSFORMAGA_SVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.ANHORIGSBESKRIVNING;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.ANLEDNING_TILL_KONTAKT_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.ANNAT;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.ARBETSPLATSKOD_CODE_SYSTEM;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.AVSLUTADBEHANDLING_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.AVSLUTADBEHANDLING_SVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.BEFATTNING_CODE_SYSTEM;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.CERTIFICATE_CODE_SYSTEM;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.DIAGNOSGRUND_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.DIAGNOSGRUND_NYBEDOMNING_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.DIAGNOSGRUND_SVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.DIAGNOS_BESKRIVNING_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.DIAGNOS_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.DIAGNOS_SVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_ANNAN_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_ANNAN_SVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_BALANSKOORDINATION_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_BALANSKOORDINATION_SVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_INTELLEKTUELL_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_INTELLEKTUELL_SVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_KOMMUNIKATION_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_KOMMUNIKATION_SVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_KONCENTRATION_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_KONCENTRATION_SVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_PSYKISK_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_PSYKISK_SVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SYNHORSELTAL_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SYNHORSELTAL_SVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANNANBESKRIVNING_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_CODE_SYSTEM;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.HSA_CODE_SYSTEM;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.JOURNALUPPGIFTER;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.KANNEDOM_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.KANNEDOM_SVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.KONTAKT_ONSKAS_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.KONTAKT_ONSKAS_SVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.MEDICINSKAFORUTSATTNINGARFORARBETE_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.MEDICINSKAFORUTSATTNINGARFORARBETE_SVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.OVRIGT_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.OVRIGT_SVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.PAGAENDEBEHANDLING_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.PAGAENDEBEHANDLING_SVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.PERSON_ID_CODE_SYSTEM;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.PLANERADBEHANDLING_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.PLANERADBEHANDLING_SVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.SJUKDOMSFORLOPP_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.SJUKDOMSFORLOPP_SVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.SUBSTANSINTAG_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.SUBSTANSINTAG_SVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.UNDERLAGFINNS_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.UNDERLAGFINNS_SVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.UNDERLAG_CODE_SYSTEM;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.UNDERLAG_DATUM_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.UNDERLAG_HAMTAS_FRAN_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.UNDERLAG_SVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.UNDERLAG_TYP_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.UNDERSOKNING_AV_PATIENT;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.apache.commons.lang3.StringUtils;

import se.inera.certificate.modules.sjukersattning.model.internal.Diagnos;
import se.inera.certificate.modules.sjukersattning.model.internal.SjukersattningUtlatande;
import se.inera.certificate.modules.sjukersattning.model.internal.Tillaggsfraga;
import se.inera.certificate.modules.sjukersattning.model.internal.Underlag;
import se.inera.intyg.common.support.common.enumerations.Diagnoskodverk;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.ArbetsplatsKod;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.Befattning;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.CVType;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.HsaId;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.IntygId;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.PersonId;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.Specialistkompetens;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.TypAvIntyg;
import se.riv.clinicalprocess.healthcond.certificate.v2.Enhet;
import se.riv.clinicalprocess.healthcond.certificate.v2.HosPersonal;
import se.riv.clinicalprocess.healthcond.certificate.v2.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v2.Patient;
import se.riv.clinicalprocess.healthcond.certificate.v2.Svar;
import se.riv.clinicalprocess.healthcond.certificate.v2.Svar.Delsvar;
import se.riv.clinicalprocess.healthcond.certificate.v2.Vardgivare;

public final class InternalToTransport {

    private static final String CERTIFICATE_DISPLAY_NAME = "Läkarutlåtande för sjukersättning";

    private InternalToTransport() {
    }

    public static RegisterCertificateType convert(SjukersattningUtlatande source) throws ConverterException {
        if (source == null) {
            throw new ConverterException("Source utlatande was null, cannot convert");
        }

        RegisterCertificateType sjukersattningType = new RegisterCertificateType();
        sjukersattningType.setIntyg(getIntyg(source));
        return sjukersattningType;
    }

    private static Intyg getIntyg(SjukersattningUtlatande source) {
        Intyg intyg = new Intyg();
        intyg.setTyp(getTypAvIntyg(source));
        intyg.setIntygsId(getIntygsId(source));
        intyg.setVersion(getTextVersion(source));
        intyg.setSigneringstidpunkt(source.getGrundData().getSigneringsdatum());
        intyg.setSkickatTidpunkt(source.getGrundData().getSigneringsdatum());
        intyg.setSkapadAv(getSkapadAv(source));
        intyg.setPatient(getPatient(source.getGrundData().getPatient()));
        intyg.getSvar().addAll(getSvar(source));
        return intyg;
    }

    private static HosPersonal getSkapadAv(SjukersattningUtlatande source) {
        HoSPersonal sourceSkapadAv = source.getGrundData().getSkapadAv();
        HosPersonal skapadAv = new HosPersonal();
        skapadAv.setPersonalId(anHsaId(sourceSkapadAv.getPersonId()));
        skapadAv.setFullstandigtNamn(sourceSkapadAv.getFullstandigtNamn());
        skapadAv.setForskrivarkod(sourceSkapadAv.getForskrivarKod());
        skapadAv.setEnhet(getEnhet(sourceSkapadAv.getVardenhet()));
        for (String sourceBefattning : sourceSkapadAv.getBefattningar()) {
            Befattning befattning = new Befattning();
            befattning.setCodeSystem(BEFATTNING_CODE_SYSTEM);
            befattning.setCode(sourceBefattning);
            skapadAv.getBefattning().add(befattning);
        }
        for (String sourceKompetens : sourceSkapadAv.getSpecialiteter()) {
            Specialistkompetens kompetens = new Specialistkompetens();
            kompetens.setCode(sourceKompetens);
            skapadAv.getSpecialistkompetens().add(kompetens);
        }
        return skapadAv;
    }

    private static Enhet getEnhet(Vardenhet sourceVardenhet) {
        Enhet vardenhet = new Enhet();
        vardenhet.setEnhetsId(anHsaId(sourceVardenhet.getEnhetsid()));
        vardenhet.setEnhetsnamn(sourceVardenhet.getEnhetsnamn());
        vardenhet.setPostnummer(sourceVardenhet.getPostnummer());
        vardenhet.setPostadress(sourceVardenhet.getPostadress());
        vardenhet.setPostort(sourceVardenhet.getPostort());
        vardenhet.setTelefonnummer(sourceVardenhet.getTelefonnummer());
        vardenhet.setEpost(sourceVardenhet.getEpost());
        vardenhet.setVardgivare(getVardgivare(sourceVardenhet.getVardgivare()));
        vardenhet.setArbetsplatskod(getArbetsplatsKod(sourceVardenhet.getArbetsplatsKod()));
        return vardenhet;
    }

    private static ArbetsplatsKod getArbetsplatsKod(String sourceArbetsplatsKod) {
        ArbetsplatsKod arbetsplatsKod = new ArbetsplatsKod();
        arbetsplatsKod.setRoot(ARBETSPLATSKOD_CODE_SYSTEM);
        arbetsplatsKod.setExtension(sourceArbetsplatsKod);
        return arbetsplatsKod;
    }

    private static Vardgivare getVardgivare(se.inera.intyg.common.support.model.common.internal.Vardgivare sourceVardgivare) {
        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setVardgivareId(anHsaId(sourceVardgivare.getVardgivarid()));
        vardgivare.setVardgivarnamn(sourceVardgivare.getVardgivarnamn());
        return vardgivare;
    }

    private static Patient getPatient(se.inera.intyg.common.support.model.common.internal.Patient sourcePatient) {
        Patient patient = new se.riv.clinicalprocess.healthcond.certificate.v2.Patient();
        patient.setEfternamn(sourcePatient.getEfternamn());
        patient.setFornamn(sourcePatient.getFornamn());
        patient.setMellannamn(sourcePatient.getMellannamn());
        PersonId personId = new PersonId();
        personId.setRoot(PERSON_ID_CODE_SYSTEM);
        personId.setExtension(sourcePatient.getPersonId().getPersonnummer().replaceAll("-", ""));
        patient.setPersonId(personId);
        patient.setPostadress(sourcePatient.getPostadress());
        patient.setPostnummer(sourcePatient.getPostnummer());
        patient.setPostort(sourcePatient.getPostort());
        return patient;
    }

    private static IntygId getIntygsId(SjukersattningUtlatande source) {
        IntygId intygId = new IntygId();
        intygId.setRoot(source.getGrundData().getSkapadAv().getVardenhet().getEnhetsid());
        intygId.setExtension(source.getId());
        return intygId;
    }

    private static TypAvIntyg getTypAvIntyg(SjukersattningUtlatande source) {
        TypAvIntyg typAvIntyg = new TypAvIntyg();
        typAvIntyg.setCode(source.getTyp().toUpperCase());
        typAvIntyg.setCodeSystem(CERTIFICATE_CODE_SYSTEM);
        typAvIntyg.setDisplayName(CERTIFICATE_DISPLAY_NAME);
        return typAvIntyg;
    }

    private static String getTextVersion(SjukersattningUtlatande source) {
        return source.getTextVersion();
    }

    private static List<Svar> getSvar(SjukersattningUtlatande source) {
        List<Svar> svars = new ArrayList<>();

        if (source.getUndersokningAvPatienten() != null) {
            svars.add(aSvar(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID).
                    withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID, aCV(GRUNDFORMEDICINSKTUNDERLAG_CODE_SYSTEM, Integer.toString(UNDERSOKNING_AV_PATIENT))).
                    withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID, source.getUndersokningAvPatienten().asLocalDate().toString()).build());
        }
        if (source.getJournaluppgifter() != null) {
            svars.add(aSvar(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID).
                    withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID, aCV(GRUNDFORMEDICINSKTUNDERLAG_CODE_SYSTEM, Integer.toString(JOURNALUPPGIFTER))).
                    withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID, source.getJournaluppgifter().asLocalDate().toString()).build());
        }
        if (source.getAnhorigsBeskrivningAvPatienten() != null) {
            svars.add(aSvar(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID).
                    withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID, aCV(GRUNDFORMEDICINSKTUNDERLAG_CODE_SYSTEM, Integer.toString(ANHORIGSBESKRIVNING))).
                    withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID, source.getAnhorigsBeskrivningAvPatienten().asLocalDate().toString()).build());
        }
        if (source.getAnnatGrundForMU() != null) {
            svars.add(aSvar(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID).
                    withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID, aCV(GRUNDFORMEDICINSKTUNDERLAG_CODE_SYSTEM, Integer.toString(ANNAT))).
                    withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID, source.getAnnatGrundForMU().asLocalDate().toString()).
                    withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_ANNANBESKRIVNING_DELSVAR_ID, source.getAnnatGrundForMUBeskrivning()).build());
        }

        if (source.getKannedomOmPatient() != null) {
            svars.add(aSvar(KANNEDOM_SVAR_ID).
                    withDelsvar(KANNEDOM_DELSVAR_ID, source.getKannedomOmPatient().asLocalDate().toString()).build());
        }

        svars.add(aSvar(UNDERLAGFINNS_SVAR_ID).
                withDelsvar(UNDERLAGFINNS_DELSVAR_ID, source.getUnderlagFinns().toString()).build());

        for (Underlag underlag : source.getUnderlag()) {
            svars.add(
                    aSvar(UNDERLAG_SVAR_ID).withDelsvar(UNDERLAG_TYP_DELSVAR_ID,
                            aCV(UNDERLAG_CODE_SYSTEM, Integer.toString(underlag.getTyp().getId()))).
                            withDelsvar(UNDERLAG_DATUM_DELSVAR_ID,
                                    underlag.getDatum() != null ? underlag.getDatum().asLocalDate().toString() : null).
                            withDelsvar(UNDERLAG_HAMTAS_FRAN_DELSVAR_ID, underlag.getHamtasFran()).build());
        }

        svars.add(aSvar(SJUKDOMSFORLOPP_SVAR_ID).
                withDelsvar(SJUKDOMSFORLOPP_DELSVAR_ID, source.getSjukdomsforlopp()).build());

        for (int i = 0; i < source.getDiagnoser().size(); i++) {
            Diagnos diagnos = source.getDiagnoser().get(i);
            Diagnoskodverk diagnoskodverk = Diagnoskodverk.valueOf(diagnos.getDiagnosKodSystem());
            svars.add(aSvar(DIAGNOS_SVAR_ID).
                    withDelsvar(DIAGNOS_DELSVAR_ID, aCV(diagnoskodverk.getCodeSystem(), diagnos.getDiagnosKod())).
                    withDelsvar(DIAGNOS_BESKRIVNING_DELSVAR_ID, diagnos.getDiagnosBeskrivning()).build());
        }

        svars.add(aSvar(DIAGNOSGRUND_SVAR_ID).
                withDelsvar(DIAGNOSGRUND_DELSVAR_ID, source.getDiagnosgrund()).
                withDelsvar(DIAGNOSGRUND_NYBEDOMNING_DELSVAR_ID, source.getNyBedomningDiagnosgrund().toString()).build());

        if (!StringUtils.isBlank(source.getFunktionsnedsattningIntellektuell())) {
            svars.add(aSvar(FUNKTIONSNEDSATTNING_INTELLEKTUELL_SVAR_ID).
                    withDelsvar(FUNKTIONSNEDSATTNING_INTELLEKTUELL_DELSVAR_ID, source.getFunktionsnedsattningIntellektuell()).build());
        }

        if (!StringUtils.isBlank(source.getFunktionsnedsattningKommunikation())) {
            svars.add(aSvar(FUNKTIONSNEDSATTNING_KOMMUNIKATION_SVAR_ID).
                    withDelsvar(FUNKTIONSNEDSATTNING_KOMMUNIKATION_DELSVAR_ID, source.getFunktionsnedsattningKommunikation()).build());
        }

        if (!StringUtils.isBlank(source.getFunktionsnedsattningKoncentration())) {
            svars.add(aSvar(FUNKTIONSNEDSATTNING_KONCENTRATION_SVAR_ID).
                    withDelsvar(FUNKTIONSNEDSATTNING_KONCENTRATION_DELSVAR_ID, source.getFunktionsnedsattningKoncentration()).build());
        }

        if (!StringUtils.isBlank(source.getFunktionsnedsattningPsykisk())) {
            svars.add(aSvar(FUNKTIONSNEDSATTNING_PSYKISK_SVAR_ID).
                    withDelsvar(FUNKTIONSNEDSATTNING_PSYKISK_DELSVAR_ID, source.getFunktionsnedsattningPsykisk()).build());
        }

        if (!StringUtils.isBlank(source.getFunktionsnedsattningSynHorselTal())) {
            svars.add(aSvar(FUNKTIONSNEDSATTNING_SYNHORSELTAL_SVAR_ID).
                    withDelsvar(FUNKTIONSNEDSATTNING_SYNHORSELTAL_DELSVAR_ID, source.getFunktionsnedsattningSynHorselTal()).build());
        }

        if (!StringUtils.isBlank(source.getFunktionsnedsattningBalansKoordination())) {
            svars.add(aSvar(FUNKTIONSNEDSATTNING_BALANSKOORDINATION_SVAR_ID).
                    withDelsvar(FUNKTIONSNEDSATTNING_BALANSKOORDINATION_DELSVAR_ID, source.getFunktionsnedsattningBalansKoordination()).build());
        }

        if (!StringUtils.isBlank(source.getFunktionsnedsattningAnnan())) {
            svars.add(aSvar(FUNKTIONSNEDSATTNING_ANNAN_SVAR_ID).
                    withDelsvar(FUNKTIONSNEDSATTNING_ANNAN_DELSVAR_ID, source.getFunktionsnedsattningAnnan()).build());
        }

        svars.add(aSvar(AKTIVITETSBEGRANSNING_SVAR_ID).withDelsvar(AKTIVITETSBEGRANSNING_DELSVAR_ID, source.getAktivitetsbegransning()).build());

        if (!StringUtils.isBlank(source.getAvslutadBehandling())) {
            svars.add(aSvar(AVSLUTADBEHANDLING_SVAR_ID).withDelsvar(AVSLUTADBEHANDLING_DELSVAR_ID, source.getAvslutadBehandling()).build());
        }

        if (!StringUtils.isBlank(source.getPagaendeBehandling())) {
            svars.add(aSvar(PAGAENDEBEHANDLING_SVAR_ID).withDelsvar(PAGAENDEBEHANDLING_DELSVAR_ID, source.getPagaendeBehandling()).build());
        }

        if (!StringUtils.isBlank(source.getPlaneradBehandling())) {
            svars.add(aSvar(PLANERADBEHANDLING_SVAR_ID).withDelsvar(PLANERADBEHANDLING_DELSVAR_ID, source.getPlaneradBehandling()).build());
        }

        if (!StringUtils.isBlank(source.getSubstansintag())) {
            svars.add(aSvar(SUBSTANSINTAG_SVAR_ID).withDelsvar(SUBSTANSINTAG_DELSVAR_ID, source.getSubstansintag()).build());
        }

        svars.add(aSvar(MEDICINSKAFORUTSATTNINGARFORARBETE_SVAR_ID).
                withDelsvar(MEDICINSKAFORUTSATTNINGARFORARBETE_DELSVAR_ID, source.getMedicinskaForutsattningarForArbete()).build());

        if (!StringUtils.isBlank(source.getAktivitetsFormaga())) {
            svars.add(aSvar(AKTIVITETSFORMAGA_SVAR_ID).withDelsvar(AKTIVITETSFORMAGA_DELSVAR_ID, source.getAktivitetsFormaga()).build());
        }

        if (!StringUtils.isBlank(source.getOvrigt())) {
            svars.add(aSvar(OVRIGT_SVAR_ID).withDelsvar(OVRIGT_DELSVAR_ID, source.getOvrigt()).build());
        }

        if (source.getKontaktMedFk() != null) {
            if (source.getKontaktMedFk() && !StringUtils.isBlank(source.getAnledningTillKontakt())) {
                svars.add(aSvar(KONTAKT_ONSKAS_SVAR_ID).
                        withDelsvar(KONTAKT_ONSKAS_DELSVAR_ID, source.getKontaktMedFk().toString()).
                        withDelsvar(ANLEDNING_TILL_KONTAKT_DELSVAR_ID, source.getAnledningTillKontakt()).build());
            } else {
                svars.add(aSvar(KONTAKT_ONSKAS_SVAR_ID).
                        withDelsvar(KONTAKT_ONSKAS_DELSVAR_ID, source.getKontaktMedFk().toString()).build());
            }
        }

        for (Tillaggsfraga tillaggsfraga : source.getTillaggsfragor()) {
            if (!StringUtils.isBlank(tillaggsfraga.getSvar())) {
                svars.add(aSvar(tillaggsfraga.getId()).
                        withDelsvar(tillaggsfraga.getId() + ".1", tillaggsfraga.getSvar()).build());
            }
        }

        return svars;
    }

    private static HsaId anHsaId(String id) {
        HsaId hsaId = new HsaId();
        hsaId.setRoot(HSA_CODE_SYSTEM);
        hsaId.setExtension(id);
        return hsaId;
    }

    private static JAXBElement<CVType> aCV(String codeSystem, String code) {
        CVType cv = new CVType();
        cv.setCodeSystem(codeSystem);
        cv.setCode(code);
        return new JAXBElement<>(new QName("urn:riv:clinicalprocess:healthcond:certificate:types:2", "cv"), CVType.class, null, cv);
    }

    private static SvarBuilder aSvar(String id) {
        return new SvarBuilder(id);
    }

    private static class SvarBuilder {
        private String id;
        private List<Delsvar> delSvars = new ArrayList<>();

        SvarBuilder(String id) {
            this.id = id;
        }

        public Svar build() {
            Svar svar = new Svar();
            svar.setId(id);
            svar.getDelsvar().addAll(delSvars);
            return svar;
        }

        public SvarBuilder withDelsvar(String delsvarsId, Object content) {
            Delsvar delsvar = new Delsvar();
            delsvar.setId(delsvarsId);
            delsvar.getContent().add(content);
            delSvars.add(delsvar);
            return this;
        }
    }

}
