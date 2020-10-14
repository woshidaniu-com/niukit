package com.woshidaniu.authz.uaac.servlet;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.ServletException;
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
import com.woshidaniu.shiro.servlet.AuthenticatingHttpServlet;
import com.woshidaniu.shiro.token.LoginType;
import com.woshidaniu.web.Parameter;
import com.woshidaniu.web.Parameters;
import com.woshidaniu.web.utils.WebRequestUtils;
import com.woshidaniu.web.utils.WebUtils;

/**
 * 
 * @类名称 ： UaacSsoServlet.java
 * @类描述 ：科大讯飞单点登录接口,在web.xml增加如下配置：
 * @创建人 ：jiangyangyong（111）
 * @创建时间 ：Nov 22, 2017 11:06:18 AM
 * @修改人 ：
 * @修改时间 ：
 * @版本号 :v1.0
 */
@SuppressWarnings("serial")
public class UaacSsoServlet extends AuthenticatingHttpServlet {

	protected final Logger LOG = LoggerFactory.getLogger(UaacSsoServlet.class);

	public UaacSsoServlet() {
		LOG.info("UaacSsoServlet inited.");
	}
	
	 
	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException {
		String url = request.getParameter("url");
		//如果url不为空，说明是功能集成
		if(!BlankUtils.isBlank(url)){
			onAccessDeniad(request, response);
		}else{
			if(isAccessAllowed(request, response)){
				// 已登录会话直接进入主页
				issueSuccessRedirect(request, response);
				return;
			}
			onAccessDeniad(request, response);
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onAccessDeniad(ServletRequest request, ServletResponse response) throws IOException {
		HttpServletRequest httpRequest = WebUtils.toHttp(request); 
        HttpServletResponse httpResponse = WebUtils.toHttp(response); 
        HttpSession session = httpRequest.getSession();
        String url = httpRequest.getParameter("url");
		// 第一步：获取session中的账号信息
    	String userCode = UaacServiceContext.getUaacService().getCurrentLoginName(session);
    	UserInfo userInfo = UaacServiceContext.getUserService().getUserInfoByUserLoginName(userCode);
    	String loginName=BlankUtils.isBlank(userInfo.getUserDetail().getJwzgh())?userInfo.getLoginName():userInfo.getUserDetail().getJwzgh();
		if (StringUtils.isEmpty(loginName)) {
			//销毁应用本地会话等其它操作... 
            String authServerUrl = httpRequest.getHeader("AUTH_SERVER_URL"); 
            String service = UaacSsoUtils.getCurrentPath(httpRequest, httpResponse); 
            WebUtils.issueRedirect(httpRequest, httpResponse, authServerUrl + "/login?service="  + URLEncoder.encode(service, "UTF-8"), null, false);
		}
		LOG.info("Get loginName In Session ：loginName = " + loginName);
		try {
			//session增加登录类型信息:单点登录，方便后面退出时做判断
			session.setAttribute(Parameters.getGlobalString(Parameter.LOGIN_TYPE_KEY), LoginType.SSO.getKey());
			String name = "";     //当前已认证用户的姓名 
			LOG.info("Current user id:" + loginName + " name:"  + URLDecoder.decode(name, "UTF-8")); 
            //建立应用本地会话等其它操作... 
			// 登录IP
			String host = WebRequestUtils.getRemoteAddr((HttpServletRequest) request );
			// 构造Token对象
			AuthenticationToken authToken =  new UaacAuthenticationToken(loginName, name, host);
			SecurityUtils.getSubject().login(authToken);
			//登录成功,如果url不为空，说明是功能集成，则进入对应的url地址
			if(!BlankUtils.isBlank(url)){
				String redirectUrl = "";
				if(url.contains("html&")){
					 redirectUrl = url.replace("html&", "html?");
				}else{
					String gnmkdm = request.getParameter("gnmkdm");
					String layout = request.getParameter("layout");
					String doType = request.getParameter("doType");
					redirectUrl = url+"?gnmkdm="+gnmkdm+"&layout="+layout;
					if(!BlankUtils.isBlank(doType)){
						redirectUrl += "&doType="+doType;
					}
				}
				org.apache.shiro.web.util.WebUtils.redirectToSavedRequest(request, response, redirectUrl);
			}else{
			//登录成功,如果url为空，说明是单点登录，则进入首页
				issueSuccessRedirect(request, response);
			} 
		} catch (Exception e) {
			LOG.error(e.getMessage());
			// 登录失败,进入登录页
			redirectToLogin(request, response);
		}
	}

	
}
