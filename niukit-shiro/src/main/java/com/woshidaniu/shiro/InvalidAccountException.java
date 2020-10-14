package com.woshidaniu.shiro;

import org.apache.shiro.authc.AccountException;

public class InvalidAccountException extends AccountException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2110512266031697524L;

	public InvalidAccountException() {
		super();
	}

	public InvalidAccountException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidAccountException(String message) {
		super(message);
	}

	public InvalidAccountException(Throwable cause) {
		super(cause);
	}

	
}
