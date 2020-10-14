/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.zfca.shiro.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basicutils.StringUtils;
import com.woshidaniu.shiro.filter.ZFLogoutFilter;
import com.woshidaniu.shiro.token.LoginType;
import com.woshidaniu.web.Parameter;
import com.woshidaniu.web.Parameters;
import com.woshidaniu.web.context.WebContext;
import com.woshidaniu.niuca.tp.cas.client.ZfssoConfig;
import com.woshidaniu.niuca.tp.cas.client.ZfssoReadConfig;

/**
 * 
 * @className	： ZFShiroSsoLogoutFilter
 * @description	：  实现Shiro登录注销与Cas单点登录注销的整合
 * @author 		：康康（1571）
 * @date		： 2018年4月24日 上午8:43:25
 * @version 	V1.0
 */
public class ZFSsoLogoutShiroFilter extends ZFLogoutFilter {
	
	private static final Logger log = LoggerFactory.getLogger(ZFSsoLogoutShiroFilter.class);

	protected String protocol = "http";
	
	@Override
	protected void onFilterConfigSet() throws Exception {
		super.onFilterConfigSet();
		if (!StringUtils.hasText(ZfssoConfig.casurl)) {
			ServletContext servletContext = getFilterConfig().getServletContext();
			String dbpagh = servletContext.getRealPath("/WEB-INF/zfssoconfig.properties");
			new ZfssoReadConfig(dbpagh);
		}
	}
	
	@Override
	public boolean isCasLogin(Subject subject) {
		// 登录成功;记录登录方式标记；1：页面登录；2：单点登录；3：票据登录（通过握手秘钥等参数认证登录）
        HttpSession session =  WebContext.getSession(); 
        String loginType = String.valueOf(session.getAttribute(Parameters.getGlobalString(Parameter.LOGIN_TYPE_KEY)));
        if(LoginType.INNER.getKey().equals(loginType)){
            return false;
        }
        return StringUtils.equals(ZfssoConfig.usezfca, "1");
	}
	
	
	
	@Override
	public void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		
		if(log.isDebugEnabled()) {
			HttpServletRequest httpServletRequest = (HttpServletRequest)request;
			log.debug("doFilter url:{}", httpServletRequest.getRequestURI());
		}
		super.doFilterInternal(request, response, chain);
	}

	/**
	 * 
	 * @description ： 构造单点登出请求地址
	 * @author ：大康（743）
	 * @date ：2017年8月29日 上午9:34:08
	 * @param request
	 * @param response
	 * @return 单点登出请求地址;<br/>
	 *         格式如： <br/>
	 *         http://10.71.31.1:8043/zfca/logout?service=http://10.71.33.167:8080/niutal-web-demo/ssoLogout<br/>
	 */
	@Override
	public String getCasRedirectUrl(ServletRequest request, ServletResponse response) {

		StringBuilder casRedirectUrl = new StringBuilder(ZfssoConfig.casurl);
		if (!ZfssoConfig.casurl.endsWith("/")) {
			casRedirectUrl.append("/");
		}
		// Cas注销地址
		casRedirectUrl.append("logout?service=");

		// 登出的重定向地址：用于重新回到业务系统登录界面
		StringBuilder callbackUrl = new StringBuilder(getProtocol());
		callbackUrl.append("://").append(ZfssoConfig.ywxtservername)
				.append(request.getServletContext().getContextPath()).append(getRedirectUrl());

		casRedirectUrl.append(callbackUrl.toString());

		if(log.isDebugEnabled()) {
			log.debug("logout success , get cas redirect url:{}",casRedirectUrl);
		}
		
		return casRedirectUrl.toString();
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

}
