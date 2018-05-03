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
package se.inera.intyg.common.db.model.internal;

import java.util.stream.Stream;

public enum Undersokning {
    JA(""), // Transport is never used as it is represented as boolean 'true'
    UNDERSOKNING_GJORT(UndersokningConstants.UNDERSOKNING_GJORT),
    UNDERSOKNING_SKA_GORAS(UndersokningConstants.UNDERSOKNING_SKA_GORAS);

    private final String transport;

    Undersokning(final String transport) {
        this.transport = transport;
    }

    public String getTransport() {
        return transport;
    }

    public static Undersokning fromTransport(String transport) {
        return Stream.of(Undersokning.values())
                .filter(v -> v.name().equals(transport))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("unknown value: " + transport));
    }
}
