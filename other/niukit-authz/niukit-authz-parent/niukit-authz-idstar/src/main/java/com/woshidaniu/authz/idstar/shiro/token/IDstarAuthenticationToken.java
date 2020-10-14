/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.idstar.shiro.token;

import org.apache.shiro.authc.HostAuthenticationToken;

import com.woshidaniu.shiro.token.DelegateAuthenticationToken;

/**
 * 
 * @className	： IDstarAuthenticationToken
 * @description	： 金智的单点认证登录Token
 * @author 		：康康（1571）
 * @date		： 2018年5月10日 上午10:35:31
 * @version 	V1.0
 */
@SuppressWarnings("serial")
public class IDstarAuthenticationToken implements DelegateAuthenticationToken,HostAuthenticationToken {

	protected String token; // Cookie中的Token信息
	protected String uid; // 账号ID
	protected String host; // 登陆IP

	public IDstarAuthenticationToken() {
	}
	
	public IDstarAuthenticationToken(String token, String uid, String host) {
		this.token = token;
		this.uid = uid;
		this.host = host;
	}
	
	@Override
	public Object getPrincipal() {
		return uid;
	}

	@Override
	public Object getCredentials() {
		return uid;
	}

	@Override
	public String getHost() {
		return host;
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public String getToken() {
		return token;
	}
	
	@Override
	public String getUsername() {
		return uid;
	}

	@Override
	public String getUsertype() {
		return null;
	}

	@Override
	public char[] getPassword() {
		return null;
	}

	@Override
	public int getStrength() {
		return 0;
	}

	@Override
	public String getCaptcha() {
		return null;
	}

	@Override
	public boolean isRememberMe() {
		return false;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public void setToken(String token) {
		this.token = token;
	}

	
	
}
