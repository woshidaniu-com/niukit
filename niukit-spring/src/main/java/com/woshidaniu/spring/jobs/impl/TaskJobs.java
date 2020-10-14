package com.woshidaniu.spring.jobs.impl;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.woshidaniu.spring.jobs.Task;
import com.woshidaniu.spring.jobs.TimerServer;

public class TaskJobs implements TimerServer{
	

	private ScheduledExecutorService scheduler = null;
	
	public TaskJobs(){
		scheduler = Executors.newScheduledThreadPool(2);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.woshidaniu.common.jobs.TimerTaskManage#registerTask(com.woshidaniu.common.jobs.Task)
	 */
	public ScheduledFuture<?> registerTask(Task task) {
		
		return scheduler.scheduleAtFixedRate(task.getRunnable(), 
									task.getInitialDelay(), 
									task.getPeriod(),
									TimeUnit.MILLISECONDS
								);
	}


	/*
	 * (non-Javadoc)
	 * @see com.woshidaniu.common.jobs.TimerTaskManage#cancleTask(java.util.concurrent.ScheduledFuture)
	 */
	public void cancleTask(final ScheduledFuture<?> future)
			throws Exception {
		
		scheduler.schedule(new Runnable() {
			public void run() {
				future.cancel(true);
			}
		}, -1, TimeUnit.MILLISECONDS);
	}
}
