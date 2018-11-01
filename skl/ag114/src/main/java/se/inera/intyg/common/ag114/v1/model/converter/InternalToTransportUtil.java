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

import se.inera.intyg.common.ag114.v1.model.internal.Diagnos;
import se.inera.intyg.common.support.common.enumerations.Diagnoskodverk;
import se.inera.intyg.common.support.modules.converter.InternalConverterUtil.SvarBuilder;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;

import java.util.List;

import static se.inera.intyg.common.agparent.model.converter.RespConstants.TYP_AV_BIDIAGNOS_1_BESKRIVNING_DELSVAR_ID_4;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.TYP_AV_BIDIAGNOS_1_DELSVAR_ID_4;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.TYP_AV_BIDIAGNOS_2_BESKRIVNING_DELSVAR_ID_4;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.TYP_AV_BIDIAGNOS_2_DELSVAR_ID_4;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.TYP_AV_DIAGNOS_BESKRIVNING_DELSVAR_ID_4;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.TYP_AV_DIAGNOS_DELSVAR_ID_4;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.TYP_AV_DIAGNOS_SVAR_ID_4;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.aCV;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.aSvar;

public final class InternalToTransportUtil {
    private InternalToTransportUtil() {
    }

    public static void handleDiagnosSvar(List<Svar> svars, List<Diagnos> diagnoser) {
        SvarBuilder diagnosSvar = aSvar(TYP_AV_DIAGNOS_SVAR_ID_4);
        for (int i = 0; i < diagnoser.size(); i++) {
            Diagnos diagnos = diagnoser.get(i);
            if (diagnos.getDiagnosKod() == null) {
                continue;
            }
            Diagnoskodverk diagnoskodverk = Diagnoskodverk.valueOf(diagnos.getDiagnosKodSystem());
            switch (i) {
            case 0:
                diagnosSvar.withDelsvar(TYP_AV_DIAGNOS_DELSVAR_ID_4,
                        aCV(diagnoskodverk.getCodeSystem(), diagnos.getDiagnosKod(), diagnos.getDiagnosDisplayName()))
                        .withDelsvar(TYP_AV_DIAGNOS_BESKRIVNING_DELSVAR_ID_4, diagnos.getDiagnosBeskrivning());
                break;
            case 1:
                diagnosSvar.withDelsvar(TYP_AV_BIDIAGNOS_1_DELSVAR_ID_4,
                        aCV(diagnoskodverk.getCodeSystem(), diagnos.getDiagnosKod(), diagnos.getDiagnosDisplayName()))
                        .withDelsvar(TYP_AV_BIDIAGNOS_1_BESKRIVNING_DELSVAR_ID_4, diagnos.getDiagnosBeskrivning());
                break;
            case 2:
                diagnosSvar.withDelsvar(TYP_AV_BIDIAGNOS_2_DELSVAR_ID_4,
                        aCV(diagnoskodverk.getCodeSystem(), diagnos.getDiagnosKod(), diagnos.getDiagnosDisplayName()))
                        .withDelsvar(TYP_AV_BIDIAGNOS_2_BESKRIVNING_DELSVAR_ID_4, diagnos.getDiagnosBeskrivning());
                break;
            default:
                throw new IllegalArgumentException();
            }
        }
        if (!diagnosSvar.delSvars.isEmpty()) {
            svars.add(diagnosSvar.build());
        }
    }
}