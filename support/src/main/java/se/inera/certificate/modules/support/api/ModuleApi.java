package se.inera.certificate.modules.support.api;

import org.joda.time.LocalDateTime;

import se.inera.certificate.modules.support.ApplicationOrigin;
import se.inera.certificate.modules.support.api.dto.CertificateResponse;
import se.inera.certificate.modules.support.api.dto.CreateNewDraftHolder;
import se.inera.certificate.modules.support.api.dto.HoSPersonal;
import se.inera.certificate.modules.support.api.dto.InternalModelHolder;
import se.inera.certificate.modules.support.api.dto.InternalModelResponse;
import se.inera.certificate.modules.support.api.dto.PdfResponse;
import se.inera.certificate.modules.support.api.dto.ValidateDraftResponse;
import se.inera.certificate.modules.support.api.exception.ModuleException;

/**
 * The module API defines methods that interact with one of the tree models that every module handles:
 * <ul>
 * <li>Transport model (XML-model that can be used to transmit an intyg between different parties)
 * <li>Internal model (JSON-model used to visualize an intyg in Mina Intyg and Webcert)
 * </ul>
 * 
 * There exists methods for converting between models, generate PDFs, interact with the internal model and extract meta
 * data.
 */
public interface ModuleApi {

    /**
     * Register the container for this module.
     * 
     * @param moduleContainer
     *            The module container.
     */
    void setModuleContainer(ModuleContainerApi moduleContainer);

    /**
     * Get the container for this module.
     * 
     * @return the module container.
     */
    ModuleContainerApi getModuleContainer();

    /**
     * Validates the internal model. The status (complete, incomplete) and a list of validation errors is returned.
     * 
     * @param internalModel
     *            The internal model to validate.
     * 
     * @return response The validation result.
     */
    ValidateDraftResponse validateDraft(InternalModelHolder internalModel) throws ModuleException;

    /**
     * Generates a PDF from the external model.
     * 
     * @param externalModel
     *            The external model to generate a PDF from.
     * @param applicationOrigin
     *            The context from which this method was called (i.e Webcert or MinaIntyg)
     * 
     * @return A {@link PdfResponse} consisting of a binary stream containing a PDF data and a suitable filename.
     */
    PdfResponse pdf(InternalModelHolder internalModel, ApplicationOrigin applicationOrigin) throws ModuleException;

    /**
     * Creates a new internal model. The model is prepopulated using data contained in the {@link CreateNewDraftHolder}
     * parameter.
     *
     * @param draftCertificateHolder
     *            The id of the new internal model, the {@link HoSPersonal} and
     *            {@link se.inera.certificate.modules.support.api.dto.Patient} data.
     *
     * @return A new instance of the internal model.
     */
    InternalModelResponse createNewInternal(CreateNewDraftHolder draftCertificateHolder) throws ModuleException;

    /**
     * Creates a new internal model. The model is prepopulated using data contained in the {@link CreateNewDraftHolder}
     * parameter and {@link InternalModelHolder} template.
     * 
     * @param draftCertificateHolder
     *            The id of the new internal model, the {@link HoSPersonal} and
     *            {@link se.inera.certificate.modules.support.api.dto.Patient} data.
     * @param template
     *            An internal model used as a template for the new internal model.
     * 
     * @return A new instance of the internal model.
     */
    InternalModelResponse createNewInternalFromTemplate(CreateNewDraftHolder draftCertificateHolder, InternalModelHolder template)
            throws ModuleException;

    /**
     * Returns an updated version of the internal model with new HoS person information.
     * 
     * @param internalModel
     *            The internal model to use as a base.
     * @param hosPerson
     *            The HoS person to complement the model with.
     * @param signingDate
     *            The timestamp of the signing of the intyg.
     * 
     * @return A new internal model updated with the hosPerson info.
     */
    InternalModelResponse updateInternal(InternalModelHolder internalModel, HoSPersonal hosPerson, LocalDateTime signingDate) throws ModuleException;

    /**
     * Register certificate in Intygstjänsten.
     * 
     * @param internalModel
     *            The internal model of the certificate to send.
     */
    void registerCertificate(InternalModelHolder internalModel) throws ModuleException;

    /**
     * Send certificate to specified recipient.
     * 
     * @param internalModel
     *            The internal model of the certificate to send.
     * @param recipient
     *            Where to send the certificate.
     */
    void sendCertificateToRecipient(InternalModelHolder internalModel, String recipient) throws ModuleException;

    /**
     * Get a certificate from intygstjansten.
     * 
     * @param certificateId
     *            The certificate id.
     * @return internal model of the certificate
     */
    CertificateResponse getCertificate(String certificateId) throws ModuleException;
}
