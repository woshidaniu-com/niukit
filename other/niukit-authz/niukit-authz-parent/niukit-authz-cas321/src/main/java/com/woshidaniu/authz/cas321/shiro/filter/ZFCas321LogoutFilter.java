package com.woshidaniu.authz.cas321.shiro.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.jasig.cas.client.session.SingleSignOutFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

public class ZFCas321LogoutFilter implements Filter,InitializingBean{
	
	private static final Logger log = LoggerFactory.getLogger(ZFCas321LogoutFilter.class);

	private SingleSignOutFilter delgateFilter = new SingleSignOutFilter();

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		this.delgateFilter.doFilter(request, response, chain);
	}

	@Override
	public void destroy() {
		
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		log.info("init zfCas321LogoutFilter");
	}
}
