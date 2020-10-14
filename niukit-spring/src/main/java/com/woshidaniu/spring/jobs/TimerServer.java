package com.woshidaniu.spring.jobs;

import java.util.concurrent.ScheduledFuture;

/**
 * 定时任务管理
 * @author Penghui.Qu
 *
 */
public interface TimerServer {

	/**
	 * 注册定时任务
	 * @param task
	 * @throws Exception
	 */
	public ScheduledFuture<?> registerTask(Task task) throws Exception;
	
	
	/**
	 * 取消注册的任务
	 * @param future
	 * @return
	 * @throws Exception
	 */
	public void cancleTask(ScheduledFuture<?> future) throws Exception;
}
