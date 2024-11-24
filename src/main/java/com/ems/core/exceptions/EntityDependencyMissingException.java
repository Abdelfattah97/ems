package com.ems.core.exceptions;

/**
 * This Exception is thrown when a parent in relationship cannot be found before saving or updating
 */
public class EntityDependencyMissingException extends RuntimeException {

	public EntityDependencyMissingException(String message) {
        super(message);
    }
	
}
