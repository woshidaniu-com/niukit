/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.idsplus.shiro.realm;

import org.apache.shiro.authc.AuthenticationToken;

import com.woshidaniu.authz.idsplus.shiro.token.IdsPlusAuthenticationToken;
import com.woshidaniu.shiro.realm.AccountRealm;
import com.woshidaniu.shiro.service.AccountService;
import com.woshidaniu.shiro.token.DelegateAuthenticationToken;

public class IdsPlusSsoRealm extends AccountRealm {
	
	protected AccountService accountService;

	public IdsPlusSsoRealm() {
		super.setAuthenticationTokenClass(IdsPlusAuthenticationToken.class);
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
