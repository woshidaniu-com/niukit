package com.woshidaniu.qa.core;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/** 
* @author 作者 E-mail: 
* @version 创建时间：2017年5月12日 下午3:18:24 
* 类说明：全局配置文件缓存类 
*/
public class ConfigValues {
	private static ConcurrentHashMap<String, HashMap<String, String>> values = new ConcurrentHashMap<String, HashMap<String, String>>();
	 
	 /**
	     * 取得全局值的内容
	     * 
	     * @param className
	     * @return
	     */
	    public static HashMap<String, String> getConfigValue(String className) {
	        return values.get(className);
	    }

	    /**
	     * 取得全局值的内容
	     * 
	     * @param className
	     * @return
	     */
	    public static HashMap<String, String> getConfigValue(Class<?> clazz) {
	        return values.get(clazz.getName());
	    }

	    /**
	     * 设置全局值
	     * 
	     * @param className
	     * @param configValues
	     */
	    public static void addConfigValue(String className, HashMap<String, String> configValues) {
	        if (values.containsKey(className))
	            values.get(className).putAll(configValues);
	        else
	            values.put(className, configValues);
	    }

	    /**
	     * 设置全局值
	     * 
	     * @param className
	     * @param configValues
	     */
	    public static void addConfigValue(Class<?> clazz, HashMap<String, String> configValues) {
	        String className = clazz.getName();
	        addConfigValue(className,configValues);
	    }
}
 