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

public enum Diagnoskodverk {

    // TODO Move snomed_CT to its own enum
    SNOMED_CT("1.2.752.116.2.1.1.1", "1.2.752.116.2.1.1.1", "SNOMED-CT", null),

    ICD_10_SE("1.2.752.116.1.1.1.1.3", "1.2.752.116.1.1.1.1.3", "ICD-10", null),

    KSH_97_P("1.2.752.116.1.3.1.4.1", "1.2.752.116.1.3.1.1.2", "KSH97-P", null);

    Diagnoskodverk(String codeSystemV1, String codeSystemV2, String codeSystemName, String codeSystemVersion) {
        this.codeSystemV1 = codeSystemV1;
        this.codeSystemV2 = codeSystemV2;
        this.codeSystemName = codeSystemName;
        this.codeSystemVersion = codeSystemVersion;
    }

    private final String codeSystemV1;
    private final String codeSystemV2;

    private final String codeSystemName;

    private final String codeSystemVersion;

    public String getCodeSystem(boolean old) {
        return old ? codeSystemV1 : codeSystemV2;
    }

    public String getCodeSystemName() {
        return codeSystemName;
    }

    public String getCodeSystemVersion() {
        return codeSystemVersion;
    }

    public static Diagnoskodverk getEnumByCodeSystem(String oid, boolean old) {
        for (Diagnoskodverk kodverk : Diagnoskodverk.values()) {
            if (kodverk.getCodeSystem(old).equals(oid)) {
                return kodverk;
            }
        }
        return null;
    }
}
