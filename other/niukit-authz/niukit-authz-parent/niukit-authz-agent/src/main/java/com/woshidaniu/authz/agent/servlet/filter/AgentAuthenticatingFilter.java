/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.agent.servlet.filter;

import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.authz.agent.shiro.token.AgentAuthenticationToken;
import com.woshidaniu.basicutils.StringUtils;
import com.woshidaniu.shiro.servlet.filter.AbstractAuthenticatingFilter;
import com.woshidaniu.web.utils.WebRequestUtils;
import com.woshidaniu.web.utils.WebUtils;

/**
 * 
 * @className	： AgentAuthenticatingFilter
 * @description	： 湖南机电职业（江苏达科教育科技有限公司）
 * @author 		：大康（743）
 * @date		： 2017年10月12日 下午3:48:59
 * @version 	V1.0
 */
@Deprecated
public class AgentAuthenticatingFilter extends AbstractAuthenticatingFilter {

	protected Logger LOG = LoggerFactory.getLogger(AgentAuthenticatingFilter.class);
	
	//是否允许匿名访问 
    private boolean permitAnonymousAccess = false;
	
	@Override
	protected void onAccessDeniad(ServletRequest request, ServletResponse response) {
		
		try {
			
			HttpServletRequest httpRequest = WebUtils.toHttp(request); 
	        HttpServletResponse httpResponse = WebUtils.toHttp(response); 
	        
	        // 第一步：获取Header中的账号信息
			String uid = httpRequest.getHeader("UID"); 
	        if (StringUtils.isNotEmpty(uid)) { 
	        	
	        	LOG.info("Get Uid In Header ：uid = " + uid);
	        	 
	            //获取当前已认证用户的姓名 
	            String name = httpRequest.getHeader("CN"); 
	            System.out.println("current user id:" + uid + " name:"  + URLDecoder.decode(name, "UTF-8")); 
	            //建立应用本地会话等其它操作... 
	           
				// 登录IP
				String host = WebRequestUtils.getRemoteAddr((HttpServletRequest) request );
				// 构造Token对象
				AuthenticationToken authToken =  new AgentAuthenticationToken(uid, name, host);
				
				SecurityUtils.getSubject().login(authToken);
				// 登录成功,进入主页
				issueSuccessRedirect(request, response);
				
	        } 
	        
	        //用户未认证或SSO会话已失效 
	        if (!permitAnonymousAccess) { 
	            //销毁应用本地会话等其它操作... 
	            String authServerUrl = httpRequest.getHeader("AUTH_SERVER_URL"); 
	            String service = getCurrentPath(httpRequest, httpResponse); 
	            WebUtils.issueRedirect(httpRequest, httpResponse, authServerUrl + "/login?service="  + URLEncoder.encode(service, "UTF-8"), null, false);
	            //httpResponse.sendRedirect(authServerUrl + "/login?service="  + URLEncoder.encode(service, "UTF-8")); 
	        } 
			   
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
		
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

	
}
