package com.woshidaniu.orm.mybatis.interceptor.meta;

import org.apache.ibatis.executor.CachingExecutor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.transaction.Transaction;

import com.woshidaniu.orm.mybatis.utils.MetaObjectUtils;

public class MetaExecutor {

	protected MetaObject metaObject;
	protected Transaction transaction;
	protected Configuration configuration;

	protected MetaExecutor(MetaObject metaObject, Transaction transaction, Configuration configuration) {
		this.metaObject = metaObject;
		this.transaction = transaction;
		this.configuration = configuration;
	}

	public static MetaExecutor metaObject(Executor executor) {
		MetaObject metaObject = MetaObjectUtils.forObject(executor);
		if(executor instanceof CachingExecutor){
			// 获取当前MappedStatement的Mybatis Configuration对象
			Configuration configuration = (Configuration) metaObject.getValue("delegate.configuration");
			Transaction transaction = (Transaction) metaObject.getValue("delegate.transaction");
			return new MetaExecutor(metaObject, transaction, configuration);
		}else {
			// 获取当前MappedStatement的Mybatis Configuration对象
			Configuration configuration = (Configuration) metaObject.getValue("configuration");
			Transaction transaction = (Transaction) metaObject.getValue("transaction");
			return new MetaExecutor(metaObject, transaction, configuration);
		}
	}
	
	public MetaObject getMetaObject() {
		return metaObject;
	}

	public void setMetaObject(MetaObject metaObject) {
		this.metaObject = metaObject;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

}
