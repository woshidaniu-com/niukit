package com.woshidaniu.web.servlet.filter.cache;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.woshidaniu.cache.core.CacheManager;
import com.woshidaniu.cache.core.exception.CacheException;
import com.woshidaniu.web.servlet.filter.AbstractPathMatchFilter;

/**
 * <p>
 *   <h3>niutal框架<h3>
 *   <br>说明：缓存支持的过滤器
 *	 <br>class：com.woshidaniu.web.filter.cache.CacheSupportedFilter.java
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
public abstract class CacheSupportedFilter extends AbstractPathMatchFilter implements CacheSupport{

	/**
	 * 是否支持缓存
	 */
	protected boolean cacheEnabled = Boolean.FALSE;
	
	/**
	 * 缓存名称
	 */
	protected String cacheName;
	
	/**
	 * 缓存管理器
	 */
	protected CacheManager cacheManager;
  
    @Override
	public void setCacheManager(CacheManager cm) throws CacheException {
		this.cacheManager = cm;
	}
    
	protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        return true;
    }

   
    protected void postHandle(ServletRequest request, ServletResponse response) throws Exception {
    	
    }

    public void afterCompletion(ServletRequest request, ServletResponse response, Exception exception) throws Exception {
    
    }

    protected String getCacheName() {
		return cacheName;
	}

	public void setCacheName(String cacheName) {
		this.cacheName = cacheName;
	}

    protected CacheManager getCacheManager() {
		return cacheManager;
	}

	public boolean isCacheEnabled() {
		return cacheEnabled;
	}

	public void setCacheEnabled(boolean cacheEnabled) {
		this.cacheEnabled = cacheEnabled;
	}
	
}
