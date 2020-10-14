package com.woshidaniu.basicutils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ConfigUtils {
	
	public static Logger LOG = LoggerFactory.getLogger(ConfigUtils.class);
	protected static final String XML_FILE_EXTENSION = ".xml";
	protected static final String ENCODING = "UTF-8";
	protected static ConcurrentMap<String, Properties> cachedProperties = new ConcurrentHashMap<String, Properties>();
	
	public static <T> void initPropertiesConfig(T config,String location,String prefix){
		if(cachedProperties.get(location) == null){
			synchronized (location) {
				cachedProperties.put(location,ConfigUtils.getProperties(config.getClass(), location));
			}
		}
        Properties properties = cachedProperties.get(location);
        ConfigUtils.initPropertiesConfig(config, properties, prefix);
	}
	
	public static <T> void initPropertiesConfig(T config,Properties properties,String prefix){
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
				LOG.error(ExceptionUtils.getFullStackTrace(e));
			}
		}
	}
	
	public static <T> Properties getProperties(Class<T> clazz,String location){
		if (location != null ) {
			Properties ret = cachedProperties.get(location);
 			if (ret != null) {
 				return ret;
 			}
 			ret = new Properties();
 			synchronized (clazz) {
 				InputStream stream = null;
 	 			try {
 	 				stream = ConfigUtils.getInputStream(clazz,location);
 	 				String filename = FilenameUtils.getName(location);
 	 				if (filename != null && filename.endsWith(XML_FILE_EXTENSION)) {
 	 					stream = ConfigUtils.getInputStream(clazz,location);
 	 					ret.loadFromXML(stream);
 	 				}else {
 	 					ret.load(new InputStreamReader(stream, ENCODING));
 	 				}
 	 			} catch (IOException e) {
 	 				LOG.error(e.getMessage());
				} finally {
 	 				IOUtils.closeQuietly(stream);
 	 			}
			}
 			Properties existing = cachedProperties.putIfAbsent(location, ret);
 			if (existing != null) {
 				ret = existing;
 			}
 			return ret;
 		}
 		return null;
	}
	
	public static <T> Properties getNewProperties(Class<T> clazz,String location){
		if (location != null ) {
 			Properties ret = new Properties();
 			synchronized (clazz) {
 				InputStream stream = null;
 	 			try {
 	 				stream = ConfigUtils.getInputStream(clazz,location);
 	 				String filename = FilenameUtils.getName(location);
 	 				if (filename != null && filename.endsWith(XML_FILE_EXTENSION)) {
 	 					stream = ConfigUtils.getInputStream(clazz,location);
 	 					ret.loadFromXML(stream);
 	 				}else {
 	 					ret.load(new InputStreamReader(stream, ENCODING));
 	 				}
 	 			} catch (IOException e) {
 	 				LOG.error(e.getMessage());
				} finally {
 	 				IOUtils.closeQuietly(stream);
 	 			}
			}
 			return ret;
 		}
 		return null;
	}
	
	public static <T> InputStream getInputStream(Class<T> clazz,String location) throws IOException{
		URL url = LocationUtils.getResourceAsURL(location, clazz);
		InputStream  input = (url != null) ? url.openStream() : null;
        if( input == null){
        	throw new IllegalArgumentException("[" + location + "] is not found!");
        }
        return input;
	}
	/**
	 * 
	 *@描述		：为包含如 { 0 }占位符的字符串中的占位符设值
	 *@创建人	: kangzhidong
	 *@创建时间	: Mar 24, 20165:27:19 PM
	 *@param message
	 *@param params
	 *@return
	 *@修改人	: 
	 *@修改时间	: 
	 *@修改描述	:
	 */
	protected static String setParams(String message,Object... params) {
		if (null != params && params.length > 0){
			for (int i = 0 ; i < params.length ; i++){
				message = message.replaceFirst("\\{"+i+"\\}", String.valueOf(params[i]));
			}
		}
		return message;
	}
 
}
