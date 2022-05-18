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
package se.inera.intyg.common.lisjp.v1.rest;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import se.inera.intyg.common.fkparent.model.internal.Diagnos;
import se.inera.intyg.common.fkparent.pdf.PdfGenerator;
import se.inera.intyg.common.fkparent.pdf.PdfGeneratorException;
import se.inera.intyg.common.fkparent.pdf.model.FkPdfDefinition;
import se.inera.intyg.common.fkparent.rest.FkParentModuleApi;
import se.inera.intyg.common.lisjp.model.internal.Sjukskrivning;
import se.inera.intyg.common.lisjp.support.LisjpEntryPoint;
import se.inera.intyg.common.lisjp.v1.model.converter.CertificateToInternal;
import se.inera.intyg.common.lisjp.v1.model.converter.InternalToCertificate;
import se.inera.intyg.common.lisjp.v1.model.converter.InternalToTransport;
import se.inera.intyg.common.lisjp.v1.model.converter.TransportToInternal;
import se.inera.intyg.common.lisjp.v1.model.converter.UtlatandeToIntyg;
import se.inera.intyg.common.lisjp.v1.model.internal.LisjpUtlatandeV1;
import se.inera.intyg.common.lisjp.v1.pdf.AbstractLisjpPdfDefinitionBuilder;
import se.inera.intyg.common.lisjp.v1.pdf.DefaultLisjpPdfDefinitionBuilder;
import se.inera.intyg.common.services.messages.CertificateMessagesProvider;
import se.inera.intyg.common.services.messages.DefaultCertificateMessagesProvider;
import se.inera.intyg.common.services.messages.MessagesParser;
import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.model.InternalLocalDateInterval;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.UtkastStatus;
import se.inera.intyg.common.support.model.common.internal.Relation;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.api.dto.AdditionalMetaData;
import se.inera.intyg.common.support.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.intyg.common.support.modules.support.api.dto.PdfResponse;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleConverterException;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleSystemException;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;

@Component(value = "moduleapi.lisjp.v1")
public class LisjpModuleApiV1 extends FkParentModuleApi<LisjpUtlatandeV1> {

    public static final String SCHEMATRON_FILE = "lisjp.v1.sch";
    private static final Logger LOG = LoggerFactory.getLogger(LisjpModuleApiV1.class);

    private static final String CERTIFICATE_FILE_PREFIX = "lakarintyg_sjukpenning";

    private Map<String, String> validationMessages;

    public LisjpModuleApiV1() {
        super(LisjpUtlatandeV1.class);
        init();
    }

