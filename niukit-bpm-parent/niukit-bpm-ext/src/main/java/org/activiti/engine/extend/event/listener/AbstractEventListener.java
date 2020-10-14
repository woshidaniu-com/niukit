package org.activiti.engine.extend.event.listener;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * <p>
 *   <h3>niutal框架<h3>
 *   <br>说明：TODO
 *	 <br>class：org.activiti.engine.extend.event.listener.AbstractEventListener.java
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
public abstract class AbstractEventListener implements ProcessRelatedEventListener,
		TaskRelatedEventlistener, TimerRelatedEventListener, JobRelatedEventListener, CustomEventListener {

	static final Logger log = LoggerFactory.getLogger(AbstractEventListener.class);
	
	@Override
	public void onAuditorRevocation(ActivitiEvent event) {
		log.info("do nothing!");
		
	}

	@Override
	public void onInitiatorRevocation(ActivitiEvent event) {
		log.info("do nothing!");		
	}
	
	@Override
	public void onAuditorProcessCancellation(ActivitiEvent event) {
		log.info("do nothing!");	
	}
	
	@Override
	public void onJobCanceled(ActivitiEvent event) {
		log.info("do nothing!");		
	}

	@Override
	public void onJobExecutionSuccess(ActivitiEvent event) {
		log.info("do nothing!");		
	}

	@Override
	public void onJobExecutionFailure(ActivitiEvent event) {
		log.info("do nothing!");		
	}

	@Override
	public void onJobRetriesDecremented(ActivitiEvent event) {
		log.info("do nothing!");		
	}

	@Override
	public void onTimerFired(ActivitiEvent event) {
		log.info("do nothing!");		
	}

	@Override
	public void onTaskCreated(ActivitiEvent event) {
		log.info("do nothing!");
	}

	@Override
	public void onTaskAssigned(ActivitiEvent event) {
		log.info("do nothing!");		
	}

	@Override
	public void onTaskCompleted(ActivitiEvent event) {
		log.info("do nothing!");		
	}

	@Override
	public void onTaskSuspended(ActivitiEvent event) {
		log.info("do nothing!");		
	}

	@Override
	public void onTaskActivated(ActivitiEvent event) {
		log.info("do nothing!");		
	}

	@Override
	public void onProcessStarted(ActivitiEvent event) {
		log.info("do nothing!");		
	}

	@Override
	public void onProcessCompleted(ActivitiEvent event) {
		log.info("do nothing!");		
	}

	@Override
	public void onProcessCompletedWithErrorEvent(ActivitiEvent event) {
		log.info("do nothing!");		
	}

	@Override
	public void onProcessCancelled(ActivitiEvent event) {
		log.info("do nothing!");		
	}

	@Override
	public void onProcessSuspended(ActivitiEvent event) {
		log.info("do nothing!");		
	}

	@Override
	public void onProcessActivated(ActivitiEvent event) {
		log.info("do nothing!");		
	}
}
