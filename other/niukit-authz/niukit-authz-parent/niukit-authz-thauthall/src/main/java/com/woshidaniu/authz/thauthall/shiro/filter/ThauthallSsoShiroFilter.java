/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.thauthall.shiro.filter;

import java.io.IOException;
import java.util.Hashtable;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.authz.thauthall.shiro.token.ThauthallAuthenticationToken;
import com.woshidaniu.basicutils.StringUtils;
import com.woshidaniu.web.utils.WebRequestUtils;

import thauth.Roam;
import thauth.ThauthConst;

/**
 * 
 * @className ： ThauthallSsoFilter
 * @description ：华中师范大学单点登录过滤器
 * @author ：康康（1571）
 * @date ： 2018年5月11日 上午10:07:32
 * @version V1.0
 */
public class ThauthallSsoShiroFilter extends AuthenticatingFilter {

	private static final Logger log = LoggerFactory.getLogger(ThauthallSsoShiroFilter.class);

	private String appId;
	
	//登录成功后是否重定向到之前保存的请求地址
	private boolean redirectSavedRequestAfterLoginSuccess = false;

	@Override
	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
		String ticketId = request.getParameter("ticket");
		// 登录IP
		String host = WebRequestUtils.getRemoteAddr((HttpServletRequest) request);

		// 第二步： 根据票据获取用户信息
		@SuppressWarnings("rawtypes")
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
		AuthenticationToken authToken = new ThauthallAuthenticationToken(code, dw, email, mm, skey, ticket, xm, yhlb,yhm, yhzt, zjh, host);
		return authToken;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {

		// 票据ID
		String ticketId = request.getParameter("ticket");

		if (StringUtils.isEmpty(ticketId)) {
			log.debug("Get ticketId null In Request");
			this.redirectToLogin(request, response);
			return false;
		} else {
			log.debug("Get ticketId In Request ：ticketId = " + ticketId);
			return this.executeLogin(request, response);
		}
	}

	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
			ServletResponse response) throws Exception {
		
		if(this.redirectSavedRequestAfterLoginSuccess) {//重定向到saved request
			WebUtils.redirectToSavedRequest(request, response, getSuccessUrl());
		}else {//重定向到真正的首页
			WebUtils.issueRedirect(request, response, getSuccessUrl());
		}
		return false;
	}
	
	@Override
	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request,
			ServletResponse response) {
		try {
			this.redirectToLogin(request, response);
		} catch (IOException e1) {
			log.error("重定向到登录页面异常",e1);
		}
		return false;
	}
	
	public void setAppId(String appId) {
		this.appId = appId;
	}

}
