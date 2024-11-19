package com.ems.core.validation.validator;

import com.ems.core.validation.annotation.MultiWord;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MultiWordValidator implements ConstraintValidator<MultiWord, String>{

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		
		return value!=null && value.matches("[a-zA-Z]+(')*[a-zA-Z]+( [a-zA-Z]+(')*[a-zA-Z]+)*");
		
	}

}
