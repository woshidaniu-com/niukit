/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.ticket.shiro.realm;

import org.apache.shiro.authc.AuthenticationToken;

import com.woshidaniu.authz.ticket.shiro.token.ZFTicketAuthenticationToken;
import com.woshidaniu.shiro.realm.AccountRealm;
import com.woshidaniu.shiro.service.AccountService;
import com.woshidaniu.shiro.token.DelegateAuthenticationToken;

/**
 * 
 * @className	： ZFTicketRealm
 * @description	： ticket认证登录的Realm
 * @author 		：康康（1571）
 * @date		： 2018年5月2日 下午2:14:56
 * @version 	V1.0
 */
public class ZFTicketRealm extends AccountRealm {

	protected AccountService accountService;

	public ZFTicketRealm() {
		super.setAuthenticationTokenClass(ZFTicketAuthenticationToken.class);
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
