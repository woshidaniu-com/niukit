package com.woshidaniu.qa.db;

import javax.sql.DataSource;

/**
 * @author
 * @version 创建时间：2017年5月12日 下午2:31:54 类说明
 */
public interface TransactionManager {
	DataSource getTransactionalDataSource(DataSource dataSource);

	/**
	 * Starts a transaction.
	 */
	void startTransaction();

	/**
	 * Commits the currently active transaction. This transaction must have been
	 * initiated by calling {@link #startTransaction(Object)} with the same
	 * testObject within the same thread.
	 */
	void commit();

	/**
	 * Rolls back the currently active transaction. This transaction must have
	 * been initiated by calling {@link #startTransaction(Object)} with the same
	 * testObject within the same thread.
	 */
	void rollback();

	void activateTransactionIfNeeded();
}
