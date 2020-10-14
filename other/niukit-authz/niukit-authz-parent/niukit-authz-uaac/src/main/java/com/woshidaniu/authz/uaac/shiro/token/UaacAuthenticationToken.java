/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.uaac.shiro.token;

import org.apache.shiro.authc.HostAuthenticationToken;

import com.woshidaniu.shiro.token.DelegateAuthenticationToken;

@SuppressWarnings("serial")
public class UaacAuthenticationToken implements DelegateAuthenticationToken,HostAuthenticationToken {
	
	protected String name; // 
	protected String uid; // 账号ID
	protected String host; // 登陆IP

	public UaacAuthenticationToken() {
	}
	
	public UaacAuthenticationToken(String uid, String name,  String host) {
		this.name = name;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
