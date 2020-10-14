/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.zfca.shiro.spring;

import javax.servlet.ServletContext;

import org.apache.shiro.web.filter.authc.AuthenticationFilter;
import org.springframework.beans.BeansException;

import com.woshidaniu.basicutils.StringUtils;
import com.woshidaniu.basicutils.URLUtils;
import com.woshidaniu.shiro.filter.session.SessionControlFilter;
import com.woshidaniu.shiro.spring.ZFShiroFilterFactoryBean;
import com.woshidaniu.web.context.WebContext;
import com.woshidaniu.niuca.tp.cas.client.ZfssoConfig;
import com.woshidaniu.niuca.tp.cas.client.ZfssoReadConfig;

/**
 * 
 * @className	： ZFShiroSsoFilterFactoryBean
 * @description	： 实现Shiro登录与Cas单点登录的整合
 * @author 		：康康（1571）
 * @date		： 2018年4月24日 上午8:43:53
 * @version 	V1.0
 */
public class ZFShiroSsoFilterFactoryBean extends ZFShiroFilterFactoryBean {

	protected String protocol = "http";

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if (!StringUtils.hasText(ZfssoConfig.casurl)) {
			ServletContext servletContext = WebContext.getServletContext();
			String dbpagh = servletContext.getRealPath("/WEB-INF/zfssoconfig.properties");
			new ZfssoReadConfig(dbpagh);
		}
		return super.postProcessBeforeInitialization(bean, beanName);
	}

	@Override
	public String getCasLoginUrl() {
		return getCasLoginUrl(getSuccessUrl());
	}
	
	@Override
	public boolean isCasLogin() {
		return StringUtils.equals(ZfssoConfig.usezfca, "1");
	}
	
	@Override
	public String getCasLoginUrl(AuthenticationFilter filter) {
		
		if( filter instanceof SessionControlFilter){
			
			/*
			 * 格式如： <br/>
			 *       http://10.71.31.1:8043/zfca/logout?service=http://10.71.33.167:8080/niutal-web-demo/xtgl/login/slogin.zf?kickout=1<br/>
			 */
			
			StringBuilder casRedirectUrl = new StringBuilder(ZfssoConfig.casurl);
			if (!ZfssoConfig.casurl.endsWith("/")) {
				casRedirectUrl.append("/");
			}
			// Cas注销地址
			casRedirectUrl.append("logout?service=");
			
			// 登出的重定向地址：用于重新回到业务系统登录界面
			StringBuilder callbackUrl = new StringBuilder(getProtocol());
			callbackUrl.append("://").append(ZfssoConfig.ywxtservername)
					.append(WebContext.getServletContext().getContextPath()).append(filter.getLoginUrl());
			casRedirectUrl.append(URLUtils.escape(callbackUrl.toString()));
			
			return casRedirectUrl.toString();
		}
		
		return getCasLoginUrl(filter.getSuccessUrl());
		 
	}
	
	/**
	 * 
	 * @description	： 获得cas登录地址
	 * @author 		： 大康（743）
	 * @date 		：2017年8月29日 下午4:33:36
	 * @param successUrl
	 * 格式如： <br/>
	 *       http://10.71.31.1:8043/zfca/login?service=http://10.71.33.167:8080/niutal-web-demo/xtgl/login/slogin.zf<br/>
	 * @return
	 */
	public String getCasLoginUrl(String successUrl) {
		
		StringBuilder casRedirectUrl = new StringBuilder(ZfssoConfig.casurl);
		if (!ZfssoConfig.casurl.endsWith("/")) {
			casRedirectUrl.append("/");
		}
		// Cas登录地址
		casRedirectUrl.append("login?service=");

		// 登出的重定向地址：用于重新回到业务系统登录界面
		StringBuilder callbackUrl = new StringBuilder(getProtocol());
		callbackUrl.append("://").append(ZfssoConfig.ywxtservername)
				.append(WebContext.getServletContext().getContextPath()).append(successUrl);

		casRedirectUrl.append(URLUtils.escape(callbackUrl.toString()));

		return casRedirectUrl.toString();
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

}