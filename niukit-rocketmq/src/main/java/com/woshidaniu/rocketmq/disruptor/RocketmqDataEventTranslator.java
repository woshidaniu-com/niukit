package com.woshidaniu.rocketmq.disruptor;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.woshidaniu.rocketmq.event.RocketmqDisruptorEvent;

public class RocketmqDataEventTranslator implements EventTranslatorOneArg<RocketmqDisruptorEvent, MessageExt> {
	
	private ConsumeConcurrentlyContext context;
	
	public RocketmqDataEventTranslator(ConsumeConcurrentlyContext context) throws Exception {
		this.context = context;
	}
	
	@Override
	public void translateTo(RocketmqDisruptorEvent event, long sequence, MessageExt msgExt) {
		
		event.setMessageExt(msgExt);
		event.setTopic(msgExt.getTopic());
		event.setTag(msgExt.getTags());
		event.setBody(msgExt.getBody());
		
	}

	public ConsumeConcurrentlyContext getContext() {
		return context;
	}

	public void setContext(ConsumeConcurrentlyContext context) {
		this.context = context;
	}
	
}