package de.hsos.roomplanner.util.paramconverter;

import javax.ws.rs.ext.ParamConverter;

import de.hsos.roomplanner.util.Dimension;

/**
 * @author Benno Steinkamp
 */
public class DimensionConverter implements ParamConverter<Dimension> {

    @Override
    public Dimension fromString(String value) {
        String[] numbers = value.split(",");
        if (numbers.length != 2) {
            return null;
        }
        try {
            float width = Float.parseFloat(numbers[0]);
            float height = Float.parseFloat(numbers[1]);
            return new Dimension(width, height);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    @Override
    public String toString(Dimension value) {
        if (value == null) {
            return "null";
        }
        return String.format("%f,%f", value.getWidth(), value.getHeight());
    }

}
