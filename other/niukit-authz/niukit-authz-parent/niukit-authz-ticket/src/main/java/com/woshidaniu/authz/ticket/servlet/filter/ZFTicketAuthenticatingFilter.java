/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.ticket.servlet.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.subject.WebSubject;
import org.apache.shiro.mgt.SecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.authz.ticket.shiro.token.ZFTicketAuthenticationToken;
import com.woshidaniu.authz.ticket.utils.TicketTokenUtils;
import com.woshidaniu.basicutils.DateUtils;
import com.woshidaniu.basicutils.StringUtils;
import com.woshidaniu.shiro.servlet.filter.AbstractAuthenticatingFilter;
import com.woshidaniu.web.utils.WebRequestUtils;

/**
 * 
 * @className ： IDstarAuthenticatingFilter
 * @description ： 整合金智的单点认证登录，已经废弃，使用实现Shiro框架的Filter
 * @author ： 大康（743）
 * @author : 康康（1571）
 * @date ： 2017年8月24日 下午7:51:14
 * @version V1.0
 */
@Deprecated
public class ZFTicketAuthenticatingFilter extends AbstractAuthenticatingFilter {

	protected Logger log = LoggerFactory.getLogger(ZFTicketAuthenticatingFilter.class);
	
	private SecurityManager securityManager;
	
	private TicketTokenUtils ticketTokenUtils;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		super.init(filterConfig);
	}

	@Override
	protected void onFilterConfigSet(FilterConfig filterConfig) throws Exception {
		super.onFilterConfigSet(filterConfig);
		this.appliedPaths.add("/**");
	}
	
	@Override
    public void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {
		WebSubject webSubject = new WebSubject.Builder(this.securityManager, request, response).buildWebSubject();
		Subject subject = (Subject)webSubject;
		ThreadContext.bind(subject);
		super.doFilterInternal(request, response, chain);
    }
	
	@Override
	protected boolean onPreHandle(ServletRequest request, ServletResponse response) throws Exception {
		
		HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		if(log.isDebugEnabled()) {
			log.debug("onPreHandle url:{}", httpServletRequest.getRequestURL().toString());
		}
		
		boolean isAccessAllowed = this.isAccessAllowed(request, response);
		if(isAccessAllowed) {
			if(log.isDebugEnabled()) {
				log.debug("is isAccessAllowed , not into onAccessDeniad()");
			}
			return true;
		}else {
			if(log.isDebugEnabled()) {
				log.debug("is not AccessAllowed , into onAccessDeniad()");
			}
			onAccessDeniad(request, response);
			return true;
		}
	}

	@Override
	protected void onAccessDeniad(ServletRequest request, ServletResponse response) {
		
		HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		if(log.isDebugEnabled()) {
			log.debug("onAccessDeniad url:{}", httpServletRequest.getRequestURL().toString());
			log.debug("onAccessDeniad queryString:{}", httpServletRequest.getQueryString());
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
		// 验证用户名和用户类型
		if (StringUtils.isEmpty(userid) || StringUtils.isEmpty(roleid) || StringUtils.isEmpty(token)
				|| StringUtils.isEmpty(verify) || StringUtils.isEmpty(timestamp)) {
			return;
		}
		
		try {
			
			// 学校代码
			String xxdm = this.ticketTokenUtils.getXxdm();
			
			// 验证时间戳
			if (!this.ticketTokenUtils.validTimestamp(timestamp)) {
				return;
			}

			// 验证token信息
			if (!this.ticketTokenUtils.validToken(xxdm, token)) {
				return;
			}

			// 验证参数信息
			String localVerify = this.ticketTokenUtils.genVerify(userid, roleid, timestamp, token);
			if (!localVerify.equals(verify)) {
				return;
			}
			
			// 登录IP
			String host = WebRequestUtils.getRemoteAddr((HttpServletRequest) request);
			// 构造Token对象
			AuthenticationToken authToken = new ZFTicketAuthenticationToken(userid, roleid, xxdm, token, verify,
					timestamp, host);

			SecurityUtils.getSubject().login(authToken);

		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	public void setSecurityManager(SecurityManager securityManager) {
		this.securityManager = securityManager;
	}

	public void setTicketTokenUtils(TicketTokenUtils ticketTokenUtils) {
		this.ticketTokenUtils = ticketTokenUtils;
	}
	
	
	
}
