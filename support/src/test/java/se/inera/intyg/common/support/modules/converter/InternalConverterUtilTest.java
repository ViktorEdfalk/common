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

package se.inera.intyg.common.support.modules.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.junit.Test;

import se.inera.intyg.common.support.common.enumerations.*;
import se.inera.intyg.common.support.model.common.internal.*;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Relation;
import se.inera.intyg.common.support.model.common.internal.Vardgivare;
import se.inera.intyg.common.support.modules.support.api.dto.Personnummer;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.CVType;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.DatePeriodType;
import se.riv.clinicalprocess.healthcond.certificate.v2.*;

public class InternalConverterUtilTest {

    @Test
    public void testConvert() throws Exception {
        final String intygsId = "intygsid";
        final String enhetsId = "enhetsid";
        final String enhetsnamn = "enhetsnamn";
        final String patientPersonId = "pid";
        final String skapadAvFullstandigtNamn = "fullstÃ¤ndigt namn";
        final String skapadAvPersonId = "skapad av pid";
        final LocalDateTime signeringsdatum = LocalDateTime.now();
        final String arbetsplatsKod = "arbetsplatsKod";
        final String postadress = "postadress";
        final String postNummer = "postNummer";
        final String postOrt = "postOrt";
        final String epost = "epost";
        final String telefonNummer = "telefonNummer";
        final String vardgivarid = "vardgivarid";
        final String vardgivarNamn = "vardgivarNamn";
        final String forskrivarKod = "forskrivarKod";
        final String fornamn = "fornamn";
        final String efternamn = "efternamn";
        final String mellannamn = "mellannamn";
        final String patientPostadress = "patientPostadress";
        final String patientPostnummer = "patientPostnummer";
        final String patientPostort = "patientPostort";

        Utlatande utlatande = buildUtlatande(intygsId, enhetsId, enhetsnamn, patientPersonId,
                skapadAvFullstandigtNamn, skapadAvPersonId, signeringsdatum, arbetsplatsKod, postadress, postNummer, postOrt, epost, telefonNummer,
                vardgivarid, vardgivarNamn, forskrivarKod, fornamn, efternamn, mellannamn, patientPostadress, patientPostnummer, patientPostort,
                null, null);

        Intyg intyg = InternalConverterUtil.getIntyg(utlatande);

        assertEquals(enhetsId, intyg.getIntygsId().getRoot());
        assertEquals(intygsId, intyg.getIntygsId().getExtension());
        assertNotNull(intyg.getVersion());
        assertEquals(signeringsdatum, intyg.getSigneringstidpunkt());
        assertNotNull(patientPersonId, intyg.getPatient().getPersonId().getRoot());
        assertEquals(patientPersonId, intyg.getPatient().getPersonId().getExtension());
        assertEquals(skapadAvFullstandigtNamn, intyg.getSkapadAv().getFullstandigtNamn());
        assertNotNull(skapadAvPersonId, intyg.getSkapadAv().getPersonalId().getRoot());
        assertEquals(skapadAvPersonId, intyg.getSkapadAv().getPersonalId().getExtension());
        assertNotNull(intyg.getSkapadAv().getEnhet().getEnhetsId().getRoot());
        assertEquals(enhetsId, intyg.getSkapadAv().getEnhet().getEnhetsId().getExtension());
        assertNotNull(intyg.getSkapadAv().getEnhet().getEnhetsId().getExtension());
        assertEquals(enhetsnamn, intyg.getSkapadAv().getEnhet().getEnhetsnamn());
        assertNotNull(intyg.getSkapadAv().getEnhet().getArbetsplatskod().getRoot());
        assertEquals(arbetsplatsKod, intyg.getSkapadAv().getEnhet().getArbetsplatskod().getExtension());
        assertEquals(postadress, intyg.getSkapadAv().getEnhet().getPostadress());
        assertEquals(postNummer, intyg.getSkapadAv().getEnhet().getPostnummer());
        assertEquals(postOrt, intyg.getSkapadAv().getEnhet().getPostort());
        assertEquals(epost, intyg.getSkapadAv().getEnhet().getEpost());
        assertEquals(telefonNummer, intyg.getSkapadAv().getEnhet().getTelefonnummer());
        assertNotNull(intyg.getSkapadAv().getEnhet().getVardgivare().getVardgivareId().getRoot());
        assertEquals(vardgivarid, intyg.getSkapadAv().getEnhet().getVardgivare().getVardgivareId().getExtension());
        assertEquals(vardgivarNamn, intyg.getSkapadAv().getEnhet().getVardgivare().getVardgivarnamn());
        assertEquals(forskrivarKod, intyg.getSkapadAv().getForskrivarkod());
        assertEquals(fornamn, intyg.getPatient().getFornamn());
        assertEquals(efternamn, intyg.getPatient().getEfternamn());
        assertEquals(mellannamn, intyg.getPatient().getMellannamn());
        assertEquals(patientPostadress, intyg.getPatient().getPostadress());
        assertEquals(patientPostnummer, intyg.getPatient().getPostnummer());
        assertEquals(patientPostort, intyg.getPatient().getPostort());
        assertTrue(intyg.getRelation().isEmpty());
    }

