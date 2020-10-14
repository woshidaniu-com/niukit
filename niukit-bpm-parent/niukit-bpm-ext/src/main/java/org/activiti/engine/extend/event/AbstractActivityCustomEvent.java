package org.activiti.engine.extend.event;

import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.engine.delegate.event.impl.ActivitiEntityEventImpl;

/**
 * <p>
 * <h3>niutal框架
 * <h3><br>
 * 说明： <br>
 * class：org.activiti.engine.extend.event.ExtendActivityEvent.java
 * <p>
 * 
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
public abstract class AbstractActivityCustomEvent extends ActivitiEntityEventImpl implements ActivitiCustomEvent {

	public ExtendActivitiEventType getExtendEvent() {
		return extendEvent;
	}

	protected ExtendActivitiEventType extendEvent;

	public AbstractActivityCustomEvent(Object entity, ActivitiEventType type, ExtendActivitiEventType extendEvent) {
		super(entity, type);
		this.extendEvent = extendEvent;
	}

	public void setExtendEvent(ExtendActivitiEventType extendEvent) {
		this.extendEvent = extendEvent;
	}

}
