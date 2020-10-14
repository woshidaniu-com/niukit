package org.activiti.engine.extend.cfg;

import java.io.InputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.extend.log.ProcessLoggerManager;
import org.activiti.engine.extend.service.impl.ExtendServiceImpl;
import org.activiti.spring.SpringProcessEngineConfiguration;

/**
 * <p>
 * <h3>niutal框架
 * <h3><br>
 * class：org.activiti.engine.extend.cfg.ExtendSpringProcessEnginConfiguration.java
 * <p>
 * 
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
public class ExtendSpringProcessEnginConfiguration extends SpringProcessEngineConfiguration {

	public static final String EXTEND_MYBATIS_MAPPING_FILE = "org/activiti/extend/db/mapping/mappings.xml";
	
	protected ExtendServiceImpl extendService = new ExtendServiceImpl();

	protected ProcessLoggerManager processLoggerManager;

	@Override
	public ProcessEngine buildProcessEngine() {
		ProcessEngine processEngine = super.buildProcessEngine();
		if (processLoggerManager != null) {
			processLoggerManager.init();
		}
		return processEngine;
	}

	protected void initServices() {
		super.initServices();
		initService(extendService);
	}

	public ExtendServiceImpl getExtendService() {
		return extendService;
	}

	public ProcessLoggerManager getProcessLoggerManager() {
		return processLoggerManager;
	}

	public void setProcessLoggerManager(ProcessLoggerManager processLoggerManager) {
		this.processLoggerManager = processLoggerManager;
	}

	@Override
	protected InputStream getMyBatisXmlConfigurationSteam() {
		return getResourceAsStream(EXTEND_MYBATIS_MAPPING_FILE);
	}

}
