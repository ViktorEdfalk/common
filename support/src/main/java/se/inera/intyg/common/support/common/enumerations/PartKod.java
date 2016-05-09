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

package se.inera.intyg.common.support.common.enumerations;

import java.util.stream.Stream;

public enum PartKod {

    FKASSA("FK", "Försäkringskassan"),
    HSVARD("HV", "Hälso- och sjukvården"),
    INVANA("MI", "Invånaren"),
    TRANSP("TS", "Transportstyrelsen");

    private String value;
    private String displayName;

    PartKod(String value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    public String getValue() {
        return value;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static PartKod fromValue(String value) {
        return Stream.of(PartKod.values()).filter(s -> value.equals(s.getValue())).findFirst()
                .orElseThrow(() -> new IllegalArgumentException(value));
    }

}
