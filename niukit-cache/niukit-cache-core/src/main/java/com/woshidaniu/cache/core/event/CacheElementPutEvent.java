package com.woshidaniu.cache.core.event;

import org.springframework.enhanced.context.event.EnhancedEvent;
import org.springframework.enhanced.context.event.EventPoint;

/**
 * 
 *@类名称		： CacheElementPutEvent.java
 *@类描述		：缓存入栈事件对象
 *@创建人		：kangzhidong
 *@创建时间	：2017年6月7日 下午4:43:06
 *@修改人		：
 *@修改时间	：
 *@版本号		: v1.0
 */
@SuppressWarnings("serial")
public class CacheElementPutEvent extends EnhancedEvent<EventPoint> {

	public CacheElementPutEvent(Object source, EventPoint bind) {
		super(source, bind);
	}
	
}
