/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.ag7804.v1.model.converter;

import static se.inera.intyg.common.ag7804.converter.RespConstants.AKTIVITETSBEGRANSNING_DELSVAR_BESKRIVNING;
import static se.inera.intyg.common.ag7804.converter.RespConstants.AKTIVITETSBEGRANSNING_DELSVAR_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_ID_17;
import static se.inera.intyg.common.ag7804.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_JSON_ID_17;
import static se.inera.intyg.common.ag7804.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ANLEDNING_TILL_KONTAKT_DELSVAR_ID_103;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ANLEDNING_TILL_KONTAKT_DELSVAR_JSON_ID_103;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ANLEDNING_TILL_KONTAKT_DELSVAR_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ANSWER_NO;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ANSWER_NOT_SELECTED;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ANSWER_YES;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_ID_44;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_JSON_ID_44;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ARBETSLIVSINRIKTADE_ATGARDER_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ARBETSRESOR_SVAR_ID_34;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ARBETSRESOR_SVAR_JSON_ID_34;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ARBETSRESOR_SVAR_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_ID_33;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_JSON_ID_33;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ARBETSTIDSFORLAGGNING_MOTIVERING_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ARBETSTIDSFORLAGGNING_SVAR_BESKRIVNING;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ARBETSTIDSFORLAGGNING_SVAR_ID_33;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ARBETSTIDSFORLAGGNING_SVAR_JSON_ID_33;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ARBETSTIDSFORLAGGNING_SVAR_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ATGARDER_CATEGORY_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.AVSTANGNING_SMITTSKYDD_CATEGORY_DESCRIPTION;
import static se.inera.intyg.common.ag7804.converter.RespConstants.AVSTANGNING_SMITTSKYDD_CATEGORY_ID;
import static se.inera.intyg.common.ag7804.converter.RespConstants.AVSTANGNING_SMITTSKYDD_CATEGORY_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.AVSTANGNING_SMITTSKYDD_QUESTION_LABEL;
import static se.inera.intyg.common.ag7804.converter.RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_ID_27;
import static se.inera.intyg.common.ag7804.converter.RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27;
import static se.inera.intyg.common.ag7804.converter.RespConstants.BEDOMNING_CATEGORY_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.BEHOV_AV_SJUKSKRIVNING_EN_FJARDEDEL;
import static se.inera.intyg.common.ag7804.converter.RespConstants.BEHOV_AV_SJUKSKRIVNING_HALFTEN;
import static se.inera.intyg.common.ag7804.converter.RespConstants.BEHOV_AV_SJUKSKRIVNING_HELT_NEDSATT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.BEHOV_AV_SJUKSKRIVNING_SVAR_BESKRIVNING;
import static se.inera.intyg.common.ag7804.converter.RespConstants.BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32;
import static se.inera.intyg.common.ag7804.converter.RespConstants.BEHOV_AV_SJUKSKRIVNING_SVAR_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.BEHOV_AV_SJUKSKRIVNING_TRE_FJARDEDEL;
import static se.inera.intyg.common.ag7804.converter.RespConstants.CATEGORY_ATGARDER;
import static se.inera.intyg.common.ag7804.converter.RespConstants.CATEGORY_BEDOMNING;
import static se.inera.intyg.common.ag7804.converter.RespConstants.CATEGORY_DIAGNOS;
import static se.inera.intyg.common.ag7804.converter.RespConstants.CATEGORY_FUNKTIONSNEDSATTNING;
import static se.inera.intyg.common.ag7804.converter.RespConstants.CATEGORY_GRUNDFORMU;
import static se.inera.intyg.common.ag7804.converter.RespConstants.CATEGORY_KONTAKT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.CATEGORY_MEDICINSKABEHANDLINGAR;
import static se.inera.intyg.common.ag7804.converter.RespConstants.CATEGORY_OVRIGT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.CATEGORY_SYSSELSATTNING;
import static se.inera.intyg.common.ag7804.converter.RespConstants.DESCRIPTION;
import static se.inera.intyg.common.ag7804.converter.RespConstants.DIAGNOS_CATEGORY_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.DIAGNOS_ICD_10_ID;
import static se.inera.intyg.common.ag7804.converter.RespConstants.DIAGNOS_ICD_10_LABEL;
import static se.inera.intyg.common.ag7804.converter.RespConstants.DIAGNOS_KSH_97_ID;
import static se.inera.intyg.common.ag7804.converter.RespConstants.DIAGNOS_KSH_97_LABEL;
import static se.inera.intyg.common.ag7804.converter.RespConstants.DIAGNOS_SVAR_BESKRIVNING;
import static se.inera.intyg.common.ag7804.converter.RespConstants.DIAGNOS_SVAR_ID_6;
import static se.inera.intyg.common.ag7804.converter.RespConstants.DIAGNOS_SVAR_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_BESKRIVNING;
import static se.inera.intyg.common.ag7804.converter.RespConstants.FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_ID_37;
import static se.inera.intyg.common.ag7804.converter.RespConstants.FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_JSON_ID_37;
import static se.inera.intyg.common.ag7804.converter.RespConstants.FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.FUNKTIONSNEDSATTNING_CATEGORY_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.FUNKTIONSNEDSATTNING_DELSVAR_BESKRIVNING;
import static se.inera.intyg.common.ag7804.converter.RespConstants.FUNKTIONSNEDSATTNING_DELSVAR_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.FUNKTIONSNEDSATTNING_SVAR_ID_35;
import static se.inera.intyg.common.ag7804.converter.RespConstants.FUNKTIONSNEDSATTNING_SVAR_JSON_ID_35;
import static se.inera.intyg.common.ag7804.converter.RespConstants.FUNKTIONSNEDSATTNING_SVAR_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANNANBESKRIVNING_DELSVAR_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_BESKRIVNING_DELSVAR_JSON_ID_1;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_CATEGORY_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_BESKRIVNING;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_TELEFONKONTAKT_PATIENT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMU_ANNAT_LABEL;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMU_JOURNALUPPGIFTER_LABEL;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMU_TELEFONKONTAKT_LABEL;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMU_UNDERSOKNING_LABEL;
import static se.inera.intyg.common.ag7804.converter.RespConstants.KONTAKT_CATEGORY_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.KONTAKT_ONSKAS_DELSVAR_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.KONTAKT_ONSKAS_SVAR_ID_103;
import static se.inera.intyg.common.ag7804.converter.RespConstants.KONTAKT_ONSKAS_SVAR_JSON_ID_103;
import static se.inera.intyg.common.ag7804.converter.RespConstants.KONTAKT_ONSKAS_SVAR_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.MEDICINSKABEHANDLINGAR_CATEGORY_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.NO_ID;
import static se.inera.intyg.common.ag7804.converter.RespConstants.NUVARANDE_ARBETE_SVAR_ID_29;
import static se.inera.intyg.common.ag7804.converter.RespConstants.NUVARANDE_ARBETE_SVAR_JSON_ID_29;
import static se.inera.intyg.common.ag7804.converter.RespConstants.NUVARANDE_ARBETE_SVAR_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ONSKAR_FORMEDLA_DIAGNOS_SVAR_ID_100;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ONSKAR_FORMEDLA_DIAGNOS_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.OVRIGT_CATEGORY_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.OVRIGT_SVAR_ID_25;
import static se.inera.intyg.common.ag7804.converter.RespConstants.OVRIGT_SVAR_JSON_ID_25;
import static se.inera.intyg.common.ag7804.converter.RespConstants.OVRIGT_SVAR_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.PAGAENDEBEHANDLING_DELSVAR_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.PAGAENDEBEHANDLING_SVAR_ID_19;
import static se.inera.intyg.common.ag7804.converter.RespConstants.PAGAENDEBEHANDLING_SVAR_JSON_ID_19;
import static se.inera.intyg.common.ag7804.converter.RespConstants.PAGAENDEBEHANDLING_SVAR_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.PLANERADBEHANDLING_DELSVAR_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.PLANERADBEHANDLING_SVAR_ID_20;
import static se.inera.intyg.common.ag7804.converter.RespConstants.PLANERADBEHANDLING_SVAR_JSON_ID_20;
import static se.inera.intyg.common.ag7804.converter.RespConstants.PLANERADBEHANDLING_SVAR_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.PROGNOS_BESKRIVNING_DELSVAR_ID_39;
import static se.inera.intyg.common.ag7804.converter.RespConstants.PROGNOS_DAGAR_180;
import static se.inera.intyg.common.ag7804.converter.RespConstants.PROGNOS_DAGAR_30;
import static se.inera.intyg.common.ag7804.converter.RespConstants.PROGNOS_DAGAR_365;
import static se.inera.intyg.common.ag7804.converter.RespConstants.PROGNOS_DAGAR_60;
import static se.inera.intyg.common.ag7804.converter.RespConstants.PROGNOS_DAGAR_90;
import static se.inera.intyg.common.ag7804.converter.RespConstants.PROGNOS_SVAR_ATER_X_ANTAL_DAGAR;
import static se.inera.intyg.common.ag7804.converter.RespConstants.PROGNOS_SVAR_BESKRIVNING;
import static se.inera.intyg.common.ag7804.converter.RespConstants.PROGNOS_SVAR_ID_39;
import static se.inera.intyg.common.ag7804.converter.RespConstants.PROGNOS_SVAR_PROGNOS_OKLAR;
import static se.inera.intyg.common.ag7804.converter.RespConstants.PROGNOS_SVAR_SANNOLIKT_INTE;
import static se.inera.intyg.common.ag7804.converter.RespConstants.PROGNOS_SVAR_STOR_SANNOLIKHET;
import static se.inera.intyg.common.ag7804.converter.RespConstants.PROGNOS_SVAR_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.SYSSELSATTNING_ARBETE;
import static se.inera.intyg.common.ag7804.converter.RespConstants.SYSSELSATTNING_ARBETSSOKANDE;
import static se.inera.intyg.common.ag7804.converter.RespConstants.SYSSELSATTNING_CATEGORY_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.SYSSELSATTNING_FORALDRALEDIG;
import static se.inera.intyg.common.ag7804.converter.RespConstants.SYSSELSATTNING_STUDIER;
import static se.inera.intyg.common.ag7804.converter.RespConstants.SYSSELSATTNING_SVAR_BESKRIVNING;
import static se.inera.intyg.common.ag7804.converter.RespConstants.SYSSELSATTNING_SVAR_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.TYP_AV_SYSSELSATTNING_SVAR_ID_28;
import static se.inera.intyg.common.ag7804.converter.RespConstants.YES_ID;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.multipleOrExpression;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.not;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import se.inera.intyg.common.ag7804.model.internal.ArbetslivsinriktadeAtgarder;
import se.inera.intyg.common.ag7804.model.internal.ArbetslivsinriktadeAtgarder.ArbetslivsinriktadeAtgarderVal;
import se.inera.intyg.common.ag7804.model.internal.Prognos;
import se.inera.intyg.common.ag7804.model.internal.PrognosDagarTillArbeteTyp;
import se.inera.intyg.common.ag7804.model.internal.PrognosTyp;
import se.inera.intyg.common.ag7804.model.internal.Sjukskrivning;
import se.inera.intyg.common.ag7804.model.internal.Sjukskrivning.SjukskrivningsGrad;
import se.inera.intyg.common.ag7804.model.internal.Sysselsattning;
import se.inera.intyg.common.ag7804.model.internal.Sysselsattning.SysselsattningsTyp;
import se.inera.intyg.common.ag7804.support.Ag7804EntryPoint;
import se.inera.intyg.common.ag7804.v1.model.internal.Ag7804UtlatandeV1;
import se.inera.intyg.common.agparent.model.internal.Diagnos;
import se.inera.intyg.common.fkparent.model.converter.RespConstants;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.common.enumerations.RelationKod;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.CertificateDataElementStyleEnum;
import se.inera.intyg.common.support.facade.model.Staff;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCategory;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCheckboxBoolean;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCheckboxMultipleCode;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCheckboxMultipleDate;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigDiagnoses;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigDropdown;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigRadioBoolean;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigRadioMultipleCode;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigRadioMultipleCodeOptionalDropdown;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigSickLeavePeriod;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTextArea;
import se.inera.intyg.common.support.facade.model.config.CheckboxDateRange;
import se.inera.intyg.common.support.facade.model.config.CheckboxMultipleCode;
import se.inera.intyg.common.support.facade.model.config.CheckboxMultipleDate;
import se.inera.intyg.common.support.facade.model.config.DiagnosesListItem;
import se.inera.intyg.common.support.facade.model.config.DiagnosesTerminology;
import se.inera.intyg.common.support.facade.model.config.DropdownItem;
import se.inera.intyg.common.support.facade.model.config.RadioMultipleCode;
import se.inera.intyg.common.support.facade.model.config.RadioMultipleCodeOptionalDropdown;
import se.inera.intyg.common.support.facade.model.metadata.CertificateMetadata;
import se.inera.intyg.common.support.facade.model.metadata.Unit;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationDisable;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationEnable;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationHide;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationHighlight;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMaxDate;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationShow;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationText;
import se.inera.intyg.common.support.facade.model.value.CertificateDataTextValue;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueBoolean;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCode;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCodeList;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDate;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDateList;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDateRange;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDateRangeList;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDiagnosis;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDiagnosisList;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.common.internal.Relation;

