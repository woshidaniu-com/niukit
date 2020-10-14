package com.woshidaniu.ftpclient.pool;

import java.util.Properties;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basicutils.ExceptionUtils;
import com.woshidaniu.ftpclient.utils.FTPConfigUtils;
/**
 * 
 * @className	： FTPClientPoolConfig
 * @description	： 基于apache-pool2的线程池初始化对象
 * @author 		：kangzhidong
 * @date		： Dec 18, 2015 11:55:01 AM
 */
public class FTPClientPoolConfig extends GenericObjectPoolConfig {
	
	protected static Logger LOG = LoggerFactory.getLogger(FTPClientPoolConfig.class);
	
	public FTPClientPoolConfig(){
		
	}
	
	public FTPClientPoolConfig(Properties properties){
		try {
			FTPConfigUtils.initPropertiesConfig(this, properties ,"ftpclient.pool");
		} catch (Exception e) {
			LOG.error(ExceptionUtils.getFullStackTrace(e));
		}
	}
	
	public FTPClientPoolConfig(String location){
		try {
			FTPConfigUtils.initPropertiesConfig(this,location,"ftpclient.pool");
		} catch (Exception e) {
			LOG.error(ExceptionUtils.getFullStackTrace(e));
		}
	}

	
}
