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

package se.inera.intyg.intygstyper.ts_diabetes.support;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.ModuleEntryPoint;
import se.inera.intyg.common.support.modules.support.api.ModuleApi;
import se.inera.intyg.common.support.modules.support.feature.ModuleFeaturesFactory;
import se.inera.intyg.intygstyper.ts_diabetes.rest.TsDiabetesModuleApi;

public class TsDiabetesEntryPoint implements ModuleEntryPoint {

    private static final String TRANSPORTSTYRELSEN_LOGICAL_ADRESS = "TS";
    public static final String MODULE_ID = "ts-diabetes";

    @Autowired
    private TsDiabetesModuleApi tsDiabetesModuleService;

    @Override
    public String getModuleId() {
        return "ts-diabetes";
    }

    @Override
    public String getModuleName() {
        return "Transportstyrelsens läkarintyg, diabetes";
    }

    @Override
    public String getModuleDescription() {
        return "Läkarintyg diabetes avseende lämpligheten att inneha körkort m.m.";
    }

    /*@Override
    public String getDefaultRecieverLogicalAddress() {
        return TRANSPORTSTYRELSEN_LOGICAL_ADRESS;
    }*/

    @Override
    public ModuleApi getModuleApi() {
        return tsDiabetesModuleService;
    }

    @Override
    public Map<String, Boolean> getModuleFeatures() {
        return ModuleFeaturesFactory.getFeatures("ts-diabetes-features.properties");
    }

    @Override
    public String getModuleCssPath(ApplicationOrigin originator) {
        switch (originator) {
        case MINA_INTYG:
            return "/web/webjars/ts-diabetes/minaintyg/css/ts-diabetes.css";
        case WEBCERT:
            return "/web/webjars/ts-diabetes/webcert/css/ts-diabetes.css";
        default:
            return null;
        }
    }

    @Override
    public String getModuleScriptPath(ApplicationOrigin originator) {
        switch (originator) {
        case MINA_INTYG:
            return "/web/webjars/ts-diabetes/minaintyg/js/module";
        case WEBCERT:
            return "/web/webjars/ts-diabetes/webcert/module";
        default:
            return null;
        }
    }

    @Override
    public String getModuleDependencyDefinitionPath(ApplicationOrigin originator) {
        switch (originator) {
        case MINA_INTYG:
            return "/web/webjars/ts-diabetes/minaintyg/js/module-deps.json";
        case WEBCERT:
            return "/web/webjars/ts-diabetes/webcert/module-deps.json";
        default:
            return null;
        }
    }

    @Override
    public String getDefaultRecipient() {
        return TRANSPORTSTYRELSEN_LOGICAL_ADRESS;
    }
}
