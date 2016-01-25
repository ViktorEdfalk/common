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

package se.inera.intyg.common.support.modules.support.api.dto;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PatientTest {

    @Test
    public void testFullstandigtNamnWithMellannamn() {
        Patient patient = new Patient("Test", "Svensson", "Testsson", new Personnummer("19121212-1212"), null, null, null);
        assertEquals("Test Svensson Testsson", patient.getFullstandigtNamn());
    }

    @Test
    public void testFullstandigtNamnWithoutMellannamn() {
        Patient patient = new Patient("Test", null, "Testsson", new Personnummer("19121212-1212"), null, null, null);
        assertEquals("Test Testsson", patient.getFullstandigtNamn());
    }
}
