package com.woshidaniu.qa.db;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

import com.woshidaniu.qa.tools.MessageHelper;


/**
 * @author 作者 E-mail:
 * @version 创建时间：2017年5月12日 下午2:14:34 类说明：自定义数据源
 */
public class ZTesterDataSource implements DataSource {

	private DataSource dataSource;

	private final DataSourceType type;
	private final String driver;
	private final String url;
	private final String username;
	private final String password;
	private final String schemaNames;

	public ZTesterDataSource(DataSourceType type, String driver, String url, String schemaNames, String user,
			String pass) {
		this.type = type;
		this.driver = driver;
		this.url = url;
		this.username = user;
		this.password = pass;
		this.schemaNames = schemaNames;
		this.dataSource=this.createDataSource();
	}

	/**
	 * 创建datasource
	 * 
	 * @return
	 */
	private DataSource createDataSource() {
		this.checkDoesTestDB();
		this.registerDriver();
		BasicDataSource dataSource = new BasicDataSource();
		MessageHelper.info("Creating data source. Driver: " + driver + ", url: " + url + ", user: " + username
				+ ", password: <not shown>");
		dataSource.setDriverClassName(driver);
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);

		// dataSource.setMaxActive(2);

		return dataSource;
	}

	Connection conn;

	/**
	 * 判断是否本地数据库或者是test数据库<br>
	 * 如果不是返回RuntimeException
	 */
	private void checkDoesTestDB() {

		if (url.contains("127.0.0.1") || url.toUpperCase().contains("LOCALHOST")) {
			return;
		}
		String[] schemas = this.schemaNames.split(";");
		for (String schema : schemas) {
			if (schema.trim().equals("")) {
				continue;
			}
			String temp = schema.toUpperCase();
			if (!temp.endsWith("TEST") && !temp.startsWith("TEST")) {
				throw new RuntimeException("only local db or test db will be allowed to connect,url:" + url
						+ ", schemas:" + this.schemaNames);
			}
		}
	}

	private final static Set<String> registered = new HashSet<String>();

	/**
	 * 注册数据库驱动
	 */
	private void registerDriver() {
		try {
			if (registered.contains(driver)) {
				return;
			}
			DriverManager.registerDriver((Driver) Class.forName(driver).newInstance());
			registered.add(driver);
		} catch (Throwable e) {
			throw new RuntimeException("Cannot register SQL driver " + driver);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.sql.CommonDataSource#getLogWriter()
	 */
	@Override
	public PrintWriter getLogWriter() throws SQLException {
		// TODO Auto-generated method stub
		return dataSource.getLogWriter();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.sql.CommonDataSource#setLogWriter(java.io.PrintWriter)
	 */
	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.sql.CommonDataSource#setLoginTimeout(int)
	 */
	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.sql.CommonDataSource#getLoginTimeout()
	 */
	@Override
	public int getLoginTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.sql.CommonDataSource#getParentLogger()
	 */
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Wrapper#unwrap(java.lang.Class)
	 */
	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		throw new RuntimeException("unimplement");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Wrapper#isWrapperFor(java.lang.Class)
	 */
	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		throw new RuntimeException("unimplement");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.sql.DataSource#getConnection()
	 */
	@Override
	public Connection getConnection() throws SQLException {
		// TODO Auto-generated method stub
		Connection conn = dataSource.getConnection();
		
		return conn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.sql.DataSource#getConnection(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		// TODO Auto-generated method stub
		Connection conn = dataSource.getConnection(username, password);
		return conn;
	}

}
