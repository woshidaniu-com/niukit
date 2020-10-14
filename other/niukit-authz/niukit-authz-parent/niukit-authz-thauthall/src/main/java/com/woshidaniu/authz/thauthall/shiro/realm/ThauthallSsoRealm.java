/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.thauthall.shiro.realm;

import org.apache.shiro.authc.AuthenticationToken;

import com.woshidaniu.authz.thauthall.shiro.token.ThauthallAuthenticationToken;
import com.woshidaniu.shiro.realm.AccountRealm;
import com.woshidaniu.shiro.service.AccountService;
import com.woshidaniu.shiro.token.DelegateAuthenticationToken;

public class ThauthallSsoRealm extends AccountRealm {

	protected AccountService accountService;

	public ThauthallSsoRealm() {
		super.setAuthenticationTokenClass(ThauthallAuthenticationToken.class);
	}

	@Override
	protected DelegateAuthenticationToken createDelegateAuthenticationToken(AuthenticationToken token) {
		return (DelegateAuthenticationToken) token;
	}

	@Override
	public AccountService getAccountService() {
		return accountService;
	}

	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}
	
}
