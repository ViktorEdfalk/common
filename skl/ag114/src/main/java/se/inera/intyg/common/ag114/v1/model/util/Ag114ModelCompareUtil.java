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
package se.inera.intyg.common.ag114.v1.model.util;

import org.springframework.stereotype.Component;

import se.inera.intyg.common.ag114.v1.model.internal.Ag114UtlatandeV1;
import se.inera.intyg.common.agparent.model.util.AgParentModelCompareUtil;
import se.inera.intyg.common.support.model.util.ModelCompareUtil;

@Component(value = "ag114.Ag114ModelCompareUtil.v1")
public class Ag114ModelCompareUtil extends AgParentModelCompareUtil implements ModelCompareUtil<Ag114UtlatandeV1> {

    @Override
    public boolean isValidForNotification(Ag114UtlatandeV1 utlatande) {
        return true;
    }

}
