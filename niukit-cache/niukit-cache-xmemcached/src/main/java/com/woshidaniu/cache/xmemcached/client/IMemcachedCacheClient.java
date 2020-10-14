package com.woshidaniu.cache.xmemcached.client;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import net.rubyeye.xmemcached.GetsResponse;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;
import net.rubyeye.xmemcached.transcoders.Transcoder;

import com.woshidaniu.cache.core.ICacheClient;

/**
 * 
 *@类名称	: IMemcachedCacheClient.java
 *@类描述	： memcached缓存客户端接口
 *@创建人	：kangzhidong
 *@创建时间	：Mar 18, 2016 10:21:46 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public interface IMemcachedCacheClient extends ICacheClient{

   
    /**
     * 
     *@描述：新增指定key的缓存，当key已经存储过，则存储失败并返回false.
     *@创建人:kangzhidong
     *@创建时间:2014-11-7下午03:47:52
     *@param key	：键（key）
	 *@param value	：该键的值（value），如果没有则插入
     *@return	
     */
	public boolean add(String key, Object value);
	
	/**
	 * 
	 *@描述： 新增指定key的缓存，当key已经存储过，则存储失败并返回false
	 *@创建人:kangzhidong
	 *@创建时间:2014-11-7下午03:42:10
	 *@param key	：键（key）
	 *@param expiry	：过期时间（单位是秒）
	 *@param value	：该键的值（value），如果没有则插入
	 *@return
	 */
	public boolean add(String key,int expiry,Object value);
	
	/**
	 * 
	 * @description	：新增指定key的缓存，无结果响应
	 * @author 		： kangzhidong
	 * @date 		：Dec 17, 2015 7:10:14 PM
	 * @param key
	 * @param value
	 */
	public void addWithNoReply(String key, Object value);
	
	/**
	 * 
	 * @description	： 新增指定key的缓存，无结果响应
	 * @author 		： kangzhidong
	 * @date 		：Dec 17, 2015 7:07:58 PM
	 * @param key
	 * @param expiry
	 * @param value
	 */
	public void addWithNoReply(String key,int expiry, Object value);
	
	/**
	 * 
	 * @description	： 在指定key缓存值之前添加内容;响应成功或失败的标记
	 * @author 		： kangzhidong
	 * @date 		：Dec 17, 2015 7:10:22 PM
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean append(String key, Object value);
	
	/**
	 * 
	 * @description	：  在指定key缓存值之前添加内容;不响应成功或失败的标记
	 * @author 		： kangzhidong
	 * @date 		：Dec 17, 2015 7:11:31 PM
	 * @param key
	 * @param value
	 */
	public void appendWithNoReply(String key, Object value);
	

	/**
	 * 
	 * @description	： 在指定key缓存值之后添加内容;响应成功或失败的标记
	 * @author 		： kangzhidong
	 * @date 		：Dec 17, 2015 7:10:22 PM
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean prepend(String key, Object value);
	
	/**
	 * 
	 * @description	：  在指定key缓存值之后添加内容;不响应成功或失败的标记
	 * @author 		： kangzhidong
	 * @date 		：Dec 17, 2015 7:11:31 PM
	 * @param key
	 * @param value
	 */
	public void prependWithNoReply(String key, Object value);
	
	/**
	 * 
	 * @description	： 替换指定key的缓存对象且不响应结果，如果有则修改。
	 * @author 		： kangzhidong
	 * @date 		：Dec 17, 2015 7:07:21 PM
	 * @param key
	 * @param value
	 */
	public void replaceWithNoReply(String key,Object value);
	
	/**
	 * 
	 * @description	： 替换指定key的缓存对象且不响应结果，如果有则修改。
	 * @author 		： kangzhidong
	 * @date 		：Dec 17, 2015 7:06:54 PM
	 * @param key
	 * @param expiry
	 * @param value
	 */
	public void replaceWithNoReply(String key,int expiry,Object value);
	
	/**
	 * 
	 *@描述：删除指定key的缓存值，不返回删除结果，适合批量删除.
	 *@创建人:kangzhidong
	 *@创建时间:2014-11-7下午03:51:06
	 *@param key ：键（key）
	 */
	public void deleteWithNoReply(String key);
	
	/**
	 * @see #cas(String, int, Object, Transcoder, long, long)
	 * @param <T>
	 * @param key
	 * @param exp
	 * @param value
	 * @param transcoder
	 * @param cas
	 * @return
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public <T> GetsResponse<T> gets(String key);
	
	
	/**
	 * @see #cas(String, int, Object, Transcoder, long, long)
	 * @param key
	 * @param exp
	 * @param value
	 * @param cas
	 * @return
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public boolean cas(String key, int exp, Object value, long cas);
	
	public Map<InetSocketAddress, Map<String, String>> getStats();
	
	public void flushAllWithNoReply();
	
	/**
	 * 
	 * @description	： 获得缓存客户端元素对象
	 * @author 		： kangzhidong
	 * @date 		：Dec 17, 2015 3:45:09 PM
	 * @return
	 */
	public MemcachedClient getMemcachedClient();
	
}
