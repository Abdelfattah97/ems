package com.ems.core.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ems.core.dto.FieldViolationMessage;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<String> resourceNotFoundHandler(ResourceNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).header("message", ex.getMessage()).body(ex.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> resourceNotFoundHandler(MethodArgumentNotValidException ex) {
		Map<String, Object> errorMsgs = new HashMap<String, Object>();
		errorMsgs.put("message", "Invalid Arguments!");
		errorMsgs.put("errors",
				ex.getFieldErrors().stream().map(
						e -> FieldViolationMessage.builder().field(e.getField()).message(e.getDefaultMessage()).build())
						.toList());

		String errorMsg = String.format("Field: %s, Error: %s", ex.getFieldError().getField(),
				ex.getFieldError().getDefaultMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
				.header("message", errorMsg).body(errorMsgs);
	}

	@ExceptionHandler(value = { NullPointerException.class, NullIdUpdateException.class,
			EntityDependencyMissingException.class, IllegalArgumentException.class })
	public ResponseEntity<String> handleBadReqExceptions(RuntimeException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("message", ex.getMessage()).body(ex.getMessage());
	}

}
