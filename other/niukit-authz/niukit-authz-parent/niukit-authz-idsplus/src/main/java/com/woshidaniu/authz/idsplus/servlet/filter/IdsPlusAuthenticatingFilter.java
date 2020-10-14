/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.idsplus.servlet.filter;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.validation.Assertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.authz.idsplus.shiro.token.IdsPlusAuthenticationToken;
import com.woshidaniu.authz.idsplus.utils.IdsPlusUtils;
import com.woshidaniu.basicutils.BlankUtils;
import com.woshidaniu.basicutils.StringUtils;
import com.woshidaniu.shiro.servlet.filter.AbstractAuthenticatingFilter;
import com.woshidaniu.shiro.token.LoginType;
import com.woshidaniu.web.Parameter;
import com.woshidaniu.web.Parameters;
import com.woshidaniu.web.utils.WebRequestUtils;
import com.woshidaniu.web.utils.WebUtils;

/**
 * 
 * @className	： IdsPlusAuthenticatingFilter
 * @description	： 整合南京苏迪单点认证登录
 * @author 		：大康（743）
 * @date		： 2017年8月24日 下午7:51:14
 * @version 	V1.0
 */
@Deprecated
public class IdsPlusAuthenticatingFilter extends AbstractAuthenticatingFilter {
 
	protected Logger LOG = LoggerFactory.getLogger(IdsPlusAuthenticatingFilter.class);

	@Override
	protected void onFilterConfigSet(FilterConfig filterConfig) throws Exception {
		super.onFilterConfigSet(filterConfig);
	}
	 
	@Override
	protected void onAccessDeniad(ServletRequest request, ServletResponse response) {
		
		try {
			
			HttpServletRequest httpRequest = WebUtils.toHttp(request);
			HttpServletResponse httpResponse = WebUtils.toHttp(response);
			HttpSession session = httpRequest.getSession();
			String url = httpRequest.getParameter("url");
			// 第一步：获取session中的账号信息
			String loginName = (String) session.getAttribute("cas.client.user");
			if (StringUtils.isEmpty(loginName)) {
				// 销毁应用本地会话等其它操作...
				String authServerUrl = httpRequest.getHeader("AUTH_SERVER_URL");
				String service = IdsPlusUtils.getCurrentPath(httpRequest, httpResponse);
				WebUtils.issueRedirect(httpRequest, httpResponse,
						authServerUrl + "/login?service=" + URLEncoder.encode(service, "UTF-8"), null, false);
			}
			LOG.info("Get loginName In Session ：loginName = " + loginName);
			
			Assertion assertion = (Assertion) session.getAttribute("_const_cas_assertion_");
			// session增加登录类型信息:单点登录，方便后面退出时做判断
			session.setAttribute(Parameters.getGlobalString(Parameter.LOGIN_TYPE_KEY), LoginType.SSO.getKey());
			String name = ""; // 当前已认证用户的姓名
			if (assertion != null) {
				AttributePrincipal principal = assertion.getPrincipal();
				name = principal.getName();// 获得认证中心传过来的认证名，一般为用户登录名
			}
			LOG.info("Current user id:" + loginName + " name:" + URLDecoder.decode(name, "UTF-8"));
			// 建立应用本地会话等其它操作...
			// 登录IP
			String host = WebRequestUtils.getRemoteAddr((HttpServletRequest) request);

			String existsLoginName = (String) session
					.getAttribute(Parameters.getGlobalString(Parameter.SESSION_SSO_USER_KEY));
			// 如果已经用户登录，并且登录的用户跟当前用户不一致，先把之前已经登录的用户退出
			if (!BlankUtils.isBlank(existsLoginName) && !BlankUtils.isBlank(loginName)
					&& !existsLoginName.equals(loginName)) {
				Subject subject = SecurityUtils.getSubject();
				subject.logout();
			}

			// 构造Token对象
			AuthenticationToken authToken = new IdsPlusAuthenticationToken(loginName, name, host);
			SecurityUtils.getSubject().login(authToken);
			// 登录成功,如果url不为空，说明是功能集成，则进入对应的url地址
			if (!BlankUtils.isBlank(url)) {
				org.apache.shiro.web.util.WebUtils.redirectToSavedRequest(request, response, url);
			} else {
				// 登录成功,如果url为空，说明是单点登录，则进入首页
				issueSuccessRedirect(request, response);
			}
			
		} catch (Exception e) {
			LOG.error(e.getMessage());
			// 登录失败,进入登录页
			try {
				redirectToLogin(request, response);
			} catch (IOException e1) {
			}
		}
		
	}
	
}
