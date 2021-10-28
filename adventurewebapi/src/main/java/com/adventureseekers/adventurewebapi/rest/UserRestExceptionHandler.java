package com.adventureseekers.adventurewebapi.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.adventureseekers.adventurewebapi.error.response.UserErrorResponse;
import com.adventureseekers.adventurewebapi.exception.TokenNotFoundException;

/**
 * Handles the exceptions for user rest controller
 */
@ControllerAdvice
public class UserRestExceptionHandler {
	
	/**
	 * Handle any exceptions
	 * @param exc The general exception
	 * @return BAD_REQUEST for any exception
	 */
	@ExceptionHandler
	public ResponseEntity<UserErrorResponse> handleException(Exception exc) {
		UserErrorResponse error = new UserErrorResponse(
										HttpStatus.BAD_REQUEST.value(),
										exc.getMessage(),
										System.currentTimeMillis());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * Invalid token exceptions
	 * @return NOT_FOUND exception
	 */
	@ExceptionHandler
	public ResponseEntity<UserErrorResponse> handleException(TokenNotFoundException exc) {
		UserErrorResponse error = new UserErrorResponse(
										HttpStatus.NOT_FOUND.value(),
										exc.getMessage(),
										System.currentTimeMillis());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
}
