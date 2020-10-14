package com.woshidaniu.runtime;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import com.woshidaniu.basicutils.ConfigUtils;
import com.woshidaniu.basicutils.StringUtils;

/**
 * 
 *@类名称		： RuntimeParameters.java
 *@类描述		：
 *@创建人		：kangzhidong
 *@创建时间	：Feb 21, 2017 8:15:28 AM
 *@修改人		：
 *@修改时间	：
 *@版本号		: v1.0
 */
public abstract class RuntimeParameters {
	
	private static final File TEMPORARY_DIRECTORY = new File(System.getProperty("java.io.tmpdir"));
	
	private RuntimeParameters() {
		super();
	}

	public static File getStorageDirectory(String application) {
		final String param = getString(RuntimeParam.RUNTIME_STORAGE_DIRECTORY);
		final String dir;
		if (param == null) {
			dir = RuntimeParam.RUNTIME_STORAGE_DIRECTORY.getDefault();
		} else {
			dir = param;
		}
		// ('temp' dans TOMCAT_HOME pour tomcat).
		final String directory;
		if (dir.length() > 0 && new File(dir).isAbsolute()) {
			directory = dir;
		} else {
			directory = TEMPORARY_DIRECTORY.getPath() + '/' + dir;
		}
		return new File(directory);
	}

	/**
	 * 单个Boolean值解析
	 */
	public static boolean getBoolean(RuntimeParam parameter) {
		assert parameter != null;
		final String name = parameter.getName();
		String para = getParameterByName(name);
		return Boolean.parseBoolean( para == null ? parameter.getDefault() : para);
	}
	
	/**
	 * 单个Boolean值解析
	 */
	public static boolean getBoolean(RuntimeParam parameter,String def) {
		assert parameter != null;
		final String name = parameter.getName();
		String para = getParameterByName(name);
		return Boolean.parseBoolean( para == null ? def : para);
	}
	
	/**
	 * 单个Int值解析
	 */
	public static int getInt(RuntimeParam parameter) {
		assert parameter != null;
		final String name = parameter.getName();
		String para = getParameterByName(name);
		return Integer.parseInt(para == null ? parameter.getDefault() : para);
	}
	
	/**
	 * 单个Int值解析
	 */
	public static int getInt(RuntimeParam parameter,String def) {
		assert parameter != null;
		final String name = parameter.getName();
		String para = getParameterByName(name);
		return Integer.parseInt(para == null ? def : para);
	}
	
	/**
	 * 单个Long值解析
	 */
	public static long getLong(RuntimeParam parameter,String def) {
		assert parameter != null;
		final String name = parameter.getName();
		String para = getParameterByName(name);
		return Long.parseLong(para == null ? def : para);
	}
	
	/**
	 * 单个Long值解析
	 */
	public static long getLong(RuntimeParam parameter) {
		assert parameter != null;
		final String name = parameter.getName();
		String para = getParameterByName(name);
		return Long.parseLong(para == null ? parameter.getDefault() : para);
	}
	
	/**
	 * 单个String值解析
	 */
	public static String getString(RuntimeParam parameter) {
		assert parameter != null;
		final String name = parameter.getName();
		String para = getParameterByName(name);
		return para == null ? parameter.getDefault() : para;
	}
	
	/**
	 * 单个String值解析
	 */
	public static String getString(RuntimeParam parameter,String def) {
		assert parameter != null;
		final String name = parameter.getName();
		String para = getParameterByName(name);
		return para == null ? def : para;
	}
	
	/**
	 * 多个String值解析 ;多个配置可以用",; \t\n"中任意字符分割
	 */
	public static String[] getStringArray(RuntimeParam parameter){
		assert parameter != null;
		final String name = parameter.getName();
		String para = getParameterByName(name);
		return StringUtils.tokenizeToStringArray(para == null ? parameter.getDefault() : para);
	}
	
	/**多个键值对解析*/
	public static Map<String, String[]> getStringMultiMap(RuntimeParam paramName) {
        Map<String, String[]> result = new LinkedHashMap<String, String[]>();
        String[] entries = getStringArray(paramName);
        if (entries != null) {
            for (String entry : entries) {
            	if(StringUtils.isEmpty(entry)){
					continue;
				}
                String[] split = entry.split("=", 2);
                if (split.length == 2) {
                    String itemKey = split[0];
                    String[] itemValue = split[1].split("\\|");
                    result.put(itemKey, itemValue);
                }
            }
        }
        return result;
    }

	public static String getParameterByName(String parameterName) {
		
		assert parameterName != null;
		
		// 1、获取指定的环境变量的值。环境变量是依赖于系统的外部命名值
		String result = System.getenv(parameterName);
		if (result != null) {
			return result;
		}
		
		// 2、获取系统的相关属性，包括文件编码、操作系统名称、区域、用户名等，此属性一般由jvm自动获取，不能设置
		result = System.getProperty(parameterName);
		if (result != null) {
			return result;
		}
		
		// 3、获取 runtime.properties 文件的参数值
		result = getRuntimeProperty(parameterName);
		if (result != null) {
			return result;
		}
		
		return null;
	}
	
	public static Properties getRuntimeProperties() {
		return ConfigUtils.getProperties(RuntimeParameters.class, "runtime.properties");
	}
	
	public static String getRuntimeProperty(String key) {
		return getRuntimeProperties().getProperty(key); 
	}
	
	public static String getRuntimeProperty(String key, String defaultValue) {
		return getRuntimeProperties().getProperty(key, defaultValue); 
	}
	
}
