/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.orm.mybatis.cache;

import java.util.List;

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basemodel.QueryModel;
import com.woshidaniu.basicutils.BlankUtils;
import com.woshidaniu.db.core.builder.AnnotationSQLBuilder;
import com.woshidaniu.orm.mybatis.utils.MetaObjectUtils;
import com.woshidaniu.orm.mybatis.utils.ParameterUtils;

/**
 *@类名称	: DefaultCacheKeyHandler.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Aug 25, 2016 10:00:20 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */

public class DefaultCacheKeyHandler implements CacheKeyHandler {
	
	protected static Logger LOG = LoggerFactory.getLogger(DefaultCacheKeyHandler.class);
	
	/* 拦截分页方法的正则表达式 */
	protected String paginationID = null; // 分页Id,mapper.xml中需要拦截的ID(正则匹配)
	protected AnnotationSQLBuilder builder = null;
	
	public DefaultCacheKeyHandler(String paginationID, AnnotationSQLBuilder builder) {
		this.paginationID = paginationID;
		this.builder = builder;
	}

	@Override
	public CacheKey createCacheKey(MappedStatement mappedStatement,Object parameterObject, RowBounds rowBounds, BoundSql boundSql) {
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
					newSql = builder.buildQuerySQL(parameterObject);
				}
				SqlSource sqlSource = buildSqlSource(configuration, newSql,parameterObject.getClass());
				parameterMappings = sqlSource.getBoundSql(parameterObject).getParameterMappings();
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
			QueryModel queryModel = ParameterUtils.getQueryModel(parameterObject);
			if (null != queryModel) {
				cacheKey.update(queryModel.getPageNo());
				cacheKey.update(queryModel.getPageSize());
			}
		}
		return cacheKey;
	}

	public SqlSource buildSqlSource(Configuration configuration, String originalSql, Class<?> parameterType) {
        SqlSourceBuilder builder = new SqlSourceBuilder(configuration);
        return  builder.parse(originalSql, parameterType, null);
    }
	
	public String getPaginationID() {
		return paginationID;
	}

	public AnnotationSQLBuilder getBuilder() {
		return builder;
	}
}
