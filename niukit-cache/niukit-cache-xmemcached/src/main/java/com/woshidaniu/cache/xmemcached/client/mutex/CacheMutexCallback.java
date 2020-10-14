package com.woshidaniu.cache.xmemcached.client.mutex;

/**
 * 
 *@类名称	: CacheMutexCallback.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 18, 2016 11:01:14 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public interface CacheMutexCallback {

	public void doCallBack(String key,int expiry);
	
}
