package com.woshidaniu.authz.zjucookie.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import validate.CookieValidate;
import validate.CookieValidating;

/**
 * 
 * @className	： CookieSsoUtils
 * @description	： CookieSso工具
 * @author 		：康康（1571）
 * @date		： 2018年5月10日 上午11:24:48
 * @version 	V1.0
 */
@Deprecated
public class CookieSsoUtils {

	protected static Logger LOG = LoggerFactory.getLogger(CookieSsoUtils.class);
	protected static String TOKEN_KEY = "iPlanetDirectoryPro";
	
	public static void logout(String sessionURL,String appUid,String appPwd, String token) throws ServletException, IOException {
		CookieValidate cv = new CookieValidating();
		cv.deleteValidateData(token);
		HttpURLConnection httpConn = null;
		try {
			URL url = new URL(sessionURL);
			URLConnection conn = url.openConnection();
			httpConn = (HttpURLConnection) conn;
			httpConn.setRequestMethod("DELETE");
			httpConn.addRequestProperty(TOKEN_KEY, token);
			httpConn.addRequestProperty("appUid", appUid);
			httpConn.addRequestProperty("appPwd", appPwd);
			httpConn.connect();
			LOG.info("Cookie SSO Logout Success !");
		} catch (IOException e) {
			LOG.error("Cookie SSO Logout Error ." , e);
		} finally {
			if (httpConn != null) {
				httpConn.disconnect();
			}
		}
	}

	public static String login(String sessionURL,String appUid,String appPwd, String username, String password,  HttpServletResponse response) {
		HttpURLConnection httpConn = null;
		String token = null;
		try {
			URL url = new URL(sessionURL);
			URLConnection conn = url.openConnection();
			httpConn = (HttpURLConnection) conn;
			httpConn.setRequestMethod("POST");
			httpConn.addRequestProperty("name", username);
			httpConn.addRequestProperty("password", password);
			httpConn.addRequestProperty("module", "DataStore");
			httpConn.addRequestProperty("type", "1");
			httpConn.addRequestProperty("appUid", appUid);
			httpConn.addRequestProperty("appPwd", appPwd);
			httpConn.setDoOutput(true);
			httpConn.connect();
			token = httpConn.getHeaderField(TOKEN_KEY);
			if ((token == null) || ("".equals(token.trim()))) {
				return null;
			}
			token = URLEncoder.encode(token, "UTF-8");
			Cookie sso_cookie = new Cookie(TOKEN_KEY, token);
			sso_cookie.setPath("/");
			sso_cookie.setDomain(".zju.edu.cn");
			response.addCookie(sso_cookie);
			
			LOG.info("Cookie SSO Login Success !");
			LOG.info("token:" + token);
		} catch (Exception e) {
			LOG.error("Cookie SSO Login Error ." , e);
		} finally {
			if (httpConn != null) {
				httpConn.disconnect();
			}
		}
		return token;
	}

	public static String getUidByTokenInCookie(String sessionURL,String appUid,String appPwd, HttpServletRequest request,HttpServletResponse response) throws IOException {
		String uid = null;
		String token = null;
		Cookie[] cs = request.getCookies();
		if (cs == null) {
			return null;
		}
		for (int i = 0; i < cs.length; i++) {
			if (TOKEN_KEY.equals(cs[i].getName())) {
				token = cs[i].getValue();
				break;
			}
		}
		if ((token == null) || ("".equals(token.trim()))) {
			return null;
		}
		CookieValidate cv = new CookieValidating();
		String status = cv.validate(request, response, token);
		if ((status == null) || ("".equals(status.trim()))) {
			return null;
		}
		if ("1".equalsIgnoreCase(status.trim())) {
			uid = getUidByToken(sessionURL, appUid, appPwd,token);
		}
		return uid;
	}

	public static String getUidByToken(String sessionURL,String appUid,String appPwd,String token) throws IOException {
		String uid = null;
		if ((token == null) || ("".equals(token.trim()))) {
			return null;
		}
		HttpURLConnection httpConn = null;
		try {
			URL url = new URL(sessionURL);
			URLConnection conn = url.openConnection();
			httpConn = (HttpURLConnection) conn;
			httpConn.setRequestMethod("GET");
			httpConn.addRequestProperty(TOKEN_KEY, token);
			httpConn.addRequestProperty("appUid", appUid);
			httpConn.addRequestProperty("appPwd", appPwd);
			httpConn.connect();
			uid = httpConn.getHeaderField("ZJU_SSO_UID");
			LOG.info("Get Uid By Token Success !");
			LOG.info("uid:" + uid);
		} catch (IOException e) {
			LOG.error("Get Uid By Token Error ." , e);
		} finally {
			if (httpConn != null) {
				httpConn.disconnect();
			}
		}
		return uid;
	}

	public static String getNameByUid(String userURL,String appUid,String appPwd,String uid) {
		HttpURLConnection httpConn = null;
		String name = null;
		try {
			URL url = new URL(userURL);
			URLConnection conn = url.openConnection();
			httpConn = (HttpURLConnection) conn;
			httpConn.setRequestMethod("GET");
			httpConn.addRequestProperty("id", uid);
			httpConn.addRequestProperty("type", "1");
			httpConn.addRequestProperty("appUid", appUid);
			httpConn.addRequestProperty("appPwd", appPwd);
			httpConn.setDoOutput(true);
			httpConn.connect();
			name = httpConn.getHeaderField("name");
			String uids = httpConn.getHeaderField("ZJU_SSO_UIDS");
			if (uids != null)"".equals(uids.trim());

			if ((name != null) && (!"".equals(name.trim()))){
				name = new String(name.trim().getBytes("ISO8859-1"), "gbk");
			}
			LOG.info("Get Name By Uid Success !");
			LOG.info("name:" + name);
		} catch (IOException e) {
			LOG.error("Get Name By Uid Error ." , e);
		} finally {
			if (httpConn != null) {
				httpConn.disconnect();
			}
		}
		return name;
	}

	public static String getDepNoByUid(String userURL,String appUid,String appPwd,String uid) {
		HttpURLConnection httpConn = null;
		String depNo = null;
		try {
			URL url = new URL(userURL);
			URLConnection conn = url.openConnection();
			httpConn = (HttpURLConnection) conn;
			httpConn.setRequestMethod("GET");
			httpConn.addRequestProperty("id", uid);
			httpConn.addRequestProperty("type", "1");
			httpConn.addRequestProperty("appUid", appUid);
			httpConn.addRequestProperty("appPwd", appPwd);
			httpConn.setDoOutput(true);
			httpConn.connect();
			depNo = httpConn.getHeaderField("depNo");
			if ((depNo != null) && (!"".equals(depNo.trim()))){
				depNo = new String(depNo.trim().getBytes("ISO8859-1"), "gbk");
			}
			LOG.info("Get DepNo By Uid Success !");
			LOG.info("depNo:" + depNo);
		} catch (IOException e) {
			LOG.error("Get DepNo By Uid Error ." , e);
		} finally {
			if (httpConn != null) {
				httpConn.disconnect();
			}
		}
		return depNo;
	}
	
}
