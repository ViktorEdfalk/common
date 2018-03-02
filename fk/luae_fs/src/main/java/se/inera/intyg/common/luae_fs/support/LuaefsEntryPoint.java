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
package se.inera.intyg.common.luae_fs.support;

import org.springframework.beans.factory.annotation.Autowired;
import se.inera.intyg.common.fkparent.support.FkAbstractModuleEntryPoint;
import se.inera.intyg.common.luae_fs.rest.LuaefsModuleApi;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.api.ModuleApi;

public class LuaefsEntryPoint extends FkAbstractModuleEntryPoint {

    public static final String ISSUER_TYPE_ID = "FK 7802";
    public static final String MODULE_ID = "luae_fs";
    public static final String MODULE_NAME = "Läkarutlåtande för aktivitetsersättning vid förlängd skolgång";
    public static final String MODULE_DESCRIPTION = "Läkarutlåtande för aktivitetsersättning vid förlängd skolgång";
    public static final String SCHEMATRON_FILE = "luae_fs.sch";

    @Autowired
    private LuaefsModuleApi moduleApi;

    @Override
    public String getModuleId() {
        return MODULE_ID;
    }

    @Override
    public String getModuleName() {
        return MODULE_NAME;
    }

    @Override
    public String getModuleDescription() {
        return MODULE_DESCRIPTION;
    }

    @Override
    public String getIssuerTypeId() {
        return ISSUER_TYPE_ID;
    }

    @Override
    public ModuleApi getModuleApi() {
        return moduleApi;
    }

    @Override
    public String getModuleCssPath(ApplicationOrigin originator) {
        switch (originator) {
        case MINA_INTYG:
            return "/web/webjars/luae_fs/minaintyg/css/luae_fs.css";
        case WEBCERT:
            return "/web/webjars/luae_fs/webcert/css/luae_fs.css";
        default:
            return null;
        }
    }

    @Override
    public String getModuleScriptPath(ApplicationOrigin originator) {
        switch (originator) {
        case MINA_INTYG:
            return "/web/webjars/luae_fs/minaintyg/js/module";
        case WEBCERT:
            return "/web/webjars/luae_fs/webcert/module";
        default:
            return null;
        }
    }

    @Override
    public String getModuleDependencyDefinitionPath(ApplicationOrigin originator) {
        switch (originator) {
        case MINA_INTYG:
            return "/web/webjars/luae_fs/minaintyg/js/module-deps.json";
        case WEBCERT:
            return "/web/webjars/luae_fs/webcert/module-deps.json";
        default:
            return null;
        }
    }
}
