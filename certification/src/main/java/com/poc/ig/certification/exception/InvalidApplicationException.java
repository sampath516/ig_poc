package com.poc.ig.certification.exception;

public class InvalidApplicationException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InvalidApplicationException(String message) {
		super(message);
	}
}
