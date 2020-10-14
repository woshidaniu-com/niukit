package com.woshidaniu.cache.ehcache.event;

import org.springframework.enhanced.context.event.EventPoint;
import org.springframework.enhanced.factory.EnhancedBeanFactory;

import com.woshidaniu.basicutils.uid.Sequence;
import com.woshidaniu.cache.core.event.CacheCreatedEvent;
import com.woshidaniu.cache.core.event.CacheElementRemovedEvent;
import com.woshidaniu.cache.core.event.CacheStartedEvent;
import com.woshidaniu.cache.core.event.CacheStoppedEvent;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.Status;
import net.sf.ehcache.event.CacheManagerEventListener;

public class EhCacheManagerEventListener extends EnhancedBeanFactory implements CacheManagerEventListener {

	protected Sequence sequence;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		sequence = sequence == null ? new Sequence(): sequence; 
	}
	
	@Override
	public void init() throws CacheException {
		//当前事件UID
		String uid = String.valueOf(sequence.nextId());
		//事件相关数据
		EventPoint data = new EventPoint( uid, "CacheManager Init.");
		//推送事件
		getApplicationContext().publishEvent(new CacheStartedEvent(this, data));
	}

	@Override
	public Status getStatus() {
		return Status.STATUS_ALIVE;
	}

	@Override
	public void dispose() throws CacheException {
		//当前事件UID
		String uid = String.valueOf(sequence.nextId());
		//事件相关数据
		EventPoint data = new EventPoint( uid, "CacheManager Dispose.");
		//推送事件
		getApplicationContext().publishEvent(new CacheStoppedEvent(this, data));
	}

	@Override
	public void notifyCacheAdded(String cacheName) {
		//当前事件UID
		String uid = String.valueOf(sequence.nextId());
		//事件相关数据
		EventPoint data = new EventPoint( uid, "Cache [" + cacheName + "] Added.");
		data.put("cacheName", cacheName);
		//推送事件
		getApplicationContext().publishEvent(new CacheCreatedEvent(this, data));
	}

	@Override
	public void notifyCacheRemoved(String cacheName) {
		//当前事件UID
		String uid = String.valueOf(sequence.nextId());
		//事件相关数据
		EventPoint data = new EventPoint( uid, "Cache [" + cacheName + "] Removed.");
		data.put("cacheName", cacheName);
		//推送事件
		getApplicationContext().publishEvent(new CacheElementRemovedEvent(this, data));
	}

}
