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
package se.inera.intyg.common.ts_diabetes.v3.model.converter;

import static se.inera.intyg.common.support.Constants.KV_ID_KONTROLL_CODE_SYSTEM;
import static se.inera.intyg.common.support.Constants.KV_INTYGET_AVSER_CODE_SYSTEM;
import static se.inera.intyg.common.support.Constants.KV_KORKORTSBEHORIGHET_CODE_SYSTEM;
import static se.inera.intyg.common.support.Constants.KV_UTLATANDETYP_INTYG_CODE_SYSTEM;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.aCV;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.aPartialDate;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.aSvar;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.addIfNotBlank;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.getInternalDateContent;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.getYearContent;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.ALLMANT_BEHANDLING_ANNAN_BEHANDLING_BESKRIVNING_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.ALLMANT_BEHANDLING_ANNAN_BEHANDLING_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.ALLMANT_BEHANDLING_ENDAST_KOST_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.ALLMANT_BEHANDLING_INSULIN_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.ALLMANT_BEHANDLING_INSULIN_SEDAN_AR_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.ALLMANT_BEHANDLING_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.ALLMANT_BEHANDLING_TABLETTER_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.ALLMANT_BEHANDLING_TABLETTER_RISK_HYPOGLYKEMI_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.ALLMANT_BESKRIVNING_ANNAN_TYP_AV_DIABETES_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.ALLMANT_DIABETES_DIAGNOS_AR_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.ALLMANT_DIABETES_DIAGNOS_AR_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.ALLMANT_TYP_AV_DIABETES_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.ALLMANT_TYP_AV_DIABETES_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.BEDOMNING_BOR_UNDERSOKAS_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.BEDOMNING_BOR_UNDERSOKAS_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.BEDOMNING_LAMPLIGHET_ATT_INNEHA_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.BEDOMNING_LAMPLIGHET_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.BEDOMNING_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.BEDOMNING_UPPFYLLER_BEHORIGHETSKRAV_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_ARET_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_ARET_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_ARET_TIDPUNKT_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_KVARTALET_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_KVARTALET_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_TIDPUNKT_VAKEN_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_EGENKONTROLL_BLODSOCKER_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_EGENKONTROLL_BLODSOCKER_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_FOREKOMST_SENASTE_TRAFIK_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_FOREKOMST_TRAFIK_SVAR_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_FOREKOMST_TRAFIK_TIDPUNKT_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_FORSTAR_RISKER_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_FORSTAR_RISKER_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_FORTROGEN_MED_SYMPTOM_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_FORTROGEN_MED_SYMPTOM_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_KUNSKAP_LAMPLIGA_ATGARDER_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_KUNSKAP_LAMPLIGA_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_NEDSATT_HJARNFUNKTION_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_NEDSATT_HJARNFUNKTION_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_SAKNAR_FORMAGA_VARNINGSTECKEN_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_SAKNAR_FORMAGA_VARNINGSTECKEN_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_SJUKDOMEN_UNDER_KONTROLL_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.IDENTITET_STYRKT_GENOM_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.IDENTITET_STYRKT_GENOM_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.INTYGETAVSER_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.INTYGETAVSER_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.OVRIGT_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.OVRIGT_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.SYNFUNKTION_MISSTANKE_OGONSJUKDOM_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.SYNFUNKTION_OGONBOTTENFOTO_SAKNAS_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.SYNFUNKTION_OGONBOTTENFOTO_SAKNAS_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.SYNFUNKTION_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.SYNFUNKTION_SYNSKARPA_BINOKULART_MED_KORREKTION_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.SYNFUNKTION_SYNSKARPA_BINOKULART_UTAN_KORREKTION_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.SYNFUNKTION_SYNSKARPA_HOGER_MED_KORREKTION_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.SYNFUNKTION_SYNSKARPA_HOGER_UTAN_KORREKTION_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.SYNFUNKTION_SYNSKARPA_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.SYNFUNKTION_SYNSKARPA_VANSTER_MED_KORREKTION_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.SYNFUNKTION_SYNSKARPA_VANSTER_UTAN_KORREKTION_DELSVAR_ID;

