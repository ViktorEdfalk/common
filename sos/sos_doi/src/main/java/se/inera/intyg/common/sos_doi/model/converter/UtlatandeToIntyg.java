package se.inera.intyg.common.sos_doi.model.converter;

import static se.inera.intyg.common.sos_parent.model.converter.SosUtlatandeToIntyg.getSharedSvar;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BIDRAGANDE_SJUKDOM_DATUM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BIDRAGANDE_SJUKDOM_OM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BIDRAGANDE_SJUKDOM_SPECIFIKATION_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BIDRAGANDE_SJUKDOM_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSORSAK_DATUM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSORSAK_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSORSAK_SPECIFIKATION_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSORSAK_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_DATUM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_OM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_SPECIFIKATION_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_DATUM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_OM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_ORSAK_CODE_SYSTEM;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_ORSAK_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_UPPKOMMELSE_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.GRUNDER_CODE_SYSTEM;
import static se.inera.intyg.common.sos_parent.support.RespConstants.GRUNDER_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.GRUNDER_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.LAND_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.LAND_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.OPERATION_ANLEDNING_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.OPERATION_DATUM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.OPERATION_OM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.OPERATION_SVAR_ID;
import static se.inera.intyg.common.support.Constants.KV_INTYGSTYP_CODE_SYSTEM;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.aCV;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.aSvar;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.addIfNotBlank;

import java.util.List;

import se.inera.intyg.common.sos_doi.model.internal.BidragandeSjukdom;
import se.inera.intyg.common.sos_doi.model.internal.Dodsorsaksgrund;
import se.inera.intyg.common.sos_doi.model.internal.DodsorsaksintygUtlatande;
import se.inera.intyg.common.sos_doi.model.internal.Foljd;
import se.inera.intyg.common.sos_doi.support.DodsorsaksintygModuleEntryPoint;
import se.inera.intyg.common.support.common.enumerations.Diagnoskodverk;
import se.inera.intyg.common.support.model.common.internal.Tillaggsfraga;
import se.inera.intyg.common.support.modules.converter.InternalConverterUtil;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.TypAvIntyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;

public final class UtlatandeToIntyg {
    private UtlatandeToIntyg() {
    }

    public static Intyg convert(DodsorsaksintygUtlatande utlatande) {
        Intyg intyg = InternalConverterUtil.getIntyg(utlatande);
        intyg.setTyp(getTypAvIntyg(utlatande));
        intyg.getSvar().addAll(getSvar(utlatande));
        return intyg;
    }

    private static TypAvIntyg getTypAvIntyg(DodsorsaksintygUtlatande utlatande) {
        TypAvIntyg typAvIntyg = new TypAvIntyg();
        typAvIntyg.setCode(utlatande.getTyp());
        typAvIntyg.setCodeSystem(KV_INTYGSTYP_CODE_SYSTEM);
        typAvIntyg.setDisplayName(DodsorsaksintygModuleEntryPoint.MODULE_NAME);
        return typAvIntyg;
    }

