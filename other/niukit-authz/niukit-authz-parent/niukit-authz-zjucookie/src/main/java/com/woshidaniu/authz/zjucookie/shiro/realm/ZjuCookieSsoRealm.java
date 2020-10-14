/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.zjucookie.shiro.realm;

import org.apache.shiro.authc.AuthenticationToken;

import com.woshidaniu.authz.zjucookie.shiro.token.ZjuCookieAuthcToken;
import com.woshidaniu.shiro.realm.AccountRealm;
import com.woshidaniu.shiro.service.AccountService;
import com.woshidaniu.shiro.token.DelegateAuthenticationToken;

/**
 * 
 * @className	： ZjuCookieSsoRealm
 * @description	： ZjuCookie认证的Realm
 * @author 		：康康（1571）
 * @date		： 2018年5月10日 上午10:45:40
 * @version 	V1.0
 */
public class ZjuCookieSsoRealm extends AccountRealm {

	protected AccountService accountService;

	public ZjuCookieSsoRealm() {
		super.setAuthenticationTokenClass(ZjuCookieAuthcToken.class);
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
