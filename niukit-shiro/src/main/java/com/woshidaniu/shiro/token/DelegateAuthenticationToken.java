package com.woshidaniu.shiro.token;

import java.io.Serializable;

public interface DelegateAuthenticationToken extends Serializable {

	String getUsername();

	String getUsertype();

	char[] getPassword();
	
	int getStrength();

	String getCaptcha();

	String getHost();

	boolean isRememberMe();
}
