package com.woshidaniu.shiro.authenticator;

import java.util.Collection;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.AbstractAuthenticationStrategy;
import org.apache.shiro.realm.Realm;

/**
 * @author xiaobin zhang
 * 创建，基础代码
 * 
 * @author weiguangue
 * 修改，进入和退出，清空当前线程绑定的异常
 */
public class ZFOneSuccessfulStrategy extends AbstractAuthenticationStrategy {
	
	@Override
	public AuthenticationInfo afterAttempt(Realm realm, AuthenticationToken token, AuthenticationInfo singleRealmInfo,AuthenticationInfo aggregateInfo, Throwable t) throws AuthenticationException {

		if (t instanceof AuthenticationException) {
			ModularRealmAuthenticationExceptionHelper.addException((AuthenticationException) t);
		}

		return super.afterAttempt(realm, token, singleRealmInfo, aggregateInfo, t);
	}
	
	@Override
	public AuthenticationInfo beforeAllAttempts(Collection<? extends Realm> realms, AuthenticationToken token)throws AuthenticationException {
		
		//尝试所有realms之前,清空当前线程绑定的所有异常
		ModularRealmAuthenticationExceptionHelper.clear();
		
		return super.beforeAllAttempts(realms, token);
	}

	@Override
	public AuthenticationInfo afterAllAttempts(AuthenticationToken token, AuthenticationInfo aggregate)throws AuthenticationException {

		if (ModularRealmAuthenticationExceptionHelper.hasAuthenticationExceptions()) {
			
			AuthenticationException exception = ModularRealmAuthenticationExceptionHelper.getException();
			
			//尝试所有realms之后,清空当前线程绑定的所有异常
			ModularRealmAuthenticationExceptionHelper.clear();
			
			throw exception;
		}

		return aggregate;
	}
}
