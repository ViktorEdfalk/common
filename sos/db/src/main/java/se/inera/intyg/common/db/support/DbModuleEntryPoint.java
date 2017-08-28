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
package se.inera.intyg.common.db.support;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import se.inera.intyg.common.db.rest.DbModuleApi;
import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.services.texts.repo.IntygTextsRepository;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.ModuleEntryPoint;
import se.inera.intyg.common.support.modules.support.api.ModuleApi;
import se.inera.intyg.common.support.modules.support.feature.ModuleFeaturesFactory;

public class DbModuleEntryPoint implements ModuleEntryPoint {

    public static final String SCHEMATRON_FILE = "db.sch";
    public static final String MODULE_ID = "db";
    public static final String MODULE_NAME = "Dödsbevis";

    private static final String DEFAULT_RECIPIENT_ID = "Skatteverket";
    private static final String MODULE_DESCRIPTION = "Dödsbevis";
    private static final String DETAILED_DESCRIPTION_TEXT_KEY = "FRM_1.RBK";

    // Depending on context, an IntygTextRepository may not be available (e.g Intygstjansten)
    @Autowired(required = false)
    private Optional<IntygTextsRepository> repo;

    @Autowired
    private DbModuleApi moduleApi;

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
        if (repo.isPresent()) {
            final String latestVersion = repo.get().getLatestVersion(getModuleId());
            final IntygTexts texts = repo.get().getTexts(getModuleId(), latestVersion);
            if (texts != null) {
                return texts.getTexter().get(DETAILED_DESCRIPTION_TEXT_KEY);
            }
        }
        return null;
    }
    @Override
    public String getExternalId() {
        return MODULE_ID.toUpperCase();
    }

    @Override
    public String getDefaultRecipient() {
        return DEFAULT_RECIPIENT_ID;
    }

    @Override
    public ModuleApi getModuleApi() {
        return moduleApi;
    }

    @Override
    public Map<String, Boolean> getModuleFeatures() {
        return ModuleFeaturesFactory.getFeatures(MODULE_ID, "db-features.properties");
    }

    @Override
    public String getModuleCssPath(ApplicationOrigin originator) {
        return "/web/webjars/db/webcert/css/db.css";
    }

    @Override
    public String getModuleScriptPath(ApplicationOrigin originator) {
        return "/web/webjars/db/webcert/module";
    }

    @Override
    public String getModuleDependencyDefinitionPath(ApplicationOrigin originator) {
        return "/web/webjars/db/webcert/module-deps.json";
    }
}
