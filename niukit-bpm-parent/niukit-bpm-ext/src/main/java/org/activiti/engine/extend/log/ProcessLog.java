package org.activiti.engine.extend.log;

/**
 * <p>
 *   <h3>niutal框架<h3>
 *   <br>说明：流程日志
 *	 <br>class：org.activiti.engine.extend.log.ProcessLog.java
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
public interface ProcessLog {

	/**
	 * 
	 * <p>方法说明：获取日志处理器的名称<p>
	 * <p>作者：a href="#">Zhangxiaobin[1036]<a><p>
	 * <p>时间：2016年12月14日下午1:15:24<p>
	 */
	String getType();
	String getProcessInstanceId();
	String getTaskId();
	String getLogMessage();
	String getLogMoreMessage();
	
}
