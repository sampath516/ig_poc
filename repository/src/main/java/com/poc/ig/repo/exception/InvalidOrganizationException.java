package com.poc.ig.repo.exception;

public class InvalidOrganizationException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InvalidOrganizationException(String message) {
		super(message);
	}
}
