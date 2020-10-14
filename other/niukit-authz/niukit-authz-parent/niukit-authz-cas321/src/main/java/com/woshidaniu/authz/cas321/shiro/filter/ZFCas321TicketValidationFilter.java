/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.cas321.shiro.filter;


import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.web.servlet.OncePerRequestFilter;
import org.jasig.cas.client.validation.Cas20ProxyReceivingTicketValidationFilter;
import org.jasig.cas.client.validation.Cas20ProxyTicketValidator;
import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author 		：康康（1571）
 */
public class ZFCas321TicketValidationFilter extends OncePerRequestFilter implements InitializingBean{
	
	private static final Logger log = LoggerFactory.getLogger(ZFCas321TicketValidationFilter.class);
	
	private Cas20ProxyReceivingTicketValidationFilter delegate = new Cas20ProxyReceivingTicketValidationFilter();
	
	private String serverName;
	private String service;
	private boolean acceptAnyProxy;
	private String casServerUrlPrefix;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		
		this.delegate.setIgnoreInitConfiguration(true);
		
		this.delegate.setServerName(this.serverName);
		this.delegate.setService(service);
		
		log.info("init zfCas321TicketValidationFilter start");
		
		log.info("serverName[{}]",this.serverName);
		log.info("service[{}]",this.service);
		log.info("casServerUrlPrefix[{}]",this.casServerUrlPrefix);
		log.info("acceptAnyProxy[{}]",this.acceptAnyProxy);
		
		if(this.acceptAnyProxy){
			this.delegate.setTicketValidator(new Cas20ProxyTicketValidator(this.casServerUrlPrefix));
		}else{
			this.delegate.setTicketValidator(new Cas20ServiceTicketValidator(this.casServerUrlPrefix));
		}
		
		log.info("init zfCas321TicketValidationFilter end");
	}
	
	@Override
	protected void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
		
		this.delegate.doFilter(request, response, chain);
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
}
