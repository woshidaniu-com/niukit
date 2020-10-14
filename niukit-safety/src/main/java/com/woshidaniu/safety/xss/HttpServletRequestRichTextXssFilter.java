package com.woshidaniu.safety.xss;

import java.io.IOException;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.woshidaniu.safety.SafetyParameters;
import com.woshidaniu.safety.xss.factory.AntiSamyBoundFactory;
import com.woshidaniu.safety.xss.factory.AntiSamyProxy;
import com.woshidaniu.safety.xss.http.HttpServletRichTextXSSRequestWrapper;
import com.woshidaniu.web.servlet.filter.AbstractPathMatchFilter;


/**
 * 
 * @className	： HttpServletRequestRichTextXssFilter
 * @description	： RichText XSS(Cross Site Scripting)，即跨站脚本攻击请求过滤
 * @author 		：大康（743）
 * @date		： 2017年9月14日 下午5:21:18
 * @version 	V1.0
 */
public class HttpServletRequestRichTextXssFilter extends AbstractPathMatchFilter {
	
	protected AntiSamyBoundFactory antiSamyFactory = AntiSamyBoundFactory.getInstance();
	
	@Override
	protected void onFilterConfigSet(FilterConfig filterConfig) throws Exception {
		super.onFilterConfigSet(filterConfig);
		//初始化安全组件参数
		SafetyParameters.initialize(filterConfig);
		//初始化AntiSamy工厂
		antiSamyFactory.initFactory(filterConfig);
	}
	
	@Override
	public void doFilterInternal(ServletRequest request,
			ServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		
		if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
			throw new ServletException( "just supports HTTP requests");
		}
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		if (this.antiSamyFactory.matches(httpRequest)) {
			//根据请求获取响应的
			AntiSamyProxy antiSamyProxy = antiSamyFactory.getAntiSamyForRequest(httpRequest);
			filterChain.doFilter(new HttpServletRichTextXSSRequestWrapper(antiSamyProxy, httpRequest), httpResponse);
		} else {
			filterChain.doFilter(request,response);
		}
	}

	public void setDefaultPolicy(String defaultPolicy) {
		antiSamyFactory.setDefaultPolicy(defaultPolicy);
	}
	
	public void setScanType(int scanType) {
		antiSamyFactory.setScanType(scanType);
	}

	public void setIncludePatterns(String[] includePatterns) {
		antiSamyFactory.setIncludePatterns(includePatterns);
	}

	public void setExcludePatterns(String[] excludePatterns) {
		antiSamyFactory.setExcludePatterns(excludePatterns);
	}

	public void setPolicyHeaders(String[] policyHeaders) {
		antiSamyFactory.setPolicyHeaders(policyHeaders);
	}
	
	public void setPolicyDefinitions(String policyDefinitions) {
		antiSamyFactory.setPolicyDefinitions(policyDefinitions);
	}
	
	public void setPolicyMappings(Map<String, String> policyMappings) {
		antiSamyFactory.setPolicyMappings(policyMappings);
	}

	public void destroy() {
		super.destroy();
		antiSamyFactory.destroy();
	}

}
