package com.opensymphony.oscache.plugins.clustersupport;

/**
 * *******************************************************************
 * @className	： JavaGroupsBroadcastingListenerImpl
 * @description	： TODO(描述这个类的作用)
 * @author 		： <a href="mailto:hnxyhcwdl1003@163.com">kangzhidong</a>
 * @date		： Nov 5, 2016 3:02:00 PM
 * @version 	V1.0 
 * *******************************************************************
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.oscache.CacheConstants;
import com.opensymphony.oscache.base.events.CacheEntryEvent;
import com.opensymphony.oscache.base.events.CacheGroupEvent;
import com.opensymphony.oscache.event.SerialCacheEvent;

/**
 * oscache集群监听器，继承自jgroup的监听器，根据需要重写相应的函数来实现通知和同步更新。
 * 
 * @author slzs Nov 29, 2012 9:14:00 AM each engineer has a duty to keep the code elegant
 */
public class JavaGroupsBroadcastingListenerImpl extends com.opensymphony.oscache.plugins.clustersupport.JavaGroupsBroadcastingListener {
	
	private static final Logger LOG = LoggerFactory.getLogger(JavaGroupsBroadcastingListenerImpl.class);
	
	/**
	 * 处理集群通知，接收来自某个节点发来的通知，并进行相应的同步处理 overriding
	 * 
	 * @see com.opensymphony.oscache.plugins.clustersupport.AbstractBroadcastingListener#handleClusterNotification(com.opensymphony.oscache.plugins.clustersupport.ClusterNotification)
	 * @author: slzs Nov 29, 2012 9:16:28 AM
	 * @param message
	 *            each engineer has a duty to keep the code elegant
	 */
	public void handleClusterNotification(ClusterNotification message) {

		switch (message.getType()) {
			case CacheConstants.CLUSTER_ENTRY_ADD:{
				if(LOG.isTraceEnabled()){
					LOG.trace("集群新增:" + message.getData());
				}
				if (message.getData() instanceof SerialCacheEvent) {
					SerialCacheEvent event = (SerialCacheEvent) message.getData();
					cache.putInCache(event.getKey(), event.getEntry().getContent(), null, null, CLUSTER_ORIGIN);
				}
			};break;
			case CacheConstants.CLUSTER_ENTRY_UPDATE:{
				if(LOG.isTraceEnabled()){
					LOG.trace("集群更新:" + message.getData());
				}
				if (message.getData() instanceof SerialCacheEvent) {
					SerialCacheEvent event = (SerialCacheEvent) message.getData();
					cache.putInCache(event.getKey(), event.getEntry().getContent(), null, null, CLUSTER_ORIGIN);
				}
			};break;
			case CacheConstants.CLUSTER_ENTRY_DELETE:{
				if(LOG.isTraceEnabled()){
					LOG.trace("集群删除:" + message.getData());
				}
				if (message.getData() instanceof SerialCacheEvent) {
					SerialCacheEvent event = (SerialCacheEvent) message.getData();
					cache.removeEntry(event.getKey());
				}
			};break;
		}

	}

	/**
	 * 新增缓存后通知其它子节点 overriding
	 * 
	 * @see com.opensymphony.oscache.plugins.clustersupport.AbstractBroadcastingListener#cacheEntryAdded(com.opensymphony.oscache.base.events.CacheEntryEvent)
	 * @author: slzs Nov 29, 2012 9:17:29 AM
	 * @param event
	 *            each engineer has a duty to keep the code elegant
	 */
	@Override
	public void cacheEntryAdded(CacheEntryEvent event) {
		super.cacheEntryAdded(event);
		if (!CLUSTER_ORIGIN.equals(event.getOrigin())) {
			sendNotification(new ClusterNotification( CacheConstants.CLUSTER_ENTRY_ADD, new SerialCacheEvent( event.getMap(), event.getEntry(), CLUSTER_ORIGIN)));
			//sendNotification(new ClusterNotification(ClusterNotification.CLUSTER_ENTRY_ADD,new CacheEntryEvent(event.getMap(),event.getEntry(),CLUSTER_ORIGIN)));
		}
	}

	/**
	 * 移除缓存后通知其它子节点 overriding
	 * 
	 * @see com.opensymphony.oscache.plugins.clustersupport.AbstractBroadcastingListener#cacheEntryAdded(com.opensymphony.oscache.base.events.CacheEntryEvent)
	 * @author: slzs Nov 29, 2012 9:17:29 AM
	 * @param event
	 *            each engineer has a duty to keep the code elegant
	 */
	@Override
	public void cacheEntryRemoved(CacheEntryEvent event) {

		super.cacheEntryRemoved(event);
		if (!CLUSTER_ORIGIN.equals(event.getOrigin())) {
			sendNotification(new ClusterNotification( CacheConstants.CLUSTER_ENTRY_DELETE, new SerialCacheEvent( event.getMap(), event.getEntry(), CLUSTER_ORIGIN)));
			//sendNotification(new ClusterNotification( ClusterNotification.CLUSTER_ENTRY_DELETE, new CacheEntryEvent(event.getMap(), event.getEntry(), CLUSTER_ORIGIN)));
		}
	}

	/**
	 * 更新缓存后通知其它子节点 overriding
	 * 
	 * @see com.opensymphony.oscache.plugins.clustersupport.AbstractBroadcastingListener#cacheEntryAdded(com.opensymphony.oscache.base.events.CacheEntryEvent)
	 * @author: slzs Nov 29, 2012 9:17:29 AM
	 * @param event
	 *            each engineer has a duty to keep the code elegant
	 */
	@Override
	public void cacheEntryUpdated(CacheEntryEvent event) {
		super.cacheEntryUpdated(event);
		if (!CLUSTER_ORIGIN.equals(event.getOrigin())) {
			sendNotification(new ClusterNotification( CacheConstants.CLUSTER_ENTRY_UPDATE, new SerialCacheEvent( event.getMap(), event.getEntry(), CLUSTER_ORIGIN)));
			//sendNotification(new ClusterNotification( ClusterNotification.CLUSTER_ENTRY_UPDATE, new CacheEntryEvent(event.getMap(), event.getEntry(), CLUSTER_ORIGIN)));
		}
	}
  
    public void cacheGroupAdded(CacheGroupEvent event) {
    }

    public void cacheGroupEntryAdded(CacheGroupEvent event) {
    }

    public void cacheGroupEntryRemoved(CacheGroupEvent event) {
    }

    public void cacheGroupRemoved(CacheGroupEvent event) {
    }

    public void cacheGroupUpdated(CacheGroupEvent event) {
    }
    
}
