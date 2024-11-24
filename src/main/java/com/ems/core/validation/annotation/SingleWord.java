package com.ems.core.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.ems.core.validation.validator.SingleWordValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
/**
 * Ensures Field is not blank and contains a single alphabetic word with no spaces 
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SingleWordValidator.class)
@NotBlank(message = "This field should not be empty!")
@Target(ElementType.FIELD)
public @interface SingleWord {

	String message() default "This Field Should Contain One Alphabetic Word!";
	
	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
	
}
