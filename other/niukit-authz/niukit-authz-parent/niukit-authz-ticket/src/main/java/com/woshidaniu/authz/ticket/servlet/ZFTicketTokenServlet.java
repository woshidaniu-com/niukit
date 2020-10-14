package com.woshidaniu.authz.ticket.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.authz.ticket.utils.ResultUtils;
import com.woshidaniu.authz.ticket.utils.TicketTokenUtils;
import com.woshidaniu.shiro.servlet.AuthenticatingHttpServlet;
import com.woshidaniu.web.utils.WebResponseUtils;

/**
 * 
 * @类名称 ： ZFTicketTokenServlet.java
 * @类描述 ： 除cas认证、密码登录外额外提供的票据登录接口；解决部门学校无法使用单点又系统直接访问系统功能的问题，同事兼具了一定的安全性,在web.xml增加如下配置：
 * 
 * <pre>
&lt;servlet>
	&lt;servlet-name>ZFTicketTokenServlet&lt;/servlet-name&gt;
	&lt;servlet-class&gt;com.woshidaniu.authz.ticket.servlet.ZFTicketTokenServlet&lt;/servlet-class&gt;
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
	&lt;servlet-name&gt;ZFTicketTokenServlet&lt;/servlet-name&gt;
	&lt;url-pattern&gt;/api/ticket_access_token&lt;/url-pattern&gt;
&lt;/servlet-mapping&gt;
 * </pre>
 * <pre>
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
 *
 *</pre>
 * @创建人 ：wandalong
 * @创建时间 ：Jul 6, 2016 11:06:18 AM
 * @修改人 ：
 * @修改时间 ：
 * @版本号 :v1.0
 */
@SuppressWarnings("serial")
@Deprecated
public class ZFTicketTokenServlet extends AuthenticatingHttpServlet {

	protected final Logger log = LoggerFactory.getLogger(ZFTicketTokenServlet.class);

	private TicketTokenUtils ticketTokenUtils;
	
	public ZFTicketTokenServlet() {
		log.info("ZFTicketTokenServlet inited.");
	}
	
	/**
	 * 日期格式：yyyy-MM-dd HH:mm:ss.SSS
	 */
	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final SimpleDateFormat FORMAT = new SimpleDateFormat(DATE_FORMAT);
	
	/**
	 * 
	 * @description	： 获取授权access_token值
	 * @author 		： 大康（743）
	 * @date 		：2017年8月24日 下午9:03:26
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			// 学校代码
			String xxdm = this.ticketTokenUtils.getXxdm();
			// 业务系统ID
			String appid = request.getParameter("appid");
			
			// 验证appid
			if (!this.ticketTokenUtils.validAppid(xxdm, appid)) {
				// 无效的appid值
				WebResponseUtils.renderJSON(response, ResultUtils.token("0", "无效的appid值"));
			}
			
			// RSA的私钥，验证方法会通过对应的公钥进行验证
			String secret = request.getParameter("secret");
			//返回token
			WebResponseUtils.renderJSON(response, ResultUtils.token("1", this.ticketTokenUtils.genToken(appid , secret , xxdm)));
			
		} catch (Exception e) {
			WebResponseUtils.renderJSON(response, ResultUtils.token("0", "token值生成失败."));
			log.error(e.getMessage());
		}
	}

	public void setTicketTokenUtils(TicketTokenUtils ticketTokenUtils) {
		this.ticketTokenUtils = ticketTokenUtils;
	}
	

}
