package com.poc.ig.repo.exception;

public class InvalidOrganizationIdException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InvalidOrganizationIdException(String message) {
		super(message);
	}
}
