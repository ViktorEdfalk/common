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
package se.inera.intyg.common.ts_parent.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.StatusKod;
import se.inera.intyg.common.support.model.UtkastStatus;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.model.converter.WebcertModelFactory;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.model.converter.util.WebcertModelFactoryUtil;
import se.inera.intyg.common.support.model.util.ModelCompareUtil;
import se.inera.intyg.common.support.modules.converter.InternalToRevoke;
import se.inera.intyg.common.support.modules.converter.TransportConverterUtil;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.api.ModuleApi;
import se.inera.intyg.common.support.modules.support.api.dto.CertificateMetaData;
import se.inera.intyg.common.support.modules.support.api.dto.CertificateResponse;
import se.inera.intyg.common.support.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.intyg.common.support.modules.support.api.dto.CreateNewDraftHolder;
import se.inera.intyg.common.support.modules.support.api.dto.PdfResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateXmlResponse;
import se.inera.intyg.common.support.modules.support.api.exception.ExternalServiceCallException;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleConverterException;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleSystemException;
import se.inera.intyg.common.support.validate.RegisterCertificateValidator;
import se.inera.intyg.common.support.validate.XmlValidator;
import se.inera.intyg.common.ts_parent.codes.IntygAvserKod;
import se.inera.intyg.common.ts_parent.pdf.PdfGenerator;
import se.inera.intyg.common.ts_parent.pdf.PdfGeneratorException;
import se.inera.intyg.common.ts_parent.validator.InternalDraftValidator;
import se.riv.clinicalprocess.healthcond.certificate.getCertificate.v2.GetCertificateResponderInterface;
import se.riv.clinicalprocess.healthcond.certificate.getCertificate.v2.GetCertificateResponseType;
import se.riv.clinicalprocess.healthcond.certificate.getCertificate.v2.GetCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateResponderInterface;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateResponseType;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.revokeCertificate.v2.RevokeCertificateResponderInterface;
import se.riv.clinicalprocess.healthcond.certificate.revokeCertificate.v2.RevokeCertificateResponseType;
import se.riv.clinicalprocess.healthcond.certificate.revokeCertificate.v2.RevokeCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.CVType;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.IntygId;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.Part;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.ResultCodeType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar.Delsvar;

import javax.xml.bind.JAXB;
import javax.xml.ws.soap.SOAPFaultException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static se.inera.intyg.common.support.Constants.KV_PART_CODE_SYSTEM;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.INTYG_AVSER_DELSVAR_ID_1;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.INTYG_AVSER_SVAR_ID_1;

public abstract class TsParentModuleApi<T extends Utlatande> implements ModuleApi {

    private static final Logger LOG = LoggerFactory.getLogger(TsParentModuleApi.class);

    @Autowired
    private ModelCompareUtil<T> modelCompareUtil;

    @Autowired
    private InternalDraftValidator<T> validator;

    @Autowired
    private PdfGenerator<T> pdfGenerator;

    @Autowired
    private WebcertModelFactory<T> webcertModelFactory;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired(required = false)
    private GetCertificateResponderInterface getCertificateResponderInterface;

    @Autowired(required = false)
    @Qualifier("registerCertificateClient")
    private RegisterCertificateResponderInterface registerCertificateResponderInterface;

    @Autowired(required = false)
    private RevokeCertificateResponderInterface revokeCertificateClient;

    private Class<T> type;

    public TsParentModuleApi(Class<T> type) {
        this.type = type;
    }

    private RegisterCertificateValidator xmlValidator = new RegisterCertificateValidator(getSchematronFileName());

    @Override
    public CertificateResponse getCertificate(String certificateId, String logicalAddress, String recipientId) throws ModuleException {
        GetCertificateType request = new GetCertificateType();
        request.setIntygsId(getIntygsId(certificateId));
        request.setPart(getPart(recipientId));

        try {
            return convert(getCertificateResponderInterface.getCertificate(logicalAddress, request));
        } catch (SOAPFaultException e) {
            String error = String.format("Could not get certificate with id %s from Intygstjansten. SOAPFault: %s",
                    certificateId, e.getMessage());
            LOG.error(error);
            throw new ModuleException(error);
        }
    }