import com.google.common.base.Strings;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.PartialDateType;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.PartialDateTypeFormatEnum;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.TypAvIntyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;
import javax.xml.bind.JAXBElement;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import se.inera.intyg.common.support.common.enumerations.Diagnoskodverk;
import se.inera.intyg.common.support.modules.converter.InternalConverterUtil;
import se.inera.intyg.common.ts_diabetes.support.TsDiabetesEntryPoint;
import se.inera.intyg.common.ts_diabetes.v3.model.internal.Allmant;
import se.inera.intyg.common.ts_diabetes.v3.model.internal.Bedomning;
import se.inera.intyg.common.ts_diabetes.v3.model.internal.BedomningKorkortstyp;
import se.inera.intyg.common.ts_diabetes.v3.model.internal.Hypoglykemier;
import se.inera.intyg.common.ts_diabetes.v3.model.internal.IntygAvserKategori;
import se.inera.intyg.common.ts_diabetes.v3.model.internal.Synfunktion;
import se.inera.intyg.common.ts_diabetes.v3.model.internal.Synskarpevarden;
import se.inera.intyg.common.ts_diabetes.v3.model.internal.TsDiabetesUtlatandeV3;
import se.inera.intyg.common.ts_parent.codes.IntygAvserKod;
import se.inera.intyg.common.ts_parent.codes.KorkortsbehorighetKod;

public final class UtlatandeToIntyg {

    private UtlatandeToIntyg() {
    }

    public static Intyg convert(TsDiabetesUtlatandeV3 utlatande) {
        Intyg intyg = InternalConverterUtil.getIntyg(utlatande, false);
        intyg.setTyp(getTypAvIntyg(utlatande));
        intyg.getSvar().addAll(getSvar(utlatande));
        intyg.setUnderskrift(InternalConverterUtil.base64StringToUnderskriftType(utlatande));
        return intyg;
    }

    private static TypAvIntyg getTypAvIntyg(TsDiabetesUtlatandeV3 source) {
        TypAvIntyg typAvIntyg = new TypAvIntyg();
        typAvIntyg.setCode(TsDiabetesEntryPoint.KV_UTLATANDETYP_INTYG_CODE);
        typAvIntyg.setCodeSystem(KV_UTLATANDETYP_INTYG_CODE_SYSTEM);
        typAvIntyg.setDisplayName(TsDiabetesEntryPoint.ISSUER_MODULE_NAME);
        return typAvIntyg;
    }

    private static List<Svar> getSvar(TsDiabetesUtlatandeV3 source) {
        List<Svar> svars = new ArrayList<>();

        // Kat 1 - Intyget avser
        if (source.getIntygAvser() != null && source.getIntygAvser().getKategorier().size() > 0) {
            int intygAvserInstans = 1;
            for (IntygAvserKategori intygAvserKategori : source.getIntygAvser().getKategorier()) {
                IntygAvserKod intygAvserKod = IntygAvserKod.valueOf(intygAvserKategori.name());
                svars.add(aSvar(INTYGETAVSER_SVAR_ID, intygAvserInstans++)
                        .withDelsvar(INTYGETAVSER_DELSVAR_ID,
                                aCV(KV_INTYGET_AVSER_CODE_SYSTEM, intygAvserKod.getCode(), intygAvserKod.getDescription()))
                        .build());
            }
        }

        // Kat 2 - Identitet
        if (source.getIdentitetStyrktGenom() != null && source.getIdentitetStyrktGenom().getTyp() != null) {
            svars.add(aSvar(IDENTITET_STYRKT_GENOM_SVAR_ID)
                    .withDelsvar(IDENTITET_STYRKT_GENOM_DELSVAR_ID,
                            aCV(KV_ID_KONTROLL_CODE_SYSTEM, source.getIdentitetStyrktGenom().getTyp().getCode(),
                                    source.getIdentitetStyrktGenom().getTyp().getDescription()))
                    .build());
        }

        // Kat 3 - Allmänt
        if (source.getAllmant() != null) {
            buildAllmant(source.getAllmant(), svars);
        }

        // Kat 4 - Hypoglykemier
        if (source.getHypoglykemier() != null) {
            buildHypoglykemier(source.getHypoglykemier(), svars);
        }

        // Kat 5 - Synfunktion
        if (source.getSynfunktion() != null) {
            buildSynfunktion(source.getSynfunktion(), svars);
        }

        // Kat 6 - Övrigt
        if (source.getOvrigt() != null) {
            addIfNotBlank(svars, OVRIGT_SVAR_ID, OVRIGT_DELSVAR_ID, buildOvrigaUpplysningar(source));

        }

        // Kat 7 - Bedömning
        if (source.getBedomning() != null) {
            buildBedomning(source.getBedomning(), svars);
        }

        return svars;
    }

