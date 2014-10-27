package se.inera.certificate.modules.ts_diabetes.model.codes;

import se.inera.certificate.model.Kod;
import se.inera.certificate.model.common.codes.CodeConverter;
import se.inera.certificate.model.common.codes.CodeSystem;

public enum VardkontakttypKod implements CodeSystem {

    /** "5880005", "Min undersökning med patienten". */
    MIN_UNDERSOKNING("5880005", "Min undersökning med patienten"),

    /** "185317003", "Min telefonkontakt med patienten". */
    MIN_TELEFONKONTAKT("185317003", "Min telefonkontakt med patienten");

    private static final String CODE_SYSTEM_NAME = "SNOMED-CT";

    private static final String CODE_SYSTEM = "1.2.752.116.2.1.1.1";

    private static final String CODE_SYSTEM_VERSION = null;

    private final String code;

    private final String description;

    private VardkontakttypKod(String code, String desc) {
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
        return CODE_SYSTEM;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCodeSystemName() {
        return CODE_SYSTEM_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCodeSystemVersion() {
        return CODE_SYSTEM_VERSION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean matches(Kod kod) {
        return CodeConverter.matches(this, kod);
    }
}
