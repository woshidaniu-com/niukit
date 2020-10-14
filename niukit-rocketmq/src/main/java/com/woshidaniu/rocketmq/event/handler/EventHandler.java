package com.woshidaniu.rocketmq.event.handler;

import com.woshidaniu.rocketmq.event.RocketmqEvent;
import com.woshidaniu.rocketmq.event.chain.HandlerChain;

/**
 */
public interface EventHandler<T extends RocketmqEvent> {

	public void doHandler(T event, HandlerChain<T> handlerChain) throws Exception;
	
}
