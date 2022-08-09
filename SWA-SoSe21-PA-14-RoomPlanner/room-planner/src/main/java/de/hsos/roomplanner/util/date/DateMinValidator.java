package de.hsos.roomplanner.util.date;

import java.time.DateTimeException;
import java.time.LocalDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Benno Steinkamp
 */
public class DateMinValidator implements ConstraintValidator<DateMin, LocalDate> {

    private LocalDate dateMin;

    public void initialize(DateMin dateMin) {
        try {
            this.dateMin = LocalDate.of(dateMin.year(), dateMin.month(), dateMin.day());
        } catch (DateTimeException ex) {

        }
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        return value == null || dateMin == null || !value.isBefore(dateMin);
    }

}