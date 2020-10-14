package com.woshidaniu.disruptor.event.handler.chain;

import com.woshidaniu.disruptor.event.DisruptorEvent;

public interface HandlerChainResolver<T extends DisruptorEvent> {

	HandlerChain<T> getChain(T event , HandlerChain<T> originalChain);
	
}