public final class InternalToCertificate {

    private static final short LIMIT_OVRIGT = 4000;
    private static final short LIMIT_DIAGNOSIS_DESC = (short) 250;

    private InternalToCertificate() {

    }

    public static Certificate convert(Ag7804UtlatandeV1 internalCertificate, CertificateTextProvider texts) {
        var index = 0;
        return CertificateBuilder.create()
            .metadata(createMetadata(internalCertificate, texts))
            .addElement(createSmittbararpenningCategory(index++, texts))
            .addElement(createAvstangningSmittskyddQuestion(internalCertificate.getAvstangningSmittskydd(), index++, texts))
            .addElement(createGrundForMUCategory(index++, texts))
            .addElement(createIntygetBaseratPa(internalCertificate, index++, texts))
            .addElement(createAnnatGrundForMUBeskrivning(internalCertificate.getAnnatGrundForMUBeskrivning(), index++, texts))
            .addElement(createSysselsattningCategory(index++, texts))
            .addElement(createSysselsattningQuestion(internalCertificate.getSysselsattning(), index++, texts))
            .addElement(createSysselsattningYrkeQuestion(internalCertificate.getNuvarandeArbete(), index++, texts))
            .addElement(createDiagnosCategory(index++, texts))
            .addElement(createShouldIncludeDiagnosesQuestion(internalCertificate.getOnskarFormedlaDiagnos(), index++, texts))
            .addElement(createDiagnosQuestion(internalCertificate.getDiagnoser(), index++, texts))
            .addElement(createFunktionsnedsattningCategory(index++, texts))
            .addElement(createFunktionsnedsattningQuestion(internalCertificate.getFunktionsnedsattning(), index++, texts))
            .addElement(createAktivitetsbegransningQuestion(internalCertificate.getAktivitetsbegransning(), index++, texts))
            .addElement(createMedicinskaBehandlingarCategory(index++, texts))
            .addElement(createPagaendeBehandlingQuestion(internalCertificate.getPagaendeBehandling(), index++, texts))
            .addElement(createPlaneradBehandlingQuestion(internalCertificate.getPlaneradBehandling(), index++, texts))
            .addElement(createBedomningCategory(index++, texts))
            .addElement(createBehovAvSjukskrivningQuestion(internalCertificate.getSjukskrivningar(), index++, texts,
                internalCertificate.getGrundData().getRelation()))
            .addElement(
                createForsakringsmedicinsktBeslutsstodQuestion(internalCertificate.getForsakringsmedicinsktBeslutsstod(), index++, texts))
            .addElement(createArbetstidsforlaggningQuestion(internalCertificate.getArbetstidsforlaggning(), index++, texts))
            .addElement(
                createMotiveringArbetstidsforlaggningQuestion(internalCertificate.getArbetstidsforlaggningMotivering(), index++, texts))
            .addElement(createArbetsresorQuestion(internalCertificate.getArbetsresor(), index++, texts))
            .addElement(createPrognosQuestion(internalCertificate.getPrognos(), index++, texts))
            .addElement(createPrognosTimeperiodQuestion(internalCertificate.getPrognos(), index++, texts))
            .addElement(createAtgarderCategory(index++, texts))
            .addElement(createAtgarderQuestion(internalCertificate.getArbetslivsinriktadeAtgarder(), index++, texts))
            .addElement(createAtgarderBeskrivning(internalCertificate.getArbetslivsinriktadeAtgarderBeskrivning(), index++, texts))
            .addElement(createOvrigtCategory(index++, texts))
            .addElement(createOvrigtQuestion(internalCertificate.getOvrigt(), index++, texts))
            .addElement(createKontaktCategory(index++, texts))
            .addElement(createKontaktQuestion(internalCertificate.getKontaktMedAg(), index++, texts))
            .addElement(createKontaktBeskrivning(internalCertificate.getAnledningTillKontakt(), index++, texts))
            .build();
    }

