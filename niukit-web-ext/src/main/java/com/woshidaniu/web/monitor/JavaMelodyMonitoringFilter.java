package com.woshidaniu.web.monitor;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * 
 *@类名称	: RequestMonitoringFilter.java
 *@类描述	：Javamelody监控安全过滤，扩展相关过滤逻辑
 *@创建人	：kangzhidong
 *@创建时间	：Mar 18, 2016 2:12:14 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public abstract class JavaMelodyMonitoringFilter extends net.bull.javamelody.MonitoringFilter{
	
	private FilterConfig config;
	
	@Override
	public void init(FilterConfig config) throws ServletException {
		super.init(config);
		this.config = config;
	}
	
	
	public FilterConfig getFilterConfig() {
		return config;
	}
	
	protected final String getFilterName() {
		return (this.config != null ? this.config.getFilterName() : this.getClass().getSimpleName());
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		//判断是否是在方法监控平台
		if (httpRequest.getRequestURI().equals(getMonitoringUrl(httpRequest))) {
			this.doFilterInternal(httpRequest,httpResponse,chain);
		}
		super.doFilter(request, response, chain);
	}

	protected abstract void doFilterInternal(HttpServletRequest httpRequest, HttpServletResponse httpResponse, FilterChain filterChain) throws ServletException, IOException;

	
	@Override
	public void destroy() {
		super.destroy();
	}
}
