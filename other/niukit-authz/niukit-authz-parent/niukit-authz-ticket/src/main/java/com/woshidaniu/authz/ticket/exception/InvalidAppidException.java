/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.ticket.exception;

import org.apache.shiro.authc.AuthenticationException;

/**
 * 
 * @className	： InvalidAppidException
 * @description	： 无效Appid异常
 * @author 		：康康（1571）
 * @date		： 2018年5月10日 上午10:36:46
 * @version 	V1.0
 */
public class InvalidAppidException extends AuthenticationException {

	private static final long serialVersionUID = 1L;
	/**
     * Creates a new InvalidAppidException.
     */
    public InvalidAppidException() {
        super();
    }

    /**
     * Constructs a new InvalidAppidException.
     *
     * @param message the reason for the exception
     */
    public InvalidAppidException(String message) {
        super(message);
    }

    /**
     * Constructs a new InvalidAppidException.
     *
     * @param cause the underlying Throwable that caused this exception to be thrown.
     */
    public InvalidAppidException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new InvalidAppidException.
     *
     * @param message the reason for the exception
     * @param cause   the underlying Throwable that caused this exception to be thrown.
     */
    public InvalidAppidException(String message, Throwable cause) {
        super(message, cause);
    }
	
}
