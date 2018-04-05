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
package se.inera.intyg.common.support.persistence.dao.util;

import static org.junit.Assert.*;

import org.junit.Test;

import se.inera.intyg.schemas.contract.Personnummer;
import se.inera.intyg.common.support.peristence.dao.util.DaoUtil;

public class DaoUtilTest {
    @Test
    public void testPnrWithAndWithoutDashYieldsSameResult() {
        Personnummer pnr1 = new Personnummer("191212121212");
        Personnummer pnr2 = new Personnummer("19121212-1212");

        assertEquals("19121212-1212", DaoUtil.formatPnrForPersistence(pnr1));
        assertEquals("19121212-1212", DaoUtil.formatPnrForPersistence(pnr2));
    }
}