    private static void buildAllmant(Allmant allmant, List<Svar> svars) {
        if (allmant.getDiabetesDiagnosAr() != null) {
            // If getDiabetesDiagnosAr can not be converted to year getYearContent will return null and the svar will not be added.
            Year diabetesDiagnosAr = getYearContent(allmant.getDiabetesDiagnosAr());
            if (diabetesDiagnosAr != null) {
                svars.add(aSvar(ALLMANT_DIABETES_DIAGNOS_AR_SVAR_ID)
                        .withDelsvar(ALLMANT_DIABETES_DIAGNOS_AR_DELSVAR_ID,
                                aPartialDate(PartialDateTypeFormatEnum.YYYY, diabetesDiagnosAr))
                        .build());
            }
        }

        if (allmant.getTypAvDiabetes() != null || allmant.getBeskrivningAnnanTypAvDiabetes() != null) {
            // Here we rely on withDelsvar not adding a delsvar if content is null
            svars.add(aSvar(ALLMANT_TYP_AV_DIABETES_SVAR_ID)
                    .withDelsvar(ALLMANT_TYP_AV_DIABETES_DELSVAR_ID,
                            // https://inera-certificate.atlassian.net/wiki/spaces/IT/pages/652083265/Utformning+TS-diabetes+-+version+2
                            // "Svar annan leder inte till något svar utan bara till att 18.2 bli obligatorisk att fylla i."
                            allmant.getTypAvDiabetes() != null
                                    ? aCV(Diagnoskodverk.ICD_10_SE.getCodeSystem(), allmant.getTypAvDiabetes().getCode(),
                                            allmant.getTypAvDiabetes().getDescription())
                                    : null)
                    .withDelsvar(ALLMANT_BESKRIVNING_ANNAN_TYP_AV_DIABETES_DELSVAR_ID,
                            allmant.getBeskrivningAnnanTypAvDiabetes())
                    .build());
        }

        if (allmant.getBehandling() != null) {

            // If getInsulinSedanAr can not be converted to year getYearContent will return null and the delsvar will not be added.
            JAXBElement<PartialDateType> insulinSedanArContent = null;
            Year insulinSedanAr = getYearContent(allmant.getBehandling().getInsulinSedanAr());
            if (insulinSedanAr != null) {
                insulinSedanArContent = aPartialDate(PartialDateTypeFormatEnum.YYYY, insulinSedanAr);
            }

            // Here we rely on withDelsvar not adding a delsvar if content is null
            Svar behandlingSvar = aSvar(ALLMANT_BEHANDLING_SVAR_ID)
                    .withDelsvar(ALLMANT_BEHANDLING_ENDAST_KOST_DELSVAR_ID,
                            InternalConverterUtil.getBooleanContent(allmant.getBehandling().getEndastKost()))
                    .withDelsvar(ALLMANT_BEHANDLING_TABLETTER_DELSVAR_ID,
                            InternalConverterUtil.getBooleanContent(allmant.getBehandling().getTabletter()))
                    .withDelsvar(ALLMANT_BEHANDLING_TABLETTER_RISK_HYPOGLYKEMI_DELSVAR_ID,
                            InternalConverterUtil.getBooleanContent(allmant.getBehandling().getTablettRiskHypoglykemi()))
                    .withDelsvar(ALLMANT_BEHANDLING_INSULIN_DELSVAR_ID,
                            InternalConverterUtil.getBooleanContent(allmant.getBehandling().getInsulin()))
                    .withDelsvar(ALLMANT_BEHANDLING_INSULIN_SEDAN_AR_DELSVAR_ID,
                            insulinSedanArContent)
                    .withDelsvar(ALLMANT_BEHANDLING_ANNAN_BEHANDLING_DELSVAR_ID,
                            InternalConverterUtil.getBooleanContent(allmant.getBehandling().getAnnanBehandling()))
                    .withDelsvar(ALLMANT_BEHANDLING_ANNAN_BEHANDLING_BESKRIVNING_DELSVAR_ID,
                            allmant.getBehandling().getAnnanBehandlingBeskrivning())
                    .build();
            boolean validElementInIntygXmlSchema = behandlingSvar.getDelsvar().size() != 0;
            if (validElementInIntygXmlSchema) {
                svars.add(behandlingSvar);
            }
        }
    }

