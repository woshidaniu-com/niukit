/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.oauth2.shiro.token;


import org.apache.shiro.authc.AuthenticationToken;
/**
 * 
 * @className	： OAuth2Token
 * @description	： Oauth2登录认证的Shiro的Token
 * @author 		：康康（1571）
 * @date		： 2018年5月7日 上午9:21:51
 * @version 	V1.0
 */
public class OAuth2Token implements AuthenticationToken{
	
	private static final long serialVersionUID = 1L;
	private String authCode;
	private String username;
	
	public OAuth2Token(String authCode) {
		super();
		this.authCode = authCode;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public Object getPrincipal() {
		return authCode;
	}
	public String getAuthCode() {
		return authCode;
	}
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	@Override
	public Object getCredentials() {
		return null;
	}
}
