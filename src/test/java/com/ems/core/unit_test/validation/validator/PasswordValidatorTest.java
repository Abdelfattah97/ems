package com.ems.core.unit_test.validation.validator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.ems.core.validation.validator.PasswordValidator;

public class PasswordValidatorTest {
	PasswordValidator validator = new PasswordValidator();
	
	@Test
	public void SHOULD_RETURN_FALSE_WHEN_DOESNOT_CONTAIN_NUMBERS() {
		assertFalse(validator.isValid("Password@", null));
	}
	@Test
	public void SHOULD_RETURN_FALSE_WHEN_DOESNOT_CONTAIN_SPECIAL() {
		assertFalse(validator.isValid("Password5", null));
	}
	@Test
	public void SHOULD_RETURN_FALSE_WHEN_DOESNOT_CONTAIN_CAPITAL() {
		assertFalse(validator.isValid("password@5", null));
	}
	@Test
	public void SHOULD_RETURN_FALSE_WHEN_SHORTER_THAN_SIX_CHARS() {
		assertFalse(validator.isValid("Pas@5", null));
	}
	@Test
	public void SHOULD_RETURN_TRUE_WHEN_CONTAIN_NUMBERS_CAPITAL_SPECIAL_ATLEAST_SIX_CHARS() {
		assertTrue(validator.isValid("Password@5", null));
		assertTrue(validator.isValid("Pass@5", null));
		assertTrue(validator.isValid("Pass@5", null));
		assertTrue(validator.isValid("@Pass5", null));
		assertTrue(validator.isValid("@5Pass", null));
		assertTrue(validator.isValid("5@Pass", null));
		assertTrue(validator.isValid("5Pa@ss", null));
		assertTrue(validator.isValid("Pa@5ss", null));
	}
	
}
