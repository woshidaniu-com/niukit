/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.ticket.exception;

import org.apache.shiro.authc.AuthenticationException;

/**
 * 
 * @className	： InvalidTokenException
 * @description	： 无效Token异常
 * @author 		：康康（1571）
 * @date		： 2018年5月10日 上午10:37:20
 * @version 	V1.0
 */
public class InvalidTokenException extends AuthenticationException {

	private static final long serialVersionUID = 1L;
	/**
     * Creates a new InvalidTokenException.
     */
    public InvalidTokenException() {
        super();
    }

    /**
     * Constructs a new InvalidTokenException.
     *
     * @param message the reason for the exception
     */
    public InvalidTokenException(String message) {
        super(message);
    }

    /**
     * Constructs a new InvalidTokenException.
     *
     * @param cause the underlying Throwable that caused this exception to be thrown.
     */
    public InvalidTokenException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new InvalidTokenException.
     *
     * @param message the reason for the exception
     * @param cause   the underlying Throwable that caused this exception to be thrown.
     */
    public InvalidTokenException(String message, Throwable cause) {
        super(message, cause);
    }
	
}
