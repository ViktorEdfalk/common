/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.afmu.validator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import se.inera.intyg.common.afmu.model.internal.AfmuUtlatande;
import se.inera.intyg.common.fkparent.model.validator.ValidatorUtilFK;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.model.common.internal.Vardgivare;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.schemas.contract.Personnummer;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class InternalDraftValidatorTest {

    @InjectMocks
    InternalDraftValidatorImpl validator;

    @InjectMocks
    ValidatorUtilFK validatorUtil;

    AfmuUtlatande.Builder builderTemplate;

    @Mock
    WebcertModuleService moduleService;

    @Before
    public void setUp() throws Exception {
        builderTemplate = AfmuUtlatande.builder()
                .setId("intygsId")
                .setGrundData(buildGrundData(LocalDateTime.now()))
                .setFunktionsnedsattning("funktionsnedsattning")
                .setAktivitetsbegransning("aktivitetsbegransning")
                .setTextVersion("");

        // use reflection to set InternalValidatorUtil in InternalDraftValidator
        Field field = InternalDraftValidatorImpl.class.getDeclaredField("validatorUtilFK");
        field.setAccessible(true);
        field.set(validator, validatorUtil);
    }

    @Test
    public void validateDraft() throws Exception {
        AfmuUtlatande utlatande = builderTemplate.build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertFalse(res.hasErrorMessages());
        assertTrue(res.getValidationErrors().isEmpty());
    }

    @Test
    public void validateFunktionsnedsattningMissing() throws Exception {
        AfmuUtlatande utlatande = builderTemplate
                .setFunktionsnedsattning(null)
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("funktionsnedsattning", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateFunktionsnedsattningBlank() throws Exception {
        AfmuUtlatande utlatande = builderTemplate
                .setFunktionsnedsattning(" ")
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("funktionsnedsattning", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateAktivitetsbegransningMissing() throws Exception {
        AfmuUtlatande utlatande = builderTemplate
                .setAktivitetsbegransning(null)
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("funktionsnedsattning", res.getValidationErrors().get(0).getCategory());
        assertEquals("aktivitetsbegransning", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateAktivitetsbegransningBlank() throws Exception {
        AfmuUtlatande utlatande = builderTemplate
                .setAktivitetsbegransning(" ")
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("funktionsnedsattning", res.getValidationErrors().get(0).getCategory());
        assertEquals("aktivitetsbegransning", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateBlankstegPagaendeBehandling() throws Exception {
        AfmuUtlatande utlatande = builderTemplate
                .setPagaendeBehandling(" ")
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("afmu.validation.blanksteg.otillatet", res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateBlankstegPlaneradBehandling() throws Exception {
        AfmuUtlatande utlatande = builderTemplate
                .setPlaneradBehandling(" ")
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("afmu.validation.blanksteg.otillatet", res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateBlankstegOvrigt() throws Exception {
        AfmuUtlatande utlatande = builderTemplate
                .setOvrigt(" ")
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("afmu.validation.blanksteg.otillatet", res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateEnhetPostadressMissing() throws Exception {
        AfmuUtlatande utlatande = builderTemplate.build();
        utlatande.getGrundData().getSkapadAv().getVardenhet().setPostadress(null);

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("vardenhet", res.getValidationErrors().get(0).getCategory());
        assertEquals("grunddata.skapadAv.vardenhet.postadress", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateEnhetPostadressBlank() throws Exception {
        AfmuUtlatande utlatande = builderTemplate.build();
        utlatande.getGrundData().getSkapadAv().getVardenhet().setPostadress(" ");

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("vardenhet", res.getValidationErrors().get(0).getCategory());
        assertEquals("grunddata.skapadAv.vardenhet.postadress", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateEnhetPostnummerMissing() throws Exception {
        AfmuUtlatande utlatande = builderTemplate.build();
        utlatande.getGrundData().getSkapadAv().getVardenhet().setPostnummer(null);

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("vardenhet", res.getValidationErrors().get(0).getCategory());
        assertEquals("grunddata.skapadAv.vardenhet.postnummer", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.INVALID_FORMAT, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateEnhetPostnummerBlank() throws Exception {
        AfmuUtlatande utlatande = builderTemplate.build();
        utlatande.getGrundData().getSkapadAv().getVardenhet().setPostnummer(" ");

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("vardenhet", res.getValidationErrors().get(0).getCategory());
        assertEquals("grunddata.skapadAv.vardenhet.postnummer", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.INVALID_FORMAT, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateEnhetPostnummerInvalid() throws Exception {
        AfmuUtlatande utlatande = builderTemplate.build();
        utlatande.getGrundData().getSkapadAv().getVardenhet().setPostnummer("invalid");

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("vardenhet", res.getValidationErrors().get(0).getCategory());
        assertEquals("grunddata.skapadAv.vardenhet.postnummer", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.INVALID_FORMAT, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateEnhetPostortMissing() throws Exception {
        AfmuUtlatande utlatande = builderTemplate.build();
        utlatande.getGrundData().getSkapadAv().getVardenhet().setPostort(null);

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("vardenhet", res.getValidationErrors().get(0).getCategory());
        assertEquals("grunddata.skapadAv.vardenhet.postort", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateEnhetPostortBlank() throws Exception {
        AfmuUtlatande utlatande = builderTemplate.build();
        utlatande.getGrundData().getSkapadAv().getVardenhet().setPostort(" ");

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("vardenhet", res.getValidationErrors().get(0).getCategory());
        assertEquals("grunddata.skapadAv.vardenhet.postort", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateEnhetTelefonnummerMissing() throws Exception {
        AfmuUtlatande utlatande = builderTemplate.build();
        utlatande.getGrundData().getSkapadAv().getVardenhet().setTelefonnummer(null);

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("vardenhet", res.getValidationErrors().get(0).getCategory());
        assertEquals("grunddata.skapadAv.vardenhet.telefonnummer", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateEnhetTelefonnummerBlank() throws Exception {
        AfmuUtlatande utlatande = builderTemplate.build();
        utlatande.getGrundData().getSkapadAv().getVardenhet().setTelefonnummer(" ");

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("vardenhet", res.getValidationErrors().get(0).getCategory());
        assertEquals("grunddata.skapadAv.vardenhet.telefonnummer", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    private GrundData buildGrundData(LocalDateTime timeStamp) {
        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setVardgivarid("vardgivareId");
        vardgivare.setVardgivarnamn("vardgivareNamn");

        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setEnhetsid("enhetId");
        vardenhet.setEnhetsnamn("enhetNamn");
        vardenhet.setVardgivare(vardgivare);
        vardenhet.setPostadress("postadress");
        vardenhet.setPostnummer("11111");
        vardenhet.setPostort("postort");
        vardenhet.setTelefonnummer("0112312313");

        HoSPersonal skapadAv = new HoSPersonal();
        skapadAv.setVardenhet(vardenhet);
        skapadAv.setPersonId("HSAID_123");
        skapadAv.setFullstandigtNamn("Torsten Ericsson");

        Patient patient = new Patient();
        patient.setPersonId(Personnummer.createPersonnummer("19121212-1212").get());
        patient.setPostadress("postadress");
        patient.setPostnummer("11111");
        patient.setPostort("postort");

        GrundData grundData = new GrundData();
        grundData.setSkapadAv(skapadAv);
        grundData.setPatient(patient);
        grundData.setSigneringsdatum(timeStamp);

        return grundData;
    }

}
