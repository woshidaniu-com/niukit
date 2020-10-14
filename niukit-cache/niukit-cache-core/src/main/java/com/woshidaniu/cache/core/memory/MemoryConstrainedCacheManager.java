package com.woshidaniu.cache.core.memory;

import com.woshidaniu.cache.core.AbstractCacheManager;
import com.woshidaniu.cache.core.Cache;


/**
 * *******************************************************************
 * @className	： MemoryConstrainedCacheManager
 * @description	： TODO(描述这个类的作用)
 * @author 		： <a href="mailto:hnxyhcwdl1003@163.com">kangzhidong</a>
 * @date		： Oct 31, 2016 9:22:12 AM
 * @version 	V1.0 
 * *******************************************************************
 */
public class MemoryConstrainedCacheManager extends AbstractCacheManager {
	
	@Override
    protected Cache<Object, Object> createCache(String name) {
        return new MapCache<Object, Object>(name, new SoftHashMap<Object, Object>());
    }
	
}
