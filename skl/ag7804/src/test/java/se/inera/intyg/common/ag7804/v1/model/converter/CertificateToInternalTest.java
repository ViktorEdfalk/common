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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import se.inera.intyg.common.ag7804.model.internal.ArbetslivsinriktadeAtgarder;
import se.inera.intyg.common.ag7804.model.internal.ArbetslivsinriktadeAtgarder.ArbetslivsinriktadeAtgarderVal;
import se.inera.intyg.common.ag7804.model.internal.Prognos;
import se.inera.intyg.common.ag7804.model.internal.PrognosDagarTillArbeteTyp;
import se.inera.intyg.common.ag7804.model.internal.PrognosTyp;
import se.inera.intyg.common.ag7804.model.internal.Sjukskrivning;
import se.inera.intyg.common.ag7804.model.internal.Sjukskrivning.SjukskrivningsGrad;
import se.inera.intyg.common.ag7804.model.internal.Sysselsattning;
import se.inera.intyg.common.ag7804.model.internal.Sysselsattning.SysselsattningsTyp;
import se.inera.intyg.common.ag7804.v1.model.internal.Ag7804UtlatandeV1;
import se.inera.intyg.common.agparent.model.internal.Diagnos;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.InternalLocalDateInterval;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;

@DisplayName("Should convert Certificate to AG7804")
class CertificateToInternalTest {

    @Mock
    WebcertModuleService moduleService;
    CertificateTextProvider texts;

