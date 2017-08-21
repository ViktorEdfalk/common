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
package se.inera.intyg.common.fk7263.model.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.DifferenceConstants;
import org.custommonkey.xmlunit.DifferenceListener;
import org.custommonkey.xmlunit.ElementNameAndTextQualifier;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.LakarutlatandeType;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;
import se.inera.intyg.common.fk7263.model.internal.Fk7263Utlatande;
import se.inera.intyg.common.fk7263.utils.ModelAssert;
import se.inera.intyg.common.fk7263.utils.Scenario;
import se.inera.intyg.common.fk7263.utils.ScenarioFinder;
import se.inera.intyg.common.support.Constants;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.model.common.internal.Vardgivare;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.schemas.contract.Personnummer;
import se.inera.intyg.common.util.integration.json.CustomObjectMapper;

/**
 * @author marced, andreaskaltenbach
 */
public class InternalToTransportConverterTest {

    @Test
    public void testConvertUtlatande() throws Exception {
        for (Scenario scenario : ScenarioFinder.getInternalScenarios("valid-*")) {
            Fk7263Utlatande intUtlatande = scenario.asInternalModel();

            RegisterMedicalCertificateType actual = InternalToTransport.getJaxbObject(intUtlatande);

            RegisterMedicalCertificateType expected = scenario.asTransportModel();

            ModelAssert.assertEquals("Error in scenario " + scenario.getName(), expected, actual);
        }
    }

    @Test
    public void testConversionUtanFalt5() throws JAXBException, IOException, SAXException, ConverterException {

        ObjectMapper objectMapper = new CustomObjectMapper();
        Fk7263Utlatande internalFormat = objectMapper.readValue(
                new ClassPathResource("InternalToTransportConverterTest/fk7263-utan-falt5.json").getInputStream(), Fk7263Utlatande.class);

        RegisterMedicalCertificateType registerMedicalCertificate = InternalToTransport.getJaxbObject(internalFormat);

        JAXBContext jaxbContext = JAXBContext.newInstance(RegisterMedicalCertificateType.class, LakarutlatandeType.class);
        StringWriter stringWriter = new StringWriter();
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.marshal(wrapJaxb(registerMedicalCertificate), stringWriter);

        // read expected XML and compare with resulting RegisterMedicalCertificateType
        String expectation = Resources.toString(new ClassPathResource("InternalToTransportConverterTest/fk7263-utan-falt5.xml")
                .getURL(), Charsets.UTF_8);

        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setIgnoreAttributeOrder(true);
        Diff diff = XMLUnit.compareXML(expectation, stringWriter.toString());
        diff.overrideDifferenceListener(new NamespacePrefixNameIgnoringListener());
        diff.overrideElementQualifier(new ElementNameAndTextQualifier());
        assertTrue(diff.toString(), diff.similar());
    }

   @Test
    public void testConversionMaximal() throws JAXBException, IOException, SAXException, ConverterException {

        ObjectMapper objectMapper = new CustomObjectMapper();
        Fk7263Utlatande internalFormat = objectMapper.readValue(
                new ClassPathResource("InternalToTransportConverterTest/maximalt-fk7263-internal.json").getInputStream(), Fk7263Utlatande.class);

        RegisterMedicalCertificateType registerMedicalCertificate = InternalToTransport.getJaxbObject(internalFormat);

        JAXBContext jaxbContext = JAXBContext.newInstance(RegisterMedicalCertificateType.class, LakarutlatandeType.class);
        StringWriter stringWriter = new StringWriter();
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.marshal(wrapJaxb(registerMedicalCertificate), stringWriter);

        // read expected XML and compare with resulting RegisterMedicalCertificateType
        String expectation = Resources.toString(new ClassPathResource("InternalToTransportConverterTest/maximalt-fk7263-transport.xml")
                .getURL(), Charsets.UTF_8);

        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setIgnoreAttributeOrder(true);
        Diff diff = XMLUnit.compareXML(expectation, stringWriter.toString());
        diff.overrideDifferenceListener(new NamespacePrefixNameIgnoringListener());
        diff.overrideElementQualifier(new ElementNameAndTextQualifier());
        assertTrue(diff.toString(), diff.similar());
    }

