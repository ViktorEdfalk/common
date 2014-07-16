/**
 * Copyright (C) 2013 Inera AB (http://www.inera.se)
 *
 * This file is part of Inera Certificate Modules (http://code.google.com/p/inera-certificate-modules).
 *
 * Inera Certificate Modules is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Inera Certificate Modules is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.certificate.modules.rli.model.converter;

import iso.v21090.dt.v1.CD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.inera.certificate.model.Kod;
import se.inera.certificate.model.PatientRelation;
import se.inera.certificate.model.Rekommendation;
import se.inera.certificate.model.Vardgivare;
import se.inera.certificate.model.util.Strings;
import se.inera.certificate.modules.rli.model.external.Aktivitet;
import se.inera.certificate.modules.rli.model.external.HosPersonal;
import se.inera.certificate.modules.rli.model.external.Observation;
import se.inera.certificate.modules.rli.model.external.Patient;
import se.inera.certificate.modules.rli.model.external.Utforarroll;
import se.inera.certificate.modules.rli.model.external.Vardenhet;
import se.inera.certificate.rli.model.ext.v1.Arrangemang;
import se.inera.certificate.rli.model.v1.AktivitetType;
import se.inera.certificate.rli.model.v1.EnhetType;
import se.inera.certificate.rli.model.v1.HosPersonalType;
import se.inera.certificate.rli.model.v1.ObservationType;
import se.inera.certificate.rli.model.v1.PartialDateInterval;
import se.inera.certificate.rli.model.v1.PatientRelationType;
import se.inera.certificate.rli.model.v1.PatientType;
import se.inera.certificate.rli.model.v1.RekommendationType;
import se.inera.certificate.rli.model.v1.UtforarrollType;
import se.inera.certificate.rli.model.v1.Utlatande;
import se.inera.certificate.rli.model.v1.VardgivareType;

import java.util.ArrayList;
import java.util.List;

import static se.inera.certificate.modules.rli.model.converter.IsoTypeConverter.toArbetsplatsKod;
import static se.inera.certificate.modules.rli.model.converter.IsoTypeConverter.toHsaId;
import static se.inera.certificate.modules.rli.model.converter.IsoTypeConverter.toPersonId;
import static se.inera.certificate.modules.rli.model.converter.IsoTypeConverter.toUtlatandeId;
import static se.inera.certificate.modules.rli.model.converter.IsoTypeConverter.toUtlatandeTyp;

public class ExternalToTransportConverter {

    private static final Logger LOG = LoggerFactory.getLogger(ExternalToTransportConverter.class);

    public Utlatande convert(se.inera.certificate.modules.rli.model.external.Utlatande source)
            throws ConverterException {

        LOG.debug("Converting Utlatande '{}' from external to transport", source.getId());

        Utlatande transportModel = new Utlatande();

        transportModel.setUtlatandeId(toUtlatandeId((source.getId())));

        transportModel.setTypAvUtlatande(toUtlatandeTyp(source.getTyp()));

        if (source.getKommentarer() != null) {
            transportModel.getKommentars().addAll(source.getKommentarer());
        }

        transportModel.setSigneringsdatum(source.getSigneringsdatum());

        transportModel.setSkickatdatum(source.getSkickatdatum());

        // Convertions from here on

        transportModel.setPatient(convertPatient(source.getPatient()));

        transportModel.setSkapadAv(convertHosPersonal(source.getSkapadAv()));

        transportModel.setArrangemang(convertArrangemang(source.getArrangemang()));

        if (source.getAktiviteter() != null) {
            transportModel.getAktivitets().addAll(convertAktiviteter(source.getAktiviteter()));
        }
        if (source.getObservationer() != null) {
            transportModel.getObservations().addAll(convertObservationer(source.getObservationer()));
        }
        if (source.getRekommendationer() != null) {
            transportModel.getRekommendations().addAll(convertRekommendationer(source.getRekommendationer()));
        }
        return transportModel;
    }

    private List<RekommendationType> convertRekommendationer(List<Rekommendation> source) {
        LOG.trace("Starting convertRekommendationer");

        List<RekommendationType> rekommendationTypes = new ArrayList<>();
        for (Rekommendation r : source) {
            if (r != null) {
                rekommendationTypes.add(convertRekommendation(r));
            }
        }
        return rekommendationTypes;
    }

    private RekommendationType convertRekommendation(Rekommendation source) {
        if (source == null) {
            LOG.trace("Rekommendation was null, could not convert");
            return null;
        }
        RekommendationType rType = new RekommendationType();
        rType.setBeskrivning(source.getBeskrivning());
        rType.setRekommendationskod(IsoTypeConverter.toCD(source.getRekommendationskod()));
        rType.setSjukdomskannedom(IsoTypeConverter.toCD(source.getSjukdomskannedom()));

        return rType;
    }

    private List<ObservationType> convertObservationer(List<Observation> source) {
        LOG.trace("Starting convertObservationer");
        if (source == null) {
            LOG.trace("List<Observation> was null, could not convert");
            return null;
        }
        List<ObservationType> observationTypes = new ArrayList<>();
        for (Observation o : source) {
            if (o != null) {
                observationTypes.add(convertObservation(o));
            }
        }
        return observationTypes;
    }

    private ObservationType convertObservation(Observation source) {
        if (source == null) {
            LOG.trace("Observation was null, could not convert");
            return null;
        }
        ObservationType observationType = new ObservationType();

        observationType.setObservationskod(IsoTypeConverter.toCD(source.getObservationskod()));
        observationType.setObservationsperiod(convertPartialDateInterval(source.getObservationsperiod()));
        observationType.setUtforsAv(convertUtforarroll(source.getUtforsAv()));

        return observationType;
    }

    private List<UtforarrollType> convertUtforarroller(List<Utforarroll> source) {
        LOG.trace("Starting convertUtforarroller");
        if (source == null) {
            LOG.trace("Utforarroll list was null, could not convert");
            return null;
        }
        List<UtforarrollType> converted = new ArrayList<>();
        for (Utforarroll a : source) {
            if (a != null) {
                converted.add(convertUtforarroll(a));
            }
        }
        return converted;
    }

    private UtforarrollType convertUtforarroll(Utforarroll source) {
        if (source == null) {
            LOG.trace("Utforarroll was null, could not convert");
            return null;
        }
        UtforarrollType utforsAv = new UtforarrollType();
        utforsAv.setAntasAv(convertHosPersonal(source.getAntasAv()));
        utforsAv.setUtforartyp(IsoTypeConverter.toCD(source.getUtforartyp()));

        return utforsAv;
    }

    private HosPersonalType convertHosPersonal(HosPersonal source) {
        if (source == null) {
            LOG.trace("HosPersonal was null, could not convert");
            return null;
        }
        HosPersonalType hosPersonalType = new HosPersonalType();
        hosPersonalType.setEnhet(convertEnhet(source.getVardenhet()));
        hosPersonalType.setFullstandigtNamn(source.getNamn());
        hosPersonalType.setPersonalId(IsoTypeConverter.toII(source.getId()));

        return hosPersonalType;
    }

    private PartialDateInterval convertPartialDateInterval(se.inera.certificate.model.PartialInterval source) {
        if (source == null) {
            LOG.trace("Source PartialDateInterval was null, could not convert");
            return null;
        }
        PartialDateInterval pdi = new PartialDateInterval();
        pdi.setFrom(source.getFrom());
        pdi.setTom(source.getTom());
        return pdi;
    }

    private List<AktivitetType> convertAktiviteter(List<Aktivitet> source) {
        LOG.trace("Starting convertAktiviteter");
        if (source == null) {
            LOG.trace("Aktivitet list was null, could not convert");
            return null;
        }
        List<AktivitetType> converted = new ArrayList<>();
        for (Aktivitet a : source) {
            if (a != null) {
                converted.add(convertAktivitet(a));
            }
        }
        return converted;
    }

    private AktivitetType convertAktivitet(Aktivitet source) {
        if (source == null) {
            LOG.trace("Aktivitet was null, could not convert");
            return null;
        }
        AktivitetType aktivitet = new AktivitetType();

        aktivitet.setAktivitetskod(IsoTypeConverter.toCD(source.getAktivitetskod()));
        aktivitet.setAktivitetstid(convertPartialDateInterval(source.getAktivitetstid()));
        aktivitet.setUtforsVidEnhet(convertEnhet(source.getUtforsVid()));
        aktivitet.setPlats(source.getPlats());
        aktivitet.getBeskrivsAvs().addAll(convertUtforarroller(source.getBeskrivsAv()));

        return aktivitet;
    }

    private EnhetType convertEnhet(Vardenhet source) {
        if (source == null) {
            LOG.trace("Enhet was null, could not convert");
            return null;
        }
        EnhetType enhetType = new EnhetType();

        if (source.getArbetsplatskod() != null) {
            enhetType.setArbetsplatskod(toArbetsplatsKod(source.getArbetsplatskod()));
        }
        enhetType.setEnhetsId(toHsaId(source.getId()));
        enhetType.setEnhetsnamn(source.getNamn());
        enhetType.setEpost(source.getEpost());
        enhetType.setPostadress(source.getPostadress());
        enhetType.setPostnummer(source.getPostnummer());
        enhetType.setPostort(source.getPostort());
        enhetType.setTelefonnummer(source.getTelefonnummer());
        enhetType.setVardgivare(convertVardgivare(source.getVardgivare()));

        return enhetType;
    }

    private VardgivareType convertVardgivare(Vardgivare source) {
        if (source == null) {
            LOG.trace("Vardgivare was null, could not convert");
            return null;
        }
        VardgivareType vardgivareType = new VardgivareType();

        vardgivareType.setVardgivareId(toHsaId(source.getId()));
        vardgivareType.setVardgivarnamn(source.getNamn());
        return vardgivareType;
    }

    private Arrangemang convertArrangemang(se.inera.certificate.modules.rli.model.external.Arrangemang source) {
        if (source == null) {
            LOG.trace("Arrangemang was null, could not convert");
            return null;
        }

        Arrangemang arrangemang = new Arrangemang();

        arrangemang.setArrangemangstid(convertPartialDateInterval(source.getArrangemangstid()));
        arrangemang.setArrangemangstyp(IsoTypeConverter.toCD(source.getArrangemangstyp()));
        arrangemang.setAvbestallningsdatum(source.getAvbestallningsdatum());
        arrangemang.setBokningsdatum(source.getBokningsdatum());
        arrangemang.setBokningsreferens(source.getBokningsreferens());
        arrangemang.setPlats(source.getPlats());

        return arrangemang;
    }

    private PatientType convertPatient(Patient source) {
        if (source == null) {
            LOG.trace("Patient was null, could not convert");
            return null;
        }

        PatientType patientType = new PatientType();
        patientType.getFornamns().addAll(source.getFornamn());
        if (source.getMellannamn().isEmpty()) {
            patientType.setEfternamn(source.getEfternamn());
        } else {
            patientType.setEfternamn(Strings.join(" ", source.getMellannamn()) + " " + source.getEfternamn());
        }
        patientType.setPersonId(toPersonId(source.getId()));
        patientType.setPostadress(source.getPostadress());
        patientType.setPostnummer(source.getPostnummer());
        patientType.setPostort(source.getPostort());

        if (source.getPatientrelationer() != null) {
            List<PatientRelationType> patientRelationsTypes = convertPatientRelations(source.getPatientrelationer());
            patientType.getPatientRelations().addAll(patientRelationsTypes);

        }

        return patientType;
    }

    private List<PatientRelationType> convertPatientRelations(List<? extends PatientRelation> source) {
        LOG.trace("Starting convert in convertPatientRelations");
        if (source == null) {
            LOG.trace("PatientRelation was null, could not convert");
            return null;
        }
        List<PatientRelationType> patientRelationTypes = new ArrayList<>();

        for (PatientRelation p : source) {
            if (p != null) {
                patientRelationTypes.add(convertPatientRelation(p));
            }
        }
        return patientRelationTypes;
    }

    private PatientRelationType convertPatientRelation(PatientRelation source) {
        if (source == null) {
            LOG.trace("PatientRelation was null, could not convert");
            return null;
        }
        PatientRelationType patientRelationType = new PatientRelationType();

        patientRelationType.setPostadress(source.getPostadress());
        patientRelationType.setPostnummer(source.getPostnummer());
        patientRelationType.setPostort(source.getPostort());

        patientRelationType.setPersonId(toPersonId(source.getPersonId()));
        patientRelationType.setRelationskategori(IsoTypeConverter.toCD(source.getRelationskategori()));
        patientRelationType.getFornamns().addAll(source.getFornamn());
        patientRelationType.getMellannamns().addAll(source.getMellannamn());
        patientRelationType.setEfternamn(source.getEfternamn());
        patientRelationType.getRelationTyps().addAll(convertRelationTyps(source.getRelationtyper()));

        return patientRelationType;
    }

    private List<CD> convertRelationTyps(List<Kod> source) {
        List<CD> relationTyps = new ArrayList<>();
        for (Kod k : source) {
            relationTyps.add(IsoTypeConverter.toCD(k));
        }
        return relationTyps;
    }
}
