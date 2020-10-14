package com.woshidaniu.spring.jobs;

/**
 * 定时任务模型
 * @author Penghui.Qu
 *
 */
public class Task {

	private Runnable runnable;
	private long initialDelay ;
	private long period;

	public void setRunnable(Runnable runnable) {
		this.runnable = runnable;
	}

	public Runnable getRunnable() {
		return runnable;
	}

	public long getInitialDelay() {
		return initialDelay;
	}

	public void setInitialDelay(long initialDelay) {
		this.initialDelay = initialDelay;
	}

	public long getPeriod() {
		return period;
	}

	public void setPeriod(long period) {
		this.period = period;
	}

	
	
}
