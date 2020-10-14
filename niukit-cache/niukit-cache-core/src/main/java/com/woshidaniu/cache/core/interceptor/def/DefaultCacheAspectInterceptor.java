package com.woshidaniu.cache.core.interceptor.def;

import com.woshidaniu.basicutils.StringUtils;
import com.woshidaniu.beanutils.reflection.AnnotationUtils;
import com.woshidaniu.cache.core.Cache;
import com.woshidaniu.cache.core.CacheManager;
import com.woshidaniu.cache.core.annotation.CachName;
import com.woshidaniu.cache.core.interceptor.AspectInterceptor;
import com.woshidaniu.cache.core.interceptor.AspectInvocation;

public class DefaultCacheAspectInterceptor implements AspectInterceptor {

	private CacheManager cacheManager;
	
	@Override
	public int getIndex() {
		return 0;
	}

	@Override
	public <K,V> Cache<K,V> intercept(AspectInvocation invocation) throws Exception {
		CachName cachName = AnnotationUtils.findAnnotation(invocation.getMethod(), CachName.class);
		if(cachName != null && !StringUtils.isEmpty(cachName.value())){
			return getCacheManager().getCache(cachName.value());
		}
		return null;
	}

	public CacheManager getCacheManager() {
		return cacheManager;
	}

	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	
	
}
