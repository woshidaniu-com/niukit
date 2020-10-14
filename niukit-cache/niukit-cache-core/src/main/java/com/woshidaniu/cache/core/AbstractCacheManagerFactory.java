package com.woshidaniu.cache.core;

import java.util.HashMap;
import java.util.Map;
 

/**
 * 生成各种缓存管理器的工厂方法。其中：<br/>
 * 1:EhCache <li>默认的配置为/ehcache.xml <li>
 * 默认只能创建一个EhCacheManager实例，且只创建第一个被实例化的EhCacheManager <br/>
 * 2:Redis <br/>
 * 3:Memcached<br/>
 */
public abstract class AbstractCacheManagerFactory {

	// 缓存管理器Map。这个地方主要是为了实现多个缓存
	protected final static Map<String, CacheManager> cacheManagerMap = new HashMap<String, CacheManager>();

	public AbstractCacheManagerFactory() {
		
	}

	/**
	 * 
	 * @description	： 默认的缓存管理器
	 * @author 		： kangzhidong
	 * @date 		：Jan 23, 2016 8:58:41 PM
	 * @return
	 */
	public CacheManager getCacheManager() {
		return getCacheManager("default");
	}

	/**
	 * 
	 * @description	： 根据管理器名称获得缓存管理器
	 * @author 		： kangzhidong
	 * @date 		：Jan 23, 2016 8:58:30 PM
	 * @param 		: managerName
	 * @return
	 */
	public CacheManager getCacheManager(String managerName) {
		if (cacheManagerMap.get(managerName) != null) {
			return cacheManagerMap.get(managerName);
		}
		// 根据配置文件信息实例化不同的CacheManager
		CacheManager cacheManager = getCacheManagerInstance(managerName);
		cacheManagerMap.put(managerName, cacheManager);
		return cacheManager;
	}

	/**
	 * 
	 * @description	：初始化各个CacheManager
	 * @author 		： kangzhidong
	 * @date 		：Jan 25, 2016 3:14:59 PM
	 * @param managerName
	 * @return
	 */
	public abstract CacheManager getCacheManagerInstance(String managerName);

}
