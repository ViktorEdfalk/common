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
package se.inera.intyg.common.ag114.v1.model.converter;

import com.google.common.base.Strings;
import se.inera.intyg.common.ag114.support.Ag114EntryPoint;
import se.inera.intyg.common.ag114.v1.model.internal.Ag114UtlatandeV1;
import se.inera.intyg.common.ag114.v1.model.internal.Sysselsattning;
import se.inera.intyg.common.support.model.InternalLocalDateInterval;
import se.inera.intyg.common.support.modules.converter.InternalConverterUtil;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.TypAvIntyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;

import java.util.ArrayList;
import java.util.List;

import static se.inera.intyg.common.ag114.v1.model.converter.InternalToTransportUtil.handleDiagnosSvar;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.ANLEDNING_TILL_KONTAKT_DELSVAR_ID_9;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.ARBETSFORMAGA_TROTS_SJUKDOM_DELSVAR_ID_6_1;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.ARBETSFORMAGA_TROTS_SJUKDOM_DELSVAR_ID_6_2;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.ARBETSFORMAGA_TROTS_SJUKDOM_SVAR_ID_6;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.BEDOMNING_SVAR_ID_7;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.KONTAKT_ONSKAS_DELSVAR_ID_9;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.KONTAKT_ONSKAS_SVAR_ID_9;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.NEDSATT_ARBETSFORMAGA_DELSVAR_ID_5;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.NEDSATT_ARBETSFORMAGA_SVAR_ID_5;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.NUVARANDE_ARBETE_DELSVAR_ID_2;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.NUVARANDE_ARBETE_SVAR_ID_2;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.ONSKAR_FORMEDLA_DIAGNOS_DELSVAR_ID_3;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.ONSKAR_FORMEDLA_DIAGNOS_SVAR_ID_3;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.OVRIGT_DELSVAR_ID_8;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.OVRIGT_SVAR_ID_8;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.SJUKSKRIVNINGSGRAD_DELSVAR_ID_7_1;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.SJUKSKRIVNINGSPERIOD_DELSVAR_ID_7_2;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.TYP_AV_SYSSELSATTNING_CODE_SYSTEM;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.TYP_AV_SYSSELSATTNING_DELSVAR_ID_1;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.TYP_AV_SYSSELSATTNING_SVAR_ID_1;
import static se.inera.intyg.common.support.Constants.KV_INTYGSTYP_CODE_SYSTEM;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.aCV;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.aDatePeriod;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.aSvar;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.addIfNotBlank;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.addIfNotNull;

public final class UtlatandeToIntyg {

    private UtlatandeToIntyg() {
    }

    public static Intyg convert(Ag114UtlatandeV1 utlatande) {
        Intyg intyg = InternalConverterUtil.getIntyg(utlatande, false);
        intyg.setTyp(getTypAvIntyg(utlatande));
        intyg.getSvar().addAll(getSvar(utlatande));
        intyg.setUnderskrift(InternalConverterUtil.base64StringToUnderskriftType(utlatande));
        return intyg;
    }

    private static TypAvIntyg getTypAvIntyg(Ag114UtlatandeV1 source) {
        TypAvIntyg typAvIntyg = new TypAvIntyg();
        typAvIntyg.setCode(source.getTyp().toUpperCase());
        typAvIntyg.setCodeSystem(KV_INTYGSTYP_CODE_SYSTEM);
        typAvIntyg.setDisplayName(Ag114EntryPoint.MODULE_NAME);
        return typAvIntyg;
    }

