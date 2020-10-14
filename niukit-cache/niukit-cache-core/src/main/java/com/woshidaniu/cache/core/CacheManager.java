package com.woshidaniu.cache.core;

import java.util.Collection;

import com.woshidaniu.cache.core.exception.CacheException;

/**
 * 
 * @className	： CacheManager
 * @description	： 缓存管理器（各不同的缓存框架实现各自的CacheManager）
 * @author 		： kangzhidong
 * @date		： Jan 23, 2016 8:52:19 PM
 */
public interface CacheManager {
	
	/**
	 * 
	 * <p>方法说明：获取缓存<p>
	 * <p>作者：a href="#">Zhangxiaobin[1036]<a><p>
	 * <p>时间：2016年10月14日上午11:01:17<p>
	 */
	<K,V> Cache<K,V> getCache(String name) throws CacheException;
	
	/**
	 * 
	 * @description	：获得缓存的所有名称
	 * @author 		： kangzhidong
	 * @date 		：Jan 23, 2016 8:55:31 PM
	 * @return
	 */
	Collection<String> getCacheNames();

}
