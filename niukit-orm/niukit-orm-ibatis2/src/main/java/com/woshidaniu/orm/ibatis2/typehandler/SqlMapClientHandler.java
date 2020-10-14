package com.woshidaniu.orm.ibatis2.typehandler;

import java.io.IOException;
import java.io.Reader;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

public final class SqlMapClientHandler {
	
	protected static Logger LOG = LoggerFactory.getLogger(SqlMapClientHandler.class);
	protected static String DEFAULT_RESOUCE = "SqlMapConfig.xml";
	protected static SqlMapClientHandler instance = null;
	protected SqlMapClient sqlMapClient = null;
	
	public static SqlMapClientHandler getInstance()  {
		if (instance == null) {
			instance = new SqlMapClientHandler();
		}
		return instance;
	}
	
	public static SqlMapClientHandler getInstance(Reader reader) {
		if (instance == null) {
			instance = new SqlMapClientHandler(reader);
		}
		return instance;
	}
	
	public static SqlMapClientHandler getInstance(String resource)  {
		if (instance == null) {
			instance = new SqlMapClientHandler(resource);
		}
		return instance;
	}
	
	private SqlMapClientHandler()  {
		try {
			sqlMapClient =  SqlMapClientBuilder.buildSqlMapClient(Resources.getResourceAsReader(DEFAULT_RESOUCE));
		} catch (IOException e) {
			LOG.error(e.getMessage(),e.getCause());
		}
	}
	
	private SqlMapClientHandler(String resource)  {
		try {
			sqlMapClient =  SqlMapClientBuilder.buildSqlMapClient(Resources.getResourceAsReader(resource));
		} catch (IOException e) {
			getInstance();
			LOG.error(e.getMessage(),e.getCause());
		}
	}

	private SqlMapClientHandler(Reader reader) {
		try {
			sqlMapClient =  SqlMapClientBuilder.buildSqlMapClient(reader);
		} catch (Exception e) {
			LOG.error("default file 'SqlMapConfig.xml' not finded on root path !");
			LOG.error(e.getMessage(),e.getCause());
		}
	}

	public synchronized void setSqlMapClient(SqlMapClient sqlMapClient) {
		this.sqlMapClient = sqlMapClient;
	}
	
	public synchronized void startTransaction() {
		try {
			sqlMapClient.startTransaction();
		} catch (SQLException e) {
			LOG.error(e.getMessage(),e.getCause());
		}
	}
	
	public synchronized void endTransaction() {
		try {
			sqlMapClient.endTransaction();
		} catch (SQLException e) {
			LOG.error(e.getMessage(),e.getCause());
		}
	}

	public synchronized void commitTransaction() {
		try {
			sqlMapClient.commitTransaction();
		} catch (SQLException e) {
			LOG.error(e.getMessage(),e.getCause());
		}
	}
	
	public SqlMapClient getSqlMapClient() {
		return sqlMapClient;
	}
	
}



