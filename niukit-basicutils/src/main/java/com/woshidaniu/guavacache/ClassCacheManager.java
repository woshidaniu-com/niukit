/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.guavacache;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.woshidaniu.beanutils.BeanUtils;
import com.woshidaniu.beanutils.reflection.ClassUtils;
import com.woshidaniu.guavacache.constant.CacheCanstant;
import com.woshidaniu.guavacache.utils.CacheConfigUtils;

/**
 * 
 *@类名称	: ClassCacheManager.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 25, 2016 11:34:45 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public abstract class ClassCacheManager{

	protected static Logger LOG = LoggerFactory.getLogger(StreamCacheManager.class);
	protected static Properties config = CacheConfigUtils.getConfig();
	protected static LoadingCache<String, Class<?>> classCache;
	
	static {
		
		String maxThread = config.getProperty(CacheCanstant.GUAVA_CACHE_MAXTHREAD,"8");
		String minSize = config.getProperty(CacheCanstant.GUAVA_CACHE_MINSIZE,"10");
		String maxSize = config.getProperty(CacheCanstant.GUAVA_CACHE_MAXSIZE,"100");
		String expireAfterAccess = config.getProperty(CacheCanstant.GUAVA_CACHE_EXPIRE_AFTER_ACCESS,"100");
		String expireAfterWrite = config.getProperty(CacheCanstant.GUAVA_CACHE_EXPIRE_AFTER_WRITE,"100");
		//Class对象缓存，减少对象创建的消耗
		//CacheBuilder的构造函数是私有的，只能通过其静态方法newBuilder()来获得CacheBuilder的实例
		classCache = CacheBuilder.newBuilder()
	        //设置并发级别，并发级别是指可以同时写缓存的线程数
	        .concurrencyLevel(Integer.parseInt(maxThread))
	        //设置缓存被创建或值被替换后多少分钟后过期
	        .expireAfterWrite(Integer.parseInt(expireAfterWrite), TimeUnit.MINUTES)
	        //设置缓存最后一次访问多少分钟后过期
	        .expireAfterWrite(Integer.parseInt(expireAfterAccess), TimeUnit.MINUTES)
	        //设置缓存容器的初始容量
	        .initialCapacity(Integer.parseInt(minSize))
	        //设置缓存最大容量限制，超过限制之后就会按照LRU最近虽少使用算法来移除缓存项
	        .maximumSize(Integer.parseInt(maxSize))
	        //设置要统计缓存的命中率
	        .recordStats()
	        //设置缓存的移除通知
	        .removalListener(new RemovalListener<String, Class<?>>() {
	            @Override
	            public void onRemoval(RemovalNotification<String, Class<?>> notification) {
	            	LOG.info(notification.getKey() + " was removed, cause is " + notification.getCause());
	            }
	        })
	        //build方法中可以指定CacheLoader，在缓存不存在时通过CacheLoader的实现自动加载缓存
			.build(new CacheLoader<String, Class<?>>() {
				@Override
				public Class<?> load(String className) throws Exception {
					LOG.info("Reload Class [ {0} ] " , className);
					ClassLoader contextCL = Thread.currentThread().getContextClassLoader();
			        ClassLoader classLoader = contextCL == null ? ClassCacheManager.class.getClassLoader() : contextCL;
					return ClassUtils.forName(className,classLoader);
				}
			});
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getObject(String className) {
		try {
			Class<T> clazz  = (Class<T>) classCache.get(className);
			//复杂数据,防止操作原数据
			return BeanUtils.instantiateClass(clazz);
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
