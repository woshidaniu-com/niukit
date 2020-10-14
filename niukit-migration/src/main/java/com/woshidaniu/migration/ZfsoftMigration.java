/**
 * 
 */
package com.woshidaniu.migration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.internal.util.Location;
import org.flywaydb.core.internal.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.migration.util.ClassPathConfigReader;
import com.woshidaniu.migration.util.ConfigReader;
import com.woshidaniu.migration.util.FileSystemConfigReader;

/**
 * @author xiaobin.zhang
 *
 * @desc 屏蔽一些危险的配置 <br>
 *       <strong>cleanOnValidationError=false; </strong><br>
 *       <strong>cleanDisabled=true; </strong><br>
 * @desc 统一配置方式
 */
public class woshidaniuMigration {

	public static final String DEFAULT_CONFIG_FILE_PATH = "classpath:migration.properties";
	
	public static final String DEFAULT_MIGRATION_SCHEMA_TABLE = "niutal_MIGRATION";
	
	public static final String DEFAULT_MIGRATION_USER = "woshidaniu.COM";

	public static final ConfigReader CLASSPATH_CONFIG_READER = new ClassPathConfigReader();
	public static final ConfigReader FILESYSTEM_CONFIG_READER = new FileSystemConfigReader();
	
	static final Logger logger = LoggerFactory.getLogger(woshidaniuMigration.class);
	
	/**
	 * 迁移配置文件所在的文件路径
	 */
	private String configFilePath = DEFAULT_CONFIG_FILE_PATH;

	/**
	 * 迁移配置文件路径对象
	 */
	private Location configFileLocation;

	/**
	 * 内置配置文件解析器
	 */
	private List<ConfigReader> configReaders = new ArrayList<ConfigReader>();
	
	/**
	 * 自定义配置文件解析器
	 */
	private List<ConfigReader> customConfigReaders;
	
	/**
	 * flyway
	 */
	private Flyway flyway;
	
	
	public woshidaniuMigration(){
		init();
	}
	
	/**
	 * 构造函数
	 * 
	 * @param configFilePath
	 */
	public woshidaniuMigration(String configFilePath) {
		if (StringUtils.hasText(configFilePath)) {
			setConfigFilePath(configFilePath);
		}
		init();
	}

	private void init() {
		
		configFileLocation = new Location(getConfigFilePath());
		
		if (logger.isDebugEnabled()) {
			logger.debug("woshidaniu migration is init..." + configFileLocation);
		}
		
		configReaders.add(CLASSPATH_CONFIG_READER);
		configReaders.add(FILESYSTEM_CONFIG_READER);
		
		if(customConfigReaders != null){
			configReaders.addAll(customConfigReaders);
		}
		
		Map<String,String> config = new HashMap<String, String>();
		
		for (ConfigReader configReader : configReaders) {
			if(!configReader.isSupport(configFileLocation)){
				continue;
			}
			config = configReader.read(configFileLocation);
		}
		
		flyway = new Flyway();
		
		// 配置flyway
		flyway.configure(config);
		
		// 固定schema table
		flyway.setTable(DEFAULT_MIGRATION_SCHEMA_TABLE);
		// 固定installby user
		flyway.setInstalledBy(DEFAULT_MIGRATION_USER);
		
		// 永久禁用clean功能，此功能非常危险，如果疏忽导致线上数据库被清空！
		flyway.setCleanDisabled(Boolean.TRUE);
		flyway.setCleanOnValidationError(Boolean.FALSE);

	}

	/**
	 * 调用flyway处理脚本升级任务
	 * @return
	 */
	public int migrate(){
		return flyway.migrate();
	}
	
	
	public String getConfigFilePath() {
		return configFilePath;
	}

	public void setConfigFilePath(String configFilePath) {
		this.configFilePath = configFilePath;
	}

	public Location getConfigFileLocation() {
		return configFileLocation;
	}

	public void setConfigFileLocation(Location configFileLocation) {
		this.configFileLocation = configFileLocation;
	}

	public List<ConfigReader> getCustomConfigReaders() {
		return customConfigReaders;
	}

	public void setCustomConfigReaders(List<ConfigReader> customConfigReaders) {
		this.customConfigReaders = customConfigReaders;
	}

}
