/**
 * 我是大牛软件股份有限公司
 */
package com.woshidaniu.shiro;

import org.apache.shiro.authc.AccountException;

/**
 * @类名 InvalidStateException.java
 * @工号 [1036]
 * @姓名 zhangxiaobin
 * @创建时间 2015 2015年11月19日 下午1:09:04
 * @功能描述 用户状态异常
 * 
 */
public class InvalidStateException extends AccountException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7850089047311189478L;

	public InvalidStateException() {
		super();

	}

	public InvalidStateException(String message, Throwable cause) {
		super(message, cause);

	}

	public InvalidStateException(String message) {
		super(message);

	}

	public InvalidStateException(Throwable cause) {
		super(cause);

	}

}
