package com.poc.ig.certification.exception;

public class EntitlementsPublishedEventException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public EntitlementsPublishedEventException(String message) {
		super(message);
	}

	public EntitlementsPublishedEventException(String message, Throwable cause) {
		super(message, cause);
	}

	public EntitlementsPublishedEventException(Throwable cause) {
		super(cause);
	}
	
	
}
