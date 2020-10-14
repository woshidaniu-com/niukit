package com.woshidaniu.web.servlet.filter.cache;

import com.woshidaniu.cache.core.CacheManager;
import com.woshidaniu.cache.core.exception.CacheException;

/**
 * <p>
 *   <h3>niutal框架<h3>
 *   <br>说明：缓存支持
 *	 <br>class：com.woshidaniu.web.filter.cache.CacheSupport.java
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
public interface CacheSupport {

	/**
	 * 
	 * <p>方法说明： 设置缓存管理器<p>
	 * <p>作者：a href="#">Zhangxiaobin[1036]<a><p>
	 * <p>时间：2016年10月14日上午11:07:52<p>
	 */
	void setCacheManager(CacheManager cm) throws CacheException;
	
}
