package com.woshidaniu.cache.jedis;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.woshidaniu.basicutils.CollectionUtils;
import com.woshidaniu.cache.core.Cache;
import com.woshidaniu.cache.core.exception.CacheException;
import com.woshidaniu.io.utils.IOUtils;

@SuppressWarnings("unchecked")
public class JedisCache<K, V> implements Cache<K, V> {

	protected static final Logger LOG = LoggerFactory.getLogger(JedisCache.class);
	protected final Jedis cache;
	protected final String name;
	protected String keyPrefix; //关键字前缀字符，区别属于哪个模块  
	
	public JedisCache(String name, Jedis jedis) {
		this(name, jedis, null);
	}
	
	public JedisCache(String name, Jedis jedis,String keyPrefix) {
		if (jedis == null) {
			throw new CacheException("error jedis");
		}
		this.name = name;
		this.cache = jedis;
		this.keyPrefix = keyPrefix;
	}

	public JedisCache(String name, JedisPool jedisPool) {
		this(name, jedisPool, null);
	}
	
	public JedisCache(String name, JedisPool jedisPool,String keyPrefix) {
		if (jedisPool == null) {
			throw new CacheException("error jedisPool");
		}
		this.name = name;
		this.cache = jedisPool.getResource();
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
		if (LOG.isTraceEnabled()) {
			LOG.trace("Clearing all objects from cache [" + name + "]");
        }
        try {
        	cache.flushAll();
        } catch (Throwable t) {
            throw new CacheException(t);
        }
	}

	@Override
	public long size() throws CacheException {
		try {
			return cache.dbSize();
        } catch (Throwable t) {
            throw new CacheException(t);
        }
	}

	@Override
	public Set<K> keys() throws CacheException {
		Set<String> keys = cache.keys("*");
		if (!(keys == null || keys.isEmpty())) {
			return (Set<K>) Collections.unmodifiableSet(new LinkedHashSet<String>(keys));
		} else {
			return Collections.emptySet();
		}
	}
	
	@Override
	public Collection<V> values() throws CacheException {
        try {
        	Set<String> keys = cache.keys("*");
            if (!CollectionUtils.isEmpty(keys)) {
                List<V> values = new ArrayList<V>(keys.size());
                for (String key : keys) {
                    V value = get((K) key);
                    if (value != null) {
                        values.add(value);
                    }
                }
                return Collections.unmodifiableList(values);
            } else {
                return Collections.emptyList();
            }
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }
	
	@Override
	public Map<String, Map<String, String>> stats() throws CacheException {
		try {
			
			Map<String, Map<String, String>> statsMaps = new HashMap<String, Map<String,String>>();
			
			
			List<String> statLines = IOUtils.readLines(new StringReader(cache.info()));
			
			
			Map<String, String> statsMap = new HashMap<String, String>();
			
			return statsMaps;
		} catch (Exception e) {
			throw new CacheException(e);
		}
	}

	@Override
	public Object origin() throws CacheException {
		return cache;
	}

}
