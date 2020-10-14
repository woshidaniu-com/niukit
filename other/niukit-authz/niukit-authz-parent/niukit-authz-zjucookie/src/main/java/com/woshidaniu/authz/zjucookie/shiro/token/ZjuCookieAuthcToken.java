/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.zjucookie.shiro.token;

import org.apache.shiro.authc.HostAuthenticationToken;

import com.woshidaniu.shiro.token.DelegateAuthenticationToken;

@SuppressWarnings("serial")
public class ZjuCookieAuthcToken implements DelegateAuthenticationToken , HostAuthenticationToken {

	protected ZjuCookie cookie;	//Cookie中的账号信息
	
	public ZjuCookieAuthcToken() {
	}
	
	public ZjuCookieAuthcToken(ZjuCookie cookie) {
		this.cookie = cookie;
	}
	
	@Override
	public Object getPrincipal() {
		return cookie.getUid();
	}
	
	@Override
	public Object getCredentials() {
		return cookie.getUid();
	}
	
	@Override
	public String getHost() {
		return cookie.getHost();
	}
	
	@Override
	public String getUsername() {
		return cookie.getUid();
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
	
}
