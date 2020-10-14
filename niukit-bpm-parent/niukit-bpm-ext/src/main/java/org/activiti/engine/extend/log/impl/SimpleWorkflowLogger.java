package org.activiti.engine.extend.log.impl;

import org.activiti.engine.extend.comment.CommentMessage;
import org.activiti.engine.extend.log.ProcessLog;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.CommentEntity;
import org.activiti.engine.task.Event;

/**
 * <p>
 * <h3>niutal框架
 * <h3><br>
 * 说明：TODO <br>
 * class：org.activiti.engine.extend.log.impl.SimpleWorkflowLogger.java
 * <p>
 * 
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
public class SimpleWorkflowLogger extends AbstractActivitiCommentBasedProcessLogger {

	protected static final String LOGGER_NAME = "simpleLogger";

	@Override
	protected CommentEntity getCommentEntity(ProcessLog processLog) {
		if (processLog == null)
			return null;
		Object message = processLog.getLogMessage();
		if (message != null) {
			CommentMessage commentMessage = getCommentMessageMapper().getObject(message.toString());
			if (commentMessage != null) {
				message = commentMessage.getDisplayName();
			}
		}
		Object moreMessage = processLog.getLogMoreMessage();

		String processInstanceId = processLog.getProcessInstanceId(), 
				taskId = processLog.getTaskId(),
				userId = Authentication.getAuthenticatedUserId();
		CommentEntity comment = new CommentEntity();
		comment.setUserId(userId);
		comment.setType(LOG_TYPE_PREFIX);
		comment.setTime(Context.getProcessEngineConfiguration().getClock().getCurrentTime());
		comment.setTaskId(taskId);
		comment.setProcessInstanceId(processInstanceId);
		comment.setAction(Event.ACTION_ADD_COMMENT);
		comment.setMessage(message == null ? null : message.toString());
		comment.setFullMessage(moreMessage == null ? null : moreMessage.toString());
		return comment;
	}

	@Override
	public String getLoggerName() {
		return LOGGER_NAME;
	}
}
