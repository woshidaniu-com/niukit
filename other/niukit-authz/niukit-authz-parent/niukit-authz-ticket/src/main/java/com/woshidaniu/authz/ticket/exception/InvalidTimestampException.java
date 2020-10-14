/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.ticket.exception;

import org.apache.shiro.authc.AuthenticationException;

/**
 * 
 * @className	： InvalidTimestampException
 * @description	： 无效时间戳异常
 * @author 		：康康（1571）
 * @date		： 2018年5月10日 上午10:37:03
 * @version 	V1.0
 */
public class InvalidTimestampException extends AuthenticationException {

	private static final long serialVersionUID = 1L;
	/**
     * Creates a new InvalidTimestampException.
     */
    public InvalidTimestampException() {
        super();
    }

    /**
     * Constructs a new InvalidTimestampException.
     *
     * @param message the reason for the exception
     */
    public InvalidTimestampException(String message) {
        super(message);
    }

    /**
     * Constructs a new InvalidTimestampException.
     *
     * @param cause the underlying Throwable that caused this exception to be thrown.
     */
    public InvalidTimestampException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new InvalidTimestampException.
     *
     * @param message the reason for the exception
     * @param cause   the underlying Throwable that caused this exception to be thrown.
     */
    public InvalidTimestampException(String message, Throwable cause) {
        super(message, cause);
    }
	
}
