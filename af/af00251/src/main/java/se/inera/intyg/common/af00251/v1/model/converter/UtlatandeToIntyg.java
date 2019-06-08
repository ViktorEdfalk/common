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
package se.inera.intyg.common.af00251.v1.model.converter;

import se.inera.intyg.common.af00251.support.AF00251EntryPoint;
import se.inera.intyg.common.af00251.v1.model.internal.AF00251UtlatandeV1;
import se.inera.intyg.common.af00251.v1.model.internal.ArbetsmarknadspolitisktProgram;
import se.inera.intyg.common.af00251.v1.model.internal.BegransningSjukfranvaro;
import se.inera.intyg.common.af00251.v1.model.internal.PrognosAtergang;
import se.inera.intyg.common.af00251.v1.model.internal.Sjukfranvaro;
import se.inera.intyg.common.support.model.InternalLocalDateInterval;
import se.inera.intyg.common.support.modules.converter.InternalConverterUtil;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.TypAvIntyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;

import java.util.ArrayList;
import java.util.List;

import static se.inera.intyg.common.af00251.v1.model.converter.AF00251RespConstants.*;
import static se.inera.intyg.common.support.Constants.KV_INTYGSTYP_CODE_SYSTEM;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.*;

public final class UtlatandeToIntyg {

    private static final String UNIT_HOUR = "h";
    private static final String UNIT_PERCENT = "%";

    private UtlatandeToIntyg() {
    }

    public static Intyg convert(AF00251UtlatandeV1 utlatande) {
        Intyg intyg = InternalConverterUtil.getIntyg(utlatande, false);
        intyg.setTyp(getTypAvIntyg(utlatande));
        intyg.getSvar()
             .addAll(getSvar(utlatande));
        intyg.setUnderskrift(InternalConverterUtil.base64StringToUnderskriftType(utlatande));
        return intyg;
    }

    private static TypAvIntyg getTypAvIntyg(AF00251UtlatandeV1 source) {
        TypAvIntyg typAvIntyg = new TypAvIntyg();
        typAvIntyg.setCode(source.getTyp()
                                 .toUpperCase());
        typAvIntyg.setCodeSystem(KV_INTYGSTYP_CODE_SYSTEM);
        typAvIntyg.setDisplayName(AF00251EntryPoint.MODULE_NAME);
        return typAvIntyg;
    }

