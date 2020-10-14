/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.idsplus.utils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.util.WebUtils;

import com.woshidaniu.shiro.token.LoginType;
import com.woshidaniu.web.Parameter;
import com.woshidaniu.web.Parameters;


public class IdsPlusUtils {

	public static boolean isCasLogin(ServletRequest request) {
		HttpServletRequest httpRequest = WebUtils.toHttp(request);
		// 登录成功;记录登录方式标记；1：页面登录；2：单点登录；3：票据登录（通过握手秘钥等参数认证登录）
		String loginType = String.valueOf(httpRequest.getSession().getAttribute(Parameters.getGlobalString(Parameter.LOGIN_TYPE_KEY)));
		return (LoginType.SSO.getKey()).equals(loginType);
	}
	
	// 获取当前访问资源的URL
	public static String getCurrentPath(HttpServletRequest request, HttpServletResponse response) {
		// 可以从HTTP头中获取到当前访问资源的URI（不包含参数）
		String requestUrl = request.getHeader("X-REQUEST-URL");
		String queryStr = request.getQueryString();
		if (queryStr != null) {
			requestUrl = requestUrl + "?" + queryStr;
		}
		return requestUrl;
	}
	
}
