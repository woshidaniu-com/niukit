package com.woshidaniu.ui.v5.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class AccessControllAllowFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// To change body of implemented methods use File | Settings | File
		// Templates.
	}

	@Override
	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
		//在某域名下使用Ajax向另一个域名下的页面请求数据，会遇到跨域问题。另一个域名必须在response中添加 Access-Control-Allow-Origin 的header，才能让前者成功拿到数据。
		httpResponse.setHeader("Access-Control-Allow-Origin", "*");
		httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
		httpResponse.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		httpResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
		
		httpResponse.setHeader("X-Frame-Options", "SAMEORIGIN");
		
		filterChain.doFilter(servletRequest, servletResponse);
		// To change body of implemented methods use File | Settings | File
		// Templates.
	}

	@Override
	public void destroy() {
		// To change body of implemented methods use File | Settings | File
		// Templates.
	}
}
