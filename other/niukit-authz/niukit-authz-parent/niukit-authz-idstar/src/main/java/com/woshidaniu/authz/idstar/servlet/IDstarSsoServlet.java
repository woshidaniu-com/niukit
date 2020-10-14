/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.idstar.servlet;


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

import com.woshidaniu.authz.idstar.shiro.token.IDstarAuthenticationToken;
import com.woshidaniu.authz.idstar.utils.IDstarSsoUtils;
import com.woshidaniu.basicutils.StringUtils;
import com.woshidaniu.shiro.servlet.AuthenticatingHttpServlet;
import com.woshidaniu.web.utils.WebRequestUtils;
import com.woshidaniu.web.utils.WebUtils;

/**
 * 
 * @类名称 ： IDstarSsoServlet.java
 * @类描述 ： 金智单点登录接口,在web.xml增加如下配置：
 * 
 * <pre>
&lt;servlet>
	&lt;servlet-name>IDstarSsoServlet&lt;/servlet-name&gt;
	&lt;servlet-class&gt;com.woshidaniu.authz.thirdparty.servlet.IDstarSsoServlet&lt;/servlet-class&gt;
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
	&lt;servlet-name&gt;IDstarSsoServlet&lt;/servlet-name&gt;
	&lt;url-pattern&gt;/idstarlogin&lt;/url-pattern&gt;
&lt;/servlet-mapping&gt;
 * </pre>
 * 
 * @创建人 ：wandalong
 * @创建时间 ：Jul 6, 2016 11:06:18 AM
 * @修改人 ：
 * @修改时间 ：
 * @版本号 :v1.0
 */
@Deprecated
@SuppressWarnings("serial")
public class IDstarSsoServlet extends AuthenticatingHttpServlet {

	protected final Logger LOG = LoggerFactory.getLogger(IDstarSsoServlet.class);
	protected String configPath = "/WEB-INF/properties/client.properties";

	public IDstarSsoServlet() {
		LOG.info("IDstarSsoServlet inited.");
	}

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		this.configPath = config.getInitParameter("configPath");
	}

	@Override
	protected void onAccessDeniad(ServletRequest request, ServletResponse response) throws IOException {
		

		// 第一步：获取Cookie中的Token信息
		String token = IDstarSsoUtils.getTokenInCookie(WebUtils.toHttp(request), WebUtils.toHttp(response));
		if (StringUtils.isEmpty(token)) {
			redirectToLogin(request, response);
		}

		LOG.debug("Get Token In Cookie ：token = " + token);

		try {

			// 客户端配置
			String is_config = request.getServletContext().getRealPath(configPath);
			// 登录IP
			String host = WebRequestUtils.getRemoteAddr((HttpServletRequest) request);
			// 登录账号ID
			String uid = IDstarSsoUtils.getUidByToken(is_config, token);
			// 构造Token对象
			AuthenticationToken authToken = new IDstarAuthenticationToken(token, uid, host);

			SecurityUtils.getSubject().login(authToken);

			// 登录成功,进入主页
			issueSuccessRedirect(request, response);

		} catch (Exception e) {
			LOG.error(e.getMessage());
			// 登录失败,进入登录页
			redirectToLogin(request, response);
		}
	}

}
