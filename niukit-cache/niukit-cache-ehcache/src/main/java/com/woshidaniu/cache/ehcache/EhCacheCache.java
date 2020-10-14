package com.woshidaniu.cache.ehcache;

import java.net.InetAddress;
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

import com.woshidaniu.cache.core.Cache;
import com.woshidaniu.cache.core.exception.CacheException;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.management.CacheStatistics;

/**
 * 
 * @className	： EhCacheCache
 * @description	： TODO(描述这个类的作用)
 * @author 		： kangzhidong
 * @date		： Jan 23, 2016 9:04:02 PM
 */
@SuppressWarnings("unchecked")
public class EhCacheCache<K, V> implements Cache<K, V> {
	
	protected static final Logger LOG = LoggerFactory.getLogger(EhCacheCache.class);
	protected final Ehcache cache;
	protected String keyPrefix; //关键字前缀字符，区别属于哪个模块  
	/**
     * JMX cache statistics
     */
	protected CacheStatistics statistics;
    
	public EhCacheCache(Ehcache ehcache) {
		this(ehcache, null);
	}
	
	public EhCacheCache(Ehcache ehcache,String keyPrefix) {
		if (ehcache == null) {
			throw new CacheException("error ehcache");
		}
		this.cache = ehcache;
		this.statistics = new CacheStatistics(ehcache);
		this.keyPrefix = keyPrefix;
	}

	@Override
	public String getName() throws CacheException {
		try {
			return this.cache.getName();
		} catch (Exception e) {
			throw new CacheException(e);
		}
	}

	protected String getKey(K k) {
		return this.keyPrefix == null ? String.valueOf(k) : this.keyPrefix + "_" + String.valueOf(k);
	}
	
