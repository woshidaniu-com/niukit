/**
 * 
 */
package com.woshidaniu.shiro.realm;

import org.apache.shiro.authc.AuthenticationToken;

import com.woshidaniu.shiro.service.AccountService;
import com.woshidaniu.shiro.token.DefaultAuthenticationToken;
import com.woshidaniu.shiro.token.DelegateAuthenticationToken;

/**
 * @author zhangxb
 * @desc 框架默认realm实现,只接受 {@link DefaultAuthenticationToken DefaultAuthenticationToken}
 * @class com.woshidaniu.shiro.realm.DefaultAccountRealm
 */
public class DefaultAccountRealm extends AccountRealm {
	
	protected AccountService accountService;
	
	public DefaultAccountRealm(){
		setAuthenticationTokenClass(DefaultAuthenticationToken.class);
	}
	
	@Override
	protected DelegateAuthenticationToken createDelegateAuthenticationToken(AuthenticationToken token) {
		return (DelegateAuthenticationToken)token;
	}

	public AccountService getAccountService() {
		return accountService;
	}
	
	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}
	
}
