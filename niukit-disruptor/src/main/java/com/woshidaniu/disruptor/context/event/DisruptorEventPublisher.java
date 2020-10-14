package com.woshidaniu.disruptor.context.event;

import com.woshidaniu.disruptor.event.DisruptorEvent;

public interface DisruptorEventPublisher {

	void publishEvent(DisruptorEvent event);
	
}