    @Test
    public void testConvertWithRelation() {
        RelationKod relationKod = RelationKod.FRLANG;
        String relationIntygsId = "relationIntygsId";
        Utlatande utlatande = buildUtlatande(relationKod, relationIntygsId);

        Intyg intyg = InternalConverterUtil.getIntyg(utlatande);
        assertNotNull(intyg.getRelation());
        assertEquals(1, intyg.getRelation().size());
        assertEquals(relationKod.value(), intyg.getRelation().get(0).getTyp().getCode());
        assertNotNull(intyg.getRelation().get(0).getTyp().getCodeSystem());
        assertEquals(relationIntygsId, intyg.getRelation().get(0).getIntygsId().getExtension());
        assertNotNull(intyg.getRelation().get(0).getIntygsId().getRoot());
    }

    @Test
    public void getMeddelandeReferensOfTypeTest() {
        final RelationKod type = RelationKod.KOMPLT;
        final String meddelandeId = "meddelandeId";
        final String referensId = "referensId";
        Utlatande utlatande = buildUtlatande(type, "relationIntygsId");
        utlatande.getGrundData().getRelation().setMeddelandeId(meddelandeId);
        utlatande.getGrundData().getRelation().setReferensId(referensId);
        MeddelandeReferens result = InternalConverterUtil.getMeddelandeReferensOfType(utlatande, type);
        assertNotNull(result);
        assertEquals(meddelandeId, result.getMeddelandeId());
        assertEquals(1, result.getReferensId().size());
        assertEquals(referensId, result.getReferensId().get(0));
    }

    @Test
    public void getMeddelandeReferensOfTypeReferensIdNullTest() {
        final RelationKod type = RelationKod.KOMPLT;
        final String meddelandeId = "meddelandeId";
        Utlatande utlatande = buildUtlatande(type, "relationIntygsId");
        utlatande.getGrundData().getRelation().setMeddelandeId(meddelandeId);
        MeddelandeReferens result = InternalConverterUtil.getMeddelandeReferensOfType(utlatande, type);
        assertNotNull(result);
        assertEquals(meddelandeId, result.getMeddelandeId());
        assertTrue(result.getReferensId().isEmpty());
    }

    @Test
    public void getMeddelandeReferensOfTypeNoRelationTest() {
        final RelationKod type = RelationKod.KOMPLT;
        Utlatande utlatande = buildUtlatande(null, null);
        MeddelandeReferens result = InternalConverterUtil.getMeddelandeReferensOfType(utlatande, type);
        assertNull(result);
    }

    @Test
    public void getMeddelandeReferensOfTypeWrongTypeTest() {
        final String meddelandeId = "meddelandeId";
        final String referensId = "referensId";
        Utlatande utlatande = buildUtlatande(RelationKod.FRLANG, "relationIntygsId");
        utlatande.getGrundData().getRelation().setMeddelandeId(meddelandeId);
        utlatande.getGrundData().getRelation().setReferensId(referensId);
        MeddelandeReferens result = InternalConverterUtil.getMeddelandeReferensOfType(utlatande, RelationKod.KOMPLT);
        assertNull(result);
    }

