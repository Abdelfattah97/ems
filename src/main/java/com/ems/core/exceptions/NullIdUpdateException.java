package com.ems.core.exceptions;


/**
 * this exception is thrown when trying to update an entity without providing its id
 */
public class NullIdUpdateException extends RuntimeException {

	public NullIdUpdateException(String message) {
        super(message);
    }
	
}