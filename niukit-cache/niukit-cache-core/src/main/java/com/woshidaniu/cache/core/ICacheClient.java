package com.woshidaniu.cache.core;

import com.woshidaniu.cache.core.interceptor.CacheInvocation;

/**
 * 
 *@类名称	: ICacheClient.java
 *@类描述	：通用缓存客户端接口
 *@创建人	：kangzhidong
 *@创建时间	：Mar 18, 2016 10:04:00 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public interface ICacheClient {
	
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
	 *@描述		：设置key指定对象的缓存;生命周期为永久
	 *@创建人	: kangzhidong
	 *@创建时间	: Mar 18, 201610:04:13 AM
	 *@param key	：键（key）
	 *@param value	：要设置缓存中的对象（value），如果没有则插入，如果有则修改
	 *@return
	 *@修改人	: 
	 *@修改时间	: 
	 *@修改描述	:
	 */
	public boolean set( String key, Object value);

	/**
	 * 
	 *@描述		：  设置key指定对象的缓存
	 *@创建人	: kangzhidong
	 *@创建时间	: Mar 18, 201610:07:36 AM
	 * @param key	：键（key）
	 * @param expiry：过期时间（单位是秒）
	 * @param value	：要设置缓存中的对象（value），如果没有则插入，如果有则修改。
	 *@return
	 *@修改人	: 
	 *@修改时间	: 
	 *@修改描述	:
	 */
	public boolean set(String key,int expiry,Object value);
	 
	/**
	 * 
	 *@描述		：替换指定key的缓存对象，如果有则修改
	 *@创建人	: kangzhidong
	 *@创建时间	: Mar 18, 201610:10:21 AM
	 *@param key	：键（key）
	 *@param value	：该键的新值（new value），如果有则修改。
	 *@return
	 *@修改人	: 
	 *@修改时间	: 
	 *@修改描述	:
	 */
	public boolean replace(String key,Object value);
	
	/**
	 * 
	 *@描述		：替换指定key的缓存对象，如果有则修改。
	 *@创建人	: kangzhidong
	 *@创建时间	: Mar 18, 201610:10:45 AM
	 *@param key	：键（key）
	 *@param expiry	：过期时间（单位是秒）
	 *@param value	：该键的新值（new value），如果有则修改。
	 *@return
	 *@修改人	: 
	 *@修改时间	: 
	 *@修改描述	:
	 */
	public boolean replace(String key,int expiry,Object value);
	
	/**
	 * 
	 *@描述		： 删除指定key的缓存值，并返回删除结果
	 *@创建人	: kangzhidong
	 *@创建时间	: Mar 18, 201610:07:21 AM
	 *@param key：  键（key）
	 *@return
	 *@修改人	: 
	 *@修改时间	: 
	 *@修改描述	:
	 */
	public boolean delete( String key );
	
	/**
	 * 
	 *@描述		：获取指定key的缓存值
	 *@创建人	: kangzhidong
	 *@创建时间	: Mar 18, 201610:07:09 AM
	 *@param <T>
	 *@param key ：  键（key）
	 *@return
	 *@修改人	: 
	 *@修改时间	: 
	 *@修改描述	:
	 */
	public <T> T get(String key);
	
	/**
	 * 
	 *@描述		：获取指定key的缓存值
	 *@创建人	: kangzhidong
	 *@创建时间	: Mar 18, 201610:06:48 AM
	 *@param <T>
	 *@param key			 ：  键（key）
	 *@param invocation 	: 回调接口，用于获取缓存周期和原始对象
	 *@return
	 *@修改人	: 
	 *@修改时间	: 
	 *@修改描述	:
	 */
	public <T> T get(String key,CacheInvocation<T> invocation);

	/**
	 * 
	 *@描述		： 获取指定key的缓存值
	 *@创建人	: kangzhidong
	 *@创建时间	: Mar 18, 201610:06:19 AM
	 *@param <T>
	 *@param lock : 作为同步锁的对象
	 *@param key  ： 键（key） 
	 *@param invocation  : 回调接口，用于获取缓存周期和原始对象
	 *@return
	 *@修改人	: 
	 *@修改时间	: 
	 *@修改描述	:
	 */
	public <T> T get(Object lock,String key,CacheInvocation<T> invocation);
	
	/**
	 * 
	 *@描述		：判断相同key,相同过期时间的对象是否已经过期：可以用此方法判断是否冲突
	 *@创建人	: kangzhidong
	 *@创建时间	: Mar 18, 201610:06:00 AM
	 *@param key：键（key）
	 *@return
	 *@修改人	: 
	 *@修改时间	: 
	 *@修改描述	:
	 */
	public boolean isMutex(String key);

	/**
	 * 
	 *@描述		：判断相同key,相同过期时间的对象是否已经过期：可以用此方法判断是否冲突
	 *@创建人	: kangzhidong
	 *@创建时间	: Mar 18, 201610:05:37 AM
	 *@param key ：键（key）
	 *@param exp ：过期时间（单位是秒）
	 *@return
	 *@修改人	: 
	 *@修改时间	: 
	 *@修改描述	:
	 */
	public boolean isMutex(String key, int exp);
		
	/**
	 * 
	 *@描述		：判断指定key缓存是否已经快接近过期时间
	 *@创建人	: kangzhidong
	 *@创建时间	: Mar 18, 201610:05:28 AM
	 *@param key
	 *@param exp
	 *@return
	 *@修改人	: 
	 *@修改时间	: 
	 *@修改描述	:
	 */
	public boolean isNearExpiry(String key, int exp);
	
	/**
	 * 
	 *@描述		：判断缓存服务是否有效
	 *@创建人	: kangzhidong
	 *@创建时间	: Mar 18, 201610:05:22 AM
	 *@return
	 *@修改人	: 
	 *@修改时间	: 
	 *@修改描述	:
	 */
	public boolean isEffective();
	
	/**
	 * 
	 *@描述		：判断缓存服务是否已经关闭
	 *@创建人	: kangzhidong
	 *@创建时间	: Mar 18, 201610:05:16 AM
	 *@return
	 *@修改人	: 
	 *@修改时间	: 
	 *@修改描述	:
	 */
	public boolean isShutdown(); 
	
	/**
	 * 
	 *@描述		： 获取客户名称
	 *@创建人	: kangzhidong
	 *@创建时间	: Mar 18, 201610:05:03 AM
	 *@return
	 *@修改人	: 
	 *@修改时间	: 
	 *@修改描述	:
	 */
	public String getClientName();
	
	/**
	 * 
	 *@描述		：关闭缓存服务
	 *@创建人	: kangzhidong
	 *@创建时间	: Mar 18, 201610:05:09 AM
	 *@修改人	: 
	 *@修改时间	: 
	 *@修改描述	:
	 */
	public void shutdown();
 
	/**
	 * 
	 *@描述		：清空所有缓存，使cache中所有的数据项失效，如果是分布式缓存，则所有节点数据项都将失效.
	 *@创建人	: kangzhidong
	 *@创建时间	: Mar 18, 201610:13:11 AM
	 *@修改人	: 
	 *@修改时间	: 
	 *@修改描述	:
	 */
	public void flushAll();
	
}