    @Test
    public void testConversionWithDiagnosisAsKSH97() throws JAXBException, IOException, SAXException, ConverterException {

        ObjectMapper objectMapper = new CustomObjectMapper();
        Fk7263Utlatande internalFormat = objectMapper.readValue(
                new ClassPathResource("InternalToTransportConverterTest/maximalt-fk7263-with-ksh97.json").getInputStream(), Fk7263Utlatande.class);

        RegisterMedicalCertificateType registerMedicalCertificate = InternalToTransport.getJaxbObject(internalFormat);

        JAXBContext jaxbContext = JAXBContext.newInstance(RegisterMedicalCertificateType.class, LakarutlatandeType.class);
        StringWriter stringWriter = new StringWriter();
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.marshal(wrapJaxb(registerMedicalCertificate), stringWriter);

        // read expected XML and compare with resulting RegisterMedicalCertificateType
        String expectation = Resources.toString(new ClassPathResource("InternalToTransportConverterTest/maximalt-fk7263-with-ksh97.xml")
                .getURL(), Charsets.UTF_8);

        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setIgnoreAttributeOrder(true);
        Diff diff = XMLUnit.compareXML(expectation, stringWriter.toString());
        diff.overrideDifferenceListener(new NamespacePrefixNameIgnoringListener());
        diff.overrideElementQualifier(new ElementNameAndTextQualifier());
        assertTrue(diff.toString(), diff.similar());
    }

    @Test
    public void testConversionMinimal() throws JAXBException, IOException, SAXException, ConverterException {

        ObjectMapper objectMapper = new CustomObjectMapper();
        Fk7263Utlatande externalFormat = objectMapper.readValue(
                new ClassPathResource("InternalToTransportConverterTest/minimalt-fk7263-internal.json").getInputStream(), Fk7263Utlatande.class);

        RegisterMedicalCertificateType registerMedicalCertificateType = InternalToTransport.getJaxbObject(externalFormat);

        JAXBContext jaxbContext = JAXBContext.newInstance(RegisterMedicalCertificateType.class, LakarutlatandeType.class);
        StringWriter stringWriter = new StringWriter();
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.marshal(wrapJaxb(registerMedicalCertificateType), stringWriter);

        // read expected XML and compare with resulting RegisterMedicalCertificateType
        String expectation = Resources.toString(new ClassPathResource("InternalToTransportConverterTest/minimalt-fk7263-transport.xml")
                .getURL(), Charsets.UTF_8);

        XMLUnit.setIgnoreWhitespace(true);
        Diff diff = new Diff(expectation, stringWriter.toString());
        diff.overrideDifferenceListener(new NamespacePrefixNameIgnoringListener());
        assertTrue(diff.toString(), diff.similar());
    }

    @Test
    public void testConversionKommentar() throws JAXBException, IOException, SAXException, ConverterException {

        ObjectMapper objectMapper = new CustomObjectMapper();
        Fk7263Utlatande externalFormat = objectMapper.readValue(
                new ClassPathResource("InternalToTransportConverterTest/friviligttext-fk7263-internal.json").getInputStream(), Fk7263Utlatande.class);
        RegisterMedicalCertificateType registerMedicalCertificateType = InternalToTransport.getJaxbObject(externalFormat);
        String expected = "8b: " + "nedsattMed25Beskrivning. " + "nedsattMed50Beskrivning. " + "nedsattMed75Beskrivning. kommentar";
        String result = registerMedicalCertificateType.getLakarutlatande().getKommentar();
        assertEquals(expected, result);
    }

    @Test
    public void testConversionOrimligtDatum() throws JAXBException, IOException, SAXException, ConverterException {


        ObjectMapper objectMapper = new CustomObjectMapper();
        Fk7263Utlatande externalFormat = objectMapper.readValue(
                new ClassPathResource("InternalToTransportConverterTest/minimalt-fk7263-internal-orimligt-datum.json").getInputStream(), Fk7263Utlatande.class);

        RegisterMedicalCertificateType registerMedicalCertificateType = InternalToTransport.getJaxbObject(externalFormat);

        JAXBContext jaxbContext = JAXBContext.newInstance(RegisterMedicalCertificateType.class, LakarutlatandeType.class);
        StringWriter stringWriter = new StringWriter();
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.marshal(wrapJaxb(registerMedicalCertificateType), stringWriter);

        // read expected XML and compare with resulting RegisterMedicalCertificateType
        String expectation = Resources.toString(new ClassPathResource("InternalToTransportConverterTest/minimalt-fk7263-transport-orimligt-datum.xml")
                .getURL(), Charsets.UTF_8);

        XMLUnit.setIgnoreWhitespace(true);
        Diff diff = new Diff(expectation, stringWriter.toString());
        diff.overrideDifferenceListener(new NamespacePrefixNameIgnoringListener());
        assertTrue(diff.toString(), diff.similar());
    }

