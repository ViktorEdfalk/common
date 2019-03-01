package se.inera.intyg.common.ts_tstrk1062.v1.validator;

import static org.junit.Assert.*;
import static se.inera.intyg.common.ts_tstrk1062.v1.model.converter.TSTRK1062Constants.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.common.internal.*;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.common.ts_parent.codes.IdKontrollKod;
import se.inera.intyg.common.ts_tstrk1062.v1.model.internal.*;
import se.inera.intyg.schemas.contract.Personnummer;

@RunWith(MockitoJUnitRunner.class)
public class InternalValidatorInstanceImplTest {

    @InjectMocks
    InternalValidatorInstanceImpl validator;

    TsTstrk1062UtlatandeV1.Builder builderTemplate;

    @Before
    public void setUp() throws Exception {
        builderTemplate = TsTstrk1062UtlatandeV1.builder()
                .setId("intygsId")
                .setGrundData(buildGrundData(LocalDateTime.now()))
                .setIntygAvser(IntygAvser.create(EnumSet.of(IntygAvser.BehorighetsTyp.IAV11)))
                .setIdKontroll(IdKontroll.create(IdKontrollKod.KORKORT))
                .setDiagnosRegistrering(DiagnosRegistrering.create(DiagnosRegistrering.DiagnosRegistreringsTyp.DIAGNOS_FRITEXT))
                .setDiagnosFritext(DiagnosFritext.create("Diagnoser", "2017"))
                .setLakemedelsbehandling(Lakemedelsbehandling.create(false, false, "", false, false, false, null, ""))
                .setBedomningAvSymptom("Bedömning av symptom")
                .setPrognosTillstand(PrognosTillstand.create(PrognosTillstand.PrognosTillstandTyp.JA))
                .setOvrigaKommentarer(null)
                .setBedomning(Bedomning.builder().setUppfyllerBehorighetskrav(EnumSet.of(Bedomning.BehorighetsTyp.VAR11)).build())
                .setTextVersion("");

    }

    @Test
    public void validateDraft() throws Exception {
        final TsTstrk1062UtlatandeV1 utlatande = builderTemplate.build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);

