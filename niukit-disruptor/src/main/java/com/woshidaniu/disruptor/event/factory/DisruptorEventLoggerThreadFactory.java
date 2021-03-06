package com.woshidaniu.disruptor.event.factory;

import java.util.concurrent.ThreadFactory;

import org.slf4j.LoggerFactory;

public class DisruptorEventLoggerThreadFactory implements ThreadFactory {

	@Override
	public Thread newThread(Runnable r) {
		Thread t = new Thread(r);
		t.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				LoggerFactory.getLogger(t.getName()).error(e.getMessage(), e);
			}
		});
		return t;
	}
}
