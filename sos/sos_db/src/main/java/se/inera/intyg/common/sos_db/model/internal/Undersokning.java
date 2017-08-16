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
package se.inera.intyg.common.sos_db.model.internal;

import java.util.stream.Stream;

public enum Undersokning {
    JA(""), // Transport is never used as it is represented as boolean 'true'
    UNDERSOKNING_GJORT_KORT_FORE_DODEN("UNDERSOKNING_GJORT"),
    UNDERSOKNING_SKA_GORAS("UNDERSOKNING_SKA_GORAS");

    private final String transport;

    Undersokning(String transport) {
        this.transport = transport;
    }

    public static Undersokning fromTransport(String transport) {
        return Stream.of(Undersokning.values())
                .filter(undersokning -> undersokning.getTransport().equals(transport))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException());
    }

    public String getTransport() {
        return transport;
    }
}
