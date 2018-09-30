package com.poc.ig.repo.exception;

public class InvalidRoleIdException extends RuntimeException {
	private static final long serialVersionUID = 1L; 

	public InvalidRoleIdException(String message) {
		super(message);
	}

}
