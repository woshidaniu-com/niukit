/**
 * 
 */
package com.woshidaniu.migration.util;

import java.util.Map;

import org.flywaydb.core.internal.util.Location;

/**
 * @author xiaobin.zhang
 * 
 * @desc 配置文件解析
 */
public interface ConfigReader {

	public static final String MIGRATION_PROPERTIES_FILE_NAME = "migration.properties";
	
	/**
	 * 
	 * @param configFileLocation
	 * @return
	 */
	Map<String, String> read(Location configFileLocation);
	
	/**
	 * 
	 * @param location
	 * @return
	 */
	boolean isSupport(Location location);
	
}
