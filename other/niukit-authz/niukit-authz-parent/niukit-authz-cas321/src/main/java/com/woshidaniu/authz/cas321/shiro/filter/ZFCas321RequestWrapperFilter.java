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
import org.jasig.cas.client.util.HttpServletRequestWrapperFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author 		：康康（1571）
 */
public class ZFCas321RequestWrapperFilter extends OncePerRequestFilter implements InitializingBean{
	
	private static final Logger log = LoggerFactory.getLogger(ZFCas321RequestWrapperFilter.class);
	
	private HttpServletRequestWrapperFilter delegate = new HttpServletRequestWrapperFilter();

	@Override
	protected void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
		this.delegate.doFilter(request, response, chain);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		log.info("init zfCas321RequestWrapperFilter");
	}
}
