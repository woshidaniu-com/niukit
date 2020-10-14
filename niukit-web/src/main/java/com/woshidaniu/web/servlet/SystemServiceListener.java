package com.woshidaniu.web.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.web.Parameter;
import com.woshidaniu.web.Parameters;


/**
 * 
 *@类名称	: SystemServiceListener.java
 *@类描述	：系统服务启动监听
 *@创建人	：kangzhidong
 *@创建时间	：Mar 8, 2016 3:36:21 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class SystemServiceListener implements ServletContextListener {
	
	protected static Logger LOG = LoggerFactory.getLogger(SystemServiceListener.class);
	protected String serviceName = null;
	
	public SystemServiceListener() {
		super();
	}
	
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceName() {
		return serviceName;
	}
	
	public void contextInitialized(ServletContextEvent event) {
		try {
			// 初始化参数取值对象
			Parameters.initialize(event.getServletContext());
			setServiceName(Parameters.getGlobalString(Parameter.APPLICATION_NAME));
			LOG.info(getServiceName() + " start success !");
		} catch (Exception e) {
			LOG.error(getServiceName() + " start failed !",e);
		}
	}

	public void contextDestroyed(ServletContextEvent event) {
		LOG.info(getServiceName() + " stopped .");
	}
	
}