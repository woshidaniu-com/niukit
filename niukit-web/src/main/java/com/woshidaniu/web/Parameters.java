package com.woshidaniu.web;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import com.woshidaniu.web.context.WebContext;
import com.woshidaniu.web.utils.WebParameterUtils;

/**
 * 
 * *******************************************************************
 * @className	： Parameters
 * @description	： TODO(描述这个类的作用)
 * @author 		： <a href="mailto:hnxyhcwdl1003@163.com">kangzhidong</a>
 * @date		： Sep 17, 2016 9:21:34 PM
 * @version 	V1.0 
 * *******************************************************************
 */
public abstract class Parameters {
	
	private static final File TEMPORARY_DIRECTORY = new File(System.getProperty("java.io.tmpdir"));
	private static boolean dnsLookupsDisabled;
	
	private Parameters() {
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
		String name = Parameter.DNS_LOOKUPS_DISABLED.getName();
		String def = Parameter.DNS_LOOKUPS_DISABLED.getDefault();
		dnsLookupsDisabled = WebParameterUtils.getBoolean(name, def);
	}
	
	/**
	 * @return Contexte de servlet de la webapp, soit celle monitorée ou soit celle de collecte.
	 */
	public static ServletContext getServletContext() {
		return WebContext.getServletContext();
	}
	
	public static String getHostName() {
		if (dnsLookupsDisabled) {
			return "localhost";
		}
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (final UnknownHostException ex) {
			return "unknown";
		}
	}

	/**
	 * @return adresse ip de la machine
	 */
	public static String getHostAddress() {
		if (dnsLookupsDisabled) {
			return "127.0.0.1"; // NOPMD
		}
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (final UnknownHostException ex) {
			return "unknown";
		}
	}

	public static String getResourcePath(String fileName) {
		final Class<Parameters> classe = Parameters.class;
		final String packageName = classe.getName().substring(0, classe.getName().length() - classe.getSimpleName().length() - 1);
		return '/' + packageName.replace('.', '/') + "/resource/" + fileName;
	}

