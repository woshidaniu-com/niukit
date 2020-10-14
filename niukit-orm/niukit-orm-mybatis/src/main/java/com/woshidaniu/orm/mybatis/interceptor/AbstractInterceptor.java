package com.woshidaniu.orm.mybatis.interceptor;

import java.util.HashMap;
import java.util.Properties;

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.session.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basicutils.BlankUtils;
import com.woshidaniu.db.core.dialect.Dialect;
import com.woshidaniu.db.core.dialect.DialectFactory;
import com.woshidaniu.orm.mybatis.exception.MybatisException;
import com.woshidaniu.orm.mybatis.interceptor.meta.MetaExecutor;
import com.woshidaniu.orm.mybatis.interceptor.meta.MetaParameterHandler;
import com.woshidaniu.orm.mybatis.interceptor.meta.MetaResultSetHandler;
import com.woshidaniu.orm.mybatis.interceptor.meta.MetaStatementHandler;
import com.woshidaniu.orm.mybatis.utils.MybatisUtils;

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
	protected String dialect;
	/* 方言实现类 */
	protected String dialectClass = DEFAULT_DIALECT_CLASS;
	/* 定义数据库方言 */
	protected Dialect dialectObject = null;
	/* 定义需要拦截方法的表达式 */
	protected String methodRegexp = null; 
	/* Context参数 */
	protected HashMap<String,Object> extraContext = new HashMap<String,Object>();
	
	/**
	 * <pre>
	 *  统一调用抽象方法：使用metaExecutor可获取以下对象：
	 *  MetaObject metaObject = metaExecutor.getMetaObject();
	 * 	Transaction transaction = metaExecutor.getTransaction();
 	 * 	Configuration configuration = metaExecutor.getConfiguration();
	 * </pre>
	 */
	public abstract Object doExecutorIntercept(Invocation invocation,Executor executorProxy,MetaExecutor metaExecutor) throws Throwable;
	  
	/**
	 * <pre>
	 *  统一调用抽象方法：使用metaParameterHandler可获取以下对象：
	 *  MetaObject metaObject = metaParameterHandler.getMetaObject();
	 * 	Configuration configuration = metaParameterHandler.getConfiguration();
	 * 	TypeHandlerRegistry typeHandlerRegistry = metaParameterHandler.getTypeHandlerRegistry();
	 *  MappedStatement mappedStatement = metaParameterHandler.getMappedStatement();
	 * 	Object parameterObject = metaParameterHandler.getParameterObject();
	 * 	BoundSql boundSql = metaParameterHandler.getBoundSql();
	 * </pre>
	 */
	public abstract Object doParameterIntercept(Invocation invocation,ParameterHandler parameterHandler,MetaParameterHandler metaParameterHandler) throws Throwable;

	/**
	 * <pre>
	 *  统一调用抽象方法：期中metaStatementHandler可以使用如下方法获取对象：
	 *  MetaObject metaObject = metaStatementHandler.getMetaObject();
	 * 	Configuration configuration = metaStatementHandler.getConfiguration();
 	 * 	ObjectFactory objectFactory = metaStatementHandler.getObjectFactory();
  	 * 	TypeHandlerRegistry typeHandlerRegistry = metaStatementHandler.getTypeHandlerRegistry();
 	 * 	ResultSetHandler resultSetHandler = metaStatementHandler.getResultSetHandler();
  	 * 	ParameterHandler parameterHandler = metaStatementHandler.getParameterHandler();
 	 * 	Executor executor = metaStatementHandler.getExecutor();
 	 *  MappedStatement mappedStatement = metaStatementHandler.getMappedStatement();
  	 * 	RowBounds rowBounds = metaStatementHandler.getRowBounds();
  	 * 	BoundSql boundSql = metaStatementHandler.getBoundSql();
	 * </pre>
	 */
	public abstract Object doStatementIntercept(Invocation invocation,StatementHandler statementHandler,MetaStatementHandler metaStatementHandler) throws Throwable;
	
	/**
	 * <pre>
	 *  统一调用抽象方法：使用metaResultSetHandler可获取以下对象：
	 *  MetaObject metaObject = metaResultSetHandler.getMetaObject();
 	 * 	Executor executor = metaResultSetHandler.getExecutor();
 	 * 	Configuration configuration = metaResultSetHandler.getConfiguration();
 	 *  MappedStatement mappedStatement = metaResultSetHandler.getMappedStatement();
  	 * 	RowBounds rowBounds = metaResultSetHandler.getRowBounds();
  	 * 	ParameterHandler parameterHandler = metaResultSetHandler.getParameterHandler();
  	 * 	ResultHandler<?> resultHandler = metaResultSetHandler.getResultHandler();
  	 * 	BoundSql boundSql = metaResultSetHandler.getBoundSql();
  	 * 	TypeHandlerRegistry typeHandlerRegistry = metaResultSetHandler.getTypeHandlerRegistry();
  	 *  ObjectFactory objectFactory = metaResultSetHandler.getObjectFactory();
  	 *  ReflectorFactory reflectorFactory = metaResultSetHandler.getReflectorFactory();
	 * </pre>
	 */
	public abstract Object doResultSetIntercept(Invocation invocation,ResultSetHandler resultSetHandler,MetaResultSetHandler metaResultSetHandler) throws Throwable;
	
	/**
	 * 扩展拦截方法，用于所有拦截执行完成后的资源释放操作
	 * @param invocation
	 * @throws Throwable
	 */
	public abstract void doDestroyIntercept(Invocation invocation) throws Throwable;
	
	/**
	 * 
	 * @description	： （Executor，ParameterHandler，ResultSetHandler，StatementHandler）等对象
	 *<pre>
	 *	创建顺序是:Executor，StatementHandler，ParameterHandler，ResultSetHandler
	 *	执行顺序是:Executor，ParameterHandler，StatementHandler，ResultSetHandler
	 *</pre>
	 * @author 		： <a href="mailto:hnxyhcwdl1003@163.com">kangzhidong</a>
	 * @date 		：Nov 3, 2016 11:40:12 AM
	 * @param invocation
	 * @return
	 * @see org.apache.ibatis.session.Configuration
	 * @throws Throwable
	 */
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		try {
			// 通过invocation获取代理的目标对象
			Object target = invocation.getTarget();
			// 分离代理对象链(由于目标类可能被多个拦截器拦截，从而形成多次代理，通过下面的两次循环可以分离出最原始的的目标类)
			target = MybatisUtils.getTarget(target);
			
			if(target instanceof Executor){
				Executor executor = (Executor)target;
				// Executor的MetaObject处理
				MetaExecutor meta = MetaExecutor.metaObject(executor);
				// 调用抽象接口，执行子类的代码
				return this.doExecutorIntercept(invocation, executor, meta );
			}
			
			else if(target instanceof ParameterHandler){
			    // 是通过Interceptor的plugin方法进行包裹的，所以我们这里拦截到的目标对象肯定是ParameterHandler对象。
				ParameterHandler parameterHandler = (ParameterHandler) target;
				// ParameterHandler的MetaObject处理
				MetaParameterHandler meta = MetaParameterHandler.metaObject(parameterHandler);
				// 调用抽象接口，执行子类的代码
				return this.doParameterIntercept(invocation, parameterHandler, meta );
			}
			
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
			else if(target instanceof StatementHandler){
			    // 是通过Interceptor的plugin方法进行包裹的，所以我们这里拦截到的目标对象肯定是RoutingStatementHandler对象。
				StatementHandler statementHandler = (StatementHandler) target;
				// StatementHandler的MetaObject处理
				MetaStatementHandler meta = MetaStatementHandler.metaObject(statementHandler);
				//调用抽象接口，执行子类的代码
				return this.doStatementIntercept(invocation, statementHandler, meta );
			}
			
			// 暂时ResultSetHandler只有FastResultSetHandler这一种实现
			else if (target instanceof ResultSetHandler) {
				// 获取ResultSetHandler的实现
				ResultSetHandler resultSetHandler = (ResultSetHandler) target;
				// ResultSetHandler的MetaObject处理
				MetaResultSetHandler meta = MetaResultSetHandler.metaObject(resultSetHandler);
				//调用抽象接口，执行子类的代码
				return this.doResultSetIntercept(invocation, resultSetHandler, meta );
			} 
			// 将执行权交给下一个拦截器  
			return invocation.proceed();
			
		} finally  {
			// 执行完成后的销毁操作
			this.doDestroyIntercept(invocation);
		}
	}
	
	public abstract void setInterceptProperties(Properties properties);
	
	@Override
	public void setProperties(Properties properties) {
		
		dialect  = properties.getProperty("dialect");
		if (!BlankUtils.isBlank(dialect)) {
			dialectObject = DialectFactory.getDialectByType(dialect);
		} else {
			dialectClass = properties.getProperty("dialect");
			if (BlankUtils.isBlank(dialectClass)) {
				LOG.warn("Property dialect is not setted,use default '" + DEFAULT_DIALECT_CLASS + "' ");
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
		
		/* 需要拦截方法的表达式 */
		String methodRegexp = properties.getProperty("methodRegexp");
		if (!BlankUtils.isBlank(methodRegexp)) {
			this.setMethodRegexp(methodRegexp);
		}
		
		/* 未配置方言则抛出异常 */
		if (dialectObject == null) {
			throw new MybatisException("The value of the dialect property in mybatis configuration.xml is not defined.");
		}
		
		//调用抽象接口，执行子类的代码
		this.setInterceptProperties(properties);
	}

	public SqlSource buildSqlSource(Configuration configuration, String originalSql, Class<?> parameterType) {
        SqlSourceBuilder builder = new SqlSourceBuilder(configuration);
        return  builder.parse(originalSql, parameterType, null);
    }
	
	public String getDialect() {
		return dialect;
	}

	public void setDialect(String dialect) {
		this.dialect = dialect;
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

	public String getMethodRegexp() {
		return methodRegexp;
	}

	public void setMethodRegexp(String methodRegexp) {
		this.methodRegexp = methodRegexp;
	}
	
}
