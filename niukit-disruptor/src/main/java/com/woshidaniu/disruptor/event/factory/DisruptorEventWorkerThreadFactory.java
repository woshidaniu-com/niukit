package com.woshidaniu.disruptor.event.factory;

import java.util.concurrent.ThreadFactory;

public class DisruptorEventWorkerThreadFactory implements ThreadFactory {

	private int counter = 0;
	private String prefix = "";

	public DisruptorEventWorkerThreadFactory(String prefix) {
		this.prefix = prefix;
	}

	public Thread newThread(Runnable r) {
		return new Thread(r, prefix + "-" + counter++);
	}
	
}