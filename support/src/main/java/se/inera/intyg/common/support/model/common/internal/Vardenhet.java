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
package se.inera.intyg.common.support.model.common.internal;

import java.util.Objects;

public class Vardenhet {

    private String enhetsid;

    private String enhetsnamn;

    private String postadress;

    private String postnummer;

    private String postort;

    private String telefonnummer;

    private String epost;

    private Vardgivare vardgivare;

    private String arbetsplatsKod;

    public Vardenhet() {

    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (getClass() != object.getClass()) {
            return false;
        }
        final Vardenhet that = (Vardenhet) object;
        return Objects.equals(this.enhetsid, that.enhetsid) &&
                Objects.equals(this.enhetsnamn, that.enhetsnamn) &&
                Objects.equals(this.postadress, that.postadress) &&
                Objects.equals(this.postnummer, that.postnummer) &&
                Objects.equals(this.postort, that.postort) &&
                Objects.equals(this.telefonnummer, that.telefonnummer) &&
                Objects.equals(this.epost, that.epost) &&
                Objects.equals(this.vardgivare, that.vardgivare) &&
                Objects.equals(this.arbetsplatsKod, that.arbetsplatsKod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.enhetsid, this.enhetsnamn, this.postadress, this.postnummer, this.postort,
                this.telefonnummer, this.epost, this.vardgivare, this.arbetsplatsKod);
    }

    public String getEnhetsid() {
        return enhetsid;
    }

    public void setEnhetsid(String enhetsId) {
        this.enhetsid = enhetsId;
    }

    public String getEnhetsnamn() {
        return enhetsnamn;
    }

    public void setEnhetsnamn(String enhetsNamn) {
        this.enhetsnamn = enhetsNamn;
    }

    public String getPostadress() {
        return postadress;
    }

    public void setPostadress(String postadress) {
        this.postadress = postadress;
    }

    public String getPostnummer() {
        return postnummer;
    }

    public void setPostnummer(String postNummer) {
        this.postnummer = postNummer;
    }

    public String getPostort() {
        return postort;
    }

    public void setPostort(String postOrt) {
        this.postort = postOrt;
    }

    public String getTelefonnummer() {
        return telefonnummer;
    }

    public void setTelefonnummer(String telefonNummer) {
        this.telefonnummer = telefonNummer;
    }

    public String getEpost() {
        return epost;
    }

    public void setEpost(String epost) {
        this.epost = epost;
    }

    public Vardgivare getVardgivare() {
        return vardgivare;
    }

    public void setVardgivare(Vardgivare vardgivare) {
        this.vardgivare = vardgivare;
    }

    public String getArbetsplatsKod() {
        return arbetsplatsKod;
    }

    public void setArbetsplatsKod(String arbetsplatsKod) {
        this.arbetsplatsKod = arbetsplatsKod;
    }
}
