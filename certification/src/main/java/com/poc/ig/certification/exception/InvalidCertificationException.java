package com.poc.ig.certification.exception;

public class InvalidCertificationException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InvalidCertificationException(String message) {
		super(message);
	}
}
