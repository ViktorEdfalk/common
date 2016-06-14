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
package se.inera.intyg.common.support.common.enumerations;

import java.util.Optional;
import java.util.stream.Stream;

public enum SpecialistkompetensKod {

    // TODO add more values
    ALLERGI("1001", "Allergi"),
    BARN_UNGDOMSKIRURGI("1006", "Barn och ungdoms kirurgi");

    private String code;

    private String description;

    SpecialistkompetensKod(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return this.code;
    }

    public String getDescription() {
        return this.description;
    }

    /**
     * Try to get a description from the given code.
     */
    public static Optional<String> getDescriptionFromCode(String code) {
        return Stream.of(SpecialistkompetensKod.values())
                .filter(s -> code.equals(s.getCode()))
                .findFirst().map(s -> s.getDescription());
    }

    /**
     * Try to get a code from the given description.
     */
    public static Optional<String> getCodeFromDescription(String description) {
        return Stream.of(SpecialistkompetensKod.values())
                .filter(s -> description.equals(s.getDescription()))
                .findFirst().map(s -> s.getCode());
    }
}
