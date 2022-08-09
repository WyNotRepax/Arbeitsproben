package de.hsos.roomplanner.util.color;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;

import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * The annotated element must match a html colorr string
 * 
 * @author Benno Steinkamp
 */
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ColorStringValidator.class)
@Documented
public @interface ColorString {

    String message() default "de.hsos.roomplanner.util.color.ColorString.message";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
