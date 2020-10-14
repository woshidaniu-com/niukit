package com.woshidaniu.authz.agent.servlet;

import java.io.IOException;
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
import com.woshidaniu.shiro.servlet.AuthenticatingHttpServlet;
import com.woshidaniu.web.utils.WebRequestUtils;
import com.woshidaniu.web.utils.WebUtils;


/**
 * 
 * @类名称 ： AgentSsoServlet.java
 * @类描述 ： 单点登录接口,在web.xml增加如下配置：
 * 
 * <pre>
&lt;servlet>
	&lt;servlet-name>AgentSsoServlet&lt;/servlet-name&gt;
	&lt;servlet-class&gt;com.woshidaniu.authz.thirdparty.servlet.AgentSsoServlet&lt;/servlet-class&gt;
	&lt;init-param&gt;
		&lt;param-name&gt;appId&lt;/param-name&gt;
		&lt;param-value&gt;XK&lt;/param-value&gt;
	&lt;/init-param&gt;
	&lt;init-param&gt;
		&lt;param-name&gt;loginUrl&lt;/param-name&gt;
		&lt;param-value&gt;&lt;/param-value&gt;
	&lt;/init-param&gt;
	&lt;init-param&gt;
		&lt;param-name&gt;successUrl&lt;/param-name&gt;
		&lt;param-value&gt;&lt;/param-value&gt;
	&lt;/init-param&gt;
&lt;/servlet&gt;
&lt;servlet-mapping&gt;
	&lt;servlet-name&gt;AgentSsoServlet&lt;/servlet-name&gt;
	&lt;url-pattern&gt;/agentlogin&lt;/url-pattern&gt;
&lt;/servlet-mapping&gt;
 * </pre>
 * 
 * @创建人 ：wandalong
 * @创建时间 ：Jul 6, 2016 11:06:18 AM
 * @修改人 ：
 * @修改时间 ：
 * @版本号 :v1.0
 */
@SuppressWarnings("serial")
@Deprecated
public class AgentSsoServlet extends AuthenticatingHttpServlet {

	protected final Logger LOG = LoggerFactory.getLogger(AgentSsoServlet.class);

	public AgentSsoServlet() {
		LOG.info("AgentSsoServlet inited.");
	}

	@Override
	protected void onAccessDeniad(ServletRequest request, ServletResponse response) throws IOException {
		
		HttpServletRequest httpRequest = WebUtils.toHttp(request); 
        HttpServletResponse httpResponse = WebUtils.toHttp(response); 
        
		// 第一步：获取Header中的账号信息
		String uid = httpRequest.getHeader("UID"); 
		if (StringUtils.isEmpty(uid)) {
			
			//销毁应用本地会话等其它操作... 
            String authServerUrl = httpRequest.getHeader("AUTH_SERVER_URL"); 
            String service = getCurrentPath(httpRequest, httpResponse); 
            WebUtils.issueRedirect(httpRequest, httpResponse, authServerUrl + "/login?service="  + URLEncoder.encode(service, "UTF-8"), null, false);
            //httpResponse.sendRedirect(authServerUrl + "/login?service="  + URLEncoder.encode(service, "UTF-8")); 
		}

		LOG.info("Get Uid In Header ：uid = " + uid);

		try {

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
						
		} catch (Exception e) {
			LOG.error(e.getMessage());
			// 登录失败,进入登录页
			redirectToLogin(request, response);
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
