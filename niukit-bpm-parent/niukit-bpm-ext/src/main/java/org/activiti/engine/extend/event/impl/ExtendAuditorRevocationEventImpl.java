package org.activiti.engine.extend.event.impl;

import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.engine.extend.event.AbstractActivityCustomEvent;
import org.activiti.engine.extend.event.ExtendActivitiEventType;

/**
 * 
 * <p>
 * <h3>niutal框架
 * <h3><br>
 * 说明：流程任务操作人发起撤销事件 <br>
 * class：org.activiti.engine.extend.event.impl.ExtendAuditorRevocationEventImpl.java
 * <p>
 * 
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
public class ExtendAuditorRevocationEventImpl extends AbstractActivityCustomEvent {

	protected Object taskEntity;
	
	protected Object hisTaskEntity;
	
	public ExtendAuditorRevocationEventImpl(Object entity) {
		super(entity, ActivitiEventType.CUSTOM, ExtendActivitiEventType.AUDITOR_REVOCATION_EVENT);
	}

	public Object getTaskEntity() {
		return taskEntity;
	}

	public void setTaskEntity(Object taskEntity) {
		this.taskEntity = taskEntity;
	}

	public Object getHisTaskEntity() {
		return hisTaskEntity;
	}

	public void setHisTaskEntity(Object hisTaskEntity) {
		this.hisTaskEntity = hisTaskEntity;
	}
}
