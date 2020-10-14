/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.agent.shiro.realm;

import org.apache.shiro.authc.AuthenticationToken;

import com.woshidaniu.authz.agent.shiro.token.AgentAuthenticationToken;
import com.woshidaniu.shiro.realm.AccountRealm;
import com.woshidaniu.shiro.service.AccountService;
import com.woshidaniu.shiro.token.DelegateAuthenticationToken;

/**
 * 
 * @className	： AgentSsoRealm
 * @description	： 湖南机电职业（江苏达科教育科技有限公司）认证Shiro的Realm
 * @author 		：康康（1571）
 * @date		： 2018年5月11日 下午2:44:45
 * @version 	V1.0
 */
public class AgentSsoRealm extends AccountRealm {

	protected AccountService accountService;

	public AgentSsoRealm() {
		super.setAuthenticationTokenClass(AgentAuthenticationToken.class);
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
