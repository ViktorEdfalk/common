package se.inera.certificate.integration.module.exception;

/**
 * Exception thrown whenever a certificate with unknown certificate ID is tried to access, or the civic registration
 * number doesn't match the one in the certificate.
 *
 * @author andreaskaltenbach
 */
public class InvalidCertificateException extends Exception {

    private static final long serialVersionUID = 9207157337550587128L;

    public InvalidCertificateException(String certificateId, String civicRegistrationNumber) {
        super(civicRegistrationNumber != null
                ? String.format("Certificate '%s' does not exist for user '%s'", certificateId, civicRegistrationNumber)
                : String.format("Unknown certificate ID: %s", certificateId));
    }
}
