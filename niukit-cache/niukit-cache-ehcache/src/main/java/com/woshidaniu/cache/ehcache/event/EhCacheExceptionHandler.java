package com.woshidaniu.cache.ehcache.event;

import org.springframework.enhanced.context.event.EventPoint;
import org.springframework.enhanced.factory.EnhancedBeanFactory;

import com.woshidaniu.basicutils.uid.Sequence;
import com.woshidaniu.cache.core.event.CacheExceptionEvent;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.exceptionhandler.CacheExceptionHandler;

public class EhCacheExceptionHandler extends EnhancedBeanFactory implements CacheExceptionHandler {

	protected Sequence sequence;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		sequence = sequence == null ? new Sequence(): sequence; 
	}
	
	@Override
	public void onException(Ehcache cache, Object key, Exception exception) {
		//当前事件UID
		String uid = String.valueOf(sequence.nextId());
		//事件相关数据
		EventPoint data = new EventPoint( uid, exception.getMessage());
		data.put("cache", cache);
		data.put("key", key);
		data.put("exception", exception);
		//推送事件
		getApplicationContext().publishEvent(new CacheExceptionEvent(this, data));
	}
	
}
