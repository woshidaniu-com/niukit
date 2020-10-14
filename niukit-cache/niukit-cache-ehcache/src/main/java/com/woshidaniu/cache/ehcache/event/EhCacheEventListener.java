package com.woshidaniu.cache.ehcache.event;

import org.springframework.enhanced.context.event.EventPoint;
import org.springframework.enhanced.factory.EnhancedBeanFactory;

import com.woshidaniu.basicutils.uid.Sequence;
import com.woshidaniu.cache.core.event.CacheElementEvictedEvent;
import com.woshidaniu.cache.core.event.CacheElementExpiredEvent;
import com.woshidaniu.cache.core.event.CacheElementPutEvent;
import com.woshidaniu.cache.core.event.CacheElementRemovedEvent;
import com.woshidaniu.cache.core.event.CacheElementUpdatedEvent;
import com.woshidaniu.cache.core.event.CacheRefreshedEvent;
import com.woshidaniu.cache.core.event.CacheStoppedEvent;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.event.CacheEventListener;

public class EhCacheEventListener extends EnhancedBeanFactory implements CacheEventListener {

	protected Sequence sequence;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		sequence = sequence == null ? new Sequence(): sequence; 
	}
	
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public void notifyElementRemoved(Ehcache cache, Element element) throws CacheException {
    	//当前事件UID
		String uid = String.valueOf(sequence.nextId());
		//事件相关数据
		EventPoint data = new EventPoint( uid, "Cache [" + cache.getName() + "] , Element [" + element.getObjectKey() + "] Removed.");
		data.put("cache", cache);
		data.put("element", element);
		//推送事件
    	getApplicationContext().publishEvent(new CacheElementRemovedEvent(this, data));
    }
    
    @Override
    public void notifyElementPut(Ehcache cache, Element element) throws CacheException {
    	//当前事件UID
		String uid = String.valueOf(sequence.nextId());
		//事件相关数据
		EventPoint data = new EventPoint( uid, "Cache [" + cache.getName() + "] , Element [" + element.getObjectKey() + "] Put.");
		data.put("cache", cache);
		data.put("element", element);
		//推送事件
    	getApplicationContext().publishEvent(new CacheElementPutEvent(this, data));
    }

    @Override
    public void notifyElementUpdated(Ehcache cache, Element element) throws CacheException {
    	//当前事件UID
		String uid = String.valueOf(sequence.nextId());
		//事件相关数据
		EventPoint data = new EventPoint( uid, "Cache [" + cache.getName() + "] , Element [" + element.getObjectKey() + "] Updated.");
		data.put("cache", cache);
		data.put("element", element);
		//推送事件
    	getApplicationContext().publishEvent(new CacheElementUpdatedEvent(this, data));
    }

    @Override
    public void notifyElementExpired(Ehcache cache, Element element) {
    	//当前事件UID
		String uid = String.valueOf(sequence.nextId());
		//事件相关数据
		EventPoint data = new EventPoint( uid, "Cache [" + cache.getName() + "] , Element [" + element.getObjectKey() + "] Expired.");
		data.put("cache", cache);
		data.put("element", element);
		//推送事件
    	getApplicationContext().publishEvent(new CacheElementExpiredEvent(this, data));
    }

    @Override
    public void notifyElementEvicted(Ehcache cache, Element element) {
    	//当前事件UID
		String uid = String.valueOf(sequence.nextId());
		//事件相关数据
		EventPoint data = new EventPoint( uid, "Cache [" + cache.getName() + "] , Element [" + element.getObjectKey() + "] Evicted.");
		data.put("cache", cache);
		data.put("element", element);
		//推送事件
    	getApplicationContext().publishEvent(new CacheElementEvictedEvent(this, data));
    }
    
    @Override
    public void notifyRemoveAll(Ehcache cache) {
    	
    	//当前事件UID
		String uid = String.valueOf(sequence.nextId());
		//事件相关数据
		EventPoint data = new EventPoint( uid, "Cache [" + cache.getName() + "] Refreshed.");
		data.put("cache", cache);
		//推送事件
		getApplicationContext().publishEvent(new CacheRefreshedEvent(this, data));
		
    }

    @Override
    public void dispose() {
    	//当前事件UID
		String uid = String.valueOf(sequence.nextId());
		//事件相关数据
		EventPoint data = new EventPoint( uid, "Cache Dispose.");
		//推送事件
		getApplicationContext().publishEvent(new CacheStoppedEvent(this, data));
    }

	public Sequence getSequence() {
		return sequence;
	}

	public void setSequence(Sequence sequence) {
		this.sequence = sequence;
	}
	
}
