package se.inera.certificate.modules.fk7263.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import iso.v21090.dt.v1.II;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import se.inera.certificate.modules.fk7263.validator.LakarUtlatandeTypeValidator;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.LakarutlatandeType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.PatientType;

public class LakarUtlatandeTypeValidatorTest {

    private LakarutlatandeType lakarutlatande;
    private PatientType patient;
    private II patientId;
    private List<String> errors;
    private LakarUtlatandeTypeValidator validator;

    @Before
    public void setup() {
        lakarutlatande = new LakarutlatandeType();
        lakarutlatande.setLakarutlatandeId("id");
        patient = new PatientType();
        patientId = new II();
        patientId.setRoot("1.2.752.129.2.1.3.1");
        patientId.setExtension("19121212-1212");
        patient.setPersonId(patientId);
        patient.setFullstandigtNamn("namn");
        lakarutlatande.setPatient(patient);
        errors = new ArrayList<>();
        validator = new LakarUtlatandeTypeValidator(lakarutlatande, errors);
    }

    @Test
    public void testErrorsOnEmtpyCertificate() {
        lakarutlatande.setLakarutlatandeId(null);
        lakarutlatande.setPatient(null);
        validator.validateAndCorrect();
        assertTrue(errors.contains("No Lakarutlatande Id found!"));
        assertTrue(errors.contains("No Patient element found!"));
    }

    @Test
    public void testInvalidPatientIdExtension() {
        patientId.setExtension("19121212-121X");
        validator.validateAndCorrect();
        assertTrue(errors.contains("Wrong format for person-id! Valid format is YYYYMMDD-XXXX or YYYYMMDD+XXXX."));
    }

    @Test
    public void testDashInPatientIdExtensionIsCorrected() {
        patientId.setExtension("191212121212");
        validator.validateAndCorrect();
        assertTrue(errors.isEmpty());
        assertEquals("19121212-1212", patientId.getExtension());
    }

}
