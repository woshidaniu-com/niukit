package com.woshidaniu.authz.thauthall.bak;

import java.io.IOException;
import java.util.Hashtable;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.web.servlet.AbstractHttpServlet;

import thauth.Roam;
import thauth.ThauthConst;
/**
 * 
 *@类名称		： ThauthallSsoServlet.java
 *@类描述		： 华中师范大学单点登录接口,在web.xml增加如下配置：
 *<pre>
&lt;servlet>
	&lt;servlet-name>HzsfdxLoginServlet&lt;/servlet-name&gt;
	
	&lt;servlet-class&gt;com.woshidaniu.api.servlet.ThauthallSsoServlet&lt;/servlet-class&gt;
	&lt;init-param&gt;
		&lt;param-name&gt;redirectURL&lt;/param-name&gt;
		&lt;param-value&gt;xtgl/login_tickitLogin.html&lt;/param-value&gt;
	&lt;/init-param&gt;
&lt;/servlet&gt;
&lt;servlet-mapping&gt;
	&lt;servlet-name&gt;HzsfdxLoginServlet&lt;/servlet-name&gt;
	&lt;url-pattern&gt;/hzsflogin&lt;/url-pattern&gt;
&lt;/servlet-mapping&gt;
 *</pre>
 *@创建人		：wandalong
 *@创建时间	：Jul 6, 2016 11:06:18 AM
 *@修改人		：
 *@修改时间	：
 *@版本号	:v1.0
 */
@SuppressWarnings("serial")
public class ThauthallSsoServlet extends AbstractHttpServlet {

	private String redirect;
	protected final Logger LOG = LoggerFactory.getLogger(ThauthallSsoServlet.class);
	
	public ThauthallSsoServlet() {
		LOG.info("ThauthallSsoServlet inited.");
	}
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		//获取重定向路径
		redirect = config.getInitParameter("redirectURL");
	}
	
	@SuppressWarnings({ "rawtypes", "unused" })
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String ticket = request.getParameter("ticket");
		String remoteAddr = request.getRemoteAddr();
		// System.out.println(" -----ticketֵ:"+ticket);

		request.getSession().setAttribute("ticket", ticket);
		request.getSession().setAttribute("remoteAddr", remoteAddr);

		// System.out.println("servlet getRemoteAddr : " +
		// request.getRemoteAddr());

		Hashtable ht = Roam.thauthCheckTicket(ticket, "XK", remoteAddr);
		int code = ((Integer) ht.get(ThauthConst.THAUTH_CODE)).intValue();
		String zjh = (String) ht.get(ThauthConst.THAUTH_ZJH);
		String yhm = (String) ht.get(ThauthConst.THAUTH_YHM);
		String xm = (String) ht.get(ThauthConst.THAUTH_XM);

		request.getSession().setAttribute("code", code);
		request.getSession().setAttribute("zjh", zjh);

		// System.out.println("servlet code : " + code + "  zjh : " + zjh);

		response.sendRedirect(redirect);
	}

	/**
	 * @return the redirect
	 */
	public String getRedirect() {
		return redirect;
	}

	/**
	 * @param redirect the redirect to set
	 */
	public void setRedirect(String redirect) {
		this.redirect = redirect;
	}
	
	
}
