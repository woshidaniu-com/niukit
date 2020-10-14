package com.woshidaniu.authz.thauthall.servlet;

import java.io.IOException;
import java.util.Hashtable;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.authz.thauthall.shiro.token.ThauthallAuthenticationToken;
import com.woshidaniu.basicutils.StringUtils;
import com.woshidaniu.shiro.servlet.AuthenticatingHttpServlet;
import com.woshidaniu.web.utils.WebRequestUtils;

import thauth.Roam;
import thauth.ThauthConst;

/**
 * 
 * @类名称 ： ThauthallSsoServlet.java
 * @类描述 ： 华中师范大学单点登录接口,在web.xml增加如下配置：
 * 
 *      <pre>
&lt;servlet>
	&lt;servlet-name>ThauthallSsoServlet&lt;/servlet-name&gt;
	&lt;servlet-class&gt;com.woshidaniu.authz.thirdparty.servlet.ThauthallSsoServlet&lt;/servlet-class&gt;
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
	&lt;servlet-name&gt;ThauthallSsoServlet&lt;/servlet-name&gt;
	&lt;url-pattern&gt;/hzsflogin&lt;/url-pattern&gt;
&lt;/servlet-mapping&gt;
 *      </pre>
 * 
 * @创建人 ：wandalong
 * @创建时间 ：Jul 6, 2016 11:06:18 AM
 * @修改人 ：
 * @修改时间 ：
 * @版本号 :v1.0
 */
@SuppressWarnings({"serial","rawtypes"})
@Deprecated
public class ThauthallSsoServlet extends AuthenticatingHttpServlet {

	protected final Logger LOG = LoggerFactory.getLogger(ThauthallSsoServlet.class);
	protected String appId;

	public ThauthallSsoServlet() {
		LOG.info("ThauthallSsoServlet inited.");
	}

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		this.appId = config.getInitParameter("appId");
	}
	
	@Override
	protected void onAccessDeniad(ServletRequest request, ServletResponse response) throws IOException {
		
		// 第一步：获取请求中的票据信息

		// 票据ID
		String ticketId = request.getParameter("ticket");
		if (StringUtils.isEmpty(ticketId)) {
			redirectToLogin(request, response);
		}

		LOG.debug("Get ticketId In Request ：ticketId = " + ticketId);

		try {
			// 登录IP
			String host = WebRequestUtils.getRemoteAddr((HttpServletRequest) request);

			// 第二步： 根据票据获取用户信息
			Hashtable ht = Roam.thauthCheckTicket(ticketId, appId, host);
			// 登录状态
			int code = ((Integer) ht.get(ThauthConst.THAUTH_CODE)).intValue();
			// 单位
			String dw = (String) ht.get(ThauthConst.THAUTH_DW);
			// 邮箱
			String email = (String) ht.get(ThauthConst.THAUTH_EMAIL);
			// 密码
			String mm = (String) ht.get(ThauthConst.THAUTH_MM);
			// 秘钥
			String skey = (String) ht.get(ThauthConst.THAUTH_SKEY);
			// 票据
			String ticket = (String) ht.get(ThauthConst.THAUTH_TICKET);
			// 姓名
			String xm = (String) ht.get(ThauthConst.THAUTH_XM);
			// 用户类别
			String yhlb = (String) ht.get(ThauthConst.UUTYPE_YHLB);
			// 用户名
			String yhm = (String) ht.get(ThauthConst.THAUTH_YHM);
			// 用户状态
			String yhzt = (String) ht.get(ThauthConst.THAUTH_YHZT);
			// 职工号
			String zjh = (String) ht.get(ThauthConst.THAUTH_ZJH);

			// 构造Token对象
			AuthenticationToken authToken = new ThauthallAuthenticationToken(code, dw, email, mm, skey, ticket, xm,
					yhlb, yhm, yhzt, zjh, host);

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
