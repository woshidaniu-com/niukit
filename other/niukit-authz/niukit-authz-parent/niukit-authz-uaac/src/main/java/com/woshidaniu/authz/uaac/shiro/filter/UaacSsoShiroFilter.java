/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.uaac.shiro.filter;

import java.io.IOException;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iflytek.uaac.client.rest.context.UaacServiceContext;
import com.iflytek.uaac.common.domain.UserInfo;
import com.woshidaniu.authz.uaac.shiro.token.UaacAuthenticationToken;
import com.woshidaniu.authz.uaac.utils.UaacSsoUtils;
import com.woshidaniu.basicutils.BlankUtils;
import com.woshidaniu.basicutils.StringUtils;
import com.woshidaniu.shiro.token.LoginType;
import com.woshidaniu.web.Parameter;
import com.woshidaniu.web.Parameters;
import com.woshidaniu.web.utils.WebRequestUtils;
import com.woshidaniu.web.utils.WebUtils;

public class UaacSsoShiroFilter extends AuthenticatingFilter {
	
	private static final Logger log = LoggerFactory.getLogger(UaacSsoShiroFilter.class);
	
	private String authServerUrl;

	@Override
	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletRequest httpRequest = WebUtils.toHttp(request); 
        HttpSession session = httpRequest.getSession();
        String userCode = UaacServiceContext.getUaacService().getCurrentLoginName(session);
        @SuppressWarnings("deprecation")
		UserInfo userInfo = UaacServiceContext.getUserService().getUserInfoByUserLoginName(userCode);
		String loginName=BlankUtils.isBlank(userInfo.getUserDetail().getJwzgh())?userInfo.getLoginName():userInfo.getUserDetail().getJwzgh();
		// 登录IP
		String host = WebRequestUtils.getRemoteAddr((HttpServletRequest) request );
		//当前已认证用户的姓名 
		String name = "";
		// 构造Token对象
		AuthenticationToken authToken =  new UaacAuthenticationToken(loginName, name, host);
		return authToken;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletRequest httpRequest = WebUtils.toHttp(request); 
        HttpServletResponse httpResponse = WebUtils.toHttp(response); 
        HttpSession session = httpRequest.getSession();
		// 第一步：获取session中的账号信息
    	String userCode = UaacServiceContext.getUaacService().getCurrentLoginName(session);
    	@SuppressWarnings("deprecation")
		UserInfo userInfo = UaacServiceContext.getUserService().getUserInfoByUserLoginName(userCode);
    	String loginName=BlankUtils.isBlank(userInfo.getUserDetail().getJwzgh())?userInfo.getLoginName():userInfo.getUserDetail().getJwzgh();
		if (StringUtils.isEmpty(loginName)) {
			
			//销毁应用本地会话等其它操作... 
            String authServerUrl = httpRequest.getHeader("AUTH_SERVER_URL"); 
            if(StringUtils.isEmpty(authServerUrl)) {
            	authServerUrl = this.authServerUrl;
            }
            String service = UaacSsoUtils.getCurrentPath(httpRequest, httpResponse); 
            WebUtils.issueRedirect(httpRequest, httpResponse, authServerUrl + "/login?service="  + URLEncoder.encode(service, "UTF-8"), null, false);
            return false;
		}else {
			log.info("Get loginName In Session ：loginName = " + loginName);
			//session增加登录类型信息:单点登录，方便后面退出时做判断
			session.setAttribute(Parameters.getGlobalString(Parameter.LOGIN_TYPE_KEY), LoginType.SSO.getKey());
			
			return this.executeLogin(httpRequest, httpResponse);
		}
	}

	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
			ServletResponse response) throws Exception {
		HttpServletRequest httpRequest = WebUtils.toHttp(request);
		String url = httpRequest.getParameter("url");
		// 登录成功,如果url不为空，说明是功能集成，则进入对应的url地址
		if (!BlankUtils.isBlank(url)) {
			String redirectUrl = "";
			if (url.contains("html&")) {
				redirectUrl = url.replace("html&", "html?");
			} else {
				String gnmkdm = request.getParameter("gnmkdm");
				String layout = request.getParameter("layout");
				String doType = request.getParameter("doType");
				redirectUrl = url + "?gnmkdm=" + gnmkdm + "&layout=" + layout;
				if (!BlankUtils.isBlank(doType)) {
					redirectUrl += "&doType=" + doType;
				}
			}
			org.apache.shiro.web.util.WebUtils.redirectToSavedRequest(request, response, redirectUrl);
		} else {
			// 登录成功,如果url为空，说明是单点登录，则进入首页
			issueSuccessRedirect(request, response);
		}
		return false;
	}

	@Override
	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request,
			ServletResponse response) {
		HttpServletRequest httpRequest = WebUtils.toHttp(request); 
        HttpServletResponse httpResponse = WebUtils.toHttp(response); 
		String service = UaacSsoUtils.getCurrentPath(httpRequest, httpResponse); 
        try {
			WebUtils.issueRedirect(httpRequest, httpResponse, authServerUrl + "/login?service="  + URLEncoder.encode(service, "UTF-8"), null, false);
		} catch (IOException e1) {
			log.error("Uaac认证登录失败重定向异常",e1);
		}
        return false;
	}

	public String getAuthServerUrl() {
		return authServerUrl;
	}

	public void setAuthServerUrl(String authServerUrl) {
		this.authServerUrl = authServerUrl;
	}
	
}
