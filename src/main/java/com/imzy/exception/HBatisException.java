package com.imzy.exception;

public class HBatisException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public HBatisException() {
		super();
	}

	public HBatisException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public HBatisException(String message, Throwable cause) {
		super(message, cause);
	}

	public HBatisException(String message) {
		super(message);
	}

	public HBatisException(Throwable cause) {
		super(cause);
	}

}
