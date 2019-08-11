package com.poc.ig.repo.exception;

public class InvalidTenantException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InvalidTenantException(String message) {
		super(message); 
	}

}
