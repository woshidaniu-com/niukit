/**
 * 
 */
package com.woshidaniu.migration.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.flywaydb.core.internal.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.migration.woshidaniuMigration;

/**
 * @author xiaobin.zhang
 * @desc servlet context listener
 */
public class woshidaniuMigrationContextListener implements ServletContextListener {

	public static final String CONTEXT_INIT_PARAMETER_NAME = "migration.configFilePath";

	private woshidaniuMigration migration;

	Logger logger = LoggerFactory.getLogger(woshidaniuMigrationContextListener.class);
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.ServletContextListener#contextInitialized(javax.servlet.
	 * ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent sce) {

		logger.info("程序开始升级数据库脚本初始化...");
		
		String configFilePath = sce.getServletContext().getInitParameter(CONTEXT_INIT_PARAMETER_NAME);

		if (StringUtils.hasText(configFilePath)) {
			migration = new woshidaniuMigration(configFilePath);
		} else {
			migration = new woshidaniuMigration();
		}
		
		
		int migrate = migration.migrate();
		
		logger.info("程序升级数据库脚本完成，本次共处理 ：" + migrate + "个升级脚本。");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.
	 * ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		//do nothing here 
	}

}
