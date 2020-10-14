/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.agent.shiro.filter;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.authz.agent.shiro.token.AgentAuthenticationToken;
import com.woshidaniu.basicutils.StringUtils;
import com.woshidaniu.web.utils.WebRequestUtils;

/**
 * 
 * @className	： AgentSsoShiroFilter
 * @description	：  湖南机电职业（江苏达科教育科技有限公司）单点登录认证过滤器
 * @author 		：康康（1571）
 * @date		： 2018年5月11日 下午2:40:56
 * @version 	V1.0
 */
public class AgentSsoShiroFilter extends AuthenticatingFilter{
	
	private static final Logger log = LoggerFactory.getLogger(AgentSsoShiroFilter.class);
	
	//登录成功后是否重定向到之前保存的请求地址
	private boolean redirectSavedRequestAfterLoginSuccess = false;
	
	private String authServerUrl;

	@Override
	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
		
		HttpServletRequest httpRequest = WebUtils.toHttp(request);
		
		// 获取Header中的账号信息
		String uid = httpRequest.getHeader("UID");
		// 获取当前已认证用户的姓名
		String name = httpRequest.getHeader("CN");
		// 登录IP
		String host = WebRequestUtils.getRemoteAddr((HttpServletRequest) request);
		// 构造Token对象
		AuthenticationToken authToken = new AgentAuthenticationToken(uid, name, host);
		return authToken;
	}


	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletRequest httpRequest = WebUtils.toHttp(request); 
        HttpServletResponse httpResponse = WebUtils.toHttp(response); 
        
        //获取Header中的账号信息
        String uid = httpRequest.getHeader("UID");
        //获取当前已认证用户的姓名 
        String name = httpRequest.getHeader("CN"); 
        
        if (StringUtils.isNotEmpty(uid) && StringUtils.isNotEmpty(name)) { 
        	
        	if(log.isDebugEnabled()) {
        		log.debug("get uid in header uid[" + uid+ "], get name ["+ name +"]");        		
        		log.debug("current user id:" + uid + " name:"  + URLDecoder.decode(name, "UTF-8")); 
        	}
        	this.executeLogin(httpRequest, httpResponse);
			// 登录成功,进入主页
			issueSuccessRedirect(request, response);
			return false;
        }else {
        	String authServerUrl = httpRequest.getHeader("AUTH_SERVER_URL");
        	if(StringUtils.isEmpty(authServerUrl)) {
        		authServerUrl = this.authServerUrl;
        	}
        	String service = getCurrentPath(httpRequest, httpResponse); 
        	WebUtils.issueRedirect(httpRequest, httpResponse, authServerUrl + "/login?service="  + URLEncoder.encode(service, "UTF-8"), null, false);
        	return false;
        }
	}

	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
			ServletResponse response) throws Exception {
		
		if(this.redirectSavedRequestAfterLoginSuccess) {//重定向到saved request
			WebUtils.redirectToSavedRequest(request, response, getSuccessUrl());
		}else {//重定向到真正的首页
			WebUtils.issueRedirect(request, response, getSuccessUrl());
		}
		return false;
	}

	@Override
	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request,
			ServletResponse response) {
		HttpServletRequest httpRequest = WebUtils.toHttp(request); 
        HttpServletResponse httpResponse = WebUtils.toHttp(response); 
		try {
			String service = getCurrentPath(httpRequest, httpResponse); 
        	WebUtils.issueRedirect(httpRequest, httpResponse, authServerUrl + "/login?service="  + URLEncoder.encode(service, "UTF-8"), null, false);
		} catch (IOException e1) {
			log.error("Agent登录失败后重定向到登录页面异常",e1);
		}
		return false;
	}
	
	//获取当前访问资源的URL 
    private String getCurrentPath(HttpServletRequest request, HttpServletResponse response) { 
        //可以从HTTP头中获取到当前访问资源的URI（不包含参数） 
        String requestUrl = request.getHeader("X-REQUEST-URL"); 
        String queryStr = request.getQueryString(); 
        if (queryStr != null) { 
            requestUrl = requestUrl + "?" + queryStr; 
        } 
        return requestUrl; 
    }

	public void setAuthServerUrl(String authServerUrl) {
		this.authServerUrl = authServerUrl;
	}

}