    public static CertificateMetadata createMetadata(Ag7804UtlatandeV1 internalCertificate, CertificateTextProvider texts) {
        final var unit = internalCertificate.getGrundData().getSkapadAv().getVardenhet();
        return CertificateMetadata.builder()
            .id(internalCertificate.getId())
            .type(internalCertificate.getTyp())
            .typeVersion(internalCertificate.getTextVersion())
            .name(Ag7804EntryPoint.MODULE_NAME)
            .description(texts.get(DESCRIPTION))
            .unit(
                Unit.builder()
                    .unitId(unit.getEnhetsid())
                    .unitName(unit.getEnhetsnamn())
                    .address(unit.getPostadress())
                    .zipCode(unit.getPostnummer())
                    .city(unit.getPostort())
                    .email(unit.getEpost())
                    .phoneNumber(unit.getTelefonnummer())
                    .build()
            )
            .issuedBy(
                Staff.builder()
                    .personId(internalCertificate.getGrundData().getSkapadAv().getPersonId())
                    .fullName(internalCertificate.getGrundData().getSkapadAv().getFullstandigtNamn())
                    .build()
            )
            .build();
    }

    private static CertificateDataElement createSmittbararpenningCategory(int index, CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(AVSTANGNING_SMITTSKYDD_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigCategory.builder()
                    .text(texts.get(AVSTANGNING_SMITTSKYDD_CATEGORY_TEXT))
                    .description(texts.get(AVSTANGNING_SMITTSKYDD_CATEGORY_DESCRIPTION))
                    .build()
            )
            .build();
    }

    public static CertificateDataElement createAvstangningSmittskyddQuestion(Boolean value, int index, CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(AVSTANGNING_SMITTSKYDD_SVAR_ID_27)
            .index(index)
            .parent(AVSTANGNING_SMITTSKYDD_CATEGORY_ID)
            .config(
                CertificateDataConfigCheckboxBoolean.builder()
                    .id(AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27)
                    .label(texts.get(AVSTANGNING_SMITTSKYDD_QUESTION_LABEL))
                    .selectedText(texts.get(ANSWER_YES))
                    .unselectedText(texts.get(ANSWER_NOT_SELECTED))
                    .build()
            )
            .value(
                CertificateDataValueBoolean.builder()
                    .id(AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27)
                    .selected(value)
                    .build()
            )
            .build();
    }

