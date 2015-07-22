package com.pantuo.util;
@SuppressWarnings("serial")
public class SendMailException extends RuntimeException {

	public SendMailException(String message) {
		super(message);
	}

	public SendMailException(String message, Throwable cause) {
		super(message, cause);
	}

}
