/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.fkparent.model.validator;

import com.helger.schematron.svrl.SVRLHelper;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import org.oclc.purl.dsdl.svrl.SchematronOutputType;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.xml.XmlMarshallerHelper;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.ObjectFactory;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;

public final class InternalToSchematronValidatorTestUtil {

    private InternalToSchematronValidatorTestUtil() {
    }

    public static String getTransportValidationErrorString(SchematronOutputType result) {
        return SVRLHelper.getAllFailedAssertions(result).stream()
            .map(e -> String.format("Test: %s, Text: %s", e.getTest(), e.getText()))
            .collect(Collectors.joining(";"));
    }

    public static String getInternalValidationErrorString(ValidateDraftResponse internalValidationResponse) {
        return internalValidationResponse.getValidationErrors().stream()
            .map(e -> e.getMessage())
            .collect(Collectors.joining(", "));
    }

    public static String getXmlFromModel(RegisterCertificateType transport) throws IOException, JAXBException {
        ObjectFactory objectFactory = new ObjectFactory();
        JAXBElement<RegisterCertificateType> jaxbElement = objectFactory.createRegisterCertificate(transport);
        return XmlMarshallerHelper.marshal(jaxbElement);
    }

    public static int getNumberOfInternalValidationErrors(ValidateDraftResponse internalValidationResponse, List<String> ignoredFields) {
        return (int) internalValidationResponse.getValidationErrors().stream()
            .filter(e -> !ignoredFields.contains(e.getField()))
            .count();
    }

    public static int getNumberOfInternalValidationErrors(ValidateDraftResponse internalValidationResponse) {
        return getNumberOfInternalValidationErrors(internalValidationResponse, Collections.emptyList());
    }

    public static int getNumberOfTransportValidationErrors(SchematronOutputType result) {
        return SVRLHelper.getAllFailedAssertions(result).size();
    }
}
