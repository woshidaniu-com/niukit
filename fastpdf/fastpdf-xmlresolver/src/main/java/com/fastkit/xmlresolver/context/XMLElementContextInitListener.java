package com.fastkit.xmlresolver.context;


import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.woshidaniu.configuration.config.Config;
/**
 * 
 * @className: ItextContextInitListener
 * @description: ServletContext监听器 ，用来监听ItextContext上下文初始化状态
 * @author : kangzhidong
 * @date : 下午1:53:10 2013-11-9
 * @modify by:
 * @modify date :
 * @modify description :
 */
public class XMLElementContextInitListener implements ServletContextListener{
	public static final Log log = LogFactory.getLog(XMLElementContextInitListener.class);

	public void contextDestroyed(ServletContextEvent sce) {
		XMLElementContext.getInstance().destroy();
		log.info(" ItextContext has Destroyed ！");
	}

	public void contextInitialized(final ServletContextEvent sce) {
		log.info(" ItextContext initialize starting ...... ");
		try {
			XMLElementContext.getInstance().initialize(new Config() {
				public String getInitParameter(String name) {
					return sce.getServletContext().getInitParameter(name);
				}

				public ServletContext getServletContext() {
					return sce.getServletContext();
				}
			});
		} catch (ServletException e) {
			log.error(" ItextContext initialized ！ ",e);
		}
		log.info(" ItextContext initialize success ！ ");
	}

	
}
