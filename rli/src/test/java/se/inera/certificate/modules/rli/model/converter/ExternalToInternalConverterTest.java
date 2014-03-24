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

import static org.unitils.reflectionassert.ReflectionAssert.assertLenientEquals;

import org.junit.Before;
import org.junit.Test;

import se.inera.certificate.modules.rli.model.external.Utlatande;
import se.inera.certificate.modules.rli.utils.Scenario;
import se.inera.certificate.modules.rli.utils.ScenarioFinder;

/**
 * Unit test for the ExternalToInteralConverter. This test is Spring configured.
 * 
 * @author Niklas Pettersson, R2M
 * 
 */
public class ExternalToInternalConverterTest {

    private static ExternalToInternalConverter converter;

    @Before
    public void setUp() throws Exception {
        converter = new ExternalToInternalConverter();
    }

    @Test
    public void testConvertUtlatande() throws Exception {
        for (Scenario scenario : ScenarioFinder.getExternalScenarios("valid-*")) {
            Utlatande extUtlatande = scenario.asExternalModel();

            se.inera.certificate.modules.rli.model.internal.mi.Utlatande actual = converter.convert(extUtlatande);

            se.inera.certificate.modules.rli.model.internal.mi.Utlatande expected = scenario.asInternalMIModel();
            assertLenientEquals("Error in scenario " + scenario.getName(), expected, actual);
        }
    }
}
