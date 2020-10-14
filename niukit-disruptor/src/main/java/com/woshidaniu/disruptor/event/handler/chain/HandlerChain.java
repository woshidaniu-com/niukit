package com.woshidaniu.disruptor.event.handler.chain;

import com.woshidaniu.disruptor.event.DisruptorEvent;

public interface HandlerChain<T extends DisruptorEvent>{

	void doHandler(T event) throws Exception;
	
}
