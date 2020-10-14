package com.woshidaniu.qa.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.woshidaniu.qa.IPropItem;
import com.woshidaniu.qa.exception.ZTesterException;
import com.woshidaniu.qa.utils.ConfigurationLoader;

/**
 * @author shouquan
 * @version 创建时间：2017年5月12日 上午11:13:06 类说明:配置文件工具类
 */

public class ConfigHelper implements IPropItem {

	private static Properties proterties = null;

	public static Properties getConfiguration() {
		if (proterties == null) {
			proterties = ConfigurationLoader.loading();
		}
		return proterties;
	}

	public static int logLevel() {
		String level = getString("log.level", "INFO");
		if ("DEBUG".equalsIgnoreCase(level)) {
			return MessageHelper.DEBUG;
		} else if ("INFO".equalsIgnoreCase(level)) {
			return MessageHelper.INFO;
		} else if ("WARNING".equalsIgnoreCase(level)) {
			return MessageHelper.WARNING;
		} else if ("ERROR".equalsIgnoreCase(level)) {
			return MessageHelper.ERROR;
		}
		return MessageHelper.INFO;
	}

	public static String getString(String key) {
		String value = getConfiguration().getProperty(key);
		return value;
	}

	public static String getString(String key, String defaultValue) {
		String value = getConfiguration().getProperty(key);
		if (StringHelper.isBlankOrNull(value)) {
			return defaultValue;
		} else {
			return value.trim();
		}
	}

	public static String getString(Properties properties, String key) {
		String value = getString(properties, key, "");
		if (StringHelper.isBlankOrNull(value)) {
			throw new ZTesterException("No value found for property " + key);
		} else {
			return value.trim();
		}
	}

	public static String getString(Properties properties, String key, String defaultValue) {
		String value = null;
		if (properties == null) {
			value = getConfiguration().getProperty(key);
		} else {
			value = properties.getProperty(key);
			if (StringHelper.isBlankOrNull(value)) {
				value = getConfiguration().getProperty(key);
			}
		}
		if (StringHelper.isBlankOrNull(value)) {
			return defaultValue;
		} else {
			return value.trim();
		}
	}

	/**
	 * 数据库驱动
	 * 
	 * @return
	 */
	public static String databaseDriver() {
		return getConfiguration().getProperty(PROPKEY_DATASOURCE_DRIVERCLASSNAME);
	}

	/**
	 * 数据库url
	 * 
	 * @return
	 */
	public static String databaseUrl() {
		return getConfiguration().getProperty(PROPKEY_DATASOURCE_URL);
	}

	public static String databaseUserName() {
		return getConfiguration().getProperty(PROPKEY_DATASOURCE_USERNAME);
	}

	public static String databasePassword() {
		return getConfiguration().getProperty(PROPKEY_DATASOURCE_PASSWORD);
	}

	public static String databaseType() {
		String type = System.getProperty(PROPKEY_DATABASE_TYPE);// from vm
		if (!StringHelper.isBlankOrNull(type)) {
			return type;
		}
		// from jTester property
		type = getConfiguration().getProperty(PROPKEY_DATABASE_TYPE);
		return type;
	}

	public static List<String> getStringList(String propertyName) {
		return getStringList(propertyName, false);
	}

	public static List<String> getStringList(String propertyName, boolean required) {
		String values = getString(propertyName);
		if (values == null || "".equals(values.trim())) {
			if (required) {
				throw new ZTesterException("No value found for property " + propertyName);
			}
			return new ArrayList<String>(0);
		}
		String[] splitValues = values.split(",");
		List<String> result = new ArrayList<String>(splitValues.length);
		for (String value : splitValues) {
			result.add(value.trim());
		}

		if (required && result.isEmpty()) {
			throw new ZTesterException("No value found for property " + propertyName);
		}
		return result;
	}

	public static boolean getBoolean(String key) {
		String prop = getConfiguration().getProperty(key);
		return "true".equalsIgnoreCase(prop);
	}

	public static boolean getBoolean(String key, boolean defaultValue) {
		String value = getConfiguration().getProperty(key);
		if (StringHelper.isBlankOrNull(value)) {
			return defaultValue;
		} else {
			return "true".equalsIgnoreCase(value);
		}
	}
}
