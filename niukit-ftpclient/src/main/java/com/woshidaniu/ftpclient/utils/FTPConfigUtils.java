package com.woshidaniu.ftpclient.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Properties;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basicutils.ConfigUtils;
import com.woshidaniu.basicutils.StringUtils;
import com.woshidaniu.ftpclient.FTPClientConfig;

public class FTPConfigUtils extends ConfigUtils {
	
	protected static Logger LOG = LoggerFactory.getLogger(FTPConfigUtils.class);
	
	public static void initPropertiesConfig(FTPClientConfig config,String location){
        try {
        	Properties properties = ConfigUtils.getProperties(FTPConfigUtils.class, location );
        	FTPConfigUtils.initPropertiesConfig( config, properties);
        } catch (Exception e) {
        	LOG.error("Couldn't load properties ！", e);
		}
	}
	
	public static void initPropertiesConfig(FTPClientConfig config,Properties properties){
        try {
			if(!properties.isEmpty()){
				try {
					BeanInfo beanInfo = Introspector.getBeanInfo(config.getClass());
					PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
					for (PropertyDescriptor propDes : propertyDescriptors) {
						String key1 = "ftpclient." + propDes.getName();
						String key2 = "ftpsclient." + propDes.getName();
						// 如果是class属性 跳过
						if ( (!properties.containsKey(key1) && !properties.containsKey(key2)) || propDes.getName().equals("class") || propDes.getReadMethod() == null || propDes.getWriteMethod() == null) {
							continue;
						}
						BeanUtils.setProperty(config, propDes.getName(), StringUtils.getSafeStr(properties.getProperty(key1),properties.getProperty(key2)));
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
