package com.woshidaniu.fastxls.jexcel.utils;

import java.util.Properties;

import com.woshidaniu.basicutils.ConfigUtils;
import com.woshidaniu.basicutils.StringUtils;

public class JXLConfigUtils extends ConfigUtils {
	
	private static ThreadLocal<Properties> configCached = new ThreadLocal<Properties>();  
	public	static String DEFAULT_LOCATION = "fastjxl.properties";
	
	public static Properties getDefaultConfig() {
		return getConfig(DEFAULT_LOCATION); 
	}
	
	public static Properties getConfig(String location) {
		if (configCached.get() == null) {
			configCached.set(ConfigUtils.getProperties(JXLConfigUtils.class, location));
        }
		return configCached.get(); 
	}
	
	public static String getText(String key) {
		return getText(key,"");
	}
	
	public static String getText(String key, String defaultValue) {
		return getDefaultConfig().getProperty(key, defaultValue); 
	}
	
	public static String getText(String key, Object... params) {
		String message = getText(key,"");
		return StringUtils.getSafeStr(setParams(message,params), "") ;
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
