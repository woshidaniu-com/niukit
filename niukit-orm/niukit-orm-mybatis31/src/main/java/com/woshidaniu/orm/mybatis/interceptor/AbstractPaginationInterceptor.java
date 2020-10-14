package com.woshidaniu.orm.mybatis.interceptor;

import java.util.List;
import java.util.Properties;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basemodel.QueryModel;
import com.woshidaniu.basicutils.BlankUtils;
import com.woshidaniu.db.core.builder.AnnotationSQLBuilder;
import com.woshidaniu.db.core.builder.impl.MybatisDynamicSQLBuilder;
import com.woshidaniu.orm.mybatis.utils.MetaObjectUtils;
import com.woshidaniu.orm.mybatis.utils.MyBatisSQLUtils;

/**
 * 
 * @className	： AbstractPaginationInterceptor
 * @description	： TODO(描述这个类的作用)
 * @author 		： kangzhidong
 * @date		： Jan 26, 2016 4:02:43 PM
 * @version 	V1.0
 */
public class AbstractPaginationInterceptor extends AbstractStatementInterceptor {

	protected static Logger LOG = LoggerFactory.getLogger(AbstractPaginationInterceptor.class);
	protected AnnotationSQLBuilder builder = new MybatisDynamicSQLBuilder();
	/* 拦截分页方法的正则表达式 */
	protected String paginationID = ".*Paged*.*"; // 分页Id,mapper.xml中需要拦截的ID(正则匹配)
	
	@Override
	public Object doExecutorIntercept(Invocation invocation, Executor executorProxy,MetaObject metaExecutor) throws Throwable {
		Object[] args = invocation.getArgs();
		MappedStatement mappedStatement = (MappedStatement) args[0];
		// 拦截需要分页的SQL语句。通过MappedStatement的ID匹配，默认重写以包含Paged字符的ID
		if (mappedStatement.getId().matches(this.getPaginationID())) {
			//解析parameterObject
			Object parameterObject = args[1];
			//解析RowBounds
			RowBounds rowBounds = (RowBounds) args[2];
			//解析ResultHandler
			ResultHandler resultHandler = (ResultHandler) args[3];
			//解析BoundSql
			BoundSql boundSql = mappedStatement.getBoundSql(parameterObject);
			//构建新的cacheKey对象
			CacheKey cacheKey = createCacheKey(mappedStatement, parameterObject, rowBounds, boundSql);
			//获得原始执行对象
			Executor executor = (Executor) metaExecutor.getOriginalObject();
			//执行查询操作
			return executor.query(mappedStatement, parameterObject, rowBounds, resultHandler, cacheKey, boundSql);
		}
		// 将执行权交给下一个拦截器  
		return invocation.proceed();
	}

	public CacheKey createCacheKey(MappedStatement mappedStatement, Object parameterObject,RowBounds rowBounds, BoundSql boundSql) {
		Configuration configuration = mappedStatement.getConfiguration();
		CacheKey cacheKey = new CacheKey();
		cacheKey.update(mappedStatement.getId());
		cacheKey.update(rowBounds.getOffset());
		cacheKey.update(rowBounds.getLimit());
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		
		// 解决自动生成SQL，SQL语句为空导致key生成错误的bug
		if ( mappedStatement.getId().matches(getPaginationID()) && BlankUtils.isBlank(boundSql.getSql())) {
			String newSql = null;
			try {
				if(SqlCommandType.SELECT.ordinal() == mappedStatement.getSqlCommandType().ordinal()){
					newSql = MyBatisSQLUtils.getRunSQL(mappedStatement, parameterObject ,false);
				}
				parameterMappings = mappedStatement.getSqlSource().getBoundSql(parameterObject).getParameterMappings();
				cacheKey.update(newSql);
			} catch (Exception e) {
				LOG.error("Update cacheKey error.", e);
			}
		}else {
			cacheKey.update(boundSql.getSql());
		}

		MetaObject metaObject = MetaObjectUtils.forObject(parameterObject);
		if (parameterMappings.size() > 0 && parameterObject != null) {
			TypeHandlerRegistry typeHandlerRegistry = mappedStatement.getConfiguration().getTypeHandlerRegistry();
			if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
				cacheKey.update(parameterObject);
			} else {
				for (ParameterMapping parameterMapping : parameterMappings) {
					String propertyName = parameterMapping.getProperty();
					if (metaObject.hasGetter(propertyName)) {
						cacheKey.update(metaObject.getValue(propertyName));
					} else if (boundSql.hasAdditionalParameter(propertyName)) {
						cacheKey.update(boundSql.getAdditionalParameter(propertyName));
					}
				}
			}
		}
		// 当需要分页查询时，将queryModel参数里的当前页和每页数加到cachekey里
		if (mappedStatement.getId().matches(this.getPaginationID())) {
			QueryModel queryModel = getQueryModel(parameterObject);
			if (null != queryModel) {
				cacheKey.update(queryModel.getPageNo());
				cacheKey.update(queryModel.getPageSize());
			}
		}
		return cacheKey;
	}
	
	@Override
	public void setInterceptProperties(Properties properties) {
		String paginationID = properties.getProperty("paginationID");
		if (paginationID != null && paginationID.length() != 0) {
			this.setPaginationID(paginationID);
		} else {
			LOG.warn("Property paginationID is not setted,use default '.*Paged*' !");
		}
	}
	
	public String getPaginationID() {
		return paginationID;
	}

	public void setPaginationID(String paginationID) {
		this.paginationID = paginationID;
	}

}
