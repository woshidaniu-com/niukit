package com.woshidaniu.cache.ehcache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

/***
 * 
 * @className	： EhcacheCacheBuilder
 * @description	： Ehcache缓存cache对象构建
 * @author 		： kangzhidong
 * @date		： Jan 1, 2016 10:23:09 PM
 */
public class EhcacheCacheBuilder{

	/**
	 * 缓存节点 格式 IP:PORT:WEIGHT 如 192.168.137.2:11211:1
	 * 如果需要配置多台缓存此方法还需要重新实现
	 * @throws Exception 
	 */
	public EhcacheCacheBuilder(String configXML,String nodeName) throws Exception {
		CacheManager cacheManager = CacheManager.create(configXML);
		/*
		// 或者
		cacheManager = CacheManager.getInstance();
		// 或者
		cacheManager = CacheManager.create("/config/ehcache.xml");
		// 或者
		cacheManager = CacheManager.create("http://localhost:8080/test/ehcache.xml");
		cacheManager = CacheManager.newInstance("/config/ehcache.xml");
		// .......
*/		
		// 获取ehcache配置文件中的一个cache

		Cache sample = cacheManager.getCache(nodeName);
		
		
	}
	
}
