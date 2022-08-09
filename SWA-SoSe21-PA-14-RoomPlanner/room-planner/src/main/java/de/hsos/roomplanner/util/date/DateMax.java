package de.hsos.roomplanner.util.date;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * @author Benno Steinkamp
 */
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateMaxValidator.class)
@Documented
public @interface DateMax {

    String message() default "de.hsos.roomplanner.util.date.DateMax.message";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int year() default DateUtil.MAX_DATE_YEAR;

    int month() default DateUtil.MAX_DATE_MONTH;

    int day() default DateUtil.MAX_DATE_DAY;

}