    private static List<Svar> getSvar(Ag114UtlatandeV1 source) {
        List<Svar> svars = new ArrayList<>();

        // Kategori 1
        int sysselsattningInstans = 1;
        if (source.getSysselsattning() != null) {
            for (Sysselsattning sysselsattning : source.getSysselsattning()) {
                if (sysselsattning.getTyp() != null) {
                    svars.add(aSvar(TYP_AV_SYSSELSATTNING_SVAR_ID_1, sysselsattningInstans++)
                            .withDelsvar(TYP_AV_SYSSELSATTNING_DELSVAR_ID_1,
                                    aCV(TYP_AV_SYSSELSATTNING_CODE_SYSTEM, sysselsattning.getTyp().getId(),
                                            sysselsattning.getTyp().getLabel()))
                            .build());
                }
            }
        }
        addIfNotBlank(svars, NUVARANDE_ARBETE_SVAR_ID_2, NUVARANDE_ARBETE_DELSVAR_ID_2, source.getNuvarandeArbete());

        // Kategori 2 Diagnos
        addIfNotNull(svars, ONSKAR_FORMEDLA_DIAGNOS_SVAR_ID_3, ONSKAR_FORMEDLA_DIAGNOS_DELSVAR_ID_3, source.getOnskarFormedlaDiagnos());

        // Lägg endast till diagnos om önskar förmedla är true.
        if (source.getOnskarFormedlaDiagnos() != null && source.getOnskarFormedlaDiagnos()) {
            handleDiagnosSvar(svars, source.getDiagnoser());
        }

        // Kategori 5 Arbetsformaga
        addIfNotBlank(svars, NEDSATT_ARBETSFORMAGA_SVAR_ID_5, NEDSATT_ARBETSFORMAGA_DELSVAR_ID_5, source.getNedsattArbetsformaga());

        // Kategori 6 Arbetsformaga trots sjukdom
        if (source.getArbetsformagaTrotsSjukdom() != null) {
            if (!source.getArbetsformagaTrotsSjukdom()) {
                svars.add(aSvar(ARBETSFORMAGA_TROTS_SJUKDOM_SVAR_ID_6).withDelsvar(ARBETSFORMAGA_TROTS_SJUKDOM_DELSVAR_ID_6_1,
                        source.getArbetsformagaTrotsSjukdom().toString()).build());
            } else if (source.getArbetsformagaTrotsSjukdom() && !Strings.isNullOrEmpty(source.getArbetsformagaTrotsSjukdomBeskrivning())) {
                svars.add(aSvar(ARBETSFORMAGA_TROTS_SJUKDOM_SVAR_ID_6).withDelsvar(ARBETSFORMAGA_TROTS_SJUKDOM_DELSVAR_ID_6_1,
                        source.getArbetsformagaTrotsSjukdom().toString()).withDelsvar(ARBETSFORMAGA_TROTS_SJUKDOM_DELSVAR_ID_6_2,
                                source.getArbetsformagaTrotsSjukdomBeskrivning()).build());
            }
        }

        // Kategori Behov av sjukskrivning
        InternalLocalDateInterval sjukskrivningsperiod = source.getSjukskrivningsperiod();
        if (sjukskrivningsperiod != null) {
            svars.add(aSvar(BEDOMNING_SVAR_ID_7)
                    .withDelsvar(SJUKSKRIVNINGSGRAD_DELSVAR_ID_7_1, source.getSjukskrivningsgrad())
                    .withDelsvar(SJUKSKRIVNINGSPERIOD_DELSVAR_ID_7_2,
                            aDatePeriod(sjukskrivningsperiod.fromAsLocalDate(), sjukskrivningsperiod.tomAsLocalDate()))
                    .build());
        }

        // Kategori 8 övrigt
        addIfNotBlank(svars, OVRIGT_SVAR_ID_8, OVRIGT_DELSVAR_ID_8, source.getOvrigaUpplysningar());

        // Kategori 9 Kontakt
        if (source.getKontaktMedArbetsgivaren() != null) {
            if (source.getKontaktMedArbetsgivaren() && !Strings.nullToEmpty(source.getAnledningTillKontakt()).trim().isEmpty()) {
                svars.add(aSvar(KONTAKT_ONSKAS_SVAR_ID_9).withDelsvar(KONTAKT_ONSKAS_DELSVAR_ID_9,
                        source.getKontaktMedArbetsgivaren().toString())
                        .withDelsvar(ANLEDNING_TILL_KONTAKT_DELSVAR_ID_9, source.getAnledningTillKontakt()).build());
            } else {
                svars.add(aSvar(KONTAKT_ONSKAS_SVAR_ID_9).withDelsvar(KONTAKT_ONSKAS_DELSVAR_ID_9,
                        source.getKontaktMedArbetsgivaren().toString())
                        .build());
            }
        }

        return svars;
    }
}