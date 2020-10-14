package org.activiti.engine.extend.service;

import java.util.List;

import org.activiti.engine.extend.assignment.Assignment;

/**
 * <p>
 * <h3>niutal框架
 * <h3><br>
 * 说明：TODO <br>
 * class：org.activiti.engine.extend.service.ExtendService.java
 * <p>
 * 
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
public interface ExtendService {

	/**
	 * 
	 * <p>方法说明：流程任务操作人撤销<p>
	 * <p>作者：a href="#">Zhangxiaobin[1036]<a><p>
	 * <p>时间：2016年12月9日上午11:27:13<p>
	 */
	void auditorRevocation(String processInstanceId, String taskId, String reason);

	/**
	 * 
	 * <p>方法说明：流程任务操作人退回申请人【取消流程，申请人需重新提交流程】<p>
	 * <p>作者：a href="#">Zhangxiaobin[1036]<a><p>
	 * <p>时间：2016年12月27日下午5:01:37<p>
	 */
	void auditorProcessCancellation(String processInstanceId, String taskId, String reason);
	
	/**
	 * 
	 * <p>方法说明：流程发起人撤销<p>
	 * <p>作者：a href="#">Zhangxiaobin[1036]<a><p>
	 * <p>时间：2016年12月9日上午11:27:01<p>
	 */
	void initiatorRevocation(String processInstanceId);
	
	/**
	 * 
	 * <p>方法说明：获取任务人员设置信息<p>
	 * <p>作者：a href="#">Zhangxiaobin[1036]<a><p>
	 * <p>时间：2016年12月23日下午1:35:53<p>
	 */
	List<Assignment> getAssignmentForTask(String processDefinitionId, String taskDefinitionId);
	
	/**
	 * 
	 * <p>方法说明：获取流程所有任务人员设置信息<p>
	 * <p>作者：a href="#">Zhangxiaobin[1036]<a><p>
	 * <p>时间：2016年12月23日下午1:35:53<p>
	 */
	List<Assignment> getAssignmentForProcess(String processDefinitionId);
	
	/**
	 * 
	 * <p>方法说明：设置任务办理人<p>
	 * <p>作者：a href="#">Zhangxiaobin[1036]<a><p>
	 * <p>时间：2016年12月26日上午9:59:07<p>
	 */
	void setupTaskAssignment(String processDefinitionId, List<Assignment> taskAssignment);

}
