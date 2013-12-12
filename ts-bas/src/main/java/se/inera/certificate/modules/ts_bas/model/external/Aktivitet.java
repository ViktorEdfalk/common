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
package se.inera.certificate.modules.ts_bas.model.external;

import se.inera.certificate.model.Id;
import se.inera.certificate.model.Kod;

/**
 * The aktivitet used by RLI. This class is a copy of the common external model (defined in se.inera.certificate.model),
 * extending with:
 * <ul>
 * <li> {@link #plats}
 * </ul>
 */
public class Aktivitet extends se.inera.certificate.model.Aktivitet {

    private Id id;

    private String plats;

    private Kod metod;
    
    private Kod aktivitetsstatus;

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public String getPlats() {
        return plats;
    }

    public void setPlats(String plats) {
        this.plats = plats;
    }

    public Kod getMetod() {
        return metod;
    }

    public void setMetod(Kod metod) {
        this.metod = metod;
    }
    
    public Kod getAktivitetsstatus() {
        return aktivitetsstatus;
    }

    public void setAktivitetsstatus(Kod aktivitetsstatus) {
        this.aktivitetsstatus = aktivitetsstatus;
    }
}
