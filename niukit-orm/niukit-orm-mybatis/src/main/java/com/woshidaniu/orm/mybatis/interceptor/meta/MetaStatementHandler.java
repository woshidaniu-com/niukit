package com.woshidaniu.orm.mybatis.interceptor.meta;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;

import com.woshidaniu.orm.mybatis.utils.MetaObjectUtils;

public class MetaStatementHandler {

	protected MetaObject metaObject;
	protected Configuration configuration;
	protected ObjectFactory objectFactory;
	protected TypeHandlerRegistry typeHandlerRegistry;
	protected ResultSetHandler resultSetHandler;
	protected ParameterHandler parameterHandler;
	protected Executor executor;
	protected MappedStatement mappedStatement;
	protected RowBounds rowBounds;
	protected BoundSql boundSql;
	
	public MetaStatementHandler(MetaObject metaObject, Configuration configuration,
			ObjectFactory objectFactory,
			TypeHandlerRegistry typeHandlerRegistry,
			ResultSetHandler resultSetHandler,
			ParameterHandler parameterHandler, Executor executor,
			MappedStatement mappedStatement, RowBounds rowBounds,
			BoundSql boundSql) {
		this.metaObject = metaObject;
		this.configuration = configuration;
		this.objectFactory = objectFactory;
		this.typeHandlerRegistry = typeHandlerRegistry;
		this.resultSetHandler = resultSetHandler;
		this.parameterHandler = parameterHandler;
		this.executor = executor;
		this.mappedStatement = mappedStatement;
		this.rowBounds = rowBounds;
		this.boundSql = boundSql;
	}

	public static MetaStatementHandler metaObject(StatementHandler statementHandler) {
		MetaObject metaObject = MetaObjectUtils.forObject(statementHandler);
		if(statementHandler instanceof RoutingStatementHandler){
			Configuration configuration = (Configuration) metaObject.getValue("delegate.configuration");
			ObjectFactory objectFactory = (ObjectFactory) metaObject.getValue("delegate.objectFactory");
			TypeHandlerRegistry typeHandlerRegistry = (TypeHandlerRegistry) metaObject.getValue("delegate.typeHandlerRegistry");
			ResultSetHandler resultSetHandler = (ResultSetHandler) metaObject.getValue("delegate.resultSetHandler");
			ParameterHandler parameterHandler = (ParameterHandler) metaObject.getValue("delegate.parameterHandler");
			Executor executor = (Executor) metaObject.getValue("delegate.executor");
			MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
			RowBounds rowBounds = (RowBounds) metaObject.getValue("delegate.rowBounds");
			BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
			return new MetaStatementHandler(metaObject, configuration, objectFactory, typeHandlerRegistry, resultSetHandler, parameterHandler, executor, mappedStatement, rowBounds, boundSql);
		}else {
			Configuration configuration = (Configuration) metaObject.getValue("configuration");
			ObjectFactory objectFactory = (ObjectFactory) metaObject.getValue("objectFactory");
			TypeHandlerRegistry typeHandlerRegistry = (TypeHandlerRegistry) metaObject.getValue("typeHandlerRegistry");
			ResultSetHandler resultSetHandler = (ResultSetHandler) metaObject.getValue("resultSetHandler");
			ParameterHandler parameterHandler = (ParameterHandler) metaObject.getValue("parameterHandler");
			Executor executor = (Executor) metaObject.getValue("executor");
			MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("mappedStatement");
			RowBounds rowBounds = (RowBounds) metaObject.getValue("rowBounds");
			BoundSql boundSql = (BoundSql) metaObject.getValue("boundSql");
			return new MetaStatementHandler(metaObject, configuration, objectFactory, typeHandlerRegistry, resultSetHandler, parameterHandler, executor, mappedStatement, rowBounds, boundSql);
		}
	}
	
	public MetaObject getMetaObject() {
		return metaObject;
	}

	public void setMetaObject(MetaObject metaObject) {
		this.metaObject = metaObject;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public ObjectFactory getObjectFactory() {
		return objectFactory;
	}

	public void setObjectFactory(ObjectFactory objectFactory) {
		this.objectFactory = objectFactory;
	}

	public TypeHandlerRegistry getTypeHandlerRegistry() {
		return typeHandlerRegistry;
	}

	public void setTypeHandlerRegistry(TypeHandlerRegistry typeHandlerRegistry) {
		this.typeHandlerRegistry = typeHandlerRegistry;
	}

	public ResultSetHandler getResultSetHandler() {
		return resultSetHandler;
	}

	public void setResultSetHandler(ResultSetHandler resultSetHandler) {
		this.resultSetHandler = resultSetHandler;
	}

	public ParameterHandler getParameterHandler() {
		return parameterHandler;
	}

	public void setParameterHandler(ParameterHandler parameterHandler) {
		this.parameterHandler = parameterHandler;
	}

	public Executor getExecutor() {
		return executor;
	}

	public void setExecutor(Executor executor) {
		this.executor = executor;
	}

	public MappedStatement getMappedStatement() {
		return mappedStatement;
	}

	public void setMappedStatement(MappedStatement mappedStatement) {
		this.mappedStatement = mappedStatement;
	}

	public RowBounds getRowBounds() {
		return rowBounds;
	}

	public void setRowBounds(RowBounds rowBounds) {
		this.rowBounds = rowBounds;
	}

	public BoundSql getBoundSql() {
		return boundSql;
	}

	public void setBoundSql(BoundSql boundSql) {
		this.boundSql = boundSql;
	}
	
}
