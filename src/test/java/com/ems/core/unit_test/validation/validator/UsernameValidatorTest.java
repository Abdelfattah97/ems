package com.ems.core.unit_test.validation.validator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.ems.core.validation.validator.UsernameValidator;
public class UsernameValidatorTest{
	UsernameValidator validator = new UsernameValidator();

//	@Override
//	public boolean isValid(String value, ConstraintValidatorContext context) {
//		return value!=null && value.matches("^[A-Za-z]+[A-Za-z_0-9]{3,}$");
//	}

	@Test
	public void SHOULD_RETURN_FALSE_ON_BLANK() {
        assertFalse(validator.isValid("", null));
        assertFalse(validator.isValid("   ", null));
        assertFalse(validator.isValid(null, null));
	}
	
	@Test
	public void SHOULD_RETURN_FALSE_WHEN_LENGTH_IS_SMALLER_THAN_FOUR() {
		assertFalse(validator.isValid("aaa", null));
	}
	
	@Test
	public void SHOULD_RETURN_FALSE_WHEN_STARTS_WHITH_NON_ALPHABETIC() {
		assertFalse(validator.isValid("0BGH", null));
	}
	
	@Test
	public void SHOULD_RETURN_FALSE_CONTAINING_OTHER_SPEC_CHARS_EXCEPT_UNDERSCORE() {
		assertFalse(validator.isValid("BGH!A", null));
		assertFalse(validator.isValid("BGH A", null));
	}
	
	@Test
	public void SHOULD_RETURN_TRUE_WHEN_VALID() {
		assertTrue(validator.isValid("a_as", null));
		assertTrue(validator.isValid("a_a5", null));
		assertTrue(validator.isValid("as_5", null));
		assertTrue(validator.isValid("a5_5", null));
		assertTrue(validator.isValid("a5_e", null));
		assertTrue(validator.isValid("aaaaaaaaa", null));
	}

}
