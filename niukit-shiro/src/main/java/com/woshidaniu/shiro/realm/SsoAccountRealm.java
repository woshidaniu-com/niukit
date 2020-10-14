/**
 * 
 */
package com.woshidaniu.shiro.realm;

import org.apache.shiro.authc.AuthenticationToken;

import com.woshidaniu.shiro.service.AccountService;
import com.woshidaniu.shiro.token.DelegateAuthenticationToken;
import com.woshidaniu.shiro.token.ZfSsoToken;

/**
 * @author zhangxb
 *
 */
public class SsoAccountRealm extends AccountRealm {

	protected AccountService accountService;
	
	public SsoAccountRealm(){
		setAuthenticationTokenClass(ZfSsoToken.class);
	}
	
	/* (non-Javadoc)
	 * @see com.woshidaniu.shiro.realm.AccountRealm#getAccountService()
	 */
	@Override
	public AccountService getAccountService() {
		return accountService;
	}

	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}

	/* (non-Javadoc)
	 * @see com.woshidaniu.shiro.realm.AccountRealm#createDelegateAuthenticationToken(org.apache.shiro.authc.AuthenticationToken)
	 */
	@Override
	protected DelegateAuthenticationToken createDelegateAuthenticationToken(AuthenticationToken token) {
		return (DelegateAuthenticationToken)token;
	}

}
