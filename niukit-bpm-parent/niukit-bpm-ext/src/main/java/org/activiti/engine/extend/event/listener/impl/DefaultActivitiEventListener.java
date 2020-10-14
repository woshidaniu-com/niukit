package org.activiti.engine.extend.event.listener.impl;

import org.activiti.engine.delegate.event.ActivitiCancelledEvent;
import org.activiti.engine.delegate.event.ActivitiEntityEvent;
import org.activiti.engine.delegate.event.ActivitiEntityWithVariablesEvent;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.engine.delegate.event.ActivitiProcessStartedEvent;
import org.activiti.engine.delegate.event.impl.ActivitiEntityEventImpl;
import org.activiti.engine.extend.context.BusinessContext;
import org.activiti.engine.extend.event.AbstractActivityCustomEvent;
import org.activiti.engine.extend.event.ExtendActivitiEventType;
import org.activiti.engine.extend.event.listener.AbstractEventListener;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * <p>
 *   <h3>niutal框架<h3>
 *   <br>说明：TODO
 *	 <br>class：org.activiti.engine.extend.event.listener.impl.DefaultActivitiEventListener.java
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
public class DefaultActivitiEventListener implements ActivitiEventListener {

	static final Logger log = LoggerFactory.getLogger(DefaultActivitiEventListener.class);

	protected boolean isFailOnException = true;

	protected void logDEBUG_NOT_IMPLEMENTED(ActivitiEventType type) {
		if (log.isDebugEnabled()) {
			log.debug(type + " current version not implemented yet");
		}
	}

	protected void logDEBUG(ActivitiEvent event) {
		if (log.isDebugEnabled()) {
			String processDefinationId = event.getProcessDefinitionId();
			String processInstanceId = event.getProcessInstanceId();
			String executionId = event.getExecutionId();
			logDEBUG(processDefinationId, processInstanceId, executionId, event.getType());
		}
	}

	protected void logDEBUG(String processDefinationId, String processInstanceId, String executionId,
			ActivitiEventType eventType) {
		if (log.isDebugEnabled()) {
			log.debug("流程开始事件,流程定义ID:{},流程实例ID:{},执行实例ID:{},事件类型:{}.",
					new Object[] { processDefinationId, processInstanceId, executionId, eventType });
		}
	}

