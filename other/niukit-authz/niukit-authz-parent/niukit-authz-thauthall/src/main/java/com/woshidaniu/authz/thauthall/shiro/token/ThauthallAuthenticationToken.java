/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.thauthall.shiro.token;

import org.apache.shiro.authc.HostAuthenticationToken;

import com.woshidaniu.shiro.token.DelegateAuthenticationToken;

@SuppressWarnings("serial")
public class ThauthallAuthenticationToken implements DelegateAuthenticationToken, HostAuthenticationToken {
	// 登录状态
	protected int code;
	// 单位
	protected String dw;
	// 邮箱
	protected String email;
	// 密码
	protected String mm;
	// 秘钥
	protected String skey;
	// 票据
	protected String ticket;
	// 姓名
	protected String xm;
	// 用户类别
	protected String yhlb;
	// 用户名
	protected String yhm;
	// 用户状态
	protected String yhzt;
	// 职工号
	protected String zjh;
	// 登陆IP
	protected String host;

	public ThauthallAuthenticationToken() {
	}

	public ThauthallAuthenticationToken(int code, String dw, String email, String mm, String skey, String ticket,
			String xm, String yhlb, String yhm, String yhzt, String zjh, String host) {
		this.code = code;
		this.dw = dw;
		this.email = email;
		this.mm = mm;
		this.skey = skey;
		this.ticket = ticket;
		this.xm = xm;
		this.yhlb = yhlb;
		this.yhm = yhm;
		this.yhzt = yhzt;
		this.zjh = zjh;
		this.host = host;
	}

	@Override
	public Object getPrincipal() {
		return yhm;
	}

	@Override
	public Object getCredentials() {
		return yhm;
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
		return yhm;
	}

	@Override
	public String getUsertype() {
		return yhlb;
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

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getDw() {
		return dw;
	}

	public void setDw(String dw) {
		this.dw = dw;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMm() {
		return mm;
	}

	public void setMm(String mm) {
		this.mm = mm;
	}

	public String getSkey() {
		return skey;
	}

	public void setSkey(String skey) {
		this.skey = skey;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public String getXm() {
		return xm;
	}

	public void setXm(String xm) {
		this.xm = xm;
	}

	public String getYhlb() {
		return yhlb;
	}

	public void setYhlb(String yhlb) {
		this.yhlb = yhlb;
	}

	public String getYhm() {
		return yhm;
	}

	public void setYhm(String yhm) {
		this.yhm = yhm;
	}

	public String getYhzt() {
		return yhzt;
	}

	public void setYhzt(String yhzt) {
		this.yhzt = yhzt;
	}

	public String getZjh() {
		return zjh;
	}

	public void setZjh(String zjh) {
		this.zjh = zjh;
	}

}
