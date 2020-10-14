/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.cas.shiro;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.web.servlet.AdviceFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.authz.cas.config.ProxyFilterConfig;
import com.woshidaniu.authz.cas.config.ShiroCasProperties;

/**
 * 
 * @className	： AbstractCasPropertiesNeedProxyShiroFilter
 * @description	： 抽象的代理过滤器，集成进shiro过滤器链的基础
 * @author 		：康康（1571）
 * @date		： 2018年5月3日 上午9:30:59
 * @version 	V1.0
 */
public abstract class AbstractCasPropertiesNeedProxyShiroFilter extends AdviceFilter{

	protected Logger log = LoggerFactory.getLogger(AbstractCasPropertiesNeedProxyShiroFilter.class);

	protected Filter delegate = null;
	
	protected ShiroCasProperties casProperties;
	/**
	 * @description	： 创建委托并初始化代理配置
	 * @author 		： 康康（1571）
	 * @date 		：2018年4月24日 上午10:37:10
	 * @param proxyFilterConfig
	 * @throws ServletException
	 */
	abstract protected void doInitDelegateAndConfigLazy(ProxyFilterConfig proxyFilterConfig)throws ServletException;
    
	protected void doInitLazy(ServletRequest request) throws ServletException {
		
		//被代理对象Filter的初始化
		if(this.delegate == null) {
			ProxyFilterConfig proxyFilterConfig = null;
			if(filterConfig == null) {
				proxyFilterConfig = new ProxyFilterConfig(request.getServletContext(),this.getName());
				this.filterConfig = proxyFilterConfig;
			}else {				
				 proxyFilterConfig = (ProxyFilterConfig)filterConfig;				
			}
			this.doInitDelegateAndConfigLazy(proxyFilterConfig);
			this.delegate.init(this.getFilterConfig());		
			
			if (log.isDebugEnabled()) {
				String delegateName = this.delegate.getClass().getSimpleName();
				log.debug("init delegate {}", delegateName);
				StringBuilder sb = new StringBuilder("config:\n");
				Enumeration<String> enumer = this.getFilterConfig().getInitParameterNames();
				while (enumer.hasMoreElements()) {
					String key = enumer.nextElement();
					String value = this.getFilterConfig().getInitParameter(key);
					sb.append("key=").append(key).append(",value=").append(value).append("\n");
				}
				log.debug(sb.toString());
			}
		}
	}
	
	@Override
	public void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		this.doInitLazy(request);
		this.doDelegateFilter(request, response, chain);
	}

	public void doDelegateFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if(this.delegate != null && this.isEnabled()) {
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
