/**
 * 
 */
package com.woshidaniu.migration.util;

import java.io.File;
import java.io.FileInputStream;
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
 * @desc 读取文件系统的配置文件
 */
public class FileSystemConfigReader implements ConfigReader {

	Logger logger = LoggerFactory.getLogger(FileSystemConfigReader.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.woshidaniu.migration.util.ConfigReader#read(org.flywaydb.core.internal.
	 * util.Location)
	 */
	@Override
	public Map<String, String> read(Location configFileLocation) {
		if (!configFileLocation.isFileSystem())
			throw new woshidaniuMigrationException("FileSystemConfigReader只支持读取系统文件路径下的配置文件。");

		File configFile = new File(configFileLocation.getPath() + File.pathSeparator + MIGRATION_PROPERTIES_FILE_NAME);

		if (configFile.exists() || configFile.isDirectory() || !configFile.canRead()) {
			throw new woshidaniuMigrationException("配置文件路径不是合法的文件，该文件路径为空或是个目录或则不可读。");
		}

		InputStream configStream = null;

		Properties p = new Properties();

		try {
			configStream = new FileInputStream(configFile);
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
		return location.isFileSystem();
	}

}
