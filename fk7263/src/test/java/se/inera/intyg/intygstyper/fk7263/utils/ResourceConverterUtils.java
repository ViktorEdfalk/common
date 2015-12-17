/*
 * Copyright (C) 2015 Inera AB (http://www.inera.se)
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

package se.inera.intyg.intygstyper.fk7263.utils;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXB;

import se.inera.intyg.common.util.integration.integration.json.CustomObjectMapper;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class ResourceConverterUtils {

    private ResourceConverterUtils() {
    }

    private static final ObjectMapper OBJECT_MAPPER = new CustomObjectMapper();

    public static RegisterMedicalCertificateType toTransport(File resource) throws IOException {
        return JAXB.unmarshal(resource, RegisterMedicalCertificateType.class);
    }

    public static se.inera.intyg.intygstyper.fk7263.model.internal.Utlatande toInternal(File resource)
            throws IOException {
        return OBJECT_MAPPER.readValue(resource, se.inera.intyg.intygstyper.fk7263.model.internal.Utlatande.class);
    }

    public static se.inera.intyg.intygstyper.fk7263.model.internal.Utlatande toInternal(String resource)
            throws IOException {
        return OBJECT_MAPPER.readValue(resource, se.inera.intyg.intygstyper.fk7263.model.internal.Utlatande.class);
    }
}
