package com.my.goldmanager.service.exception;

public class ValidationException extends Exception {

	private static final long serialVersionUID = -1399095606806062917L;

	public ValidationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ValidationException(String message) {
		super(message);
	}


}
