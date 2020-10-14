package com.woshidaniu.qa.utils;

import java.util.Properties;


import com.woshidaniu.qa.exception.ZTesterException;
import com.woshidaniu.qa.tools.ConfigHelper;
import com.woshidaniu.qa.tools.MessageHelper;
import com.woshidaniu.qa.tools.PropertiesReader;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2017年5月12日 下午1:45:25 类说明:配置文件加载类
 */
public class ConfigurationLoader {

	public static final String DEFAULT_PROPERTIES_FILE_NAME = "ztester.properties";// 默认的配置文件名称

	public static final String PROPKEY_CUSTOM_CONFIGURATION = "ztester.configuration.customFileName";

	public static final String PROPKEY_LOCAL_CONFIGURATION = "ztester.configuration.localFileName";

	private static Properties properties = null;

	private PropertiesReader propertiesReader = new PropertiesReader();

	/**
	 * 加载配置文件
	 * 
	 * @return
	 */
	public static synchronized Properties loading() {
		if (properties == null) {
			ConfigurationLoader loader = new ConfigurationLoader();
			properties = new Properties();

			loader.loadDefaultConfiguration(properties);
			loader.loadCustomConfiguration(properties);
			loader.loadLocalConfiguration(properties);
			loader.loadSystemProperties(properties);//系统内置配置文件
		}
		return properties;
	}

	private ConfigurationLoader() {

	}

	private void loadDefaultConfiguration(Properties properties) {
		Properties defaultProperties = propertiesReader.loadPropertiesFileFromClasspath(DEFAULT_PROPERTIES_FILE_NAME);
		if (defaultProperties == null) {
			throw new ZTesterException(
					"Configuration file: " + DEFAULT_PROPERTIES_FILE_NAME + " not found in classpath.");
		}
		properties.putAll(defaultProperties);
	}

	private void loadCustomConfiguration(Properties properties) {
		String customConfigurationFileName = getConfigurationFileName(PROPKEY_CUSTOM_CONFIGURATION, properties);
		Properties customProperties = propertiesReader.loadPropertiesFileFromClasspath(customConfigurationFileName);
		if (customProperties == null) {
			MessageHelper.warn("No custom configuration file " + customConfigurationFileName + " found.");
		} else {
			properties.putAll(customProperties);
		}
	}

	private String getConfigurationFileName(String propertyName, Properties properties) {
		String configurationFileName = System.getProperty(propertyName);
		if (configurationFileName != null) {
			return configurationFileName;
		}
		return ConfigHelper.getString(properties, propertyName);
	}

	private void loadLocalConfiguration(Properties properties) {
		String localConfigurationFileName = getConfigurationFileName(PROPKEY_LOCAL_CONFIGURATION, properties);
		Properties localProperties = propertiesReader.loadPropertiesFileFromClasspath(localConfigurationFileName);
		if (localProperties == null) {
			localProperties = propertiesReader.loadPropertiesFileFromUserHome(localConfigurationFileName);
		}
		if (localProperties == null) {
			MessageHelper.info("No local configuration file " + localConfigurationFileName + " found.");
		} else {
			properties.putAll(localProperties);
		}
	}

	private void loadSystemProperties(Properties properties) {
		properties.putAll(System.getProperties());
	}

}