    @Test
    public void addIfNotBlankTest() {
        List<Svar> svars = new ArrayList<>();
        String svarsId = "1";
        String delsvarsId = "1.2";
        String content = "content";
        InternalConverterUtil.addIfNotBlank(svars, svarsId, delsvarsId, content);

        assertEquals(1, svars.size());
        assertEquals(svarsId, svars.get(0).getId());
        assertEquals(1, svars.get(0).getDelsvar().size());
        assertEquals(delsvarsId, svars.get(0).getDelsvar().get(0).getId());
        assertEquals(content, TransportConverterUtil.getStringContent(svars.get(0).getDelsvar().get(0)));
    }

    @Test
    public void addIfNotBlankContentNullTest() {
        List<Svar> svars = new ArrayList<>();
        String content = null;
        InternalConverterUtil.addIfNotBlank(svars, "svarsId", "delsvarsId", content);

        assertTrue(svars.isEmpty());
    }

    @Test
    public void addIfNotBlankContentEmptyStringTest() {
        List<Svar> svars = new ArrayList<>();
        String content = "";
        InternalConverterUtil.addIfNotBlank(svars, "svarsId", "delsvarsId", content);

        assertTrue(svars.isEmpty());
    }

    @Test
    public void aDatePeriodTest() {
        LocalDate from = LocalDate.now();
        LocalDate tom = LocalDate.now().plusDays(4);
        JAXBElement<DatePeriodType> result = InternalConverterUtil.aDatePeriod(from, tom);

        assertNotNull(result);
        assertEquals(from, result.getValue().getStart());
        assertEquals(tom, result.getValue().getEnd());
    }

    @Test
    public void aCVTest() {
        String codeSystem = "codesystem";
        String code = "code";
        String displayName = "displayname";
        JAXBElement<CVType> result = InternalConverterUtil.aCV(codeSystem, code, displayName);

        assertNotNull(result);
        assertEquals(code, result.getValue().getCode());
        assertEquals(codeSystem, result.getValue().getCodeSystem());
        assertEquals(displayName, result.getValue().getDisplayName());
    }

    @Test
    public void testSpecialistkompetensAppendsDisplayName() {
        SpecialistkompetensKod specialistkompetens = SpecialistkompetensKod.ALLERGI;
        Utlatande utlatande = buildUtlatande(null, null);
        utlatande.getGrundData().getSkapadAv().getSpecialiteter().clear();
        utlatande.getGrundData().getSkapadAv().getSpecialiteter().add(specialistkompetens.getCode());
        HosPersonal skapadAv = InternalConverterUtil.getIntyg(utlatande).getSkapadAv();
        assertEquals(1, skapadAv.getSpecialistkompetens().size());
        assertEquals(specialistkompetens.getCode(), skapadAv.getSpecialistkompetens().get(0).getCode());
        assertEquals(specialistkompetens.getDescription(), skapadAv.getSpecialistkompetens().get(0).getDisplayName());
    }

    @Test
    public void testSpecialistkompetensDoNotAppendDisplayNameIfNoSpecialistkompetensKodMatch() {
        String specialistkompetens = "kod";
        Utlatande utlatande = buildUtlatande(null, null);
        utlatande.getGrundData().getSkapadAv().getSpecialiteter().clear();
        utlatande.getGrundData().getSkapadAv().getSpecialiteter().add(specialistkompetens);
        HosPersonal skapadAv = InternalConverterUtil.getIntyg(utlatande).getSkapadAv();
        assertEquals(1, skapadAv.getSpecialistkompetens().size());
        assertEquals(specialistkompetens, skapadAv.getSpecialistkompetens().get(0).getCode());
        assertNull(skapadAv.getSpecialistkompetens().get(0).getDisplayName());
    }

    @Test
    public void testBefattningAppendsDisplayName() {
        BefattningKod befattningskod = BefattningKod.LAKARE_LEG_ST;
        Utlatande utlatande = buildUtlatande(null, null);
        utlatande.getGrundData().getSkapadAv().getBefattningar().clear();
        utlatande.getGrundData().getSkapadAv().getBefattningar().add(befattningskod.getCode());
        HosPersonal skapadAv = InternalConverterUtil.getIntyg(utlatande).getSkapadAv();
        assertEquals(1, skapadAv.getBefattning().size());
        assertEquals(befattningskod.getCode(), skapadAv.getBefattning().get(0).getCode());
        assertEquals(befattningskod.getDescription(), skapadAv.getBefattning().get(0).getDisplayName());
    }

