package com.ems.core.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.ems.core.validation.validator.UsernameValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
/**
 * Ensures Field contains valid username that starts with alphabetic character and can contain alphanumeric characters and underscore with minimum length of 4 characters 
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UsernameValidator.class)
@NotBlank(message = "This Username should not be blank!")
@Target(ElementType.FIELD)
public @interface Username {

	String message() default "Username is not Valid!";
	
	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
	
}
