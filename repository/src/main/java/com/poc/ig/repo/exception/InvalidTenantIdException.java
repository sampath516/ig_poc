package com.poc.ig.repo.exception;

public class InvalidTenantIdException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InvalidTenantIdException(String message) {
		super(message); 
	}

}
