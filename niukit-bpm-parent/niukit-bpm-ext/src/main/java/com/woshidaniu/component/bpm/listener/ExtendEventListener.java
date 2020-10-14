package com.woshidaniu.component.bpm.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;

/**
 * 
 * <p>
 *   <h3>niutal框架<h3>
 *   <br>说明：扩展事件监听接口
 *	 <br>class：com.woshidaniu.component.bpm.listener.ExtendEventListener.java
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
public interface ExtendEventListener {

	/**
	 * 
	 * <p>方法说明：当申请人撤消申请<p>
	 * <p>作者：a href="#">Zhangxiaobin[1036]<a><p>
	 * <p>时间：2016年12月8日下午10:15:51<p>
	 */
	void onInitiatorRevocation(DelegateExecution execution, Object eventObject);
	
	/**
	 * 
	 * <p>方法说明：当任务操作人撤消操作时<p>
	 * <p>作者：a href="#">Zhangxiaobin[1036]<a><p>
	 * <p>时间：2016年12月8日下午10:15:51<p>
	 */
	void onAuditorRevocation(DelegateExecution execution, DelegateTask task, Object eventObject);
	
	/**
	 * 
	 * <p>方法说明：当任务操作人取消流程是【退回到申请人】<p>
	 * <p>作者：a href="#">Zhangxiaobin[1036]<a><p>
	 * <p>时间：2016年12月27日下午4:42:25<p>
	 */
	void onAuditorProcessCancellation(DelegateTask task, Object eventObject);
	
}
