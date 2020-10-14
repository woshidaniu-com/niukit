package com.woshidaniu.component.bpm.listener;

import org.activiti.engine.delegate.DelegateTask;

/**
 * 
 * <p>
 *   <h3>niutal框架<h3>
 *   <br>说明：流程任务事件监听接口
 *	 <br>class：com.woshidaniu.component.bpm.listener.TaskEventListener.java
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
public interface TaskEventListener {

	/**
	 * 
	 * <p>方法说明：任务被创建事件<p>
	 * <p>作者：a href="#">Zhangxiaobin[1036]<a><p>
	 * <p>时间：2016年12月8日下午10:45:48<p>
	 */
	void onTaskCreated(DelegateTask task, Object eventObject);
	
	/**
	 * 
	 * <p>方法说明：任务签发事件<p>
	 * <p>作者：a href="#">Zhangxiaobin[1036]<a><p>
	 * <p>时间：2016年12月8日下午10:46:45<p>
	 */
	void onTaskAssigned(DelegateTask task, Object eventObject);
	
	/**
	 * 
	 * <p>方法说明：任务完成事件<p>
	 * <p>作者：a href="#">Zhangxiaobin[1036]<a><p>
	 * <p>时间：2016年12月8日下午10:47:31<p>
	 */
	void onTaskCompleted(DelegateTask task, Object eventObject);
	
}
