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
package se.inera.intyg.common.ts_bas.integration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import se.inera.intyg.common.support.integration.module.exception.InvalidCertificateException;
import se.inera.intyg.common.support.integration.module.exception.MissingConsentException;
import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.modules.support.api.CertificateHolder;
import se.inera.intyg.common.support.modules.support.api.CertificateStateHolder;
import se.inera.intyg.common.support.modules.support.api.ModuleContainerApi;
import se.inera.intyg.common.ts_bas.utils.ScenarioFinder;
import se.inera.intyg.schemas.contract.Personnummer;
import se.inera.intygstjanster.ts.services.GetTSBasResponder.v1.GetTSBasResponseType;
import se.inera.intygstjanster.ts.services.GetTSBasResponder.v1.GetTSBasType;
import se.inera.intygstjanster.ts.services.RegisterTSBasResponder.v1.RegisterTSBasType;
import se.inera.intygstjanster.ts.services.types.v1.II;
import se.inera.intygstjanster.ts.services.v1.ErrorIdType;
import se.inera.intygstjanster.ts.services.v1.ResultCodeType;
import se.inera.intygstjanster.ts.services.v1.Status;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBException;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GetTSBasResponderImplTest {

    private final String LOGICAL_ADDRESS = "logicalAddress";
    private final String PNR_FJORTON = "19141214-1414";
    private final String PNR_TOLVAN = "19121212-1212";


    @Mock
    private ModuleContainerApi moduleContainer;

    @InjectMocks
    private GetTSBasResponderImpl responder;

    @Test
    public void testGetTSBas() throws Exception {
        final String intygId = "intygId";
        final String target = "target";
        final CertificateState state = CertificateState.RECEIVED;
        final LocalDateTime timestamp = LocalDateTime.now();
        final String additionalInfo = "additionalInfo";

        RegisterTSBasType originalCertificate = ScenarioFinder.getTransportScenario("valid-minimal").asTransportModel();
        Personnummer pnr = createPnr(PNR_TOLVAN);

        CertificateHolder certificate = new CertificateHolder();
        certificate.setCivicRegistrationNumber(pnr);
        certificate.setCertificateStates(Arrays.asList(new CertificateStateHolder(target, state, timestamp)));
        certificate.setOriginalCertificate(xmlToString(originalCertificate));
        certificate.setAdditionalInfo(additionalInfo);
        certificate.setDeleted(false);
        when(moduleContainer.getCertificate(intygId, pnr, false)).thenReturn(certificate);

        GetTSBasType request = new GetTSBasType();
        request.setIntygsId(intygId);
        request.setPersonId(new II());
        request.getPersonId().setExtension(PNR_TOLVAN);
        GetTSBasResponseType res = responder.getTSBas(LOGICAL_ADDRESS, request);

        assertNotNull(res);
        assertEquals(ResultCodeType.OK, res.getResultat().getResultCode());
        assertEquals(originalCertificate.getIntyg().getIntygsId(), res.getIntyg().getIntygsId());
        assertNotNull(res.getMeta());
        assertEquals(additionalInfo, res.getMeta().getAdditionalInfo());
        assertEquals("true", res.getMeta().getAvailable());
        assertEquals(1, res.getMeta().getStatus().size());
        assertEquals(target, res.getMeta().getStatus().get(0).getTarget());
        assertEquals(timestamp.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), res.getMeta().getStatus().get(0).getTimestamp());
        assertEquals(Status.RECEIVED, res.getMeta().getStatus().get(0).getType());
    }

    @Test
    public void testGetTSBasDeleted() throws Exception {
        final String intygId = "intygId";

        RegisterTSBasType originalCertificate = ScenarioFinder.getTransportScenario("valid-minimal").asTransportModel();
        Personnummer pnr = createPnr(PNR_TOLVAN);

        CertificateHolder certificate = new CertificateHolder();
        certificate.setCivicRegistrationNumber(pnr);
        certificate.setCertificateStates(Arrays.asList(new CertificateStateHolder("target", CertificateState.RECEIVED, LocalDateTime.now())));
        certificate.setOriginalCertificate(xmlToString(originalCertificate));
        certificate.setDeleted(true);
        when(moduleContainer.getCertificate(intygId, pnr, false)).thenReturn(certificate);

        GetTSBasType request = new GetTSBasType();
        request.setIntygsId(intygId);
        request.setPersonId(new II());
        request.getPersonId().setExtension(PNR_TOLVAN);
        GetTSBasResponseType res = responder.getTSBas(LOGICAL_ADDRESS, request);

        assertNotNull(res);
        assertEquals(ResultCodeType.OK, res.getResultat().getResultCode());
        assertEquals(originalCertificate.getIntyg().getIntygsId(), res.getIntyg().getIntygsId());
        assertEquals("false", res.getMeta().getAvailable());
    }

    @Test
    public void testGetTSBasNoPersonId() throws Exception {
        final String intygId = "intygId";

        RegisterTSBasType originalCertificate = ScenarioFinder.getTransportScenario("valid-minimal").asTransportModel();

        CertificateHolder certificate = new CertificateHolder();
        certificate.setCertificateStates(Arrays.asList(new CertificateStateHolder("target", CertificateState.RECEIVED, LocalDateTime.now())));
        certificate.setOriginalCertificate(xmlToString(originalCertificate));
        when(moduleContainer.getCertificate(intygId, null, false)).thenReturn(certificate);

        GetTSBasType request = new GetTSBasType();
        request.setIntygsId(intygId);

        GetTSBasResponseType res = responder.getTSBas(LOGICAL_ADDRESS, request);

        assertNotNull(res);
        assertEquals(ResultCodeType.OK, res.getResultat().getResultCode());
        assertEquals(originalCertificate.getIntyg().getIntygsId(), res.getIntyg().getIntygsId());
    }

    @Test
    public void testGetTSBasNoCertificateId() throws Exception {
        GetTSBasResponseType res = responder.getTSBas(LOGICAL_ADDRESS, new GetTSBasType());

        assertNotNull(res);
        assertEquals(ResultCodeType.ERROR, res.getResultat().getResultCode());
        assertEquals(ErrorIdType.APPLICATION_ERROR, res.getResultat().getErrorId());
        assertEquals("non-existing certificateId", res.getResultat().getResultText());
    }

    @Test
    public void testGetTSBasPersonIdMismatch() throws Exception {
        final String intygId = "intygId";

        CertificateHolder certificate = new CertificateHolder();
        certificate.setCivicRegistrationNumber(createPnr(PNR_FJORTON));
        when(moduleContainer.getCertificate(intygId, createPnr(PNR_TOLVAN), false)).thenReturn(certificate);

        GetTSBasType request = new GetTSBasType();
        request.setIntygsId(intygId);
        request.setPersonId(new II());
        request.getPersonId().setExtension(PNR_TOLVAN);
        GetTSBasResponseType res = responder.getTSBas(LOGICAL_ADDRESS, request);

        assertNotNull(res);
        assertEquals(ResultCodeType.ERROR, res.getResultat().getResultCode());
        assertEquals(ErrorIdType.VALIDATION_ERROR, res.getResultat().getErrorId());
        assertEquals("nationalIdentityNumber mismatch", res.getResultat().getResultText());
    }

    @Test
    public void testGetTSBasDeletedByCareGiver() throws Exception {
        final String intygId = "intygId";

        CertificateHolder certificate = new CertificateHolder();
        certificate.setDeletedByCareGiver(true);
        when(moduleContainer.getCertificate(intygId, null, false)).thenReturn(certificate);

        GetTSBasType request = new GetTSBasType();
        request.setIntygsId(intygId);

        GetTSBasResponseType res = responder.getTSBas(LOGICAL_ADDRESS, request);

        assertNotNull(res);
        assertEquals(ResultCodeType.ERROR, res.getResultat().getResultCode());
        assertEquals(ErrorIdType.APPLICATION_ERROR, res.getResultat().getErrorId());
        assertEquals("Certificate 'intygId' has been deleted by care giver", res.getResultat().getResultText());
    }

    @Test
    public void testGetTSBasRevoked() throws Exception {
        final String intygId = "intygId";
        final String target = "target";
        final CertificateState state = CertificateState.RECEIVED;
        final LocalDateTime timestamp = LocalDateTime.now();
        final String additionalInfo = "additionalInfo";

        RegisterTSBasType originalCertificate = ScenarioFinder.getTransportScenario("valid-minimal").asTransportModel();
        Personnummer pnr = createPnr(PNR_TOLVAN);

        CertificateHolder certificate = new CertificateHolder();
        certificate.setCivicRegistrationNumber(pnr);
        certificate.setCertificateStates(Arrays.asList(new CertificateStateHolder(target, state, timestamp)));
        certificate.setOriginalCertificate(xmlToString(originalCertificate));
        certificate.setAdditionalInfo(additionalInfo);
        certificate.setDeleted(false);
        certificate.setRevoked(true);

        when(moduleContainer.getCertificate(intygId, pnr, false)).thenReturn(certificate);

        GetTSBasType request = new GetTSBasType();
        request.setIntygsId(intygId);
        request.setPersonId(new II());
        request.getPersonId().setExtension(PNR_TOLVAN);
        GetTSBasResponseType res = responder.getTSBas(LOGICAL_ADDRESS, request);

        assertNotNull(res);
        assertEquals(ResultCodeType.ERROR, res.getResultat().getResultCode());
        assertEquals(ErrorIdType.REVOKED, res.getResultat().getErrorId());
        assertEquals("Certificate 'intygId' has been revoked", res.getResultat().getResultText());
        assertEquals(originalCertificate.getIntyg().getIntygsId(), res.getIntyg().getIntygsId());
        assertNotNull(res.getMeta());
        assertEquals(additionalInfo, res.getMeta().getAdditionalInfo());
        assertEquals("true", res.getMeta().getAvailable());
        assertEquals(1, res.getMeta().getStatus().size());
        assertEquals(target, res.getMeta().getStatus().get(0).getTarget());
        assertEquals(timestamp.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), res.getMeta().getStatus().get(0).getTimestamp());
        assertEquals(Status.RECEIVED, res.getMeta().getStatus().get(0).getType());
    }

    @Test
    public void testGetTSBasInvalidCertificate() throws Exception {
        final String intygId = "intygId";
        when(moduleContainer.getCertificate(intygId, null, false)).thenThrow(new InvalidCertificateException(intygId, null));

        GetTSBasType request = new GetTSBasType();
        request.setIntygsId(intygId);

        GetTSBasResponseType res = responder.getTSBas(LOGICAL_ADDRESS, request);

        assertNotNull(res);
        assertEquals(ResultCodeType.ERROR, res.getResultat().getResultCode());
        assertEquals(ErrorIdType.VALIDATION_ERROR, res.getResultat().getErrorId());
        assertEquals("Unknown certificate ID: intygId", res.getResultat().getResultText());
    }

    @Test
    public void testGetTSBasMissingConsent() throws Exception {
        final String intygId = "intygId";

        Personnummer pnr = createPnr(PNR_TOLVAN);
        when(moduleContainer.getCertificate(intygId, pnr, false)).thenThrow(new MissingConsentException(pnr));

        GetTSBasType request = new GetTSBasType();
        request.setIntygsId(intygId);
        request.setPersonId(new II());
        request.getPersonId().setExtension(PNR_TOLVAN);
        GetTSBasResponseType res = responder.getTSBas(LOGICAL_ADDRESS, request);

        assertNotNull(res);
        assertEquals(ResultCodeType.ERROR, res.getResultat().getResultCode());
        assertEquals(ErrorIdType.VALIDATION_ERROR, res.getResultat().getErrorId());
        assertEquals("Consent required from user 9a8b138a666f84da32e9383b49a15f46f6e08d2c492352aa0dfcc3f993773b0d", res.getResultat().getResultText());
    }

    private Personnummer createPnr(String pnr) {
        return Personnummer.createPersonnummer(pnr).get();
    }

    private String xmlToString(RegisterTSBasType registerTsBas) throws JAXBException {
        StringWriter stringWriter = new StringWriter();
        JAXB.marshal(registerTsBas, stringWriter);
        return stringWriter.toString();
    }
}
