package com.ems.core.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.ems.core.dto.ArgumentTypeMismatchMessage;
import com.ems.core.dto.FieldViolationMessage;

import io.jsonwebtoken.JwtException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<String> resourceNotFoundHandler(ResourceNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).header("message", ex.getMessage()).body(ex.getMessage());
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ResponseEntity<ArgumentTypeMismatchMessage> resourceNotFoundHandler(MethodArgumentTypeMismatchException ex) {
		ArgumentTypeMismatchMessage argMsg = new ArgumentTypeMismatchMessage();
		argMsg.setField(ex.getName());
		argMsg.setValue(ex.getValue().toString());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.header("message", ex.getMessage())
				.contentType(MediaType.APPLICATION_JSON)
				.body(argMsg);
	}

	@ExceptionHandler(value = { MethodArgumentNotValidException.class })
	public ResponseEntity<Map<String, Object>> resourceNotFoundHandler(MethodArgumentNotValidException ex) {
		Map<String, Object> errorMsgs = new HashMap<String, Object>();
		errorMsgs.put("message", "Invalid Arguments!");
		errorMsgs.put("errors", ex.getFieldErrors()
				.stream()
				.map(e -> FieldViolationMessage.builder().field(e.getField()).message(e.getDefaultMessage()).build())
				.toList());

		String errorMsg = String.format("Field: %s, Error: %s", ex.getFieldError().getField(),
				ex.getFieldError().getDefaultMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.contentType(MediaType.APPLICATION_JSON)
				.header("message", errorMsg)
				.body(errorMsgs);
	}

	@ExceptionHandler(value = { NullPointerException.class, NullIdUpdateException.class,
			EntityDependencyMissingException.class, IllegalArgumentException.class })
	public ResponseEntity<String> handleBadReqExceptions(RuntimeException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("message", ex.getMessage()).body(ex.getMessage());
	}

	@ExceptionHandler(value = AccessDeniedException.class)
	@ResponseStatus(code = HttpStatus.FORBIDDEN)
	public ResponseEntity<String> handleBadCredentialsException(AccessDeniedException ex) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN).header("message", ex.getMessage()).body(ex.getMessage());
	}

	@ExceptionHandler(value = AuthenticationException.class)
	@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
	public ResponseEntity<String> handleBadCredentialsException(AuthenticationException ex) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).header("message", ex.getMessage()).body(ex.getMessage());
	}

	@ExceptionHandler(value = JwtException.class)
	public ResponseEntity<String> handleJwtException(JwtException ex) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).header("message", ex.getMessage()).body(ex.getMessage());
	}

	@ExceptionHandler(value = NoResourceFoundException.class)
	public ResponseEntity<String> handleNoResourceFoundException(NoResourceFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).header("message", ex.getMessage()).body(ex.getMessage());
	}

	@ExceptionHandler(value = HttpMessageNotReadableException.class)
	public ResponseEntity<String> handleHttpMwssageNotReadable(HttpMessageNotReadableException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("message", ex.getMessage()).body("Your Request doesn't follow the API specifications");
	}

	@ExceptionHandler(value = Exception.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<Void> handleGlobalException(Exception ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("message", ex.getMessage()).build();
	}

}
