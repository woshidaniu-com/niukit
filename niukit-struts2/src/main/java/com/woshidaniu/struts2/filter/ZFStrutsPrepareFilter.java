package com.woshidaniu.struts2.filter;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.dispatcher.ng.filter.StrutsPrepareFilter;

import com.woshidaniu.web.context.WebContext;
import com.woshidaniu.web.utils.LocaleUtils;

public class ZFStrutsPrepareFilter extends StrutsPrepareFilter {

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		
		//绑定上下文请求
		HttpSession session = ((HttpServletRequest)req).getSession();
		WebContext.bindServletContext(session.getServletContext());
		WebContext.bindRequest(req);
		WebContext.bindResponse(res);
		
		//国际化语言切换
		Locale locale = LocaleUtils.interceptLocale((HttpServletRequest)req);
		WebContext.setLocale(locale);
		
		super.doFilter(req, res, chain);
	}
	
}
