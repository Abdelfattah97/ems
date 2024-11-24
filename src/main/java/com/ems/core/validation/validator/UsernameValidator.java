package com.ems.core.validation.validator;

import com.ems.core.validation.annotation.Username;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UsernameValidator implements ConstraintValidator<Username, String>{

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return value!=null && value.matches("^[A-Za-z]+[A-Za-z_0-9]{3,}$");
	}

}
