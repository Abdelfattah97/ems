package com.ems.core.validation.validator;

import com.ems.core.validation.annotation.SingleWord;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SingleWordValidator implements ConstraintValidator<SingleWord, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return value!=null && value.matches("^[a-zA-z]+$");
	}

}