	@Override
	public V get(K k) throws CacheException {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.trace("Get data in cache [" + cache.getName() + "] for key [" + k + "]");
			}
			if(k == null){
				return null;
			}
			Element element = cache.get(k);
			if(element == null){
				if (LOG.isDebugEnabled()) {
					LOG.debug("Data from KEY:{} is null.", k);
				}
				return null;
			}
			return (V) element.getObjectValue();
		} catch (Exception e) {
			throw new CacheException(e);
		}
	}

	@Override
	public V put(K k, V v) throws CacheException {
		if (LOG.isDebugEnabled()) {
			LOG.trace("Put data in cache [" + cache.getName() + "] for key [" + k + "]");
        }
        try {
            V previous = get(k);
            Element element = new Element(k, v);
            cache.put(element);
            cache.flush();
            return previous;
        } catch (Throwable t) {
            throw new CacheException(t);
        }
	}

	@Override
	public V remove(K k) throws CacheException {
		if (LOG.isDebugEnabled()) {
			LOG.trace("Remove data from cache [" + cache.getName() + "] for key [" + k + "]");
        }
        try {
            V previous = get(k);
            cache.remove(k);
            return previous;
        } catch (Throwable t) {
            throw new CacheException(t);
        }
	}

	@Override
	public void clear() throws CacheException {
		if (LOG.isDebugEnabled()) {
			LOG.trace("Clear all data from cache [" + cache.getName() + "]");
        }
        try {
        	cache.removeAll();
        } catch (Throwable t) {
            throw new CacheException(t);
        }
	}

	@Override
	public Set<K> keys() throws CacheException {
		 try {
            List<K> keys = cache.getKeys();
            if (keys != null && keys.size() > 0) {
                return Collections.unmodifiableSet(new LinkedHashSet<K>(keys));
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
			List<K> keys = cache.getKeys();
			if (keys != null && keys.size() > 0) {
				List<V> values = new ArrayList<V>(keys.size());
				for (K key : keys) {
					V value = get(key);
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
	public long size() throws CacheException {
		try {
            return cache.getSize();
        } catch (Throwable t) {
            throw new CacheException(t);
        }
	}
	
	@Override
	public Map<String, Map<String, String>> stats() throws CacheException {
		try {
			
			Map<String, Map<String, String>> statsMaps = new HashMap<String, Map<String,String>>();
			
			Map<String, String> statsMap = new HashMap<String, String>();
			//
			statsMap.put(EhCacheStats.STAT_CACHE_HITS.getName(), Long.toString(statistics.getCacheHits()));
			statsMap.put(EhCacheStats.STAT_CACHE_HITS_PERCENTAGE.getName(), Double.toString(statistics.getCacheHitPercentage()));
			statsMap.put(EhCacheStats.STAT_CACHE_MISSES.getName(), Long.toString(statistics.getCacheMisses()));
			statsMap.put(EhCacheStats.STAT_CACHE_MISSES_PERCENTAGE.getName(), Double.toString(statistics.getCacheMissPercentage()));
			statsMap.put(EhCacheStats.STAT_DISK_STORE_OBJECT_COUNT.getName(), Long.toString(statistics.getDiskStoreObjectCount()));
			statsMap.put(EhCacheStats.STAT_IN_MEMORY_HIT_PERCENTAGE.getName(), Double.toString(statistics.getInMemoryHitPercentage()));
			statsMap.put(EhCacheStats.STAT_IN_MEMORY_HITS.getName(), Long.toString(statistics.getInMemoryHits()));
			statsMap.put(EhCacheStats.STAT_IN_MEMORY_MISSES.getName(), Long.toString(statistics.getInMemoryMisses()));
			statsMap.put(EhCacheStats.STAT_MEMORY_STORE_OBJECT_COUNT.getName(), Long.toString(statistics.getMemoryStoreObjectCount()));
			statsMap.put(EhCacheStats.STAT_OBJECT_COUNT.getName(), Long.toString(statistics.getObjectCount()));
			statsMap.put(EhCacheStats.STAT_OFF_HEAP_HIT_PERCENTAGE.getName(), Double.toString(statistics.getOffHeapHitPercentage()));
			statsMap.put(EhCacheStats.STAT_OFF_HEAP_HITS.getName(), Long.toString(statistics.getOffHeapHits()));
			statsMap.put(EhCacheStats.STAT_OFF_HEAP_MISSES.getName(), Long.toString(statistics.getOffHeapMisses()));
			statsMap.put(EhCacheStats.STAT_OFF_HEAP_STORE_OBJECT_COUNT.getName(), Long.toString(statistics.getOffHeapStoreObjectCount()));
			statsMap.put(EhCacheStats.STAT_ON_DISK_HIT_PERCENTAGE.getName(), Double.toString(statistics.getOnDiskHitPercentage()));
			statsMap.put(EhCacheStats.STAT_ON_DISK_HITS.getName(), Long.toString(statistics.getOnDiskHits()));
			statsMap.put(EhCacheStats.STAT_ON_DISK_MISSES.getName(), Long.toString(statistics.getOnDiskMisses()));
			//statsMap.put(EhCacheStats.STAT_STATISTICS_ACCURACY.getName(), Integer.toString(statistics.getStatisticsAccuracy()));
			//statsMap.put(EhCacheStats.STAT_STATISTICS_ACCURACY_DESCRIPTION.getName(), statistics.getStatisticsAccuracyDescription());
			statsMap.put(EhCacheStats.STAT_WRITER_QUEUE_LENGTH.getName(), Long.toString(statistics.getWriterQueueLength()));
			statsMap.put(EhCacheStats.STAT_WRITER_MAX_QUEUE_SIZE.getName(), Integer.toString(statistics.getWriterMaxQueueSize()));
			
			statsMaps.put(InetAddress.getLocalHost().getHostAddress() , statsMap);
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
     * Returns the size (in bytes) that this EhCache is using in memory (RAM), or <code>-1</code> if that
     * number is unknown or cannot be calculated.
     *
     * @return the size (in bytes) that this EhCache is using in memory (RAM), or <code>-1</code> if that
     *         number is unknown or cannot be calculated.
     */
    public long getMemoryUsage() {
        try {
            return cache.calculateInMemorySize();
        }
        catch (Throwable t) {
            return -1;
        }
    }

    /**
     * Returns the size (in bytes) that this EhCache's memory store is using (RAM), or <code>-1</code> if
     * that number is unknown or cannot be calculated.
     *
     * @return the size (in bytes) that this EhCache's memory store is using (RAM), or <code>-1</code> if
     *         that number is unknown or cannot be calculated.
     */
    public long getMemoryStoreSize() {
        try {
            return cache.getMemoryStoreSize();
        }
        catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    /**
     * Returns the size (in bytes) that this EhCache's disk store is consuming or <code>-1</code> if
     * that number is unknown or cannot be calculated.
     *
     * @return the size (in bytes) that this EhCache's disk store is consuming or <code>-1</code> if
     *         that number is unknown or cannot be calculated.
     */
    public long getDiskStoreSize() {
        try {
            return cache.getDiskStoreSize();
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    /**
     * Returns &quot;EhCache [&quot; + cache.getName() + &quot;]&quot;
     *
     * @return &quot;EhCache [&quot; + cache.getName() + &quot;]&quot;
     */
    public String toString() {
        return "EhCache [" + cache.getName() + "]";
    }

}
