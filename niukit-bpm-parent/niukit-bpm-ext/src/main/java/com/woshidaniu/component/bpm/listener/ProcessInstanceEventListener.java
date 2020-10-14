package com.woshidaniu.component.bpm.listener;

import org.activiti.engine.delegate.DelegateExecution;

/**
 * 
 * <p>
 *   <h3>niutal框架<h3>
 *   <br>说明：流程事件监听接口，业务系统需要实现该接口用于流程状态变化时处理业务数据
 *	 <br>class：com.woshidaniu.component.bpm.listener.ProcessInstanceEventListener.java
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
public interface ProcessInstanceEventListener {

	/**
	 * 
	 * <p>方法说明：流程启动事件<p>
	 * <p>作者：a href="#">Zhangxiaobin[1036]<a><p>
	 * <p>时间：2016年12月8日下午10:15:51<p>
	 */
	void onProcessInstanceStarted(DelegateExecution execution, Object eventObject);
	
	/**
	 * 
	 * <p>方法说明：流程结束事件<p>
	 * <p>作者：a href="#">Zhangxiaobin[1036]<a><p>
	 * <p>时间：2016年12月8日下午10:33:52<p>
	 */
	void onProcessInstanceEnded(DelegateExecution execution, Object eventObject);
	
	/**
	 * 
	 * <p>方法说明：流程取消事件<p>
	 * <p>作者：a href="#">Zhangxiaobin[1036]<a><p>
	 * <p>时间：2016年12月8日下午10:34:16<p>
	 */
	void onProcessInstanceCancelled(DelegateExecution execution, Object eventObject);
}