    @Override
    public void registerCertificate(String internalModel, String logicalAddress) throws ModuleException {
        RegisterCertificateType request;
        try {
            request = internalToTransport(getInternal(internalModel));
        } catch (ConverterException e) {
            LOG.error("Failed to convert to transport format during registerCertificate", e);
            throw new ModuleConverterException("Failed to convert to transport format during registerCertificate", e);
        }

        RegisterCertificateResponseType response = registerCertificateResponderInterface.registerCertificate(logicalAddress, request);

        // check whether call was successful or not
        if (response.getResult().getResultCode() == ResultCodeType.INFO) {
            throw new ExternalServiceCallException(response.getResult().getResultText(),
                    "Certificate already exists".equals(response.getResult().getResultText())
                            ? ExternalServiceCallException.ErrorIdEnum.VALIDATION_ERROR
                            : ExternalServiceCallException.ErrorIdEnum.APPLICATION_ERROR);
        } else if (response.getResult().getResultCode() == ResultCodeType.ERROR) {
            throw new ExternalServiceCallException(response.getResult().getErrorId() + " : " + response.getResult().getResultText());
        }
    }

    @Override
    public ValidateDraftResponse validateDraft(String internalModel) throws ModuleException {
        return validator.validateDraft(getInternal(internalModel));
    }

    @Override
    public String createNewInternal(CreateNewDraftHolder draftCertificateHolder) throws ModuleException {
        try {
            return toInternalModelResponse(webcertModelFactory.createNewWebcertDraft(draftCertificateHolder));

        } catch (ConverterException e) {
            LOG.error("Could not create a new internal Webcert model", e);
            throw new ModuleConverterException("Could not create a new internal Webcert model", e);
        }
    }

    @Override
    public String createNewInternalFromTemplate(CreateDraftCopyHolder draftCertificateHolder, Utlatande template)
            throws ModuleException {
        try {
            return toInternalModelResponse(webcertModelFactory.createCopy(draftCertificateHolder, template));
        } catch (ConverterException e) {
            LOG.error("Could not create a new internal Webcert model", e);
            throw new ModuleConverterException("Could not create a new internal Webcert model", e);
        }
    }

    @Override
    public String createRenewalFromTemplate(CreateDraftCopyHolder draftCopyHolder, Utlatande template)
            throws ModuleException {
        return createNewInternalFromTemplate(draftCopyHolder, template);
    }

    @Override
    public String updateBeforeSave(String internalModel, HoSPersonal hosPerson) throws ModuleException {
        return updateInternal(internalModel, hosPerson, null);
    }

    @Override
    public String updateBeforeSave(String internalModel, Patient patient) throws ModuleException {
        return updateInternal(internalModel, patient);
    }

    @Override
    public String updateBeforeSigning(String internalModel, HoSPersonal hosPerson, LocalDateTime signingDate)
            throws ModuleException {
        return updateInternal(internalModel, hosPerson, signingDate);
    }

    @Override
    public PdfResponse pdf(String internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin, UtkastStatus utkastStatus)
            throws ModuleException {
        try {
            return new PdfResponse(pdfGenerator.generatePDF(getInternal(internalModel), statuses, applicationOrigin, utkastStatus),
                    pdfGenerator.generatePdfFilename(getInternal(internalModel)));
        } catch (PdfGeneratorException e) {
            LOG.error("Failed to generate PDF for certificate!", e);
            throw new ModuleSystemException("Failed to generate PDF for certificate!", e);
        }
    }

    @Override
    public PdfResponse pdfEmployer(String internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin,
            List<String> optionalFields, UtkastStatus utkastStatus)
            throws ModuleException {
        throw new ModuleException("Feature not supported");
    }

    @Override
    public Utlatande getUtlatandeFromJson(String utlatandeJson) throws IOException {
        return objectMapper.readValue(utlatandeJson, type);
    }

    @Override
    public boolean shouldNotify(String persistedState, String currentState) throws ModuleException {
        T newUtlatande;
        newUtlatande = getInternal(currentState);

        return modelCompareUtil.isValidForNotification(newUtlatande);
    }

    @Override
    public String transformToStatisticsService(String inputXml) throws ModuleException {
        return inputXml;
    }

    @Override
    public ValidateXmlResponse validateXml(String inputXml) throws ModuleException {
        return XmlValidator.validate(xmlValidator, inputXml);
    }

    @Override
    public Map<String, List<String>> getModuleSpecificArendeParameters(Utlatande utlatande, List<String> frageIds) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Intyg getIntygFromUtlatande(Utlatande utlatande) throws ModuleException {
        try {
            return utlatandeToIntyg(type.cast(utlatande));
        } catch (Exception e) {
            LOG.error("Could not get intyg from utlatande: {}", e.getMessage());
            throw new ModuleException("Could not get intyg from utlatande", e);
        }
    }

