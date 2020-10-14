package org.activiti.engine.extend.event.listener;

import org.activiti.engine.delegate.event.ActivitiEvent;

/**
 * <p>
 *   <h3>niutal框架<h3>
 *   <br>说明：TODO
 *	 <br>class：org.activiti.engine.extend.business.TaskRelatedEventCallback.java
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
public interface TaskRelatedEventlistener {

	public void onTaskCreated(ActivitiEvent event);
	
	public void onTaskAssigned(ActivitiEvent event);
	
	public void onTaskCompleted(ActivitiEvent event);
	
	public void onTaskSuspended(ActivitiEvent event);
	
	public void onTaskActivated(ActivitiEvent event);
	
}
