package com.adventureseekers.adventurewebapi.exception;

public class TokenExpiredException extends RuntimeException {

	public TokenExpiredException() {
		// TODO Auto-generated constructor stub
	}

	public TokenExpiredException(String message) {
		super("Token expired - " + message);
		// TODO Auto-generated constructor stub
	}

	public TokenExpiredException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public TokenExpiredException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public TokenExpiredException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
