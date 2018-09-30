package com.poc.ig.repo.exception;

public class InvalidUserIdException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InvalidUserIdException(String message) {
		super(message);
	} 

}
