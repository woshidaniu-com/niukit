package com.woshidaniu.smbclient.pool;

import java.util.Properties;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.smbclient.utils.SMBConfigUtils;
/**
 * 
 * @className	： SMBClientPoolConfig
 * @description	：  基于apache-pool2的对象池初始化对象
 * @author 		： kangzhidong
 * @date		： Jan 20, 2016 12:28:51 PM
 */
public class SMBClientPoolConfig extends GenericObjectPoolConfig {
	
	protected static Logger LOG = LoggerFactory.getLogger(SMBClientPoolConfig.class);
	
	public SMBClientPoolConfig(){
		
	}
	
	public SMBClientPoolConfig(Properties properties){
		try {
			SMBConfigUtils.initPropertiesConfig(this, properties ,"smbclient.pool");
		} catch (Exception e) {
			LOG.error(ExceptionUtils.getFullStackTrace(e));
		}
	}
	
	public SMBClientPoolConfig(String location){
		try {
			SMBConfigUtils.initPropertiesConfig(this,location,"smbclient.pool");
		} catch (Exception e) {
			LOG.error(ExceptionUtils.getFullStackTrace(e));
		}
	}
	
	
}
