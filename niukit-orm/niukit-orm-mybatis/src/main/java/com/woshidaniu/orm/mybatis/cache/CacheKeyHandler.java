/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.orm.mybatis.cache;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.RowBounds;

/**
 *@类名称	: CacheKeyHandler.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Aug 25, 2016 9:46:05 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */

public interface CacheKeyHandler {
	
	public CacheKey createCacheKey(MappedStatement mappedStatement, Object parameterObject,RowBounds rowBounds, BoundSql boundSql);
	
}
