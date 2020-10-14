package com.woshidaniu.cache.jedis;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.woshidaniu.cache.core.AbstractCacheManager;
import com.woshidaniu.cache.core.Cache;
import com.woshidaniu.cache.core.exception.CacheException;

public class JedisCacheManager extends AbstractCacheManager {

	private JedisPool jedisPool;

	public JedisCacheManager() {
		
	}

	public JedisCacheManager(String host, int port, int timeout) {
		this(new JedisPoolConfig(), host, port, timeout);
	}

	public JedisCacheManager(JedisPoolConfig poolConfig, String host, int port, int timeout) {
		this.jedisPool = new JedisPool(poolConfig, host, port, timeout);
	}

	public JedisCacheManager(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}

	public JedisPool getJedisPool() {
		return jedisPool;
	}

	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}
	
	@Override
	protected <K, V> Cache<K, V> createCache(String name) throws CacheException {
		if (name == null || name.length() == 0) {
			throw new IllegalArgumentException("error name");
		}
		return new JedisCache<K, V>(name, jedisPool);
	}

}
