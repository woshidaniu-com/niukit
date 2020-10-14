package com.woshidaniu.cache.core.interceptor;

/**
 * 
 *@类名称	: CacheInvocation.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 18, 2016 10:04:49 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 *@param <T>
 */
public abstract class CacheInvocation<T> {

	public int getExpiry(){
		return 0;
	};
	
	public abstract T getOriginal();
	
}

