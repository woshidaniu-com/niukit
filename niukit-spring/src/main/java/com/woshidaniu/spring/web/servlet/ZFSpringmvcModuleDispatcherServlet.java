package com.woshidaniu.spring.web.servlet;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.enhanced.web.servlet.ModuleDispatcherServlet;

import com.woshidaniu.web.context.WebContext;
import com.woshidaniu.web.utils.LocaleUtils;


/**
 * 
 *@类名称		： ZFSpringmvcModuleDispatcherServlet.java
 *@类描述		：
 *@创建人		：kangzhidong
 *@创建时间	：2017年4月20日 下午4:07:13
 *@修改人		：
 *@修改时间	：
 *@版本号		: v1.0
 */
public class ZFSpringmvcModuleDispatcherServlet extends ModuleDispatcherServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7755708083665181328L;

	@Override
	protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		WebContext.bindServletContext(request.getSession().getServletContext());
		WebContext.bindRequest(request);
		WebContext.bindResponse(response);
		
		//国际化语言切换
		Locale locale = LocaleUtils.interceptLocale(request);
		WebContext.setLocale(locale);
		
		super.doService(request, response);
	}
	
	

}
