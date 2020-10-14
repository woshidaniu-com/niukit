/**
 * 
 */
package com.woshidaniu.shiro.authenticator;

import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.authc.AuthenticationException;

/**
 * @author xiaobin zhang
 *
 */
public class ModularRealmAuthenticationException extends AuthenticationException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Map<Class<?>, AuthenticationException> exceptions = new HashMap<Class<?>, AuthenticationException>();

	public Map<Class<?>, AuthenticationException> getExceptions() {
		return exceptions;
	}

	public void setExceptions(Map<Class<?>, AuthenticationException> exceptions) {
		this.exceptions = exceptions;
	}

	public void addAuthenticationException(AuthenticationException ex) {
		this.exceptions.put(ex.getClass(), ex);
	}

	public ModularRealmAuthenticationException() {
		super();
	}

	public ModularRealmAuthenticationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ModularRealmAuthenticationException(String message) {
		super(message);
	}

	public ModularRealmAuthenticationException(Throwable cause) {
		super(cause);
	}

}
