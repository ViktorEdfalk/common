/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.services.texts.model;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.SortedMap;

import com.google.common.base.Splitter;
import com.google.common.primitives.Ints;

/**
 * Data container for the texts used in certificates.
 */
public final class IntygTexts {

    private static final String DELIMITER = "\\.";
    private final String version;
    private final String intygsTyp;
    private final Properties properties;
    private final LocalDate validFrom;
    private final LocalDate validTo;
    private final SortedMap<String, String> texter;
    private final List<Tillaggsfraga> tillaggsfragor;

    public IntygTexts(String version, String intygsTyp, LocalDate validFrom, LocalDate validTo, SortedMap<String, String> texts,
            List<Tillaggsfraga> tillaggsfragor, Properties properties) {

        // Validate input
        validateVersion(version);

        this.version = version;
        this.intygsTyp = intygsTyp;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.texter = texts;
        this.tillaggsfragor = tillaggsfragor;
        if (this.tillaggsfragor != null) {
            Collections.sort(this.tillaggsfragor);
        }
        this.properties = properties;
    }

    public static int compareVersions(IntygTexts candidate1, IntygTexts candidate2) {
        List<String> tokens1 = Splitter.onPattern(DELIMITER).splitToList(candidate1.getVersion());
        List<String> tokens2 = Splitter.onPattern(DELIMITER).splitToList(candidate2.getVersion());
        int length = Math.max(tokens1.size(), tokens2.size());
        for (int i = 0; i < length; i++) {
            int part1 = i < tokens1.size() ? Integer.parseInt(tokens1.get(i)) : 0;
            int part2 = i < tokens2.size() ? Integer.parseInt(tokens2.get(i)) : 0;
            if (part1 < part2) {
                return -1;
            }
            if (part1 > part2) {
                return 1;
            }
        }
        return 0;
    }

    private void validateVersion(String version) {
        if (version == null || !Arrays.stream(version.split(DELIMITER)).allMatch(s -> Ints.tryParse(s) != null)) {
            throw new IllegalArgumentException("Version " + version + " is not in format 'x.x.x.x'");
        }
    }

    public String getVersion() {
        return version;
    }

    public String getIntygsTyp() {
        return intygsTyp;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public LocalDate getValidTo() {
        return validTo;
    }

    public SortedMap<String, String> getTexter() {
        return texter;
    }

    public List<Tillaggsfraga> getTillaggsfragor() {
        return tillaggsfragor;
    }

    @Override
    public int hashCode() {
        // Autogenerated
        final int prime = 31;
        int result = 1;
        result = prime * result + ((intygsTyp == null) ? 0 : intygsTyp.hashCode());
        result = prime * result + ((version == null) ? 0 : version.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        // Autogenerated - we are only using version and type
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        IntygTexts other = (IntygTexts) obj;
        if (intygsTyp == null) {
            if (other.intygsTyp != null) {
                return false;
            }
        } else if (!intygsTyp.equals(other.intygsTyp)) {
            return false;
        }
        return compareVersions(this, other) == 0;
    }

    public Properties getProperties() {
        return properties;
    }
}
