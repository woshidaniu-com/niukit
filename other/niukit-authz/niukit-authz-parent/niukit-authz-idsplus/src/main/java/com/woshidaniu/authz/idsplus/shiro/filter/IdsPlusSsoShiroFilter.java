/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.idsplus.shiro.filter;


import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.validation.Assertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.authz.idsplus.shiro.token.IdsPlusAuthenticationToken;
import com.woshidaniu.authz.idsplus.utils.IdsPlusUtils;
import com.woshidaniu.basicutils.BlankUtils;
import com.woshidaniu.basicutils.StringUtils;
import com.woshidaniu.shiro.token.LoginType;
import com.woshidaniu.web.Parameter;
import com.woshidaniu.web.Parameters;
import com.woshidaniu.web.utils.WebRequestUtils;

/**
 * 
 * @className	： IdsPlusLoginShiroFilter
 * @description	：  浙江越秀外国语学院单点(南京苏迪科技单点认证)，注销登录拦截器 
 * @author 		：康康（1571）
 * @date		： 2018年5月11日 上午9:50:27
 * @version 	V1.0
 */
public class IdsPlusSsoShiroFilter extends AuthenticatingFilter {

	private static final Logger log = LoggerFactory.getLogger(IdsPlusSsoShiroFilter.class);
	
	//登录成功后是否重定向到之前保存的请求地址
	private boolean redirectSavedRequestAfterLoginSuccess = false;
	
	private String authServerUrl;
	@Override
	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletRequest httpRequest = WebUtils.toHttp(request);
		HttpServletResponse httpResponse = WebUtils.toHttp(response);
		HttpSession session = httpRequest.getSession();
		// 第一步：获取session中的账号信息
		String loginName = (String) session.getAttribute("cas.client.user");
		Assertion assertion = (Assertion) session.getAttribute("_const_cas_assertion_");
		// session增加登录类型信息:单点登录，方便后面退出时做判断
		session.setAttribute(Parameters.getGlobalString(Parameter.LOGIN_TYPE_KEY), LoginType.SSO.getKey());
		AttributePrincipal principal = assertion.getPrincipal();
		String name = principal.getName();// 获得认证中心传过来的认证名，一般为用户登录名
		if(log.isDebugEnabled()) {
			log.debug("Current user id:" + loginName + " name:" + URLDecoder.decode(name, "UTF-8"));			
		}
		// 建立应用本地会话等其它操作...
		// 登录IP
		String host = WebRequestUtils.getRemoteAddr((HttpServletRequest) request);

		String existsLoginName = (String) session
				.getAttribute(Parameters.getGlobalString(Parameter.SESSION_SSO_USER_KEY));
		// 如果已经用户登录，并且登录的用户跟当前用户不一致，先把之前已经登录的用户退出
		if (!BlankUtils.isBlank(existsLoginName) && !BlankUtils.isBlank(loginName)
				&& !existsLoginName.equals(loginName)) {
			Subject subject = getSubject(httpRequest, httpResponse);
			subject.logout();
		}
		
		// 构造Token对象
		AuthenticationToken authToken = new IdsPlusAuthenticationToken(loginName, name, host);
		return authToken;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletRequest httpRequest = WebUtils.toHttp(request);
		HttpServletResponse httpResponse = WebUtils.toHttp(response);
		HttpSession session = httpRequest.getSession();
		String loginName = (String) session.getAttribute("cas.client.user");
		Assertion assertion = (Assertion) session.getAttribute("_const_cas_assertion_");
		if (StringUtils.isEmpty(loginName) || assertion == null) {
			// 销毁应用本地会话等其它操作...
			String authServerUrl = httpRequest.getHeader("AUTH_SERVER_URL");
			if(StringUtils.isEmpty(authServerUrl)) {
				authServerUrl = this.authServerUrl;
			}
			String service = IdsPlusUtils.getCurrentPath(httpRequest, httpResponse);
			WebUtils.issueRedirect(httpRequest, httpResponse,authServerUrl + "/login?service=" + URLEncoder.encode(service, "UTF-8"), null, false);
		}else {
			this.executeLogin(request, response);
		}
		return false;
	}

	/**
	 * 
	 * @description	： 登录成功后
	 * @author 		：康康（1571）
	 * @date 		：2018年5月11日 上午9:17:09
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
		HttpServletRequest httpRequest = WebUtils.toHttp(request);
		String url = httpRequest.getParameter("url");
		// 登录成功,如果url不为空，说明是功能集成，则进入对应的url地址
		if (!BlankUtils.isBlank(url)) {
			org.apache.shiro.web.util.WebUtils.redirectToSavedRequest(request, response, url);
		} else {
			// 登录成功,如果url为空，说明是单点登录，则进入首页
			if(this.redirectSavedRequestAfterLoginSuccess) {//重定向到saved request
				WebUtils.redirectToSavedRequest(request, response, getSuccessUrl());
			}else {//重定向到真正的首页
				WebUtils.issueRedirect(request, response, getSuccessUrl());
			}
		}
		return false;
	}

	/**
	 * @description	： 登录失败后
	 * @author 		：康康（1571）
	 * @date 		：2018年5月11日 上午9:17:25
	 * @param token
	 * @param e
	 * @param request
	 * @param response
	 * @return
	 */
	@Override
	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request,
			ServletResponse response) {
		HttpServletRequest httpRequest = WebUtils.toHttp(request);
		HttpServletResponse httpResponse = WebUtils.toHttp(response);
		try {
			String service = IdsPlusUtils.getCurrentPath(httpRequest, httpResponse);
			WebUtils.issueRedirect(httpRequest, httpResponse,authServerUrl + "/login?service=" + URLEncoder.encode(service, "UTF-8"), null, false);
			redirectToLogin(request, response);
		} catch (IOException e1) {
			log.error("尝试登录失败，重定向到登录界面异常",e1);
		}
		return false;
	}

	public void setAuthServerUrl(String authServerUrl) {
		this.authServerUrl = authServerUrl;
	}

	
}
