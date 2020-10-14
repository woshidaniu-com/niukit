package com.woshidaniu.web.servlet.filter.impl;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.woshidaniu.web.Parameter;
import com.woshidaniu.web.Parameters;
import com.woshidaniu.web.servlet.http.HttpServletCharacterEncodingRequestWrapper;

/**
 * 
 *@类名称	: HttpServletRequestCharacterEncodingFilter.java
 *@类描述	：此过滤器用来解决解决get、post请求方式下的中文乱码问题
 *@创建人	：kangzhidong
 *@创建时间	：Mar 8, 2016 9:01:00 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class HttpServletRequestCharacterEncodingFilter implements Filter {

	private String charset = "UTF-8";

	public void init(FilterConfig filterConfig) throws ServletException {
		// 初始化参数取值对象
		Parameters.initialize(filterConfig);
		// 得到在web.xml中配置的字符编码
		charset = Parameters.getString(filterConfig.getFilterName(), Parameter.APPLICATION_CHARSET);
	}

	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		request.setCharacterEncoding(charset);
		response.setCharacterEncoding(charset);
		response.setContentType("text/html;charset=" + charset);
		HttpServletCharacterEncodingRequestWrapper requestWrapper = new HttpServletCharacterEncodingRequestWrapper(request);
		chain.doFilter(requestWrapper, response);
	}

	public void destroy() {

	}
}
