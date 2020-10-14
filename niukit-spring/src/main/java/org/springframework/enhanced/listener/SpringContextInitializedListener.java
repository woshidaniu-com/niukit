package org.springframework.enhanced.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.enhanced.context.SpringWebInstanceContext;

/**
 * 
 * @className	： SpringContextInitializedListener
 * @description	： Spring 自定义上下文初始化监听
 * @author 		：大康（743）
 * @date		： 2017年4月18日 下午9:08:27
 * @version 	V1.0
 */
public class SpringContextInitializedListener implements ServletContextListener {

	protected static Logger LOG = LoggerFactory.getLogger(SpringContextInitializedListener.class);
	
	public void contextInitialized(ServletContextEvent event) {
		LOG.info(" SpringContext initialize start ... ");
		SpringWebInstanceContext.initialInstanceContext(event.getServletContext());
		LOG.info("SpringContext initialized.");
	}

	public void contextDestroyed(ServletContextEvent event) {
		LOG.info("SpringContext destroyed .");
	}
	
}
