package com.woshidaniu.qa;

/**
 * @author shouquan
 * @version 创建时间：2017年5月12日 上午11:09:31 类说明:Property文件类名
 */
public interface IPropItem {

	public static final String PROPKEY_MODULES = "ztester.modules";
	
	/**
	 * 数据库类名
	 */
	public static final String PROPKEY_DATABASE_TYPE = "database.type";

	public static final String PROPKEY_DATASOURCE_URL = "database.url";

	public static final String PROPKEY_DATASOURCE_SCHEMAS = "database.schemaNames";

	public static final String PROPKEY_DATASOURCE_USERNAME = "database.userName";

	public static final String PROPKEY_DATASOURCE_PASSWORD = "database.password";

	public static final String PROPKEY_DATASOURCE_DRIVERCLASSNAME = "database.driverClassName";
}
