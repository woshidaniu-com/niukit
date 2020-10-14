package com.woshidaniu.shiro.authc;

import java.io.Serializable;

public interface DelegateAuthenticationInfo extends Serializable{

	Object getPrincipal();

	Object getCredentials();

}