    @BeforeEach
    void setup() {
        texts = Mockito.mock(CertificateTextProvider.class);
        when(texts.get(Mockito.any(String.class))).thenReturn("Test string");
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class QuestionAvstangningSmittskydd {

        private Ag7804UtlatandeV1 internalCertificate;

        @BeforeEach
        void setup() {
            internalCertificate = Ag7804UtlatandeV1.builder()
                .setGrundData(new GrundData())
                .setId("id")
                .setTextVersion("TextVersion")
                .build();
        }

        Stream<Boolean> booleanValues() {
            return Stream.of(true, false, null);
        }

        @ParameterizedTest
        @MethodSource("booleanValues")
        void shouldIncludeAvstangningSmittskyddValue(Boolean expectedValue) {
            final var index = 1;

            final var certificate = CertificateBuilder.create().addElement(InternalToCertificate
                .createAvstangningSmittskyddQuestion(expectedValue, index, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate, moduleService);

            assertEquals(expectedValue, updatedCertificate.getAvstangningSmittskydd());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class QuestionGrundForMU {

        private Ag7804UtlatandeV1 internalCertificate;

        @BeforeEach
        void setup() {
            internalCertificate = Ag7804UtlatandeV1.builder()
                .setGrundData(new GrundData())
                .setId("id")
                .setTextVersion("TextVersion")
                .build();
        }

        Stream<InternalDate> dateValues() {
            return Stream.of(new InternalDate(LocalDate.now().plusMonths(10)), new InternalDate(LocalDate.now()), null);
        }

        @ParameterizedTest
        @MethodSource("dateValues")
        void shouldIncludeGrundForMUUndersokningValue(InternalDate expectedValue) {
            final var index = 1;

            final var utlatande =
                Ag7804UtlatandeV1.builder()
                    .setId("id")
                    .setTextVersion("1.0")
                    .setGrundData(new GrundData())
                    .setUndersokningAvPatienten(expectedValue)
                    .build();

            final var certificate = CertificateBuilder.create()
                .addElement(InternalToCertificate.createIntygetBaseratPa(utlatande, index, texts)).build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate, moduleService);

            assertEquals(expectedValue, updatedCertificate.getUndersokningAvPatienten());
        }

        @ParameterizedTest
        @MethodSource("dateValues")
        void shouldIncludeGrundForMUTelefonkontaktValue(InternalDate expectedValue) {
            final var index = 1;

            final var utlatande =
                Ag7804UtlatandeV1.builder()
                    .setId("id")
                    .setTextVersion("1.0")
                    .setGrundData(new GrundData())
                    .setTelefonkontaktMedPatienten(expectedValue)
                    .build();

            final var certificate = CertificateBuilder.create()
                .addElement(InternalToCertificate.createIntygetBaseratPa(utlatande, index, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate, moduleService);

            assertEquals(expectedValue, updatedCertificate.getTelefonkontaktMedPatienten());
        }

        @ParameterizedTest
        @MethodSource("dateValues")
        void shouldIncludeGrundForMUJournalUppgifterValue(InternalDate expectedValue) {
            final var index = 1;

            final var utlatande =
                Ag7804UtlatandeV1.builder()
                    .setId("id")
                    .setTextVersion("1.0")
                    .setGrundData(new GrundData())
                    .setJournaluppgifter(expectedValue)
                    .build();

            final var certificate = CertificateBuilder.create()
                .addElement(InternalToCertificate.createIntygetBaseratPa(utlatande, index, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate, moduleService);

            assertEquals(expectedValue, updatedCertificate.getJournaluppgifter());
        }

        @ParameterizedTest
        @MethodSource("dateValues")
        void shouldIncludeGrundForMUAnnatValue(InternalDate expectedValue) {
            final var index = 1;

            final var utlatande =
                Ag7804UtlatandeV1.builder()
                    .setId("id")
                    .setTextVersion("1.0")
                    .setGrundData(new GrundData())
                    .setAnnatGrundForMU(expectedValue)
                    .build();

            final var certificate = CertificateBuilder.create()
                .addElement(InternalToCertificate.createIntygetBaseratPa(utlatande, index, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate, moduleService);

            assertEquals(expectedValue, updatedCertificate.getAnnatGrundForMU());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class QuestionAnnatGrundForMUBeskrivning {

        private Ag7804UtlatandeV1 internalCertificate;

        @BeforeEach
        void setup() {
            internalCertificate = Ag7804UtlatandeV1.builder()
                .setGrundData(new GrundData())
                .setId("id")
                .setTextVersion("TextVersion")
                .build();
        }

        Stream<String> textValues() {
            return Stream.of("Här kommer en text!", "", null);
        }

        @ParameterizedTest
        @MethodSource("textValues")
        void shouldIncludeAnnatGrundForMUBeskrivningValue(String expectedValue) {
            final var index = 1;

            final var certificate = CertificateBuilder.create().addElement(InternalToCertificate
                .createAnnatGrundForMUBeskrivning(expectedValue, index, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate, moduleService);

            assertEquals(expectedValue, updatedCertificate.getAnnatGrundForMUBeskrivning());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class QuestionSysselsattning {

        private Ag7804UtlatandeV1 internalCertificate;

        @BeforeEach
        void setup() {
            internalCertificate = Ag7804UtlatandeV1.builder()
                .setGrundData(new GrundData())
                .setId("id")
                .setTextVersion("TextVersion")
                .build();
        }

        Stream<List<Sysselsattning>> codeListValues() {
            return Stream.of(Arrays.asList(
                Sysselsattning.create(SysselsattningsTyp.NUVARANDE_ARBETE),
                Sysselsattning.create(SysselsattningsTyp.ARBETSSOKANDE),
                Sysselsattning.create(SysselsattningsTyp.FORADLRARLEDIGHET_VARD_AV_BARN),
                Sysselsattning.create(SysselsattningsTyp.STUDIER)
            ), Collections.emptyList());
        }

        @ParameterizedTest
        @MethodSource("codeListValues")
        void shouldIncludeSysselsattningValue(List<Sysselsattning> expectedValue) {
            final var index = 1;

            final var certificate = CertificateBuilder.create().addElement(InternalToCertificate
                .createSysselsattningQuestion(expectedValue, index, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate, moduleService);

            assertEquals(expectedValue, updatedCertificate.getSysselsattning());
        }

        @Test
        void shouldIncludeAtgardValueNull() {
            final var index = 1;
            final List<Sysselsattning> expectedValue = Collections.emptyList();

            final var certificate = CertificateBuilder.create()
                .addElement(InternalToCertificate.createSysselsattningQuestion(null, index, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate, moduleService);

            assertEquals(expectedValue, updatedCertificate.getSysselsattning());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class QuestionNuvarandeArbete {

        private Ag7804UtlatandeV1 internalCertificate;

        @BeforeEach
        void setup() {
            internalCertificate = Ag7804UtlatandeV1.builder()
                .setGrundData(new GrundData())
                .setId("id")
                .setTextVersion("TextVersion")
                .build();
        }

        Stream<String> textValues() {
            return Stream.of("Här kommer en text!", "", null);
        }

        @ParameterizedTest
        @MethodSource("textValues")
        void shouldIncludeNuvarandeArbeteValue(String expectedValue) {
            final var index = 1;

            final var certificate = CertificateBuilder.create().addElement(InternalToCertificate
                .createSysselsattningYrkeQuestion(expectedValue, index, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate, moduleService);

            assertEquals(expectedValue, updatedCertificate.getNuvarandeArbete());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class QuestionShouldIncludeDiagnoses {

        private Ag7804UtlatandeV1 internalCertificate;

        @BeforeEach
        void setup() {
            internalCertificate = Ag7804UtlatandeV1.builder()
                .setGrundData(new GrundData())
                .setId("id")
                .setTextVersion("TextVersion")
                .build();
        }

        Stream<Boolean> booleanValues() {
            return Stream.of(true, false);
        }

        @ParameterizedTest
        @MethodSource("booleanValues")
        void shouldIncludePatientWantsDiagnosesIncludedValue(Boolean expectedValue) {
            final var index = 1;

            final var certificate = CertificateBuilder.create()
                .addElement(InternalToCertificate.createShouldIncludeDiagnosesQuestion(expectedValue, index, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate, moduleService);

            assertEquals(expectedValue, updatedCertificate.getOnskarFormedlaDiagnos());
        }

        @Test
        void shouldIncludePatientWantsDiagnosesIncludedNullValue() {
            final var index = 1;
            final var expectedValue = false;

            final var certificate = CertificateBuilder.create()
                .addElement(InternalToCertificate.createShouldIncludeDiagnosesQuestion(null, index, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate, moduleService);

            assertEquals(expectedValue, updatedCertificate.getOnskarFormedlaDiagnos());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class QuestionDiagnos {

        private final String DIAGNOSIS_DESCRIPTION = "Beskrivning med egen text";
        private final String DIAGNOSIS_DESCRIPTION_WITHOUT_ADDITION = "Beskrivning utan egen text";
        private Ag7804UtlatandeV1 internalCertificate;

        @BeforeEach
        void setup() {
            moduleService = mock(WebcertModuleService.class);
            when(moduleService.getDescriptionFromDiagnosKod(anyString(), anyString())).thenReturn(DIAGNOSIS_DESCRIPTION_WITHOUT_ADDITION);
            internalCertificate = Ag7804UtlatandeV1.builder()
                .setGrundData(new GrundData())
                .setId("id")
                .setTextVersion("TextVersion")
                .build();
        }

        Stream<List<Diagnos>> diagnosisListValues() {
            return Stream.of(Arrays.asList(
                Diagnos.create("F500", "ICD-10", DIAGNOSIS_DESCRIPTION, DIAGNOSIS_DESCRIPTION_WITHOUT_ADDITION)
            ), Arrays.asList(
                Diagnos.create("", "ICD-10", DIAGNOSIS_DESCRIPTION, DIAGNOSIS_DESCRIPTION_WITHOUT_ADDITION),
                Diagnos.create("F501", "ICD-10", DIAGNOSIS_DESCRIPTION, DIAGNOSIS_DESCRIPTION_WITHOUT_ADDITION),
                Diagnos.create("F502", "ICD-10", DIAGNOSIS_DESCRIPTION, DIAGNOSIS_DESCRIPTION_WITHOUT_ADDITION)
            ), Collections.emptyList());
        }

        @ParameterizedTest
        @MethodSource("diagnosisListValues")
        void shouldIncludeDiagnosValue(List<Diagnos> expectedValue) {
            final var index = 1;

            final var certificate = CertificateBuilder.create()
                .addElement(InternalToCertificate.createDiagnosQuestion(expectedValue, index, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate, moduleService);

            assertEquals(expectedValue, updatedCertificate.getDiagnoser());
        }

        @Test
        void shouldIncludeDiagnosValueNull() {
            final var index = 1;
            final List<Diagnos> expectedValue = Collections.emptyList();

            final var certificate = CertificateBuilder.create().addElement(InternalToCertificate.createDiagnosQuestion(null, index, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate, moduleService);

            assertEquals(expectedValue, updatedCertificate.getDiagnoser());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class QuestionFunktionsnedsattning {

        private Ag7804UtlatandeV1 internalCertificate;

        @BeforeEach
        void setup() {
            internalCertificate = Ag7804UtlatandeV1.builder()
                .setGrundData(new GrundData())
                .setId("id")
                .setTextVersion("TextVersion")
                .build();
        }

        Stream<String> textValues() {
            return Stream.of("Här kommer en text!", "", null);
        }

        @ParameterizedTest
        @MethodSource("textValues")
        void shouldIncludeFunktionsnedsattningValue(String expectedValue) {
            final var index = 1;

            final var certificate = CertificateBuilder.create().addElement(InternalToCertificate
                .createFunktionsnedsattningQuestion(expectedValue, index, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate, moduleService);

            assertEquals(expectedValue, updatedCertificate.getFunktionsnedsattning());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class QuestionAktivitetsbegransning {

        private Ag7804UtlatandeV1 internalCertificate;

        @BeforeEach
        void setup() {
            internalCertificate = Ag7804UtlatandeV1.builder()
                .setGrundData(new GrundData())
                .setId("id")
                .setTextVersion("TextVersion")
                .build();
        }

        Stream<String> textValues() {
            return Stream.of("Här kommer en text!", "", null);
        }

        @ParameterizedTest
        @MethodSource("textValues")
        void shouldIncludeFunktionsnedsattningValue(String expectedValue) {
            final var index = 1;

            final var certificate = CertificateBuilder.create().addElement(
                InternalToCertificate.createAktivitetsbegransningQuestion(expectedValue, index, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate, moduleService);

            assertEquals(expectedValue, updatedCertificate.getAktivitetsbegransning());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class QuestionMedicinskaBehandlingar {

        private Ag7804UtlatandeV1 internalCertificate;

        @BeforeEach
        void setup() {
            internalCertificate = Ag7804UtlatandeV1.builder()
                .setGrundData(new GrundData())
                .setId("id")
                .setTextVersion("TextVersion")
                .build();
        }

        Stream<String> textValues() {
            return Stream.of("Här kommer en text!", "", null);
        }

        @ParameterizedTest
        @MethodSource("textValues")
        void shouldIncludePagaendeBehandlingValue(String expectedValue) {
            final var index = 1;

            final var certificate = CertificateBuilder.create().addElement(
                InternalToCertificate.createPagaendeBehandlingQuestion(expectedValue, index, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate, moduleService);

            assertEquals(expectedValue, updatedCertificate.getPagaendeBehandling());
        }

        @ParameterizedTest
        @MethodSource("textValues")
        void shouldIncludePlaneradBehandlingValue(String expectedValue) {
            final var index = 1;

            final var certificate = CertificateBuilder.create().addElement(
                InternalToCertificate.createPlaneradBehandlingQuestion(expectedValue, index, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate, moduleService);

            assertEquals(expectedValue, updatedCertificate.getPlaneradBehandling());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class QuestionBedomning {

        private Ag7804UtlatandeV1 internalCertificate;

        @BeforeEach
        void setup() {
            internalCertificate = Ag7804UtlatandeV1.builder()
                .setGrundData(new GrundData())
                .setId("id")
                .setTextVersion("TextVersion")
                .build();
        }

        Stream<List<Sjukskrivning>> sickLeaveValues() {
            return Stream.of(
                Arrays.asList(
                    Sjukskrivning.create(
                        SjukskrivningsGrad.HELT_NEDSATT, new InternalLocalDateInterval(
                            new InternalDate(LocalDate.now()), new InternalDate(LocalDate.now())
                        )
                    ),
                    Sjukskrivning.create(
                        SjukskrivningsGrad.NEDSATT_HALFTEN, new InternalLocalDateInterval(
                            new InternalDate(LocalDate.now()), new InternalDate(LocalDate.now())
                        )
                    )
                ), Collections.emptyList()
            );
        }

        @ParameterizedTest
        @MethodSource("sickLeaveValues")
        void shouldIncludeBehovAvSjukskrivningValue(List<Sjukskrivning> expectedValue) {
            final var index = 1;

            final var certificate = CertificateBuilder.create().addElement(
                InternalToCertificate.createBehovAvSjukskrivningQuestion(expectedValue, index, texts, internalCertificate.getGrundData()
                    .getRelation()))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate, moduleService);

            assertEquals(expectedValue, updatedCertificate.getSjukskrivningar());
        }

        @Test
        void shouldIncludeBehovAvSjukskrivningValueNull() {
            final var index = 1;

            final var certificate = CertificateBuilder.create().addElement(
                InternalToCertificate.createBehovAvSjukskrivningQuestion(null, index, texts, internalCertificate.getGrundData()
                    .getRelation()))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate, moduleService);

            assertEquals(Collections.emptyList(), updatedCertificate.getSjukskrivningar());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class QuestionForsakringsmedicinsktBeslutsstod {

        private Ag7804UtlatandeV1 internalCertificate;

        @BeforeEach
        void setup() {
            internalCertificate = Ag7804UtlatandeV1.builder()
                .setGrundData(new GrundData())
                .setId("id")
                .setTextVersion("TextVersion")
                .build();
        }

        Stream<String> textValues() {
            return Stream.of("Här kommer en text!", "", null);
        }

        @ParameterizedTest
        @MethodSource("textValues")
        void shouldIncludeForsakringsmedicinsktBeslutsstödValue(String expectedValue) {
            final var index = 1;

            final var certificate = CertificateBuilder.create().addElement(
                InternalToCertificate.createForsakringsmedicinsktBeslutsstodQuestion(expectedValue, index, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate, moduleService);

            assertEquals(expectedValue, updatedCertificate.getForsakringsmedicinsktBeslutsstod());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class QuestionArbetstidsforlaggning {

        private Ag7804UtlatandeV1 internalCertificate;

        @BeforeEach
        void setup() {
            internalCertificate = Ag7804UtlatandeV1.builder()
                .setGrundData(new GrundData())
                .setId("id")
                .setTextVersion("TextVersion")
                .build();
        }

        Stream<Boolean> booleanValues() {
            return Stream.of(true, false, null);
        }

        @ParameterizedTest
        @MethodSource("booleanValues")
        void shouldIncludeArbetstidsforlaggningValue(Boolean expectedValue) {
            final var index = 1;

            final var certificate = CertificateBuilder.create().addElement(
                InternalToCertificate.createArbetstidsforlaggningQuestion(expectedValue, index, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate, moduleService);

            assertEquals(expectedValue, updatedCertificate.getArbetstidsforlaggning());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class QuestionMotiveringArbetstidsforlaggning {

        private Ag7804UtlatandeV1 internalCertificate;

        @BeforeEach
        void setup() {
            internalCertificate = Ag7804UtlatandeV1.builder()
                .setGrundData(new GrundData())
                .setId("id")
                .setTextVersion("TextVersion")
                .build();
        }

        Stream<String> textValues() {
            return Stream.of("Här kommer en text!", "", null);
        }

        @ParameterizedTest
        @MethodSource("textValues")
        void shouldIncludeMotiveringArbetstidsforlaggningValue(String expectedValue) {
            final var index = 1;

            final var certificate = CertificateBuilder.create().addElement(
                InternalToCertificate.createMotiveringArbetstidsforlaggningQuestion(expectedValue, index, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate, moduleService);

            assertEquals(expectedValue, updatedCertificate.getArbetstidsforlaggningMotivering());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class QuestionArbetsresor {

        private Ag7804UtlatandeV1 internalCertificate;

        @BeforeEach
        void setup() {
            internalCertificate = Ag7804UtlatandeV1.builder()
                .setGrundData(new GrundData())
                .setId("id")
                .setTextVersion("TextVersion")
                .build();
        }

        Stream<Boolean> booleanValues() {
            return Stream.of(true, false, null);
        }

        @ParameterizedTest
        @MethodSource("booleanValues")
        void shouldIncludeArbetstidsforlaggningValue(Boolean expectedValue) {
            final var index = 1;

            final var certificate = CertificateBuilder.create().addElement(
                InternalToCertificate.createArbetsresorQuestion(expectedValue, index, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate, moduleService);

            assertEquals(expectedValue, updatedCertificate.getArbetsresor());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class QuestionPrognos {

        private Ag7804UtlatandeV1 internalCertificate;

        @BeforeEach
        void setup() {
            internalCertificate = Ag7804UtlatandeV1.builder()
                .setGrundData(new GrundData())
                .setId("id")
                .setTextVersion("TextVersion")
                .build();
        }

        Stream<Prognos> prognosValues() {
            return Stream.of(Prognos.create(PrognosTyp.MED_STOR_SANNOLIKHET, null),
                Prognos.create(PrognosTyp.SANNOLIKT_EJ_ATERGA_TILL_SYSSELSATTNING, null),
                Prognos.create(PrognosTyp.PROGNOS_OKLAR, null),
                Prognos.create(PrognosTyp.ATER_X_ANTAL_DGR, PrognosDagarTillArbeteTyp.DAGAR_30),
                Prognos.create(PrognosTyp.ATER_X_ANTAL_DGR, PrognosDagarTillArbeteTyp.DAGAR_60),
                Prognos.create(PrognosTyp.ATER_X_ANTAL_DGR, PrognosDagarTillArbeteTyp.DAGAR_90),
                Prognos.create(PrognosTyp.ATER_X_ANTAL_DGR, PrognosDagarTillArbeteTyp.DAGAR_180),
                Prognos.create(PrognosTyp.ATER_X_ANTAL_DGR, PrognosDagarTillArbeteTyp.DAGAR_365), null);
        }

        @ParameterizedTest
        @MethodSource("prognosValues")
        void shouldIncludePrognosValue(Prognos expectedValue) {
            final var index = 1;

            final var certificate = CertificateBuilder.create()
                .addElement(
                    InternalToCertificate.createPrognosQuestion(expectedValue, index, texts))
                .addElement(InternalToCertificate
                    .createPrognosTimeperiodQuestion(expectedValue, index, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate, moduleService);

            assertEquals(expectedValue, updatedCertificate.getPrognos());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class QuestionAtgard {

        private Ag7804UtlatandeV1 internalCertificate;

        @BeforeEach
        void setup() {
            internalCertificate = Ag7804UtlatandeV1.builder()
                .setGrundData(new GrundData())
                .setId("id")
                .setTextVersion("TextVersion")
                .build();
        }

        Stream<List<ArbetslivsinriktadeAtgarder>> codeListValues() {
            return Stream.of(
                Arrays.asList(
                    ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.INTE_AKTUELLT)
                ),
                Arrays.asList(
                    ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.ARBETSANPASSNING),
                    ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.ARBETSTRANING),
                    ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.OVRIGT),
                    ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.OMFORDELNING_AV_ARBETSUPPGIFTER),
                    ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.ERGONOMISK_BEDOMNING),
                    ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.BESOK_PA_ARBETSPLATSEN),
                    ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.HJALPMEDEL)

                ),
                Collections.emptyList());
        }

        @ParameterizedTest
        @MethodSource("codeListValues")
        void shouldIncludeAtgardValue(List<ArbetslivsinriktadeAtgarder> expectedValue) {
            final var index = 1;

            final var certificate = CertificateBuilder.create()
                .addElement(
                    InternalToCertificate
                        .createAtgarderQuestion(expectedValue, index, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate, moduleService);

            assertEquals(expectedValue, updatedCertificate.getArbetslivsinriktadeAtgarder());
        }

        @Test
        void shouldIncludeAtgardValueNull() {
            final var index = 1;
            final List<ArbetslivsinriktadeAtgarder> expectedValue = Collections.emptyList();

            final var certificate = CertificateBuilder.create().addElement(
                InternalToCertificate.createAtgarderQuestion(null, index, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate, moduleService);

            assertEquals(expectedValue, updatedCertificate.getArbetslivsinriktadeAtgarder());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class QuestionAtgarderBeskrivning {

        private Ag7804UtlatandeV1 internalCertificate;

        @BeforeEach
        void setup() {
            internalCertificate = Ag7804UtlatandeV1.builder()
                .setGrundData(new GrundData())
                .setId("id")
                .setTextVersion("TextVersion")
                .build();
        }

        Stream<String> textValues() {
            return Stream.of("Här kommer en text!", "", null);
        }

        @ParameterizedTest
        @MethodSource("textValues")
        void shouldIncludeAtgarderBeskrivningValue(String expectedValue) {
            final var index = 1;

            final var certificate = CertificateBuilder.create().addElement(
                InternalToCertificate.createAtgarderBeskrivning(expectedValue, index, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate, moduleService);

            assertEquals(expectedValue, updatedCertificate.getArbetslivsinriktadeAtgarderBeskrivning());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class QuestionOvrigt {

        private Ag7804UtlatandeV1 internalCertificate;

        @BeforeEach
        void setup() {
            internalCertificate = Ag7804UtlatandeV1.builder()
                .setGrundData(new GrundData())
                .setId("id")
                .setTextVersion("TextVersion")
                .build();
        }

        Stream<String> textValues() {
            return Stream.of("Här kommer en text!", "", null);
        }

        @ParameterizedTest
        @MethodSource("textValues")
        void shouldIncludeOvrigtValue(String expectedValue) {
            final var index = 1;

            final var certificate = CertificateBuilder.create()
                .addElement(
                    InternalToCertificate.createOvrigtQuestion(expectedValue, index, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate, moduleService);

            assertEquals(expectedValue, updatedCertificate.getOvrigt());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class QuestionKontakt {

        private Ag7804UtlatandeV1 internalCertificate;

        @BeforeEach
        void setup() {
            internalCertificate = Ag7804UtlatandeV1.builder()
                .setGrundData(new GrundData())
                .setId("id")
                .setTextVersion("TextVersion")
                .build();
        }

        Stream<Boolean> booleanValues() {
            return Stream.of(true, false, null);
        }

        @ParameterizedTest
        @MethodSource("booleanValues")
        void shouldIncludeKontaktValue(Boolean expectedValue) {
            final var index = 1;

            final var certificate = CertificateBuilder.create()
                .addElement(
                    InternalToCertificate.createKontaktQuestion(expectedValue, index, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate, moduleService);

            assertEquals(expectedValue, updatedCertificate.getKontaktMedAg());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class QuestionAnledningKontakt {

        private Ag7804UtlatandeV1 internalCertificate;

        @BeforeEach
        void setup() {
            internalCertificate = Ag7804UtlatandeV1.builder()
                .setGrundData(new GrundData())
                .setId("id")
                .setTextVersion("TextVersion")
                .build();
        }

        Stream<String> textValues() {
            return Stream.of("Här kommer en text!", "", null);
        }

        @ParameterizedTest
        @MethodSource("textValues")
        void shouldIncludeKontaktBeskrivning(String expectedValue) {
            final var index = 1;

            final var certificate = CertificateBuilder.create()
                .addElement(InternalToCertificate.createKontaktBeskrivning(expectedValue, index, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate, moduleService);

            assertEquals(expectedValue, updatedCertificate.getAnledningTillKontakt());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class GrundDataTest {

        private Ag7804UtlatandeV1 internalCertificate;

        @BeforeEach
        void setup() {
            internalCertificate = Ag7804UtlatandeV1.builder()
                .setGrundData(new GrundData())
                .setId("id")
                .setTextVersion("TextVersion")
                .build();
        }

        Stream<String> values() {
            return Stream.of("test string", "", null);
        }

        @ParameterizedTest
        @MethodSource("values")
        void shouldIncludeUnitAddress(String expectedValue) {
            var grundData = new GrundData();
            var hosPersonal = new HoSPersonal();
            var vardenhet = new Vardenhet();
            vardenhet.setPostadress(expectedValue);
            hosPersonal.setVardenhet(vardenhet);
            grundData.setSkapadAv(hosPersonal);

            final var utlatande =
                Ag7804UtlatandeV1.builder()
                    .setId("id")
                    .setTextVersion("1.0")
                    .setGrundData(grundData)
                    .build();

            final var certificate = CertificateBuilder.create()
                .metadata(InternalToCertificate.createMetadata(utlatande, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate, moduleService);

            assertEquals(expectedValue, updatedCertificate.getGrundData().getSkapadAv().getVardenhet().getPostadress());
        }

        @ParameterizedTest
        @MethodSource("values")
        void shouldIncludeUnitCity(String expectedValue) {
            var grundData = new GrundData();
            var hosPersonal = new HoSPersonal();
            var vardenhet = new Vardenhet();
            vardenhet.setPostort(expectedValue);
            hosPersonal.setVardenhet(vardenhet);
            grundData.setSkapadAv(hosPersonal);

            final var utlatande =
                Ag7804UtlatandeV1.builder()
                    .setId("id")
                    .setTextVersion("1.0")
                    .setGrundData(grundData)
                    .build();

            final var certificate = CertificateBuilder.create()
                .metadata(InternalToCertificate.createMetadata(utlatande, texts))
                .build();

            final var updatedCertificate = CertificateToInternal
                .convert(certificate, internalCertificate, moduleService);

            assertEquals(expectedValue, updatedCertificate.getGrundData().getSkapadAv().getVardenhet().getPostort());
        }

        @ParameterizedTest
        @MethodSource("values")
        void shouldIncludeUnitZipCode(String expectedValue) {
            var grundData = new GrundData();
            var hosPersonal = new HoSPersonal();
            var vardenhet = new Vardenhet();
            vardenhet.setPostnummer(expectedValue);
            hosPersonal.setVardenhet(vardenhet);
            grundData.setSkapadAv(hosPersonal);

            final var utlatande =
                Ag7804UtlatandeV1.builder()
                    .setId("id")
                    .setTextVersion("1.0")
                    .setGrundData(grundData)
                    .build();

            final var certificate = CertificateBuilder.create()
                .metadata(InternalToCertificate.createMetadata(utlatande, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate, moduleService);

            assertEquals(expectedValue, updatedCertificate.getGrundData().getSkapadAv().getVardenhet().getPostnummer());
        }

        @ParameterizedTest
        @MethodSource("values")
        void shouldIncludeUnitPhonenumber(String expectedValue) {
            var grundData = new GrundData();
            var hosPersonal = new HoSPersonal();
            var vardenhet = new Vardenhet();
            vardenhet.setTelefonnummer(expectedValue);
            hosPersonal.setVardenhet(vardenhet);
            grundData.setSkapadAv(hosPersonal);

            final var utlatande =
                Ag7804UtlatandeV1.builder()
                    .setId("id")
                    .setTextVersion("1.0")
                    .setGrundData(grundData)
                    .build();

            final var certificate = CertificateBuilder.create()
                .metadata(InternalToCertificate.createMetadata(utlatande, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate, moduleService);

            assertEquals(expectedValue, updatedCertificate.getGrundData().getSkapadAv().getVardenhet().getTelefonnummer());
        }
    }
}