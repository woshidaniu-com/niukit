package com.woshidaniu.safety.xss;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.woshidaniu.safety.SafetyParameters;
import com.woshidaniu.safety.xss.http.EscapedRequestWrapper;
import com.woshidaniu.web.servlet.filter.AbstractPathMatchFilter;


/**
 * 
 * @className	： HttpServletRequestEscapedFilter
 * @description	： 基于StringEscapeUtils.escapeHtml4()方法的XSS(Cross Site Scripting)，即跨站脚本攻击请求过滤
 * @author 		：大康（743）
 * @date		： 2017年9月14日 下午12:19:27
 * @version 	V1.0
 */
public class HttpServletRequestEscapedFilter extends AbstractPathMatchFilter {
	
	@Override
	protected void onFilterConfigSet(FilterConfig filterConfig) throws Exception {
		super.onFilterConfigSet(filterConfig);
		//初始化安全组件参数
		SafetyParameters.initialize(filterConfig);
	}

	@Override
	public void doFilterInternal(ServletRequest request,
			ServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		
		if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
			throw new ServletException( "just supports HTTP requests");
		}
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		//执行下个责任链：将封装后的请求传递下去
		filterChain.doFilter(new EscapedRequestWrapper(httpRequest), httpResponse);
		
	}

}
