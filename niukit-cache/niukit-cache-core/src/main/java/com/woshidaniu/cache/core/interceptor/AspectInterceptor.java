package com.woshidaniu.cache.core.interceptor;

import com.woshidaniu.cache.core.Cache;

/**
 * 
 *@类名称	: AspectInterceptor.java
 *@类描述	：拦截链器接口
 *@创建人	：kangzhidong
 *@创建时间	：Mar 21, 2016 10:17:33 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public interface AspectInterceptor {


    /**
     * 
     *@描述		：调用的顺序
     *@创建人	: kangzhidong
     *@创建时间	: Mar 21, 201610:17:26 AM
     *@return
     *@修改人	: 
     *@修改时间	: 
     *@修改描述	:
     */
    public int getIndex();
    
    public <K,V> Cache<K,V> intercept(AspectInvocation invocation) throws Exception;
    
}
