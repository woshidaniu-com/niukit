package com.woshidaniu.cache.core;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.woshidaniu.basicutils.StringUtils;
import com.woshidaniu.cache.core.exception.CacheException;
import com.woshidaniu.cache.core.utils.LifecycleUtils;


@SuppressWarnings({"unchecked","rawtypes"})
public abstract class AbstractCacheManager implements CacheManager,Destroyable {

	/**
     * Retains all Cache objects maintained by this cache manager.
     */
   
	private final ConcurrentMap<String, Cache> caches;
	
	 /**
     * Default no-arg constructor that instantiates an internal name-to-cache {@code ConcurrentMap}.
     */
    public AbstractCacheManager() {
        this.caches = new ConcurrentHashMap<String, Cache>();
    }
	
	//protected abstract Collection<? extends Cache> addCaches();
	protected abstract <K, V> Cache<K, V> createCache(String name) throws CacheException;
	
	@Override
	public final <K, V> Cache<K, V> getCache(String name) throws IllegalArgumentException, CacheException {
		if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("Cache name cannot be null or empty.");
        }

        Cache cache = caches.get(name);
        if (cache == null) {
            cache = this.createCache(name);
            Cache existing = caches.putIfAbsent(name, cache);
            if (existing != null) {
                cache = existing;
            }
        }

        //noinspection unchecked
        return cache;
	}

	@Override
	public Collection<String> getCacheNames() {
		if(caches != null){
			Set<String> cacheNames = new LinkedHashSet<String>();
			 for (Cache cache : caches.values()) {
				 cacheNames.add(cache.getName());
			}
			return Collections.unmodifiableSet(cacheNames);
		}
		return null;
	}

	public void destroy() throws Exception {
        while (!caches.isEmpty()) {
            for (Cache cache : caches.values()) {
                LifecycleUtils.destroy(cache);
            }
            caches.clear();
        }
    }
	
	public String toString() {
        Collection<Cache> values = caches.values();
        StringBuilder sb = new StringBuilder(getClass().getSimpleName())
                .append(" with ")
                .append(caches.size())
                .append(" cache(s)): [");
        int i = 0;
        for (Cache cache : values) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(cache.toString());
            i++;
        }
        sb.append("]");
        return sb.toString();
    }
	
}
