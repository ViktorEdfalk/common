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
package se.inera.intyg.common.support.modules.support;

import se.inera.intyg.common.support.modules.support.api.ModuleApi;

import java.util.Map;

/**
 * Defines the contract for modules so they can be discovered by an application.
 */
public interface ModuleEntryPoint {

    /**
     * Returns the unique id for the module. The id should only contain the a-z, 0-9 and '_' characters.
     *
     * @return the unique module id
     */
    String getModuleId();

    /**
     * Returns the friendly name of the module, suitable for display in the GUI.
     *
     * @return the friendly name of the module
     */
    String getModuleName();

    /**
     * Returns description for the module, suitable for display in the GUI.
     *
     * @return the friendly description of the module
     */
    String getModuleDescription();

    /**
     * Returns a detailed description for the module, suitable for display in the GUI.
     *
     * @return the detailed description of the module
     */
    String getDetailedModuleDescription();

    /**
     * Returns a unique external id for the module.
     *
     * @return the id used outside of the application
     */
    String getExternalId();

    /**
     * Returns the identifier of the default recipient of this module, or null if no default recipient is specified.
     *
     * @return the identifier of the default recipient or null if none is specified
     */
    String getDefaultRecipient();

    /**
     * Returns the module specific implementation of the module API.
     *
     * @return the module API implementation
     */
    ModuleApi getModuleApi();

    /**
     * Returns a Map containing which {@code se.inera.intyg.common.support.modules.support.feature.ModuleFeature} that this
     * module will support and what state these have.
     *
     * @return the map of feature to on/off state of the module feature
     */
    Map<String, Boolean> getModuleFeatures();

    /**
     * Returns the module css path.
     *
     * @param originator the calling application
     * @return the module css path for the calling application
     */
    String getModuleCssPath(ApplicationOrigin originator);

    /**
     * Returns the module script path.
     *
     * @param originator the calling application
     * @return the module script path for the calling application
     */
    String getModuleScriptPath(ApplicationOrigin originator);

    /**
     * Returns the path to a resource containing the dependencies for the module.
     *
     * @param originator the calling application
     * @return the path to the module dependency definition for the calling application
     */
    String getModuleDependencyDefinitionPath(ApplicationOrigin originator);
}
