package com.woshidaniu.shiro.authc;

public class DefaultDelegateAuthenticationInfo implements DelegateAuthenticationInfo {

	private static final long serialVersionUID = 4271132717405297898L;

	private final Object principal;
	private final Object credentials;
	
	public DefaultDelegateAuthenticationInfo(Object principal, Object credentials) {
		super();
		this.principal = principal;
		this.credentials = credentials;
	}
	
	@Override
	public Object getPrincipal() {
		return this.principal;
	}
	
	@Override
	public Object getCredentials() {
		return this.credentials;
	}
}
