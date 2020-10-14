package com.woshidaniu.cache.xmemcached.client.mutex;

import com.woshidaniu.cache.xmemcached.client.IMemcachedCacheClient;

/**
 * 
 *@类名称	: CacheMutexThread.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 18, 2016 11:01:22 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class CacheMutexThread extends Thread {

	
	/**数据缓存接口*/
	protected IMemcachedCacheClient cacheClient;
	protected String key;
	protected int expiry;
	protected CacheMutexCallback callback;
	
	public CacheMutexThread(IMemcachedCacheClient cacheClient,String key,int expiry,CacheMutexCallback callback){
		this.cacheClient = cacheClient;
		this.key = key;
		this.expiry = expiry;
		this.callback = callback;
	}
	
	@Override
	public void run() {
		if( cacheClient.isNearExpiry(key, expiry)){
			callback.doCallBack(key, expiry);
		}
	}
	
}
