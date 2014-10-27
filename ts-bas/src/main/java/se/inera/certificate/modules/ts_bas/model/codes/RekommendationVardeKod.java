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
package se.inera.certificate.modules.ts_bas.model.codes;

import se.inera.certificate.model.Kod;
import se.inera.certificate.model.common.codes.CodeConverter;
import se.inera.certificate.model.common.codes.CodeSystem;

/**
 * Represents all the codes used by this module to define HoSPersonal.
 */
public enum RekommendationVardeKod implements CodeSystem {

    /**
     * Medeltung lastbil (C1).
     */
    C1("VAR1", "Medeltung lastbil"),

    /**
     * Medeltung lastbil med tungt släpfordon (C1E).
     */
    C1E("VAR2", "Medeltung lastbil med tungt släpfordon"),

    /**
     * Tung lastbil (C).
     */
    C("VAR3", "Tung lastbil"),

    /**
     * Tung lastbil med tungt släpfordon (CE).
     */
    CE("VAR4", "Tung lastbil med tungt släpfordon"),

    /**
     * Mellanstor buss (D1).
     */
    D1("VAR5", "Mellanstor buss"),

    /**
     * Mellanstor buss med tungt släpfordon (D1E).
     */
    D1E("VAR6", "Mellanstor buss med tungt släpfordon"),

    /**
     * Buss (D).
     */
    D("VAR7", "Buss"),

    /**
     * Buss med tungt släpfordon (DE).
     */
    DE("VAR8", "Buss med tungt släpfordon"),

    /**
     * Taxi (TAXI).
     */
    TAXI("VAR9", "Taxi"),

    /**
     * Intyget avser inget av ovanstående (ANNAT).
     */
    ANNAT("VAR10", "Intyget avser inget av ovanstående"),

    /**
     * Kan inte ta ställning.
     */
    INTE_TA_STALLNING("VAR11", "Kan inte ta ställning");

    private static String codeSystemName = "kv_värde";

    private static String codeSystem = "e889fa20-1dee-4f79-8b37-03853e75a9f8";

    private static String codeSystemVersion = null;

    private String code;

    private String description;

    private RekommendationVardeKod(String code, String desc) {
        this.code = code;
        this.description = desc;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCode() {
        return this.code;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
        return this.description;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCodeSystem() {
        return codeSystem;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCodeSystemName() {
        return codeSystemName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCodeSystemVersion() {
        return codeSystemVersion;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean matches(Kod kod) {
        return CodeConverter.matches(this, kod);
    }
}
