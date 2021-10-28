package com.adventureseekers.adventurewebapi.exception;

public class TokenNotFoundException extends RuntimeException {

	public TokenNotFoundException() {
		// TODO Auto-generated constructor stub
	}

	public TokenNotFoundException(String message) {
		super("Token not found - " + message);
	}

	public TokenNotFoundException(Throwable cause) {
		super(cause);
	}

	public TokenNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public TokenNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
