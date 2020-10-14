package com.woshidaniu.cache.jedis.client.mutex;

/**
 *@类名称:CacheMutexCallback.java
 *@类描述：
 *@创建人：kangzhidong
 *@创建时间：2014-11-7 下午05:09:18
 *@版本号:v1.0
 */
public interface CacheMutexCallback {

	public void doCallBack(String key,int expiry);
	
}
