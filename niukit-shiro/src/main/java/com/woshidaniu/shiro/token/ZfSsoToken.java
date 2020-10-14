/**
 * 
 */
package com.woshidaniu.shiro.token;

import org.apache.shiro.authc.HostAuthenticationToken;
import org.apache.shiro.authc.RememberMeAuthenticationToken;

@SuppressWarnings("serial")
public class ZfSsoToken implements DelegateAuthenticationToken, HostAuthenticationToken, RememberMeAuthenticationToken {
	
	protected String username; //用户名
	
	protected String host;	//登陆IP
	
	protected boolean rememberMe = false;//是否记住我
	
	public ZfSsoToken() {
		super();
	}

	public ZfSsoToken(String username, String host, boolean rememberMe) {
		this.username = username;
		this.host = host;
		this.rememberMe = rememberMe;
	}

	@Override
	public Object getPrincipal() {
		return username;
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public String getUsername() {
		return username;
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
	public String getHost() {
		return host;
	}

	@Override
	public boolean isRememberMe() {
		return rememberMe;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setRememberMe(boolean rememberMe) {
		this.rememberMe = rememberMe;
	}
	
}
