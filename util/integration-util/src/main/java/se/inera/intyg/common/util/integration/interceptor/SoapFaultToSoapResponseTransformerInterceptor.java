package se.inera.intyg.common.util.integration.interceptor;

import javax.xml.transform.TransformerFactory;
import java.lang.reflect.Field;

import org.apache.cxf.feature.transform.AbstractXSLTInterceptor;
import org.apache.cxf.feature.transform.XSLTOutInterceptor;
import org.apache.cxf.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.intyg.common.util.logging.LogMarkers;

/**
 * CXF interceptor which turns SOAP faults into valid SOAP responses.
 *
 * Transformation is performed using XSLTs which transform the <soap:Fault> element to a proper response element
 * containing a <result> element giving more specifics about the error.
 *
 * @author andreaskaltenbach
 */
public class SoapFaultToSoapResponseTransformerInterceptor extends XSLTOutInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(SoapFaultToSoapResponseTransformerInterceptor.class);

    static {
        try {
            // Configure the private TransformerFactory defined in AbstractXSLTInterceptor
            Field transformFactoryField  = AbstractXSLTInterceptor.class.getDeclaredField("TRANSFORM_FACTORIY");
            transformFactoryField.setAccessible(true);
            TransformerFactory transformerFactory = (TransformerFactory) transformFactoryField.get(null);
            transformerFactory.setURIResolver(new ClasspathUriResolver());
            transformFactoryField.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set UriResolver for TransactionFactory", e);
        }
    }

    public static final int HTTP_OK = 200;

    public SoapFaultToSoapResponseTransformerInterceptor(String phase) {
        super(phase);
    }

    @Override
    public void handleMessage(Message message) {
        Exception exception = message.getContent(Exception.class);
        Throwable cause = exception.getCause();
        if (cause instanceof javax.xml.bind.UnmarshalException) {
            LOGGER.error(LogMarkers.VALIDATION, exception.getMessage());
        } else {
            LOGGER.error(exception.getMessage(), exception);
        }

        // switch HTTP status from 500 (internal server error) to 200 (ok)
        message.getExchange().getOutFaultMessage().put(Message.RESPONSE_CODE, HTTP_OK);

        super.handleMessage(message);
    }
}
