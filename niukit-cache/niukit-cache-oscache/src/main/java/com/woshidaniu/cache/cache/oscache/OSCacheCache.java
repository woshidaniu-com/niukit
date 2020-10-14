package com.woshidaniu.cache.cache.oscache;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.oscache.OSCache;
import com.woshidaniu.cache.core.Cache;
import com.woshidaniu.cache.core.exception.CacheException;
import com.woshidaniu.cache.core.exception.CacheGettingException;

@SuppressWarnings({"unchecked"})
public class OSCacheCache<K, V> implements Cache<K, V> {
	
	/**
     * Private internal log instance.
     */
	protected static final Logger LOG = LoggerFactory.getLogger(OSCacheCache.class);
    protected OSCache cache;
    protected String keyPrefix; //关键字前缀字符，区别属于哪个模块  
    
	public OSCacheCache(OSCache oscache) {
		this(oscache, null);
	}
	
	public OSCacheCache(OSCache oscache,String keyPrefix) {
		if (oscache == null) {
			throw new IllegalArgumentException("Cache argument cannot be null.");
		}
		this.cache = oscache;
		this.keyPrefix = keyPrefix;
	}
	
	@Override
	public String getName() throws CacheException {
		return cache.getName();
	}
	
	protected String getKey(K key) {
		return this.keyPrefix == null ? "K_" + String.valueOf(key) : this.keyPrefix + "_" + String.valueOf(key);
	}
	
	@Override
	public V get(K key) throws CacheException {
		if (key == null) {
			return null;
		}
		String keyString = getKey(key);
		try {
            if (LOG.isTraceEnabled()) {
                LOG.trace("Getting object from cache [" + getName() + "] for key [" + keyString + "]");
            }
            Object element = cache.getFromCache(keyString);
            if (element == null) {
                if (LOG.isTraceEnabled()) {
                    LOG.trace("Element for [" + key + "] is null.");
                }
                return null;
            } else {
                return (V) element;
            }
        } catch (Throwable t) {
        	cache.cancelUpdate(keyString);
            throw new CacheGettingException(t);
        }
	}

	@Override
	public V put(K key, V value) throws CacheException {
		if (key == null) {
            return null;
        }
        String keyString = String.valueOf(key);
		
        try {
            cache.putInCache(keyString, value);
            if (LOG.isTraceEnabled()) {
    			LOG.trace("Putting object in cache [" + getName() + "] for key [" + keyString + "]");
            }
            return value;
        } catch (Throwable t) {
            throw new CacheException(t);
        }
	}

	@Override
	public V remove(K key) throws CacheException {
		if (key == null) {
            return null;
        }
		String keyString = key.toString();
        try {
            V previous = get(key);
            cache.remove(keyString);
            if (LOG.isTraceEnabled()) {
    			LOG.trace("Removing object from cache [" + getName() + "] for key [" + keyString + "]");
            }
            return previous;
        } catch (Throwable t) {
            throw new CacheException(t);
        }
	}

	@Override
	public void clear() throws CacheException {
		if (LOG.isTraceEnabled()) {
			LOG.trace("Clearing all objects from cache [" + getName() + "]");
        }
        try {
        	cache.removeAll();
        } catch (Throwable t) {
            throw new CacheException(t);
        }
	}

	@Override
	public long size() throws CacheException {
		try {
            return cache.getSize();
        } catch (Throwable t) {
            throw new CacheException(t);
        }
	}
	
	@Override
	public Set<K> keys() throws CacheException {
		try {
            Set<String> keys = cache.getKeys();
            if (!CollectionUtils.isEmpty(keys)) {
                return (Set<K>) Collections.unmodifiableSet(keys);
            } else {
                return Collections.emptySet();
            }
        } catch (Throwable t) {
            throw new CacheException(t);
        }
	}

	@Override
	public Collection<V> values() throws CacheException {
        try {
            Set<String> keys = cache.getKeys();
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
	
	/**
     * Returns &quot;OSCache [&quot; + cache.getName() + &quot;]&quot;
     * @return &quot;OSCache [&quot; + cache.getName() + &quot;]&quot;
     */
    public String toString() {
        return "OSCache [" + cache.getName() + "]";
    }

}