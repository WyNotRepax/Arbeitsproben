package de.hsos.roomplanner.util.date;

import java.time.DateTimeException;
import java.time.LocalDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Benno Steinkamp
 */
public class DateMaxValidator implements ConstraintValidator<DateMax, LocalDate> {

    private LocalDate dateMax;

    public void initialize(DateMax dateMax) {
        try {
            this.dateMax = LocalDate.of(dateMax.year(), dateMax.month(), dateMax.day());
        } catch (DateTimeException ex) {
        }
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        return value == null || dateMax == null || !value.isAfter(dateMax);
    }

}