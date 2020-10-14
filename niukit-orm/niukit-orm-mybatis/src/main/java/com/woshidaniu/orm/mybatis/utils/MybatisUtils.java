package com.woshidaniu.orm.mybatis.utils;

import java.sql.Connection;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.sql.DataSource;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.executor.result.DefaultResultHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.defaults.DefaultSqlSession.StrictMap;
import org.apache.ibatis.transaction.Transaction;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.managed.ManagedTransactionFactory;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class MybatisUtils {
	
	protected static Logger LOG = LoggerFactory.getLogger(MybatisUtils.class);
	protected static final ConcurrentMap<ObjectFactory, ResultHandler<Object>> handlersMap = new ConcurrentHashMap<ObjectFactory, ResultHandler<Object>>();
	
	public static Object getTarget(Object target) {
		MetaObject metaTarget = MetaObjectUtils.forObject(target);
		if(metaTarget.hasGetter("h") || metaTarget.hasGetter("target")){
			// 分离代理对象链(由于目标类可能被多个拦截器拦截，从而形成多次代理，通过下面的两次循环可以分离出最原始的的目标类)
			// 分离 Plugin 对象
			while (metaTarget.hasGetter("h")) {
				target = metaTarget.getValue("h");
				metaTarget = MetaObjectUtils.forObject(target);
			}
			// 分离最后一个代理对象的目标类
			while (metaTarget.hasGetter("target")) {
				target = metaTarget.getValue("target");
				metaTarget = MetaObjectUtils.forObject(target);
			}
			return getTarget(target);
		}
		return target;
	}
	
	public static TransactionFactory getTransactionFactoryFromEnvironment(Configuration configuration) {
		final Environment environment = configuration.getEnvironment();
		return getTransactionFactoryFromEnvironment(environment);
	}

	public static TransactionFactory getTransactionFactoryFromEnvironment(Environment environment) {
		if (environment == null || environment.getTransactionFactory() == null) {
			return new ManagedTransactionFactory();
		}
		return environment.getTransactionFactory();
	}
	
	public static DataSource getDataSourceFromEnvironment(Configuration configuration) {
		final Environment environment = configuration.getEnvironment();
		return environment.getDataSource();
	}
	
	public static Executor newExecutor(Configuration configuration) {
		final Environment environment = configuration.getEnvironment();
		if (environment == null) {
			throw new ExecutorException("Environment was not configured.");
		}
		final DataSource ds = environment.getDataSource();
		if (ds == null) {
			throw new ExecutorException("DataSource was not configured.");
		}
		final TransactionFactory transactionFactory = environment.getTransactionFactory();
		final Transaction tx = transactionFactory.newTransaction(ds, null, false);
		return configuration.newExecutor(tx, ExecutorType.SIMPLE);
	}

	public static Executor newExecutor(Configuration configuration,Connection connection) {
		final Environment environment = configuration.getEnvironment();
		if (environment == null) {
			throw new ExecutorException("Environment was not configured.");
		}
		final TransactionFactory transactionFactory = environment.getTransactionFactory();
		final Transaction tx = transactionFactory.newTransaction(connection);
		return configuration.newExecutor(tx, ExecutorType.SIMPLE);
	}
	
	public static ResultHandler<Object> newResultHandler(Configuration configuration) {
		return newResultHandler(configuration.getObjectFactory());
	}
	
	public static ResultHandler<Object> newResultHandler(ObjectFactory objectFactory) {
		ResultHandler<Object> resultHandler = null;
		if (handlersMap.containsKey(objectFactory)) {
			resultHandler = handlersMap.get(objectFactory.hashCode());
		} else {
			handlersMap.putIfAbsent(objectFactory, new DefaultResultHandler(objectFactory));
		}
		return resultHandler;
	}
	
	public static List<Object> doQuery(Configuration configuration,String statementID,Object parameterObject,RowBounds rowBounds){
		try {
			// 运行环境参数
			Environment environment = configuration.getEnvironment();
			//获取数据库连接
			Connection connection = environment.getDataSource().getConnection();
			//获取当前事物工厂对象
			TransactionFactory transactionFactory = MybatisUtils.getTransactionFactoryFromEnvironment(configuration);
			Transaction tx = transactionFactory.newTransaction(connection);
			Executor executor = configuration.newExecutor(tx, ExecutorType.SIMPLE);
			
			
			// 获取与当前查询ID相同的Statement对象
			MappedStatement ms = configuration.getMappedStatement(statementID);
			ResultHandler<Object> resultHandler =	new DefaultResultHandler(configuration.getObjectFactory());
			return executor.query(ms, MybatisUtils.wrapCollection(parameterObject), new RowBounds(), resultHandler);
		} catch (Exception e) {
			LOG.error("Ignore this exception", e);
		}
		return null;
	}
	
	public static CacheKey createCacheKey(MappedStatement ms, Object parameterObject, RowBounds rowBounds, BoundSql boundSql) {
		return createCacheKey(ms, parameterObject, rowBounds, boundSql, null);
	}
	
	public static CacheKey createCacheKey(MappedStatement ms, Object parameterObject, RowBounds rowBounds, BoundSql boundSql,Locale locale) {
		CacheKey cacheKey = new CacheKey();
		cacheKey.update(ms.getId() + "_" + locale.getLanguage() + "-" + locale.getCountry());
		if(locale != null){
			cacheKey.update(locale.toString());
		}
		cacheKey.update(Integer.valueOf(rowBounds.getOffset()));
		cacheKey.update(Integer.valueOf(rowBounds.getLimit()));
		cacheKey.update(boundSql.getSql());
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		Configuration configuration = ms.getConfiguration();
		TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
		// mimic DefaultParameterHandler logic
		for (int i = 0; i < parameterMappings.size(); i++) {
			ParameterMapping parameterMapping = parameterMappings.get(i);
			if (parameterMapping.getMode() != ParameterMode.OUT) {
				Object value;
				String propertyName = parameterMapping.getProperty();
				if (boundSql.hasAdditionalParameter(propertyName)) {
					value = boundSql.getAdditionalParameter(propertyName);
				} else if (parameterObject == null) {
					value = null;
				} else if (typeHandlerRegistry.hasTypeHandler(parameterObject
						.getClass())) {
					value = parameterObject;
				} else {
					MetaObject metaObject = configuration.newMetaObject(parameterObject);
					value = metaObject.getValue(propertyName);
				}
				cacheKey.update(value);
			}
		}
		if (configuration.getEnvironment() != null) {
			// issue #176
			cacheKey.update(configuration.getEnvironment().getId());
		}
		return cacheKey;
	}
	
	public static Object wrapCollection(final Object object) {
		if (object instanceof Collection) {
			StrictMap<Object> map = new StrictMap<Object>();
			map.put("collection", object);
			if (object instanceof List) {
				map.put("list", object);
			}
			return map;
		} else if (object != null && object.getClass().isArray()) {
			StrictMap<Object> map = new StrictMap<Object>();
			map.put("array", object);
			return map;
		}
		return object;
	}
	
}



