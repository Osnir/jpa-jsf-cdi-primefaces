package com.financeiro.dao.exception;

public class DataAccessException extends Exception {

	private static final long serialVersionUID = 1L;

	public DataAccessException() {
		super();
	}
	
	public DataAccessException(String message, Throwable cause, boolean enableSuppression, boolean writtableStackTrace) {
		super(message, cause, enableSuppression, writtableStackTrace);
	}
	
	public DataAccessException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public DataAccessException(String message) {
		super(message);
	}
	
	public DataAccessException(Throwable cause) {
		super(cause);
	}
}
