package com.poc.ig.connector.im.exception;

public class JsonParseException extends RuntimeException { 
	private static final long serialVersionUID = 1L;

	public JsonParseException(String message) {
		super(message);
	}

	public JsonParseException(String message, Throwable cause) {
		super(message, cause);
	}

	public JsonParseException(Throwable cause) {  
		super(cause); 
	}
	
	
}
