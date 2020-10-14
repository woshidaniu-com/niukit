/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.ftpclient;


/**
 *@类名称	: FTPConstants.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Dec 5, 2016 9:54:07 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public final class FTPConstants {
	
	//获取文件存储路径
	public static String DEFAULT_FTPCLIEN_WEB_LOCALDIR = "tmpdir";
	//获取请求过滤前缀
	public static String DEFAULT_FTPCLIEN_WEB_REQUESTPREFIX = "/ftp/";
	//获取是否缓存FTP文件到本地存储路径
	public static boolean DEFAULT_FTPCLIEN_WEB_CACHELOCAL = false;
	//获取共享文件在本地缓存的时间;默认10分钟
	public static long DEFAULT_FTPCLIEN_WEB_CACHEEXPIRY = 10 * 60 * 1000;
	
	//获取文件存储路径
	public static String FTPCLIEN_WEB_LOCALDIR_KEY = "ftpclient.web.tmpdir";
	//获取请求过滤前缀
	public static String FTPCLIEN_WEB_REQUESTPREFIX_KEY = "ftpclient.web.requestPrefix";
	//获取是否缓存FTP文件到本地存储路径
	public static String FTPCLIEN_WEB_CACHELOCAL_KEY = "ftpclient.web.cacheLocal";
	//获取共享文件在本地缓存的时间;默认10分钟
	public static String FTPCLIEN_WEB_CACHEEXPIRY_KEY = "ftpclient.web.cacheExpiry";
	//异常信息重定向路径
	public static String FTPCLIEN_WEB_REDIRECTURL_KEY = "ftpclient.web.redirectURL";
	
}