    @Test
    public void testConversionMinimalSmiL() throws JAXBException, IOException, SAXException, ConverterException {

        ObjectMapper objectMapper = new CustomObjectMapper();
        Fk7263Utlatande externalFormat = objectMapper.readValue(
                new ClassPathResource("InternalToTransportConverterTest/minimalt-SmiL-fk7263-internal.json").getInputStream(), Fk7263Utlatande.class);

        RegisterMedicalCertificateType registerMedicalCertificateType = InternalToTransport.getJaxbObject(externalFormat);

        JAXBContext jaxbContext = JAXBContext.newInstance(RegisterMedicalCertificateType.class, LakarutlatandeType.class);
        StringWriter stringWriter = new StringWriter();
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.marshal(wrapJaxb(registerMedicalCertificateType), stringWriter);

        // read expected XML and compare with resulting RegisterMedicalCertificateType
        String expectation = Resources.toString(new ClassPathResource("InternalToTransportConverterTest/minimalt-SmiL-fk7263-transport.xml")
                .getURL(), Charsets.UTF_8);

        XMLUnit.setIgnoreWhitespace(true);
        Diff diff = new Diff(expectation, stringWriter.toString());
        diff.overrideDifferenceListener(new NamespacePrefixNameIgnoringListener());
        assertTrue(diff.toString(), diff.similar());
    }

    @Test
    public void testPersonnummerRoot() throws Exception {
        final String personnummer = "19121212-1212";
        Fk7263Utlatande utlatande = new Fk7263Utlatande();
        GrundData grundData = new GrundData();
        Patient patient = new Patient();
        patient.setPersonId(new Personnummer(personnummer));
        grundData.setPatient(patient);
        HoSPersonal skapadAv = new HoSPersonal();
        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setVardgivare(new Vardgivare());
        skapadAv.setVardenhet(vardenhet);
        grundData.setSkapadAv(skapadAv);
        utlatande.setGrundData(grundData);
        RegisterMedicalCertificateType res = InternalToTransport.getJaxbObject(utlatande);
        assertEquals(Constants.PERSON_ID_OID, res.getLakarutlatande().getPatient().getPersonId().getRoot());
        assertEquals(personnummer, res.getLakarutlatande().getPatient().getPersonId().getExtension());
    }

    @Test
    public void testSamordningRoot() throws Exception {
        final String personnummer = "19800191-0002";
        Fk7263Utlatande utlatande = new Fk7263Utlatande();
        GrundData grundData = new GrundData();
        Patient patient = new Patient();
        patient.setPersonId(new Personnummer(personnummer));
        grundData.setPatient(patient);
        HoSPersonal skapadAv = new HoSPersonal();
        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setVardgivare(new Vardgivare());
        skapadAv.setVardenhet(vardenhet);
        grundData.setSkapadAv(skapadAv);
        utlatande.setGrundData(grundData);
        RegisterMedicalCertificateType res = InternalToTransport.getJaxbObject(utlatande);
        assertEquals(Constants.SAMORDNING_ID_OID, res.getLakarutlatande().getPatient().getPersonId().getRoot());
        assertEquals(personnummer, res.getLakarutlatande().getPatient().getPersonId().getExtension());
    }

    private JAXBElement<?> wrapJaxb(RegisterMedicalCertificateType ws) {
        JAXBElement<?> jaxbElement = new JAXBElement<>(
                new QName("urn:riv:insuranceprocess:healthreporting:RegisterMedicalCertificateResponder:3", "RegisterMedicalCertificate"),
                RegisterMedicalCertificateType.class, ws);
        return jaxbElement;
    }

    private class NamespacePrefixNameIgnoringListener implements DifferenceListener {
        @Override
        public int differenceFound(Difference difference) {
            if (DifferenceConstants.NAMESPACE_PREFIX_ID == difference.getId()) {
                // differences in namespace prefix IDs are ok (eg. 'ns1' vs 'ns2'), as long as the namespace URI is the
                // same
                return RETURN_IGNORE_DIFFERENCE_NODES_IDENTICAL;
            } else {
                return RETURN_ACCEPT_DIFFERENCE;
            }
        }

        @Override
        public void skippedComparison(Node control, Node test) {
        }
    }
}
