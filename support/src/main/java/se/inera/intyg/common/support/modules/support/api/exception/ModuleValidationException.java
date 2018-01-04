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
package se.inera.intyg.common.support.modules.support.api.exception;

import java.util.Collections;
import java.util.List;

import com.google.common.base.Joiner;

public class ModuleValidationException extends ModuleException {

    private static final long serialVersionUID = -4808921021590102478L;

    private final List<String> validationEntries;

    public ModuleValidationException(List<String> validationEntries) {
        super(Joiner.on(',').join(validationEntries));
        this.validationEntries = validationEntries;
    }

    public ModuleValidationException(List<String> validationEntries, String message, Throwable cause) {
        super(message, cause);
        this.validationEntries = validationEntries;
    }

    public ModuleValidationException(List<String> validationEntries, String message) {
        super(message);
        this.validationEntries = validationEntries;
    }

    public ModuleValidationException(List<String> validationEntries, Throwable cause) {
        super(Joiner.on(',').join(validationEntries), cause);
        this.validationEntries = validationEntries;
    }

    public List<String> getValidationEntries() {
        return Collections.unmodifiableList(validationEntries);
    }
}
