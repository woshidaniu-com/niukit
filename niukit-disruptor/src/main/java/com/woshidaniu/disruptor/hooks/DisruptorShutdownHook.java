package com.woshidaniu.disruptor.hooks;

import com.lmax.disruptor.dsl.Disruptor;
import com.woshidaniu.disruptor.event.DisruptorEvent;

public class DisruptorShutdownHook extends Thread{
	
	private Disruptor<DisruptorEvent> disruptor;
	
	public DisruptorShutdownHook(Disruptor<DisruptorEvent> disruptor) {
		this.disruptor = disruptor;
	}
	
	@Override
	public void run() {
		disruptor.shutdown();
	}
	
}
