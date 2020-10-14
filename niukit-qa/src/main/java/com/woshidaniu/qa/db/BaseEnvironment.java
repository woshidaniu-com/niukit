package com.woshidaniu.qa.db;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceUtils;

import com.woshidaniu.qa.core.TestContext;
import com.woshidaniu.qa.exception.ZTesterException;
import com.woshidaniu.qa.types.AbstractTypeMap;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2017年5月12日 下午2:12:45 类说明 基础数据库环境类
 */
public class BaseEnvironment implements DBEnvironment {

	protected final String dataSourceName;

	protected final String dataSourceFrom;

	protected DataSourceType dataSourceType;

	protected AbstractTypeMap typeMap;
	
	private ZTesterDataSource dataSource;

	protected BaseEnvironment(DataSourceType dataSourceType, String dataSourceName, String dataSourceFrom) {
		this.dataSourceName = dataSourceName;
		this.dataSourceFrom = dataSourceFrom;
		this.dataSourceType = dataSourceType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.woshidaniu.qa.db.DBEnvironment#setDataSource(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void setDataSource(String driver, String url, String schemas, String username, String password) {
		// TODO Auto-generated method stub
		this.dataSource = new ZTesterDataSource(dataSourceType, driver, url, schemas, username, password);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.woshidaniu.qa.db.DBEnvironment#getDataSource()
	 */
	@Override
	public DataSource getDataSource() {
		// TODO Auto-generated method stub
		return this.dataSource;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.woshidaniu.qa.db.DBEnvironment#connect()
	 */
	@Override
	public Connection connect() {
		// TODO Auto-generated method stub
		ThreadTransactionManager currMethodConnection = threadTransactionManager.get();
		if (currMethodConnection == null) {
			currMethodConnection = new ThreadTransactionManager();
			threadTransactionManager.set(currMethodConnection);
		}
		Connection connection = currMethodConnection.getConnection();
		if (connection != null) {
			return connection;
		}
		DataSource dataSource = this.getDataSourceAndActivateTransactionIfNeeded();
		try {
			connection = currMethodConnection.initMethodConnection(dataSource);
			return connection;
		} catch (SQLException e) {
			throw new ZTesterException(e);
		}
	}

	private final ThreadLocal<ThreadTransactionManager> threadTransactionManager = new ThreadLocal<ThreadTransactionManager>();

	private class ThreadTransactionManager {
		/**
		 * The transaction manager
		 */
		private TransactionManager transactionManager;

		private Object testedObject;

		private Method testedMethod;

		private Connection connection;

		public Connection getConnection() {
			if (this.testedObject != TestContext.currTestedObject()
					|| this.testedMethod != TestContext.currTestedMethod()) {
				this.connection = null;
			}
			return connection;
		}

		public Connection initMethodConnection(DataSource dataSource) throws SQLException {
			this.testedObject = TestContext.currTestedObject();
			this.testedMethod = TestContext.currTestedMethod();
			connection = DataSourceUtils.doGetConnection(dataSource);
			return connection;
		}

		public void release() {
			if (this.connection == null) {
				return;
			}
			DataSource dataSource = getDataSourceAndActivateTransactionIfNeeded();
			try {
				if (connection.isClosed() == false) {
					DataSourceUtils.doReleaseConnection(connection, dataSource);
				}
				this.connection = null;
			} catch (SQLException e) {
				throw new ZTesterException(
						String.format("close datasource[%s] connection error.", dataSource.toString()), e);
			}
		}

		boolean hasActivated = false;

		public void activateTransaction() {
			if (hasActivated == false && transactionManager != null) {
				transactionManager.activateTransactionIfNeeded();
				this.hasActivated = true;
			}
		}

		public void rollback() {
			if (this.transactionManager != null) {
				this.transactionManager.rollback();
			}
			this.release();
		}

		public void commit() {
			if (this.transactionManager != null) {
				transactionManager.commit();
			}
			this.release();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.woshidaniu.qa.db.DBEnvironment#getExceptionCode(java.sql.SQLException)
	 */
	@Override
	public int getExceptionCode(SQLException ex) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.woshidaniu.qa.db.DBEnvironment#createStatementWithBoundFixtureSymbols(
	 * java.lang.String)
	 */
	@Override
	public PreparedStatement createStatementWithBoundFixtureSymbols(String commandText) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.woshidaniu.qa.db.DBEnvironment#getDefaultValue(java.lang.String)
	 */
	@Override
	public Object getDefaultValue(String javaType) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.woshidaniu.qa.db.DBEnvironment#toObjectValue(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public Object toObjectValue(String input, String javaType) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.woshidaniu.qa.db.DBEnvironment#getFieldQuato()
	 */
	@Override
	public String getFieldQuato() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.woshidaniu.qa.db.DBEnvironment#converToSqlValue(java.lang.Object)
	 */
	@Override
	public Object converToSqlValue(Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.woshidaniu.qa.db.DBEnvironment#
	 * getDataSourceAndActivateTransactionIfNeeded()
	 */
	@Override
	public DataSource getDataSourceAndActivateTransactionIfNeeded() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.woshidaniu.qa.db.DBEnvironment#startTransaction()
	 */
	@Override
	public void startTransaction() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.woshidaniu.qa.db.DBEnvironment#endTransaction()
	 */
	@Override
	public void endTransaction() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.woshidaniu.qa.db.DBEnvironment#commit()
	 */
	@Override
	public void commit() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.woshidaniu.qa.db.DBEnvironment#rollback()
	 */
	@Override
	public void rollback() {
		// TODO Auto-generated method stub

	}

}
