package com.woshidaniu.web.servlet.filter.impl;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.woshidaniu.web.context.WebContext;
import com.woshidaniu.web.servlet.filter.OncePerRequestFilter;

/**
 * 
 *@类名称		： HttpServletRequestBindFilter.java
 *@类描述		：WebContext对象绑定过滤器
 *@创建人		：kangzhidong
 *@创建时间	：2017年8月25日 下午5:51:24
 *@修改人		：
 *@修改时间	：
 *@版本号		: v1.0
 */
public class HttpServletRequestBindFilter extends OncePerRequestFilter {
	
	@Override
	public void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		WebContext.bindRequest(request);
		WebContext.bindResponse(response);
		chain.doFilter(request, response);
	}
	
}
