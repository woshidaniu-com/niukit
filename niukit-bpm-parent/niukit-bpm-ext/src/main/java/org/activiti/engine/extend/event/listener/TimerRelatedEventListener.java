package org.activiti.engine.extend.event.listener;

import org.activiti.engine.delegate.event.ActivitiEvent;

/**
 * <p>
 *   <h3>niutal框架<h3>
 *   <br>说明：TODO
 *	 <br>class：org.activiti.engine.extend.business.TimerRelatedEventCallback.java
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
public interface TimerRelatedEventListener {

	public void onTimerFired(ActivitiEvent event);
	
}
