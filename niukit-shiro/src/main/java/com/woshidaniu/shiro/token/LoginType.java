/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.shiro.token;

/**
 * 
 *@类名称		：LoginType.java
 *@类描述		：登录方式枚举；1：页面登录；2：单点登录；3：票据登录（通过握手秘钥等参数认证登录）
 *@创建人		：kangzhidong
 *@创建时间	：Sep 6, 2016 5:43:26 PM
 *@修改人		：
 *@修改时间	：
 *@版本号	:v2.0.0
 */
public enum LoginType {

	/**
	 * 1：页面登录
	 */
	INNER("1","页面登录"),
	/**
	 * 2：单点登录
	 */
	SSO("2","单点登录"),
	/**
	 * 3：票据登录（通过握手秘钥等参数认证登录）
	 */
	TICKIT("3","票据登录（通过握手秘钥等参数认证登录）");
	
	protected String key;
	protected String desc;
	
	LoginType(String key,String desc){
		this.key = key;
		this.desc = desc;
	}
	
	@Override
	public String toString() {
		return desc;
	}

	public String getKey() {
		return key;
	}

	public String getDesc() {
		return desc;
	}
	
}