package com.woshidaniu.cache.ehcache;

import java.util.Set;

import com.woshidaniu.basicutils.ConfigUtils;
import com.woshidaniu.cache.core.AbstractCacheManagerFactory;
import com.woshidaniu.cache.core.CacheManager;
import com.woshidaniu.cache.core.exception.CacheException;

public class EhcacheCacheManagerFactory extends AbstractCacheManagerFactory {

	/**
	 * 创建EhCacheCacheManager实例
	 */
	@Override
	public CacheManager getCacheManagerInstance(String managerName) {
		// 在EhCache中要确保只有一个被实例化
		Set<String> keySet = cacheManagerMap.keySet();
		Object[] keys = keySet.toArray();

		for (int i = 0; i < keys.length; i++) {
			String key = keys[i].toString();
			CacheManager cm = cacheManagerMap.get(key);
			// 如果cacheManagerMap已经存在EhCacheCacheManager的缓存，则直接返回
			if (cm instanceof EhCacheCacheManager) { 
				return cm;
			}
		}
		try {
			return new EhCacheCacheManager(ConfigUtils.getInputStream(this.getClass(), "ehcache.xml"));
		} catch (Exception e) {
			 throw new CacheException(e);
		}
	}

}
