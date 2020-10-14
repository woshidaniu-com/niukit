package com.woshidaniu.spring.web.filter;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import com.woshidaniu.spring.factory.ServiceFactory;

/**
 * 
 * <p>
 *   <h3>niutal框架<h3>
 *   说明：Freemarker过滤器，用于sitemesh3布局
 * <p>
 * @author <a href="mailto:337836629@qq.com">Penghui.Qu[445]<a>
 * @version 2016年9月2日下午2:38:59
 */
public class FreemarkerFilter implements Filter {

	private Locale locale;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		String localeStr = filterConfig.getInitParameter("locale");
		if(StringUtils.hasText(localeStr)){
			locale = new Locale(localeStr);
		}else {
			locale = Locale.getDefault();
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse res = (HttpServletResponse)response;
		String path = req.getServletPath();
		
		try {
			FreeMarkerViewResolver viewResolver = ServiceFactory.getService(FreeMarkerViewResolver.class);
			View view = viewResolver.resolveViewName(path.replace(".ftl", ""), locale);
			view.render(null, req, res);
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	@Override
	public void destroy() {
		
	}

	
}
