package com.woshidaniu.cache.xmemcached;

import java.util.Properties;

import com.woshidaniu.cache.core.AbstractCacheManager;
import com.woshidaniu.cache.core.Cache;
import com.woshidaniu.cache.core.exception.CacheException;


public class XmemcachedManager extends AbstractCacheManager {

	public XmemcachedManager(Properties properties) {
	}

	@Override
	protected <K, V> Cache<K, V> createCache(String name) throws CacheException {
		return null;
	}

}
