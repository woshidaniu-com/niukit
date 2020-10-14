/**
 * 
 */
package com.woshidaniu.shiro;

import org.apache.shiro.authc.AccountException;

/**
 * @author xiaobin zhang
 *
 */
public class CheckAttemptsAccountException extends AccountException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3445118589846558176L;

	public CheckAttemptsAccountException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CheckAttemptsAccountException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public CheckAttemptsAccountException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public CheckAttemptsAccountException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
