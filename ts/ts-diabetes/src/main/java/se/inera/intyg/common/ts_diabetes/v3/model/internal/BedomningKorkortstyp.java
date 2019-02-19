/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.ts_diabetes.v3.model.internal;

/**
 * This is a subset (VAR1-9, VAR11-18) of "Kv körkortsbehörighet"
 * {@link se.inera.intyg.common.ts_parent.codes.KorkortsbehorighetKod}.
 * The subset is in specified in
 * https://inera-certificate.atlassian.net/wiki/spaces/IT/pages/459374755/
 * Informationsspecifikation+TS+Diabetes+-+nytt+intygsformat+ppen+info
 *
 * Note - the order is also adjusted to match requirements above.
 *
 */
public enum BedomningKorkortstyp {
    AM,
    A1,
    A2,
    A,
    B,
    BE,
    TRAKTOR,
    C1,
    C1E,
    C,
    CE,
    D1,
    D1E,
    D,
    DE,
    TAXI,
    KANINTETASTALLNING
}
