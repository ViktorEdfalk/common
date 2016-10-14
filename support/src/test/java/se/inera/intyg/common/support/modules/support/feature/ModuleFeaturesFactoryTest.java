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

package se.inera.intyg.common.support.modules.support.feature;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Map;

public class ModuleFeaturesFactoryTest {

    private static final String MODUL_ID = "modulId";
    private static final String TEST_FILE = "/Features/test-features.properties";

    @Test
    public void testFactory() {
        Map<String, Boolean> features = ModuleFeaturesFactory.getFeatures(MODUL_ID, TEST_FILE);
        assertNotNull(features);

        // There are 8 features in ModuleFeature.java
        assertEquals(8, features.size());

        assertTrue(features.get(ModuleFeature.HANTERA_FRAGOR.getName()));
        assertFalse(features.get(ModuleFeature.MAKULERA_INTYG.getName()));
        assertFalse(features.get(ModuleFeature.SKICKA_INTYG.getName()));
    }

    @Test
    public void testFactoryExternalFile() {
        try {
            System.setProperty("feature." + MODUL_ID + ".file", System.getProperty("user.dir") + "/src/test/resources/" + TEST_FILE);

            Map<String, Boolean> features = ModuleFeaturesFactory.getFeatures(MODUL_ID, "does not exist");
            assertNotNull(features);

            // There are 8 features in ModuleFeature.java
            assertEquals(8, features.size());

            assertTrue(features.get(ModuleFeature.HANTERA_FRAGOR.getName()));
            assertFalse(features.get(ModuleFeature.MAKULERA_INTYG.getName()));
            assertFalse(features.get(ModuleFeature.SKICKA_INTYG.getName()));
        } finally {
            System.clearProperty("feature." + MODUL_ID + ".file");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFactoryNoFile() {
        ModuleFeaturesFactory.getFeatures(MODUL_ID, "does not exist");
    }
}
