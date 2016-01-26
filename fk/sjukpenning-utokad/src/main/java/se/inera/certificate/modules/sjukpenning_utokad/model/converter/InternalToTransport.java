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

package se.inera.certificate.modules.sjukpenning_utokad.model.converter;

import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.*;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;


import se.inera.certificate.modules.sjukpenning_utokad.model.internal.*;
import se.inera.certificate.modules.sjukpenning_utokad.support.SjukpenningUtokadEntryPoint;
import se.inera.intyg.common.support.common.enumerations.Diagnoskodverk;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.*;
import se.riv.clinicalprocess.healthcond.certificate.v2.*;
import se.riv.clinicalprocess.healthcond.certificate.v2.Svar.Delsvar;

public final class InternalToTransport {

    private InternalToTransport() {
    }

    public static RegisterCertificateType convert(SjukpenningUtokadUtlatande source) throws ConverterException {
        if (source == null) {
            throw new ConverterException("Source utlatande was null, cannot convert");
        }

        RegisterCertificateType sjukpenningUtokadType = new RegisterCertificateType();
        sjukpenningUtokadType.setIntyg(getIntyg(source));
        return sjukpenningUtokadType;
    }

    private static Intyg getIntyg(SjukpenningUtokadUtlatande source) {
        Intyg intyg = new Intyg();
        intyg.setTyp(getTypAvIntyg(source));
        intyg.setIntygsId(getIntygsId(source));
        intyg.setVersion("1"); // TODO
        intyg.setSigneringstidpunkt(source.getGrundData().getSigneringsdatum());
        // Set this to something else perhaps?
        intyg.setSkickatTidpunkt(source.getGrundData().getSigneringsdatum());
        intyg.setSkapadAv(getSkapadAv(source));
        intyg.setPatient(getPatient(source.getGrundData().getPatient()));
        intyg.getSvar().addAll(getSvar(source));
        return intyg;
    }

    private static HosPersonal getSkapadAv(SjukpenningUtokadUtlatande source) {
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

    private static IntygId getIntygsId(SjukpenningUtokadUtlatande source) {
        IntygId intygId = new IntygId();
        intygId.setRoot(source.getGrundData().getSkapadAv().getVardenhet().getEnhetsid());
        intygId.setExtension(source.getId());
        return intygId;
    }

    private static TypAvIntyg getTypAvIntyg(SjukpenningUtokadUtlatande source) {
        TypAvIntyg typAvIntyg = new TypAvIntyg();
        typAvIntyg.setCode(SjukpenningUtokadEntryPoint.MODULE_ID);
        typAvIntyg.setCodeSystem(CERTIFICATE_CODE_SYSTEM);
        typAvIntyg.setDisplayName(SjukpenningUtokadEntryPoint.MODULE_NAME);
        return typAvIntyg;
    }

    private static List<Svar> getSvar(SjukpenningUtokadUtlatande source) {
        List<Svar> svars = new ArrayList<>();

        
        if (source.getUndersokningAvPatienten() != null) {
            svars.add(aSvar(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID)
                    .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID,
                            aCV(GRUNDFORMEDICINSKTUNDERLAG_CODE_SYSTEM, Integer.toString(UNDERSOKNING_AV_PATIENT)))
                    .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID, source.getUndersokningAvPatienten().asLocalDate().toString()).build());
        }

