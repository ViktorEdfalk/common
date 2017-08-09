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
package se.inera.intyg.common.sos_doi.model.internal;

import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_DATUM_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_OM_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_SPECIFIKATION_JSON_ID;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

import se.inera.intyg.common.support.model.InternalDate;

@AutoValue
public abstract class Foljd {

    @JsonCreator
    public static Foljd create(@JsonProperty(FOLJD_OM_JSON_ID) String beskrivning,
            @JsonProperty(FOLJD_DATUM_JSON_ID) InternalDate datum,
            @JsonProperty(FOLJD_SPECIFIKATION_JSON_ID) Specifikation specifikation) {
        return new AutoValue_Foljd(beskrivning, datum, specifikation);
    }

    @Nullable
    public abstract String getBeskrivning();

    @Nullable
    public abstract InternalDate getDatum();

    @Nullable
    public abstract Specifikation getSpecifikation();
}