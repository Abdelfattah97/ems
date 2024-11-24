package com.ems.core.unit_test.validation.validator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.ems.core.validation.validator.MultiWordValidator;


public class MultiWordValidatorTest {
	MultiWordValidator validator = new MultiWordValidator();

	@Test
	public void Should_return_true_on_alphabetic_single_word() {
		assertTrue(validator.isValid("hello", null));
		assertTrue(validator.isValid("hello world", null));
		assertTrue(validator.isValid("hello world it's java", null));
		assertTrue(validator.isValid("Mohamed ALi", null));
		
	}
	
	@Test
	public void Should_return_false_on_blank() {
		assertFalse(validator.isValid("", null));
		assertFalse(validator.isValid(" ", null));
	}

	@Test
	public void Should_Return_false_on_non_alphabetic() {
		assertFalse(validator.isValid("hello8", null));
		assertFalse(validator.isValid("hello!", null));
		assertFalse(validator.isValid(" hello", null));
		assertFalse(validator.isValid("hello ", null));
		assertFalse(validator.isValid("hello world ", null));
		assertFalse(validator.isValid("hello world!", null));
		assertFalse(validator.isValid("hello world !", null));
	}

	@Test
	public void Should_return_true_on_multiWord() {
		assertTrue(validator.isValid("hello world", null));
	}
}
