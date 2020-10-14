package com.woshidaniu.smbclient.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Properties;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basicutils.ConfigUtils;
import com.woshidaniu.basicutils.StringUtils;
import com.woshidaniu.smbclient.SMBClientConfig;
import com.woshidaniu.smbclient.pool.SMBClientPoolConfig;

public class SMBConfigUtils extends ConfigUtils {
	
	protected static Logger LOG = LoggerFactory.getLogger(SMBConfigUtils.class);
	
	public static void initPropertiesConfig(SMBClientConfig config,String location){
        try {
        	Properties properties = ConfigUtils.getProperties(SMBConfigUtils.class, location );
        	SMBConfigUtils.initPropertiesConfig( config, properties);
        } catch (Exception e) {
        	LOG.error("Couldn't load properties ！", e);
		}
	}
	
	public static void initPropertiesConfig(SMBClientConfig config,Properties properties){
        try {
			if(!properties.isEmpty()){
				try {
					BeanInfo beanInfo = Introspector.getBeanInfo(config.getClass());
					PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
					for (PropertyDescriptor propDes : propertyDescriptors) {
						String key = "smbclient." + propDes.getName();
						// 如果是class属性 跳过
						if ( !properties.containsKey(key) || propDes.getName().equals("class") || propDes.getReadMethod() == null || propDes.getWriteMethod() == null) {
							continue;
						}
						BeanUtils.setProperty(config, propDes.getName(), properties.getProperty(key));
					}
				} catch (Exception e) {
					LOG.error(e.getMessage(), e);
				}
			}
        } catch (Exception e) {
        	LOG.error("Couldn't load properties ！", e);
		}
	}
	
	public static void initPropertiesConfig(SMBClientPoolConfig config, String location) {
        try {
        	Properties properties = ConfigUtils.getProperties(SMBConfigUtils.class, location );
			if(!properties.isEmpty()){
				try {
					BeanInfo beanInfo = Introspector.getBeanInfo(config.getClass());
					PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
					for (PropertyDescriptor propDes : propertyDescriptors) {
						String key = "smbclient.pool." + propDes.getName();
						// 如果是class属性 跳过
						if ( !properties.containsKey(key) || propDes.getName().equals("class") || propDes.getReadMethod() == null || propDes.getWriteMethod() == null) {
							continue;
						}
						BeanUtils.setProperty(config, propDes.getName(), StringUtils.getSafeStr(properties.getProperty(key)));
					}
				} catch (Exception e) {
					LOG.error(e.getMessage(), e);
				}
			}
        } catch (Exception e) {
        	LOG.error("Couldn't load properties ！", e);
		}
	}
 
}
