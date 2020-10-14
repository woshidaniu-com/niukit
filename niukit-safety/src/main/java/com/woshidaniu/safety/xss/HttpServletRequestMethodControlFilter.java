/**
 * 
 */
package com.woshidaniu.safety.xss;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basicutils.StringUtils;
import com.woshidaniu.web.servlet.filter.AbstractAccessControlFilter;
import com.woshidaniu.web.utils.WebUtils;

/**
 * <p>
 *   <h3>niutal框架<h3>
 *   说明：控制HTTP请求方法
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年7月20日上午10:39:28
 */
public class HttpServletRequestMethodControlFilter extends AbstractAccessControlFilter {
	
	private static final Logger log = LoggerFactory.getLogger(HttpServletRequestMethodControlFilter.class);
	/**
	 * 应许访问的HTTP方法
	 */
	private Set<String> allowedHTTPMethodSet = new HashSet<String>(4);

	public String[] getAllowedHTTPMethods() {
		String[] array = new String[allowedHTTPMethodSet.size()];
		array = this.allowedHTTPMethodSet.toArray(array);
		return array;
	}

	public void setAllowedHTTPMethods(String[] allowedHTTPMethods) {
		for(String s : allowedHTTPMethods) {
			if(StringUtils.isNotEmpty(s)) {
				allowedHTTPMethodSet.add(s.toUpperCase());
			}
		}
	}
	
	protected void onFilterConfigSet(FilterConfig filterConfig) throws Exception {
		String initParameter_HTTP_HEAD_ALLOWED_METHODS = filterConfig.getInitParameter("ALLOWED_HTTP_METHODS");
		if(initParameter_HTTP_HEAD_ALLOWED_METHODS != null && initParameter_HTTP_HEAD_ALLOWED_METHODS.trim().length() > 0){
			String[] allowedHTTPMethods = initParameter_HTTP_HEAD_ALLOWED_METHODS.trim().split(",");
			this.setAllowedHTTPMethods(allowedHTTPMethods);
			log.info("初始化Http请求方法过滤器,配置允许的方法列表:{},系统采用的允许的请求方法列表:{}",initParameter_HTTP_HEAD_ALLOWED_METHODS,getAllowedHTTPMethods());
		}
	}

	@Override
	protected boolean isAccessAllowed(ServletRequest request,ServletResponse response) {
		//如果没有配置任何方法，直接返回FALSE
		if(allowedHTTPMethodSet.isEmpty()){
			return false;
		}
		
		HttpServletRequest httpRequest = WebUtils.toHttp(request);
		//请求使用的方法
		String method = httpRequest.getMethod();
		String UpperCaseMethod = method.toUpperCase();
		boolean isAllowed = this.allowedHTTPMethodSet.contains(UpperCaseMethod);
		if(!isAllowed) {
			log.warn("not allowed http method:{}. for request:{}",method,WebUtils.toHttp(httpRequest).getRequestURL());
		}
		return isAllowed;
	}

	@Override
	protected void onAccessDeniad(ServletRequest request,ServletResponse response) {
		HttpServletResponse httpResponse = WebUtils.toHttp(response);
		try {
			httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Request Denied!");
		} catch (IOException e) {
			if(log.isErrorEnabled()){
				log.error("Send Response Error:{}.",e.getCause());
			}
			e.printStackTrace();
		}
		
	}
	
}
