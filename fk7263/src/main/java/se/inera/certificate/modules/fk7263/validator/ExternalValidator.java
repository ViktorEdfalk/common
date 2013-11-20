package se.inera.certificate.modules.fk7263.validator;

import static java.util.Arrays.asList;

import java.util.List;

import org.springframework.util.StringUtils;

import se.inera.certificate.model.Observation;
import se.inera.certificate.model.util.Strings;
import se.inera.certificate.modules.fk7263.model.codes.ObservationsKoder;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Utlatande;

/**
 * Validates a fk7263 certificate's specific rules that's not covered by internal validation.
 * 
 * @author marced
 */
public class ExternalValidator extends AbstractValidator{

    private Fk7263Utlatande externalUtlatande;

    private static final List<String> VALID_DIAGNOSE_CODE_SYSTEMS = asList("ICD-10");

    public ExternalValidator(Fk7263Utlatande utlatande) {
        this.externalUtlatande = utlatande;
    }

    public List<String> validate() {

        validateDiagnose();

        return validationErrors;
    }

    private void validateDiagnose() {

        Observation huvudDiagnos = externalUtlatande.findObservationByKategori(ObservationsKoder.DIAGNOS);
        if (huvudDiagnos == null || huvudDiagnos.getObservationskod() == null
                || StringUtils.isEmpty(huvudDiagnos.getObservationskod().getCode())) {
            addValidationError("Field 2: Missing diagnose code");
            return;
        }

        if (StringUtils.isEmpty(huvudDiagnos.getObservationskod().getCodeSystemName())
                || !VALID_DIAGNOSE_CODE_SYSTEMS.contains(huvudDiagnos.getObservationskod().getCodeSystemName())) {
            addValidationError(String.format("Field 2: Invalid diagnose code system! Should be one of %s",
                    Strings.join(" or ", VALID_DIAGNOSE_CODE_SYSTEMS)));
            return;
        }

    }  

}
