package se.inera.certificate.modules.ts_bas.utils;

import se.inera.certificate.modules.ts_bas.rest.dto.CertificateContentHolder;
import se.inera.certificate.ts_bas.model.v1.Utlatande;

/**
 * Defines a scenario that can be tested. The following models (as POJOs) can be extracted from a scenario:
 * <ul>
 * <li>Transport model
 * <li>Export model (with and without a {@link CertificateContentHolder})
 * <li>Internal model used by Mina Intyg
 * <li>Internal model used by WebCert
 * </ul>
 * 
 * @see ScenarioFinder
 */
public interface Scenario {

    /**
     * Returns the name of the scenario. Useful for assertion messages.
     * 
     * @return The scenario name.
     */
    String getName();

    /**
     * Returns the scenario as a transport model.
     * 
     * @return The scenario as a transport model.
     * @throws ScenarioNotFoundException
     *             if the scenario wasn't found.
     */
    Utlatande asTransportModel() throws ScenarioNotFoundException;

    /**
     * Returns the scenario as a external model.
     * 
     * @return The scenario as a external model.
     * @throws ScenarioNotFoundException
     *             if the scenario wasn't found.
     */
    se.inera.certificate.modules.ts_bas.model.external.Utlatande asExternalModel() throws ScenarioNotFoundException;

    /**
     * Returns the scenario as a external model, wrapped in a {@link CertificateContentHolder}.
     * 
     * @return The scenario as a wrapped external model.
     * @throws ScenarioNotFoundException
     *             if the scenario wasn't found.
     */
    CertificateContentHolder asExternalModelWithHolder() throws ScenarioNotFoundException;

    /**
     * Returns the scenario as a internal Mina Intyg model.
     * 
     * @return The scenario as a internal Mina Intyg model.
     * @throws ScenarioNotFoundException
     *             if the scenario wasn't found.
     */
    se.inera.certificate.modules.ts_bas.model.internal.Utlatande asInternalModel() throws ScenarioNotFoundException;
}
