/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.ticket.shiro.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.session.Session;

import com.woshidaniu.shiro.realm.RealmListener;
import com.woshidaniu.shiro.token.LoginType;
import com.woshidaniu.web.Parameter;
import com.woshidaniu.web.Parameters;

public class ZFTicketRealmListener implements RealmListener {

	@Override
	public void onAuthenticationSuccess(AuthenticationInfo info, Session session) {
		
		// 登录成功;记录登录方式标记；1：页面登录；2：单点登录；3：票据登录（通过握手秘钥等参数认证登录）
		session.setAttribute(Parameters.getGlobalString(Parameter.LOGIN_TYPE_KEY), LoginType.TICKIT);
		
	}
	
	@Override
	public void onAuthenticationFail(AuthenticationToken token, AuthenticationException ex) {
		
	}

	@Override
	public void onBeforeAuthentication(AuthenticationToken token) {
		
	}

}
