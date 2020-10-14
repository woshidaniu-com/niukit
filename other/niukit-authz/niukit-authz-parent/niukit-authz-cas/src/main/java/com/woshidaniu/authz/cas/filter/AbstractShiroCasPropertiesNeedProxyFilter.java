/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.cas.filter;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.authz.cas.config.ProxyFilterConfig;
import com.woshidaniu.authz.cas.config.ShiroCasProperties;

/**
 * 
 * @className	： AbstractProxyLifecycleFilter
 * @description	： 
 * 				创建并代理Filter，用于创建我们的代理Filter，子类实现doCreateDelegateAndInitConfig，完成配置FilterConfig
 * @author 		：康康（1571）
 * @date		： 2018年4月24日 上午10:22:05
 * @version 	V1.0
 */
public abstract class AbstractShiroCasPropertiesNeedProxyFilter implements Filter {

	protected Logger log = LoggerFactory.getLogger(AbstractShiroCasPropertiesNeedProxyFilter.class);

	protected Filter delegate = null;
	
	protected ShiroCasProperties casProperties;
	/**
	 * @description	： 创建委托并初始化代理配置
	 * @author 		： 康康（1571）
	 * @date 		：2018年4月24日 上午10:37:10
	 * @param proxyFilterConfig
	 * @throws ServletException
	 */
	abstract protected void doInitDelegateAndConfig(ProxyFilterConfig proxyFilterConfig)throws ServletException;
	
	/**
	 * @description	： 是否启用被代理Filter
	 * @author 		： 康康（1571）
	 * @date 		：2018年4月24日 上午10:36:49
	 * @return
	 */
	abstract protected boolean isEnable();
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
		ProxyFilterConfig proxyFilterConfig = new ProxyFilterConfig(filterConfig);
		this.doInitDelegateAndConfig(proxyFilterConfig);
		//被代理对象Filter的初始化
		if(this.delegate != null && this.isEnable()) {
			String delegateName = this.delegate.getClass().getSimpleName();
			if(log.isDebugEnabled()) {
				log.debug("init delegate {}",delegateName);
				StringBuilder sb = new StringBuilder("config:\n");
				Enumeration<String> enumer = proxyFilterConfig.getInitParameterNames();
				while(enumer.hasMoreElements()) {
					String key = enumer.nextElement();
					String value = proxyFilterConfig.getInitParameter(key);
					sb.append("key=").append(key).append(",value=").append(value).append("\n");
				}
				log.debug(sb.toString());
			}
			this.delegate.init(proxyFilterConfig);			
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if(this.delegate != null && this.isEnable()) {
			if(log.isDebugEnabled()) {
				HttpServletRequest httpServletRequest  =(HttpServletRequest)request;
				String delegateName = this.delegate.getClass().getSimpleName();
				log.debug("delegate {} doFilter,url:{}",delegateName,httpServletRequest.getRequestURL().toString());
			}
			this.delegate.doFilter(request, response, chain);
		}
	}
	
	public void setCasProperties(ShiroCasProperties casProperties) {
		this.casProperties = casProperties;
	}

	@Override
	public void destroy() {

		if (this.delegate != null) {
			this.delegate.destroy();
		}
	}
}
