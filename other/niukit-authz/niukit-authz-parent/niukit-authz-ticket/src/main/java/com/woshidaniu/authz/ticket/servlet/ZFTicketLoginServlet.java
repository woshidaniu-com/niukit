package com.woshidaniu.authz.ticket.servlet;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.authz.ticket.shiro.token.ZFTicketAuthenticationToken;
import com.woshidaniu.authz.ticket.utils.TicketTokenUtils;
import com.woshidaniu.basicutils.DateUtils;
import com.woshidaniu.basicutils.StringUtils;
import com.woshidaniu.shiro.servlet.AuthenticatingHttpServlet;
import com.woshidaniu.web.utils.WebRequestUtils;

/**
 * 
 * @类名称 ： ZFTicketLoginServlet.java
 * @类描述 ： 除cas认证、密码登录外额外提供的票据登录接口；解决部门学校无法使用单点又系统直接访问系统功能的问题，同事兼具了一定的安全性,在web.
 *      xml增加如下配置：
 * 
 *      <pre>
&lt;servlet>
	&lt;servlet-name>ZFTicketLoginServlet&lt;/servlet-name&gt;
	&lt;servlet-class&gt;com.woshidaniu.authz.ticket.servlet.ZFTicketLoginServlet&lt;/servlet-class&gt;
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
	&lt;servlet-name&gt;ZFTicketLoginServlet&lt;/servlet-name&gt;
	&lt;url-pattern&gt;/api/ticketlogin&lt;/url-pattern&gt;
&lt;/servlet-mapping&gt;
 *      </pre>
 * 
 *      <pre>
 *	仿微信实现系统间的认证接口对接规范
 *	以下方法是为了提供其他系统的安全无密码登录或不需要登录即可访问数据接口
 *	
 *	1、为每个应用分配appid和secret值
 *	
 *	appid是唯一的值
 *	secret值是RSA的私钥，验证方法会通过对应的公钥进行验证
 *	
 *	
 *	2、访问获取access_token的路径通过获取授权access_token值
 *	
 *  #获取授权access_token
 *	access_token_get_url = http://niutal.edu.cn/api/ticket_access_token?appid={0}
 *	#（access_token）登录
 *	access_token_login_url =  http://niutal.edu.cn/api/ticketlogin?openid={0}&token={1}
 *
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
public class ZFTicketLoginServlet extends AuthenticatingHttpServlet {

	protected final Logger log = LoggerFactory.getLogger(ZFTicketLoginServlet.class);
	
	private TicketTokenUtils ticketTokenUtils;
	
	public ZFTicketLoginServlet() {
		log.info("ZFTicketLoginServlet inited.");
	}
	
	@Override
	protected void onAccessDeniad(ServletRequest request, ServletResponse response) throws IOException {

		// 第一步：获取请求中的登录信息

		// 用户ID
		String userid = request.getParameter("userid");
		// 角色ID，xs,js：方便区别用户角色
		String roleid = request.getParameter("roleid");
		// 系统双方约定的秘钥:基于Des + Base64加密的值
		String token = request.getParameter("token");
		// 32位MD5加密信息（大写）:格式为：(卡号-用户类型-时间戳-token)值组合的MD5值
		String verify = request.getParameter("verify");
		// 时间戳;格式: yyyyMMddHHmmssSSS
		String timestamp = request.getParameter("time");
		// 验证用户名和用户类型
		if (StringUtils.isEmpty(userid) || StringUtils.isEmpty(roleid) || StringUtils.isEmpty(token)
				|| StringUtils.isEmpty(verify) || StringUtils.isEmpty(timestamp)) {
			// 跳转到登录口
			redirectToLogin(request, response);
			return;
		}
		
		try {

			// 学校代码
			String xxdm = this.ticketTokenUtils.getXxdm();
			
			// 验证时间戳
			if(!this.ticketTokenUtils.validTimestamp(timestamp)) {
				// 时间戳超时，登录失败,进入登录页
				redirectToLogin(request, response);
				return;
			}
			
			// 验证token信息
			if(!this.ticketTokenUtils.validToken(xxdm, token)) {
				// token验证失败，登录失败,进入登录页
				redirectToLogin(request, response);
				return;
			}
			
			// 验证参数信息
			String localVerify = this.ticketTokenUtils.genVerify( userid , roleid, timestamp, token );
			if(!localVerify.equals(verify)) {
				// 验证参数信息失败，登录失败,进入登录页
				redirectToLogin(request, response);
				return;
			}
			
			// 登录IP
			String host = WebRequestUtils.getRemoteAddr((HttpServletRequest) request);
			// 构造Token对象
			AuthenticationToken authToken = new ZFTicketAuthenticationToken(userid, roleid, xxdm, token, verify,
					timestamp, host);

			SecurityUtils.getSubject().login(authToken);
			
			// 登录成功,进入主页
			issueSuccessRedirect(request, response);

		} catch (Exception e) {
			log.error(e.getMessage());
			// 登录失败,进入登录页
			redirectToLogin(request, response);
		}
	}

	public void setTicketTokenUtils(TicketTokenUtils ticketTokenUtils) {
		this.ticketTokenUtils = ticketTokenUtils;
	}

}