    private static List<Svar> getSvar(DodsorsaksintygUtlatande utlatande) {
        List<Svar> svar = getSharedSvar(utlatande);

        // Svar 8
        if (utlatande.getDodsorsak() != null || utlatande.getDodsorsakDatum() != null || utlatande.getDodsorsakSpecifikation() != null) {
            InternalConverterUtil.SvarBuilder dodsorsak = aSvar(DODSORSAK_SVAR_ID);
            if (utlatande.getDodsorsak() != null) {
                dodsorsak.withDelsvar(DODSORSAK_DELSVAR_ID, utlatande.getDodsorsak());
            }
            if (utlatande.getDodsorsakDatum() != null) {
                dodsorsak.withDelsvar(DODSORSAK_DATUM_DELSVAR_ID, utlatande.getDodsorsakDatum().asLocalDate().toString());
            }
            if (utlatande.getDodsorsakSpecifikation() != null) {
                dodsorsak.withDelsvar(DODSORSAK_SPECIFIKATION_DELSVAR_ID, aCV(Diagnoskodverk.SNOMED_CT.getCodeSystem(),
                        utlatande.getDodsorsakSpecifikation().getId(), utlatande.getDodsorsakSpecifikation().getLabel()));
            }
            svar.add(dodsorsak.build());
        }

        // Svar 9
        if (utlatande.getFoljd() != null && !utlatande.getFoljd().isEmpty()) {
            for (Foljd foljd : utlatande.getFoljd()) {
                if (foljd.getBeskrivning() != null || foljd.getDatum() != null || foljd.getSpecifikation() != null) {
                    InternalConverterUtil.SvarBuilder foljdSvar = aSvar(FOLJD_SVAR_ID);
                    if (foljd.getBeskrivning() != null) {
                        foljdSvar.withDelsvar(FOLJD_OM_DELSVAR_ID, foljd.getBeskrivning());
                    }
                    if (foljd.getDatum() != null) {
                        foljdSvar.withDelsvar(FOLJD_DATUM_DELSVAR_ID, foljd.getDatum().asLocalDate().toString());
                    }
                    if (foljd.getSpecifikation() != null) {
                        foljdSvar.withDelsvar(FOLJD_SPECIFIKATION_DELSVAR_ID, aCV(Diagnoskodverk.SNOMED_CT.getCodeSystem(),
                                foljd.getSpecifikation().getId(), foljd.getSpecifikation().getLabel()));

                    }
                    svar.add(foljdSvar.build());
                }
            }
        }

        // Svar 10
        if (utlatande.getBidragandeSjukdomar() != null && !utlatande.getBidragandeSjukdomar().isEmpty()) {
            for (BidragandeSjukdom bidragandeSjukdom : utlatande.getBidragandeSjukdomar()) {
                if (bidragandeSjukdom.getBeskrivning() != null || bidragandeSjukdom.getDatum() != null
                        || bidragandeSjukdom.getSpecifikation() != null) {
                    InternalConverterUtil.SvarBuilder sjukdomSvar = aSvar(BIDRAGANDE_SJUKDOM_SVAR_ID);
                    if (bidragandeSjukdom.getBeskrivning() != null) {
                        sjukdomSvar.withDelsvar(BIDRAGANDE_SJUKDOM_OM_DELSVAR_ID, bidragandeSjukdom.getBeskrivning());
                    }
                    if (bidragandeSjukdom.getDatum() != null) {
                        sjukdomSvar.withDelsvar(BIDRAGANDE_SJUKDOM_DATUM_DELSVAR_ID, bidragandeSjukdom.getDatum().asLocalDate().toString());
                    }
                    if (bidragandeSjukdom.getSpecifikation() != null) {
                        sjukdomSvar.withDelsvar(BIDRAGANDE_SJUKDOM_SPECIFIKATION_DELSVAR_ID, aCV(Diagnoskodverk.SNOMED_CT.getCodeSystem(),
                                bidragandeSjukdom.getSpecifikation().getId(), bidragandeSjukdom.getSpecifikation().getLabel()));
                    }
                    svar.add(sjukdomSvar.build());
                }
            }
        }

        // Svar 11
        InternalConverterUtil.SvarBuilder operation = aSvar(OPERATION_SVAR_ID).withDelsvar(OPERATION_OM_DELSVAR_ID,
                utlatande.getOperation().toString());
        if (utlatande.getOperationDatum() != null) {
            operation.withDelsvar(OPERATION_DATUM_DELSVAR_ID, utlatande.getOperationDatum().asLocalDate().toString());
        }
        if (utlatande.getOperationAnledning() != null) {
            operation.withDelsvar(OPERATION_ANLEDNING_DELSVAR_ID, utlatande.getOperationAnledning());
        }
        svar.add(operation.build());

        // Svar 12
        if (utlatande.getForgiftning() != null || utlatande.getForgiftningOrsak() != null || utlatande.getForgiftningDatum() != null
                || utlatande.getForgiftningUppkommelse() != null) {
            InternalConverterUtil.SvarBuilder forgiftning = aSvar(FORGIFTNING_SVAR_ID);
            if (utlatande.getForgiftning() != null) {
                forgiftning.withDelsvar(FORGIFTNING_OM_DELSVAR_ID, utlatande.getForgiftning().toString());
            }
            if (utlatande.getForgiftningOrsak() != null) {
                forgiftning.withDelsvar(FORGIFTNING_ORSAK_DELSVAR_ID,
                        aCV(FORGIFTNING_ORSAK_CODE_SYSTEM, utlatande.getForgiftningOrsak().name(), utlatande.getForgiftningOrsak().name()));
            }
            if (utlatande.getForgiftningDatum() != null) {
                forgiftning.withDelsvar(FORGIFTNING_DATUM_DELSVAR_ID, utlatande.getForgiftningDatum().asLocalDate().toString());
            }
            if (utlatande.getForgiftningUppkommelse() != null) {
                forgiftning.withDelsvar(FORGIFTNING_UPPKOMMELSE_DELSVAR_ID, utlatande.getForgiftningUppkommelse());
            }
            svar.add(forgiftning.build());
        }

        // Svar 13
        if (utlatande.getGrunder() != null && !utlatande.getGrunder().isEmpty()) {
            for (Dodsorsaksgrund grund : utlatande.getGrunder()) {
                svar.add(aSvar(GRUNDER_SVAR_ID).withDelsvar(GRUNDER_DELSVAR_ID, aCV(GRUNDER_CODE_SYSTEM, grund.name(), grund.name()))
                        .build());
            }
        }

        // Svar 14
        addIfNotBlank(svar, LAND_SVAR_ID, LAND_DELSVAR_ID, utlatande.getLand());

        for (Tillaggsfraga tillaggsfraga : utlatande.getTillaggsfragor()) {
            addIfNotBlank(svar, tillaggsfraga.getId(), tillaggsfraga.getId() + ".1", tillaggsfraga.getSvar());
        }

        return svar;
    }
}
