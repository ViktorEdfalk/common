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
package se.inera.intyg.common.support.modules.registry;

import java.util.*;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.ModuleEntryPoint;
import se.inera.intyg.common.support.modules.support.api.ModuleApi;

public class IntygModuleRegistryImpl implements IntygModuleRegistry {

    private static final Logger LOG = LoggerFactory.getLogger(IntygModuleRegistryImpl.class);

    /*
     * The Autowired annotation will automagically pickup all registered beans with type ModuleEntryPoint and
     * insert into the moduleEntryPoints list.
     */
    @Autowired
    private List<ModuleEntryPoint> moduleEntryPoints;

    private Map<String, ModuleEntryPoint> moduleApiMap = new HashMap<>();

    private Map<String, IntygModule> intygModuleMap = new HashMap<>();

    private Map<String, String> externalIdToModuleId = new HashMap<>();

    private ApplicationOrigin origin;

    @PostConstruct
    public void initModulesList() {

        for (ModuleEntryPoint entryPoint : moduleEntryPoints) {
            moduleApiMap.put(entryPoint.getModuleId(), entryPoint);
            externalIdToModuleId.put(entryPoint.getExternalId(), entryPoint.getModuleId());
            IntygModule module = new IntygModule(entryPoint.getModuleId(), entryPoint.getModuleName(),
                    entryPoint.getModuleDescription(), entryPoint.getDetailedModuleDescription(),
                    entryPoint.getModuleCssPath(origin), entryPoint.getModuleScriptPath(origin),
                    entryPoint.getModuleDependencyDefinitionPath(origin), entryPoint.getDefaultRecipient());

            intygModuleMap.put(module.getId(), module);
        }

        LOG.info("Module registry loaded with {} modules", moduleApiMap.size());
    }

    @Override
    public List<IntygModule> listAllModules() {
        List<IntygModule> moduleList = new ArrayList<>(intygModuleMap.values());
        Collections.sort(moduleList);
        return moduleList;
    }

    @Override
    public ModuleApi getModuleApi(String id) throws ModuleNotFoundException {
        ModuleEntryPoint api = moduleApiMap.get(id);
        if (api == null) {
            throw new ModuleNotFoundException("Could not find module " + id);
        }
        return api.getModuleApi();
    }

    @Override
    public ModuleEntryPoint getModuleEntryPoint(String id) throws ModuleNotFoundException {
        ModuleEntryPoint entryPoint = moduleApiMap.get(id);
        if (entryPoint == null) {
            throw new ModuleNotFoundException("Could not find module " + id);
        }
        return entryPoint;
    }

    @Override
    public IntygModule getIntygModule(String id) throws ModuleNotFoundException {
        if (!intygModuleMap.containsKey(id)) {
            throw new ModuleNotFoundException("Could not find module " + id);
        }
        return intygModuleMap.get(id);
    }

    @Override
    public List<ModuleEntryPoint> getModuleEntryPoints() {
        return moduleEntryPoints;
    }

    @Override
    public boolean moduleExists(String moduleId) {
        return moduleApiMap.containsKey(moduleId);
    }

    public void setOrigin(ApplicationOrigin origin) {
        this.origin = origin;
    }

    public ApplicationOrigin getOrigin() {
        return origin;
    }

    @Override
    public String getModuleIdFromExternalId(String externalId) {
        return externalIdToModuleId.get(externalId);
    }
}
