/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.safety.xss;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.woshidaniu.safety.xss.http.InputRequestWrapper;
import com.woshidaniu.web.servlet.filter.AbstractPathMatchFilter;

/**
 * @className	： InputFilter
 * @description	： 表单输入过滤器,针对具有input和textarea这两个标签提交请求，进行封装请求，放置xss攻击
 * @author 		：康康（1571）
 * @date		： 2018年8月8日 上午11:28:56
 * @version 	V1.0
 */
public class InputFilter extends AbstractPathMatchFilter{

	@Override
	public void doFilterInternal(ServletRequest request,
			ServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		
		if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
			throw new ServletException( "just supports HTTP requests");
		}
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		filterChain.doFilter(new InputRequestWrapper(httpRequest), httpResponse);
		
	}
}
