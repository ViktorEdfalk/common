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
package se.inera.certificate.modules.rli.model.converters;

import iso.v21090.dt.v1.CD;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import riv.insuranceprocess.healthreporting._2.EnhetType;
import riv.insuranceprocess.healthreporting._2.HosPersonalType;
import riv.insuranceprocess.healthreporting._2.VardgivareType;
import se.inera.certificate.common.v1.AktivitetType;
import se.inera.certificate.common.v1.ObservationType;
import se.inera.certificate.common.v1.PartialDateInterval;
import se.inera.certificate.common.v1.PatientRelationType;
import se.inera.certificate.common.v1.PatientType;
import se.inera.certificate.common.v1.RekommendationType;
import se.inera.certificate.common.v1.UtforarrollType;
import se.inera.certificate.common.v1.Utlatande;
import se.inera.certificate.model.HosPersonal;
import se.inera.certificate.model.Kod;
import se.inera.certificate.model.Observation;
import se.inera.certificate.model.Patient;
import se.inera.certificate.model.PatientRelation;
import se.inera.certificate.model.Rekommendation;
import se.inera.certificate.model.Utforarroll;
import se.inera.certificate.model.Vardenhet;
import se.inera.certificate.model.Vardgivare;
import se.inera.certificate.modules.rli.model.external.Aktivitet;

import se.inera.certificate.rli.v1.Arrangemang;

public class ExternalToTransportConverterImpl implements ExternalToTransportConverter {
    private static final Logger LOG = LoggerFactory.getLogger(ExternalToTransportConverterImpl.class);

    public ExternalToTransportConverterImpl() {

    }

    @Override
    public Utlatande externalToTransport(se.inera.certificate.modules.rli.model.external.Utlatande source) {

        LOG.debug("Starting conversion");

        Utlatande transportModel = new Utlatande();

        transportModel.setUtlatandeId(IsoTypeConverter.toII(source.getId()));

        transportModel.setTypAvUtlatande(IsoTypeConverter.toCD(source.getTyp()));

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

    List<RekommendationType> convertRekommendationer(List<Rekommendation> source) {
        LOG.debug("Starting convertRekommendationer");

        List<RekommendationType> rekommendationTypes = new ArrayList<RekommendationType>();
        for (Rekommendation r : source) {
            if (r != null) {
                rekommendationTypes.add(convertRekommendation(r));
            }
        }
        return rekommendationTypes;
    }

    RekommendationType convertRekommendation(Rekommendation source) {
        if (source == null) {
            LOG.debug("Rekommendation was null, could not convert");
            return null;
        }
        RekommendationType rType = new RekommendationType();
        rType.setBeskrivning(source.getBeskrivning());
        rType.setRekommendationskod(IsoTypeConverter.toCD(source.getRekommendationskod()));
        rType.setSjukdomskannedom(IsoTypeConverter.toCD(source.getSjukdomskannedom()));

        return rType;
    }

    List<ObservationType> convertObservationer(List<Observation> source) {
        LOG.debug("Starting convertObservationer");
        if (source == null) {
            LOG.debug("List<Observation> was null, could not convert");
            return null;
        }
        List<ObservationType> observationTypes = new ArrayList<ObservationType>();
        for (Observation o : source) {
            if (o != null) {
                observationTypes.add(convertObservation(o));
            }
        }
        return observationTypes;
    }

    ObservationType convertObservation(Observation source) {
        if (source == null) {
            LOG.debug("Observation was null, could not convert");
            return null;
        }
        ObservationType observationType = new ObservationType();

        observationType.setObservationskod(IsoTypeConverter.toCD(source.getObservationsKod()));
        observationType.setObservationsperiod(convertPartialDateInterval(source.getObservationsPeriod()));
        observationType.setUtforsAv(convertUtforarroll(source.getUtforsAv()));

        return observationType;
    }

    UtforarrollType convertUtforarroll(Utforarroll source) {
        if (source == null) {
            LOG.debug("Utforarroll was null, could not convert");
            return null;
        }
        UtforarrollType utforsAv = new UtforarrollType();
        utforsAv.setAntasAv(convertHosPersonal(source.getAntasAv()));
        utforsAv.setUtforartyp(IsoTypeConverter.toCD(source.getUtforartyp()));

        return utforsAv;
    }

    HosPersonalType convertHosPersonal(HosPersonal source) {
        if (source == null) {
            LOG.debug("HosPersonal was null, could not convert");
            return null;
        }
        HosPersonalType hosPersonalType = new HosPersonalType();
        hosPersonalType.setEnhet(convertEnhet(source.getVardenhet()));
        hosPersonalType.setForskrivarkod(source.getForskrivarkod());
        hosPersonalType.setFullstandigtNamn(source.getNamn());
        hosPersonalType.setPersonalId(IsoTypeConverter.toII(source.getId()));

        return hosPersonalType;
    }

    private PartialDateInterval convertPartialDateInterval(
            se.inera.certificate.model.PartialInterval source) {
        if (source == null) {
            LOG.debug("Source PartialDateInterval was null, could not convert");
            return null;
        }
        PartialDateInterval pdi = new PartialDateInterval();
        pdi.setFrom(source.getFrom());
        pdi.setTom(source.getTom());
        return pdi;
    }

    List<AktivitetType> convertAktiviteter(List<Aktivitet> source) {
        LOG.debug("Starting convertAktiviteter");
        if (source == null) {
            LOG.debug("Aktivitet list was null, could not convert");
            return null;
        }
        List<AktivitetType> converted = new ArrayList<AktivitetType>();
        for (Aktivitet a : source) {
            if (a != null) {
                converted.add(convertAktivitet(a));
            }
        }
        return converted;
    }

    AktivitetType convertAktivitet(Aktivitet source) {
        if (source == null) {
            LOG.debug("Aktivitet was null, could not convert");
            return null;
        }
        AktivitetType aktivitet = new AktivitetType();

        aktivitet.setAktivitetskod(IsoTypeConverter.toCD(source.getAktivitetskod()));
        aktivitet.setAktivitetstid(convertPartialDateInterval(source.getAktivitetstid()));
        aktivitet.setUtforsVidEnhet(convertEnhet(source.getUtforsVid()));

        return aktivitet;
    }

    private EnhetType convertEnhet(Vardenhet source) {
        if (source == null) {
            LOG.debug("Enhet was null, could not convert");
            return null;
        }
        EnhetType enhetType = new EnhetType();

        if (source.getArbetsplatskod() != null) {
            enhetType.setArbetsplatskod(IsoTypeConverter.toII(source.getArbetsplatskod()));
        }
        enhetType.setEnhetsId(IsoTypeConverter.toII(source.getId()));
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
            LOG.debug("Vardgivare was null, could not convert");
            return null;
        }
        VardgivareType vardgivareType = new VardgivareType();

        vardgivareType.setVardgivareId(IsoTypeConverter.toII(source.getId()));
        vardgivareType.setVardgivarnamn(source.getNamn());
        return vardgivareType;
    }

