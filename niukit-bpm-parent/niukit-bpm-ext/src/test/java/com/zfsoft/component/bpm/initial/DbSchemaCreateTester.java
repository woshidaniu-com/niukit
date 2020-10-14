package com.woshidaniu.component.bpm.initial;

import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.junit.Test;

public class DbSchemaCreateTester {

	@Test
	public void testCreateActivitiDbSchema() {
		 ProcessEngineConfiguration
	      .createProcessEngineConfigurationFromResourceDefault()
	      .setDatabaseSchemaUpdate(ProcessEngineConfigurationImpl.DB_SCHEMA_UPDATE_CREATE)
	      .buildProcessEngine();
	}

}
