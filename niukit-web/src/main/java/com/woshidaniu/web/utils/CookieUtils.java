package com.woshidaniu.web.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basicutils.Assert;
import com.woshidaniu.web.core.cookie.CookieModel;


/**
 * 
 *@类名称	: CookieUtils.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 7, 2016 3:26:25 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public abstract class CookieUtils {

	/**
	 * Default path that cookies will be visible to: "/", i.e. the entire server.
	 */
	protected static final String DEFAULT_COOKIE_PATH = "/";

	protected static Logger LOG = LoggerFactory.getLogger(CookieUtils.class);

	/**
	 * Retrieve the first cookie with the given name. Note that multiple
	 * cookies can have the same name but different paths or domains.
	 * @param request current servlet request
	 * @param cookieName cookie name
	 * @return the first cookie with the given name, or {@code null} if none is found
	 */
	public static Cookie getCookie(HttpServletRequest request, String cookieName) {
		Assert.notNull(request, "Request must not be null");
		Cookie cookies[] = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookieName.equals(cookie.getName())) {
					return cookie;
				}
			}
		}
		return null;
	}
	
	/**
	 * 
	 * ******************************************************************
	 * @description	：Add a cookie with the given value to the response, using the cookie descriptor settings of this generator.
	 * <p>Delegates to {@link #createCookie} for cookie creation.
	 * @author 		： kangzhidong
	 * @date 		：Feb 16, 2016 10:01:31 AM
	 * @param response the HTTP response to add the cookie to
	 * @param cookieName
	 * @param cookieDomain
	 * @param cookiePath
	 * @param cookieValue the value of the cookie to add
	 * @param cookieMaxAge
	 * @param cookieSecure
	 * @param cookieHttpOnly
	 * @see #setCookieName
	 * @see #setCookieDomain
	 * @see #setCookiePath
	 * @see #setCookieMaxAge
	 * ******************************************************************
	 */
	public static void addCookie(HttpServletResponse response, String cookieName,String cookieDomain,String cookiePath,String cookieValue,
			Integer cookieMaxAge,boolean cookieSecure,boolean cookieHttpOnly) {
		Assert.notNull(response, "HttpServletResponse must not be null");
		Cookie cookie = createCookie(cookieName, cookieDomain, cookiePath, cookieValue);
		if (cookieMaxAge != null) {
			cookie.setMaxAge(cookieMaxAge);
		}
		if (cookieSecure) {
			cookie.setSecure(true);
		}
		if (cookieHttpOnly) {
			cookie.setHttpOnly(true);
		}
		response.addCookie(cookie);
		if (LOG.isDebugEnabled()) {
			LOG.debug("Added cookie with name [" + cookieName + "] and value [" + cookieValue + "]");
		}
	}
	
	public static void addCookie(HttpServletResponse response, CookieModel cookieModel) {
		Assert.notNull(response, "HttpServletResponse must not be null");
		Cookie cookie = createCookie(cookieModel.getCookieName(),cookieModel.getCookieDomain(),cookieModel.getCookiePath(),cookieModel.getCookieValue());
		Integer maxAge = cookieModel.getCookieMaxAge();
		if (maxAge != null) {
			cookie.setMaxAge(maxAge);
		}
		if (cookieModel.isCookieSecure()) {
			cookie.setSecure(true);
		}
		if (cookieModel.isCookieHttpOnly()) {
			cookie.setHttpOnly(true);
		}
		response.addCookie(cookie);
		if (LOG.isDebugEnabled()) {
			LOG.debug("Added cookie with name [" + cookieModel.getCookieName() + "] and value [" + cookieModel.getCookieValue() + "]");
		}
	}
	
	/**
	 * Remove the cookie that this generator describes from the response.
	 * Will generate a cookie with empty value and max age 0.
	 * <p>Delegates to {@link #createCookie} for cookie creation.
	 * @param response the HTTP response to remove the cookie from
	 * @see #setCookieName
	 * @see #setCookieDomain
	 * @see #setCookiePath
	 */
	public static void removeCookie(HttpServletResponse response,String cookieName) {
		Assert.notNull(response, "HttpServletResponse must not be null");
		Cookie cookie = createCookie(cookieName,"","","");
		cookie.setMaxAge(0);
		response.addCookie(cookie);
		if (LOG.isDebugEnabled()) {
			LOG.debug("Removed cookie with name [" + cookieName + "]");
		}
	}

	/**
	 * 
	 * ******************************************************************
	 * @description	： Create a cookie with the given value, using the cookie descriptor settings of this generator (except for "cookieMaxAge").
	 * @author 		： kangzhidong
	 * @date 		：Feb 16, 2016 9:56:06 AM
	 * @param cookieName
	 * @param cookieDomain
	 * @param cookiePath
	 * @param cookieValue the value of the cookie to crate
	 * @return the cookie
	 * ******************************************************************
	 */
	public static Cookie createCookie(String cookieName,String cookieDomain,String cookiePath,String cookieValue) {
		Cookie cookie = new Cookie(cookieName, cookieValue);
		if (cookieDomain != null) {
			cookie.setDomain(cookieDomain);
		}
		cookie.setPath(cookiePath);
		return cookie;
	}
	
	public static Cookie createCookie(CookieModel cookieModel) {
		Cookie cookie = new Cookie(cookieModel.getCookieName(), cookieModel.getCookieValue());
		if (cookieModel.getCookieDomain() != null) {
			cookie.setDomain(cookieModel.getCookieDomain());
		}
		cookie.setPath(cookieModel.getCookiePath());
		return cookie;
	}

}
