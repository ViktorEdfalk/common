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
package se.inera.intyg.common.ts_tstrk1062.v1.model.converter;

import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.*;
import static se.inera.intyg.common.ts_tstrk1062.v1.model.converter.RespConstants.*;

import java.util.EnumSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.converter.TransportConverterUtil;
import se.inera.intyg.common.ts_parent.codes.IdKontrollKod;
import se.inera.intyg.common.ts_parent.codes.IntygAvserKod;
import se.inera.intyg.common.ts_tstrk1062.v1.model.internal.*;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar.Delsvar;

import javax.annotation.Nullable;

public final class TransportToInternal {

    private static final Logger LOG = LoggerFactory.getLogger(InternalToTransport.class);

    private TransportToInternal() {
    }

    public static TsTstrk1062UtlatandeV1 convert(Intyg source) throws ConverterException {
        TsTstrk1062UtlatandeV1.Builder utlatande = TsTstrk1062UtlatandeV1.builder();
        utlatande.setId(source.getIntygsId().getExtension());
        utlatande.setGrundData(TransportConverterUtil.getGrundData(source, true));
        utlatande.setTextVersion(source.getVersion());
        utlatande.setSignature(TransportConverterUtil.signatureTypeToBase64(source.getUnderskrift()));
        setSvar(utlatande, source);
        return utlatande.build();
    }

    private static void setSvar(TsTstrk1062UtlatandeV1.Builder utlatande, Intyg source) throws ConverterException {
        EnumSet<IntygAvserKategori> intygAvserSet = EnumSet.noneOf(IntygAvserKategori.class);

        for (Svar svar : source.getSvar()) {
            switch (svar.getId()) {
                case INTYG_AVSER_SVAR_ID_1:
                    handleIntygAvser(utlatande, svar, intygAvserSet);
                    break;
                case ID_KONTROLL_SVAR_ID_1:
                    handleIdKontroll(utlatande, svar);
                    break;
                case ALLMANT_DIAGNOSKOD_KODAD_ALLMANT_SVAR_ID:
                    handleDiagnosKodad(utlatande, svar);
                    break;
                case ALLMANT_DIAGNOSKOD_FRITEXT_ALLMANT_SVAR_ID:
                    handleDiagnosFritext(utlatande, svar);
                    break;
            }
        }

        utlatande.setIntygAvser(IntygAvser.create(intygAvserSet));
    }

    private static void handleIntygAvser(TsTstrk1062UtlatandeV1.Builder utlatande, Svar svar,
                                         EnumSet<IntygAvserKategori> intygAvserSet) throws ConverterException {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case INTYG_AVSER_DELSVAR_ID_1:
                    intygAvserSet.add(IntygAvserKategori.valueOf(IntygAvserKod.fromCode(getCVSvarContent(delsvar).getCode()).name()));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleIdKontroll(TsTstrk1062UtlatandeV1.Builder utlatande, Svar svar) throws ConverterException {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case ID_KONTROLL_DELSVAR_ID_1:
                    final IdKontroll idKontroll = IdKontroll.create(IdKontrollKod.fromCode(getCVSvarContent(delsvar).getCode()));
                    utlatande.setIdKontroll(idKontroll);
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleDiagnosKodad(TsTstrk1062UtlatandeV1.Builder utlatande, Svar svar) {
        utlatande.setDiagnosRegistrering(DiagnosRegistrering.create(DiagnosRegistrering.DiagnosRegistreringsTyp.DIAGNOS_KODAD));
        for (Delsvar delsvar :
                svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case ALLMANT_DIAGNOSKOD_KODAD_ALLMANT_KOD_DELSVAR_ID:
                    System.out.println(delsvar.toString());

            }
        }
    }

    private static void handleDiagnosFritext(TsTstrk1062UtlatandeV1.Builder utlatande, Svar svar) throws ConverterException {
        utlatande.setDiagnosRegistrering(DiagnosRegistrering.create(DiagnosRegistrering.DiagnosRegistreringsTyp.DIAGNOS_FRITEXT));
        String diagnosFritext = "";
        String diagnosArtal = "";
        for (Delsvar delsvar :
                svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case ALLMANT_DIAGNOSKOD_FRITEXT_ALLMANT_FRITEXT_DELSVAR_ID:
                    diagnosFritext = getStringContent(delsvar);
                    break;
                case ALLMANT_DIAGNOSKOD_FRITEXT_ALLMANT_FRITEXT_ARTAL_DELSVAR_ID:
                    diagnosArtal = getPartialDateContent(delsvar).getValue().toString();
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }

        utlatande.setDiagnosFritext(DiagnosFritext.create(diagnosFritext, diagnosArtal));
    }
}
