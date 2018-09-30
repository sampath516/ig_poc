package com.poc.ig.repo.exception;

public class InvalidResourceIdException extends RuntimeException {
	private static final long serialVersionUID = 1L; 

	public InvalidResourceIdException(String message) {
		super(message);
	}

}
