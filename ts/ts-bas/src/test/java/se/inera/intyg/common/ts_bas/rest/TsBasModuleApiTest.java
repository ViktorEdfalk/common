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
package se.inera.intyg.common.ts_bas.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import se.inera.intyg.common.services.texts.IntygTextsService;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.model.common.internal.Vardgivare;
import se.inera.intyg.common.support.model.converter.util.XslTransformer;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.api.ModuleApi;
import se.inera.intyg.common.support.modules.support.api.dto.CertificateResponse;
import se.inera.intyg.common.support.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.intyg.common.support.modules.support.api.dto.CreateNewDraftHolder;
import se.inera.intyg.common.support.modules.support.api.exception.ExternalServiceCallException;
import se.inera.intyg.common.support.modules.support.api.exception.ExternalServiceCallException.ErrorIdEnum;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.ts_bas.integration.RegisterTSBasResponderImpl;
import se.inera.intyg.common.ts_bas.model.converter.UtlatandeToIntyg;
import se.inera.intyg.common.ts_bas.model.converter.WebcertModelFactoryImpl;
import se.inera.intyg.common.ts_bas.model.internal.TsBasUtlatande;
import se.inera.intyg.common.ts_bas.pdf.PdfGeneratorImpl;
import se.inera.intyg.common.ts_bas.utils.Scenario;
import se.inera.intyg.common.ts_bas.utils.ScenarioFinder;
import se.inera.intyg.common.ts_bas.utils.ScenarioNotFoundException;
import se.inera.intyg.common.ts_parent.integration.ResultTypeUtil;
import se.inera.intyg.common.ts_parent.integration.SendTSClient;
import se.inera.intyg.common.util.integration.json.CustomObjectMapper;
import se.inera.intyg.schemas.contract.Personnummer;
import se.inera.intygstjanster.ts.services.GetTSBasResponder.v1.GetTSBasResponderInterface;
import se.inera.intygstjanster.ts.services.GetTSBasResponder.v1.GetTSBasResponseType;
import se.inera.intygstjanster.ts.services.GetTSBasResponder.v1.GetTSBasType;
import se.inera.intygstjanster.ts.services.RegisterTSBasResponder.v1.RegisterTSBasResponderInterface;
import se.inera.intygstjanster.ts.services.RegisterTSBasResponder.v1.RegisterTSBasResponseType;
import se.inera.intygstjanster.ts.services.RegisterTSBasResponder.v1.RegisterTSBasType;
import se.inera.intygstjanster.ts.services.v1.ErrorIdType;
import se.inera.intygstjanster.ts.services.v1.IntygMeta;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.IntygId;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar.Delsvar;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBException;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.aCV;

/**
 * Sets up an actual HTTP server and client to test the {@link ModuleApi} service. This is the place to verify that
 * response headers and response statuses etc are correct.
 */
@RunWith(MockitoJUnitRunner.class)
public class TsBasModuleApiTest {

    @InjectMocks
    private TsBasModuleApi moduleApi;

    @Spy
    private ObjectMapper objectMapper = new CustomObjectMapper();

    @Mock
    private RegisterTSBasResponderInterface registerTSBasResponderInterface;

    @Mock
    private GetTSBasResponderInterface getTSBasResponderInterface;

    @Spy
    private WebcertModelFactoryImpl webcertModelFactory = new WebcertModelFactoryImpl();

    @Mock
    private PdfGeneratorImpl pdfGenerator;

    @Mock
    private IntygTextsService intygTexts;

    @Mock
    private XslTransformer xslTransformer;

    @Mock
    private SendTSClient sendTsBasClient;

    @Before
    public void setup() throws Exception {
        // use reflection to set IntygTextsService mock in webcertModelFactory
        Field field = WebcertModelFactoryImpl.class.getDeclaredField("intygTexts");
        field.setAccessible(true);
        field.set(webcertModelFactory, intygTexts);
    }

