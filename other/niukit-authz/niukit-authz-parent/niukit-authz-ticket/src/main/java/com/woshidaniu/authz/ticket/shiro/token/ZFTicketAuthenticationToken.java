/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.ticket.shiro.token;

import org.apache.shiro.authc.HostAuthenticationToken;

import com.woshidaniu.shiro.token.DelegateAuthenticationToken;

/**
 * 
 * @className	： ZFTicketAuthenticationToken
 * @description	： ticket认证登录shrio token
 * @author 		：康康（1571）
 * @date		： 2018年5月2日 下午2:14:26
 * @version 	V1.0
 */
@SuppressWarnings("serial")
public class ZFTicketAuthenticationToken implements DelegateAuthenticationToken, HostAuthenticationToken {

	// 用户ID
	protected String userid;
	// 角色ID，xs,js：方便区别用户角色
	protected String roleid;
	// 学校代码
	protected String xxdm;
	// 系统双方约定的秘钥:基于Des + Base64加密的值
	protected String token;
	// 32位MD5加密信息（大写）:格式为：(卡号-用户类型-时间戳-token)值组合的MD5值
	protected String verify;
	// 时间戳;格式: yyyyMMddHHmmssSSS
	protected String timestamp;
	// 登陆IP
	protected String host;

	public ZFTicketAuthenticationToken() {
	}
	
	public ZFTicketAuthenticationToken(String userid, String roleid, String xxdm, String token, String verify,
			String timestamp, String host) {
		this.userid = userid;
		this.roleid = roleid;
		this.xxdm = xxdm;
		this.token = token;
		this.verify = verify;
		this.timestamp = timestamp;
		this.host = host;
	}

	@Override
	public Object getPrincipal() {
		return userid;
	}

	@Override
	public Object getCredentials() {
		return userid;
	}

	@Override
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getToken() {
		return token;
	}

	@Override
	public String getUsername() {
		return userid;
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

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getRoleid() {
		return roleid;
	}

	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}

	public String getXxdm() {
		return xxdm;
	}

	public void setXxdm(String xxdm) {
		this.xxdm = xxdm;
	}

	public String getVerify() {
		return verify;
	}

	public void setVerify(String verify) {
		this.verify = verify;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
