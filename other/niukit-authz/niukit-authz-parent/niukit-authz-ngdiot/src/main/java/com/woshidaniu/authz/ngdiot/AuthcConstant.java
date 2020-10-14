package com.zfost.authz.ngdiot;

/**
 * 
 * @className	： AuthcConstant
 * @description	：认证相关常量
 * @author 		：大康（743）
 * @date		： 2018年3月13日 上午11:18:09
 * @version 	V1.0
 */
public class AuthcConstant {
	
	//状态:否
	public static final String STATUS_NO = "0";	
	//状态:是
	public static final String STATUS_YES = "1";	
	//系统单独例登录口的重定向地址
	public static final String REDIRECT_URL = "/xtgl/login_slogin.html";
	//配置文件的相对路径(南京苏迪)
	public static final String IDSPLUS_CAS_CONFIG_PATH = "/WEB-INF/classes/casClientConfig.xml";
	//认证中心退出地址(南京苏迪)
	public static final String IDSPLUS_CAS_SERVER_LOGOUT_URL = "casServerLogoutUrl";
	//教务系统地址(南京苏迪)
	public static final String IDSPLUS_SERVER_NAME = "serverName";
	
	//配置文件的相对路径(科大讯飞)
	public static final String UAAC_CONFIG_PATH = "/WEB-INF/classes/uaac-client-rest.xml";
	//认证中心上下文(科大讯飞)
	public static final String UAAC_CAS_CONTEXT = "cas.context";
	
	//配置文件的相对路径(科大讯飞)
	public static final String AUTHENTICATION_PROPERTIES_NAME = "authentication.properties";
	//配置文件的相对路径(科探)
	public static final String CASLOGOUT_PROPERTIES_NAME = "casLogout.properties";
	//认证密钥
	public static final String AUTHENTICATION_KEY = "authentication.key";
	//登出地址(南工大--科探)
	public static final String CASLOGOUT_KEY = "casLogout.addr";
	//是否判断超时:0-否,1-是
	public static final String AUTHENTICATION_TIMEOUT = "authentication.timeout";
	//是否判断超时:如果要判断超时，超时分钟数
	public static final String AUTHENTICATION_TIMEOUT_MINUTES = "authentication.timeout.minutes";	
	
}
