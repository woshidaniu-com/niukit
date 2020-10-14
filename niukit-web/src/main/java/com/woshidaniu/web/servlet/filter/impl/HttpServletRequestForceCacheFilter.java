package com.woshidaniu.web.servlet.filter.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.time.DateUtils;

import com.woshidaniu.basicutils.StringUtils;
import com.woshidaniu.web.servlet.filter.OncePerRequestFilter;
/**
 * 
 *@类名称	: HttpServletRequestForceCacheFilter.java
 *@类描述	：设置response,使 Browser 缓存页面的过滤器
 *@创建人	：kangzhidong
 *@创建时间	：Mar 8, 2016 9:02:35 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class HttpServletRequestForceCacheFilter extends OncePerRequestFilter {

	private String defaultPattern = "EEE, dd MMM yyyy HH:mm:ss 'GMT'";
	private String expiresDatePattern;
	private SimpleDateFormat sdf;
	private int cacheSeconds;

	public HttpServletRequestForceCacheFilter() {
		sdf = new SimpleDateFormat(defaultPattern, Locale.US);
	}

	public void setCacheSeconds(int cacheSeconds) {
		this.cacheSeconds = cacheSeconds;
	}

	public void setExpiresDatePattern(String expiresDatePattern) {
		this.expiresDatePattern  =  StringUtils.getSafeStr(expiresDatePattern,defaultPattern);
		sdf = new SimpleDateFormat(this.expiresDatePattern, Locale.US);
	}

	@Override
	protected void onFilterConfigSet(FilterConfig filterConfig) throws Exception {
		setExpiresDatePattern(filterConfig.getInitParameter("expiresDatePattern"));
		setCacheSeconds(StringUtils.getSafeInt(filterConfig.getInitParameter("cacheSeconds"),"0"));
	}
	
	@Override
	protected void doFilterInternal(ServletRequest request,
			ServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		//设置有限时间的缓存 
		httpResponse.setHeader("Pragma", "Pragma"); // HTTP/1.0 
		httpResponse.setHeader("Cache-Control", "max-age=".concat(String.valueOf(cacheSeconds)));// HTTP/1.1 
		httpResponse.setHeader("Expires", sdf.format(DateUtils.addSeconds(new Date(), cacheSeconds)));
		
		/*<meta http-equiv="Pragma" http-equiv="no-cache" />
		<meta http-equiv="Cache-Control" http-equiv="no-cache, must-revalidate" />
		<meta http-equiv="Expires" http-equiv="0" />*/
		
		//无缓存 
		//httpResponse.setHeader("Pragma", "No-cache");
		//httpResponse.addHeader( "Cache-Control", "must-revalidate" );
		//httpResponse.addHeader( "Cache-Control", "no-cache" );
		//httpResponse.addHeader( "Cache-Control", "no-store" );
		//httpResponse.setDateHeader("Expires", 0);
		/*
		httpResponse.setHeader("Pragma", "no-cache");	// HTTP/1.0 
		httpResponse.addHeader( "Cache-Control", "must-revalidate" );	// HTTP/1.1
		httpResponse.addHeader( "Cache-Control", "no-cache" );
		httpResponse.addHeader( "Cache-Control", "no-store" );
		httpResponse.setDateHeader("Expires", -1);*/
		
		filterChain.doFilter(request, response);
	}
}
