package com.woshidaniu.rocketmq.event.chain;

import com.woshidaniu.rocketmq.event.RocketmqEvent;

public interface HandlerChainResolver<T extends RocketmqEvent> {

	HandlerChain<T> getChain(T event , HandlerChain<T> originalChain);
	
}
