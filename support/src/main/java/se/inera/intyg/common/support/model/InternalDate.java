package se.inera.certificate.model;

import java.util.Objects;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * A way of handling Dates in our internal model that allows faulty user input,
 * this is needed at this stage because of the auto save function among other things.
 * <br/><br/>
 *
 * This class contains util methods for various things such as getting a string as a {@link LocalDate} etc.
 * @author erik
 */
public class InternalDate {

    private static final String DATE_FORMAT = "[1-2][0-9]{3,3}(-((0[1-9])|(1[0-2]))(-((0[1-9])|([1-2][0-9])|(3[0-1]))))";

    /**Parser used for parsing LocalDate[s] from Strings, uses {@code ISODateTimeFormat}. */
    private static final DateTimeFormatter PARSER = ISODateTimeFormat.date();

    private String date;

    /**
     * Default constructor.
     */
    public InternalDate() {
    }

    /**
     * Constuct an {@link se.inera.certificate.model.InternalDate} from a String.
     * @param date a String
     */
    public InternalDate(String date) {
            this.date = date;
    }

    /**
     * Constuct an {@link se.inera.certificate.model.InternalDate} from a {@link LocalDate},
     * primarily used when converting from external to internal model.
     * @param date a {@link LocalDate}
     * @throws {@link ModelException} if null is passed
     */
    public InternalDate(LocalDate date) {
        if (date == null) {
            throw new ModelException("Got null while creating date object");
        }
        this.date = date.toString(ISODateTimeFormat.date());
    }

    /*
     * Getters and setters
     */
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Attempts to parse the String held to a LocalDate.
     * @return {@link LocalDate} if parsing was successful
     * @throws ModelException if parsing failed
     */
    public LocalDate asLocalDate() {
        if (date == null) {
            throw new ModelException("Date was null");
        }
        LocalDate localDate;
        try {
            localDate = PARSER.parseLocalDate(date);
        } catch (IllegalArgumentException ie) {
            throw new ModelException(String.format("Could not parse %s to LocalDate, failed with message: %s", date, ie.getMessage()));
        }
        return localDate;
    }

    /**
     * Check if the string held in this InternalDate matches expected dateformat (yyyy-MM-dd).
     * @return true if it does, false otherwise
     */
    public boolean isValidDate() {
        if (date == null) {
            return false;
        }
        try {
            ISODateTimeFormat.date().parseLocalDate(date);
        } catch (Exception e) {
            return false;
        }
        return date.matches(DATE_FORMAT);
    }

    public boolean invalidOrInFuture() {
        if (date == null) {
            return true;
        }
        if (!this.isValidDate()) {
            return true;
        }
        return this.asLocalDate().isAfter(LocalDate.now());
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (getClass() != object.getClass()) {
            return false;
        }
        final InternalDate that = (InternalDate) object;
        return Objects.equals(this.date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.date);
    }

    @Override
    public String toString() {
        return Objects.toString(date);
    }

}
