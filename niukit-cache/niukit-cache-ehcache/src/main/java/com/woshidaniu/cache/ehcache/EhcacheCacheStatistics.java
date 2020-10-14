package com.woshidaniu.cache.ehcache;

import java.io.Serializable;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.statistics.StatisticsGateway;

/**
 *  http://carlosfu.iteye.com/blog/2237387
 *  
 *	1. AssociatedCachedName: cache的名称，对应ehcache.xml中的<cache name="mobilServiceOffHeap".../>
 *	2. CacheHitPercentage: 总缓存命中率 
 *	3. CacheHits: 总缓存命中数 
 *	4. CacheMissPercentage: 总缓存丢失率 
 *	5. CacheMisses: 总缓存丢失数 
 *	6. DiskStoreObjectCount: 硬盘中存储的对象个数 （配置中DiskPersistent必须为true）
 *	7. InMemoryHitPercentage: 堆内命中率 
 *	8. InMemoryHits： 堆内命中数 
 *	9. InMemoryMisses: 堆内丢失数
 *	11. MemoryStoreObjectCount： 堆内存储对象个数
 *	11. OffHeapStoreObjectCount: 堆外存储对象个数
 *	12. OffHeapHitPercentage: 堆外命中率 
 *	13. OffHeapHits： 堆外命中数
 *	14. OffHeapMisses: 堆外丢失数 
 *	15. OnDiskHitPercentage: 堆外命中率
 *	16. OnDiskHits： 堆外命中数
 *	17. OnDiskMisses: 堆外丢失数
 *	18. ObjectCount: 总对象个数 
 *	
 *		如果当前cache是offheapCache，那么总对象数 = OffHeapStoreObjectCount + DiskStoreObjectCount （可能InMemory是Offheap的热点数据，认为是一个对象）
 *		如果当前cache是heapCache，那么总对象数 = InMemoryStoreObjectCount + DiskStoreObjectCount
 *	
 *	19. WriterMaxQueueSize: the maximum size of the write-behind queue (应该是写硬盘的线程，用到的队列)
 *	20. WriteQueueLength:  the size of the write-behind queue(应该是写硬盘的线程，用到的队列)
 */
public class EhcacheCacheStatistics implements Serializable {

	private transient Ehcache ehcache;
    private StatisticsGateway statistics;

    /**
     * Constructs an object from an ehcache statistics object
     *
     * @param ehcache the backing ehcache
     */
    public EhcacheCacheStatistics(Ehcache ehcache) {
        this.ehcache = ehcache;
        this.statistics = ehcache.getStatistics();
    }
    
    /**
     * @return Ehcache 缓存名称, or null is there no associated cache
     */
    public String getAssociatedCacheName() {
        if (statistics == null) {
            return null;
        } else {
            return statistics.getAssociatedCacheName();
        }
    }

    /**
     * @return 总缓存命中率
     */
    public double getCacheHitPercentage() {
        long hits = statistics.cacheHitCount();
        long misses = statistics.cacheMissCount();

        long total = hits + misses;
        return getPercentage(hits, total);
    }
    
    /**
     * @return 总缓存命中数
     */
    public long getCacheHits() {
        return statistics.cacheHitCount();
    }
    
    /**
     * @return 总缓存丢失率
     */
    public double getCacheMissPercentage() {
        long hits = statistics.cacheHitCount();
        long misses = statistics.cacheMissCount();

        long total = hits + misses;
        return getPercentage(misses, total);
    }

    /**
     * @return 总缓存丢失数 
     */
    public long getCacheMisses() {
        return statistics.cacheMissCount();
    }

    /**
     * @return 硬盘中存储的对象个数 （配置中DiskPersistent必须为true）
     */
    public long getDiskStoreObjectCount() {
        return statistics.getLocalDiskSize();
    }
    
    /**
     * @return 堆内命中率 
     */
    public double getInMemoryHitPercentage() {
        long hits = statistics.localHeapHitCount();
        long misses = statistics.localHeapMissCount();

        long total = hits + misses;
        return getPercentage(hits, total);
    }
    
    /**
     * @return 堆内命中数 
     */
    public long getInMemoryHits() {
        return statistics.localHeapHitCount();
    }
    
    /**
     * @return 堆内丢失数
     */
    public long getInMemoryMisses() {
        return statistics.localHeapMissCount();
    }
    
    /**
     * @return 堆内存储对象个数
     */
    public long getMemoryStoreObjectCount() {
        return statistics.getLocalHeapSize();
    }

    /**
     * @return 堆外命中率
     */
    public double getOffHeapHitPercentage() {
        long hits = statistics.localOffHeapHitCount();
        long misses = statistics.localOffHeapMissCount();

        long total = hits + misses;
        return getPercentage(hits, total);
    }
    
    /**
     * @return 堆外命中数
     */
    public long getOffHeapHits() {
        return statistics.localOffHeapHitCount();
    }
    
    /**
     * @return 堆外丢失数 
     */
    public long getOffHeapMisses() {
        return statistics.localOffHeapMissCount();
    }
    
    /**
     * @return 堆外存储对象个数
     */
    public long getOffHeapStoreObjectCount() {
        return statistics.getLocalOffHeapSize();
    }
   
    /**
     * @return 堆外命中率
     */
    public double getOnDiskHitPercentage() {
        long hits = statistics.localDiskHitCount();
        long misses = statistics.localDiskMissCount();

        long total = hits + misses;
        return getPercentage(hits, total);
    }

    /**
     * @return 堆外命中数
     */
    public long getOnDiskHits() {
        return statistics.localDiskHitCount();
    }

    /**
     * @return 堆外丢失数
     */
    public long getOnDiskMisses() {
        return statistics.localDiskMissCount();
    }
    
    /**
     * @return 总对象个数
     */
    public long getObjectCount() {
        return statistics.getSize();
    }

    /**
     * (应该是写硬盘的线程，用到的队列)
     * Gets the size of the write-behind queue, if any.
     * The value is for all local buckets
     * @return Elements waiting to be processed by the write behind writer. -1 if no write-behind
     */
    public long getWriterQueueLength() {
        return statistics.getWriterQueueLength();
    }

    /**
     *  (应该是写硬盘的线程，用到的队列)
     */
    public int getWriterMaxQueueSize() {
        return ehcache.getCacheConfiguration().getCacheWriterConfiguration().getWriteBehindMaxQueueSize();
    }
    
    public Ehcache getEhcache() {
        return ehcache;
    }

    private static double getPercentage(long number, long total) {
        if (total == 0) {
            return 0.0;
        } else {
            return number / (double)total;
        }
    }
    
}
