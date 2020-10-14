package org.activiti.engine.extend.event.listener;

import org.activiti.engine.delegate.event.ActivitiEvent;

/**
 * <p>
 *   <h3>niutal框架<h3>
 *   <br>说明：TODO
 *	 <br>class：com.woshidaniu.component.bpm.biz.ProcessRelatedEventCallback.java
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
public interface ProcessRelatedEventListener {

	public void onProcessStarted(ActivitiEvent event);
	
	public void onProcessCompleted(ActivitiEvent event);
	
	public void onProcessCompletedWithErrorEvent(ActivitiEvent event);
	
	public void onProcessCancelled(ActivitiEvent event);
	
	public void onProcessSuspended(ActivitiEvent event);
	
	public void onProcessActivated(ActivitiEvent event);
	
	
}
