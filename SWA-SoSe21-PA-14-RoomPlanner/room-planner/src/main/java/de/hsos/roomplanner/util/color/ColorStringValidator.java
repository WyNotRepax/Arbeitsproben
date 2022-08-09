package de.hsos.roomplanner.util.color;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Benno Steinkamp
 */
public class ColorStringValidator implements ConstraintValidator<ColorString, String> {

    /**
     * Colors from #000000 - #ffffff
     */
    private static final String PATTERN = "\\A#[0-9a-fA-F]{6}\\Z";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return value.matches(PATTERN);
    }

}
