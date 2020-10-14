package com.woshidaniu.cache.jedis;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.JedisCluster;

import com.woshidaniu.cache.core.Cache;
import com.woshidaniu.cache.core.exception.CacheException;

@SuppressWarnings("unchecked")
public class JedisClusterCache<K, V> implements Cache<K, V> {

	protected static final Logger LOG = LoggerFactory.getLogger(JedisClusterCache.class);
	protected final JedisCluster cache;
	protected final String name;
	protected String keyPrefix; //关键字前缀字符，区别属于哪个模块  
	
	public JedisClusterCache(String name, JedisCluster cluster) {
		this(name, cluster, null);
	}
	
	public JedisClusterCache(String name, JedisCluster cluster,String keyPrefix) {
		if (cluster == null) {
			throw new CacheException("error cluster");
		}
		this.name = name;
		this.cache = cluster;
		this.keyPrefix = keyPrefix;
	}
	
	
	protected String getKey(K k) {
		return this.keyPrefix == null ? String.valueOf(k) : this.keyPrefix + "_" + String.valueOf(k);
	}
	
	@Override
	public String getName() throws CacheException {
		return name;
	}

	@Override
	public V get(K key) throws CacheException {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.trace("Get data in cache [" + name + "] for key [" + key + "]");
			}
			if(key == null){
				return null;
			}
			String element = cache.get(getKey(key));
			if(element == null){
				if (LOG.isDebugEnabled()) {
					LOG.debug("Data from KEY:{} is null.", key);
				}
				return null;
			}
			return (V) element;
		} catch (Exception e) {
			throw new CacheException(e);
		}
	}

	@Override
	public V put(K key, V value) throws CacheException {
		if (LOG.isTraceEnabled()) {
			LOG.trace("Putting object in cache [" + name + "] for key [" + key + "]");
        }
		if (key == null || value == null) {
			throw new CacheException("error key / value");
		}
        try {
            return (V) cache.set(getKey(key), value.toString());
        } catch (Throwable t) {
            throw new CacheException(t);
        }
	}
	
	@Override
	public V remove(K key) throws CacheException {
		if (LOG.isTraceEnabled()) {
			LOG.trace("Removing object from cache [" + name + "] for key [" + key + "]");
        }
        try {
            V previous = get(key);
            cache.del(getKey(key));
            return previous;
        } catch (Throwable t) {
            throw new CacheException(t);
        }
	}

	@Override
	public void clear() throws CacheException {
		throw new CacheException("No way to dispatch this command to Redis Cluster.");
	}

	@Override
	public long size() throws CacheException {
		throw new CacheException("No way to dispatch this command to Redis Cluster.");
	}

	@Override
	public Set<K> keys() throws CacheException {
		throw new CacheException("No way to dispatch this command to Redis Cluster.");
	}
	
	@Override
	public Collection<V> values() throws CacheException {
		throw new CacheException("No way to dispatch this command to Redis Cluster.");
    }
	
	@Override
	public Map<String, Map<String, String>> stats() throws CacheException {
		throw new CacheException("No way to dispatch this command to Redis Cluster.");
	}

	@Override
	public Object origin() throws CacheException {
		return cache;
	}

}
