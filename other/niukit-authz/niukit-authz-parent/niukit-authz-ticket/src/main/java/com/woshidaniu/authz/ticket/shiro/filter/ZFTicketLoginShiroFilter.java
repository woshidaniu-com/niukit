/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.ticket.shiro.filter;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.authz.ticket.shiro.token.ZFTicketAuthenticationToken;
import com.woshidaniu.authz.ticket.utils.TicketTokenUtils;
import com.woshidaniu.basicutils.StringUtils;
import com.woshidaniu.web.utils.WebRequestUtils;

/**
 * @className ： ZFTicketLoginShiroFilter
 * @description ： ticket认证的登录Filter，配置在Shiro的Filter链中
 * 
 * 此Filter配置在文件shiro-authz-ticket.xml中，属性通过外部properties文件配置loginUrl,successUrl
 * 
 * 必要参数：
 * userid(用户id)
 * roleid(角色id)
 * token(请求获得的token)
 * verify(验证参数)
 * time(时间戳)
 * xxdm(学校代码)
 * 
 * 1.当必要参数缺失任何一个，重定向到配置参数loginUrl指定的登录页面
 * 2.当必要参数都存在，但验证时间戳或token信息错误，重定向到配置参数loginUrl指定的登录页面
 * 3.当必要参数都存在，且验证验证时间戳和token信息正确，但在本系统无此用户信息，则登录失败，重定向到配置参数loginUrl指定的页面
 * 4.当必要参数都存在，且验证验证时间戳和token信息正确，但在本系统存在此用户信息，则登录成功，重定向到配置参数successUrl指定的页面
 * 
 * 后续Filter链中的Filter将不再处理请求
 * 
 * @author ：康康（1571）
 * @date ： 2018年4月28日 下午5:20:16
 * @version V1.0
 */
public class ZFTicketLoginShiroFilter extends AuthenticatingFilter {

	private static final Logger log = LoggerFactory.getLogger(ZFTicketLoginShiroFilter.class);

	private TicketTokenUtils ticketTokenUtils;
	/**
	 * @description	： 根据请求内参数创建token
	 * @author 		：康康（1571）
	 * @date 		：2018年5月2日 上午10:26:56
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@Override
	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
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
		// 学校代码
		String xxdm = this.ticketTokenUtils.getXxdm();
		// 登录IP
		String host = WebRequestUtils.getRemoteAddr((HttpServletRequest) request);
		// 构造Token对象
		AuthenticationToken authToken = new ZFTicketAuthenticationToken(userid, roleid, xxdm, token, verify, timestamp,host);
		return authToken;
	}

	/**
	 * @description	： 当用户未登录时，执行登录逻辑
	 * @author 		：康康（1571）
	 * @date 		：2018年5月2日 上午10:26:27
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		boolean right = this.validAllParam(request, response);
		if (right) {
			return executeLogin(request, response);
		} else {
			this.redirectToLogin(request, response);
			return false;
		}
	}

	/**
	 * @description ： 登录成功后，直接重定向到成功页面，后续Filter不再处理
	 * @author ：康康（1571）
	 * @date ：2018年5月2日 上午10:18:36
	 * @param token
	 * @param subject
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
			ServletResponse response) throws Exception {
		if(log.isDebugEnabled()) {
			log.debug("ticket login登录成功，重定向到成功页面:{}",this.getSuccessUrl());
		}
		this.issueSuccessRedirect(request, response);
		return false;
	}

	/**
	 * @description ： 登录失败后，直接重定向到登录页面，后续Filter不再处理
	 * @author ：康康（1571）
	 * @date ：2018年5月2日 上午10:19:13
	 * @param token
	 * @param e
	 * @param request
	 * @param response
	 * @return
	 */
	@Override
	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request,
			ServletResponse response) {
		try {
			if(log.isDebugEnabled()) {
				log.debug("ticket login登录失败，重定向到登录页面:{}",this.getLoginUrl());
			}
			this.redirectToLogin(request, response);
		} catch (IOException e1) {
			log.error("登录失败时处理发生异常", e1);
		}
		return false;
	}

	/**
	 * @description	： 验证所有参数
	 * @author 		： 康康（1571）
	 * @date 		：2018年5月2日 上午10:27:41
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected boolean validAllParam(ServletRequest request, ServletResponse response) throws Exception {

		if (log.isDebugEnabled()) {
			log.debug("ticket login,validAllParam");
		}
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
		// 学校代码
		String xxdm = this.ticketTokenUtils.getXxdm();

		if (StringUtils.isEmpty(userid)//
				|| StringUtils.isEmpty(roleid)//
				|| StringUtils.isEmpty(token)//
				|| StringUtils.isEmpty(verify) //
				|| StringUtils.isEmpty(timestamp)//
		) {
			return false;
		}
		// 验证时间戳
		if (!this.ticketTokenUtils.validTimestamp(timestamp)) {
			if(log.isDebugEnabled()) {
				log.debug("时间戳验证失败:timestamp:{}",timestamp);
			}
			return false;
		}
		// 验证token信息
		if (!this.ticketTokenUtils.validToken(xxdm, token)) {
			if(log.isDebugEnabled()) {
				log.debug("token验证失败:{}",token);
			}
			// token验证失败，登录失败,进入登录页
			return false;
		}
		// 验证参数信息
		String localVerify = this.ticketTokenUtils.genVerify( userid , roleid, timestamp, token );
		if(!localVerify.equals(verify)) {
			if(log.isDebugEnabled()) {
				log.debug("verify验证失败:{}",verify);
			}
			// 验证参数信息失败，登录失败,进入登录页
			return false;
		}
		return true;
	}

	public void setTicketTokenUtils(TicketTokenUtils ticketTokenUtils) {
		this.ticketTokenUtils = ticketTokenUtils;
	}

	
}
