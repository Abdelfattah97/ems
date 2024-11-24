package com.ems.core.exceptions;

/**
 * This Exception is thrown when a record is not found in the database
 */
public class ResourceNotFoundException extends RuntimeException {
	
    public ResourceNotFoundException(String message) {
        super(message);
    }
	
}
