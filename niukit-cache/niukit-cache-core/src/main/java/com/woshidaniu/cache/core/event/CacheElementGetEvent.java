package com.woshidaniu.cache.core.event;

import org.springframework.enhanced.context.event.EnhancedEvent;
import org.springframework.enhanced.context.event.EventPoint;

/**
 * 
 *@类名称		： CacheElementGetEvent.java
 *@类描述		： 缓存获取事件对象
 *@创建人		：kangzhidong
 *@创建时间	：2017年6月7日 下午4:43:00
 *@修改人		：
 *@修改时间	：
 *@版本号		: v1.0
 */
@SuppressWarnings("serial")
public class CacheElementGetEvent extends EnhancedEvent<EventPoint> {

	public CacheElementGetEvent(Object source, EventPoint bind) {
		super(source, bind);
	}
	
}
