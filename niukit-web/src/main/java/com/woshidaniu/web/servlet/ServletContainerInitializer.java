package com.woshidaniu.web.servlet;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;

/**
 * 
 *@类名称	: ServletContainerInitializer.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 7, 2016 3:24:48 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class ServletContainerInitializer implements ServletContextListener {


	/**
	 * @description	： TODO(描述这个方法的作用)
	 * @author 		： kangzhidong
	 * @date 		：Feb 2, 2016 3:41:21 PM
	 * @param sce
	 */
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		
		ServletContext sc = sce.getServletContext();

        // Register Servlet
        ServletRegistration sr = sc.addServlet("DynamicServlet",
            "web.servlet.dynamicregistration_war.TestServlet");
        sr.setInitParameter("servletInitName", "servletInitValue");
        sr.addMapping("/*");

        // Register Filter
        FilterRegistration fr = sc.addFilter("DynamicFilter",
            "web.servlet.dynamicregistration_war.TestFilter");
        fr.setInitParameter("filterInitName", "filterInitValue");
        fr.addMappingForServletNames(EnumSet.of(DispatcherType.REQUEST),
                                     true, "DynamicServlet");

        // Register ServletRequestListener
        sc.addListener("web.servlet.dynamicregistration_war.TestServletRequestListener");
        
	}
	
	/**
	 * @description	： TODO(描述这个方法的作用)
	 * @author 		： kangzhidong
	 * @date 		：Feb 2, 2016 3:41:21 PM
	 * @param sce
	 */
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}


}
