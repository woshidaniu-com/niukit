/**
 * 
 */
package com.woshidaniu.shiro.token;

import org.apache.shiro.authc.HostAuthenticationToken;
import org.apache.shiro.authc.RememberMeAuthenticationToken;

/**
 * @desc 框架默认的认证token
 *
 */
public class DefaultAuthenticationToken implements DelegateAuthenticationToken, HostAuthenticationToken, RememberMeAuthenticationToken, CaptchaAuthenticationToken{

	private static final long serialVersionUID = -1060600669548366260L;

	protected String username; //用户名
	
	protected String usertype;	//用户类型
	
	protected char[] password;	//用户密码
	
	protected int strength;	//密码强度
	
	protected String captcha; //验证码
	
	protected String host;	//登陆IP
	
	protected boolean rememberMe = false;//是否记住我
	
	public DefaultAuthenticationToken() {
	}
	
	public DefaultAuthenticationToken(String username, String password) {
		this(username, password != null ? password.toCharArray() : null);
	}
	
	public DefaultAuthenticationToken(String username, char[] password) {
		this.username = username;
		this.password = password;
	}
	
	public DefaultAuthenticationToken(String username, String password, String captcha) {
		this(username, password != null ? password.toCharArray() : null, captcha);
	}
	
	public DefaultAuthenticationToken(String username, char[] password, String captcha) {
		this.username = username;
		this.captcha = captcha;
		this.password = password;
	}
	
	public DefaultAuthenticationToken(String username, char[] password, String usertype, String captcha) {
		this.username = username;
		this.usertype = usertype;
		this.password = password;
		this.captcha = captcha;
	}

	public DefaultAuthenticationToken(String username, String password, String usertype, String captcha) {
		this(username, password != null ? password.toCharArray() : null, usertype, captcha);
	}
	
	public DefaultAuthenticationToken(String username, char[] password, String usertype, boolean rememberMe) {
		this.username = username;
		this.usertype = usertype;
		this.password = password;
		this.rememberMe = rememberMe;
	}

	public DefaultAuthenticationToken(String username, String password, String usertype, boolean rememberMe) {
		this(username, password != null ? password.toCharArray() : null, usertype, rememberMe);
	}
	
	public DefaultAuthenticationToken(String username, char[] password, String usertype, String host,
			boolean rememberMe) {
		this.username = username;
		this.usertype = usertype;
		this.password = password;
		this.host = host;
		this.rememberMe = rememberMe;
	}
	
	public DefaultAuthenticationToken(String username, String password, String usertype, String host,
			boolean rememberMe) {
		this(username, password != null ? password.toCharArray() : null, usertype, host, rememberMe);
	}
	

	public DefaultAuthenticationToken(String username, char[] password, String usertype, String captcha, String host,
			boolean rememberMe) {
		this.username = username;
		this.password = password;
		this.usertype = usertype;
		this.captcha = captcha;
		this.host = host;
		this.rememberMe = rememberMe;
	}
	
	public DefaultAuthenticationToken(String username, String password, String usertype, String captcha, String host,
			boolean rememberMe) {
		this(username, password != null ? password.toCharArray() : null, usertype, captcha, host, rememberMe);
		this.rememberMe = rememberMe;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsertype() {
		return usertype;
	}

	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}

	public char[] getPassword() {
		return password;
	}

	public void setPassword(char[] password) {
		this.password = password;
	}
	
	public int getStrength() {
		return strength;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setRememberMe(boolean rememberMe) {
		this.rememberMe = rememberMe;
	}

	@Override
	public Object getPrincipal() {
		return getUsername();
	}

	@Override
	public Object getCredentials() {
		return getPassword();
	}

	@Override
	public boolean isRememberMe() {
		return this.rememberMe;
	}

	@Override
	public String getHost() {
		return this.host;
	}

	@Override
	public String toString() {
		return "DefaultAuthenticationToken [username=" + username + ", usertype=" + usertype + ", host=" + host
				+ ", rememberMe=" + rememberMe + "]";
	}
}
