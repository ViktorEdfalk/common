package se.inera.certificate.integration.json;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.Partial;

import se.inera.certificate.model.InternalDate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.joda.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.joda.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.joda.ser.LocalDateTimeSerializer;

/**
 * Customized Jackson ObjectMapper for the inera-certificate projects.
 *
 * -registers additional serializers and deserializers for JODA date and time types
 * -registers a specialized serializer to represent certificate-specific data as JSON
 *
 * @author andreaskaltenbach
 */
public class CustomObjectMapper extends ObjectMapper {

    public CustomObjectMapper() {
        setSerializationInclusion(JsonInclude.Include.NON_NULL);
        configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        registerModule(new Module());
    }

    private static final class Module extends SimpleModule {
        private Module() {
            addSerializer(Partial.class, new PartialSerializer());
            addDeserializer(Partial.class, new PartialDeserializer());

            addSerializer(InternalDate.class, new InternalDateSerializer());
            addDeserializer(InternalDate.class, new InternalDateDeserializer());

            addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
            addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());

            addSerializer(LocalDate.class, new LocalDateSerializer());
            //addDeserializer(LocalDate.class, new LocalDateDeserializer());
            /*
             * Using a custom crafted deserializer that handles dates
             * on the UTC format. The original LocalDateDeserializer class do not
             * handle the UTC format.
             */
            addDeserializer(LocalDate.class, new CustomLocalDateDeserializer());
        }

    }
}
