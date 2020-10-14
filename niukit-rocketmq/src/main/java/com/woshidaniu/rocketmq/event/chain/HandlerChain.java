package com.woshidaniu.rocketmq.event.chain;

import com.woshidaniu.rocketmq.event.RocketmqEvent;

public interface HandlerChain<T extends RocketmqEvent>{

	void doHandler(T event) throws Exception;
	
}
