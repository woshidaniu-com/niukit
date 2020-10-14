package com.woshidaniu.disruptor.event.handler;

import com.woshidaniu.disruptor.event.DisruptorEvent;

/**
 * 给Handler设置路径
 */
public interface PathProcessor<T extends DisruptorEvent> {
	
	DisruptorHandler<T> processPath(String path);

}