    private Arrangemang convertArrangemang(se.inera.certificate.modules.rli.model.external.Arrangemang source) {
        if (source == null) {
            LOG.debug("Arrangemang was null, could not convert");
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

    PatientType convertPatient(Patient source) {
        if (source == null) {
            LOG.debug("Patient was null, could not convert");
            return null;
        }
        PatientType patientType = new PatientType();

        patientType.setPostadress(source.getPostadress());
        patientType.setPostnummer(source.getPostnummer());
        patientType.setPostort(source.getPostort());

        patientType.setPersonId(IsoTypeConverter.toII(source.getId()));

        if (source.getFornamns() != null) {
            patientType.getFornamns().addAll(source.getFornamns());
        }

        if (source.getMellannamns() != null) {
            patientType.getMellannamns().addAll(source.getMellannamns());
        }

        patientType.setEfternamn(StringUtils.join(source.getEfternamns(), " "));

        if (source.getPatientrelations() != null) {
            List<PatientRelationType> patientRelationsTypes = convertPatientRelations(source.getPatientrelations());
            patientType.getPatientRelations().addAll(patientRelationsTypes);

        }

        return patientType;
    }

    List<PatientRelationType> convertPatientRelations(List<PatientRelation> source) {
        LOG.debug("Starting convert in convertPatientRelations");
        if (source == null) {
            LOG.debug("PatientRelation was null, could not convert");
            return null;
        }
        List<PatientRelationType> patientRelationTypes = new ArrayList<PatientRelationType>();

        for (PatientRelation p : source) {
            if (p != null) {
                patientRelationTypes.add(convertPatientRelation(p));
            }
        }
        return patientRelationTypes;
    }

    private PatientRelationType convertPatientRelation(PatientRelation source) {
        if (source == null) {
            LOG.debug("PatientRelation was null, could not convert");
            return null;
        }
        PatientRelationType patientRelationType = new PatientRelationType();

        patientRelationType.setPostadress(source.getPostadress());
        patientRelationType.setPostnummer(source.getPostnummer());
        patientRelationType.setPostort(source.getPostort());

        patientRelationType.setPersonId(IsoTypeConverter.toII(source.getPersonId()));
        patientRelationType.setRelationskategori(IsoTypeConverter.toCD(source.getRelationskategori()));
        patientRelationType.getFornamns().addAll(source.getFornamns());
        if (source.getMellannamns() != null) {
            patientRelationType.getMellannamns().addAll(source.getMellannamns());
        }
        patientRelationType.setEfternamn(source.getEfternamn());
        patientRelationType.getRelationTyps().addAll(convertRelationTyps(source.getRelationTyps()));

        return patientRelationType;
    }

    private List<CD> convertRelationTyps(List<Kod> source) {
        List<CD> relationTyps = new ArrayList<CD>();
        for (Kod k : source) {
            relationTyps.add(IsoTypeConverter.toCD(k));
        }
        return relationTyps;
    }

}
