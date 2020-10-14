package com.woshidaniu.cache.xmemcached;

import java.util.Set;

import com.woshidaniu.basicutils.ConfigUtils;
import com.woshidaniu.cache.core.AbstractCacheManagerFactory;
import com.woshidaniu.cache.core.CacheManager;

public class XmemcachedCacheManagerFactory extends AbstractCacheManagerFactory {

	@Override
	public CacheManager getCacheManagerInstance(String managerName) {
		// 在EhCache中要确保只有一个被实例化
		Set<String> keySet = cacheManagerMap.keySet();
		Object[] keys = keySet.toArray();

		for (int i = 0; i < keys.length; i++) {
			String key = keys[i].toString();
			CacheManager cm = cacheManagerMap.get(key);
			// 如果cacheManagerMap已经存在XmemcachedManager的缓存，则直接返回
			if (cm instanceof XmemcachedManager) { 
				return cm;
			}
		}
		return new XmemcachedManager(ConfigUtils.getProperties(this.getClass() , "memcached.properties"));
	}

}