    @Override
    public String getAdditionalInfo(Intyg intyg) throws ModuleException {
        List<CVType> types = new ArrayList<>();
        try {
            for (Svar svar : intyg.getSvar()) {
                if (INTYG_AVSER_SVAR_ID_1.equals(svar.getId())) {
                    for (Delsvar delsvar : svar.getDelsvar()) {
                        if (INTYG_AVSER_DELSVAR_ID_1.equals(delsvar.getId())) {
                            CVType cv = TransportConverterUtil.getCVSvarContent(delsvar);
                            if (cv != null) {
                                types.add(cv);
                            }
                        }
                    }
                }
            }
        } catch (ConverterException e) {
            LOG.error("Failed retrieving additionalInfo for certificate {}: {}", intyg.getIntygsId().getExtension(), e.getMessage());
            return null;
        }

        if (types.isEmpty()) {
            LOG.error("Failed retrieving additionalInfo for certificate {}: Found no types.", intyg.getIntygsId().getExtension());
            return null;
        }

        return types.stream()
                .map(cv -> IntygAvserKod.fromCode(cv.getCode()))
                .map(IntygAvserKod::name)
                .collect(Collectors.joining(", "));
    }

    @Override
    public void revokeCertificate(String xmlBody, String logicalAddress) throws ModuleException {
        RevokeCertificateType request = JAXB.unmarshal(new StringReader(xmlBody), RevokeCertificateType.class);
        RevokeCertificateResponseType response = revokeCertificateClient.revokeCertificate(logicalAddress, request);
        if (!response.getResult().getResultCode().equals(ResultCodeType.OK)) {
            String message = "Could not send revoke to " + logicalAddress;
            LOG.error(message);
            throw new ExternalServiceCallException(message);
        }
    }

    @Override
    public String createRevokeRequest(Utlatande utlatande, HoSPersonal skapatAv, String meddelande) throws ModuleException {
        try {
            StringWriter writer = new StringWriter();
            JAXB.marshal(InternalToRevoke.convert(utlatande, skapatAv, meddelande), writer);
            return writer.toString();
        } catch (ConverterException e) {
            throw new ModuleException(e.getMessage());
        }
    }

    @Override
    public String updateAfterSigning(String jsonModel, String signatureXml) throws ModuleException {
        // Note - until we've migrated T-intyg to v3 of RegisterCertificate for WC -> IT, we cannot attach the signature.
        return jsonModel;
    }

    protected abstract Intyg utlatandeToIntyg(T utlatande) throws ConverterException;

    protected T getInternal(String internalModel) throws ModuleException {
        try {
            return objectMapper.readValue(internalModel, type);
        } catch (IOException e) {
            throw new ModuleSystemException("Failed to deserialize internal model", e);
        }
    }

    protected String toInternalModelResponse(Utlatande internalModel) throws ModuleException {
        try {
            return objectMapper.writeValueAsString(internalModel);
        } catch (IOException e) {
            throw new ModuleSystemException("Failed to serialize internal model", e);
        }
    }

    private String updateInternal(String internalModel, HoSPersonal hosPerson, LocalDateTime signingDate)
            throws ModuleException {
        T utlatande = getInternal(internalModel);
        WebcertModelFactoryUtil.updateSkapadAv(utlatande, hosPerson, signingDate);
        return toInternalModelResponse(utlatande);
    }

    private String updateInternal(String internalModel, Patient patient) throws ModuleException {
        T utlatande = getInternal(internalModel);
        try {
            WebcertModelFactoryUtil.populateWithPatientInfo(utlatande.getGrundData(), patient);
        } catch (ConverterException e) {
            throw new ModuleException("Failed to update internal model with patient", e);
        }
        return toInternalModelResponse(utlatande);
    }

    protected abstract String getSchematronFileName();

    protected abstract RegisterCertificateType internalToTransport(T utlatande) throws ConverterException;

    protected abstract T transportToInternal(Intyg intyg) throws ConverterException;

    private IntygId getIntygsId(String certificateId) {
        IntygId intygId = new IntygId();
        intygId.setRoot("SE5565594230-B31");
        intygId.setExtension(certificateId);
        return intygId;
    }

    private Part getPart(String recipientId) {
        Part part = new Part();
        part.setCode(recipientId);
        part.setCodeSystem(KV_PART_CODE_SYSTEM);
        return part;
    }

    private CertificateResponse convert(GetCertificateResponseType response) throws ModuleException {
        try {
            T utlatande = transportToInternal(response.getIntyg());
            String internalModel = toInternalModelResponse(utlatande);
            CertificateMetaData metaData = TransportConverterUtil.getMetaData(response.getIntyg(), getAdditionalInfo(response.getIntyg()));
            boolean revoked = response.getIntyg().getStatus().stream()
                    .anyMatch(status -> StatusKod.CANCEL.name().equals(status.getStatus().getCode()));
            return new CertificateResponse(internalModel, utlatande, metaData, revoked);
        } catch (Exception e) {
            throw new ModuleException(e);
        }
    }
}
