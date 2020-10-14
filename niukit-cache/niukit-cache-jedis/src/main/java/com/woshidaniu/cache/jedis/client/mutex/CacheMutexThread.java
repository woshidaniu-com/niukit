package com.woshidaniu.cache.jedis.client.mutex;

import com.woshidaniu.cache.jedis.client.IJedisCacheClient;


/**
 *@类名称:CacheMutexHolderThread.java
 *@类描述：
 *@创建人：kangzhidong
 *@创建时间：2014-11-7 下午05:00:56
 *@版本号:v1.0
 */
public class CacheMutexThread extends Thread {

	
	/**数据缓存接口*/
	protected IJedisCacheClient cacheClient;
	protected String key;
	protected int expiry;
	protected CacheMutexCallback callback;
	
	public CacheMutexThread(IJedisCacheClient cacheClient,String key,int expiry,CacheMutexCallback callback){
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
