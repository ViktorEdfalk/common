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
package se.inera.intyg.common.ts_tstrk1062.v1.model.internal;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

public enum IntygAvserKategori {
    C1, C1E, C, CE, D1, D1E, D, DE, TAXI, ANNAT;

    public static Set<IntygAvserKategori> getNormalCategories() {
        return ImmutableSet.of(C1, C1E, C, CE, D1, D1E, D, DE, TAXI);
    }
}
