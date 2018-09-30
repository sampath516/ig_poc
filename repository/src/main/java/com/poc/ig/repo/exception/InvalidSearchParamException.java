package com.poc.ig.repo.exception;

//@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InvalidSearchParamException extends RuntimeException {

	private static final long serialVersionUID = 1L; 

	public InvalidSearchParamException(String message) {
		super(message);
	}

}
