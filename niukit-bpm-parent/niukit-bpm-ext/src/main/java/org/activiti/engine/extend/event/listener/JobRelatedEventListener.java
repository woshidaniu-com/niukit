package org.activiti.engine.extend.event.listener;

import org.activiti.engine.delegate.event.ActivitiEvent;

/**
 * <p>
 *   <h3>niutal框架<h3>
 *   <br>说明：TODO
 *	 <br>class：org.activiti.engine.extend.business.JobRelatedEventCallback.java
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
public interface JobRelatedEventListener {

	 /**
	   * Timer has been cancelled (e.g. user task on which it was bounded has been completed earlier than expected)
	   
	  JOB_CANCELED,
	  /**
	   * A job has been successfully executed.
	  JOB_EXECUTION_SUCCESS,
	  
	  /**
	   * A job has been executed, but failed. Event should be an instance of a {@link ActivitiExceptionEvent}.
	  JOB_EXECUTION_FAILURE,
	  
	  /**
	   * The retry-count on a job has been decremented.
	   
	  JOB_RETRIES_DECREMENTED
	  */
	
	  public void onJobCanceled(ActivitiEvent event);
	  public void onJobExecutionSuccess(ActivitiEvent event);
	  public void onJobExecutionFailure(ActivitiEvent event);
	  public void onJobRetriesDecremented(ActivitiEvent event);
	
}
