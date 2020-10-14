package com.woshidaniu.authz.uaac;

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
	public static final String REDIRECT_URL = "uaac.redirect_url";
	//配置文件的相对路径(科大讯飞)
	public static final String UAAC_CONFIG_PATH = "/WEB-INF/classes/uaac-client-rest.xml";
	//认证中心上下文(科大讯飞)
	public static final String UAAC_CAS_CONTEXT = "cas.context";
	//配置文件的相对路径(科大讯飞)
	public static final String AUTHENTICATION_PROPERTIES_NAME = "authentication.properties";
	
}
