package com.woshidaniu.disruptor.event.factory;

import java.util.concurrent.ThreadFactory;

public class DisruptorEventMaxPriorityThreadFactory implements ThreadFactory {
	
	public Thread newThread(Runnable r) {
		Thread t = new Thread(r);
		t.setPriority(Thread.MAX_PRIORITY);
		return t;
	}
	
}
