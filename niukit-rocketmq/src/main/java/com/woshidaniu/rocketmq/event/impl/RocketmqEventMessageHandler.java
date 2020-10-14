package com.woshidaniu.rocketmq.event.impl;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.rocketmq.event.RocketmqEvent;
import com.woshidaniu.rocketmq.event.chain.HandlerChain;
import com.woshidaniu.rocketmq.event.chain.HandlerChainResolver;
import com.woshidaniu.rocketmq.event.chain.ProxiedHandlerChain;
import com.woshidaniu.rocketmq.event.handler.AbstractRouteableMessageHandler;
import com.woshidaniu.rocketmq.event.handler.MessageHandler;

public class RocketmqEventMessageHandler extends AbstractRouteableMessageHandler<RocketmqEvent> implements MessageHandler {

	private static final Logger LOG = LoggerFactory.getLogger(RocketmqEventMessageHandler.class);
	
	public RocketmqEventMessageHandler(HandlerChainResolver<RocketmqEvent> filterChainResolver) {
		super(filterChainResolver);
	}
	
	@Override
	public boolean handleMessage(MessageExt msgExt, ConsumeConcurrentlyContext context) throws Exception {
		try {
			//构造原始链对象
			HandlerChain<RocketmqEvent>	originalChain = new ProxiedHandlerChain();
			//执行事件处理链
			this.doHandler(new RocketmqEvent(msgExt), originalChain);
			return true;
		} catch (Exception e) {
			LOG.error(e.getMessage());
			return false;
		}
	}
	
}