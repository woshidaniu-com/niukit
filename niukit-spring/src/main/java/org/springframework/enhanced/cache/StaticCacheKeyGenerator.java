package org.springframework.enhanced.cache;

import java.lang.reflect.Method;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKey;

public class StaticCacheKeyGenerator implements KeyGenerator {  
    
    @Override  
    public Object generate(Object target, Method method, Object... params) {  
    	Cacheable cacheable = method.getAnnotation(Cacheable.class);
    	if(cacheable != null){
    		return new SimpleKey(cacheable.key());
    	}
    	CachePut cachePut = method.getAnnotation(CachePut.class);
    	if(cachePut != null){
    		return new SimpleKey(cachePut.key());
    	}
    	return SimpleKey.EMPTY;
    }  
}  
