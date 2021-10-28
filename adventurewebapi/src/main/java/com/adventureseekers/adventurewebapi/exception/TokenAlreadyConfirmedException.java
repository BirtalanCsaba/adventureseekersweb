package com.adventureseekers.adventurewebapi.exception;

public class TokenAlreadyConfirmedException extends RuntimeException {
	
	public TokenAlreadyConfirmedException() {
	}

	public TokenAlreadyConfirmedException(String message) {
		super("Token already confirmed - " + message);
	}

	public TokenAlreadyConfirmedException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public TokenAlreadyConfirmedException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public TokenAlreadyConfirmedException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
