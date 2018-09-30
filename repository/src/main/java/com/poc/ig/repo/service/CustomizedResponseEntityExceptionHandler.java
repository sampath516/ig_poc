package com.poc.ig.repo.service;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest; 
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.poc.ig.repo.exception.ExceptionDetails;
import com.poc.ig.repo.exception.InvalidOrganizationIdException;
import com.poc.ig.repo.exception.InvalidSearchParamException;
import com.poc.ig.repo.exception.InvalidTenantIdException;

@ControllerAdvice
//@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(InvalidSearchParamException.class)
	public final ResponseEntity<ExceptionDetails> handleInvalidSearchParamException(InvalidSearchParamException exc, WebRequest request) {
		ExceptionDetails excDetails = new ExceptionDetails(new Date(), exc.getMessage(), request.getDescription(true));
		return new ResponseEntity<>(excDetails, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(InvalidTenantIdException.class)
	public final ResponseEntity<ExceptionDetails> handleInvalidTenantIdException(InvalidTenantIdException exc, WebRequest request) {
		ExceptionDetails excDetails = new ExceptionDetails(new Date(), exc.getMessage(), request.getDescription(true));
		return new ResponseEntity<>(excDetails, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(InvalidOrganizationIdException.class)
	public final ResponseEntity<ExceptionDetails> handleInvalidOrganizationIdException(InvalidOrganizationIdException exc, WebRequest request) {
		ExceptionDetails excDetails = new ExceptionDetails(new Date(), exc.getMessage(), request.getDescription(true));
		return new ResponseEntity<>(excDetails, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(Exception.class)
	public final ResponseEntity<ExceptionDetails> handleAllExceptions(Exception exc, WebRequest request) {
		ExceptionDetails excDetails = new ExceptionDetails(new Date(), exc.getMessage(), request.getDescription(true));
		return new ResponseEntity<>(excDetails, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
