package com.ems.core.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.ems.core.validation.validator.MultiWordValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
/**
 * Ensures Field is not blank and contains one or more alphabetic words with no spaces 
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MultiWordValidator.class)
@NotBlank
@Target(ElementType.FIELD)
public @interface MultiWord {

	String message() default "This field should only contain one or more alphabetic words!";
	
	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
	
}
