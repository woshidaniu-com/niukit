package com.woshidaniu.authz.idsplus;

/**
 * 
 * @className	： AuthcConstant
 * @description	：认证相关常量
 * @author 		：大康（743）
 * @date		： 2018年3月13日 上午11:18:09
 * @version 	V1.0
 */
public class AuthcConstant {
	
	//系统单独例登录口的重定向地址
	public static final String REDIRECT_URL = "idsplus.redirect_url";
	//配置文件的相对路径(南京苏迪)
	public static final String IDSPLUS_CAS_CONFIG_PATH = "/WEB-INF/classes/casClientConfig.xml";
	//认证中心退出地址(南京苏迪)
	public static final String IDSPLUS_CAS_SERVER_LOGOUT_URL = "casServerLogoutUrl";
	//教务系统地址(南京苏迪)
	public static final String IDSPLUS_SERVER_NAME = "serverName";	
		
}
