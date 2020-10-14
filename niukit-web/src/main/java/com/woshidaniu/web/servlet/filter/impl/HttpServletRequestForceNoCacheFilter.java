package com.woshidaniu.web.servlet.filter.impl;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.woshidaniu.web.servlet.filter.OncePerRequestFilter;

/**
 * 
 *@类名称	: HttpServletRequestForceNoCacheFilter.java
 *@类描述	：设置response,使 Browser 不缓存页面的过滤器
 *@创建人	：kangzhidong
 *@创建时间	：Mar 8, 2016 9:02:44 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class HttpServletRequestForceNoCacheFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(ServletRequest request,
			ServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;


		/*<meta http-equiv="Pragma" http-equiv="no-cache" />
		<meta http-equiv="Cache-Control" http-equiv="no-cache, must-revalidate" />
		<meta http-equiv="Expires" http-equiv="0" />*/
		
		//无缓存 
		//httpResponse.setHeader("Pragma", "No-cache");
		//httpResponse.addHeader( "Cache-Control", "must-revalidate" );
		//httpResponse.addHeader( "Cache-Control", "no-cache" );
		//httpResponse.addHeader( "Cache-Control", "no-store" );
		//httpResponse.setDateHeader("Expires", 0);
		
		httpResponse.setHeader("Pragma", "no-cache");	// HTTP/1.0 
		httpResponse.addHeader( "Cache-Control", "must-revalidate" );	// HTTP/1.1
		httpResponse.addHeader( "Cache-Control", "no-cache" );
		httpResponse.addHeader( "Cache-Control", "no-store" );
		httpResponse.setDateHeader("Expires", -1);
		
		filterChain.doFilter(request, response);
	}

}
