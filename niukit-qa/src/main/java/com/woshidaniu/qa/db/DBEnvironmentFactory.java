package com.woshidaniu.qa.db;

import static com.woshidaniu.qa.IPropItem.PROPKEY_DATABASE_TYPE;
import static com.woshidaniu.qa.IPropItem.PROPKEY_DATASOURCE_DRIVERCLASSNAME;
import static com.woshidaniu.qa.IPropItem.PROPKEY_DATASOURCE_PASSWORD;
import static com.woshidaniu.qa.IPropItem.PROPKEY_DATASOURCE_SCHEMAS;
import static com.woshidaniu.qa.IPropItem.PROPKEY_DATASOURCE_URL;
import static com.woshidaniu.qa.IPropItem.PROPKEY_DATASOURCE_USERNAME;
import static com.woshidaniu.qa.db.DBEnvironment.CUSTOMIZED_DATASOURCE_NAME;
import static com.woshidaniu.qa.db.DBEnvironment.DEFAULT_DATASOURCE_FROM;
import static com.woshidaniu.qa.db.DBEnvironment.DEFAULT_DATASOURCE_NAME;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.woshidaniu.qa.tools.ConfigHelper;
import com.woshidaniu.qa.tools.ResourceHelper;
import com.woshidaniu.qa.tools.StringHelper;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2017年5月12日 下午2:55:20 类说明
 */
public final class DBEnvironmentFactory {

	private static Map<String, DBEnvironment> environments = new HashMap<String, DBEnvironment>();

	/**
	 * 
	 * @param dataSourceType
	 * @param dataSourceName
	 * @param dataSourceFrom
	 * @return
	 */
	private static DBEnvironment newInstance(DataSourceType dataSourceType, String dataSourceName,
			String dataSourceFrom) {
		if (dataSourceType == null) {
			throw new RuntimeException("DatabaseType can't be null.");
		}
		switch (dataSourceType) {
		// case "MYSQL":
		// return new MySqlEnvironment(dataSourceName, dataSourceFrom);
		case ORACLE:
			return new OracleEnvironment(dataSourceName, dataSourceFrom);
		default:
			throw new RuntimeException("unsupport database type:" + dataSourceType);
		}
	}

	/**
	 * 构造自定义的数据库连接识别码
	 * 
	 * @param type
	 * @param driver
	 * @param url
	 * @param username
	 * @param password
	 * @return
	 */
	public static DBEnvironment getDBEnvironment(String type, String driver, String url, String username,
			String password) {
		DataSourceType dataSourceType = DataSourceType.databaseType(type);
		String dataSourceFrom = "customized-" + UUID.randomUUID().toString();
		DBEnvironment enviroment = newInstance(dataSourceType, CUSTOMIZED_DATASOURCE_NAME, dataSourceFrom);
		environments.put(DEFAULT_DATASOURCE_NAME + "=" + dataSourceFrom, enviroment);

		if (StringHelper.isBlankOrNull(driver)) {
			driver = ConfigHelper.databaseDriver();
		}
		if (StringHelper.isBlankOrNull(url)) {
			url = ConfigHelper.databaseUrl();
		}
		if (StringHelper.isBlankOrNull(username)) {
			username = ConfigHelper.databaseUserName();
		}
		if (StringHelper.isBlankOrNull(password)) {
			password = ConfigHelper.databasePassword();
		}
		enviroment.setDataSource(driver, url, "", username, password);

		return enviroment;
	}

	final static String NO_VALID_VALUE_MESSAGE = "can't find valid value of key[%s] in file[%s]!";

	/**
	 * 获取默认的数据库连接识别码
	 * 
	 * @return
	 */
	public static DBEnvironment getDefaultDBEnvironment() {
		DBEnvironment enviroment = getDBEnvironment(DEFAULT_DATASOURCE_NAME, DEFAULT_DATASOURCE_FROM);
		return enviroment;
	}

	/**
	 * 从ztester配置中取指定的数据源
	 * 
	 * @param dataSourceName
	 * @return
	 */
	public static DBEnvironment getDBEnvironment(String dataSourceName) {
		if (StringUtils.isEmpty(dataSourceName) || StringUtils.isBlank(dataSourceName)) {
			dataSourceName = DEFAULT_DATASOURCE_NAME;
		}
		DBEnvironment enviroment = getDBEnvironment(dataSourceName, DEFAULT_DATASOURCE_FROM);
		return enviroment;
	}

