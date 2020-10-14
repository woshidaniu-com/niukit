/**
 * 
 */
package com.woshidaniu.migration.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.flywaydb.core.internal.util.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.migration.woshidaniuMigrationException;

/**
 * @author xiaobin.zhang
 * @desc 读取classpath中的配置文件
 */
public class ClassPathConfigReader implements ConfigReader {

	Logger logger = LoggerFactory.getLogger(ClassPathConfigReader.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.woshidaniu.migration.util.ConfigReader#read(org.flywaydb.core.internal.
	 * util.Location)
	 */
	@Override
	public Map<String, String> read(Location configFileLocation) {
		if (!configFileLocation.isClassPath())
			throw new woshidaniuMigrationException("ClassPathConfigReader只支持读取类路径下的配置文件。");

		InputStream configStream = this.getClass().getClassLoader().getResourceAsStream(
				configFileLocation.getPath() + File.separator + MIGRATION_PROPERTIES_FILE_NAME);

		Properties p = new Properties();

		try {
			p.load(configStream);
		} catch (IOException e) {
			logger.error("", e);
		} finally {
			try {
				if (null != configStream) {
					configStream.close();
				}
			} catch (IOException ex) {
				logger.error("", ex);
			}
		}

		return new HashMap<String, String>((Map) p);
	}

	@Override
	public boolean isSupport(Location location) {
		return location.isClassPath();
	}

}
