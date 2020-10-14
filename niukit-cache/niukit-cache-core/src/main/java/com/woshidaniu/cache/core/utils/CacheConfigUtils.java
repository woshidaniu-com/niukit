package com.woshidaniu.cache.core.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Properties;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basicutils.ConfigUtils;
import com.woshidaniu.basicutils.StringUtils;

public class CacheConfigUtils extends ConfigUtils{
	
	protected static Logger LOG = LoggerFactory.getLogger(CacheConfigUtils.class);
	
	public static <T> void initPropertiesConfig(T config,String location,String prefix){
        try {
            Properties properties = ConfigUtils.getProperties(CacheConfigUtils.class, location );
			if(!properties.isEmpty()){
				try {
					BeanInfo beanInfo = Introspector.getBeanInfo(config.getClass());
					PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
					for (PropertyDescriptor propDes : propertyDescriptors) {
						String key = prefix + "." + propDes.getName();
						// 如果是class属性 跳过
						if ( !properties.containsKey(key)|| propDes.getName().equals("class") || propDes.getReadMethod() == null || propDes.getWriteMethod() == null) {
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
