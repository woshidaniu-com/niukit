package com.woshidaniu.component.bpm.service;

import java.util.List;
import java.util.Map;

import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.woshidaniu.component.bpm.BPMEventListener;
import com.woshidaniu.component.bpm.BPMException;

/**
 * <p>
 * <h3>niutal框架
 * <h3><br>
 * 说明：流程服务 <br>
 * class：com.woshidaniu.component.bpm.BPMService.java
 * <p>
 * 
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
public interface BPMService {

	/**
	 * 
	 * <p>方法说明：设置表单处理引擎名称<p>
	 * <p>作者：a href="#">Zhangxiaobin[1036]<a><p>
	 * <p>时间：2016年12月10日下午3:48:21<p>
	 */
	void setFormEngineName(String formEngineName);
	/**
	 * 
	 * <p>方法说明：获取环节表单，返回JSON格式<p>
	 * <p>作者：a href="#">Zhangxiaobin[1036]<a><p>
	 * <p>时间：2016年12月10日下午3:39:11<p>
	 */
	ObjectNode getTaskFormJSONObject(String taskId);
	
	/**
	 * 
	 * <p>方法说明：查询流程定义<p>
	 * <p>作者：a href="#">Zhangxiaobin[1036]<a><p>
	 * <p>时间：2016年12月9日下午1:50:21<p>
	 */
	List<ProcessDefinition> queryProcessDefinitons();
	
	/**
	 * 
	 * <p>方法说明：查询流程定义，并根据类别分组<p>
	 * <p>作者：a href="#">Zhangxiaobin[1036]<a><p>
	 * <p>时间：2016年12月9日下午1:50:44<p>
	 */
	Map<String, List<ProcessDefinition>> queryProcessDefinationGroups();
	/**
	 * 
	 * <p>
	 * 方法说明：根据流程标识启动流程
	 * <p>
	 * <p>
	 * 参数说明：
	 * <ul>
	 * <li>processDefinationKey: 流程KEY</li>
	 * <li>processStarter: 流程启动人</li>
	 * <li>bizId: 业务数据ID</li>
	 * <li>processVariables: 流程变量，可为空</li>
	 * <li>eventListener: 流程事件监听器，需要业务系统实现com.woshidaniu.component.bpm.BPMEventListener抽象类</li>
	 * </ul>
	 * <p>
	 * <p>
	 * 返回值：流程实例ID
	 * <p>
	 * <p>
	 * 作者：a href="#">Zhangxiaobin[1036]<a>
	 * <p>
	 * <p>
	 * 时间：2016年11月11日上午9:26:45
	 * <p>
	 */
	ProcessInstance startProcessByKey(String processDefinationKey, String userId, String bizId,
			Map<String, Object> processVariables, BPMEventListener eventListener) throws BPMException;

	/**
	 * 
	 * <p>
	 * 方法说明：根据流程KEY启动流程
	 * <p>
	 * <p>
	 * 返回值：流程实例ID
	 * <p>
	 * <p>
	 * 参数说明：
	 * <ul>
	 * <li>processDefinationKey: 流程KEY</li>
	 * <li>processStarter: 流程启动人</li>
	 * <li>bizId: 业务数据ID</li>
	 * <li>processVariables: 流程变量，可为空</li>
	 * <li>eventListener: 流程事件监听器，需要业务系统实现com.woshidaniu.component.bpm.BPMEventListener抽象类</li>
	 * </ul>
	 * <p>
	 * <p>
	 * 返回值：流程实例ID
	 * <p>
	 * <p>
	 * 作者：a href="#">Zhangxiaobin[1036]<a>
	 * <p>
	 * <p>
	 * 时间：2016年11月11日下午3:24:00
	 * <p>
	 */
	ProcessInstance startProcessById(String processDefinationId, String userId, String bizId,
			Map<String, Object> processVariables, BPMEventListener eventListener) throws BPMException;

	/**
	 * 
	 * <p>
	 * 方法说明：认领审批任务
	 * <p>
	 * <ul>
	 * <li>taskId: 审批任务ID</li>
	 * <li>userId: 认领人用户ID</li>
	 * </ul>
	 * <p>
	 * <p>
	 * 作者：a href="#">Zhangxiaobin[1036]<a>
	 * <p>
	 * <p>
	 * 时间：2016年11月11日下午3:46:44
	 * <p>
	 */
	void claimTask(String taskId, String userId) throws BPMException;

	/**
	 * 
	 * <p>
	 * 方法说明：完成审批任务
	 * <p>
	 * <p>
	 * 参数说明：
	 * <ul>
	 * <li>taskId: 审批任务ID</li>
	 * <li>taskVaribles: 任务变量，可为空</li>
	 * <li>eventListener: 流程事件监听器，需要业务系统实现com.woshidaniu.component.bpm.BPMEventListener抽象类</li>
	 * </ul>
	 * <p>
	 * <p>
	 * 作者：a href="#">Zhangxiaobin[1036]<a>
	 * <p>
	 * <p>
	 * 时间：2016年11月11日下午3:41:03
	 * <p>
	 */
	void completeTask(String taskId, String userId, Map<String, Object> taskVaribles,
			BPMEventListener taskEventListener) throws BPMException;

	/**
	 * 
	 * <p>
	 * 方法说明：完成审批任务,带上任务表单数据
	 * <p>
	 * <p>
	 * 参数说明：
	 * <ul>
	 * <li>taskId: 审批任务ID</li>
	 * <li>taskVaribles: 任务变量，可为空</li>
	 * <li>eventListener: 流程事件监听器，需要业务系统实现com.woshidaniu.component.bpm.BPMEventListener抽象类</li>
	 * </ul>
	 * <p>
	 * <p>
	 * 作者：a href="#">Zhangxiaobin[1036]<a>
	 * <p>
	 * <p>
	 * 时间：2016年11月11日下午3:41:03
	 * <p>
	 */
	void submitTaskFormData(String taskId, String userId, Map<String, String> formProperties, BPMEventListener listener)
			throws BPMException;

	/**
	 * 
	 * <p>
	 * 方法说明：
	 * <p>
	 * <p>
	 * 参数说明：审批人撤销用
	 * <p>
	 * 需要判断下一个审核环节有没有完成，如果没有可以撤销，否则不允许
	 * <ul>
	 * <li>processInstanceId: 流程实例ID</li>
	 * <li>taskId: 撤回的任務ID</li>
	 * <li>reason: 撤销原因</li>
	 * <li>userId: 撤销人Id</li>
	 * <li>eventListener: 流程事件监听器，需要业务系统实现com.woshidaniu.component.bpm.BPMEventListener抽象类</li>
	 * </ul>
	 * <p>
	 * <p>
	 * 作者：a href="#">Zhangxiaobin[1036]<a>
	 * <p>
	 * <p>
	 * 时间：2016年11月11日下午3:59:08
	 * <p>
	 */
	void auditorRevocation(String processInstanceId, String taskId, String reason, String userId, BPMEventListener eventListener)
			throws BPMException;
	
	/**
	 * 
	 * <p>
	 * 方法说明：
	 * <p>
	 * <p>
	 * 参数说明：审批人撤销用
	 * <p>
	 * 需要判断下一个审核环节有没有完成，如果没有可以撤销，否则不允许
	 * <ul>
	 * <li>processInstanceId: 流程实例ID</li>
	 * <li>reason: 取消原因</li>
	 * <li>userId: 取消人Id</li>
	 * <li>eventListener: 流程事件监听器，需要业务系统实现com.woshidaniu.component.bpm.BPMEventListener抽象类</li>
	 * </ul>
	 * <p>
	 * <p>
	 * 作者：a href="#">Zhangxiaobin[1036]<a>
	 * <p>
	 * <p>
	 * 时间：2016年11月11日下午3:59:08
	 * <p>
	 */
	void auditorProcessCancellation(String processInstanceId, String taskId, String reason, String userId, BPMEventListener eventListener)
			throws BPMException;
	
	/**
	 * 
	 * <p>
	 * 方法说明：根据流程ID撤销，给申请人使用，
	 * <p>
	 * 需要判断是否有完成的审核任务，如果有不允许撤销，
	 * <p>
	 * 否同撤销删除流程实例
	 * <p>
	 * <ul>
	 * <li>processInstanceId: 流程实例ID</li>
	 * <li>userId: 撤销人ID</li>
	 * <li>eventListener: 流程事件监听器，需要业务系统实现com.woshidaniu.component.bpm.BPMEventListener抽象类</li>
	 * </ul>
	 * <p>
	 * 作者：a href="#">Zhangxiaobin[1036]<a>
	 * <p>
	 * <p>
	 * 时间：2016年11月18日下午5:06:21
	 * <p>
	 */
	void initiatorRevocation(String processInstanceId, String userId, BPMEventListener eventListener)
			throws BPMException;

}