    @Test
    public void testPdf() throws Exception {
        when(pdfGenerator.generatePDF(any(TsBasUtlatande.class), any(List.class), any(ApplicationOrigin.class), eq(false)))
                .thenReturn(new byte[] {});
        when(pdfGenerator.generatePdfFilename(any(TsBasUtlatande.class))).thenReturn("filename");
        for (Scenario scenario : ScenarioFinder.getInternalScenarios("valid-*")) {
            moduleApi
                    .pdf(objectMapper.writeValueAsString(scenario.asInternalModel()), Collections.emptyList(), ApplicationOrigin.MINA_INTYG,
                            false);
        }
    }

    @Test
    public void copyContainsOriginalData() throws Exception {
        Scenario scenario = ScenarioFinder.getInternalScenario("valid-maximal");
        String holder = moduleApi.createNewInternalFromTemplate(createNewDraftCopyHolder(), scenario.asInternalModel());

        assertNotNull(holder);
        TsBasUtlatande utlatande = objectMapper.readValue(holder, TsBasUtlatande.class);
        assertEquals(true, utlatande.getSyn().getSynfaltsdefekter());
    }

    @Test
    public void createNewInternal() throws ModuleException {
        CreateNewDraftHolder holder = createNewDraftHolder();

        String response = moduleApi.createNewInternal(holder);

        assertNotNull(response);
    }

    @Test
    public void testRegisterCertificate() throws JsonProcessingException, ScenarioNotFoundException {
        RegisterTSBasResponseType registerResponse = new RegisterTSBasResponseType();
        registerResponse.setResultat(ResultTypeUtil.okResult());
        Mockito.when(registerTSBasResponderInterface.registerTSBas(eq("OK"), Mockito.any(RegisterTSBasType.class)))
                .thenReturn(registerResponse);

        String logicalAddress = "OK";
        List<String> failResults = new ArrayList<>();
        for (Scenario scenario : ScenarioFinder.getInternalScenarios("valid-*")) {
            String internalModel = objectMapper.writeValueAsString(scenario.asInternalModel());
            try {
                moduleApi.registerCertificate(internalModel, logicalAddress);
            } catch (ModuleException me) {
                failResults.add(me.getMessage());
            }
        }
        assertTrue(failResults.isEmpty());
    }

    @Test(expected = ExternalServiceCallException.class)
    public void testRegisterCertificateError() throws Exception {
        RegisterTSBasResponseType registerResponse = new RegisterTSBasResponseType();
        registerResponse.setResultat(ResultTypeUtil.errorResult(ErrorIdType.APPLICATION_ERROR, "error"));
        Mockito.when(registerTSBasResponderInterface.registerTSBas(Mockito.anyString(), Mockito.any(RegisterTSBasType.class)))
                .thenReturn(registerResponse);

        String internalModel = objectMapper.writeValueAsString(ScenarioFinder.getInternalScenario("valid-minimal").asInternalModel());
        moduleApi.registerCertificate(internalModel, "logicalAddress");
    }

    @Test
    public void testRegisterCertificateAlreadyExists() throws Exception {
        final String logicalAddress = "logicalAddress";
        final String internalModel = objectMapper
                .writeValueAsString(ScenarioFinder.getInternalScenario("valid-minimal").asInternalModel());

        RegisterTSBasResponseType registerResponse = new RegisterTSBasResponseType();
        registerResponse.setResultat(ResultTypeUtil.infoResult(RegisterTSBasResponderImpl.CERTIFICATE_ALREADY_EXISTS));
        when(registerTSBasResponderInterface.registerTSBas(eq(logicalAddress), Mockito.any(RegisterTSBasType.class)))
                .thenReturn(registerResponse);

        try {
            moduleApi.registerCertificate(internalModel, logicalAddress);
            fail();
        } catch (ExternalServiceCallException e) {
            assertEquals(ErrorIdEnum.VALIDATION_ERROR, e.getErroIdEnum());
            assertEquals(RegisterTSBasResponderImpl.CERTIFICATE_ALREADY_EXISTS, e.getMessage());
        }
    }

