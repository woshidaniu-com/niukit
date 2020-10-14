package org.activiti.engine.extend.log.impl;

import java.io.Serializable;

import org.activiti.engine.delegate.VariableScope;
import org.activiti.engine.extend.log.ProcessLog;

/**
 * <p>
 * <h3>niutal框架
 * <h3><br>
 * 说明：默认流程日志对象，基于流程变量 <br>
 * class：org.activiti.engine.extend.log.ProcessLog.java
 * <p>
 * 
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
public class DefaultProcessLog implements ProcessLog, Serializable {
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getLogMessage() {
		return logMessage;
	}

	public void setLogMessage(String logMessage) {
		this.logMessage = logMessage;
	}

	public String getLogMoreMessage() {
		return logMoreMessage;
	}

	public void setLogMoreMessage(String logMoreMessage) {
		this.logMoreMessage = logMoreMessage;
	}

	private static final long serialVersionUID = 1L;
	protected String type;
	protected String processInstanceId;
	protected String taskId;
	protected String logMessage;
	protected String logMoreMessage;
	protected VariableScope variableScope;

	public DefaultProcessLog(String type, VariableScope variableScope) {
		super();
		this.type = type;
		this.variableScope = variableScope;
	}
	
	@Override
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public VariableScope getVariableScope() {
		return variableScope;
	}

	public void setVariableScope(VariableScope variableScope) {
		this.variableScope = variableScope;

	}

	@Override
	public String getProcessInstanceId() {
		return this.processInstanceId;
	}

	@Override
	public String getTaskId() {
		return this.taskId;
	}

}
