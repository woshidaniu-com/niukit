/**
 * 
 */
package com.woshidaniu.shiro.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.session.Session;

/**
 * @author realm listener
 *
 */
public interface RealmListener {

	/**
	 * 当认证之前调用
	 * @param token
	 */
	void onBeforeAuthentication(AuthenticationToken token);
	
	/**
	 * 当认证失败时调用【报异常或则是查询不到认证信息认为是失败】
	 * @param token
	 * @param ex
	 */
	void onAuthenticationFail(AuthenticationToken token, AuthenticationException ex);

	/**
	 * 当认证成功时调用
	 * 
	 * @param info
	 * @param session
	 */
	void onAuthenticationSuccess(AuthenticationInfo info, Session session);

}
