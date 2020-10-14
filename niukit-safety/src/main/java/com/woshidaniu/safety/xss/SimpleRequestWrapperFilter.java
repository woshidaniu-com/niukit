/**
 * <p>Copyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.safety.xss;

import java.io.IOException;
import java.lang.reflect.Constructor;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleRequestWrapperFilter implements Filter{
	
	private static final Logger log = LoggerFactory.getLogger(SimpleRequestWrapperFilter.class);
	
	protected Class<? extends HttpServletRequestWrapper> clazz;
	
	protected Constructor<?> constructor;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		String className = filterConfig.getInitParameter("HttpServletRequestWrapperClassName");
		
		if(className == null || className.equals("")) {
			throw new ServletException("initParameter[HttpServletRequestWrapperClassName] can't be null");
		}
		
		Class<?> loaded_class = null;
		try {
			loaded_class = Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new ServletException("can't find class:"+className,e);
		}
		
		if(loaded_class.isAssignableFrom(HttpServletRequestWrapper.class)) {
			throw new ServletException("loaded_class["+ loaded_class.getName() +"] is not assignable from HttpServletRequestWrapper");
		}
		
		clazz = (Class<? extends HttpServletRequestWrapper>) loaded_class;
		try {
			constructor = clazz.getConstructor(HttpServletRequest.class);
		} catch (SecurityException e) {
			throw new ServletException(e);
		} catch (NoSuchMethodException e) {
			throw new ServletException(e);
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
			throw new ServletException( "just supports HTTP requests");
		}
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		HttpServletRequest originHttpRequest = httpRequest;
		
		try {
			httpRequest = (HttpServletRequestWrapper) this.constructor.newInstance(originHttpRequest);
		} catch (Exception e) {
			log.error("wrapper request error: "+e.getMessage(),e);
		}finally {
			chain.doFilter(httpRequest, httpResponse);
		}
	}

	@Override
	public void destroy() {
		
	}

}
