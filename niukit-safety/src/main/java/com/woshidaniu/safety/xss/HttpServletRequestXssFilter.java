package com.woshidaniu.safety.xss;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;

import com.woshidaniu.safety.SafetyParameter;
import com.woshidaniu.safety.SafetyParameters;
import com.woshidaniu.safety.xss.http.HttpServletXSSRequestWrapper;
import com.woshidaniu.web.servlet.filter.AbstractPathMatchFilter;


/**
 * 
 * @className	： HttpServletRequestXssFilter
 * @description	： XSS(Cross Site Scripting)，即跨站脚本攻击请求过滤
 * @author 		：大康（743）
 * @date		： 2017年9月14日 下午5:37:57
 * @version 	V1.0
 */
public class HttpServletRequestXssFilter extends AbstractPathMatchFilter {
	
	protected PolicyFactory DEFAULT_POLICY = new HtmlPolicyBuilder().toFactory();
	
	/**Xss检查策略工厂*/
	protected PolicyFactory policy = DEFAULT_POLICY;
	/**需要进行Xss检查的Header*/
	protected String[] policyHeaders = null;
	
	@Override
	protected void onFilterConfigSet(FilterConfig filterConfig) throws Exception {
		super.onFilterConfigSet(filterConfig);
		//初始化安全组件参数
		SafetyParameters.initialize(filterConfig);
		//解析需要进行Xss检查的Header
		this.policyHeaders = SafetyParameters.getStringArray(filterConfig.getFilterName() ,SafetyParameter.SAFETY_XSS_POLICY_HEADERS);
	}
	
	@Override
	public void doFilterInternal(ServletRequest request,
			ServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		
		if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
			throw new ServletException( "just supports HTTP requests");
		}
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		filterChain.doFilter(new HttpServletXSSRequestWrapper(getPolicy(), getPolicyHeaders(), httpRequest), httpResponse);
		
	}

	public PolicyFactory getPolicy() {
		return policy;
	}

	public void setPolicy(PolicyFactory policy) {
		this.policy = policy;
	}

	public String[] getPolicyHeaders() {
		return policyHeaders;
	}

	public void setPolicyHeaders(String[] policyHeaders) {
		this.policyHeaders = policyHeaders;
	}
	

}
