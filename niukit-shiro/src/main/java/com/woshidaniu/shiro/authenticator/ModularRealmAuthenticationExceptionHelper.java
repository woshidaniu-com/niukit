/**
 * 
 */
package com.woshidaniu.shiro.authenticator;

import org.apache.shiro.authc.AuthenticationException;

/**
 * @author xiaobin.zhang
 *
 */
public class ModularRealmAuthenticationExceptionHelper {

	final static ThreadLocal<ModularRealmAuthenticationException> ModularRealmAuthenticationExceptionThreadLocal = new ThreadLocal<ModularRealmAuthenticationException>();

	public static void addException(AuthenticationException t) {
		ModularRealmAuthenticationException modularRealmAuthenticationException = ModularRealmAuthenticationExceptionThreadLocal
				.get();

		if (modularRealmAuthenticationException == null) {
			ModularRealmAuthenticationExceptionThreadLocal.set(new ModularRealmAuthenticationException());
		}

		ModularRealmAuthenticationExceptionThreadLocal.get().addAuthenticationException((AuthenticationException) t);

	}

	public static boolean hasAuthenticationExceptions() {
		return ModularRealmAuthenticationExceptionThreadLocal.get() != null
				&& ModularRealmAuthenticationExceptionThreadLocal.get().getExceptions().size() > 0;
	}

	public static void throwModularRealmAuthenticationException(){
		throw ModularRealmAuthenticationExceptionThreadLocal.get();
	}
	
	public static ModularRealmAuthenticationException getException(){
		return ModularRealmAuthenticationExceptionThreadLocal.get();
	}
	
	public static void clear(){
		ModularRealmAuthenticationExceptionThreadLocal.remove();
	}
	
}
