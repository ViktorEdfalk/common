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
package se.inera.intyg.common.support.modules.support.feature;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.core.io.*;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public final class ModuleFeaturesFactory {

    private ModuleFeaturesFactory() {
    }

    public static Map<String, Boolean> getFeatures(String moduleId, String fallbackFeaturePropertiesFile) {

        Properties features = loadFeaturePropertiesFile(moduleId, fallbackFeaturePropertiesFile);

        Map<String, Boolean> moduleFeaturesMap = new HashMap<>();

        for (ModuleFeature feature : ModuleFeature.values()) {

            Boolean featureState = Boolean.parseBoolean(features.getProperty(feature.getName()));

            if (featureState == null) {
                moduleFeaturesMap.put(feature.getName(), Boolean.FALSE);
                continue;
            }

            moduleFeaturesMap.put(feature.getName(), featureState);
        }

        return moduleFeaturesMap;
    }

    private static Properties loadFeaturePropertiesFile(String moduleId, String featurePropertiesFile) {
        String path = System.getProperty(String.format("feature.%s.file", moduleId));
        Resource resource = (path != null)
                ? new FileSystemResource(path)
                : new ClassPathResource(featurePropertiesFile);
        try {
            return PropertiesLoaderUtils.loadProperties(resource);
        } catch (IOException e) {
            throw new IllegalArgumentException("Feature file not found");
        }
    }
}
