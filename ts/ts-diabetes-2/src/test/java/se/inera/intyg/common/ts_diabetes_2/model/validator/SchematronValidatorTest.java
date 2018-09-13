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
package se.inera.intyg.common.ts_diabetes_2.model.validator;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.helger.commons.debug.GlobalDebug;
import org.junit.Ignore;
import org.junit.Test;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateXmlResponse;
import se.inera.intyg.common.support.validate.RegisterCertificateValidator;
import se.inera.intyg.common.support.validate.XmlValidator;
import se.inera.intyg.common.ts_diabetes_2.support.TsDiabetes2EntryPoint;

import java.net.URL;
import java.util.stream.Collectors;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SchematronValidatorTest {

    private static final RegisterCertificateValidator VALIDATOR = new RegisterCertificateValidator(TsDiabetes2EntryPoint.SCHEMATRON_FILE);

    static {
        // avoid com.helger debug log
        GlobalDebug.setDebugModeDirect(false);
    }

    @Test
    public void brokenXmlFailsTest() throws Exception {
        String inputXml = Resources.toString(getResource("transport/ts-diabetes-2_broken.xml"), Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        assertFalse(response.getValidationErrors().isEmpty());
    }

    @Test
    public void validXmlPassesTest() throws Exception {
        String inputXml = Resources.toString(getResource("transport/scenarios/ts-diabetes-2-pass-complete.xml"), Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        assertTrue(response.getValidationErrors().stream().collect(Collectors.joining("\n")), response.getValidationErrors().isEmpty());
    }

    private static URL getResource(String href) {
        return Thread.currentThread().getContextClassLoader().getResource(href);
    }
}
