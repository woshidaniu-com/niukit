/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.idstar.shiro.filter;

import java.io.File;
import java.io.IOException;

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

import com.woshidaniu.authz.idstar.shiro.token.IDstarAuthenticationToken;
import com.woshidaniu.authz.idstar.utils.IDstarSsoUtils;
import com.woshidaniu.basicutils.StringUtils;
import com.woshidaniu.io.utils.ResourceUtils;
import com.woshidaniu.web.utils.WebRequestUtils;

/**
 * @className	： IDstarSsoShiroFilter
 * @description	： IDstarSsoShiroFilter
 * @author 		：康康（1571）
 * @date		： 2018年5月10日 下午4:18:55
 * @version 	V1.0
 */
public class IDstarSsoShiroFilter extends AuthenticatingFilter {
	
	private static final Logger log = LoggerFactory.getLogger(IDstarSsoShiroFilter.class);
	
	//登录成功后是否重定向到之前保存的请求地址
	private boolean redirectSavedRequestAfterLoginSuccess = false;
	
	// 客户端配置文件路径
	private String clientPropertiesLocation;
	// 客户端配置文件绝对路径
	private String clientPropertiesPath;
	
	@Override
	protected void onFilterConfigSet() throws Exception {
		super.onFilterConfigSet();
		if(StringUtils.isEmpty(this.clientPropertiesLocation)) {
			throw new IllegalStateException("clientPropertiesLocation cant't be null");			
		}
		
		try {
			File file = ResourceUtils.getResourceAsFile(this.clientPropertiesLocation);
			if(file.isFile()) {
				this.clientPropertiesPath = file.getAbsolutePath();
			}else {
				throw new IllegalStateException("clientPropertiesLocation not be a file");	
			}
		}catch(Exception e) {
			throw new IllegalStateException(e);			
		}
	}

	@Override
	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
		
		String token = IDstarSsoUtils.getTokenInCookie(WebUtils.toHttp(request), WebUtils.toHttp(response));
		// 登录IP
		String host = WebRequestUtils.getRemoteAddr((HttpServletRequest) request);
		// 登录账号ID
		String uid = IDstarSsoUtils.getUidByToken(this.clientPropertiesPath, token);
		// 构造Token对象
		AuthenticationToken authToken = new IDstarAuthenticationToken(token, uid, host);
		
		return authToken;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		
		String token = null;
		try {			
			// 第一步：获取Cookie中的Token信息
			token = IDstarSsoUtils.getTokenInCookie(WebUtils.toHttp(request), WebUtils.toHttp(response));
			if(log.isDebugEnabled()) {
				log.debug("Get Token ["+ token + "] In Cookie ");					
			}
		}catch(IOException e) {
			log.error("从Cookie中获取Token异常",e);
			return false;
		}
		if (StringUtils.isEmpty(token)) {
			this.redirectToLogin(request, response);
			return false;
		}else {
			return this.executeLogin(request, response);
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
		
		if(this.redirectSavedRequestAfterLoginSuccess) {//重定向到saved request
			WebUtils.redirectToSavedRequest(request, response, getSuccessUrl());
		}else {//重定向到真正的首页
			WebUtils.issueRedirect(request, response, getSuccessUrl());
		}
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
				log.debug("idstar 认证登录失败，重定向到登录页面:{}",this.getLoginUrl());
			}
			this.redirectToLogin(request, response);
		} catch (IOException e1) {
			log.error("登录失败时处理发生异常", e1);
		}
		return false;
	}

	public void setClientPropertiesLocation(String clientPropertiesLocation) {
		this.clientPropertiesLocation = clientPropertiesLocation;
	}
}