    private static CertificateDataElement createGrundForMUCategory(int index, CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(CATEGORY_GRUNDFORMU)
            .index(index)
            .config(
                CertificateDataConfigCategory.builder()
                    .text(texts.get(GRUNDFORMEDICINSKTUNDERLAG_CATEGORY_TEXT))
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationHide.builder()
                        .questionId(AVSTANGNING_SMITTSKYDD_SVAR_ID_27)
                        .expression(singleExpression(AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27))
                        .build()
                }
            )
            .build();
    }

    public static CertificateDataElement createIntygetBaseratPa(Ag7804UtlatandeV1 internalCertificate, int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1)
            .index(index)
            .parent(CATEGORY_GRUNDFORMU)
            .config(
                CertificateDataConfigCheckboxMultipleDate.builder()
                    .text(texts.get(GRUNDFORMEDICINSKTUNDERLAG_SVAR_TEXT))
                    .description(texts.get(GRUNDFORMEDICINSKTUNDERLAG_SVAR_BESKRIVNING))
                    .list(
                        Arrays.asList(
                            CheckboxMultipleDate.builder()
                                .id(GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1)
                                .label(texts.get(GRUNDFORMU_UNDERSOKNING_LABEL))
                                .build(),
                            CheckboxMultipleDate.builder()
                                .id(GRUNDFORMEDICINSKTUNDERLAG_TELEFONKONTAKT_PATIENT_SVAR_JSON_ID_1)
                                .label(texts.get(GRUNDFORMU_TELEFONKONTAKT_LABEL))
                                .build(),
                            CheckboxMultipleDate.builder()
                                .id(GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1)
                                .label(texts.get(GRUNDFORMU_JOURNALUPPGIFTER_LABEL))
                                .build(),
                            CheckboxMultipleDate.builder()
                                .id(GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1)
                                .label(texts.get(GRUNDFORMU_ANNAT_LABEL))
                                .build()
                        )
                    )
                    .build()
            )
            .value(
                CertificateDataValueDateList.builder()
                    .list(createIntygetBaseratPaValue(internalCertificate))
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1)
                        .expression(
                            multipleOrExpression(
                                singleExpression(GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1),
                                singleExpression(GRUNDFORMEDICINSKTUNDERLAG_TELEFONKONTAKT_PATIENT_SVAR_JSON_ID_1),
                                singleExpression(GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1),
                                singleExpression(GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1)
                            ))
                        .build(),
                    CertificateDataValidationMaxDate.builder()
                        .id(GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1)
                        .numberOfDays((short) 0)
                        .build(),
                    CertificateDataValidationMaxDate.builder()
                        .id(GRUNDFORMEDICINSKTUNDERLAG_TELEFONKONTAKT_PATIENT_SVAR_JSON_ID_1)
                        .numberOfDays((short) 0)
                        .build(),
                    CertificateDataValidationMaxDate.builder()
                        .id(GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1)
                        .numberOfDays((short) 0)
                        .build(),
                    CertificateDataValidationMaxDate.builder()
                        .id(GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1)
                        .numberOfDays((short) 0)
                        .build(),
                    CertificateDataValidationHide.builder()
                        .questionId(AVSTANGNING_SMITTSKYDD_SVAR_ID_27)
                        .expression(singleExpression(AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27))
                        .build()
                }
            )
            .build();
    }

    private static List<CertificateDataValueDate> createIntygetBaseratPaValue(Ag7804UtlatandeV1 internalCertificate) {
        final List<CertificateDataValueDate> values = new ArrayList<>();

        if (validDate(internalCertificate.getUndersokningAvPatienten())) {
            values.add(
                CertificateDataValueDate.builder()
                    .id(GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1)
                    .date(internalCertificate.getUndersokningAvPatienten().asLocalDate())
                    .build()
            );
        }

        if (validDate(internalCertificate.getTelefonkontaktMedPatienten())) {
            values.add(
                CertificateDataValueDate.builder()
                    .id(GRUNDFORMEDICINSKTUNDERLAG_TELEFONKONTAKT_PATIENT_SVAR_JSON_ID_1)
                    .date(internalCertificate.getTelefonkontaktMedPatienten().asLocalDate())
                    .build()
            );
        }

        if (validDate(internalCertificate.getJournaluppgifter())) {
            values.add(
                CertificateDataValueDate.builder()
                    .id(GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1)
                    .date(internalCertificate.getJournaluppgifter().asLocalDate())
                    .build()
            );
        }

        if (validDate(internalCertificate.getAnnatGrundForMU())) {
            values.add(
                CertificateDataValueDate.builder()
                    .id(GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1)
                    .date(internalCertificate.getAnnatGrundForMU().asLocalDate())
                    .build()
            );
        }
        return values;
    }

    private static boolean validDate(InternalDate date) {
        return date == null ? false : date.isValidDate();
    }

    public static CertificateDataElement createAnnatGrundForMUBeskrivning(String value, int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1)
            .index(index)
            .parent(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1)
            .config(
                CertificateDataConfigTextArea.builder()
                    .id(GRUNDFORMEDICINSKTUNDERLAG_BESKRIVNING_DELSVAR_JSON_ID_1)
                    .text(texts.get(GRUNDFORMEDICINSKTUNDERLAG_ANNANBESKRIVNING_DELSVAR_TEXT))
                    .build()
            )
            .value(
                CertificateDataTextValue.builder()
                    .id(GRUNDFORMEDICINSKTUNDERLAG_BESKRIVNING_DELSVAR_JSON_ID_1)
                    .text(value)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1)
                        .expression(singleExpression(GRUNDFORMEDICINSKTUNDERLAG_BESKRIVNING_DELSVAR_JSON_ID_1))
                        .build(),
                    CertificateDataValidationShow.builder()
                        .questionId(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1)
                        .expression(singleExpression(GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1))
                        .build(),
                    CertificateDataValidationHide.builder()
                        .questionId(AVSTANGNING_SMITTSKYDD_SVAR_ID_27)
                        .expression(singleExpression(AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27))
                        .build()
                }
            )
            .build();
    }

    private static CertificateDataElement createSysselsattningCategory(int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(CATEGORY_SYSSELSATTNING)
            .index(index)
            .config(
                CertificateDataConfigCategory.builder()
                    .text(texts.get(SYSSELSATTNING_CATEGORY_TEXT))
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationHide.builder()
                        .questionId(AVSTANGNING_SMITTSKYDD_SVAR_ID_27)
                        .expression(singleExpression(AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27))
                        .build()
                }
            )
            .build();
    }

    public static CertificateDataElement createSysselsattningQuestion(List<Sysselsattning> value, int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(TYP_AV_SYSSELSATTNING_SVAR_ID_28)
            .index(index)
            .parent(CATEGORY_SYSSELSATTNING)
            .config(
                CertificateDataConfigCheckboxMultipleCode.builder()
                    .text(texts.get(SYSSELSATTNING_SVAR_TEXT))
                    .description(texts.get(SYSSELSATTNING_SVAR_BESKRIVNING))
                    .list(
                        Arrays.asList(
                            CheckboxMultipleCode.builder()
                                .id(SysselsattningsTyp.NUVARANDE_ARBETE.getId())
                                .label(texts.get(SYSSELSATTNING_ARBETE))
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(SysselsattningsTyp.ARBETSSOKANDE.getId())
                                .label(texts.get(SYSSELSATTNING_ARBETSSOKANDE))
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(SysselsattningsTyp.FORADLRARLEDIGHET_VARD_AV_BARN.getId())
                                .label(texts.get(SYSSELSATTNING_FORALDRALEDIG))
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(SysselsattningsTyp.STUDIER.getId())
                                .label(texts.get(SYSSELSATTNING_STUDIER))
                                .build()
                        )
                    )
                    .build()
            )
            .value(
                CertificateDataValueCodeList.builder()
                    .list(createSysselsattningCodeList(value))
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(TYP_AV_SYSSELSATTNING_SVAR_ID_28)
                        .expression(
                            multipleOrExpression(
                                singleExpression(SysselsattningsTyp.NUVARANDE_ARBETE.getId()),
                                singleExpression(SysselsattningsTyp.ARBETSSOKANDE.getId()),
                                singleExpression(SysselsattningsTyp.FORADLRARLEDIGHET_VARD_AV_BARN.getId()),
                                singleExpression(SysselsattningsTyp.STUDIER.getId())
                            )
                        )
                        .build(),
                    CertificateDataValidationHide.builder()
                        .questionId(AVSTANGNING_SMITTSKYDD_SVAR_ID_27)
                        .expression(singleExpression(AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27))
                        .build()
                }
            )
            .build();
    }

    private static List<CertificateDataValueCode> createSysselsattningCodeList(List<Sysselsattning> value) {
        if (value == null) {
            return Collections.emptyList();
        }

        return value.stream()
            .map(sysselsattning -> CertificateDataValueCode.builder()
                .id(Objects.requireNonNull(sysselsattning.getTyp()).getId())
                .code(sysselsattning.getTyp().getId())
                .build())
            .collect(Collectors.toList());
    }

    public static CertificateDataElement createSysselsattningYrkeQuestion(String value, int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(NUVARANDE_ARBETE_SVAR_ID_29)
            .index(index)
            .parent(CATEGORY_SYSSELSATTNING)
            .config(
                CertificateDataConfigTextArea.builder()
                    .text(texts.get(NUVARANDE_ARBETE_SVAR_TEXT))
                    .id(NUVARANDE_ARBETE_SVAR_JSON_ID_29)
                    .build()
            )
            .value(
                CertificateDataTextValue.builder()
                    .id(NUVARANDE_ARBETE_SVAR_JSON_ID_29)
                    .text(value)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(NUVARANDE_ARBETE_SVAR_ID_29)
                        .expression(singleExpression(NUVARANDE_ARBETE_SVAR_JSON_ID_29))
                        .build(),
                    CertificateDataValidationShow.builder()
                        .questionId(TYP_AV_SYSSELSATTNING_SVAR_ID_28)
                        .expression(singleExpression(SysselsattningsTyp.NUVARANDE_ARBETE.getId()))
                        .build(),
                    CertificateDataValidationHide.builder()
                        .questionId(AVSTANGNING_SMITTSKYDD_SVAR_ID_27)
                        .expression(singleExpression(AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27))
                        .build()
                }
            )
            .build();
    }

    private static CertificateDataElement createDiagnosCategory(int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(CATEGORY_DIAGNOS)
            .index(index)
            .config(
                CertificateDataConfigCategory.builder()
                    .text(texts.get(DIAGNOS_CATEGORY_TEXT))
                    .build()
            )
            .build();
    }

    private static String convertRadioBooleanToCode(Boolean value) {
        if (value == null) {
            return null;
        } else if (value) {
            return YES_ID;
        } else {
            return NO_ID;
        }
    }

    public static CertificateDataElement createShouldIncludeDiagnosesQuestion(Boolean value, int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(ONSKAR_FORMEDLA_DIAGNOS_SVAR_ID_100)
            .index(index)
            .parent(CATEGORY_DIAGNOS)
            .config(
                CertificateDataConfigRadioMultipleCode.builder()
                    .text(texts.get(ONSKAR_FORMEDLA_DIAGNOS_TEXT))
                    .list(
                        Arrays.asList(
                            RadioMultipleCode.builder()
                                .id(YES_ID)
                                .label(ANSWER_YES)
                                .build(),
                            RadioMultipleCode.builder()
                                .id(NO_ID)
                                .label(ANSWER_NO)
                                .build()
                        )
                    )
                    .build()
            )
            .value(
                CertificateDataValueCode.builder()
                    .id(convertRadioBooleanToCode(value))
                    .code(convertRadioBooleanToCode(value))
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(ONSKAR_FORMEDLA_DIAGNOS_SVAR_ID_100)
                        .expression(multipleOrExpression(YES_ID, NO_ID))
                        .build(),
                    CertificateDataValidationHighlight.builder()
                        .questionId(ONSKAR_FORMEDLA_DIAGNOS_SVAR_ID_100)
                        .expression(multipleOrExpression(YES_ID, NO_ID, not(YES_ID), not(NO_ID)))
                        .build(),
                }
            )
            .build();
    }

    public static CertificateDataElement createDiagnosQuestion(List<Diagnos> value, int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(DIAGNOS_SVAR_ID_6)
            .index(index)
            .parent(CATEGORY_DIAGNOS)
            .config(
                CertificateDataConfigDiagnoses.builder()
                    .text(texts.get(DIAGNOS_SVAR_TEXT))
                    .description(texts.get(DIAGNOS_SVAR_BESKRIVNING))
                    .terminology(
                        Arrays.asList(
                            DiagnosesTerminology.builder()
                                .id(DIAGNOS_ICD_10_ID)
                                .label(DIAGNOS_ICD_10_LABEL)
                                .build(),
                            DiagnosesTerminology.builder()
                                .id(DIAGNOS_KSH_97_ID)
                                .label(DIAGNOS_KSH_97_LABEL)
                                .build()
                        )
                    )
                    .list(
                        Arrays.asList(
                            DiagnosesListItem.builder()
                                .id("1")
                                .build(),
                            DiagnosesListItem.builder()
                                .id("2")
                                .build(),
                            DiagnosesListItem.builder()
                                .id("3")
                                .build()
                        )
                    )
                    .build()
            )
            .value(
                CertificateDataValueDiagnosisList.builder()
                    .list(createDiagnosValue(value))
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(DIAGNOS_SVAR_ID_6)
                        .expression(singleExpression("1"))
                        .build(),
                    CertificateDataValidationHide.builder()
                        .questionId(ONSKAR_FORMEDLA_DIAGNOS_SVAR_ID_100)
                        .expression(singleExpression(NO_ID))
                        .build(),
                    CertificateDataValidationEnable.builder()
                        .questionId(ONSKAR_FORMEDLA_DIAGNOS_SVAR_ID_100)
                        .expression(multipleOrExpression(YES_ID, NO_ID))
                        .build(),
                    CertificateDataValidationText.builder()
                        .limit(LIMIT_DIAGNOSIS_DESC)
                        .build()
                }
            )
            .build();
    }

    private static List<CertificateDataValueDiagnosis> createDiagnosValue(List<Diagnos> diagnoses) {
        if (diagnoses == null) {
            return Collections.emptyList();
        }

        final List<CertificateDataValueDiagnosis> newDiagnoses = new ArrayList<>();
        for (int i = 0; i < diagnoses.size(); i++) {
            final var diagnosis = diagnoses.get(i);
            if (isInvalid(diagnosis)) {
                continue;
            }

            newDiagnoses.add(createDiagnosis(Integer.toString(i + 1), diagnosis));
        }

        return newDiagnoses;
    }

    private static boolean isInvalid(Diagnos diagnos) {
        return diagnos.getDiagnosKod() == null;
    }

    private static CertificateDataValueDiagnosis createDiagnosis(String id, Diagnos diagnos) {
        return CertificateDataValueDiagnosis.builder()
            .id(id)
            .terminology(diagnos.getDiagnosKodSystem())
            .code(diagnos.getDiagnosKod())
            .description(diagnos.getDiagnosBeskrivning())
            .build();
    }

    private static CertificateDataElement createFunktionsnedsattningCategory(int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(CATEGORY_FUNKTIONSNEDSATTNING)
            .index(index)
            .config(
                CertificateDataConfigCategory.builder()
                    .text(texts.get(FUNKTIONSNEDSATTNING_CATEGORY_TEXT))
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationHide.builder()
                        .questionId(AVSTANGNING_SMITTSKYDD_SVAR_ID_27)
                        .expression(singleExpression(AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27))
                        .build()
                }
            )
            .build();
    }

    public static CertificateDataElement createFunktionsnedsattningQuestion(String value, int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(FUNKTIONSNEDSATTNING_SVAR_ID_35)
            .index(index)
            .parent(CATEGORY_FUNKTIONSNEDSATTNING)
            .config(
                CertificateDataConfigTextArea.builder()
                    .header(texts.get(FUNKTIONSNEDSATTNING_SVAR_TEXT))
                    .text(texts.get(FUNKTIONSNEDSATTNING_DELSVAR_TEXT))
                    .description(texts.get(FUNKTIONSNEDSATTNING_DELSVAR_BESKRIVNING))
                    .id(FUNKTIONSNEDSATTNING_SVAR_JSON_ID_35)
                    .build()
            )
            .value(
                CertificateDataTextValue.builder()
                    .id(FUNKTIONSNEDSATTNING_SVAR_JSON_ID_35)
                    .text(value)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(FUNKTIONSNEDSATTNING_SVAR_ID_35)
                        .expression(
                            singleExpression(FUNKTIONSNEDSATTNING_SVAR_JSON_ID_35)
                        )
                        .build(),
                    CertificateDataValidationHide.builder()
                        .questionId(AVSTANGNING_SMITTSKYDD_SVAR_ID_27)
                        .expression(singleExpression(AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27))
                        .build()
                }
            )
            .build();
    }

    public static CertificateDataElement createAktivitetsbegransningQuestion(String value, int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(AKTIVITETSBEGRANSNING_SVAR_ID_17)
            .index(index)
            .parent(CATEGORY_FUNKTIONSNEDSATTNING)
            .config(
                CertificateDataConfigTextArea.builder()
                    .header(texts.get(AKTIVITETSBEGRANSNING_SVAR_TEXT))
                    .text(texts.get(AKTIVITETSBEGRANSNING_DELSVAR_TEXT))
                    .description(texts.get(AKTIVITETSBEGRANSNING_DELSVAR_BESKRIVNING))
                    .id(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_17)
                    .build()
            )
            .value(
                CertificateDataTextValue.builder()
                    .id(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_17)
                    .text(value)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(AKTIVITETSBEGRANSNING_SVAR_ID_17)
                        .expression(
                            singleExpression(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_17)
                        )
                        .build(),
                    CertificateDataValidationHide.builder()
                        .questionId(AVSTANGNING_SMITTSKYDD_SVAR_ID_27)
                        .expression(singleExpression(AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27))
                        .build()
                }
            )
            .build();
    }

    private static CertificateDataElement createMedicinskaBehandlingarCategory(int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(CATEGORY_MEDICINSKABEHANDLINGAR)
            .index(index)
            .config(
                CertificateDataConfigCategory.builder()
                    .text(texts.get(MEDICINSKABEHANDLINGAR_CATEGORY_TEXT))
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationHide.builder()
                        .questionId(AVSTANGNING_SMITTSKYDD_SVAR_ID_27)
                        .expression(singleExpression(AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27))
                        .build()
                }
            )
            .build();
    }

    public static CertificateDataElement createPagaendeBehandlingQuestion(String value, int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(PAGAENDEBEHANDLING_SVAR_ID_19)
            .index(index)
            .parent(CATEGORY_MEDICINSKABEHANDLINGAR)
            .config(
                CertificateDataConfigTextArea.builder()
                    .header(texts.get(PAGAENDEBEHANDLING_SVAR_TEXT))
                    .text(texts.get(PAGAENDEBEHANDLING_DELSVAR_TEXT))
                    .id(PAGAENDEBEHANDLING_SVAR_JSON_ID_19)
                    .build()
            )
            .value(
                CertificateDataTextValue.builder()
                    .id(PAGAENDEBEHANDLING_SVAR_JSON_ID_19)
                    .text(value)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationHide.builder()
                        .questionId(AVSTANGNING_SMITTSKYDD_SVAR_ID_27)
                        .expression(singleExpression(AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27))
                        .build()
                }
            )
            .build();
    }

    public static CertificateDataElement createPlaneradBehandlingQuestion(String value, int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(PLANERADBEHANDLING_SVAR_ID_20)
            .index(index)
            .parent(CATEGORY_MEDICINSKABEHANDLINGAR)
            .config(
                CertificateDataConfigTextArea.builder()
                    .header(texts.get(PLANERADBEHANDLING_SVAR_TEXT))
                    .text(texts.get(PLANERADBEHANDLING_DELSVAR_TEXT))
                    .id(PLANERADBEHANDLING_SVAR_JSON_ID_20)
                    .build()
            )
            .value(
                CertificateDataTextValue.builder()
                    .id(PLANERADBEHANDLING_SVAR_JSON_ID_20)
                    .text(value)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationHide.builder()
                        .questionId(AVSTANGNING_SMITTSKYDD_SVAR_ID_27)
                        .expression(singleExpression(AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27))
                        .build()
                }
            )
            .build();
    }

    private static CertificateDataElement createBedomningCategory(int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(CATEGORY_BEDOMNING)
            .index(index)
            .config(
                CertificateDataConfigCategory.builder()
                    .text(texts.get(BEDOMNING_CATEGORY_TEXT))
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{}
            )
            .build();
    }

    public static CertificateDataElement createBehovAvSjukskrivningQuestion(List<Sjukskrivning> list, int index,
        CertificateTextProvider texts, Relation relation) {
        return CertificateDataElement.builder()
            .id(BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32)
            .index(index)
            .parent(CATEGORY_BEDOMNING)
            .config(
                CertificateDataConfigSickLeavePeriod.builder()
                    .text(texts.get(BEHOV_AV_SJUKSKRIVNING_SVAR_TEXT))
                    .description(texts.get(BEHOV_AV_SJUKSKRIVNING_SVAR_BESKRIVNING))
                    .list(
                        Arrays.asList(
                            CheckboxDateRange.builder()
                                .id(SjukskrivningsGrad.NEDSATT_1_4.getId())
                                .label(texts.get(BEHOV_AV_SJUKSKRIVNING_EN_FJARDEDEL))
                                .build(),
                            CheckboxDateRange.builder()
                                .id(SjukskrivningsGrad.NEDSATT_HALFTEN.getId())
                                .label(texts.get(BEHOV_AV_SJUKSKRIVNING_HALFTEN))
                                .build(),
                            CheckboxDateRange.builder()
                                .id(SjukskrivningsGrad.NEDSATT_3_4.getId())
                                .label(texts.get(BEHOV_AV_SJUKSKRIVNING_TRE_FJARDEDEL))
                                .build(),
                            CheckboxDateRange.builder()
                                .id(SjukskrivningsGrad.HELT_NEDSATT.getId())
                                .label(texts.get(BEHOV_AV_SJUKSKRIVNING_HELT_NEDSATT))
                                .build()
                        )
                    )
                    .previousSickLeavePeriod(getPreviousSickLeavePeriod(relation))
                    .build()
            )
            .value(
                CertificateDataValueDateRangeList.builder()
                    .list(createSjukskrivningValue(list))
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32)
                        .expression(multipleOrExpression(
                            singleExpression(SjukskrivningsGrad.NEDSATT_1_4.getId()),
                            singleExpression(SjukskrivningsGrad.NEDSATT_HALFTEN.getId()),
                            singleExpression(SjukskrivningsGrad.NEDSATT_3_4.getId()),
                            singleExpression(SjukskrivningsGrad.HELT_NEDSATT.getId())
                        ))
                        .build()
                }
            )
            .build();
    }

    private static String getPreviousSickLeavePeriod(Relation relation) {
        return hasRenewalRelation(relation) ? getPreviousSickLeavePeriodText(relation) : null;
    }

    private static String getPreviousSickLeavePeriodText(Relation relation) {
        return String.format(
            "På det ursprungliga intyget var slutdatumet för den sista sjukskrivningsperioden %s och sjukskrivningsgraden var %s.",
            DateTimeFormatter.ofPattern("yyyy-MM-dd").format(relation.getSistaGiltighetsDatum()),
            relation.getSistaSjukskrivningsgrad()
        );
    }

    private static boolean hasRenewalRelation(Relation relation) {
        return relation != null && relation.getRelationKod() == RelationKod.FRLANG;
    }

    private static List<CertificateDataValueDateRange> createSjukskrivningValue(List<Sjukskrivning> sickLeaves) {
        if (sickLeaves == null) {
            return Collections.emptyList();
        }
        return sickLeaves.stream()
            .filter(item -> item.getPeriod() != null && item.getPeriod().isValid())
            .map(item -> CertificateDataValueDateRange.builder()
                .id(Objects.requireNonNull(item.getSjukskrivningsgrad()).getId())
                .to(Objects.requireNonNull(item.getPeriod()).getTom().asLocalDate())
                .from(item.getPeriod().getFrom().asLocalDate())
                .build()
            ).collect(Collectors.toList());
    }

    public static CertificateDataElement createForsakringsmedicinsktBeslutsstodQuestion(String value, int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_ID_37)
            .index(index)
            .parent(CATEGORY_BEDOMNING)
            .config(
                CertificateDataConfigTextArea.builder()
                    .text(texts.get(FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_TEXT))
                    .description(texts.get(FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_BESKRIVNING))
                    .id(FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_JSON_ID_37)
                    .build()
            )
            .value(
                CertificateDataTextValue.builder()
                    .id(FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_JSON_ID_37)
                    .text(value)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationHide.builder()
                        .questionId(AVSTANGNING_SMITTSKYDD_SVAR_ID_27)
                        .expression(singleExpression(AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27))
                        .build()
                }
            )
            .build();
    }

    public static CertificateDataElement createArbetstidsforlaggningQuestion(Boolean value, int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(ARBETSTIDSFORLAGGNING_SVAR_ID_33)
            .index(index)
            .parent(CATEGORY_BEDOMNING)
            .config(
                CertificateDataConfigRadioBoolean.builder()
                    .id(ARBETSTIDSFORLAGGNING_SVAR_JSON_ID_33)
                    .text(texts.get(ARBETSTIDSFORLAGGNING_SVAR_TEXT))
                    .description(texts.get(ARBETSTIDSFORLAGGNING_SVAR_BESKRIVNING))
                    .selectedText(texts.get(ANSWER_YES))
                    .unselectedText(texts.get(ANSWER_NO))
                    .build()
            )
            .value(
                CertificateDataValueBoolean.builder()
                    .id(ARBETSTIDSFORLAGGNING_SVAR_JSON_ID_33)
                    .selected(value)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationShow.builder()
                        .questionId(BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32)
                        .expression(multipleOrExpression(
                            singleExpression(
                                se.inera.intyg.common.lisjp.model.internal.Sjukskrivning.SjukskrivningsGrad.NEDSATT_1_4.getId()),
                            singleExpression(
                                se.inera.intyg.common.lisjp.model.internal.Sjukskrivning.SjukskrivningsGrad.NEDSATT_HALFTEN.getId()),
                            singleExpression(
                                se.inera.intyg.common.lisjp.model.internal.Sjukskrivning.SjukskrivningsGrad.NEDSATT_3_4.getId())
                        ))
                        .build(),
                    CertificateDataValidationMandatory.builder()
                        .questionId(ARBETSTIDSFORLAGGNING_SVAR_ID_33)
                        .expression(singleExpression(ARBETSTIDSFORLAGGNING_SVAR_JSON_ID_33))
                        .build(),
                    CertificateDataValidationHide.builder()
                        .questionId(AVSTANGNING_SMITTSKYDD_SVAR_ID_27)
                        .expression(singleExpression(AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27))
                        .build()
                }
            )
            .build();
    }

    public static CertificateDataElement createMotiveringArbetstidsforlaggningQuestion(String value, int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_ID_33)
            .index(index)
            .parent(CATEGORY_BEDOMNING)
            .config(
                CertificateDataConfigTextArea.builder()
                    .text(texts.get(ARBETSTIDSFORLAGGNING_MOTIVERING_TEXT))
                    .id(ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_JSON_ID_33)
                    .build()
            )
            .value(
                CertificateDataTextValue.builder()
                    .id(ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_JSON_ID_33)
                    .text(value)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationShow.builder()
                        .questionId(ARBETSTIDSFORLAGGNING_SVAR_ID_33)
                        .expression(
                            singleExpression(ARBETSTIDSFORLAGGNING_SVAR_JSON_ID_33)
                        )
                        .build(),
                    CertificateDataValidationMandatory.builder()
                        .questionId(ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_ID_33)
                        .expression(
                            singleExpression(ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_JSON_ID_33)
                        )
                        .build(),
                    CertificateDataValidationHide.builder()
                        .questionId(AVSTANGNING_SMITTSKYDD_SVAR_ID_27)
                        .expression(singleExpression(AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27))
                        .build()
                }
            )
            .build();
    }

    public static CertificateDataElement createArbetsresorQuestion(Boolean value, int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(ARBETSRESOR_SVAR_ID_34)
            .index(index)
            .parent(CATEGORY_BEDOMNING)
            .config(
                CertificateDataConfigCheckboxBoolean.builder()
                    .id(ARBETSRESOR_SVAR_JSON_ID_34)
                    .label(texts.get(ARBETSRESOR_SVAR_TEXT))
                    .selectedText(texts.get(ANSWER_YES))
                    .unselectedText(texts.get(ANSWER_NO))
                    .build()
            )
            .value(
                CertificateDataValueBoolean.builder()
                    .id(ARBETSRESOR_SVAR_JSON_ID_34)
                    .selected(value)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationHide.builder()
                        .questionId(AVSTANGNING_SMITTSKYDD_SVAR_ID_27)
                        .expression(singleExpression(AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27))
                        .build()
                }
            )
            .build();
    }

    public static CertificateDataElement createPrognosQuestion(Prognos prognos, int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(PROGNOS_SVAR_ID_39)
            .index(index)
            .parent(CATEGORY_BEDOMNING)
            .config(
                CertificateDataConfigRadioMultipleCodeOptionalDropdown.builder()
                    .text(texts.get(PROGNOS_SVAR_TEXT))
                    .description(texts.get(PROGNOS_SVAR_BESKRIVNING))
                    .list(
                        Arrays.asList(
                            RadioMultipleCodeOptionalDropdown.builder()
                                .id(PrognosTyp.MED_STOR_SANNOLIKHET.getId())
                                .label(texts.get(PROGNOS_SVAR_STOR_SANNOLIKHET))
                                .build(),
                            RadioMultipleCodeOptionalDropdown.builder()
                                .id(PrognosTyp.ATER_X_ANTAL_DGR.getId())
                                .label(texts.get(PROGNOS_SVAR_ATER_X_ANTAL_DAGAR))
                                .dropdownQuestionId(RespConstants.PROGNOS_BESKRIVNING_DELSVAR_ID_39)
                                .build(),
                            RadioMultipleCodeOptionalDropdown.builder()
                                .id(PrognosTyp.SANNOLIKT_EJ_ATERGA_TILL_SYSSELSATTNING.getId())
                                .label(texts.get(PROGNOS_SVAR_SANNOLIKT_INTE))
                                .build(),
                            RadioMultipleCodeOptionalDropdown.builder()
                                .id(PrognosTyp.PROGNOS_OKLAR.getId())
                                .label(texts.get(PROGNOS_SVAR_PROGNOS_OKLAR))
                                .build()
                        )
                    )
                    .build()
            )
            .value(
                CertificateDataValueCode.builder()
                    .id(getPrognosValue(prognos))
                    .code(getPrognosValue(prognos))
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(PROGNOS_SVAR_ID_39)
                        .expression(
                            multipleOrExpression(
                                singleExpression(PrognosTyp.MED_STOR_SANNOLIKHET.getId()),
                                singleExpression(PrognosTyp.ATER_X_ANTAL_DGR.getId()),
                                singleExpression(PrognosTyp.SANNOLIKT_EJ_ATERGA_TILL_SYSSELSATTNING.getId()),
                                singleExpression(PrognosTyp.PROGNOS_OKLAR.getId())
                            )
                        )
                        .build(),
                    CertificateDataValidationHide.builder()
                        .questionId(AVSTANGNING_SMITTSKYDD_SVAR_ID_27)
                        .expression(singleExpression(AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27))
                        .build()
                }
            )
            .build();
    }

    private static String getPrognosValue(Prognos prognos) {
        return (prognos != null && prognos.getTyp() != null) ? prognos.getTyp().getId() : null;
    }

    public static CertificateDataElement createPrognosTimeperiodQuestion(Prognos prognos, int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(PROGNOS_BESKRIVNING_DELSVAR_ID_39)
            .index(index)
            .parent(CATEGORY_BEDOMNING)
            .config(
                CertificateDataConfigDropdown.builder()
                    .list(
                        Arrays.asList(
                            DropdownItem.builder()
                                .id("")
                                .label("Välj tidsperiod")
                                .build(),
                            DropdownItem.builder()
                                .id(PrognosDagarTillArbeteTyp.DAGAR_30.getId())
                                .label(texts.get(PROGNOS_DAGAR_30))
                                .build(),
                            DropdownItem.builder()
                                .id(PrognosDagarTillArbeteTyp.DAGAR_60.getId())
                                .label(texts.get(PROGNOS_DAGAR_60))
                                .build(),
                            DropdownItem.builder()
                                .id(PrognosDagarTillArbeteTyp.DAGAR_90.getId())
                                .label(texts.get(PROGNOS_DAGAR_90))
                                .build(),
                            DropdownItem.builder()
                                .id(PrognosDagarTillArbeteTyp.DAGAR_180.getId())
                                .label(texts.get(PROGNOS_DAGAR_180))
                                .build(),
                            DropdownItem.builder()
                                .id(PrognosDagarTillArbeteTyp.DAGAR_365.getId())
                                .label(texts.get(PROGNOS_DAGAR_365))
                                .build()
                        )
                    )
                    .build()
            )
            .value(
                CertificateDataValueCode.builder()
                    .id(getPrognosDagarTillArbeteValue(prognos))
                    .code(getPrognosDagarTillArbeteValue(prognos))
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationHide.builder()
                        .questionId(AVSTANGNING_SMITTSKYDD_SVAR_ID_27)
                        .expression(singleExpression(AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27))
                        .build(),
                    CertificateDataValidationMandatory.builder()
                        .questionId(PROGNOS_BESKRIVNING_DELSVAR_ID_39)
                        .expression(
                            multipleOrExpression(
                                singleExpression(PrognosDagarTillArbeteTyp.DAGAR_30.getId()),
                                singleExpression(PrognosDagarTillArbeteTyp.DAGAR_60.getId()),
                                singleExpression(PrognosDagarTillArbeteTyp.DAGAR_90.getId()),
                                singleExpression(PrognosDagarTillArbeteTyp.DAGAR_180.getId()),
                                singleExpression(PrognosDagarTillArbeteTyp.DAGAR_365.getId())
                            )
                        )
                        .build(),
                    CertificateDataValidationEnable.builder()
                        .questionId(PROGNOS_SVAR_ID_39)
                        .expression(singleExpression(PrognosTyp.ATER_X_ANTAL_DGR.getId()))
                        .build()
                }
            )
            .style(CertificateDataElementStyleEnum.HIDDEN)
            .build();
    }

    private static String getPrognosDagarTillArbeteValue(Prognos prognos) {
        return prognos != null && prognos.getDagarTillArbete() != null ? prognos.getDagarTillArbete().getId() : null;
    }

    private static CertificateDataElement createAtgarderCategory(int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(CATEGORY_ATGARDER)
            .index(index)
            .config(
                CertificateDataConfigCategory.builder()
                    .text(texts.get(ATGARDER_CATEGORY_TEXT))
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationHide.builder()
                        .questionId(AVSTANGNING_SMITTSKYDD_SVAR_ID_27)
                        .expression(singleExpression(AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27))
                        .build()
                }
            )
            .build();
    }


    public static CertificateDataElement createAtgarderQuestion(List<ArbetslivsinriktadeAtgarder> atgarder, int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40)
            .index(index)
            .parent(CATEGORY_ATGARDER)
            .config(
                CertificateDataConfigCheckboxMultipleCode.builder()
                    .text(texts.get(ARBETSLIVSINRIKTADE_ATGARDER_TEXT))
                    .list(
                        Arrays.asList(
                            CheckboxMultipleCode.builder()
                                .id(ArbetslivsinriktadeAtgarderVal.INTE_AKTUELLT.getId())
                                .label(ArbetslivsinriktadeAtgarderVal.INTE_AKTUELLT.getLabel())
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(ArbetslivsinriktadeAtgarderVal.ARBETSTRANING.getId())
                                .label(ArbetslivsinriktadeAtgarderVal.ARBETSTRANING.getLabel())
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(ArbetslivsinriktadeAtgarderVal.ARBETSANPASSNING.getId())
                                .label(ArbetslivsinriktadeAtgarderVal.ARBETSANPASSNING.getLabel())
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(ArbetslivsinriktadeAtgarderVal.BESOK_PA_ARBETSPLATSEN.getId())
                                .label(ArbetslivsinriktadeAtgarderVal.BESOK_PA_ARBETSPLATSEN.getLabel())
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(ArbetslivsinriktadeAtgarderVal.ERGONOMISK_BEDOMNING.getId())
                                .label(ArbetslivsinriktadeAtgarderVal.ERGONOMISK_BEDOMNING.getLabel())
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(ArbetslivsinriktadeAtgarderVal.HJALPMEDEL.getId())
                                .label(ArbetslivsinriktadeAtgarderVal.HJALPMEDEL.getLabel())
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(ArbetslivsinriktadeAtgarderVal.KONTAKT_MED_FORETAGSHALSOVARD.getId())
                                .label(ArbetslivsinriktadeAtgarderVal.KONTAKT_MED_FORETAGSHALSOVARD.getLabel())
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(ArbetslivsinriktadeAtgarderVal.OMFORDELNING_AV_ARBETSUPPGIFTER.getId())
                                .label(ArbetslivsinriktadeAtgarderVal.OMFORDELNING_AV_ARBETSUPPGIFTER.getLabel())
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(ArbetslivsinriktadeAtgarderVal.OVRIGT.getId())
                                .label(ArbetslivsinriktadeAtgarderVal.OVRIGT.getLabel())
                                .build()
                        )
                    )
                    .build()
            )
            .value(
                CertificateDataValueCodeList.builder()
                    .list(createAtgarderCodeList(atgarder))
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40)
                        .expression(
                            multipleOrExpression(
                                singleExpression(ArbetslivsinriktadeAtgarderVal.INTE_AKTUELLT.getId()),
                                singleExpression(ArbetslivsinriktadeAtgarderVal.ARBETSTRANING.getId()),
                                singleExpression(ArbetslivsinriktadeAtgarderVal.ARBETSANPASSNING.getId()),
                                singleExpression(ArbetslivsinriktadeAtgarderVal.BESOK_PA_ARBETSPLATSEN.getId()),
                                singleExpression(ArbetslivsinriktadeAtgarderVal.ERGONOMISK_BEDOMNING.getId()),
                                singleExpression(ArbetslivsinriktadeAtgarderVal.HJALPMEDEL.getId()),
                                singleExpression(ArbetslivsinriktadeAtgarderVal.KONTAKT_MED_FORETAGSHALSOVARD.getId()),
                                singleExpression(ArbetslivsinriktadeAtgarderVal.OMFORDELNING_AV_ARBETSUPPGIFTER.getId()),
                                singleExpression(ArbetslivsinriktadeAtgarderVal.OVRIGT.getId())
                            )
                        )
                        .build(),
                    CertificateDataValidationDisable.builder()
                        .questionId(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40)
                        .expression(
                            multipleOrExpression(
                                singleExpression(ArbetslivsinriktadeAtgarderVal.ARBETSTRANING.getId()),
                                singleExpression(ArbetslivsinriktadeAtgarderVal.ARBETSANPASSNING.getId()),
                                singleExpression(ArbetslivsinriktadeAtgarderVal.BESOK_PA_ARBETSPLATSEN.getId()),
                                singleExpression(ArbetslivsinriktadeAtgarderVal.ERGONOMISK_BEDOMNING.getId()),
                                singleExpression(ArbetslivsinriktadeAtgarderVal.HJALPMEDEL.getId()),
                                singleExpression(ArbetslivsinriktadeAtgarderVal.KONTAKT_MED_FORETAGSHALSOVARD.getId()),
                                singleExpression(ArbetslivsinriktadeAtgarderVal.OMFORDELNING_AV_ARBETSUPPGIFTER.getId()),
                                singleExpression(ArbetslivsinriktadeAtgarderVal.OVRIGT.getId())
                            )
                        )
                        .id(Collections.singletonList(ArbetslivsinriktadeAtgarderVal.INTE_AKTUELLT.getId()))
                        .build(),
                    CertificateDataValidationDisable.builder()
                        .questionId(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40)
                        .expression(
                            singleExpression(ArbetslivsinriktadeAtgarderVal.INTE_AKTUELLT.getId())
                        )
                        .id(
                            Arrays.asList(
                                ArbetslivsinriktadeAtgarderVal.ARBETSTRANING.getId(),
                                ArbetslivsinriktadeAtgarderVal.ARBETSANPASSNING.getId(),
                                ArbetslivsinriktadeAtgarderVal.BESOK_PA_ARBETSPLATSEN.getId(),
                                ArbetslivsinriktadeAtgarderVal.ERGONOMISK_BEDOMNING.getId(),
                                ArbetslivsinriktadeAtgarderVal.HJALPMEDEL.getId(),
                                ArbetslivsinriktadeAtgarderVal.KONTAKT_MED_FORETAGSHALSOVARD.getId(),
                                ArbetslivsinriktadeAtgarderVal.OMFORDELNING_AV_ARBETSUPPGIFTER.getId(),
                                ArbetslivsinriktadeAtgarderVal.OVRIGT.getId()
                            )
                        )
                        .build(),
                    CertificateDataValidationHide.builder()
                        .questionId(AVSTANGNING_SMITTSKYDD_SVAR_ID_27)
                        .expression(singleExpression(AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27))
                        .build()
                }
            )
            .build();
    }

    private static List<CertificateDataValueCode> createAtgarderCodeList(List<ArbetslivsinriktadeAtgarder> atgarder) {
        if (atgarder == null) {
            return Collections.emptyList();
        }

        return atgarder.stream()
            .map(atgard -> CertificateDataValueCode.builder()
                .id(atgard.getTyp().getId())
                .code(atgard.getTyp().getId())
                .build())
            .collect(Collectors.toList());
    }

    public static CertificateDataElement createAtgarderBeskrivning(String value, int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_ID_44)
            .index(index)
            .parent(CATEGORY_ATGARDER)
            .config(
                CertificateDataConfigTextArea.builder()
                    .text(texts.get(ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_TEXT))
                    .id(ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_JSON_ID_44)
                    .build()
            )
            .value(
                CertificateDataTextValue.builder()
                    .id(ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_JSON_ID_44)
                    .text(value)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationShow.builder()
                        .questionId(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40)
                        .expression(
                            multipleOrExpression(
                                singleExpression(ArbetslivsinriktadeAtgarderVal.ARBETSTRANING.getId()),
                                singleExpression(ArbetslivsinriktadeAtgarderVal.ARBETSANPASSNING.getId()),
                                singleExpression(ArbetslivsinriktadeAtgarderVal.BESOK_PA_ARBETSPLATSEN.getId()),
                                singleExpression(ArbetslivsinriktadeAtgarderVal.ERGONOMISK_BEDOMNING.getId()),
                                singleExpression(ArbetslivsinriktadeAtgarderVal.HJALPMEDEL.getId()),
                                singleExpression(ArbetslivsinriktadeAtgarderVal.KONTAKT_MED_FORETAGSHALSOVARD.getId()),
                                singleExpression(ArbetslivsinriktadeAtgarderVal.OMFORDELNING_AV_ARBETSUPPGIFTER.getId()),
                                singleExpression(ArbetslivsinriktadeAtgarderVal.OVRIGT.getId())
                            )
                        )
                        .build(),
                    CertificateDataValidationHide.builder()
                        .questionId(AVSTANGNING_SMITTSKYDD_SVAR_ID_27)
                        .expression(singleExpression(AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27))
                        .build()
                }
            )
            .build();
    }

    private static CertificateDataElement createOvrigtCategory(int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(CATEGORY_OVRIGT)
            .index(index)
            .config(
                CertificateDataConfigCategory.builder()
                    .text(texts.get(OVRIGT_CATEGORY_TEXT))
                    .build()
            )
            .build();
    }

    public static CertificateDataElement createOvrigtQuestion(String value, int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(OVRIGT_SVAR_ID_25)
            .index(index)
            .parent(CATEGORY_OVRIGT)
            .config(
                CertificateDataConfigTextArea.builder()
                    .id(OVRIGT_SVAR_JSON_ID_25)
                    .text(texts.get(OVRIGT_SVAR_TEXT))
                    .build()
            )
            .value(
                CertificateDataTextValue.builder()
                    .id(OVRIGT_SVAR_JSON_ID_25)
                    .text(value)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationText.builder()
                        .id(OVRIGT_SVAR_JSON_ID_25)
                        .limit(LIMIT_OVRIGT)
                        .build()
                }
            )
            .build();
    }

    private static CertificateDataElement createKontaktCategory(int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(CATEGORY_KONTAKT)
            .index(index)
            .config(
                CertificateDataConfigCategory.builder()
                    .text(texts.get(KONTAKT_CATEGORY_TEXT))
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationHide.builder()
                        .questionId(AVSTANGNING_SMITTSKYDD_SVAR_ID_27)
                        .expression(singleExpression(AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27))
                        .build()
                }
            )
            .build();
    }

    public static CertificateDataElement createKontaktQuestion(Boolean value, int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(KONTAKT_ONSKAS_SVAR_ID_103)
            .index(index)
            .parent(CATEGORY_KONTAKT)
            .config(
                CertificateDataConfigCheckboxBoolean.builder()
                    .id(KONTAKT_ONSKAS_SVAR_JSON_ID_103)
                    .text(texts.get(KONTAKT_ONSKAS_SVAR_TEXT))
                    .selectedText(texts.get(ANSWER_YES))
                    .unselectedText(texts.get(ANSWER_NO))
                    .label(texts.get(KONTAKT_ONSKAS_DELSVAR_TEXT))
                    .build()
            )
            .value(
                CertificateDataValueBoolean.builder()
                    .id(KONTAKT_ONSKAS_SVAR_JSON_ID_103)
                    .selected(value)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationHide.builder()
                        .questionId(AVSTANGNING_SMITTSKYDD_SVAR_ID_27)
                        .expression(singleExpression(AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27))
                        .build()
                }
            )
            .build();
    }

    public static CertificateDataElement createKontaktBeskrivning(String value, int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(ANLEDNING_TILL_KONTAKT_DELSVAR_ID_103)
            .index(index)
            .parent(CATEGORY_KONTAKT)
            .config(
                CertificateDataConfigTextArea.builder()
                    .id(ANLEDNING_TILL_KONTAKT_DELSVAR_JSON_ID_103)
                    .text(texts.get(ANLEDNING_TILL_KONTAKT_DELSVAR_TEXT))
                    .build()
            )
            .value(
                CertificateDataTextValue.builder()
                    .id(ANLEDNING_TILL_KONTAKT_DELSVAR_JSON_ID_103)
                    .text(value)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationShow.builder()
                        .questionId(KONTAKT_ONSKAS_SVAR_ID_103)
                        .expression(
                            singleExpression(KONTAKT_ONSKAS_SVAR_JSON_ID_103)
                        )
                        .build(),
                    CertificateDataValidationHide.builder()
                        .questionId(AVSTANGNING_SMITTSKYDD_SVAR_ID_27)
                        .expression(singleExpression(AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27))
                        .build()
                }
            )
            .build();
    }
}
