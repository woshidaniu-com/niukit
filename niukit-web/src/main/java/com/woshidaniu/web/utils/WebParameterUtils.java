package com.woshidaniu.web.utils;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import com.woshidaniu.basicutils.StringUtils;
import com.woshidaniu.web.context.WebContext;

public class WebParameterUtils {

	public static final String PARAMETER_SYSTEM_PREFIX = "niutal.";

	/**
	 * 单个Boolean值解析
	 */
	public static boolean getBoolean(String servletOrFilterName, String name, String def) {
		String para = getInitParameter(servletOrFilterName, name);
		return Boolean.parseBoolean( para == null ? def : para);
	}
	 
	/**
	 * 单个Int值解析
	 */
	public static int getInt(String servletOrFilterName, String name, String def) {
		String para = getInitParameter(servletOrFilterName, name);
		return Integer.parseInt(para == null ? def : para);
	}
	
	/**
	 * 单个Long值解析
	 */
	public static long getLong(String servletOrFilterName, String name, String def) {
		String para = getInitParameter(servletOrFilterName, name);
		return Long.parseLong(para == null ? def : para);
	}
	
	/**
	 * 单个String值解析
	 */
	public static String getString(String servletOrFilterName, String name, String def) {
		String para = getInitParameter(servletOrFilterName, name);
		return para == null ? def : para;
	}
	
	/**
	 * 多个String值解析 ;多个配置可以用",; \t\n"中任意字符分割
	 */
	public static String[] getStringArray(String servletOrFilterName, String name, String def){
		String para = getInitParameter(servletOrFilterName, name);
		return StringUtils.tokenizeToStringArray(para == null ? def : para);
	}
	
	/**多个键值对解析*/
	public static Map<String, String> getStringMap(String servletOrFilterName, String name, String def) {
        Map<String, String> result = new LinkedHashMap<String, String>();
        String[] entries = getStringArray(servletOrFilterName, name, def);
        if (entries != null) {
            for (String entry : entries) {
            	if(StringUtils.isEmpty(entry)){
					continue;
				}
            	String[] split = entry.split("\\|");
                result.put(split[0], split[1]);
            }
        }
        return result;
    }
	
	/**多个键值对解析*/
	public static Map<String, String[]> getStringMultiMap(String servletOrFilterName, String name, String def) {
        Map<String, String[]> result = new LinkedHashMap<String, String[]>();
        String[] entries = getStringArray(servletOrFilterName, name, def);
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
	
	public static String getInitParameter(String servletOrFilterName,String parameterName) {
		assert parameterName != null;
		
		// 1、全局参数
		String result = getGlobalInitParameter(parameterName);
		if (result != null) {
			return result;
		}
		
		// 2、获取FilterConfig对象的初始参数
		FilterConfig filterConfig = WebContext.getFilterConfig(servletOrFilterName);
		if (filterConfig != null) {
			result = filterConfig.getInitParameter(parameterName);
			if (result != null) {
				return result;
			}
		}
		// 3、获取ServletConfig对象的初始参数
		ServletConfig servletConfig = WebContext.getServletConfig(servletOrFilterName);
		if (servletConfig != null) {
			result = servletConfig.getInitParameter(parameterName);
			if (result != null) {
				return result;
			}
		}
		return null;
	}

	/**
	 * 单个Boolean值解析
	 */
	public static boolean getBoolean(String name, String def) {
		String para = getGlobalInitParameter(name);
		return Boolean.parseBoolean( para == null ? def : para);
	}
	 
	/**
	 * 单个Int值解析
	 */
	public static int getInt(String name, String def) {
		String para = getGlobalInitParameter(name);
		return Integer.parseInt(para == null ? def : para);
	}
	
	/**
	 * 单个Long值解析
	 */
	public static long getLong(String name, String def) {
		String para = getGlobalInitParameter(name);
		return Long.parseLong(para == null ? def : para);
	}
	
	/**
	 * 单个String值解析
	 */
	public static String getString(String name, String def) {
		String para = getGlobalInitParameter(name);
		return para == null ? def : para;
	}
	
	/**
	 * 多个String值解析 ;多个配置可以用",; \t\n"中任意字符分割
	 */
	public static String[] getStringArray(String name, String def){
		String para = getGlobalInitParameter(name);
		return StringUtils.tokenizeToStringArray(para == null ? def : para);
	}
	
	/**多个键值对解析*/
	public static Map<String, String> getStringMap(String name, String def) {
        Map<String, String> result = new LinkedHashMap<String, String>();
        String[] entries = getStringArray(name, def);
        if (entries != null) {
            for (String entry : entries) {
            	if(StringUtils.isEmpty(entry)){
					continue;
				}
            	String[] split = entry.split("\\|");
                result.put(split[0], split[1]);
            }
        }
        return result;
    }
	
	/**多个键值对解析*/
	public static Map<String, String[]> getStringMultiMap(String name, String def) {
        Map<String, String[]> result = new LinkedHashMap<String, String[]>();
        String[] entries = getStringArray( name, def);
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
	
    public static String getGlobalInitParameter(String parameterName) {
		assert parameterName != null;
		final String globalName = PARAMETER_SYSTEM_PREFIX + parameterName;
		
		// 1、获取指定的环境变量的值。环境变量是依赖于系统的外部命名值
		String result = System.getenv(globalName);
		if (result != null) {
			return result;
		}
		
		// 2、获取系统的相关属性，包括文件编码、操作系统名称、区域、用户名等，此属性一般由jvm自动获取，不能设置
		result = System.getProperty(globalName);
		if (result != null) {
			return result;
		}
		
		// 3、获取 runtime.properties 文件的参数值
		result = WebContext.getRuntimeProperty(globalName);
		if (result != null) {
			return result;
		}
		
		// 4、获取ServletContext对象的初始参数
		ServletContext servletContext = WebContext.getServletContext();
		if (servletContext != null) {
			result = servletContext.getInitParameter(globalName);
			if (result != null) {
				return result;
			}
			// In a ServletContextListener, it's also possible to call servletContext.setAttribute("http.xxx", "true"); for example
			final Object attribute = servletContext.getAttribute(globalName);
			if (attribute instanceof String) {
				return (String) attribute;
			}
		}
		 
		return null;
	}

}
