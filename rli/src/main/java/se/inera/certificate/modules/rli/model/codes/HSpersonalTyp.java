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
package se.inera.certificate.modules.rli.model.codes;

import org.apache.commons.lang3.StringUtils;

public enum HSpersonalTyp implements ICodeSystem {

    HSA_ID("1.2.752.129.2.1.4.1", "root");

    private static String codeSystemName = "HSA";

    private static String codeSystem = "1.2.752.129.2.1.4.1";

    private static String codeSystemVersion = null;

    private String code;

    private String description;

    private HSpersonalTyp(String code, String desc) {
        this.code = code;
        this.description = desc;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public String getCodeSystem() {
        return codeSystem;
    }

    @Override
    public String getCodeSystemName() {
        return codeSystemName;
    }

    @Override
    public String getCodeSystemVersion() {
        return codeSystemVersion;
    }

    public static HSpersonalTyp getFromCode(String code) {

        if (StringUtils.isBlank(code)) {
            return null;
        }

        for (HSpersonalTyp arrKod : values()) {
            if (arrKod.getCode().equals(code)) {
                return arrKod;
            }
        }

        return null;
    }

}
