package com.woshidaniu.orm.mybatis.interceptor.meta;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;

import com.woshidaniu.orm.mybatis.utils.MetaObjectUtils;

public class MetaResultSetHandler {

	protected MetaObject metaObject;
	protected Executor executor;
	protected Configuration configuration;
	protected MappedStatement mappedStatement;
	protected RowBounds rowBounds;
	protected ParameterHandler parameterHandler;
	protected ResultHandler<?> resultHandler;
	protected BoundSql boundSql;
	protected TypeHandlerRegistry typeHandlerRegistry;
	protected ObjectFactory objectFactory;
	protected ReflectorFactory reflectorFactory;
	
	public MetaResultSetHandler(MetaObject metaObject, Executor executor, Configuration configuration,
			MappedStatement mappedStatement, RowBounds rowBounds,
			ParameterHandler parameterHandler, ResultHandler<?> resultHandler,
			BoundSql boundSql, TypeHandlerRegistry typeHandlerRegistry,
			ObjectFactory objectFactory, ReflectorFactory reflectorFactory) {
		this.metaObject = metaObject;
		this.executor = executor;
		this.configuration = configuration;
		this.mappedStatement = mappedStatement;
		this.rowBounds = rowBounds;
		this.parameterHandler = parameterHandler;
		this.resultHandler = resultHandler;
		this.boundSql = boundSql;
		this.typeHandlerRegistry = typeHandlerRegistry;
		this.objectFactory = objectFactory;
		this.reflectorFactory = reflectorFactory;
	}

	public static MetaResultSetHandler metaObject(ResultSetHandler resultSetHandler) {
		MetaObject metaObject = MetaObjectUtils.forObject(resultSetHandler);
		Executor executor = (Executor) metaObject.getValue("executor");
		Configuration configuration = (Configuration) metaObject.getValue("configuration");
		MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("mappedStatement");
		RowBounds rowBounds = (RowBounds) metaObject.getValue("rowBounds");
		ParameterHandler parameterHandler = (ParameterHandler) metaObject.getValue("parameterHandler");
		ResultHandler<?> resultHandler = (ResultHandler<?>) metaObject.getValue("resultHandler");
		BoundSql boundSql = (BoundSql) metaObject.getValue("boundSql");
		TypeHandlerRegistry typeHandlerRegistry = (TypeHandlerRegistry) metaObject.getValue("typeHandlerRegistry");
		ObjectFactory objectFactory = (ObjectFactory) metaObject.getValue("objectFactory");
		ReflectorFactory reflectorFactory = (ReflectorFactory) metaObject.getValue("reflectorFactory");
		return new MetaResultSetHandler(metaObject, executor, configuration, mappedStatement, rowBounds, parameterHandler, resultHandler, boundSql, typeHandlerRegistry, objectFactory, reflectorFactory);
	}
	
	public MetaObject getMetaObject() {
		return metaObject;
	}

	public void setMetaObject(MetaObject metaObject) {
		this.metaObject = metaObject;
	}

	public Executor getExecutor() {
		return executor;
	}

	public void setExecutor(Executor executor) {
		this.executor = executor;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
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

	public ParameterHandler getParameterHandler() {
		return parameterHandler;
	}

	public void setParameterHandler(ParameterHandler parameterHandler) {
		this.parameterHandler = parameterHandler;
	}

	public ResultHandler<?> getResultHandler() {
		return resultHandler;
	}

	public void setResultHandler(ResultHandler<?> resultHandler) {
		this.resultHandler = resultHandler;
	}

	public BoundSql getBoundSql() {
		return boundSql;
	}

	public void setBoundSql(BoundSql boundSql) {
		this.boundSql = boundSql;
	}

	public TypeHandlerRegistry getTypeHandlerRegistry() {
		return typeHandlerRegistry;
	}

	public void setTypeHandlerRegistry(TypeHandlerRegistry typeHandlerRegistry) {
		this.typeHandlerRegistry = typeHandlerRegistry;
	}

	public ObjectFactory getObjectFactory() {
		return objectFactory;
	}

	public void setObjectFactory(ObjectFactory objectFactory) {
		this.objectFactory = objectFactory;
	}

	public ReflectorFactory getReflectorFactory() {
		return reflectorFactory;
	}

	public void setReflectorFactory(ReflectorFactory reflectorFactory) {
		this.reflectorFactory = reflectorFactory;
	}

}
