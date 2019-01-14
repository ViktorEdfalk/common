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
package se.inera.intyg.common.tstrk1009.v1.model.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.auto.value.AutoValue;
import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Set;
import se.inera.intyg.common.ts_parent.json.AbstractEnumSetDeserializer;
import se.inera.intyg.common.ts_parent.json.AbstractEnumSetSerializer;

@AutoValue
public abstract class IntygetAvserBehorigheter {

    @JsonCreator
    public static IntygetAvserBehorigheter create(@JsonProperty("behorigheter") EnumSet<IntygetAvserBehorighet> behorigheterToCreate) {

        final EnumSet<IntygetAvserBehorighet> behorigheter = (behorigheterToCreate == null)
                ? EnumSet.noneOf(IntygetAvserBehorighet.class)
                : behorigheterToCreate;

        return new AutoValue_IntygetAvserBehorigheter(behorigheter);
    }

    @Nullable
    @JsonSerialize(using = IntygetAvserBehorigheterEnumSetSerializer.class)
    @JsonDeserialize(using = IntygetAvserBehorigheterEnumSetDeserializer.class)
    public abstract Set<IntygetAvserBehorighet> getBehorigheter();

    public static class IntygetAvserBehorigheterEnumSetSerializer extends AbstractEnumSetSerializer<IntygetAvserBehorighet> {
        protected IntygetAvserBehorigheterEnumSetSerializer() {
            super(IntygetAvserBehorighet.class);
        }
    }

    public static class IntygetAvserBehorigheterEnumSetDeserializer extends AbstractEnumSetDeserializer<IntygetAvserBehorighet> {
        protected IntygetAvserBehorigheterEnumSetDeserializer() {
            super(IntygetAvserBehorighet.class);
        }
    }
}