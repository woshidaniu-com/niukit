package com.opensymphony.xwork2.plus.config;

import com.opensymphony.xwork2.config.Configuration;
import com.opensymphony.xwork2.plus.config.impl.DefaultConfiguration;

/**
 * 
 * *******************************************************************
 * @className	： ConfigurationManager
 * @description	： ConfigurationManager 主要管理 创建的各种加载器 
 * @author 		： kangzhidong
 * @date		： Feb 29, 2016 7:42:27 PM
 * @version 	V1.0 
 * *******************************************************************
 */
public class ConfigurationManager extends com.opensymphony.xwork2.config.ConfigurationManager {
	
	public ConfigurationManager() {
		super();
    }
	
	public ConfigurationManager(String name) {
		super(name);
	}

	@Override
	protected Configuration createConfiguration(String beanName) {
        return new DefaultConfiguration(beanName);
    }
	
}
