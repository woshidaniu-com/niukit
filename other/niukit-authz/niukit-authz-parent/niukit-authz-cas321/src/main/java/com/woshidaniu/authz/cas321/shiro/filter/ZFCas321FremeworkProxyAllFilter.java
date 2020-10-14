package com.woshidaniu.authz.cas321.shiro.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.servlet.OncePerRequestFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * niutal框架代理所有cas过滤器,这个类存在的目的是为了减少配置config-shiro.xml中过滤器的个数，减少失误发生
 * @author 		：康康（1571）
 */
public class ZFCas321FremeworkProxyAllFilter extends OncePerRequestFilter implements InitializingBean{
	
	private static final Logger log = LoggerFactory.getLogger(ZFCas321FremeworkProxyAllFilter.class);
	
	private boolean enable = true;
	private String casServerLoginUrl;
	private String serverName;
	private String service;
	private boolean acceptAnyProxy;
	private String casServerUrlPrefix;
	private String failureUrl;
	private String successUrl;
	private boolean redirectToSaveRequestUri = false;
	
	protected ZFCas321LogoutFilter zfCas321LogoutFilter = new ZFCas321LogoutFilter();
	
	protected ZFCas321AuthenticationFilter zfCas321AuthenticationFilter = new ZFCas321AuthenticationFilter();
	
	protected ZFCas321TicketValidationFilter zfCas321TicketValidationFilter = new ZFCas321TicketValidationFilter();
	
	protected ZFCas321RequestWrapperFilter zfCas321RequestWrapperFilter = new ZFCas321RequestWrapperFilter();
	
	protected ZFCas321AssertionThreadLocalFilter zfCas321AssertionThreadLocalFilter = new ZFCas321AssertionThreadLocalFilter();
	
	protected ZFCas321Filter zfCas321Filter = new ZFCas321Filter();

	@Override
	public void afterPropertiesSet() throws Exception {
		
		{
			zfCas321AuthenticationFilter.setCasServerLoginUrl(casServerLoginUrl);
			zfCas321AuthenticationFilter.setServerName(serverName);
			zfCas321AuthenticationFilter.setService(service);
			zfCas321AuthenticationFilter.setRedirectToSaveRequestUri(this.redirectToSaveRequestUri);
			zfCas321AuthenticationFilter.afterPropertiesSet();
		}
		
		{
			zfCas321TicketValidationFilter.setCasServerUrlPrefix(casServerUrlPrefix);
			zfCas321TicketValidationFilter.setAcceptAnyProxy(acceptAnyProxy);
			zfCas321TicketValidationFilter.setService(service);
			zfCas321TicketValidationFilter.setServerName(serverName);
			zfCas321TicketValidationFilter.afterPropertiesSet();
		}
		
		{
			zfCas321RequestWrapperFilter.afterPropertiesSet();
		}
		
		{
			zfCas321AssertionThreadLocalFilter.afterPropertiesSet();
		}
		
		{
			zfCas321Filter.setFailureUrl(failureUrl);
			zfCas321Filter.setSuccessUrl(successUrl);
			zfCas321Filter.setRedirectToSaveRequestUri(this.redirectToSaveRequestUri);
			//Do not forget it
			zfCas321Filter.processPathConfig("/**", "");
			
			zfCas321Filter.afterPropertiesSet();
		}
		
		{
			log.info("enable:{}",this.enable);
		}
	}

	@Override
	protected void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain orig) throws ServletException, IOException {
		
		if(this.enable){
			
			log.trace("process request uri : {}",WebUtils.toHttp(request).getRequestURI().toString());
			
			List<Filter> filters = new ArrayList<Filter>();
			
			boolean isAuthenticated = SecurityUtils.getSubject().isAuthenticated();
			
			filters.add(zfCas321LogoutFilter);
			
			//已经登录，则不再需要重定向到cas服务地址
			if(!isAuthenticated){
				filters.add(zfCas321AuthenticationFilter);
			}
			
			//已经登录，则不再需要验证ticket
			if(!isAuthenticated){
				filters.add(zfCas321TicketValidationFilter);
			}
			
			filters.add(zfCas321RequestWrapperFilter);
			filters.add(zfCas321AssertionThreadLocalFilter);
			
			//已经登录，则不再需要验证ticket
			if(!isAuthenticated){
				filters.add(zfCas321Filter);
			}
			
			ProxiedFilterChain proxiedFilterChain = new ProxiedFilterChain(orig, filters);
			proxiedFilterChain.doFilter(request, response);
			
		}else{
			orig.doFilter(request, response);
		}
	}

	public void setCasServerLoginUrl(String casServerLoginUrl) {
		this.casServerLoginUrl = casServerLoginUrl;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public void setService(String service) {
		this.service = service;
	}

	public void setAcceptAnyProxy(boolean acceptAnyProxy) {
		this.acceptAnyProxy = acceptAnyProxy;
	}

	public void setCasServerUrlPrefix(String casServerUrlPrefix) {
		this.casServerUrlPrefix = casServerUrlPrefix;
	}

	public void setFailureUrl(String failureUrl) {
		this.failureUrl = failureUrl;
	}

	public void setSuccessUrl(String successUrl) {
		this.successUrl = successUrl;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public void setRedirectToSaveRequestUri(boolean redirectToSaveRequestUri) {
		this.redirectToSaveRequestUri = redirectToSaveRequestUri;
	}
}
