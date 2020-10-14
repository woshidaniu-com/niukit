/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.zjucookie.shiro.filter;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.authz.zjucookie.shiro.token.ZjuCookie;
import com.woshidaniu.authz.zjucookie.shiro.token.ZjuCookieAuthcToken;
import com.woshidaniu.authz.zjucookie.utils.CookieSsoApi;
import com.woshidaniu.basicutils.StringUtils;
import com.woshidaniu.web.utils.WebRequestUtils;
import com.woshidaniu.web.utils.WebUtils;

/**
 * 
 * @className	： ZjuCookieLoginShiroFilter
 * @description	： ZjuCookie认证登录Shiro的Filter
 * @author 		：康康（1571）
 * @date		： 2018年5月10日 上午11:14:59
 * @version 	V1.0
 */
public class ZjuCookieAuthcShiroFilter extends AuthenticatingFilter{

	private static Logger log = LoggerFactory.getLogger(ZjuCookieAuthcShiroFilter.class);

	protected CookieSsoApi cookieSsoApi;

	@Override
	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
		
		String uid = this.cookieSsoApi.getUidByTokenInCookie(WebUtils.toHttp(request),WebUtils.toHttp(response));

		if (StringUtils.isNotEmpty(uid)) {
			// 登录IP
			String host = WebRequestUtils.getRemoteAddr((HttpServletRequest) request);
			// 构造Token对象
			AuthenticationToken authToken = new ZjuCookieAuthcToken(new ZjuCookie(uid, host));

			return authToken;
		}

		String yhm = request.getParameter("yhm");
		String mm = request.getParameter("mm");
		String token = this.cookieSsoApi.login(yhm, mm, WebUtils.toHttp(response));
		if (StringUtils.isNotEmpty(token)) {
			// 登录成功，从服务器获取uid
			uid = this.cookieSsoApi.getUidByToken(token);
			if (StringUtils.isNotEmpty(uid)) {
				log.info("Cookie SSO UID get Success.");
				// 登录IP
				String host = WebRequestUtils.getRemoteAddr((HttpServletRequest) request);
				// 构造Token对象
				AuthenticationToken authToken = new ZjuCookieAuthcToken(new ZjuCookie(uid, host));

				return authToken;
			}
		}
		return null;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		// 第一步：获取cookie中的账号信息
		String uid = this.cookieSsoApi.getUidByTokenInCookie(WebUtils.toHttp(request),WebUtils.toHttp(response));
		if (StringUtils.isNotEmpty(uid)) {
			return this.executeLogin(request, response);
		} else {
			// 第二步：如果没有Cookie登录信息则用参数登录
			String yhm = request.getParameter("yhm");
			String mm = request.getParameter("mm");
			// 验证用户名和密码是否已填
			if (StringUtils.isEmpty(yhm) || StringUtils.isEmpty(mm)) {				
				//没有uid,没有yhm,mm参数，重定向到登录页面
				this.redirectToLogin(request, response);
				return false;
			}else {				
				return this.executeLogin(request, response);
			}
		}
	}
	
	/**
	 * @description ： 登录成功后，直接重定向到成功页面，后续Filter不再处理
	 * @author ：康康（1571）
	 * @date ：2018年5月2日 上午10:18:36
	 * @param token
	 * @param subject
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
			ServletResponse response) throws Exception {
		if(log.isDebugEnabled()) {
			log.debug("zjucookie 认证登录失败，重定向到成功页面:{}",this.getSuccessUrl());
		}
		this.issueSuccessRedirect(request, response);
		return false;
	}
	
	/**
	 * @description ： 登录失败后，直接重定向到登录页面，后续Filter不再处理
	 * @author ：康康（1571）
	 * @date ：2018年5月2日 上午10:19:13
	 * @param token
	 * @param e
	 * @param request
	 * @param response
	 * @return
	 */
	@Override
	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request,
			ServletResponse response) {
		try {
			if(log.isDebugEnabled()) {
				log.debug("zjucookie 认证登录失败，重定向到登录页面:{}",this.getLoginUrl());
			}
			this.redirectToLogin(request, response);
		} catch (IOException e1) {
			log.error("登录失败时处理发生异常", e1);
		}
		return false;
	}

	public void setCookieSsoApi(CookieSsoApi cookieSsoApi) {
		this.cookieSsoApi = cookieSsoApi;
	}
}
