/**
 * Copyright (C) 2013 Inera AB (http://www.inera.se)
 *
 * This file is part of Inera Certificate Modules (http://code.google.com/p/inera-certificate-modules).
 *
 * Inera Certificate Modules is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Inera Certificate Modules is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.certificate.modules.rli.model.external;

import java.util.ArrayList;
import java.util.List;

/**
 * The aktivitet used by RLI. This class is a copy of the common external model (defined in se.inera.intyg.common.support.model),
 * extending with:
 * <ul>
 * <li> {@link #plats}
 * </ul>
 *
 * @author Gustav Norbäcker, R2M
 */
public class Aktivitet extends se.inera.intyg.common.support.model.Aktivitet {

    private List<Utforarroll> beskrivsAv;

    private Vardenhet utforsVid;

    private String plats;

    @Override
    public List<Utforarroll> getBeskrivsAv() {
        if (beskrivsAv == null) {
            beskrivsAv = new ArrayList<>();
        }
        return beskrivsAv;
    }

    @Override
    public Vardenhet getUtforsVid() {
        return utforsVid;
    }

    public void setUtforsVid(Vardenhet utforsVid) {
        this.utforsVid = utforsVid;
    }

    public String getPlats() {
        return plats;
    }

    public void setPlats(String plats) {
        this.plats = plats;
    }
}
