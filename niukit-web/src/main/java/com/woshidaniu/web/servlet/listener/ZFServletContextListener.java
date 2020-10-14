package com.woshidaniu.web.servlet.listener;

import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ZFServletContextListener implements ServletContextListener, ServletContextAttributeListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// TODO Auto-generated method stub

	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub

	}

	@Override
	public void attributeAdded(ServletContextAttributeEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void attributeRemoved(ServletContextAttributeEvent event) {
		// TODO Auto-generated method stub
		
		System.out.println("Servlet API " + event.getServletContext().getEffectiveMajorVersion() + "." + event.getServletContext().getMinorVersion()); 
		System.out.println("Servlet API " + event.getServletContext().getMajorVersion() + "." + event.getServletContext().getMinorVersion()); 
		
		//JspFactory.getDefaultFactory().getEngineInfo().getSpecificationVersion();
		
	}

	@Override
	public void attributeReplaced(ServletContextAttributeEvent event) {
		// TODO Auto-generated method stub
		
	}

}
