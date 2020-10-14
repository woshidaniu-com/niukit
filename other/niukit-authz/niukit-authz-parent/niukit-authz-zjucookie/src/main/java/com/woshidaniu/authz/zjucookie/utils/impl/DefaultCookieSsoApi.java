/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.zjucookie.utils.impl;

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

import com.woshidaniu.authz.zjucookie.utils.CookieSsoApi;

import validate.CookieValidate;
import validate.CookieValidating;

/**
 * 
 * @className	： DefaultCookieSsoApi
 * @description	：默认CookieSsoApi
 * @author 		：康康（1571）
 * @date		： 2018年5月10日 上午11:27:58
 * @version 	V1.0
 */
public class DefaultCookieSsoApi implements CookieSsoApi{

	private static Logger log = LoggerFactory.getLogger(DefaultCookieSsoApi.class);
	
	private String sessionURL;
	private String userURL;
	private String appUid;
	private String appPwd;
	
	@Override
	public boolean logout(String token) throws ServletException, IOException {
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
			if(log.isDebugEnabled()) {
				log.debug("Cookie SSO Logout Success !");				
			}
			return true;
		} catch (IOException e) {
			log.error("Cookie SSO Logout Error ." , e);
			return false;
		} finally {
			if (httpConn != null) {
				httpConn.disconnect();
			}
		}
	}

	@Override
	public String login(String username, String password,  HttpServletResponse response) {
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
			if(log.isDebugEnabled()) {
				log.debug("Cookie SSO Login Success by token["+ token +"]!");				
			}
		} catch (Exception e) {
			log.error("Cookie SSO Login Error ." , e);
		} finally {
			if (httpConn != null) {
				httpConn.disconnect();
			}
		}
		return token;
	}

	@Override
	public String getUidByTokenInCookie(HttpServletRequest request,HttpServletResponse response) throws IOException {
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
			uid = this.getUidByToken(token);
		}
		return uid;
	}

	@Override
	public String getUidByToken(String token) throws IOException {
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
			if(log.isDebugEnabled()) {
				log.debug("Get Uid ["+ uid +"] By Token ["+ token +"] Success!");				
			}
		} catch (IOException e) {
			log.error("Get Uid By Token Error ." , e);
		} finally {
			if (httpConn != null) {
				httpConn.disconnect();
			}
		}
		return uid;
	}

	@Override
	public String getNameByUid(String uid) {
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
			if(log.isDebugEnabled()) {
				log.debug("Get Name ["+ name +"] By Uid ["+ uid +"] Success!");				
			}
		} catch (IOException e) {
			log.error("Get Name By Uid Error ." , e);
		} finally {
			if (httpConn != null) {
				httpConn.disconnect();
			}
		}
		return name;
	}

	@Override
	public String getDepNoByUid(String uid) {
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
			if(log.isDebugEnabled()) {
				log.debug("Get DepNo ["+ depNo +"] By Uid ["+ uid +"] Success!");				
			}
		} catch (IOException e) {
			log.error("Get DepNo By Uid Error ." , e);
		} finally {
			if (httpConn != null) {
				httpConn.disconnect();
			}
		}
		return depNo;
	}

	public void setSessionURL(String sessionURL) {
		this.sessionURL = sessionURL;
	}

	public void setUserURL(String userURL) {
		this.userURL = userURL;
	}

	public void setAppUid(String appUid) {
		this.appUid = appUid;
	}

	public void setAppPwd(String appPwd) {
		this.appPwd = appPwd;
	}
	
	
}
