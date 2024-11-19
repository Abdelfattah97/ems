package com.ems.core.validation.validator;

import com.ems.core.validation.annotation.Password;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String>{

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return value!=null && value.matches("^(?=\\S+$)(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%^&+=!])[A-Za-z0-9@#$%^&+=!]{6,}$");
	}

}