    private void init() {
        try {
            final var inputStream1 = new ClassPathResource("/META-INF/resources/webjars/common/webcert/messages.js").getInputStream();
            final var inputStream2 = new ClassPathResource("/META-INF/resources/webjars/lisjp/webcert/views/messages.js").getInputStream();
            validationMessages = MessagesParser.create().parse(inputStream1).parse(inputStream2).collect();
        } catch (IOException exception) {
            LOG.error("Error during initialization. Could not read messages files");
            throw new RuntimeException("Error during initialization. Could not read messages files", exception);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PdfResponse pdf(String internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin, UtkastStatus utkastStatus)
        throws ModuleException {
        LisjpUtlatandeV1 luseIntyg = getInternal(internalModel);
        return generatePdf(new DefaultLisjpPdfDefinitionBuilder(), statuses, luseIntyg, applicationOrigin, CERTIFICATE_FILE_PREFIX,
            utkastStatus);
    }

    @Override
    public PdfResponse pdfEmployer(String internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin,
        List<String> optionalFields, UtkastStatus utkastStatus) throws ModuleException {
        throw new RuntimeException("Not implemented");
    }

    @Override
    protected String getSchematronFileName() {
        return SCHEMATRON_FILE;
    }

    @Override
    protected RegisterCertificateType internalToTransport(LisjpUtlatandeV1 utlatande) throws ConverterException {
        return InternalToTransport.convert(utlatande, moduleService);
    }

    @Override
    protected LisjpUtlatandeV1 transportToInternal(Intyg intyg) throws ConverterException {
        return TransportToInternal.convert(intyg);
    }

    @Override
    protected Intyg utlatandeToIntyg(LisjpUtlatandeV1 utlatande) throws ConverterException {
        return UtlatandeToIntyg.convert(utlatande, moduleService);
    }

    @Override
    protected LisjpUtlatandeV1 decorateDiagnoserWithDescriptions(LisjpUtlatandeV1 utlatande) {
        List<Diagnos> decoratedDiagnoser = utlatande.getDiagnoser().stream()
            .map(diagnos -> Diagnos.create(diagnos.getDiagnosKod(), diagnos.getDiagnosKodSystem(), diagnos.getDiagnosBeskrivning(),
                moduleService.getDescriptionFromDiagnosKod(diagnos.getDiagnosKod(), diagnos.getDiagnosKodSystem())))
            .collect(Collectors.toList());
        return utlatande.toBuilder().setDiagnoser(decoratedDiagnoser).build();
    }

    @Override
    protected LisjpUtlatandeV1 decorateUtkastWithComment(LisjpUtlatandeV1 utlatande, String comment) {

        return utlatande.toBuilder()
            .setOvrigt(concatOvrigtFalt(utlatande.getOvrigt(), comment))
            .build();
    }

    @Override
    protected LisjpUtlatandeV1 decorateWithSignature(LisjpUtlatandeV1 utlatande, String base64EncodedSignatureXml) {
        return utlatande.toBuilder().setSignature(base64EncodedSignatureXml).build();
    }

    @Override
    public String getAdditionalInfo(Intyg intyg) throws ModuleException {
        try {
            return transportToInternal(intyg).getSjukskrivningar().stream()
                .map(Sjukskrivning::getPeriod)
                .sorted(Comparator.comparing(InternalLocalDateInterval::fromAsLocalDate))
                .reduce((a, b) -> new InternalLocalDateInterval(a.getFrom(), b.getTom()))
                .map(interval -> interval.getFrom().toString() + " - " + interval.getTom().toString())
                .orElse(null);

        } catch (ConverterException e) {
            throw new ModuleException("Could not convert Intyg to Utlatande and as a result could not get additional info", e);
        }
    }

    @Override
    public String createRenewalFromTemplate(CreateDraftCopyHolder draftCopyHolder, Utlatande template)
        throws ModuleException {
        try {
            if (!LisjpUtlatandeV1.class.isInstance(template)) {
                LOG.error("Could not create a new internal Webcert model using template of wrong type");
                throw new ModuleConverterException("Could not create a new internal Webcert model using template of wrong type");
            }

            LisjpUtlatandeV1 internal = (LisjpUtlatandeV1) template;

            // Null out applicable fields
            LisjpUtlatandeV1 renewCopy = internal.toBuilder()
                .setKontaktMedFk(false)
                .setAnledningTillKontakt(null)
                .setUndersokningAvPatienten(null)
                .setTelefonkontaktMedPatienten(null)
                .setJournaluppgifter(null)
                .setAnnatGrundForMU(null)
                .setAnnatGrundForMUBeskrivning(null)
                .setMotiveringTillTidigtStartdatumForSjukskrivning(null)
                .setMotiveringTillInteBaseratPaUndersokning(null)
                .setPrognos(null)
                .setSjukskrivningar(new ArrayList<>())
                .setArbetstidsforlaggning(null)
                .setArbetstidsforlaggningMotivering(null)
                .build();

            Relation relation = draftCopyHolder.getRelation();
            Optional<LocalDate> lastDateOfLastIntyg = internal.getSjukskrivningar().stream()
                .sorted((s1, s2) -> s2.getPeriod().getTom().asLocalDate().compareTo(s1.getPeriod().getTom().asLocalDate()))
                .map(sjukskrivning -> sjukskrivning.getPeriod().getTom().asLocalDate())
                .findFirst();
            relation.setSistaGiltighetsDatum(lastDateOfLastIntyg.orElse(LocalDate.now()));
            Optional<Sjukskrivning.SjukskrivningsGrad> lastSjukskrivningsgradOfLastIntyg = internal.getSjukskrivningar().stream()
                .sorted((s1, s2) -> s2.getPeriod().getTom().asLocalDate().compareTo(s1.getPeriod().getTom().asLocalDate()))
                .map(sjukskrivning -> sjukskrivning.getSjukskrivningsgrad())
                .findFirst();
            lastSjukskrivningsgradOfLastIntyg.ifPresent(grad -> relation.setSistaSjukskrivningsgrad(grad.getLabel()));

            draftCopyHolder.setRelation(relation);

            return toInternalModelResponse(webcertModelFactory.createCopy(draftCopyHolder, renewCopy));
        } catch (ConverterException e) {
            LOG.error("Could not create a new internal Webcert model", e);
            throw new ModuleConverterException("Could not create a new internal Webcert model", e);
        }
    }

    private PdfResponse generatePdf(AbstractLisjpPdfDefinitionBuilder builder, List<Status> statuses, LisjpUtlatandeV1 luseIntyg,
        ApplicationOrigin applicationOrigin, String filePrefix, UtkastStatus utkastStatus) throws ModuleException {
        try {
            IntygTexts texts = getTexts(LisjpEntryPoint.MODULE_ID, luseIntyg.getTextVersion());

            final FkPdfDefinition fkPdfDefinition = builder.buildPdfDefinition(luseIntyg, statuses, applicationOrigin, texts, utkastStatus);

            return new PdfResponse(PdfGenerator.generatePdf(fkPdfDefinition),
                PdfGenerator.generatePdfFilename(LocalDateTime.now(), filePrefix));
        } catch (PdfGeneratorException e) {
            LOG.error("Failed to generate PDF for certificate!", e);
            throw new ModuleSystemException("Failed to generate (standard copy) PDF for certificate!", e);
        }
    }

    @Override
    public Optional<AdditionalMetaData> getAdditionalMetaData(Intyg certificate) throws ModuleException {
        final var additionalMetaData = new AdditionalMetaData();

        final var lisjpCertificate = convertToInternal(certificate);

        final var diagnoses = getDiagnoses(lisjpCertificate.getDiagnoser());

        additionalMetaData.setDiagnoses(diagnoses);

        return Optional.of(additionalMetaData);
    }

    private LisjpUtlatandeV1 convertToInternal(Intyg certificate) throws ModuleException {
        try {
            return transportToInternal(certificate);
        } catch (ConverterException e) {
            throw new ModuleException("Could convert Intyg to Utlatande", e);
        }
    }

    @Override
    public Certificate getCertificateFromJson(String certificateAsJson) throws ModuleException {
        final var internalCertificate = getInternal(certificateAsJson);
        final var certificateTextProvider = getTextProvider(internalCertificate.getTyp(), internalCertificate.getTextVersion());
        return InternalToCertificate.convert(internalCertificate, certificateTextProvider);
    }

    @Override
    public String getJsonFromCertificate(Certificate certificate, String certificateAsJson) throws ModuleException {
        final var internalCertificate = getInternal(certificateAsJson);
        final var updateInternalCertificate = CertificateToInternal.convert(certificate, internalCertificate, moduleService);
        return toInternalModelResponse(updateInternalCertificate);
    }

    @Override
    public CertificateMessagesProvider getMessagesProvider() {
        return DefaultCertificateMessagesProvider.create(validationMessages);
    }

    @Override
    public String prefillWithFhirData(String model, List<String> diagnosis) throws ModuleException {
        // Convert json to utlåtande.
        final var internal = getInternal(model);

        // Update utlåtande with diagnosis
        var diagnosList = createDiagnoseListFromStrings(diagnosis);

        final var updatedDraft = internal.toBuilder()
            .setDiagnoser(diagnosList)
            .build();

        // Convert utlåtande to json
        return toInternalModelResponse(updatedDraft);
    }

    private List<Diagnos> createDiagnoseListFromStrings(List<String> diagnosis) {
        String splitCharacter = ":";
        List<Diagnos> list = new ArrayList<>();

        for (var str : diagnosis) {
            var split = str.split(splitCharacter);
            var kod = split[0];
            var displayName = split[1];
            var system = split[2];

            list.add(Diagnos.create(kod, system, displayName, displayName));
        }

        return list;
    }
}