    private static void buildHypoglykemier(Hypoglykemier hypoglykemier, List<Svar> svars) {
        InternalConverterUtil.addIfNotNull(svars, HYPOGLYKEMIER_SVAR_ID,
                HYPOGLYKEMIER_SJUKDOMEN_UNDER_KONTROLL_DELSVAR_ID, hypoglykemier.getSjukdomenUnderKontroll());

        InternalConverterUtil.addIfNotNull(svars, HYPOGLYKEMIER_NEDSATT_HJARNFUNKTION_SVAR_ID,
                HYPOGLYKEMIER_NEDSATT_HJARNFUNKTION_DELSVAR_ID, hypoglykemier.getNedsattHjarnfunktion());

        InternalConverterUtil.addIfNotNull(svars, HYPOGLYKEMIER_FORSTAR_RISKER_SVAR_ID,
                HYPOGLYKEMIER_FORSTAR_RISKER_DELSVAR_ID, hypoglykemier.getForstarRisker());

        InternalConverterUtil.addIfNotNull(svars, HYPOGLYKEMIER_FORTROGEN_MED_SYMPTOM_SVAR_ID,
                HYPOGLYKEMIER_FORTROGEN_MED_SYMPTOM_DELSVAR_ID, hypoglykemier.getFortrogenMedSymptom());

        InternalConverterUtil.addIfNotNull(svars, HYPOGLYKEMIER_SAKNAR_FORMAGA_VARNINGSTECKEN_SVAR_ID,
                HYPOGLYKEMIER_SAKNAR_FORMAGA_VARNINGSTECKEN_DELSVAR_ID, hypoglykemier.getSaknarFormagaVarningstecken());

        InternalConverterUtil.addIfNotNull(svars, HYPOGLYKEMIER_KUNSKAP_LAMPLIGA_ATGARDER_SVAR_ID,
                HYPOGLYKEMIER_KUNSKAP_LAMPLIGA_DELSVAR_ID, hypoglykemier.getKunskapLampligaAtgarder());

        InternalConverterUtil.addIfNotNull(svars, HYPOGLYKEMIER_EGENKONTROLL_BLODSOCKER_SVAR_ID,
                HYPOGLYKEMIER_EGENKONTROLL_BLODSOCKER_DELSVAR_ID, hypoglykemier.getEgenkontrollBlodsocker());

        if (hypoglykemier.getAterkommandeSenasteAret() != null) {
            svars.add(aSvar(HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_ARET_SVAR_ID)
                    .withDelsvar(HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_ARET_DELSVAR_ID,
                            InternalConverterUtil.getBooleanContent(hypoglykemier.getAterkommandeSenasteAret()))
                    .withDelsvar(HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_ARET_TIDPUNKT_DELSVAR_ID,
                            getInternalDateContent(hypoglykemier.getAterkommandeSenasteTidpunkt()))
                    .build());
        }

        if (hypoglykemier.getAterkommandeSenasteKvartalet() != null) {
            svars.add(aSvar(HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_KVARTALET_SVAR_ID)
                    .withDelsvar(HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_KVARTALET_DELSVAR_ID,
                            InternalConverterUtil.getBooleanContent(hypoglykemier.getAterkommandeSenasteKvartalet()))
                    .withDelsvar(HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_TIDPUNKT_VAKEN_DELSVAR_ID,
                            getInternalDateContent(hypoglykemier.getSenasteTidpunktVaken()))
                    .build());
        }

        if (hypoglykemier.getForekomstTrafik() != null) {
            svars.add(aSvar(HYPOGLYKEMIER_FOREKOMST_SENASTE_TRAFIK_SVAR_ID)
                    .withDelsvar(HYPOGLYKEMIER_FOREKOMST_TRAFIK_SVAR_DELSVAR_ID,
                            InternalConverterUtil.getBooleanContent(hypoglykemier.getForekomstTrafik()))
                    .withDelsvar(HYPOGLYKEMIER_FOREKOMST_TRAFIK_TIDPUNKT_DELSVAR_ID,
                            getInternalDateContent(hypoglykemier.getForekomstTrafikTidpunkt()))
                    .build());
        }
    }

