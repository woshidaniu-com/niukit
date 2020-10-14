package com.woshidaniu.cache.cache.oscache;

import java.util.Set;

import com.woshidaniu.cache.core.AbstractCacheManagerFactory;
import com.woshidaniu.cache.core.CacheManager;

public class OSCacheCacheManagerFactory extends AbstractCacheManagerFactory {

	/**
	 * 创建OSCacheCacheManager实例
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
			if (cm instanceof OSCacheCacheManager) { 
				return cm;
			}
		}
		return new OSCacheCacheManager();
	}

}
