package com.woshidaniu.disruptor.event.factory;

import java.util.concurrent.ThreadFactory;

public class DisruptorEventDaemonThreadFactory implements ThreadFactory {
	
	public Thread newThread(Runnable r) {
		Thread t = new Thread(r);
		t.setDaemon(true);
		return t;
	}
	
}