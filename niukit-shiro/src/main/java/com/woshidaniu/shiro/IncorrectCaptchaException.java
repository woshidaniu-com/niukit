package com.woshidaniu.shiro;

import org.apache.shiro.authc.AuthenticationException;

/**
 * 验证码错误异常
 * 
 * @author zhangxiaobin
 *
 */
public class IncorrectCaptchaException extends AuthenticationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5804347841925337928L;

	public IncorrectCaptchaException() {
		super();
	}

	public IncorrectCaptchaException(String message, Throwable cause) {
		super(message, cause);
	}

	public IncorrectCaptchaException(String message) {
		super(message);
	}

	public IncorrectCaptchaException(Throwable cause) {
		super(cause);
	}
}