        assertFalse("Shouldn't have error messages", validateDraftResponse.hasErrorMessages());
    }

    @Test
    public void validateNullIntygAvser() throws Exception {
        final TsTstrk1062UtlatandeV1 utlatande = builderTemplate
                .setIntygAvser(null)
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessage = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessage, INTYG_AVSER_CATEGORY, INTYG_AVSER_SVAR_JSON_ID);
    }

    @Test
    public void validateMissingIntygAvser() throws Exception {
        final TsTstrk1062UtlatandeV1 utlatande = builderTemplate
                .setIntygAvser(IntygAvser.create(EnumSet.noneOf(IntygAvser.BehorighetsTyp.class)))
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessage = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessage, INTYG_AVSER_CATEGORY,
                INTYG_AVSER_SVAR_JSON_ID + PUNKT + INTYG_AVSER_DELSVAR_JSON_ID);
    }

    @Test
    public void validateNullIdKontroll() throws Exception {
        final TsTstrk1062UtlatandeV1 utlatande = builderTemplate
                .setIdKontroll(null)
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessage = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessage, ID_KONTROLL_CATEGORY,
                ID_KONTROLL_SVAR_JSON_ID + PUNKT + ID_KONTROLL_DELSVAR_JSON_ID);
    }

    @Test
    public void validateNullDiagnosRegistrering() throws Exception {
        final TsTstrk1062UtlatandeV1 utlatande = builderTemplate
                .setDiagnosRegistrering(null)
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessage = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessage, ALLMANT_KATEGORI,
                ALLMANT_INMATNING_SVAR_JSON_ID + PUNKT + ALLMANT_INMATNING_DELSVAR_JSON_ID);
    }

    @Test
    public void validateNullDiagnosRegistreringTyp() throws Exception {
        final TsTstrk1062UtlatandeV1 utlatande = builderTemplate
                .setDiagnosRegistrering(DiagnosRegistrering.create(null))
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessage = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessage, ALLMANT_KATEGORI,
                ALLMANT_INMATNING_SVAR_JSON_ID + PUNKT + ALLMANT_INMATNING_DELSVAR_JSON_ID);
    }

    @Test
    public void validateNullDiagnosFritext() throws Exception {
        final TsTstrk1062UtlatandeV1 utlatande = builderTemplate
                .setDiagnosRegistrering(DiagnosRegistrering.create(DiagnosRegistrering.DiagnosRegistreringsTyp.DIAGNOS_FRITEXT))
                .setDiagnosFritext(null)
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessage = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessage, ALLMANT_KATEGORI,
                ALLMANT_DIAGNOSKOD_FRITEXT_SVAR_JSON_ID + PUNKT + ALLMANT_DIAGNOSKOD_FRITEXT_FRITEXT_DELSVAR_JSON_ID);
    }

    @Test
    public void validateNullDiagnosFritextValues() throws Exception {
        final TsTstrk1062UtlatandeV1 utlatande = builderTemplate
                .setDiagnosRegistrering(DiagnosRegistrering.create(DiagnosRegistrering.DiagnosRegistreringsTyp.DIAGNOS_FRITEXT))
                .setDiagnosFritext(DiagnosFritext.create(null, null))
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessages = validateDraftResponse.getValidationErrors();

        assertEquals("Should have two error messages", 2, validationMessages.size());

        final Map<String, ValidationMessage> validationsMap = buildMapFromMessages(validationMessages);

        assertValidationMessage(validationsMap, ALLMANT_KATEGORI,
                ALLMANT_DIAGNOSKOD_FRITEXT_SVAR_JSON_ID + PUNKT + ALLMANT_DIAGNOSKOD_FRITEXT_FRITEXT_DELSVAR_JSON_ID);

        assertValidationMessage(validationsMap, ALLMANT_KATEGORI,
                ALLMANT_DIAGNOSKOD_FRITEXT_SVAR_JSON_ID + PUNKT + ALLMANT_DIAGNOSKOD_FRITEXT_ARTAL_DELSVAR_JSON_ID);
    }

    @Test
    public void validateEmptyDiagnosFritextValues() throws Exception {
        final TsTstrk1062UtlatandeV1 utlatande = builderTemplate
                .setDiagnosRegistrering(DiagnosRegistrering.create(DiagnosRegistrering.DiagnosRegistreringsTyp.DIAGNOS_FRITEXT))
                .setDiagnosFritext(DiagnosFritext.create("", ""))
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessages = validateDraftResponse.getValidationErrors();

        assertEquals("Should have two error messages", 2, validationMessages.size());

        final Map<String, ValidationMessage> validationsMap = buildMapFromMessages(validationMessages);

        assertValidationMessage(validationsMap, ALLMANT_KATEGORI,
                ALLMANT_DIAGNOSKOD_FRITEXT_SVAR_JSON_ID + PUNKT + ALLMANT_DIAGNOSKOD_FRITEXT_FRITEXT_DELSVAR_JSON_ID);

        assertValidationMessage(validationsMap, ALLMANT_KATEGORI,
                ALLMANT_DIAGNOSKOD_FRITEXT_SVAR_JSON_ID + PUNKT + ALLMANT_DIAGNOSKOD_FRITEXT_ARTAL_DELSVAR_JSON_ID);
    }

    @Test
    public void validateInvalidDiagnosFritextYear() throws Exception {
        final TsTstrk1062UtlatandeV1 utlatande = builderTemplate
                .setDiagnosRegistrering(DiagnosRegistrering.create(DiagnosRegistrering.DiagnosRegistreringsTyp.DIAGNOS_FRITEXT))
                .setDiagnosFritext(DiagnosFritext.create("Diagnoser", "Årtal"))
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessages = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessages, ValidationMessageType.INVALID_FORMAT, ALLMANT_KATEGORI,
                ALLMANT_DIAGNOSKOD_FRITEXT_SVAR_JSON_ID + PUNKT + ALLMANT_DIAGNOSKOD_FRITEXT_ARTAL_DELSVAR_JSON_ID);
    }

    @Test
    public void validateFutureDiagnosFritextYear() throws Exception {
        final String futureYear = Integer.toString(LocalDate.now().plusYears(1).getYear());

        final TsTstrk1062UtlatandeV1 utlatande = builderTemplate
                .setDiagnosRegistrering(DiagnosRegistrering.create(DiagnosRegistrering.DiagnosRegistreringsTyp.DIAGNOS_FRITEXT))
                .setDiagnosFritext(DiagnosFritext.create("Diagnoser", futureYear))
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessages = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessages, ValidationMessageType.OTHER, ALLMANT_KATEGORI,
                ALLMANT_DIAGNOSKOD_FRITEXT_SVAR_JSON_ID + PUNKT + ALLMANT_DIAGNOSKOD_FRITEXT_ARTAL_DELSVAR_JSON_ID);
    }

    @Test
    public void validateNullDiagnosKodad() throws Exception {
        final TsTstrk1062UtlatandeV1 utlatande = builderTemplate
                .setDiagnosRegistrering(DiagnosRegistrering.create(DiagnosRegistrering.DiagnosRegistreringsTyp.DIAGNOS_KODAD))
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessages = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessages, ALLMANT_KATEGORI,
                ALLMANT_DIAGNOSKOD_KODAD_SVAR_JSON_ID);
    }

    @Test
    public void validateEmptyDiagnosKodad() throws Exception {
        final TsTstrk1062UtlatandeV1 utlatande = builderTemplate
                .setDiagnosRegistrering(DiagnosRegistrering.create(DiagnosRegistrering.DiagnosRegistreringsTyp.DIAGNOS_KODAD))
                .setDiagnosKodad(new ArrayList<DiagnosKodad>())
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessages = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessages, ALLMANT_KATEGORI,
                ALLMANT_DIAGNOSKOD_KODAD_SVAR_JSON_ID);
    }

    @Test
    public void validateNullDiagnos() throws Exception {
        final DiagnosKodad diagnosKodad = DiagnosKodad.create(null,
                null, null, null, null);

        final List<DiagnosKodad> diagnosKodadList = new ArrayList<DiagnosKodad>(1);
        diagnosKodadList.add(diagnosKodad);

        final TsTstrk1062UtlatandeV1 utlatande = builderTemplate
                .setDiagnosRegistrering(DiagnosRegistrering.create(DiagnosRegistrering.DiagnosRegistreringsTyp.DIAGNOS_KODAD))
                .setDiagnosKodad(diagnosKodadList)
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessages = validateDraftResponse.getValidationErrors();

        assertEquals("Should have four error messages", 4, validationMessages.size());

        final Map<String, ValidationMessage> validationsMap = buildMapFromMessages(validationMessages);

        assertValidationMessage(validationsMap, ALLMANT_KATEGORI,
                ALLMANT_DIAGNOSKOD_KODAD_SVAR_JSON_ID + "[0]" + PUNKT + ALLMANT_DIAGNOSKOD_KODAD_KOD_DELSVAR_JSON_ID);

        assertValidationMessage(validationsMap, ALLMANT_KATEGORI,
                ALLMANT_DIAGNOSKOD_KODAD_SVAR_JSON_ID + "[0]" + PUNKT + ALLMANT_DIAGNOSKOD_KODAD_KOD_TEXT_DELSVAR_JSON_ID);

        assertValidationMessage(validationsMap, ALLMANT_KATEGORI,
                ALLMANT_DIAGNOSKOD_KODAD_SVAR_JSON_ID + "[0]" + PUNKT + ALLMANT_DIAGNOSKOD_KODAD_KOD_ARTAL_DELSVAR_JSON_ID);

        assertValidationMessage(validationsMap, ALLMANT_KATEGORI,
                ALLMANT_DIAGNOSKOD_KODAD_SVAR_JSON_ID + "[0]" + PUNKT + ALLMANT_DIAGNOSKOD_KODAD_KOD_KODSYSTEM_JSON_ID);
    }

    @Test
    public void validateEmptyDiagnos() throws Exception {
        final DiagnosKodad diagnosKodad = DiagnosKodad.create("", "",
                "", "", "");

        final List<DiagnosKodad> diagnosKodadList = new ArrayList<DiagnosKodad>(1);
        diagnosKodadList.add(diagnosKodad);

        final TsTstrk1062UtlatandeV1 utlatande = builderTemplate
                .setDiagnosRegistrering(DiagnosRegistrering.create(DiagnosRegistrering.DiagnosRegistreringsTyp.DIAGNOS_KODAD))
                .setDiagnosKodad(diagnosKodadList)
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessages = validateDraftResponse.getValidationErrors();

        assertEquals("Should have three error messages", 3, validationMessages.size());

        final Map<String, ValidationMessage> validationsMap = buildMapFromMessages(validationMessages);

        assertValidationMessage(validationsMap, ALLMANT_KATEGORI,
                ALLMANT_DIAGNOSKOD_KODAD_SVAR_JSON_ID + "[0]" + PUNKT + ALLMANT_DIAGNOSKOD_KODAD_KOD_DELSVAR_JSON_ID);

        assertValidationMessage(validationsMap, ALLMANT_KATEGORI,
                ALLMANT_DIAGNOSKOD_KODAD_SVAR_JSON_ID + "[0]" + PUNKT + ALLMANT_DIAGNOSKOD_KODAD_KOD_ARTAL_DELSVAR_JSON_ID);

        assertValidationMessage(validationsMap, ALLMANT_KATEGORI,
                ALLMANT_DIAGNOSKOD_KODAD_SVAR_JSON_ID + "[0]" + PUNKT + ALLMANT_DIAGNOSKOD_KODAD_KOD_KODSYSTEM_JSON_ID);
    }

    @Test
    public void validateInvalidDiagnosArtal() throws Exception {
        final DiagnosKodad diagnosKodad = DiagnosKodad.create("A01", "ICD10",
                "Diagnosbeskrivning", "A01 - Diagnosbeskrivning", "Årtal");

        final List<DiagnosKodad> diagnosKodadList = new ArrayList<DiagnosKodad>(1);
        diagnosKodadList.add(diagnosKodad);

        final TsTstrk1062UtlatandeV1 utlatande = builderTemplate
                .setDiagnosRegistrering(DiagnosRegistrering.create(DiagnosRegistrering.DiagnosRegistreringsTyp.DIAGNOS_KODAD))
                .setDiagnosKodad(diagnosKodadList)
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessages = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessages, ValidationMessageType.INVALID_FORMAT, ALLMANT_KATEGORI,
                ALLMANT_DIAGNOSKOD_KODAD_SVAR_JSON_ID + "[0]" + PUNKT + ALLMANT_DIAGNOSKOD_KODAD_KOD_ARTAL_DELSVAR_JSON_ID);
    }

    @Test
    public void validateFutureDiagnosArtal() throws Exception {
        final String futureYear = Integer.toString(LocalDate.now().plusYears(1).getYear());

        final DiagnosKodad diagnosKodad = DiagnosKodad.create("A01", "ICD10",
                "Diagnosbeskrivning", "A01 - Diagnosbeskrivning", futureYear);

        final List<DiagnosKodad> diagnosKodadList = new ArrayList<DiagnosKodad>(1);
        diagnosKodadList.add(diagnosKodad);

        final TsTstrk1062UtlatandeV1 utlatande = builderTemplate
                .setDiagnosRegistrering(DiagnosRegistrering.create(DiagnosRegistrering.DiagnosRegistreringsTyp.DIAGNOS_KODAD))
                .setDiagnosKodad(diagnosKodadList)
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessages = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessages, ValidationMessageType.OTHER, ALLMANT_KATEGORI,
                ALLMANT_DIAGNOSKOD_KODAD_SVAR_JSON_ID + "[0]" + PUNKT + ALLMANT_DIAGNOSKOD_KODAD_KOD_ARTAL_DELSVAR_JSON_ID);
    }

    @Test
    public void validateNullLakemedelsbehandling() throws Exception {
        final TsTstrk1062UtlatandeV1 utlatande = builderTemplate
                .setLakemedelsbehandling(null)
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessages = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessages, LAKEMEDELSBEHANDLING_KATEGORI,
                LAKEMEDELSBEHANDLING_FOREKOMMIT_SVAR_JSON_ID + PUNKT + LAKEMEDELSBEHANDLING_FOREKOMMIT_DELSVAR_JSON_ID);
    }

    @Test
    public void validateNullLakemedelsbehandlingHarHaft() throws Exception {
        final TsTstrk1062UtlatandeV1 utlatande = builderTemplate
                .setLakemedelsbehandling(Lakemedelsbehandling.create(null, null, null, null,
                        null, null, null, null))
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessages = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessages, LAKEMEDELSBEHANDLING_KATEGORI,
                LAKEMEDELSBEHANDLING_FOREKOMMIT_SVAR_JSON_ID + PUNKT + LAKEMEDELSBEHANDLING_FOREKOMMIT_DELSVAR_JSON_ID);
    }

    @Test
    public void validateNullLakemedelsbehandlingPagar() throws Exception {
        final TsTstrk1062UtlatandeV1 utlatande = builderTemplate
                .setLakemedelsbehandling(Lakemedelsbehandling.create(true, null, null, null,
                        null, null, null, null))
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessages = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessages, LAKEMEDELSBEHANDLING_KATEGORI,
                LAKEMEDELSBEHANDLING_FOREKOMMIT_SVAR_JSON_ID + PUNKT + LAKEMEDELSBEHANDLING_PAGAR_DELSVAR_JSON_ID);
    }

    @Test
    public void validateNullLakemedelsbehandlingAktuell() throws Exception {
        final TsTstrk1062UtlatandeV1 utlatande = builderTemplate
                .setLakemedelsbehandling(Lakemedelsbehandling.create(true, true, null, false,
                        false, false, null, null))
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessages = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessages, LAKEMEDELSBEHANDLING_KATEGORI,
                LAKEMEDELSBEHANDLING_FOREKOMMIT_SVAR_JSON_ID + PUNKT + LAKEMEDELSBEHANDLING_AKTUELL_DELSVAR_JSON_ID);
    }

    @Test
    public void validateEmptyLakemedelsbehandlingAktuell() throws Exception {
        final TsTstrk1062UtlatandeV1 utlatande = builderTemplate
                .setLakemedelsbehandling(Lakemedelsbehandling.create(true, true, "", false,
                        false, false, null, null))
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessages = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessages, LAKEMEDELSBEHANDLING_KATEGORI,
                LAKEMEDELSBEHANDLING_FOREKOMMIT_SVAR_JSON_ID + PUNKT + LAKEMEDELSBEHANDLING_AKTUELL_DELSVAR_JSON_ID);
    }

    @Test
    public void validateNullLakemedelsbehandlingPagatt() throws Exception {
        final TsTstrk1062UtlatandeV1 utlatande = builderTemplate
                .setLakemedelsbehandling(Lakemedelsbehandling.create(true, true, "Aktuell", null,
                        false, false, null, null))
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessages = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessages, LAKEMEDELSBEHANDLING_KATEGORI,
                LAKEMEDELSBEHANDLING_FOREKOMMIT_SVAR_JSON_ID + PUNKT + LAKEMEDELSBEHANDLING_MER_3_AR_DELSVAR_JSON_ID);
    }

    @Test
    public void validateNullLakemedelsbehandlingEffekt() throws Exception {
        final TsTstrk1062UtlatandeV1 utlatande = builderTemplate
                .setLakemedelsbehandling(Lakemedelsbehandling.create(true, true, "Aktuell", false,
                        null, false, null, null))
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessages = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessages, LAKEMEDELSBEHANDLING_KATEGORI,
                LAKEMEDELSBEHANDLING_FOREKOMMIT_SVAR_JSON_ID + PUNKT + LAKEMEDELSBEHANDLING_EFFEKT_DELSVAR_JSON_ID);
    }

    @Test
    public void validateNullLakemedelsbehandlingFoljsamhet() throws Exception {
        final TsTstrk1062UtlatandeV1 utlatande = builderTemplate
                .setLakemedelsbehandling(Lakemedelsbehandling.create(true, true, "Aktuell", false,
                        false, null, null, null))
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessages = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessages, LAKEMEDELSBEHANDLING_KATEGORI,
                LAKEMEDELSBEHANDLING_FOREKOMMIT_SVAR_JSON_ID + PUNKT + LAKEMEDELSBEHANDLING_FOLJSAMHET_DELSVAR_JSON_ID);
    }

    @Test
    public void validateNullLakemedelsbehandlingAvslutadTidpunkt() throws Exception {
        final TsTstrk1062UtlatandeV1 utlatande = builderTemplate
                .setLakemedelsbehandling(Lakemedelsbehandling.create(true, false, null, null,
                        null, null, null, "Orsak"))
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessages = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessages, LAKEMEDELSBEHANDLING_KATEGORI,
                LAKEMEDELSBEHANDLING_FOREKOMMIT_SVAR_JSON_ID + PUNKT + LAKEMEDELSBEHANDLING_AVSLUTAD_DELSVAR_JSON_ID);
    }

    @Test
    public void validateEmptyLakemedelsbehandlingAvslutadTidpunkt() throws Exception {
        final TsTstrk1062UtlatandeV1 utlatande = builderTemplate
                .setLakemedelsbehandling(Lakemedelsbehandling.create(true, false, null, null,
                        null, null, "", "Orsak"))
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessages = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessages, LAKEMEDELSBEHANDLING_KATEGORI,
                LAKEMEDELSBEHANDLING_FOREKOMMIT_SVAR_JSON_ID + PUNKT + LAKEMEDELSBEHANDLING_AVSLUTAD_DELSVAR_JSON_ID);
    }

    @Test
    public void validateNullLakemedelsbehandlingAvslutadOrsak() throws Exception {
        final TsTstrk1062UtlatandeV1 utlatande = builderTemplate
                .setLakemedelsbehandling(Lakemedelsbehandling.create(true, false, null, null,
                        null, null, "Förra året", null))
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessages = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessages, LAKEMEDELSBEHANDLING_KATEGORI,
                LAKEMEDELSBEHANDLING_FOREKOMMIT_SVAR_JSON_ID + PUNKT + LAKEMEDELSBEHANDLING_AVSLUTAD_ORSAK_DELSVAR_JSON_ID);
    }

    @Test
    public void validateEmptyLakemedelsbehandlingAvslutadOrsak() throws Exception {
        final TsTstrk1062UtlatandeV1 utlatande = builderTemplate
                .setLakemedelsbehandling(Lakemedelsbehandling.create(true, false, null, null,
                        null, null, "Förra året", ""))
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessages = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessages, LAKEMEDELSBEHANDLING_KATEGORI,
                LAKEMEDELSBEHANDLING_FOREKOMMIT_SVAR_JSON_ID + PUNKT + LAKEMEDELSBEHANDLING_AVSLUTAD_ORSAK_DELSVAR_JSON_ID);
    }

    @Test
    public void validateNullBedomningAvSymptom() throws Exception {
        final TsTstrk1062UtlatandeV1 utlatande = builderTemplate
                .setBedomningAvSymptom(null)
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessages = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessages, SYMPTOM_KATEGORI, SYMPTOM_BEDOMNING_DELSVAR_JSON_ID);
    }

    @Test
    public void validateEmptyBedomningAvSymptom() throws Exception {
        final TsTstrk1062UtlatandeV1 utlatande = builderTemplate
                .setBedomningAvSymptom(null)
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessages = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessages, SYMPTOM_KATEGORI, SYMPTOM_BEDOMNING_DELSVAR_JSON_ID);
    }

    @Test
    public void validateNullPrognos() throws Exception {
        final TsTstrk1062UtlatandeV1 utlatande = builderTemplate
                .setPrognosTillstand(null)
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessages = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessages, SYMPTOM_KATEGORI,
                SYMPTOM_PROGNOS_SVAR_JSON_ID + PUNKT + SYMPTOM_PROGNOS_DELSVAR_JSON_ID);
    }

    @Test
    public void validateNullPrognosType() throws Exception {
        final TsTstrk1062UtlatandeV1 utlatande = builderTemplate
                .setPrognosTillstand(PrognosTillstand.create(null))
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessages = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessages, SYMPTOM_KATEGORI,
                SYMPTOM_PROGNOS_SVAR_JSON_ID + PUNKT + SYMPTOM_PROGNOS_DELSVAR_JSON_ID);
    }

    @Test
    public void validateNullBedomning() throws Exception {
        final TsTstrk1062UtlatandeV1 utlatande = builderTemplate
                .setBedomning(null)
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessage = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessage, BEDOMNING_KATEGORI,
                BEDOMNING_UPPFYLLER_SVAR_JSON_ID + PUNKT + BEDOMNING_UPPFYLLER_DELSVAR_JSON_ID);
    }

    @Test
    public void validateNullBedomningBehorighetsKrav() throws Exception {
        final TsTstrk1062UtlatandeV1 utlatande = builderTemplate
                .setBedomning(Bedomning.builder().build())
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessage = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessage, BEDOMNING_KATEGORI,
                BEDOMNING_UPPFYLLER_SVAR_JSON_ID + PUNKT + BEDOMNING_UPPFYLLER_DELSVAR_JSON_ID);
    }

    @Test
    public void validateMissingBedomning() throws Exception {
        final TsTstrk1062UtlatandeV1 utlatande = builderTemplate
                .setBedomning(Bedomning.builder()
                        .setUppfyllerBehorighetskrav(EnumSet.noneOf(Bedomning.BehorighetsTyp.class))
                        .build())
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessage = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessage, BEDOMNING_KATEGORI,
                BEDOMNING_UPPFYLLER_SVAR_JSON_ID + PUNKT + BEDOMNING_UPPFYLLER_DELSVAR_JSON_ID);
    }

    @Test
    public void validateInvalidBedomning() throws Exception {
        final TsTstrk1062UtlatandeV1 utlatande = builderTemplate
                .setBedomning(Bedomning.builder()
                        .setUppfyllerBehorighetskrav(EnumSet.of(Bedomning.BehorighetsTyp.VAR6,
                                Bedomning.BehorighetsTyp.VAR11))
                        .build())
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessage = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessage, ValidationMessageType.INCORRECT_COMBINATION, BEDOMNING_KATEGORI,
                BEDOMNING_UPPFYLLER_SVAR_JSON_ID + PUNKT + BEDOMNING_UPPFYLLER_DELSVAR_JSON_ID);
    }

    private Map<String, ValidationMessage> buildMapFromMessages(List<ValidationMessage> validationMessages) {
        final Map<String, ValidationMessage> validationsMap = new HashMap<>(validationMessages.size());
        for (ValidationMessage validationMessage : validationMessages) {
            validationsMap.put(validationMessage.getField(), validationMessage);
        }
        return validationsMap;
    }

    private void assertValidationMessage(Map<String, ValidationMessage> validationMessageMap, String category, String field) {
        final ValidationMessage validationMessage = validationMessageMap.get(field);
        assertNotNull("Missing message for " + field, validationMessage);
        assertValidationMessage(validationMessage, ValidationMessageType.EMPTY, category, field);
    }

    private void assertOneValidationMessages(List<ValidationMessage> validationMessage, String category, String field) {
        assertOneValidationMessages(validationMessage, ValidationMessageType.EMPTY, category, field);
    }

    private void assertOneValidationMessages(List<ValidationMessage> validationMessage, ValidationMessageType validationMessageType,
            String category, String field) {
        assertEquals("Should have error messages", 1, validationMessage.size());
        assertValidationMessage(validationMessage.get(0), validationMessageType, category, field);
    }

    private void assertValidationMessage(ValidationMessage validationMessage, ValidationMessageType validationMessageType, String category,
            String field) {
        assertEquals("Should have Empty message type", validationMessageType, validationMessage.getType());
        assertEquals("Should have category: " + category, category, validationMessage.getCategory());
        assertEquals("Should have field: " + field, field, validationMessage.getField());
    }

    private GrundData buildGrundData(LocalDateTime timeStamp) {
        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setVardgivarid("vardgivareId");
        vardgivare.setVardgivarnamn("vardgivareNamn");

        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setEnhetsid("enhetId");
        vardenhet.setEnhetsnamn("enhetNamn");
        vardenhet.setVardgivare(vardgivare);
        vardenhet.setPostadress("postadress");
        vardenhet.setPostnummer("11111");
        vardenhet.setPostort("postort");
        vardenhet.setTelefonnummer("0112312313");

        HoSPersonal skapadAv = new HoSPersonal();
        skapadAv.setVardenhet(vardenhet);
        skapadAv.setPersonId("HSAID_123");
        skapadAv.setFullstandigtNamn("Torsten Ericsson");

        Patient patient = new Patient();
        patient.setPersonId(Personnummer.createPersonnummer("19121212-1212").get());
        patient.setPostadress("postadress");
        patient.setPostnummer("11111");
        patient.setPostort("postort");

        GrundData grundData = new GrundData();
        grundData.setSkapadAv(skapadAv);
        grundData.setPatient(patient);
        grundData.setSigneringsdatum(timeStamp);

        return grundData;
    }
}
