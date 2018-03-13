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
package se.inera.intyg.common.ts_diabetes.support;

import org.springframework.beans.factory.annotation.Autowired;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.ModuleEntryPoint;
import se.inera.intyg.common.support.modules.support.api.ModuleApi;
import se.inera.intyg.common.ts_diabetes.rest.TsDiabetesModuleApi;

public class TsDiabetesEntryPoint implements ModuleEntryPoint {

    public static final String MODULE_ID = "ts-diabetes";
    public static final String MODULE_NAME = "Transportstyrelsens läkarintyg, diabetes";
    public static final String MODULE_DESCRIPTION = "Läkarintyg diabetes avseende lämpligheten att inneha körkort m.m.";
    // CHECKSTYLE:ON LineLength
    public static final String KV_UTLATANDETYP_INTYG_CODE = "TSTRK1031";
    private static final String DEFAULT_RECIPIENT_ID = "TRANSP";
    // CHECKSTYLE:OFF LineLength
    private static final String MODULE_DETAILED_DESCRIPTION = "<p>Transportstyrelsens läkarintyg, diabetes ska användas vid diabetessjukdom. Föreskrivna krav på läkarens specialistkompetens vid diabetessjukdom framgår av 17 kap. i Transportstyrelsens föreskrifter (TSFS 2010:125) och allmänna råd om medicinska krav för innehav av körkort m.m.</p>Information om Transportstyrelsens föreskrifter finns på <LINK:transportstyrelsen>.";
    private static final boolean DEPRECATED = false;

    @Autowired
    private TsDiabetesModuleApi moduleApi;

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
    public String getDetailedModuleDescription() {
        return MODULE_DETAILED_DESCRIPTION;
    }

    @Override
    public ModuleApi getModuleApi() {
        return moduleApi;
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
    public boolean isDeprecated() {
        return DEPRECATED;    }

    @Override
    public String getDefaultRecipient() {
        return DEFAULT_RECIPIENT_ID;
    }

    @Override
    public String getExternalId() {
        return KV_UTLATANDETYP_INTYG_CODE;
    }

    @Override
    public String getIssuerTypeId() {
        //Same as externalId for ts
        return KV_UTLATANDETYP_INTYG_CODE;
    }
}
