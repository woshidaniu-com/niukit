package org.activiti.engine.extend.event.impl;

import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.engine.extend.event.AbstractActivityCustomEvent;
import org.activiti.engine.extend.event.ExtendActivitiEventType;

/**
 * <p>
 *   <h3>niutal框架<h3>
 *   <br>说明：流程任务环节撤销流程事件
 *	 <br>class：org.activiti.engine.extend.event.impl.ExtendAuditorProcessCancellationEvent.java
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
public class ExtendAuditorProcessCancellationEvent extends AbstractActivityCustomEvent {
	
	public ExtendAuditorProcessCancellationEvent(Object entity) {
		super(entity, ActivitiEventType.CUSTOM, ExtendActivitiEventType.AUDITOR_PROCESS_CANCELLATION_EVENT);
	}

}
