package com.woshidaniu.cache.core.memory;

/**
 * *******************************************************************
 * @className	： MapCache
 * @description	： TODO(描述这个类的作用)
 * @author 		： <a href="mailto:hnxyhcwdl1003@163.com">kangzhidong</a>
 * @date		： Oct 31, 2016 9:06:16 AM
 * @version 	V1.0 
 * *******************************************************************
 */
import java.net.InetAddress;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import com.woshidaniu.cache.core.Cache;
import com.woshidaniu.cache.core.exception.CacheException;

/**
 * A <code>MapCache</code> is a {@link Cache Cache} implementation that uses a backing {@link Map} instance to store
 * and retrieve cached data.
 *
 * @since 1.0
 */
public class MapCache<K, V> implements Cache<K, V> {

    /**
     * Backing instance.
     */
    private final Map<K, V> map;

    /**
     * The name of this cache.
     */
    private final String name;
    
    @Override
	public String getName() throws CacheException {
		return name;
	}

    public MapCache(String name, Map<K, V> backingMap) {
        if (name == null) {
            throw new IllegalArgumentException("Cache name cannot be null.");
        }
        if (backingMap == null) {
            throw new IllegalArgumentException("Backing map cannot be null.");
        }
        this.name = name;
        this.map = backingMap;
    }

    public V get(K key) throws CacheException {
        return map.get(key);
    }

    public V put(K key, V value) throws CacheException {
        return map.put(key, value);
    }

    public V remove(K key) throws CacheException {
        return map.remove(key);
    }

    public void clear() throws CacheException {
        map.clear();
    }

    public long size() {
        return map.size();
    }

    public Set<K> keys() {
        Set<K> keys = map.keySet();
        if (!keys.isEmpty()) {
            return Collections.unmodifiableSet(keys);
        }
        return Collections.emptySet();
    }

    public Collection<V> values() {
        Collection<V> values = map.values();
        if (!CollectionUtils.isEmpty(values)) {
            return Collections.unmodifiableCollection(values);
        }
        return Collections.emptySet();
    }

    @Override
	public Map<String, Map<String, String>> stats() throws CacheException {
		try {
			
			Map<String, Map<String, String>> statsMaps = new HashMap<String, Map<String,String>>();
			
			Map<String, String> statsMap = new HashMap<String, String>();
			
			
			statsMaps.put(InetAddress.getLocalHost().getHostAddress() , statsMap);
			return statsMaps;
		} catch (Exception e) {
			throw new CacheException(e);
		}
	}

	@Override
	public Object origin() throws CacheException {
		return map;
	}
    
    public String toString() {
        return new StringBuilder("MapCache '")
                .append(name).append("' (")
                .append(map.size())
                .append(" entries)")
                .toString();
    }
	
}
