/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.guavacache.constant;

/**
 *@类名称	: CacheCanstant.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 25, 2016 2:21:45 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public abstract class CacheCanstant {

	/**
	 * 设置并发级别为8，并发级别是指可以同时写缓存的线程数
	 */
	public static String GUAVA_CACHE_MAXTHREAD = "guava.cache.maxThread";
	/**
	 * 设置缓存容器的初始容量为10
	 */
	public static String GUAVA_CACHE_MINSIZE = "guava.cache.minSize";
	/**
	 * 设置缓存最大容量限制，超过限制之后就会按照LRU最近虽少使用算法来移除缓存项
	 */
	public static String GUAVA_CACHE_MAXSIZE = "guava.cache.maxSize";
	/**
	 * 根据某个键值对最后一次访问之后多少时间后移除;单位：分钟
	 */
	public static String GUAVA_CACHE_EXPIRE_AFTER_ACCESS = "guava.cache.expireAfterAccess";
	/**
	 * 根据某个键值对被创建或值被替换后多少时间移除;单位：分钟
	 */
	public static String GUAVA_CACHE_EXPIRE_AFTER_WRITE = "guava.cache.expireAfterWrite";

	
}