        if (source.getTelefonkontaktMedPatienten() != null) {
            svars.add(aSvar(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID)
                    .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID,
                            aCV(GRUNDFORMEDICINSKTUNDERLAG_CODE_SYSTEM, Integer.toString(TELEFONKONTAKT_MED_PATIENT)))
                    .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID, source.getTelefonkontaktMedPatienten().asLocalDate().toString()).build());
        }
        

        if (source.getJournaluppgifter() != null) {
            svars.add(aSvar(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID)
                    .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID,
                            aCV(GRUNDFORMEDICINSKTUNDERLAG_CODE_SYSTEM, Integer.toString(JOURNALUPPGIFTER)))
                    .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID, source.getJournaluppgifter().asLocalDate().toString()).build());
        }
        if (source.getAnhorigsBeskrivningAvPatienten() != null) {
            svars.add(aSvar(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID)
                    .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID,
                            aCV(GRUNDFORMEDICINSKTUNDERLAG_CODE_SYSTEM, Integer.toString(ANHORIGSBESKRIVNING)))
                    .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID, source.getAnhorigsBeskrivningAvPatienten().asLocalDate().toString())
                    .build());
        }
        if (source.getAnnatGrundForMU() != null) {
            svars.add(
                    aSvar(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID)
                            .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID,
                                    aCV(GRUNDFORMEDICINSKTUNDERLAG_CODE_SYSTEM, Integer.toString(ANNAT)))
                            .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID, source.getAnnatGrundForMU().asLocalDate().toString())
                            .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_ANNANBESKRIVNING_DELSVAR_ID, source.getAnnatGrundForMUBeskrivning()).build());
        }

        svars.add(aSvar(TYP_AV_SYSSELSATTNING_SVAR_ID)
                .withDelsvar(TYP_AV_SYSSELSATTNING_DELSVAR_ID, aCV(TYP_AV_SYSSELSATTNING_CODE_SYSTEM,
                        Integer.toString(source.getSysselsattning().getSysselsattningsTyp().getId()))).build());
        
        if (source.getNuvarandeArbete()!= null) {
            svars.add(aSvar(NUVARANDE_ARBETE_SVAR_ID)
                .withDelsvar(NUVARANDE_ARBETE_DELSVAR_ID, source.getNuvarandeArbete()).build());
        }

        for (Diagnos diagnos : source.getDiagnoser()) {
            Diagnoskodverk diagnoskodverk = Diagnoskodverk.valueOf(diagnos.getDiagnosKodSystem());
            svars.add(aSvar(DIAGNOS_SVAR_ID).withDelsvar(DIAGNOS_DELSVAR_ID, aCV(diagnoskodverk.getCodeSystem(), diagnos.getDiagnosKod()))
                    .withDelsvar(DIAGNOS_BESKRIVNING_DELSVAR_ID, diagnos.getDiagnosBeskrivning()).build());
        }

        svars.add(aSvar(FUNKTIONSNEDSATTNING_SVAR_ID).withDelsvar(FUNKTIONSNEDSATTNING_DELSVAR_ID, source.getFunktionsnedsattning()).build());

        svars.add(aSvar(AKTIVITETSBEGRANSNING_SVAR_ID).withDelsvar(AKTIVITETSBEGRANSNING_DELSVAR_ID, source.getAktivitetsbegransning()).build());

        if (source.getPagaendeBehandling() != null) {
            svars.add(aSvar(PAGAENDEBEHANDLING_SVAR_ID).withDelsvar(PAGAENDEBEHANDLING_DELSVAR_ID, source.getPagaendeBehandling()).build());
        }

        if (source.getPlaneradBehandling() != null) {
            svars.add(aSvar(PLANERADBEHANDLING_SVAR_ID).withDelsvar(PLANERADBEHANDLING_DELSVAR_ID, source.getPlaneradBehandling()).build());
        }

        for (Sjukskrivning sjukskrivning : source.getSjukskrivningar()) {
            DatePeriodType period = new DatePeriodType();
            period.setStart(sjukskrivning.getPeriod().fromAsLocalDate());
            period.setEnd(sjukskrivning.getPeriod().tomAsLocalDate());
            svars.add(aSvar(BEHOV_AV_SJUKSKRIVNING_SVAR_ID)
                    .withDelsvar(BEHOV_AV_SJUKSKRIVNING_NIVA_DELSVARSVAR_ID,
                            aCV(SJUKSKRIVNING_CODE_SYSTEM, Integer.toString(sjukskrivning.getSjukskrivningsgrad().getId())))
                    .withDelsvar(BEHOV_AV_SJUKSKRIVNING_PERIOD_DELSVARSVAR_ID, period).build()); 
                            
        }

        if (source.getForsakringsmedicinsktBeslutsstod() != null) {
            svars.add(aSvar(FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_ID)
                    .withDelsvar(FORSAKRINGSMEDICINSKT_BESLUTSSTOD_DELSVAR_ID, source.getForsakringsmedicinsktBeslutsstod()).build());
        }

        if (source.getArbetstidsforlaggning() != null) {
            if (source.getArbetstidsforlaggning() && source.getArbetstidsforlaggningMotivering() != null) {
                svars.add(aSvar(ARBETSTIDSFORLAGGNING_SVAR_ID)
                        .withDelsvar(ARBETSTIDSFORLAGGNING_OM_DELSVAR_ID, source.getArbetstidsforlaggning().toString())
                        .withDelsvar(ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_ID, source.getArbetstidsforlaggningMotivering()).build());
            } else {
                svars.add(aSvar(ARBETSTIDSFORLAGGNING_SVAR_ID)
                        .withDelsvar(ARBETSTIDSFORLAGGNING_OM_DELSVAR_ID, source.getArbetstidsforlaggning().toString()).build());
            }
        }

        if (source.getArbetsresor() != null) {
            svars.add(aSvar(ARBETSRESOR_SVAR_ID)
                    .withDelsvar(ARBETSRESOR_OM_DELSVAR_ID, source.getArbetsresor().toString()).build());
        }

        if (source.getFormagaTrotsBegransning() != null) {
            svars.add(aSvar(AKTIVITETSFORMAGA_SVAR_ID)
                    .withDelsvar(AKTIVITETSFORMAGA_DELSVAR_ID, source.getFormagaTrotsBegransning()).build());
        }

        svars.add(aSvar(PROGNOS_SVAR_ID)
                .withDelsvar(PROGNOS_BESKRIVNING_DELSVAR_ID, aCV(PROGNOS_CODE_SYSTEM, Integer.toString(source.getPrognos().getPrognosTyp().getId())))
                .withDelsvar(PROGNOS_FORTYDLIGANDE_DELSVAR_ID, source.getPrognos().getFortydligande()).build());

        /* Build complex object */
        SvarBuilder arbetslivsinriktadeAtgarderBuilder = aSvar(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID);
        source.getArbetslivsinriktadeAtgarder().stream()
            .forEach((ArbetslivsinriktadeAtgarder atgarder) -> {
                arbetslivsinriktadeAtgarderBuilder.withDelsvar(ARBETSLIVSINRIKTADE_ATGARDER_VAL_DELSVAR_ID,
                                    aCV(ARBETSLIVSINRIKTADE_ATGARDER_CODE_SYSTEM, Integer.toString(atgarder.getVal().getId())));
            });

        if (source.getArbetslivsinriktadeAtgarderAktuelltBeskrivning() != null) {
            arbetslivsinriktadeAtgarderBuilder.withDelsvar(ARBETSLIVSINRIKTADE_ATGARDER_AKTUELLT_BESKRIVNING_DELSVAR_ID, source.getArbetslivsinriktadeAtgarderAktuelltBeskrivning());
        }

        if (source.getArbetslivsinriktadeAtgarderEjAktuelltBeskrivning() != null) {
            arbetslivsinriktadeAtgarderBuilder.withDelsvar(ARBETSLIVSINRIKTADE_ATGARDER_EJ_AKTUELLT_BESKRIVNING_DELSVAR_ID, source.getArbetslivsinriktadeAtgarderEjAktuelltBeskrivning());
        }
        svars.add(arbetslivsinriktadeAtgarderBuilder.build());
        /* End complex object*/

        if (source.getOvrigt() != null) {
            svars.add(aSvar(OVRIGT_SVAR_ID).withDelsvar(OVRIGT_DELSVAR_ID, source.getOvrigt()).build());
        }

        if (source.getKontaktMedFk() != null) {
            if (source.getKontaktMedFk() && source.getAnledningTillKontakt() != null) {
                svars.add(aSvar(KONTAKT_ONSKAS_SVAR_ID).withDelsvar(KONTAKT_ONSKAS_DELSVAR_ID, source.getKontaktMedFk().toString())
                        .withDelsvar(ANLEDNING_TILL_KONTAKT_DELSVAR_ID, source.getAnledningTillKontakt()).build());
            } else {
                svars.add(aSvar(KONTAKT_ONSKAS_SVAR_ID).withDelsvar(KONTAKT_ONSKAS_DELSVAR_ID, source.getKontaktMedFk().toString()).build());
            }
        }

        for (Tillaggsfraga tillaggsfraga : source.getTillaggsfragor()) {
            svars.add(aSvar(tillaggsfraga.getId()).withDelsvar(tillaggsfraga.getId() + ".1", tillaggsfraga.getSvar()).build());
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
