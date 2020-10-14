package com.woshidaniu.cache.core.event;

import org.springframework.enhanced.context.event.EnhancedEvent;

/**
 * 
 *@类名称		： CacheCleanedEvent.java
 *@类描述		：缓存对象清除事件对象
 *@创建人		：kangzhidong
 *@创建时间	：2017年6月7日 下午4:42:39
 *@修改人		：
 *@修改时间	：
 *@版本号		: v1.0
 */
@SuppressWarnings("serial")
public class CacheCleanedEvent extends EnhancedEvent<String> {

	public CacheCleanedEvent(Object source, String bind) {
		super(source, bind);
	}
	
}