    @Test
    public void testRegisterCertificateGenericInfoResult() throws Exception {
        final String logicalAddress = "logicalAddress";
        final String internalModel = objectMapper
                .writeValueAsString(ScenarioFinder.getInternalScenario("valid-minimal").asInternalModel());

        RegisterTSBasResponseType registerResponse = new RegisterTSBasResponseType();
        registerResponse.setResultat(ResultTypeUtil.infoResult("INFO"));
        when(registerTSBasResponderInterface.registerTSBas(eq(logicalAddress), Mockito.any(RegisterTSBasType.class)))
                .thenReturn(registerResponse);

        try {
            moduleApi.registerCertificate(internalModel, logicalAddress);
            fail();
        } catch (ExternalServiceCallException e) {
            assertEquals(ErrorIdEnum.APPLICATION_ERROR, e.getErroIdEnum());
            assertEquals("INFO", e.getMessage());
        }
    }

    @Test
    public void testRegisterCertificateConvert() throws JsonProcessingException, ScenarioNotFoundException {
        String logicalAddress = "FAIL";
        String failResult = "";
        Scenario scenario = ScenarioFinder.getInternalScenario("invalid-missing-identitet");
        String internalModel = objectMapper.writeValueAsString(scenario.asInternalModel());
        try {
            moduleApi.registerCertificate(internalModel, logicalAddress);
        } catch (ModuleException me) {
            failResult = me.getMessage();
        }
        assertFalse(failResult.isEmpty());
    }

    @Test
    public void testRegisterCertificateFailed() throws JsonProcessingException, ScenarioNotFoundException {
        RegisterTSBasResponseType registerResponse = new RegisterTSBasResponseType();
        registerResponse.setResultat(ResultTypeUtil.errorResult(ErrorIdType.APPLICATION_ERROR, "failed"));
        when(registerTSBasResponderInterface.registerTSBas(eq("FAIL"), any(RegisterTSBasType.class)))
                .thenReturn(registerResponse);

        String logicalAddress = "FAIL";
        String failResult = "";
        Scenario scenario = ScenarioFinder.getInternalScenario("valid-maximal");
        String internalModel = objectMapper.writeValueAsString(scenario.asInternalModel());
        try {
            moduleApi.registerCertificate(internalModel, logicalAddress);
        } catch (ModuleException me) {
            failResult = me.getMessage();
        }
        assertFalse(failResult.isEmpty());
    }

    @Test
    public void testSendCertificateToRecipient() throws Exception {
        final String xmlBody = "xmlBody";
        final String logicalAddress = "logicalAddress";
        final String recipientId = "recipient";
        final String transformedXml = "transformedXml";
        when(xslTransformer.transform(xmlBody)).thenReturn(transformedXml);
        SOAPMessage response = mock(SOAPMessage.class);
        when(response.getSOAPPart()).thenReturn(mock(SOAPPart.class));
        when(response.getSOAPPart().getEnvelope()).thenReturn(mock(SOAPEnvelope.class));
        when(response.getSOAPPart().getEnvelope().getBody()).thenReturn(mock(SOAPBody.class));
        when(response.getSOAPPart().getEnvelope().getBody().hasFault()).thenReturn(false);
        when(sendTsBasClient.registerCertificate(transformedXml, logicalAddress)).thenReturn(response);

        moduleApi.sendCertificateToRecipient(xmlBody, logicalAddress, recipientId);

        verify(xslTransformer).transform(xmlBody);
        verify(sendTsBasClient).registerCertificate(transformedXml, logicalAddress);
    }

    @Test(expected = ModuleException.class)
    public void testSendCertificateToRecipientFault() throws Exception {
        final String xmlBody = "xmlBody";
        final String logicalAddress = "logicalAddress";
        final String recipientId = "recipient";
        final String transformedXml = "transformedXml";
        when(xslTransformer.transform(xmlBody)).thenReturn(transformedXml);
        SOAPMessage response = mock(SOAPMessage.class);
        when(response.getSOAPPart()).thenReturn(mock(SOAPPart.class));
        when(response.getSOAPPart().getEnvelope()).thenReturn(mock(SOAPEnvelope.class));
        when(response.getSOAPPart().getEnvelope().getBody()).thenReturn(mock(SOAPBody.class));
        when(response.getSOAPPart().getEnvelope().getBody().hasFault()).thenReturn(true);
        when(sendTsBasClient.registerCertificate(transformedXml, logicalAddress)).thenReturn(response);

        moduleApi.sendCertificateToRecipient(xmlBody, logicalAddress, recipientId);
    }

