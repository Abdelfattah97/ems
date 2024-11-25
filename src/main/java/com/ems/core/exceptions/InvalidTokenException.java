package com.ems.core.exceptions;

import java.nio.file.AccessDeniedException;

import org.springframework.security.core.AuthenticationException;

public class InvalidTokenException extends AccessDeniedException {
	private static final long serialVersionUID = 1L;

	public InvalidTokenException(String msg) {
		super(msg);
	}

}
