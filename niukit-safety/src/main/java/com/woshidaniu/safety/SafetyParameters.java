package com.woshidaniu.safety;

import com.woshidaniu.web.context.WebContext;
import com.woshidaniu.web.utils.WebParameterUtils;

import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import java.util.Map;

public abstract class SafetyParameters {
	
	private SafetyParameters() {
		super();
	}

	public static void initialize(ServletConfig config) {
		if (config != null) {
			WebContext.bindServletConfig(config);
			final ServletContext context = config.getServletContext();
			initialize(context);
		}
	}
	
	public static void initialize(FilterConfig config) {
		if (config != null) {
			WebContext.bindFilterConfig(config);
			final ServletContext context = config.getServletContext();
			initialize(context);
		}
	}

	public static void initialize(ServletContext context) {
		WebContext.bindServletContext(context);
	}
	
	/**
	 * @return Contexte de servlet de la webapp, soit celle monitorée ou soit celle de collecte.
	 */
	public static ServletContext getServletContext() {
		return WebContext.getServletContext();
	}

	/**
	 * 单个Boolean值解析
	 */
	public static boolean getBoolean(String servletOrFilterName, SafetyParameter parameter) {
		assert parameter != null;
		return WebParameterUtils.getBoolean(servletOrFilterName, parameter.getName(), parameter.getDefault() );
	}
	
	/**
	 * 单个Boolean值解析
	 */
	public static boolean getBoolean(String servletOrFilterName, SafetyParameter parameter, String def) {
		assert parameter != null;
		return WebParameterUtils.getBoolean(servletOrFilterName, parameter.getName(), def );
	}
	
	/**
	 * 单个Int值解析
	 */
	public static int getInt(String servletOrFilterName, SafetyParameter parameter) {
		assert parameter != null;
		return WebParameterUtils.getInt(servletOrFilterName, parameter.getName(), parameter.getDefault() );
	}
	
	/**
	 * 单个Int值解析
	 */
	public static int getInt(String servletOrFilterName, SafetyParameter parameter, String def) {
		assert parameter != null;
		return WebParameterUtils.getInt(servletOrFilterName, parameter.getName(), def );
	}
	
	/**
	 * 单个Long值解析
	 */
	public static long getLong(String servletOrFilterName, SafetyParameter parameter, String def) {
		assert parameter != null;
		return WebParameterUtils.getLong(servletOrFilterName, parameter.getName(), def );
	}
	
	/**
	 * 单个Long值解析
	 */
	public static long getLong(String servletOrFilterName, SafetyParameter parameter) {
		assert parameter != null;
		return WebParameterUtils.getLong(servletOrFilterName, parameter.getName(), parameter.getDefault() );
	}
	
	/**
	 * 单个String值解析
	 */
	public static String getString(String servletOrFilterName, SafetyParameter parameter) {
		assert parameter != null;
		return WebParameterUtils.getString(servletOrFilterName, parameter.getName(), parameter.getDefault() );
	}
	
	/**
	 * 单个String值解析
	 */
	public static String getString(String servletOrFilterName, SafetyParameter parameter, String def) {
		assert parameter != null;
		return WebParameterUtils.getString(servletOrFilterName, parameter.getName(), def );
	}
	
	/**
	 * 多个String值解析 ;多个配置可以用",; \t\n"中任意字符分割
	 */
	public static String[] getStringArray(String servletOrFilterName, SafetyParameter parameter){
		assert parameter != null;
		return WebParameterUtils.getStringArray(servletOrFilterName, parameter.getName(), parameter.getDefault());
	}
	
	/**多个键值对解析*/
	public static Map<String, String> getStringMap(String servletOrFilterName, SafetyParameter parameter) {
		assert parameter != null;
		return WebParameterUtils.getStringMap(servletOrFilterName, parameter.getName(), parameter.getDefault() );
    }
	
	/**多个键值对解析*/
	public static Map<String, String[]> getStringMultiMap(String servletOrFilterName, SafetyParameter parameter) {
		assert parameter != null;
		return WebParameterUtils.getStringMultiMap(servletOrFilterName, parameter.getName(), parameter.getDefault() );
    }
	
}
