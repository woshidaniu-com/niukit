 package com.woshidaniu.web.servlet.filter.impl;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.woshidaniu.web.servlet.filter.OncePerRequestFilter;
import com.woshidaniu.web.servlet.http.HttpServletHtmlFilterRequestWrapper;

 /**
  * 
  *@类名称	: HttpServletRequestHtmlFilter.java
  *@类描述	：使用Decorator模式包装request对象，实现html标签转义功能
  *@创建人	：kangzhidong
  *@创建时间：Mar 8, 2016 9:02:53 AM
  *@修改人	：
  *@修改时间：
  *@版本号	:v1.0
  */
public class HttpServletRequestHtmlFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(ServletRequest request,
			ServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		HttpServletHtmlFilterRequestWrapper myrequest = new HttpServletHtmlFilterRequestWrapper(httpRequest);
		filterChain.doFilter(myrequest, httpResponse);

	}

}


