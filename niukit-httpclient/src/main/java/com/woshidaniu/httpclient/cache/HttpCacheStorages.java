 package com.woshidaniu.httpclient.cache;

import java.io.IOException;

import org.apache.http.client.cache.HttpCacheEntry;
import org.apache.http.client.cache.HttpCacheStorage;
import org.apache.http.client.cache.HttpCacheUpdateCallback;
import org.apache.http.client.cache.HttpCacheUpdateException;

/**
 * 
 *@类名称	: HttpCacheStorages.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 8, 2016 3:08:49 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class HttpCacheStorages implements HttpCacheStorage{

	@Override
	public HttpCacheEntry getEntry(String key) throws IOException {
		
		return null;
	}

	@Override
	public void putEntry(String key, HttpCacheEntry entry) throws IOException {
		
		
	}

	@Override
	public void removeEntry(String key) throws IOException {
		
		
	}

	@Override
	public void updateEntry(String key, HttpCacheUpdateCallback callback) throws IOException, HttpCacheUpdateException {
		
	}
	
}

 
