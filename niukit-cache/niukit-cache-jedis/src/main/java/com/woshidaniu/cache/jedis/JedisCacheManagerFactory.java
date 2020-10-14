package com.woshidaniu.cache.jedis;

import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basicutils.ConfigUtils;
import com.woshidaniu.cache.core.AbstractCacheManagerFactory;
import com.woshidaniu.cache.core.CacheManager;
import com.woshidaniu.cache.core.exception.CacheException;

public class JedisCacheManagerFactory extends AbstractCacheManagerFactory {
	
	protected static Logger LOG = LoggerFactory.getLogger(JedisCacheManagerFactory.class);
	
	/**
	 * 创建RedisCacheManager实例
	 */
	@Override
	public CacheManager getCacheManagerInstance(String managerName) {
		// 在EhCache中要确保只有一个被实例化
		Set<String> keySet = cacheManagerMap.keySet();
		Object[] keys = keySet.toArray();
		for (int i = 0; i < keys.length; i++) {
			String key = keys[i].toString();
			CacheManager cm = cacheManagerMap.get(key);
			if (cm instanceof JedisCacheManager) { // 如果cacheManagerMap已经存在EhCacheCacheManager的缓存，则直接返回
				return cm;
			}
		}
		CacheManager cacheManager;
		try {
			Properties config = ConfigUtils.getProperties(this.getClass(), "jedis.properties");
			String host = config.getProperty("host");
			int port = Integer.valueOf(config.getProperty("port"));
			int timeout = Integer.valueOf(config.getProperty("timeout"));
			cacheManager = new JedisCacheManager(host, port, timeout);
		} catch (Exception e) {
			LOG.error(ExceptionUtils.getFullStackTrace(e));
			throw new CacheException("Can't load  properties");
		}
		return cacheManager;
	}

}
