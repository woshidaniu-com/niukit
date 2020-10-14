/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.disruptor.event.handler;

import com.lmax.disruptor.EventHandler;
import com.woshidaniu.disruptor.event.DisruptorEvent;
import com.woshidaniu.disruptor.event.handler.chain.HandlerChain;
import com.woshidaniu.disruptor.event.handler.chain.HandlerChainResolver;
import com.woshidaniu.disruptor.event.handler.chain.ProxiedHandlerChain;

/**
 * Disruptor 事件分发实现
 */
public class DisruptorEventHandler extends AbstractRouteableEventHandler<DisruptorEvent> implements EventHandler<DisruptorEvent>{
	

	public DisruptorEventHandler(HandlerChainResolver<DisruptorEvent> filterChainResolver) {
		super(filterChainResolver);
	}
	
	/*
	 * 责任链入口
	 */
	@Override
	public void onEvent(DisruptorEvent event, long sequence, boolean endOfBatch) throws Exception {
		//构造原始链对象
		HandlerChain<DisruptorEvent> originalChain = new ProxiedHandlerChain();
		//执行事件处理链
		this.doHandler(event, originalChain);
	}

}

