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
package se.inera.intyg.common.support.modules.registry;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.ModuleEntryPoint;
import se.inera.intyg.common.support.modules.support.api.ModuleApi;

@RunWith(MockitoJUnitRunner.class)
public class IntygModuleRegistryImplTest {

    private static final ApplicationOrigin ORIGIN = ApplicationOrigin.WEBCERT;
    private static final String MODULE_ID_1 = "moduleId1";
    private static final String EXTERNAL_ID_1 = "externalId1";
    private static final String MODULE_NAME_1 = "moduleName1";
    private static final String MODULE_DESCRIPTION_1 = "moduleDescription1";
    private static final String DETAILED_MODULE_DESCRIPTION_1 = "detailedModuleDescription1";
    private static final String MODULE_CSS_PATH_1 = "moduleCssPath1";
    private static final String MODULE_SCRIPT_PATH_1 = "moduleScriptPath1";
    private static final String MODULE_DEPENDENCY_DEFINITION_PATH_1 = "moduleDependencyDefinitionPath1";
    private static final String MODULE_ID_2 = "moduleId2";
    private static final String EXTERNAL_ID_2 = "externalId2";
    private static final String MODULE_NAME_2 = "moduleName2";
    private static final String MODULE_DESCRIPTION_2 = "moduleDescription2";
    private static final String DETAILED_MODULE_DESCRIPTION_2 = "detailedModuleDescription2";
    private static final String MODULE_CSS_PATH_2 = "moduleCssPath2";
    private static final String MODULE_SCRIPT_PATH_2 = "moduleScriptPath2";
    private static final String MODULE_DEPENDENCY_DEFINITION_PATH_2 = "moduleDependencyDefinitionPath2";


    @Mock
    private ModuleEntryPoint entryPointMock1;

    @Mock
    private ModuleEntryPoint entryPointMock2;

    private IntygModuleRegistryImpl registry;

    @Before
    public void setup() {
        when(entryPointMock1.getModuleId()).thenReturn(MODULE_ID_1);
        when(entryPointMock1.getExternalId()).thenReturn(EXTERNAL_ID_1);
        when(entryPointMock1.getModuleName()).thenReturn(MODULE_NAME_1);
        when(entryPointMock1.getModuleDescription()).thenReturn(MODULE_DESCRIPTION_1);
        when(entryPointMock1.getDetailedModuleDescription()).thenReturn(DETAILED_MODULE_DESCRIPTION_1);
        when(entryPointMock1.getModuleCssPath(ORIGIN)).thenReturn(MODULE_CSS_PATH_1);
        when(entryPointMock1.getModuleScriptPath(ORIGIN)).thenReturn(MODULE_SCRIPT_PATH_1);
        when(entryPointMock1.getModuleDependencyDefinitionPath(ORIGIN)).thenReturn(MODULE_DEPENDENCY_DEFINITION_PATH_1);
        when(entryPointMock2.getModuleId()).thenReturn(MODULE_ID_2);
        when(entryPointMock2.getExternalId()).thenReturn(EXTERNAL_ID_2);
        when(entryPointMock2.getModuleName()).thenReturn(MODULE_NAME_2);
        when(entryPointMock2.getModuleDescription()).thenReturn(MODULE_DESCRIPTION_2);
        when(entryPointMock2.getDetailedModuleDescription()).thenReturn(DETAILED_MODULE_DESCRIPTION_2);
        when(entryPointMock2.getModuleCssPath(ORIGIN)).thenReturn(MODULE_CSS_PATH_2);
        when(entryPointMock2.getModuleScriptPath(ORIGIN)).thenReturn(MODULE_SCRIPT_PATH_2);
        when(entryPointMock2.getModuleDependencyDefinitionPath(ORIGIN)).thenReturn(MODULE_DEPENDENCY_DEFINITION_PATH_2);

        registry = new IntygModuleRegistryImpl();
        registry.setOrigin(ORIGIN);
        ReflectionTestUtils.setField(registry, "moduleEntryPoints", Arrays.asList(entryPointMock1, entryPointMock2));
        registry.initModulesList();
    }

    @Test
    public void testListAllModules() throws Exception {
        List<IntygModule> res = registry.listAllModules();

        assertNotNull(res);
        assertEquals(2, res.size());
        assertEquals(MODULE_ID_1, res.get(0).getId());
        assertEquals(MODULE_NAME_1, res.get(0).getLabel());
        assertEquals(MODULE_DESCRIPTION_1, res.get(0).getDescription());
        assertEquals(DETAILED_MODULE_DESCRIPTION_1, res.get(0).getDetailedDescription());
        assertEquals(MODULE_CSS_PATH_1, res.get(0).getCssPath());
        assertEquals(MODULE_SCRIPT_PATH_1, res.get(0).getScriptPath());
        assertEquals(MODULE_DEPENDENCY_DEFINITION_PATH_1, res.get(0).getDependencyDefinitionPath());
        assertEquals(MODULE_ID_2, res.get(1).getId());
        assertEquals(MODULE_NAME_2, res.get(1).getLabel());
        assertEquals(MODULE_DESCRIPTION_2, res.get(1).getDescription());
        assertEquals(DETAILED_MODULE_DESCRIPTION_2, res.get(1).getDetailedDescription());
        assertEquals(MODULE_CSS_PATH_2, res.get(1).getCssPath());
        assertEquals(MODULE_SCRIPT_PATH_2, res.get(1).getScriptPath());
        assertEquals(MODULE_DEPENDENCY_DEFINITION_PATH_2, res.get(1).getDependencyDefinitionPath());
    }

    @Test
    public void testGetModuleApi() throws Exception {
        when(entryPointMock1.getModuleApi()).thenReturn(mock(ModuleApi.class));
        ModuleApi res = registry.getModuleApi(MODULE_ID_1);

        assertNotNull(res);
    }

    @Test(expected = ModuleNotFoundException.class)
    public void testGetModuleApiNotFound() throws Exception {
        registry.getModuleApi("nonExistentModule");
    }

    @Test
    public void testGetModuleEntryPoint() throws Exception {
        ModuleEntryPoint res = registry.getModuleEntryPoint(MODULE_ID_1);

        assertEquals(MODULE_ID_1, res.getModuleId());
    }

    @Test(expected = ModuleNotFoundException.class)
    public void testGetModuleEntryPointNotFound() throws Exception {
        registry.getModuleEntryPoint("nonExistentModule");
    }

    @Test
    public void testGetIntygModule() throws Exception {
        IntygModule res = registry.getIntygModule(MODULE_ID_1);

        assertEquals(MODULE_ID_1, res.getId());
    }

    @Test(expected = ModuleNotFoundException.class)
    public void testGetIntygModuleNotFound() throws Exception {
        registry.getIntygModule("nonExistentModule");
    }

    @Test
    public void testGetModuleEntryPoints() {
        List<ModuleEntryPoint> res = registry.getModuleEntryPoints();
        assertEquals(2, res.size());
    }

    @Test
    public void testModuleExists() {
        boolean res1 = registry.moduleExists(MODULE_ID_1);
        boolean res2 = registry.moduleExists("nonExistentModule");
        assertTrue(res1);
        assertFalse(res2);
    }

    @Test
    public void testGetModuleIdFromExternalId() {
        String res1 = registry.getModuleIdFromExternalId(EXTERNAL_ID_1);
        String res2 = registry.getModuleIdFromExternalId(EXTERNAL_ID_2);
        String res3 = registry.getModuleIdFromExternalId("nonExistentModule");
        assertEquals(MODULE_ID_1, res1);
        assertEquals(MODULE_ID_2, res2);
        assertNull(res3);
    }
}
