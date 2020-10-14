package com.woshidaniu.disruptor.event.handler;

import com.woshidaniu.disruptor.event.DisruptorEvent;
import com.woshidaniu.disruptor.event.handler.chain.HandlerChain;

/**
 * 实现EventHandler是为了作为BatchEventProcessor的事件处理器，
 * 实现WorkHandler是为了作为WorkerPool的事件处理器
 */
public interface DisruptorHandler<T extends DisruptorEvent> {

	public void doHandler(T event, HandlerChain<T> handlerChain) throws Exception;
	
}
