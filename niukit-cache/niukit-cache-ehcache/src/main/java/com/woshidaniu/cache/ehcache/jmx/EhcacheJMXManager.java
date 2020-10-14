package com.woshidaniu.cache.ehcache.jmx;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.management.ManagementService;

public class EhcacheJMXManager {

	protected CacheManager cacheManager;

	public CacheManager getCacheManager() {
		return cacheManager;
	}

	public void setCacheManager(CacheManager cacheManager) {
		
		//获取MBeanServer  
		MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();  
		//将Ehcache的MBean注册到MBeanServer  
		//ManagementService.registerMBeans(cacheManager, mBeanServer, true, true, true, true);
		
		//http://blog.csdn.net/happyqwz/article/details/8271783
		ManagementService.registerMBeans(cacheManager, mBeanServer, false, false, false, true);
		
		this.cacheManager = cacheManager;
	}
	
	
}
