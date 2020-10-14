package com.woshidaniu.rocketmq.disruptor;

import com.lmax.disruptor.EventFactory;
import com.woshidaniu.rocketmq.event.RocketmqDisruptorEvent;

public class RocketmqDataEventFactory implements EventFactory<RocketmqDisruptorEvent> {

	@Override
	public RocketmqDisruptorEvent newInstance() {
		return new RocketmqDisruptorEvent(this);
	}
	
}
