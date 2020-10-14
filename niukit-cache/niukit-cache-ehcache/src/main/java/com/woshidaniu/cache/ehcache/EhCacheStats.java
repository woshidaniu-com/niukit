package com.woshidaniu.cache.ehcache;

import java.util.Locale;

/**
 * 常用缓存对象状态
 */
public enum EhCacheStats {

	
	/**cache_hits：命中的次数,获取数据成功的次数。*/
	STAT_CACHE_HITS("cache_hits"),
	STAT_CACHE_HITS_PERCENTAGE("cache_hits_percentage"),
	/**cache_misses：没有命中的次数,获取数据失败的次数。*/
	STAT_CACHE_MISSES("cache_misses"),
	STAT_CACHE_MISSES_PERCENTAGE("cache_misses_percentage"),
	STAT_DISK_STORE_OBJECT_COUNT("disk_store_object_count"),
	STAT_IN_MEMORY_HIT_PERCENTAGE("inmemory_hit_percentage"),
	STAT_IN_MEMORY_HITS("inmemory_hits"),
	STAT_IN_MEMORY_MISSES("inmemory_misses"),
	STAT_MEMORY_STORE_OBJECT_COUNT("memory_store_object_count"),
	STAT_OBJECT_COUNT("object_count"),
	STAT_OFF_HEAP_HIT_PERCENTAGE("offheap_hit_percentage"),
	STAT_OFF_HEAP_HITS("offheap_hits"),
	STAT_OFF_HEAP_MISSES("offheap_misses"),
	STAT_OFF_HEAP_STORE_OBJECT_COUNT("offheap_store_object_count"),
	STAT_ON_DISK_HIT_PERCENTAGE("ondisk_hit_percentage"),
	STAT_ON_DISK_HITS("ondisk_hits"),
	STAT_ON_DISK_MISSES("ondisk_misses"),
	STAT_STATISTICS_ACCURACY("statistics_accuracy"),
	STAT_STATISTICS_ACCURACY_DESCRIPTION("statistics_accuracy_description"),
	STAT_WRITER_QUEUE_LENGTH("writer_queue_length"),
	STAT_WRITER_MAX_QUEUE_SIZE("writer_max_queue_size");
	
	protected String name;

	private EhCacheStats(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	static EhCacheStats valueOfIgnoreCase(String stat) {
		return valueOf(stat.toUpperCase(Locale.ENGLISH).trim());
	}
	
}
