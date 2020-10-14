package com.woshidaniu.cache.core;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.woshidaniu.cache.core.exception.CacheException;

public interface Cache<K, V> {

	/** 
     * 缓存时效 1秒钟； 过期时间单位是秒
     */  
    public static final int CACHE_EXP_SECOND = 1 ; 
    
	/** 
     * 缓存时效 1分钟； 过期时间单位是秒
     */  
    public static final int CACHE_EXP_MINUTE = 60 * CACHE_EXP_SECOND ;  
    
	/** 
     * 缓存时效 1小时； 过期时间单位是秒
     */  
    public static final int CACHE_EXP_HOUR = 60 * CACHE_EXP_MINUTE;  
     
	/** 
     * 缓存时效 1天 ； 过期时间单位是秒
     */  
    public static final int CACHE_EXP_DAY = CACHE_EXP_HOUR * 24 ;  
  
    /** 
     * 缓存时效 1周 ； 过期时间单位是秒
     */  
    public static final int CACHE_EXP_WEEK = CACHE_EXP_DAY  * 7;  
  
    /** 
     * 缓存时效 1月 ； 过期时间单位是秒
     */  
    public static final int CACHE_EXP_MONTH = CACHE_EXP_DAY * 30 ;  
  
    /** 
     * 缓存时效 永久 
     */  
    public static final int CACHE_EXP_FOREVER = 0;  
  
    /** 
     * 冲突延时 1秒 
     */  
    public static final int MUTEX_EXP = 1;  
    /** 
     * 冲突键 
     */  
    public static final String MUTEX_KEY_PREFIX = "MUTEX_";  
    
	/**
	 * 
	 * @description	： 返回缓存的名称
	 * @author 		： kangzhidong
	 * @date 		：Jan 23, 2016 8:52:44 PM
	 * @return
	 * @throws CacheException
	 */
	String getName() throws CacheException;

	/**
	 * 
	 * @description	： 获得缓存值
	 * @author 		： kangzhidong
	 * @date 		：Jan 23, 2016 8:54:31 PM
	 * @param key	: 缓存key
	 * @return		: 缓存值
	 * @throws CacheException
	 */
	V get(K k) throws CacheException;
	
	/**
	 * 
	 * @description	： 设置缓存
	 * @author 		： kangzhidong
	 * @date 		：Jan 23, 2016 8:53:57 PM
	 * @param key	: 缓存key
	 * @param value	: 缓存值
	 * @throws CacheException
	 */
	V put(K k, V v) throws CacheException;

	/**
	 * 
	 * @description	：回收缓存
	 * @author 		： kangzhidong
	 * @date 		：Jan 23, 2016 8:53:46 PM
	 * @param key
	 * @throws CacheException
	 */
	V remove(K k) throws CacheException;
	
	/**
	 * 
	 * @description	： 清空所有的缓存
	 * @author 		： kangzhidong
	 * @date 		：Jan 23, 2016 8:53:37 PM
	 * @throws CacheException
	 */
	void clear() throws CacheException;

	/**
	 * 
	 * @description	： 获得所有的key
	 * @author 		： kangzhidong
	 * @date 		：Jan 23, 2016 8:53:04 PM
	 * @return		: key集合
	 * @throws CacheException
	 */
	Set<K> keys() throws CacheException;
	
	/**
	 * 
	 * @description	： 获得所有的value
	 * @author 		： kangzhidong
	 * @date 		：Jan 23, 2016 8:53:04 PM
	 * @return		: key集合
	 * @throws CacheException
	 */
	Collection<V> values() throws CacheException;
	
	/**
	 * 
	 * @description	： 获得缓存的key-value个数
	 * @author 		： kangzhidong
	 * @date 		：Jan 23, 2016 8:55:01 PM
	 * @return		: key-value个数
	 * @throws CacheException
	 */
	long size() throws CacheException;

	/**
	 * 
	 * @description	： 获得缓存对象的统计分析结果
	 * @author 		： kangzhidong
	 * @date 		：Jan 23, 2016 8:55:01 PM
	 * @return		: 缓存对象的统计分析结果
	 * @throws CacheException
	 */
	Map<String,Map<String,String>> stats() throws CacheException;

	/**
	 * 
	 * @description	： 获得缓存对象的原生对象
	 * @author 		： kangzhidong
	 * @date 		：Jan 23, 2016 8:55:01 PM
	 * @return		: key-value个数
	 * @throws CacheException
	 */
	Object origin() throws CacheException;
	
}