	protected void logINFO() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.activiti.engine.delegate.event.ActivitiEventListener#onEvent(org.
	 * activiti.engine.delegate.event.ActivitiEvent)
	 */

	public void onEvent(ActivitiEvent event) {
		ActivitiEventType eventType = event.getType();
		logDEBUG(event);
		switch (eventType) {
		case PROCESS_STARTED:
			onProcessStarted((ActivitiProcessStartedEvent) event);
			break;
		case PROCESS_CANCELLED:
			onProcessCancelled((ActivitiCancelledEvent) event);
			break;
		case PROCESS_COMPLETED:
			onProcessCompleted((ActivitiEntityEvent) event);
			break;
		case PROCESS_COMPLETED_WITH_ERROR_END_EVENT:
			onProcessCompletedWithErrorEvent((ActivitiEntityEvent) event);
			break;
		case ENTITY_SUSPENDED:
			if (event instanceof ActivitiEntityEventImpl) {
				Object entity = ((ActivitiEntityEvent) event).getEntity();
				if (entity instanceof ExecutionEntity) {
					onProcessSuspended((ActivitiEntityEvent) event);
				} else if (entity instanceof TaskEntity) {
					onTaskSuspended((ActivitiEntityEvent) event);
				}
			}
			break;
		case ENTITY_ACTIVATED:
			if (event instanceof ActivitiEntityEventImpl) {
				Object entity = ((ActivitiEntityEvent) event).getEntity();
				if (entity instanceof ExecutionEntity) {
					onProcessActivated((ActivitiEntityEvent) event);
				} else if (entity instanceof TaskEntity) {
					onTaskActivated((ActivitiEntityEvent) event);
				}
			}
			break;
		case TASK_CREATED:
			onTaskCreated((ActivitiEntityEvent) event);
			break;
		case TASK_ASSIGNED:
			onTaskAssigned((ActivitiEntityEvent) event);
			break;
		case TASK_COMPLETED:
			onTaskCompleted((ActivitiEntityWithVariablesEvent) event);
			break;
		case TIMER_FIRED:
			onTimerFired((ActivitiEntityEvent) event);
			break;
		case JOB_EXECUTION_SUCCESS:
			onJobExecutionSuccess((ActivitiEntityEvent) event);
			break;
		case JOB_EXECUTION_FAILURE:
			onJobExecutionFailure((ActivitiEntityEvent) event);
			break;
		case JOB_RETRIES_DECREMENTED:
			onJobRetriesDecremented((ActivitiEntityEvent) event);
			break;
		case JOB_CANCELED:
			onJobCanceled((ActivitiEntityEvent) event);
			break;
		case CUSTOM:
			if (event instanceof AbstractActivityCustomEvent) {
				AbstractActivityCustomEvent extendEvent = (AbstractActivityCustomEvent) event;
				ExtendActivitiEventType extendActivitiEvent = extendEvent.getExtendEvent();
				if (extendActivitiEvent == ExtendActivitiEventType.AUDITOR_REVOCATION_EVENT) {
					onAuditorRevocation(event);
				} else if (extendActivitiEvent == ExtendActivitiEventType.INITIATOR_REVOCATION_EVENT) {
					onInitiatorRevocation(event);
				}else if (extendActivitiEvent == ExtendActivitiEventType.AUDITOR_PROCESS_CANCELLATION_EVENT) {
					onAuditorProcessCancellation(event);
				}
			}
			break;
		default:
			break;
		}
	}

	protected boolean isCustomEvent(ActivitiEvent event) {
		return ActivitiEventType.CUSTOM == event.getType();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.activiti.engine.delegate.event.ActivitiEventListener#
	 * isFailOnException()
	 */

	public boolean isFailOnException() {
		return true;
	}

	public void setFailOnException(boolean isFailOnException) {
		this.isFailOnException = isFailOnException;
	}

	public void onJobCanceled(ActivitiEvent event) {
		AbstractEventListener businessCallback = BusinessContext.get();
		if (businessCallback != null) {
			businessCallback.onJobCanceled(event);
		} else
			logDEBUG_NOT_IMPLEMENTED(event.getType());
	}

	public void onJobExecutionSuccess(ActivitiEvent event) {
		AbstractEventListener businessCallback = BusinessContext.get();
		if (businessCallback != null) {
			businessCallback.onJobExecutionSuccess(event);
		} else
			logDEBUG_NOT_IMPLEMENTED(event.getType());
	}

	public void onJobExecutionFailure(ActivitiEvent event) {
		AbstractEventListener businessCallback = BusinessContext.get();
		if (businessCallback != null) {
			businessCallback.onJobExecutionFailure(event);
		} else
			logDEBUG_NOT_IMPLEMENTED(event.getType());
	}

	public void onJobRetriesDecremented(ActivitiEvent event) {
		AbstractEventListener businessCallback = BusinessContext.get();
		if (businessCallback != null) {
			businessCallback.onJobRetriesDecremented(event);
		} else
			logDEBUG_NOT_IMPLEMENTED(event.getType());
	}

	public void onTimerFired(ActivitiEvent event) {
		AbstractEventListener businessCallback = BusinessContext.get();
		if (businessCallback != null) {
			businessCallback.onTimerFired(event);
		} else
			logDEBUG_NOT_IMPLEMENTED(event.getType());
	}

	public void onTaskCreated(ActivitiEvent event) {
		AbstractEventListener businessCallback = BusinessContext.get();
		if (businessCallback != null) {
			businessCallback.onTaskCreated(event);
		} else
			logDEBUG_NOT_IMPLEMENTED(event.getType());
	}

	public void onTaskAssigned(ActivitiEvent event) {
		AbstractEventListener businessCallback = BusinessContext.get();
		if (businessCallback != null) {
			businessCallback.onTaskAssigned(event);
		} else
			logDEBUG_NOT_IMPLEMENTED(event.getType());
	}

	public void onTaskCompleted(ActivitiEvent event) {
		AbstractEventListener businessCallback = BusinessContext.get();
		if (businessCallback != null) {
			businessCallback.onTaskCompleted(event);
		} else
			logDEBUG_NOT_IMPLEMENTED(event.getType());
	}

	public void onTaskSuspended(ActivitiEvent event) {
		AbstractEventListener businessCallback = BusinessContext.get();
		if (businessCallback != null) {
			businessCallback.onTaskSuspended(event);
		} else
			logDEBUG_NOT_IMPLEMENTED(event.getType());
	}

	public void onTaskActivated(ActivitiEvent event) {
		AbstractEventListener businessCallback = BusinessContext.get();
		if (businessCallback != null) {
			businessCallback.onTaskActivated(event);
		} else
			logDEBUG_NOT_IMPLEMENTED(event.getType());
	}

	public void onProcessStarted(ActivitiEvent event) {
		AbstractEventListener businessCallback = BusinessContext.get();
		if (businessCallback != null) {
			businessCallback.onProcessStarted(event);
		} else
			logDEBUG_NOT_IMPLEMENTED(event.getType());
	}

	public void onProcessCompleted(ActivitiEvent event) {
		AbstractEventListener businessCallback = BusinessContext.get();
		if (businessCallback != null) {
			businessCallback.onProcessCompleted(event);
		} else
			logDEBUG_NOT_IMPLEMENTED(event.getType());
	}

	public void onProcessCompletedWithErrorEvent(ActivitiEvent event) {
		AbstractEventListener businessCallback = BusinessContext.get();
		if (businessCallback != null) {
			businessCallback.onProcessCompletedWithErrorEvent(event);
		} else
			logDEBUG_NOT_IMPLEMENTED(event.getType());
	}

	public void onProcessCancelled(ActivitiEvent event) {
		AbstractEventListener businessCallback = BusinessContext.get();
		if (businessCallback != null) {
			businessCallback.onProcessCancelled(event);
		} else
			logDEBUG_NOT_IMPLEMENTED(event.getType());
	}

	public void onProcessSuspended(ActivitiEvent event) {
		AbstractEventListener businessCallback = BusinessContext.get();
		if (businessCallback != null) {
			businessCallback.onProcessSuspended(event);
		} else
			logDEBUG_NOT_IMPLEMENTED(event.getType());
	}

	public void onProcessActivated(ActivitiEvent event) {
		AbstractEventListener businessCallback = BusinessContext.get();
		if (businessCallback != null) {
			businessCallback.onProcessActivated(event);
		} else
			logDEBUG_NOT_IMPLEMENTED(event.getType());

	}

	public void onAuditorRevocation(ActivitiEvent event) {
		AbstractEventListener businessCallback = BusinessContext.get();
		if (businessCallback != null) {
			businessCallback.onAuditorRevocation(event);
		} else
			logDEBUG_NOT_IMPLEMENTED(event.getType());

	}

	public void onInitiatorRevocation(ActivitiEvent event) {
		AbstractEventListener businessCallback = BusinessContext.get();
		if (businessCallback != null) {
			businessCallback.onInitiatorRevocation(event);
		} else
			logDEBUG_NOT_IMPLEMENTED(event.getType());

	}
	
	public void onAuditorProcessCancellation(ActivitiEvent event) {
		AbstractEventListener businessCallback = BusinessContext.get();
		if (businessCallback != null) {
			businessCallback.onAuditorProcessCancellation(event);
		} else
			logDEBUG_NOT_IMPLEMENTED(event.getType());

	}

}
