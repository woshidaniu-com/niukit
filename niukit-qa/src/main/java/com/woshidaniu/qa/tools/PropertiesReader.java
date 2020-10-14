package com.woshidaniu.qa.tools;

import static com.woshidaniu.qa.utils.IOUtils.closeQuietly;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

import com.woshidaniu.qa.exception.ZTesterException;
import com.woshidaniu.qa.tools.MessageHelper;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2017年5月12日 上午11:20:34 类说明：Properties文件读取
 */

public class PropertiesReader {
	
	/**
	 * 从用户目录加载配置文件
	 * @param propertiesFileName
	 * @return
	 */
	public Properties loadPropertiesFileFromUserHome(String propertiesFileName) {
		InputStream inputStream = null;
		try {
			if ("".equals(propertiesFileName)) {
				throw new IllegalArgumentException("Properties Filename must be given.");
			}
			Properties properties = new Properties();
			String userHomeDir = System.getProperty("user.home");
			File localPropertiesFile = new File(userHomeDir, propertiesFileName);
			if (!localPropertiesFile.exists()) {
				return null;
			}
			inputStream = new FileInputStream(localPropertiesFile);
			properties.load(inputStream);
			MessageHelper.info("Loaded configuration file " + propertiesFileName + " from user home");
			return properties;

		} catch (FileNotFoundException e) {
			return null;
		} catch (Throwable e) {
			throw new ZTesterException("Unable to load configuration file: " + propertiesFileName + " from user home",
					e);
		} finally {
			closeQuietly(inputStream);
		}
	}

	/**
	 * 从classpath加载配置文件
	 * @param propertiesFileName
	 * @return
	 */
	public Properties loadPropertiesFileFromClasspath(String propertiesFileName) {
		InputStream inputStream = null;
		try {
			if ("".equals(propertiesFileName)) {
				throw new IllegalArgumentException("Properties Filename must be given.");
			}
			Properties properties = new Properties();
			inputStream = ResourceHelper.getResourceAsStream(propertiesFileName);
			if (inputStream == null) {
				return null;
			}
			properties.load(inputStream);
			return properties;
		} catch (FileNotFoundException e) {
			return null;
		} catch (Throwable e) {
			throw new ZTesterException("Unable to load configuration file: " + propertiesFileName, e);
		} finally {
			closeQuietly(inputStream);
		}
	}
}
