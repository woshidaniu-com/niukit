/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.uaac.servlet.filter;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iflytek.uaac.client.rest.context.UaacServiceContext;
import com.iflytek.uaac.common.domain.UserInfo;
import com.woshidaniu.authz.uaac.shiro.token.UaacAuthenticationToken;
import com.woshidaniu.authz.uaac.utils.UaacSsoUtils;
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
 * @className	： UaacAuthenticatingFilter
 * @description	：科大讯飞单点单点认证登录
 * @author 		：大康（743）
 * @date		： 2017年8月24日 下午7:51:14
 * @version 	V1.0
 */
public class UaacAuthenticatingFilter extends AbstractAuthenticatingFilter {
 
	protected Logger LOG = LoggerFactory.getLogger(UaacAuthenticatingFilter.class);

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
	        
			// 第一步：获取session中的账号信息
	    	String userCode = UaacServiceContext.getUaacService().getCurrentLoginName(session);
	    	@SuppressWarnings("deprecation")
			UserInfo userInfo = UaacServiceContext.getUserService().getUserInfoByUserLoginName(userCode);
	    	String loginName=BlankUtils.isBlank(userInfo.getUserDetail().getJwzgh())? userInfo.getLoginName() : userInfo.getUserDetail().getJwzgh();
			if (StringUtils.isEmpty(loginName)) {
				//销毁应用本地会话等其它操作... 
	            String authServerUrl = httpRequest.getHeader("AUTH_SERVER_URL"); 
	            String service = UaacSsoUtils.getCurrentPath(httpRequest, httpResponse); 
	            WebUtils.issueRedirect(httpRequest, httpResponse, authServerUrl + "/login?service="  + URLEncoder.encode(service, "UTF-8"), null, false);
			}
			LOG.info("Get loginName In Session ：loginName = " + loginName);
			
			//session增加登录类型信息:单点登录，方便后面退出时做判断
			session.setAttribute(Parameters.getGlobalString(Parameter.LOGIN_TYPE_KEY), LoginType.SSO.getKey());
			String name = "";     //当前已认证用户的姓名 
			LOG.info("Current user id:" + loginName + " name:"  + URLDecoder.decode(name, "UTF-8")); 
            //建立应用本地会话等其它操作... 
			// 登录IP
			String host = WebRequestUtils.getRemoteAddr((HttpServletRequest) request );
			// 构造Token对象
			AuthenticationToken authToken = new UaacAuthenticationToken(loginName, name, host);
			SecurityUtils.getSubject().login(authToken);
			
		} catch (Exception e) {
			LOG.error(e.getMessage());
			try {
				// 登录失败,进入登录页
				redirectToLogin(request, response);
			} catch (IOException e1) {
				
			}
		}
		
	}
	 
	
}
