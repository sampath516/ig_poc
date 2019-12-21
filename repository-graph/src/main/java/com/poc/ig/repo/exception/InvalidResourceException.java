package com.poc.ig.repo.exception;

public class InvalidResourceException extends RuntimeException {
	private static final long serialVersionUID = 1L; 

	public InvalidResourceException(String message) {
		super(message);
	}

}
