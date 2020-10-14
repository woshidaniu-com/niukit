package com.woshidaniu.rocketmq.event.handler;

import com.woshidaniu.rocketmq.event.RocketmqEvent;

/**
 * 给Handler设置路径
 */
public interface PathProcessor<T extends RocketmqEvent> {
	
	EventHandler<T> processPath(String path);

}
