package com.woshidaniu.cache.core.event;

import org.springframework.enhanced.context.event.EnhancedEvent;
import org.springframework.enhanced.context.event.EventPoint;

/**
 * 
 *@类名称		： CacheExceptionEvent.java
 *@类描述		：缓存服务异常事件对象
 *@创建人		：kangzhidong
 *@创建时间	：2017年6月7日 下午4:43:27
 *@修改人		：
 *@修改时间	：
 *@版本号		: v1.0
 */
@SuppressWarnings("serial")
public class CacheExceptionEvent extends EnhancedEvent<EventPoint> {

	public CacheExceptionEvent(Object source, EventPoint bind) {
		super(source, bind);
	}
	
}
