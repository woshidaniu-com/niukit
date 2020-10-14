package com.woshidaniu.orm.mybatis.interceptor;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.DefaultParameterHandler;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.session.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basemodel.QueryModel;
import com.woshidaniu.basicutils.BlankUtils;
import com.woshidaniu.db.core.dialect.Dialect;
import com.woshidaniu.db.core.dialect.DialectFactory;
import com.woshidaniu.orm.mybatis.exception.MybatisException;
import com.woshidaniu.orm.mybatis.utils.MetaObjectUtils;

/**
 * 
 *@类名称	: AbstractInterceptor.java
 *@类描述	：顶层抽象拦截器
 *@创建人	：kangzhidong
 *@创建时间	：Mar 8, 2016 4:17:53 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public abstract class AbstractInterceptor implements Interceptor {

	protected static Logger LOG = LoggerFactory.getLogger(AbstractInterceptor.class);
	/* 方言实现类 */
	protected static final String DEFAULT_DIALECT_CLASS = "com.woshidaniu.db.core.dialect.impl.OracleDialect2";
	/* 方言类型 */
	private String dialectType;
	/* 方言实现类 */
	protected String dialectClass = DEFAULT_DIALECT_CLASS;
	/* 定义数据库方言 */
	protected Dialect dialectObject = null;
	/* 默认对象工厂 */
	protected final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();  
	/* 默认对象包装工厂 */
	protected final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();  
	    
	/**
	 * 
	 * @description: 
	 * <pre>
	 *  统一调用抽象方法：期中metaStatementHandler可以使用如下方法获取对象：
	 *  StatementHandler delegate = (StatementHandler) ReflectionUtils.getFieldValue(statementHandler, "delegate");
	 * 	Configuration configuration = (Configuration) metaStatementHandler.getValue("delegate.configuration");
 	 * 	ObjectFactory objectFactory = (ObjectFactory) metaStatementHandler.getValue("delegate.objectFactory");
  	 * 	TypeHandlerRegistry typeHandlerRegistry = (TypeHandlerRegistry) metaStatementHandler.getValue("delegate.typeHandlerRegistry");
 	 * 	ResultSetHandler resultSetHandler = (ResultSetHandler) metaStatementHandler.getValue("delegate.resultSetHandler");
  	 * 	ParameterHandler parameterHandler = (ParameterHandler) metaStatementHandler.getValue("delegate.parameterHandler");
 	 * 	Executor executor = (Executor) metaStatementHandler.getValue("delegate.executor");
 	 *  MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");
  	 * 	RowBounds rowBounds = (RowBounds) metaStatementHandler.getValue("delegate.rowBounds");
  	 * 	BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");
	 * </pre>
	 * @author : kangzhidong
	 * @date 2015-4-14 
	 * @time 下午03:03:12
	 * @param invocation
	 * @param statementHandler
	 * @param metaStatementHandler
	 * @return
	 * @throws Throwable
	 * @modify by:
	 * @modify date :
	 * @modify description :
	 */
	public abstract Object doPrepareIntercept(Invocation invocation,StatementHandler statementHandler,MetaObject metaStatementHandler) throws Throwable;
	
	/**
	 * 
	 * @description: 
	 * <pre>
	 *  统一调用抽象方法：期中resultSetHandler可以使用如下方法获取对象：
 	 * 	Executor executor = (Executor) ReflectionUtils.getFieldValue(resultSetHandler,"executor");
 	 * 	Configuration configuration = (Configuration) ReflectionUtils.getFieldValue(resultSetHandler,"configuration");
 	 *  MappedStatement mappedStatement = (MappedStatement) ReflectionUtils.getFieldValue(resultSetHandler,"mappedStatement");
  	 * 	RowBounds rowBounds = (RowBounds) ReflectionUtils.getFieldValue(resultSetHandler,"rowBounds");
  	 * 	ParameterHandler parameterHandler = (ParameterHandler) ReflectionUtils.getFieldValue(resultSetHandler,"parameterHandler");
  	 * 	ResultSetHandler resultSetHandler = (ResultSetHandler) ReflectionUtils.getFieldValue(resultSetHandler,"resultSetHandler");
  	 * 	BoundSql boundSql = (BoundSql) ReflectionUtils.getFieldValue(resultSetHandler,"boundSql");
  	 * 	TypeHandlerRegistry typeHandlerRegistry = (TypeHandlerRegistry) ReflectionUtils.getFieldValue(resultSetHandler,"typeHandlerRegistry");
  	 * 	ObjectFactory objectFactory = (ObjectFactory) ReflectionUtils.getFieldValue(resultSetHandler,"objectFactory");
	 * </pre>
	 * @author : kangzhidong
	 * @date 2015-4-14 
	 * @time 下午03:03:12
	 * @param invocation
	 * @param resultSetHandler
	 * @return
	 * @throws Throwable
	 * @modify by:
	 * @modify date :
	 * @modify description :
	 */
	public abstract Object doResultSetIntercept(Invocation invocation,ResultSetHandler resultSetHandler) throws Throwable;
	
	/**
	 * 
	 * @description: 
	 * <pre>
	 *  统一调用抽象方法：期中metaStatementHandler可以使用如下方法获取对象：
	 * 	Configuration configuration = (Configuration) metaStatementHandler.getValue("delegate.configuration");
 	 * 	ObjectFactory objectFactory = (ObjectFactory) metaStatementHandler.getValue("delegate.objectFactory");
  	 * 	TypeHandlerRegistry typeHandlerRegistry = (TypeHandlerRegistry) metaStatementHandler.getValue("delegate.typeHandlerRegistry");
 	 * 	ResultSetHandler resultSetHandler = (ResultSetHandler) metaStatementHandler.getValue("delegate.resultSetHandler");
  	 * 	ParameterHandler parameterHandler = (ParameterHandler) metaStatementHandler.getValue("delegate.parameterHandler");
 	 * 	Executor executor = (Executor) metaStatementHandler.getValue("delegate.executor");
 	 *  MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");
  	 * 	RowBounds rowBounds = (RowBounds) metaStatementHandler.getValue("delegate.rowBounds");
  	 * 	BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");
	 * </pre>
	 * @author : kangzhidong
	 * @date 2015-4-14 
	 * @time 下午03:03:12
	 * @param invocation
	 * @param executorProxy
	 * @param metaExecutor
	 * @return
	 * @throws Throwable
	 * @modify by:
	 * @modify date :
	 * @modify description :
	 */
	public abstract Object doExecutorIntercept(Invocation invocation,Executor executorProxy,MetaObject metaExecutor) throws Throwable;
	
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		// 通过invocation获取代理的目标对象
		Object target = invocation.getTarget();
		/*
		 * 对于StatementHandler其实只有两个实现类，
		 * 一个是RoutingStatementHandler，另一个是抽象类BaseStatementHandler，
		 * BaseStatementHandler有三个子类：
		 * 分别是SimpleStatementHandler，PreparedStatementHandler和CallableStatementHandler，
		 * SimpleStatementHandler是用于处理Statement的，
		 * PreparedStatementHandler是处理PreparedStatement的，
		 * CallableStatementHandler是 处理CallableStatement的。
		 * Mybatis在进行Sql语句处理的时候都是建立的RoutingStatementHandler，而在RoutingStatementHandler里面拥有一个   StatementHandler类型的delegate属性，
		 * RoutingStatementHandler会依据Statement的不同建立对应的BaseStatementHandler，即SimpleStatementHandler、  PreparedStatementHandler或CallableStatementHandler，
		 * 在RoutingStatementHandler里面所有StatementHandler接口方法的实现都是调用的delegate对应的方法。 
		 * 我们在PageInterceptor类上已经用@Signature标记了该Interceptor只拦截StatementHandler接口的prepare方法，又因为Mybatis只有在建立RoutingStatementHandler的时候  
		 * 是通过Interceptor的plugin方法进行包裹的，所以我们这里拦截到的目标对象肯定是RoutingStatementHandler对象。  
		*/
		if(target instanceof RoutingStatementHandler){
		    // 是通过Interceptor的plugin方法进行包裹的，所以我们这里拦截到的目标对象肯定是RoutingStatementHandler对象。
			RoutingStatementHandler statementHandler = (RoutingStatementHandler) target;
			
			MetaObject metaStatementHandler = MetaObjectUtils.forObject(statementHandler);
			// 分离代理对象链(由于目标类可能被多个拦截器拦截，从而形成多次代理，通过下面的两次循环可以分离出最原始的的目标类)
			// 可以分离出最原始的的目标类)
			while (metaStatementHandler.hasGetter("h")) {
				Object object = metaStatementHandler.getValue("h");
				metaStatementHandler = MetaObjectUtils.forObject(object);
			}
			// 分离最后一个代理对象的目标类
			while (metaStatementHandler.hasGetter("target")) {
				Object object = metaStatementHandler.getValue("target");
				metaStatementHandler = MetaObjectUtils.forObject(object);
			}
			//调用抽象接口，执行子类的代码
			return this.doPrepareIntercept(invocation,statementHandler,metaStatementHandler);
		}else if (target instanceof ResultSetHandler) {// 暂时ResultSetHandler只有FastResultSetHandler这一种实现
			//获取ResultSetHandler的实现
			ResultSetHandler resultSetHandler = (ResultSetHandler) target;
			//调用抽象接口，执行子类的代码
			return this.doResultSetIntercept(invocation,resultSetHandler);
		}else if(target instanceof Executor){
			
			 Executor executorProxy = (Executor) invocation.getTarget();
			  
			 MetaObject metaExecutor = MetaObjectUtils.forObject(executorProxy);
			 // 分离代理对象链
			 while (metaExecutor.hasGetter("h")) {
	            Object object = metaExecutor.getValue("h");
	            metaExecutor = MetaObjectUtils.forObject(object);
			 }
			 // 分离最后一个代理对象的目标类
			 while (metaExecutor.hasGetter("target")) {
	            Object object = metaExecutor.getValue("target");
	            metaExecutor = MetaObjectUtils.forObject(object);
			 }
			 //调用抽象接口，执行子类的代码
			 return this.doExecutorIntercept(invocation,executorProxy,metaExecutor);
		}
		// 将执行权交给下一个拦截器  
		return invocation.proceed();
	}
	
	
	@SuppressWarnings("unchecked")
	public QueryModel getQueryModel(Object parameterObject){
		if(parameterObject == null){
			return null;
		}
		QueryModel queryModel = null;
		//参数就是Page实体
		if(parameterObject instanceof QueryModel){	
			queryModel = (QueryModel) parameterObject;
		}else{	
			//参数为某个实体，该实体拥有queryModel属性或者包含QueryModel类型对象
			MetaObject metaObject = MetaObjectUtils.forObject(parameterObject);
			if(metaObject.hasGetter("queryModel")){
				queryModel = (QueryModel) metaObject.getValue("queryModel");
			}else if(parameterObject instanceof Map){
				//参数就是Map
				Map<String,Object> parameterMap = (Map<String,Object>)parameterObject;
				for (String key : parameterMap.keySet()) {
					if(parameterMap.get(key) instanceof QueryModel){	
						queryModel = (QueryModel) parameterMap.get(key);
						break;
					}
				}
			}else{
				LOG.error(parameterObject.getClass().getName()+"不存在 queryModel 属性 或 QueryModel类型对象！");
			}
		}
		return queryModel;
	}
	
	@SuppressWarnings("unchecked")
	public void setQueryModel(Object parameterObject,QueryModel queryModel){
		//参数就是Page实体
		if(parameterObject instanceof QueryModel){	
			parameterObject = queryModel;
		}else{	
			//参数为某个实体，该实体拥有queryModel属性或者包含QueryModel类型对象
			MetaObject metaObject = MetaObjectUtils.forObject(parameterObject);
			if(metaObject.hasGetter("queryModel")){
				metaObject.setValue("queryModel",queryModel);
			}else if(parameterObject instanceof Map){
				//参数就是Map
				Map<String,Object> parameterMap = (Map<String,Object>)parameterObject;
				for (String key : parameterMap.keySet()) {
					if(parameterMap.get(key) instanceof QueryModel){	
						parameterMap.remove(key);
						parameterMap.put(key, queryModel);
						break;
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * @description: 定义Properties参数处理抽象方法
	 * @author : kangzhidong
	 * @date 2015-4-14 
	 * @time 下午07:01:24
	 * @param properties
	 * @modify by:
	 * @modify date :
	 * @modify description :
	 */
	public abstract void setInterceptProperties(Properties properties);
	
	@Override
	public void setProperties(Properties properties) {
		
		dialectType  = properties.getProperty("dialectType");
		if (!BlankUtils.isBlank(dialectType)) {
			dialectObject = DialectFactory.getDialectByType(dialectType);
		} else {
			dialectClass = properties.getProperty("dialect");
			if (BlankUtils.isBlank(dialectClass)) {
				LOG.warn("Property dialect is not setted,use default '" +  DEFAULT_DIALECT_CLASS + "' ");
				dialectClass = DEFAULT_DIALECT_CLASS;
			}
			this.setDialectClass(dialectClass);
			if (!BlankUtils.isBlank(dialectClass)) {
				try {
					Class<?> clazz = Class.forName(dialectClass);
					if (Dialect.class.isAssignableFrom(clazz)){
						dialectObject = (Dialect) clazz.newInstance();
					}
					this.setDialectObject(dialectObject);
				} catch (InstantiationException e) {
					throw new MybatisException("Class :" + this.getDialectClass()+ " is not defined !", e.getCause());
				} catch (IllegalAccessException e) {
					throw new MybatisException("Class :" + this.getDialectClass()+ " is IllegalAccess !", e.getCause());
				} catch (ClassNotFoundException e) {
					throw new MybatisException("Class :" + this.getDialectClass()+ " is not found !", e.getCause());
				}
			}
		}
		/* 未配置方言则抛出异常 */
		if (dialectObject == null) {
			throw new MybatisException("The value of the dialect property in mybatis configuration.xml is not defined.");
		}
		
		//调用抽象接口，执行子类的代码
		this.setInterceptProperties(properties);
	}

	/**
	 * 对SQL参数(?)设值,参考org.apache.ibatis.executor.parameter.DefaultParameterHandler
	 * @param pstmt
	 * @param mappedStatement
	 * @param boundSql
	 * @param parameterObject
	 * @throws SQLException
	 */
	public void setParameters(PreparedStatement pstmt,MappedStatement mappedStatement, BoundSql boundSql,Object parameterObject) throws SQLException {
		if (parameterObject == null) {
			LOG.info("parameterObject is null!");
		} else {
			// 通过mappedStatement、参数对象page和BoundSql对象countBoundSql建立一个用于设定参数的ParameterHandler对象
			ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, parameterObject, boundSql);  
			// 通过parameterHandler给PreparedStatement对象设置参数
			parameterHandler.setParameters(pstmt);
		}
	}

	public SqlSource buildSqlSource(Configuration configuration, String originalSql, Class<?> parameterType) {
        SqlSourceBuilder builder = new SqlSourceBuilder(configuration);
        return  builder.parse(originalSql, parameterType);
    }
	
	public String getDialectType() {
		return dialectType;
	}

	public void setDialectType(String dialectType) {
		this.dialectType = dialectType;
	}

	public String getDialectClass() {
		return dialectClass;
	}

	public void setDialectClass(String dialectClass) {
		this.dialectClass = dialectClass;
	}

	public Dialect getDialectObject() {
		return dialectObject;
	}

	public void setDialectObject(Dialect dialectObject) {
		this.dialectObject = dialectObject;
	}
	
	
}