	public static File getStorageDirectory(String application) {
		String name = Parameter.STORAGE_DIRECTORY.getName();
		String def = Parameter.STORAGE_DIRECTORY.getDefault();
		final String param = WebParameterUtils.getString(name, def);
		final String dir;
		if (param == null) {
			dir = def;
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
		ServletContext servletContext = getServletContext();
		if (servletContext != null) {
			return new File(directory + '/' + application);
		}
		return new File(directory);
	}
	
	/**
	 * 单个Boolean值解析
	 */
	public static boolean getBoolean(String servletOrFilterName,Parameter parameter) {
		assert parameter != null;
		return WebParameterUtils.getBoolean(servletOrFilterName, parameter.getName(), parameter.getDefault() );
	}
	
	/**
	 * 单个Boolean值解析
	 */
	public static boolean getBoolean(String servletOrFilterName,Parameter parameter,String def) {
		assert parameter != null;
		return WebParameterUtils.getBoolean(servletOrFilterName, parameter.getName(), def );
	}
	
	/**
	 * 单个Int值解析
	 */
	public static int getInt(String servletOrFilterName,Parameter parameter) {
		assert parameter != null;
		return WebParameterUtils.getInt(servletOrFilterName, parameter.getName(), parameter.getDefault() );
	}
	
	/**
	 * 单个Int值解析
	 */
	public static int getInt(String servletOrFilterName,Parameter parameter,String def) {
		assert parameter != null;
		return WebParameterUtils.getInt(servletOrFilterName, parameter.getName(), def );
	}
	
	/**
	 * 单个Long值解析
	 */
	public static long getLong(String servletOrFilterName,Parameter parameter,String def) {
		assert parameter != null;
		return WebParameterUtils.getLong(servletOrFilterName, parameter.getName(), def );
	}
	
	/**
	 * 单个Long值解析
	 */
	public static long getLong(String servletOrFilterName,Parameter parameter) {
		assert parameter != null;
		return WebParameterUtils.getLong(servletOrFilterName, parameter.getName(), parameter.getDefault() );
	}
	
	/**
	 * 单个String值解析
	 */
	public static String getString(String servletOrFilterName,Parameter parameter) {
		assert parameter != null;
		return WebParameterUtils.getString(servletOrFilterName, parameter.getName(), parameter.getDefault() );
	}
	
	/**
	 * 单个String值解析
	 */
	public static String getString(String servletOrFilterName,Parameter parameter,String def) {
		assert parameter != null;
		return WebParameterUtils.getString(servletOrFilterName, parameter.getName(), def );
	}
	
	/**
	 * 多个String值解析 ;多个配置可以用",; \t\n"中任意字符分割
	 */
	public static String[] getStringArray(String servletOrFilterName,Parameter parameter){
		assert parameter != null;
		return WebParameterUtils.getStringArray(servletOrFilterName, parameter.getName(), parameter.getDefault());
	}
	
	/**多个键值对解析*/
	public static Map<String, String> getStringMap(String servletOrFilterName,Parameter parameter) {
		assert parameter != null;
		return WebParameterUtils.getStringMap(servletOrFilterName, parameter.getName(), parameter.getDefault() );
    }
	
	/**多个键值对解析*/
	public static Map<String, String[]> getStringMultiMap(String servletOrFilterName,Parameter parameter) {
		assert parameter != null;
		return WebParameterUtils.getStringMultiMap(servletOrFilterName, parameter.getName(), parameter.getDefault() );
    }
	
	/**
	 * 单个Boolean值解析
	 */
	public static boolean getGlobalBoolean(Parameter parameter) {
		assert parameter != null;
		return WebParameterUtils.getBoolean(parameter.getName(), parameter.getDefault() );
	}
	
	/**
	 * 单个Boolean值解析
	 */
	public static boolean getGlobalBoolean(Parameter parameter,String def) {
		assert parameter != null;
		return WebParameterUtils.getBoolean(parameter.getName(), def );
	}
	
	/**
	 * 单个Int值解析
	 */
	public static int getGlobalInt(Parameter parameter) {
		assert parameter != null;
		return WebParameterUtils.getInt(parameter.getName(), parameter.getDefault() );
	}
	
	/**
	 * 单个Int值解析
	 */
	public static int getGlobalInt(Parameter parameter,String def) {
		assert parameter != null;
		return WebParameterUtils.getInt(parameter.getName(), def );
	}
	
	/**
	 * 单个Long值解析
	 */
	public static long getGlobalLong(Parameter parameter,String def) {
		assert parameter != null;
		return WebParameterUtils.getLong(parameter.getName(), def );
	}
	
	/**
	 * 单个Long值解析
	 */
	public static long getGlobalLong(Parameter parameter) {
		assert parameter != null;
		return WebParameterUtils.getLong(parameter.getName(), parameter.getDefault() );
	}
	
	/**
	 * 单个String值解析
	 */
	public static String getGlobalString(Parameter parameter) {
		assert parameter != null;
		return WebParameterUtils.getString(parameter.getName(), parameter.getDefault() );
	}
	
	/**
	 * 单个String值解析
	 */
	public static String getGlobalString(Parameter parameter,String def) {
		assert parameter != null;
		return WebParameterUtils.getString(parameter.getName(), def );
	}
	
	/**
	 * 多个String值解析 ;多个配置可以用",; \t\n"中任意字符分割
	 */
	public static String[] getGlobalStringArray(Parameter parameter){
		assert parameter != null;
		return WebParameterUtils.getStringArray(parameter.getName(), parameter.getDefault());
	}
	
	/**多个键值对解析*/
	public static Map<String, String> getGlobalStringMap(Parameter parameter) {
		assert parameter != null;
		return WebParameterUtils.getStringMap(parameter.getName(), parameter.getDefault() );
    }
	
	/**多个键值对解析*/
	public static Map<String, String[]> getGlobalStringMultiMap(Parameter parameter) {
		assert parameter != null;
		return WebParameterUtils.getStringMultiMap(parameter.getName(), parameter.getDefault() );
    }

	 
}
