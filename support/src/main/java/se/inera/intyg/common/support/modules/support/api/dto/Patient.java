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

package se.inera.intyg.common.support.modules.support.api.dto;

import org.apache.commons.lang3.StringUtils;

import static org.springframework.util.Assert.hasText;
import static org.springframework.util.Assert.notNull;

public class Patient {

    private final String fornamn;

    private final String mellannamn;

    private final String efternamn;

    private final Personnummer personnummer;

    private final String postadress;

    private final String postnummer;

    private final String postort;

    public Patient(String fornamn, String mellannamn, String efternamn, Personnummer personnummer, String postadress, String postnummer,
            String postort) {
        hasText(fornamn, "'fornamn' must not be empty");
        hasText(efternamn, "'efternamn' must not be empty");
        notNull(personnummer, "'personnummer' must not be null");
        hasText(personnummer.getPersonnummer(), "'personnummer' must not be empty");
        this.fornamn = fornamn;
        this.mellannamn = mellannamn;
        this.efternamn = efternamn;
        this.personnummer = personnummer;
        this.postadress = postadress;
        this.postnummer = postnummer;
        this.postort = postort;
    }

    public String getFornamn() {
        return fornamn;
    }

    public String getMellannamn() {
        return mellannamn;
    }

    public String getEfternamn() {
        return efternamn;
    }

    public Personnummer getPersonnummer() {
        return personnummer;
    }

    public String getPostadress() {
        return postadress;
    }

    public String getPostnummer() {
        return postnummer;
    }

    public String getPostort() {
        return postort;
    }

    public String getFullstandigtNamn() {
        if (StringUtils.isBlank(mellannamn)) {
            return fornamn + " " + efternamn;
        } else {
            return fornamn + " " + mellannamn + " " + efternamn;
        }
    }
}
