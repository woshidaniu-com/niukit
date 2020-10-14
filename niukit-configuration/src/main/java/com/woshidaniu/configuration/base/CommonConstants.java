package com.woshidaniu.configuration.base;

/**
 * 
 *@类名称	: CommonConstants.java
 *@类描述	：系统常量
 *@创建人	：kangzhidong
 *@创建时间	：Mar 7, 2016 4:55:21 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public abstract class CommonConstants extends JavaConstants {

	/**
	 * 当前登录角色sessionKey：System.getProperty("system.login.user", "user")
	 */
	public static final String USER_KEY = System.getProperty("system.login.user", "user");
	
}
