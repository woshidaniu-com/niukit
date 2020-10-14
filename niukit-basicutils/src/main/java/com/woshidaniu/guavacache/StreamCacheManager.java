package com.woshidaniu.guavacache;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
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
import com.woshidaniu.guavacache.constant.CacheCanstant;
import com.woshidaniu.guavacache.utils.CacheConfigUtils;
import com.woshidaniu.io.utils.FileUtils;

/**
 * 
 *@类名称	: StreamCacheManager.java
 *@类描述	：文件字节缓存管理
 *@创建人	：kangzhidong
 *@创建时间	：Mar 25, 2016 11:28:36 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public abstract class StreamCacheManager {

	protected static Logger LOG = LoggerFactory.getLogger(StreamCacheManager.class);
	protected static Properties config = CacheConfigUtils.getConfig();
	protected static LoadingCache<String, byte[]> byteCache;

	//静态代码块跟静态变量都是类加载时进行初始化的（同等条件下，初始化顺序由书写顺序决定）
	static {
		
		String maxThread = config.getProperty(CacheCanstant.GUAVA_CACHE_MAXTHREAD,"8");
		String minSize = config.getProperty(CacheCanstant.GUAVA_CACHE_MINSIZE,"10");
		String maxSize = config.getProperty(CacheCanstant.GUAVA_CACHE_MAXSIZE,"100");
		String expireAfterAccess = config.getProperty(CacheCanstant.GUAVA_CACHE_EXPIRE_AFTER_ACCESS,"100");
		String expireAfterWrite = config.getProperty(CacheCanstant.GUAVA_CACHE_EXPIRE_AFTER_WRITE,"100");
		
		//Class对象缓存，减少对象创建的消耗
		//CacheBuilder的构造函数是私有的，只能通过其静态方法newBuilder()来获得CacheBuilder的实例
		byteCache = CacheBuilder.newBuilder()
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
	        .removalListener(new RemovalListener<String, byte[]>() {
	            @Override
	            public void onRemoval(RemovalNotification<String, byte[]> notification) {
	            	LOG.info(notification.getKey() + " was removed, cause is " + notification.getCause());
	            }
	        })
	        //build方法中可以指定CacheLoader，在缓存不存在时通过CacheLoader的实现自动加载缓存
			.build(new CacheLoader<String, byte[]>() {
				@Override
				public byte[] load(String filePath) throws Exception {
					LOG.info("Reload File [ {0}] " , filePath);
					return FileUtils.toByteArray(new File(filePath));
				}
			});
	}
	
	public static InputStream getInputStream(String filePath) {
		try {
			byte[] cacheBytes = byteCache.get(filePath);
			//复杂数据,防止操作原数据
			byte[] result = Arrays.copyOf(cacheBytes, cacheBytes.length);
			return new ByteArrayInputStream(result);
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

}
