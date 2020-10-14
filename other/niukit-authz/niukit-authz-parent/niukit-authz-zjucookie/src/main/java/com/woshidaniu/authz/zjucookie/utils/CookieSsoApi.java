/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.zjucookie.utils;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @className	： CookieSsoApi
 * @description	： CookieSsoApi
 * @author 		：康康（1571）
 * @date		： 2018年5月10日 上午11:25:16
 * @version 	V1.0
 */
public interface CookieSsoApi {

	static String TOKEN_KEY = "iPlanetDirectoryPro";
	/**
	 * 
	 * @description	：注销
	 * @author 		： 康康（1571）
	 * @date 		：2018年5月10日 上午11:25:27
	 * @param sessionURL
	 * @param appUid
	 * @param appPwd
	 * @param token
	 * @throws ServletException
	 * @throws IOException
	 */
	boolean logout(String token) throws ServletException, IOException;

	/**
	 * 
	 * @description	： 登录
	 * @author 		： 康康（1571）
	 * @date 		：2018年5月10日 上午11:25:40
	 * @param sessionURL
	 * @param appUid
	 * @param appPwd
	 * @param username
	 * @param password
	 * @param response
	 * @return
	 */
	String login(String username, String password,
			HttpServletResponse response);

	/**
	 * 
	 * @description	：获取token
	 * @author 		： 康康（1571）
	 * @date 		：2018年5月10日 上午11:25:50
	 * @param sessionURL
	 * @param appUid
	 * @param appPwd
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	String getUidByTokenInCookie(HttpServletRequest request,
			HttpServletResponse response) throws IOException;

	/**
	 * 
	 * @description	：获取token
	 * @author 		： 康康（1571）
	 * @date 		：2018年5月10日 上午11:26:07
	 * @param sessionURL
	 * @param appUid
	 * @param appPwd
	 * @param token
	 * @return
	 * @throws IOException
	 */
	String getUidByToken(String token) throws IOException;

	/**
	 * 
	 * @description	： 获取用户名
	 * @author 		： 康康（1571）
	 * @date 		：2018年5月10日 上午11:26:00
	 * @param userURL
	 * @param appUid
	 * @param appPwd
	 * @param uid
	 * @return
	 */
	String getNameByUid(String uid);

	/**
	 * 
	 * @description	： 获取部门
	 * @author 		： 康康（1571）
	 * @date 		：2018年5月10日 上午11:26:03
	 * @param userURL
	 * @param appUid
	 * @param appPwd
	 * @param uid
	 * @return
	 */
	String getDepNoByUid(String uid);

}