    @Test
    public void testGetCertificate() throws ModuleException, ScenarioNotFoundException {
        GetTSBasResponseType result = new GetTSBasResponseType();
        result.setIntyg(ScenarioFinder.getTransportScenario("valid-maximal").asTransportModel().getIntyg());
        result.setMeta(createMeta());
        result.setResultat(ResultTypeUtil.okResult());
        Mockito.when(getTSBasResponderInterface.getTSBas(eq("TS"), Mockito.any(GetTSBasType.class)))
                .thenReturn(result);

        CertificateResponse internal = moduleApi.getCertificate("cert-id", "TS", "INVANA");
        assertNotNull(internal);
    }

    @Test
    public void testGetCertificateRevokedReturnsCertificate() throws Exception {
        GetTSBasResponseType result = new GetTSBasResponseType();
        result.setIntyg(ScenarioFinder.getTransportScenario("valid-maximal").asTransportModel().getIntyg());
        result.setMeta(createMeta());
        result.setResultat(ResultTypeUtil.errorResult(ErrorIdType.REVOKED, "error"));
        Mockito.when(getTSBasResponderInterface.getTSBas(eq("TS"), Mockito.any(GetTSBasType.class)))
                .thenReturn(result);

        CertificateResponse internal = moduleApi.getCertificate("cert-id", "TS", "INVANA");
        assertNotNull(internal);
    }

    @Test(expected = ModuleException.class)
    public void testGetCertificateRevokedValidationError() throws Exception {
        GetTSBasResponseType result = new GetTSBasResponseType();
        result.setResultat(ResultTypeUtil.errorResult(ErrorIdType.VALIDATION_ERROR, "error"));
        Mockito.when(getTSBasResponderInterface.getTSBas(eq("TS"), Mockito.any(GetTSBasType.class)))
                .thenReturn(result);

        moduleApi.getCertificate("cert-id", "TS", "INVANA");
    }

    @Test(expected = ModuleException.class)
    public void testGetCertificateRevokedApplicationError() throws Exception {
        GetTSBasResponseType result = new GetTSBasResponseType();
        result.setResultat(ResultTypeUtil.errorResult(ErrorIdType.APPLICATION_ERROR, "error"));
        Mockito.when(getTSBasResponderInterface.getTSBas(eq("TS"), Mockito.any(GetTSBasType.class)))
                .thenReturn(result);

        moduleApi.getCertificate("cert-id", "TS", "INVANA");
    }

    @Test
    public void testGetUtlatandeFromXml() throws Exception {
        String xml = xmlToString(ScenarioFinder.getTransportScenario("valid-minimal").asTransportModel());
        TsBasUtlatande res = moduleApi.getUtlatandeFromXml(xml);

        assertNotNull(res);
    }

    @Test(expected = ModuleException.class)
    public void testGetUtlatandeFromXmlConverterException() throws Exception {
        String xml = xmlToString(new RegisterTSBasType());
        moduleApi.getUtlatandeFromXml(xml);
    }

    @Test
    public void getAdditionalInfoFromUtlatandeTest() throws Exception {
        TsBasUtlatande utlatande = ScenarioFinder.getInternalScenario("valid-maximal").asInternalModel();
        Intyg intyg = UtlatandeToIntyg.convert(utlatande);

        String result = moduleApi.getAdditionalInfo(intyg);
        assertEquals("C1, C1E, C, CE, D1, D1E, D, DE, TAXI, ANNAT", result);
    }

    @Test
    public void getAdditionalInfoOneResultTest() throws ModuleException {
        Intyg intyg = new Intyg();
        intyg.setIntygsId(new IntygId());
        intyg.getIntygsId().setExtension("intygId");
        Svar s = new Svar();
        s.setId("1");
        Delsvar delsvar = new Delsvar();
        delsvar.setId("1.1");
        delsvar.getContent().add(aCV(null, "IAV6", null));
        s.getDelsvar().add(delsvar);
        intyg.getSvar().add(s);

        String result = moduleApi.getAdditionalInfo(intyg);
        assertEquals("D1E", result);
    }

