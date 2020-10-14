package com.woshidaniu.shiro.token;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @desc 验证码支持
 * 
 * @author 
 *
 */
public interface CaptchaAuthenticationToken extends AuthenticationToken {

	String getCaptcha();
	
}
