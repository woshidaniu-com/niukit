package com.woshidaniu.orm.mybatis.interceptor.meta;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;

import com.woshidaniu.orm.mybatis.utils.MetaObjectUtils;

public class MetaParameterHandler {

	protected MetaObject metaObject;
	protected Configuration configuration;
	protected TypeHandlerRegistry typeHandlerRegistry;
	protected MappedStatement mappedStatement;
	protected Object parameterObject;
	protected BoundSql boundSql;
	
	protected MetaParameterHandler(MetaObject metaObject, Configuration configuration,
			TypeHandlerRegistry typeHandlerRegistry,
			MappedStatement mappedStatement, Object parameterObject,
			BoundSql boundSql) {
		this.metaObject = metaObject;
		this.configuration = configuration;
		this.typeHandlerRegistry = typeHandlerRegistry;
		this.mappedStatement = mappedStatement;
		this.parameterObject = parameterObject;
		this.boundSql = boundSql;
	}
	
	public static MetaParameterHandler metaObject(ParameterHandler parameterHandler) {
		MetaObject metaObject = MetaObjectUtils.forObject(parameterHandler);
		TypeHandlerRegistry typeHandlerRegistry = (TypeHandlerRegistry) metaObject.getValue("typeHandlerRegistry");
		MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("mappedStatement");
		Object parameterObject = (Object) metaObject.getValue("parameterObject");
		BoundSql boundSql = (BoundSql) metaObject.getValue("boundSql");
		Configuration configuration = (Configuration) metaObject.getValue("configuration");
	    return new MetaParameterHandler(metaObject, configuration, typeHandlerRegistry, mappedStatement, parameterObject, boundSql);
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

	public TypeHandlerRegistry getTypeHandlerRegistry() {
		return typeHandlerRegistry;
	}

	public void setTypeHandlerRegistry(TypeHandlerRegistry typeHandlerRegistry) {
		this.typeHandlerRegistry = typeHandlerRegistry;
	}

	public MappedStatement getMappedStatement() {
		return mappedStatement;
	}

	public void setMappedStatement(MappedStatement mappedStatement) {
		this.mappedStatement = mappedStatement;
	}

	public Object getParameterObject() {
		return parameterObject;
	}

	public void setParameterObject(Object parameterObject) {
		this.parameterObject = parameterObject;
	}

	public BoundSql getBoundSql() {
		return boundSql;
	}

	public void setBoundSql(BoundSql boundSql) {
		this.boundSql = boundSql;
	}
	
}