    @Test
    public void getAdditionalInfoMultipleResultsTest() throws ModuleException {
        Intyg intyg = new Intyg();
        intyg.setIntygsId(new IntygId());
        intyg.getIntygsId().setExtension("intygId");
        Svar s = new Svar();
        s.setId("1");
        Delsvar delsvar = new Delsvar();
        delsvar.setId("1.1");
        delsvar.getContent().add(aCV(null, "IAV1", null));
        s.getDelsvar().add(delsvar);
        Svar s2 = new Svar();
        s2.setId("1");
        Delsvar delsvar2 = new Delsvar();
        delsvar2.setId("1.1");
        delsvar2.getContent().add(aCV(null, "IAV3", null));
        s2.getDelsvar().add(delsvar2);
        Svar s3 = new Svar();
        s3.setId("1");
        Delsvar delsvar3 = new Delsvar();
        delsvar3.setId("1.1");
        delsvar3.getContent().add(aCV(null, "IAV9", null));
        s3.getDelsvar().add(delsvar3);
        intyg.getSvar().add(s);
        intyg.getSvar().add(s2);
        intyg.getSvar().add(s3);

        String result = moduleApi.getAdditionalInfo(intyg);
        assertEquals("C1, C, TAXI", result);
    }

    @Test
    public void getAdditionalInfoSvarNotFoundTest() throws ModuleException {
        Intyg intyg = new Intyg();
        intyg.setIntygsId(new IntygId());
        intyg.getIntygsId().setExtension("intygId");
        Svar s = new Svar();
        s.setId("2"); // wrong svarId
        Delsvar delsvar = new Delsvar();
        delsvar.setId("1.1");
        delsvar.getContent().add(aCV(null, "IAV6", null));
        s.getDelsvar().add(delsvar);
        intyg.getSvar().add(s);

        String result = moduleApi.getAdditionalInfo(intyg);
        assertNull(result);
    }

    @Test
    public void getAdditionalInfoDelsvarNotFoundTest() throws ModuleException {
        Intyg intyg = new Intyg();
        intyg.setIntygsId(new IntygId());
        intyg.getIntygsId().setExtension("intygId");
        Svar s = new Svar();
        s.setId("1");
        Delsvar delsvar = new Delsvar();
        delsvar.setId("1.3"); // wrong delsvarId
        delsvar.getContent().add(aCV(null, "IAV6", null));
        s.getDelsvar().add(delsvar);
        intyg.getSvar().add(s);

        String result = moduleApi.getAdditionalInfo(intyg);
        assertNull(result);
    }

    private IntygMeta createMeta() throws ScenarioNotFoundException {
        IntygMeta meta = new IntygMeta();
        meta.setAdditionalInfo("C");
        meta.setAvailable("true");
        return meta;
    }

    private CreateNewDraftHolder createNewDraftHolder() {
        HoSPersonal hosPersonal = createHosPersonal();
        Patient patient = new Patient();
        patient.setFornamn("Kalle");
        patient.setEfternamn("Kula");
        patient.setPersonId(Personnummer.createPersonnummer("19121212-1212").get());
        return new CreateNewDraftHolder("Id1", hosPersonal, patient);
    }

    private CreateDraftCopyHolder createNewDraftCopyHolder() {
        return new CreateDraftCopyHolder("Id1", createHosPersonal());
    }

    private HoSPersonal createHosPersonal() {
        HoSPersonal hosPerson = new HoSPersonal();
        hosPerson.setPersonId("hsaId1");
        hosPerson.setFullstandigtNamn("Doktor A");
        hosPerson.setVardenhet(createVardenhet());
        return hosPerson;
    }

    private Vardenhet createVardenhet() {
        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setEnhetsid("hsaId");
        vardenhet.setEnhetsnamn("ve1");
        vardenhet.setVardgivare(new Vardgivare());
        vardenhet.getVardgivare().setVardgivarid("vg1");
        vardenhet.getVardgivare().setVardgivarnamn("vg1");
        return vardenhet;
    }

    private String xmlToString(RegisterTSBasType registerTsBas) throws JAXBException {
        StringWriter stringWriter = new StringWriter();
        JAXB.marshal(registerTsBas, stringWriter);
        return stringWriter.toString();
    }
}