    @Test
    public void testBefattningDoNotAppendDisplayNameIfNoSpecialistkompetensKodMatch() {
        String befattning = "kod";
        Utlatande utlatande = buildUtlatande(null, null);
        utlatande.getGrundData().getSkapadAv().getBefattningar().clear();
        utlatande.getGrundData().getSkapadAv().getBefattningar().add(befattning);
        HosPersonal skapadAv = InternalConverterUtil.getIntyg(utlatande).getSkapadAv();
        assertEquals(1, skapadAv.getBefattning().size());
        assertEquals(befattning, skapadAv.getBefattning().get(0).getCode());
        assertNull(skapadAv.getBefattning().get(0).getDisplayName());
    }

    private Utlatande buildUtlatande(RelationKod relationKod, String relationIntygsId) {
        return buildUtlatande("intygsId", "enhetsId", "enhetsnamn", "patientPersonId",
                "skapadAvFullstandigtNamn", "skapadAvPersonId", LocalDateTime.now(), "arbetsplatsKod", "postadress", "postNummer", "postOrt",
                "epost", "telefonNummer", "vardgivarid", "vardgivarNamn", "forskrivarKod", "fornamn", "efternamn", "mellannamn", "patientPostadress",
                "patientPostnummer", "patientPostort", relationKod, relationIntygsId);
    }

    private Utlatande buildUtlatande(String intygsId, String enhetsId, String enhetsnamn,
            String patientPersonId, String skapadAvFullstandigtNamn, String skapadAvPersonId, LocalDateTime signeringsdatum, String arbetsplatsKod,
            String postadress, String postNummer, String postOrt, String epost, String telefonNummer, String vardgivarid, String vardgivarNamn,
            String forskrivarKod, String fornamn, String efternamn, String mellannamn, String patientPostadress, String patientPostnummer,
            String patientPostort, RelationKod relationKod, String relationIntygsId) {
        Utlatande utlatande = mock(Utlatande.class);
        when(utlatande.getId()).thenReturn(intygsId);
        GrundData grundData = new GrundData();
        HoSPersonal skapadAv = new HoSPersonal();
        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setEnhetsid(enhetsId);
        vardenhet.setEnhetsnamn(enhetsnamn);
        vardenhet.setArbetsplatsKod(arbetsplatsKod);
        vardenhet.setPostadress(postadress);
        vardenhet.setPostnummer(postNummer);
        vardenhet.setPostort(postOrt);
        vardenhet.setEpost(epost);
        vardenhet.setTelefonnummer(telefonNummer);
        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setVardgivarid(vardgivarid);
        vardgivare.setVardgivarnamn(vardgivarNamn);
        vardenhet.setVardgivare(vardgivare);
        skapadAv.setVardenhet(vardenhet);
        skapadAv.setFullstandigtNamn(skapadAvFullstandigtNamn);
        skapadAv.setPersonId(skapadAvPersonId);
        skapadAv.setForskrivarKod(forskrivarKod);
        grundData.setSkapadAv(skapadAv);
        Patient patient = new Patient();
        Personnummer personId = new Personnummer(patientPersonId);
        patient.setPersonId(personId);
        patient.setFornamn(fornamn);
        patient.setEfternamn(efternamn);
        patient.setMellannamn(mellannamn);
        patient.setPostadress(patientPostadress);
        patient.setPostnummer(patientPostnummer);
        patient.setPostort(patientPostort);
        grundData.setPatient(patient);
        grundData.setSigneringsdatum(signeringsdatum);
        if (relationKod != null) {
            Relation relation = new Relation();
            relation.setRelationIntygsId(relationIntygsId);
            relation.setRelationKod(relationKod);
            grundData.setRelation(relation);
        }
        when(utlatande.getGrundData()).thenReturn(grundData);
        return utlatande;
    }
}
