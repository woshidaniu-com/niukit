/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.ticket.exception;

import org.apache.shiro.authc.AuthenticationException;

/**
 * 
 * @className	： InvalidVerifyException
 * @description	： 无效Verify异常
 * @author 		：康康（1571）
 * @date		： 2018年5月10日 上午10:38:12
 * @version 	V1.0
 */
public class InvalidVerifyException extends AuthenticationException {

	private static final long serialVersionUID = 1L;

	/**
     * Creates a new InvalidVerifyException.
     */
    public InvalidVerifyException() {
        super();
    }

    /**
     * Constructs a new InvalidVerifyException.
     *
     * @param message the reason for the exception
     */
    public InvalidVerifyException(String message) {
        super(message);
    }

    /**
     * Constructs a new InvalidVerifyException.
     *
     * @param cause the underlying Throwable that caused this exception to be thrown.
     */
    public InvalidVerifyException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new InvalidVerifyException.
     *
     * @param message the reason for the exception
     * @param cause   the underlying Throwable that caused this exception to be thrown.
     */
    public InvalidVerifyException(String message, Throwable cause) {
        super(message, cause);
    }
	
}
