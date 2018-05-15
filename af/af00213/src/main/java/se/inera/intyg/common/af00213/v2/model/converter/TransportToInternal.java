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
package se.inera.intyg.common.af00213.v2.model.converter;

import static se.inera.intyg.common.af00213.model.converter.Af00213RespConstants.AKTIVITETSBEGRANSNING_DELSVAR_ID_21;
import static se.inera.intyg.common.af00213.model.converter.Af00213RespConstants.AKTIVITETSBEGRANSNING_DELSVAR_ID_22;
import static se.inera.intyg.common.af00213.model.converter.Af00213RespConstants.AKTIVITETSBEGRANSNING_SVAR_ID_2;
import static se.inera.intyg.common.af00213.model.converter.Af00213RespConstants.FUNKTIONSNEDSATTNING_DELSVAR_ID_11;
import static se.inera.intyg.common.af00213.model.converter.Af00213RespConstants.FUNKTIONSNEDSATTNING_DELSVAR_ID_12;
import static se.inera.intyg.common.af00213.model.converter.Af00213RespConstants.FUNKTIONSNEDSATTNING_SVAR_ID_1;
import static se.inera.intyg.common.af00213.model.converter.Af00213RespConstants.OVRIGT_DELSVAR_ID_5;
import static se.inera.intyg.common.af00213.model.converter.Af00213RespConstants.OVRIGT_SVAR_ID_5;
import static se.inera.intyg.common.af00213.model.converter.Af00213RespConstants.UTREDNING_BEHANDLING_DELSVAR_ID_31;
import static se.inera.intyg.common.af00213.model.converter.Af00213RespConstants.UTREDNING_BEHANDLING_DELSVAR_ID_32;
import static se.inera.intyg.common.af00213.model.converter.Af00213RespConstants.UTREDNING_BEHANDLING_SVAR_ID_3;
import static se.inera.intyg.common.af00213.v2.model.converter.RespConstants.SKIPPAR_BALTE_DELSVAR_ID_4_1;
import static se.inera.intyg.common.af00213.v2.model.converter.RespConstants.SKIPPAR_BALTE_DELSVAR_ID_4_2;
import static se.inera.intyg.common.af00213.v2.model.converter.RespConstants.SKIPPAR_BALTE_SVAR_ID_4;
import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.getStringContent;

import se.inera.intyg.common.af00213.v2.model.internal.Af00213UtlatandeV2;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.converter.TransportConverterUtil;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar.Delsvar;

public final class TransportToInternal {

    private TransportToInternal() {
    }

    public static Af00213UtlatandeV2 convert(Intyg source) throws ConverterException {
        Af00213UtlatandeV2.Builder utlatande = Af00213UtlatandeV2.builder();
        utlatande.setId(source.getIntygsId().getExtension());
        utlatande.setGrundData(TransportConverterUtil.getGrundData(source, false));
        utlatande.setTextVersion(source.getVersion());
        utlatande.setSignature(TransportConverterUtil.signatureTypeToBase64(source.getUnderskrift()));
        setSvar(utlatande, source);
        return utlatande.build();
    }

    private static void setSvar(Af00213UtlatandeV2.Builder utlatande, Intyg source) throws ConverterException {

        for (Svar svar : source.getSvar()) {
            switch (svar.getId()) {
            case FUNKTIONSNEDSATTNING_SVAR_ID_1:
                handleFunktionsnedsattning(utlatande, svar);
                break;
            case AKTIVITETSBEGRANSNING_SVAR_ID_2:
                handleAktivitetsbegransning(utlatande, svar);
                break;
            case UTREDNING_BEHANDLING_SVAR_ID_3:
                handleUtredningBehandling(utlatande, svar);
                break;
            case SKIPPAR_BALTE_SVAR_ID_4:
                handleBeteendeTrafiken(utlatande, svar);
                break;
            case OVRIGT_SVAR_ID_5:
                handleOvrigt(utlatande, svar);
                break;
            default:
                break;
            }
        }
    }

    private static void handleFunktionsnedsattning(Af00213UtlatandeV2.Builder utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
            case FUNKTIONSNEDSATTNING_DELSVAR_ID_11:
                utlatande.setHarFunktionsnedsattning(Boolean.valueOf(getStringContent(delsvar)));
                break;
            case FUNKTIONSNEDSATTNING_DELSVAR_ID_12:
                utlatande.setFunktionsnedsattning(getStringContent(delsvar));
                break;
            default:
                throw new IllegalArgumentException();
            }
        }

    }

    private static void handleAktivitetsbegransning(Af00213UtlatandeV2.Builder utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
            case AKTIVITETSBEGRANSNING_DELSVAR_ID_21:
                utlatande.setHarAktivitetsbegransning(Boolean.valueOf(getStringContent(delsvar)));
                break;
            case AKTIVITETSBEGRANSNING_DELSVAR_ID_22:
                utlatande.setAktivitetsbegransning(getStringContent(delsvar));
                break;
            default:
                throw new IllegalArgumentException();
            }
        }

    }

    private static void handleUtredningBehandling(Af00213UtlatandeV2.Builder utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
            case UTREDNING_BEHANDLING_DELSVAR_ID_31:
                utlatande.setHarUtredningBehandling(Boolean.valueOf(getStringContent(delsvar)));
                break;
            case UTREDNING_BEHANDLING_DELSVAR_ID_32:
                utlatande.setUtredningBehandling(getStringContent(delsvar));
                break;
            default:
                throw new IllegalArgumentException();
            }
        }
    }

    private static void handleBeteendeTrafiken(Af00213UtlatandeV2.Builder utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
            case SKIPPAR_BALTE_DELSVAR_ID_4_1:
                utlatande.setHarSkipparBalte(Boolean.valueOf(getStringContent(delsvar)));
                break;
            case SKIPPAR_BALTE_DELSVAR_ID_4_2:
                utlatande.setSkipparBalteMotivering(getStringContent(delsvar));
                break;
            default:
                throw new IllegalArgumentException();
            }
        }
    }

    private static void handleOvrigt(Af00213UtlatandeV2.Builder utlatande, Svar svar) {
        Delsvar delsvar = svar.getDelsvar().get(0);
        switch (delsvar.getId()) {
        case OVRIGT_DELSVAR_ID_5:
            utlatande.setOvrigt(getStringContent(delsvar));
            break;
        default:
            throw new IllegalArgumentException();
        }
    }

}