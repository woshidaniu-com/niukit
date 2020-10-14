package com.woshidaniu.authz.zjucookie.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.authz.zjucookie.shiro.token.ZjuCookie;
import com.woshidaniu.authz.zjucookie.shiro.token.ZjuCookieAuthcToken;
import com.woshidaniu.authz.zjucookie.utils.CookieSsoUtils;
import com.woshidaniu.basicutils.StringUtils;
import com.woshidaniu.shiro.servlet.AuthenticatingHttpServlet;
import com.woshidaniu.web.utils.WebRequestUtils;
import com.woshidaniu.web.utils.WebUtils;

/**
 * 
 * @类名称 ： ZjuCookieSsoServlet.java
 * @类描述 ： 浙大基于Cookie的单点认证登录接口,在web.xml增加如下配置：
 * 
 *      <pre>
&lt;servlet>
	&lt;servlet-name>ZjuCookieSsoServlet&lt;/servlet-name&gt;
	&lt;servlet-class&gt;com.woshidaniu.authz.thirdparty.servlet.ZjuCookieSsoServlet&lt;/servlet-class&gt;
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
	&lt;servlet-name&gt;ZjuCookieSsoServlet&lt;/servlet-name&gt;
	&lt;url-pattern&gt;/zjulogin&lt;/url-pattern&gt;
&lt;/servlet-mapping&gt;
 *      </pre>
 * 
 * @创建人 ：wandalong
 * @创建时间 ：Jul 6, 2016 11:06:18 AM
 * @修改人 ：
 * @修改时间 ：
 * @版本号 :v1.0
 */
@SuppressWarnings("serial")
@Deprecated
public class ZjuCookieSsoServlet extends AuthenticatingHttpServlet {

	protected final Logger LOG = LoggerFactory.getLogger(ZjuCookieSsoServlet.class);
	private String sessionURL;
	private String userURL;
	private String appUid;
	private String appPwd;
	
	public ZjuCookieSsoServlet() {
		LOG.info("ZjuCookieSsoServlet inited.");
	}
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		this.sessionURL = config.getInitParameter("zjdxsso.sessionURL");
		this.userURL = config.getInitParameter("zjdxsso.userURL");
		this.appUid = config.getInitParameter("zjdxsso.appUid");
		this.appPwd = config.getInitParameter("zjdxsso.appPwd");
	}
	
	@Override
	protected void onAccessDeniad(ServletRequest request, ServletResponse response) throws IOException {
		
		try {
			
			// 第一步：获取cookie中的账号信息
			String uid = CookieSsoUtils.getUidByTokenInCookie(sessionURL, appUid, appPwd, WebUtils.toHttp(request), WebUtils.toHttp(response));
			if (StringUtils.isNotEmpty(uid)) {
				LOG.info("Get Uid By Token In Cookie ：uid = " + uid);
				// 登录IP
				String host = WebRequestUtils.getRemoteAddr((HttpServletRequest) request );
				// 构造Token对象
				AuthenticationToken authToken =  new ZjuCookieAuthcToken(new ZjuCookie(uid, host));
				
				SecurityUtils.getSubject().login(authToken);
				
				// 登录成功,进入主页
				issueSuccessRedirect(request, response);
			}
			
			// 第二步：如果没有Cookie登录信息则用参数登录
			
			LOG.info("Cookie SSO Login .");
			String yhm = request.getParameter("yhm");
			String mm = request.getParameter("mm");
			//验证用户名和密码是否已填
			if (StringUtils.isEmpty(yhm) || StringUtils.isEmpty(mm)) {
				LOG.info("Cookie SSO Login [yhm or mm is empty ].");
				// 参数异常,进入登录页
				redirectToLogin(request, response);
			}
			//SSO登录
			String token = CookieSsoUtils.login(sessionURL, appUid, appPwd, yhm, mm, WebUtils.toHttp(response));
			if (StringUtils.isNotEmpty(token)) {
				// 登录成功，从服务器获取uid
				uid = CookieSsoUtils.getUidByToken(sessionURL, appUid, appPwd,token);
				if (StringUtils.isNotEmpty(uid)) {
					LOG.info("Cookie SSO UID get Success.");
					// 登录IP
					String host = WebRequestUtils.getRemoteAddr((HttpServletRequest) request );
					// 构造Token对象
					AuthenticationToken authToken = new ZjuCookieAuthcToken(new ZjuCookie(uid, host));
					 
					SecurityUtils.getSubject().login(authToken);
					
					// 登录成功,进入主页
					issueSuccessRedirect(request, response);
				}
			}
			
		} catch (Exception e) {
			LOG.error(e.getMessage());
			// 登录失败,进入登录页
			redirectToLogin(request, response);
		}

	}

	public String getSessionURL() {
		return sessionURL;
	}

	public void setSessionURL(String sessionURL) {
		this.sessionURL = sessionURL;
	}

	public String getUserURL() {
		return userURL;
	}

	public void setUserURL(String userURL) {
		this.userURL = userURL;
	}

	public String getAppUid() {
		return appUid;
	}

	public void setAppUid(String appUid) {
		this.appUid = appUid;
	}

	public String getAppPwd() {
		return appPwd;
	}

	public void setAppPwd(String appPwd) {
		this.appPwd = appPwd;
	}

}