    private static List<Svar> getSvar(AF00251UtlatandeV1 source) {
        List<Svar> svars = new ArrayList<>();

        int medicinsktUnderlagInstans = 1;
        if (source.getUndersokningsDatum() != null && source.getUndersokningsDatum().isValidDate()) {
            final UnderlagsTyp underlagsTyp = UnderlagsTyp.UNDERSOKNING;
            final SvarBuilder svarBuilder =
                aSvar(MEDICINSKUNDERLAG_SVAR_ID_1, medicinsktUnderlagInstans++)
                    .withDelsvar(MEDICINSKUNDERLAG_DELSVAR_ID_11, aCV(UnderlagsTyp.KODVERK, underlagsTyp.getId(), underlagsTyp.getLabel()))
                    .withDelsvar(MEDICINSKUNDERLAG_DELSVAR_ID_12, getInternalDateContent(source.getUndersokningsDatum()));

            svars.add(svarBuilder.build());
        }
        if (source.getAnnatDatum() != null && source.getAnnatDatum().isValidDate()) {
            final UnderlagsTyp underlagsTyp = UnderlagsTyp.ANNAT;

            final SvarBuilder svarBuilder =
                aSvar(MEDICINSKUNDERLAG_SVAR_ID_1,
                    medicinsktUnderlagInstans)
                    .withDelsvar(MEDICINSKUNDERLAG_DELSVAR_ID_11, aCV(UnderlagsTyp.KODVERK, underlagsTyp.getId(), underlagsTyp.getLabel()))
                    .withDelsvar(MEDICINSKUNDERLAG_DELSVAR_ID_12, getInternalDateContent(source.getAnnatDatum()))
                    .withDelsvar(MEDICINSKUNDERLAG_DELSVAR_ID_13, source.getAnnatBeskrivning());

            svars.add(svarBuilder.build());
        }


        if (source.getArbetsmarknadspolitisktProgram() != null) {
            final ArbetsmarknadspolitisktProgram program = source.getArbetsmarknadspolitisktProgram();
            final ArbetsmarknadspolitisktProgram.Omfattning omfattning = program.getOmfattning();
            if (omfattning != null) {
                final InternalConverterUtil.SvarBuilder svarBuilder = aSvar(ARBETSMARKNADSPOLITISKT_PROGRAM_SVAR_ID_2)
                        .withDelsvar(ARBETSMARKNADSPOLITISKT_PROGRAM_DELSVAR_ID_21, program.getMedicinskBedomning())
                        .withDelsvar(ARBETSMARKNADSPOLITISKT_PROGRAM_DELSVAR_ID_22,
                                aCV(ArbetsmarknadspolitisktProgram.Omfattning.KODVERK, omfattning.getId(), omfattning.getLabel()));
                if (program.getOmfattningDeltid() != null) {
                    svarBuilder.withDelsvar(ARBETSMARKNADSPOLITISKT_PROGRAM_DELSVAR_ID_23,
                            aPQ(UNIT_HOUR, Double.valueOf(program.getOmfattningDeltid())));
                }
                svars.add(svarBuilder.build());
            }
        }

        addIfNotBlank(svars, FUNKTIONSNEDSATTNING_SVAR_ID_3, FUNKTIONSNEDSATTNING_DELSVAR_ID_31, source.getFunktionsnedsattning());
        addIfNotBlank(svars, AKTIVITETSBEGRANSNING_SVAR_ID_4, AKTIVITETSBEGRANSNING_DELSVAR_ID_41, source.getAktivitetsbegransning());
        addIfNotNull(svars, FORHINDER_SVAR_ID_5, FORHINDER_DELSVAR_ID_51, source.getHarForhinder());

        int sjukfranvaroInstans = 1;
        for (Sjukfranvaro sjukfranvaro : source.getSjukfranvaro()) {
            if (sjukfranvaro.getChecked() != null && sjukfranvaro.getChecked()
                    && sjukfranvaro.getPeriod() != null && sjukfranvaro.getPeriod().isValid()) {
                final InternalLocalDateInterval period = sjukfranvaro.getPeriod();
                final InternalConverterUtil.SvarBuilder svarBuilder = aSvar(SJUKFRANVARO_SVAR_ID_6, sjukfranvaroInstans++);
                if (sjukfranvaro.getNiva() != null) {
                    svarBuilder.withDelsvar(SJUKFRANVARO_DELSVAR_ID_61, aPQ(UNIT_PERCENT, Double.valueOf(sjukfranvaro.getNiva())));
                }
                svarBuilder.withDelsvar(SJUKFRANVARO_DELSVAR_ID_62, aDatePeriod(period.fromAsLocalDate(), period.tomAsLocalDate()));
                svars.add(svarBuilder.build());
            }
        }

        if (source.getBegransningSjukfranvaro() != null) {
            final BegransningSjukfranvaro begransningSjukfranvaro = source.getBegransningSjukfranvaro();
            if (begransningSjukfranvaro.getKanBegransas() != null) {
                svars.add(aSvar(BEGRANSNING_SJUKFRANVARO_SVAR_ID_7)
                        .withDelsvar(BEGRANSNING_SJUKFRANVARO_DELSVAR_ID_71, begransningSjukfranvaro.getKanBegransas().toString())
                        .withDelsvar(BEGRANSNING_SJUKFRANVARO_DELSVAR_ID_72, begransningSjukfranvaro.getBeskrivning())
                        .build());
            }
        }

        if (source.getPrognosAtergang() != null) {
            final PrognosAtergang prognosAtergang = source.getPrognosAtergang();
            final PrognosAtergang.Prognos prognos = prognosAtergang.getPrognos();
            if (prognos != null) {
                svars.add(aSvar(PROGNOS_ATERGANG_SVAR_ID_8)
                        .withDelsvar(PROGNOS_ATERGANG_DELSVAR_ID_81, aCV(PrognosAtergang.Prognos.KODVERK, prognos.getId(),
                                prognos.getLabel()))
                        .withDelsvar(PROGNOS_ATERGANG_DELSVAR_ID_82, prognosAtergang.getAnpassningar())
                        .build());
            }
        }

        return svars;
    }

}
