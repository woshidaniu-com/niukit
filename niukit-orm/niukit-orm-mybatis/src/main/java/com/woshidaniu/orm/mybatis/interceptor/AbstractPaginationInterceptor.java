package com.woshidaniu.orm.mybatis.interceptor;

import java.util.Properties;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basicutils.BlankUtils;
import com.woshidaniu.db.core.builder.AnnotationSQLBuilder;
import com.woshidaniu.db.core.builder.impl.MybatisDynamicSQLBuilder;
import com.woshidaniu.orm.mybatis.cache.CacheKeyHandler;
import com.woshidaniu.orm.mybatis.cache.DefaultCacheKeyHandler;
import com.woshidaniu.orm.mybatis.interceptor.meta.MetaExecutor;

/**
 * 
 * @className	： AbstractPaginationInterceptor
 * @description	： TODO(描述这个类的作用)
 * @author 		： kangzhidong
 * @date		： Jan 26, 2016 4:02:43 PM
 * @version 	V1.0
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractPaginationInterceptor extends AbstractInterceptorAdapter {

	protected static Logger LOG = LoggerFactory.getLogger(AbstractPaginationInterceptor.class);
	protected AnnotationSQLBuilder builder = new MybatisDynamicSQLBuilder();
	/* 默认拦截分页方法的正则表达式 */
	protected final String DEFAULT_PAGINATION_METHOD_REGEXP = ".*Paged*.*"; 
	
	protected CacheKeyHandler cacheKeyHandler = null;
	
	@Override
	protected boolean isRequireIntercept(Invocation invocation, Executor executorProxy, MetaExecutor metaExecutor) {
		Object[] args = invocation.getArgs();
		MappedStatement mappedStatement = (MappedStatement) args[0];
		// 拦截需要分页的SQL语句。通过MappedStatement的ID匹配，默认重写以包含Paged字符的ID
		return mappedStatement.getId().matches(this.getMethodRegexp());
	}
	
	@Override
	public Object doExecutorIntercept(Invocation invocation, Executor executorProxy,MetaExecutor metaExecutor) throws Throwable {
		//检查是否需要进行拦截处理
		if (isRequireIntercept(invocation, executorProxy, metaExecutor)) {
			Object[] args = invocation.getArgs();
			MappedStatement mappedStatement = (MappedStatement) args[0];
			//解析parameterObject
			Object parameterObject = args[1];
			//解析RowBounds
			RowBounds rowBounds = (RowBounds) args[2];
			//解析ResultHandler
			ResultHandler resultHandler = (ResultHandler) args[3];
			//解析BoundSql
			BoundSql boundSql = mappedStatement.getBoundSql(parameterObject);
			//构建新的cacheKey对象
			CacheKey cacheKey = cacheKeyHandler.createCacheKey(mappedStatement, parameterObject, rowBounds, boundSql);
			//获得原始执行对象
			Executor executor = (Executor) metaExecutor.getMetaObject().getOriginalObject();
			//执行查询操作
			return executor.query(mappedStatement, parameterObject, rowBounds, resultHandler, cacheKey, boundSql);
		}
		// 将执行权交给下一个拦截器  
		return invocation.proceed();
	}
	 
	
	@Override
	public void setInterceptProperties(Properties properties) {
		
		if(BlankUtils.isBlank(getMethodRegexp())){
			setMethodRegexp(DEFAULT_PAGINATION_METHOD_REGEXP);
			LOG.warn("Property methodRegexp is not setted,use default '{0}' !", DEFAULT_PAGINATION_METHOD_REGEXP);
		}
		 
		cacheKeyHandler = new DefaultCacheKeyHandler(getMethodRegexp(), builder);
		
	}

	public AnnotationSQLBuilder getBuilder() {
		return builder;
	}

	public void setBuilder(AnnotationSQLBuilder builder) {
		this.builder = builder;
	}

	public CacheKeyHandler getCacheKeyHandler() {
		return cacheKeyHandler;
	}

	public void setCacheKeyHandler(CacheKeyHandler cacheKeyHandler) {
		this.cacheKeyHandler = cacheKeyHandler;
	}

}