    private static void buildSynfunktion(Synfunktion synfunktion, List<Svar> svars) {
        InternalConverterUtil.addIfNotNull(svars, SYNFUNKTION_SVAR_ID, SYNFUNKTION_MISSTANKE_OGONSJUKDOM_DELSVAR_ID,
                synfunktion.getMisstankeOgonsjukdom());

        InternalConverterUtil.addIfNotNull(svars, SYNFUNKTION_OGONBOTTENFOTO_SAKNAS_SVAR_ID, SYNFUNKTION_OGONBOTTENFOTO_SAKNAS_DELSVAR_ID,
                synfunktion.getOgonbottenFotoSaknas());

        InternalConverterUtil.SvarBuilder synskarpa = aSvar(SYNFUNKTION_SYNSKARPA_SVAR_ID);

        final Synskarpevarden hoger = synfunktion.getHoger();
        if (hoger != null) {
            if (hoger.getUtanKorrektion() != null) {
                synskarpa.withDelsvar(SYNFUNKTION_SYNSKARPA_HOGER_UTAN_KORREKTION_DELSVAR_ID, hoger.getUtanKorrektion().toString());
            }
            if (hoger.getMedKorrektion() != null) {
                synskarpa.withDelsvar(SYNFUNKTION_SYNSKARPA_HOGER_MED_KORREKTION_DELSVAR_ID, hoger.getMedKorrektion().toString());
            }
        }

        final Synskarpevarden vanster = synfunktion.getVanster();
        if (vanster != null) {
            if (vanster.getUtanKorrektion() != null) {
                synskarpa.withDelsvar(SYNFUNKTION_SYNSKARPA_VANSTER_UTAN_KORREKTION_DELSVAR_ID, vanster.getUtanKorrektion().toString());
            }
            if (vanster.getMedKorrektion() != null) {
                synskarpa.withDelsvar(SYNFUNKTION_SYNSKARPA_VANSTER_MED_KORREKTION_DELSVAR_ID, vanster.getMedKorrektion().toString());
            }
        }

        final Synskarpevarden binokulart = synfunktion.getBinokulart();
        if (binokulart != null) {
            if (binokulart.getUtanKorrektion() != null) {
                synskarpa.withDelsvar(SYNFUNKTION_SYNSKARPA_BINOKULART_UTAN_KORREKTION_DELSVAR_ID,
                        binokulart.getUtanKorrektion().toString());
            }
            if (binokulart.getMedKorrektion() != null) {
                synskarpa.withDelsvar(SYNFUNKTION_SYNSKARPA_BINOKULART_MED_KORREKTION_DELSVAR_ID,
                        binokulart.getMedKorrektion().toString());
            }
        }

        if (!synskarpa.delSvars.isEmpty()) {
            svars.add(synskarpa.build());
        }
    }

    private static String buildOvrigaUpplysningar(TsDiabetesUtlatandeV3 source) {
        String ovrigt = null;

        if (!Strings.nullToEmpty(source.getOvrigt()).trim().isEmpty()) {
            ovrigt = source.getOvrigt();
        }
        return ovrigt;
    }

    private static void buildBedomning(Bedomning bedomning, List<Svar> svars) {
        if (bedomning.getUppfyllerBehorighetskrav() != null) {
            int behorighetskravInstans = 1;
            for (BedomningKorkortstyp bedomningKorkortstyp : bedomning.getUppfyllerBehorighetskrav()) {
                KorkortsbehorighetKod korkortsbehorighetKod = KorkortsbehorighetKod.valueOf(bedomningKorkortstyp.name());
                svars.add(aSvar(BEDOMNING_SVAR_ID, behorighetskravInstans++)
                        .withDelsvar(BEDOMNING_UPPFYLLER_BEHORIGHETSKRAV_DELSVAR_ID,
                                aCV(KV_KORKORTSBEHORIGHET_CODE_SYSTEM, korkortsbehorighetKod.getCode(),
                                        korkortsbehorighetKod.getDescription()))
                        .build());
            }
        }

        InternalConverterUtil.addIfNotNull(svars, BEDOMNING_LAMPLIGHET_SVAR_ID, BEDOMNING_LAMPLIGHET_ATT_INNEHA_DELSVAR_ID,
                bedomning.getLampligtInnehav());

        InternalConverterUtil.addIfNotBlank(svars, BEDOMNING_BOR_UNDERSOKAS_SVAR_ID, BEDOMNING_BOR_UNDERSOKAS_DELSVAR_ID,
                bedomning.getBorUndersokasBeskrivning());
    }

}