	/**
	 * 从文件中获取指定的数据库连接识别码
	 * 
	 * @param dataSourceName
	 * @param dataSourceFrom
	 * @return
	 */
	public static DBEnvironment getDBEnvironment(String dataSourceName, String dataSourceFrom) {
		dataSourceName = StringHelper.isBlankOrNull(dataSourceName) ? DEFAULT_DATASOURCE_NAME : dataSourceName;// 默认的数据库名称
		dataSourceFrom = StringHelper.isBlankOrNull(dataSourceFrom) ? DEFAULT_DATASOURCE_FROM : dataSourceFrom;// 默认的数据库配置文件

		DBEnvironment enviroment = environments.get(dataSourceName + "=" + dataSourceFrom);
		if (enviroment == null) {
			Properties props = null;
			if (StringHelper.isBlankOrNull(dataSourceFrom) == false
					&& DEFAULT_DATASOURCE_FROM.equalsIgnoreCase(dataSourceFrom) == false) {
				props = ResourceHelper.loadPropertiesFrom(dataSourceFrom);// 加载配置文件
			}
			String typeProperty = ConfigHelper.getString(props, getMergeKey(dataSourceName, PROPKEY_DATABASE_TYPE));
			DataSourceType dataSourceType = DataSourceType.databaseType(typeProperty);

			enviroment = newInstance(dataSourceType, dataSourceName, dataSourceFrom);

			environments.put(dataSourceName + "=" + dataSourceFrom, enviroment);

			String driver = ConfigHelper.getString(props,
					getMergeKey(dataSourceName, PROPKEY_DATASOURCE_DRIVERCLASSNAME));
			String url = ConfigHelper.getString(props, getMergeKey(dataSourceName, PROPKEY_DATASOURCE_URL));
			String user = ConfigHelper.getString(props, getMergeKey(dataSourceName, PROPKEY_DATASOURCE_USERNAME));
			if (StringHelper.isBlankOrNull(driver) || StringHelper.isBlankOrNull(url)
					|| StringHelper.isBlankOrNull(user)) {
				throw new RuntimeException(String.format(NO_VALID_VALUE_MESSAGE,
						dataSourceName + "." + PROPKEY_DATASOURCE_USERNAME, dataSourceFrom));
			}
			String pass = ConfigHelper.getString(props, getMergeKey(dataSourceName, PROPKEY_DATASOURCE_PASSWORD));
			if (pass == null) {
				pass = "";
			}
			String schemas = ConfigHelper.getString(props, getMergeKey(dataSourceName, PROPKEY_DATASOURCE_SCHEMAS), "");
			schemas = schemas == null ? "" : schemas;

			enviroment.setDataSource(driver, url, schemas, user, pass);
		}

		return enviroment;
	}

	private static String getMergeKey(String dataSourceName, String key) {
		if (StringHelper.isBlankOrNull(dataSourceName) || DEFAULT_DATASOURCE_NAME.equalsIgnoreCase(dataSourceName)) {
			return key;
		} else {
			return dataSourceName + "." + key;
		}
	}

	/**
	 * 当前正在使用的数据库类型
	 */
	private static DBEnvironment currDBEnvironment = null;

	/**
	 * 获取当前的数据库处理环境
	 * 
	 * @return
	 */
	public static DBEnvironment getCurrentDBEnvironment() {
		if (currDBEnvironment == null) {
			currDBEnvironment = getDefaultDBEnvironment();
		}
		return currDBEnvironment;
	}

	/**
	 * 切换数据库环境<br>
	 * 先关闭上一个数据库连接，再设置当前数据库连接
	 * 
	 * @param DataSourceIdentify
	 * @throws SQLException
	 */
	public static void changeDBEnvironment(DBEnvironment environment) {
		currDBEnvironment = environment;
	}

	/**
	 * 切换数据源<br>
	 * 先关闭上一个数据库连接，再设置当前数据库连接
	 * 
	 * @param dataSourceName
	 *            Tester中配置的数据源名称
	 */
	public static void changeDBEnvironment(String dataSourceName) {
		if (StringUtils.isEmpty(dataSourceName) || StringUtils.isBlank(dataSourceName)) {
			dataSourceName = DEFAULT_DATASOURCE_NAME;
		}
		DBEnvironment specEnvironment = DBEnvironmentFactory.getDBEnvironment(dataSourceName);
		DBEnvironmentFactory.changeDBEnvironment(specEnvironment);
	}


	private final static String DEFAULT_ENVIRONMENT_KEY = DEFAULT_DATASOURCE_NAME + "=" + DEFAULT_DATASOURCE_FROM;

	/**
	 * 结束所有可能的事务
	 * 
	 * @throws SQLException
	 */
	public static void closeDBEnvironment() {
		StringBuilder err = new StringBuilder();
		for (Map.Entry<String, DBEnvironment> environment : environments.entrySet()) {
			try {
				if (environment.getKey().equals(DEFAULT_ENVIRONMENT_KEY)) {
					environment.getValue().endTransaction();
				}
			} catch (Throwable e) {
				err.append(StringHelper.exceptionTrace(e));
			}
		}

		String msg = err.toString();
		if ("".equalsIgnoreCase(msg.trim()) == false) {
			throw new RuntimeException(msg);
		}
	}

}
