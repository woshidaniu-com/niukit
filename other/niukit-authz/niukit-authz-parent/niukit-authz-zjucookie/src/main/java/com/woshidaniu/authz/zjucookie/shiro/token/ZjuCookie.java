/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.zjucookie.shiro.token;

/**
 * 
 * @className	： ZjuCookie
 * @description	： ZjuCookie认证登录的Cookie定义
 * @author 		：康康（1571）
 * @date		： 2018年5月10日 上午11:13:27
 * @version 	V1.0
 */
public class ZjuCookie {

	protected String uid; // Cookie中的账号信息
	protected String host; // 登陆IP

	public ZjuCookie(String uid, String host) {
		this.uid = uid;
		this.host = host;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

}