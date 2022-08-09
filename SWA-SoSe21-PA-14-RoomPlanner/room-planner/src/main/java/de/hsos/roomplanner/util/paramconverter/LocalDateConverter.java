package de.hsos.roomplanner.util.paramconverter;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.ws.rs.ext.ParamConverter;

/**
 * @author Benno Steinkamp
 */
public class LocalDateConverter implements ParamConverter<LocalDate> {

    @Override
    public LocalDate fromString(String value) {
        if (value == null || value.length() == 0) {
            return null;
        }
        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException ex) {
            return null;
        }
    }

    @Override
    public String toString(LocalDate value) { return String.valueOf(value); }

}
