/**
 * <p>Copyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.safety.xss;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class TrimRequestParameterWrapperFilter extends SimpleRequestWrapperFilter{

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
		Class<?> loaded_class = TrimRequestParameterRequest.class;
		
		clazz = (Class<? extends HttpServletRequestWrapper>) loaded_class;
		try {
			constructor = clazz.getConstructor(HttpServletRequest.class);
		} catch (SecurityException e) {
			throw new ServletException(e);
		} catch (NoSuchMethodException e) {
			throw new ServletException(e);
		}
	}
}
