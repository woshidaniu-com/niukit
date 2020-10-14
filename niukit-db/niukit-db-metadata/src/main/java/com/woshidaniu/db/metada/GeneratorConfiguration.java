package com.woshidaniu.db.metada;

/**
 * <p>
 * 生成器配置类
 ********************************** 
 * 使用前必读*********************
 * saveDir 文件生成目录
 * entity_package entity 包路径
 * mapper_package mapper 包路径
 * -------------------------------------
 * 以下数据库相关配置：
 * -------------------------------------
 * db_include_prefix 表是否包含前缀，例如: tb_xxx 其中 tb_ 为前缀
 * db_driverName 驱动
 * db_user 用户名
 * db_password 密码
 * db_url 连接地址
 **************************************************************
 * </p>
 * 
 * @author hubin
 * @Date 2016-01-23
 */
public class GeneratorConfiguration {

	private String saveDir;

	private String entityPackage;

	private String mapperPackage;

	/* db_config */
	private boolean dbPrefix = false;

	private String dbDriverName;

	private String dbUser;

	private String dbPassword;

	private String dbUrl;

	public String getSaveDir() {
		return saveDir;
	}

	public void setSaveDir(String saveDir) {
		this.saveDir = saveDir;
	}

	public String getEntityPackage() {
		return entityPackage;
	}

	public void setEntityPackage(String entityPackage) {
		this.entityPackage = entityPackage;
	}

	public String getMapperPackage() {
		return mapperPackage;
	}

	public void setMapperPackage(String mapperPackage) {
		this.mapperPackage = mapperPackage;
	}

	public boolean isDbPrefix() {
		return dbPrefix;
	}

	public void setDbPrefix(boolean dbPrefix) {
		this.dbPrefix = dbPrefix;
	}

	public String getDbDriverName() {
		return dbDriverName;
	}

	public void setDbDriverName(String dbDriverName) {
		this.dbDriverName = dbDriverName;
	}

	public String getDbUser() {
		return dbUser;
	}

	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}

	public String getDbPassword() {
		return dbPassword;
	}

	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}

	public String getDbUrl() {
		return dbUrl;
	}

	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}

}
