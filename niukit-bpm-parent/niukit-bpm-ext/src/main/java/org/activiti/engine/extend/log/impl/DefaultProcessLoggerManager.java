package org.activiti.engine.extend.log.impl;

import java.util.Iterator;
import java.util.Map;

import org.activiti.engine.extend.comment.CommentMessageMappaer;
import org.activiti.engine.extend.log.ProcessLog;
import org.activiti.engine.extend.log.ProcessLogger;
import org.activiti.engine.extend.log.ProcessLoggerManager;

/**
 * 
 * <p>
 * <h3>niutal框架
 * <h3><br>
 * 说明：工作流日志管理 <br>
 * class：org.activiti.engine.extend.log.impl.DefaultProcessLoggerManager.java
 * <p>
 * 
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
public class DefaultProcessLoggerManager implements ProcessLoggerManager{

	protected Map<String, ProcessLogger> processLoggers;

	protected CommentMessageMappaer messageMapper;

	@Override
	public void log(ProcessLog processLog) {
		if (processLog == null || processLoggers == null || processLoggers.size() == 0) {
			return;
		}
		ProcessLogger processLogger = processLoggers.get(processLog.getType());
		if (processLogger == null)
			return;

		processLogger.log(processLog);
	}

	@Override
	public void init(){
		if(processLoggers != null && processLoggers.size() > 0 && messageMapper != null){
			Iterator<String> iterator = processLoggers.keySet().iterator();
			while(iterator.hasNext()){
				ProcessLogger processLogger = processLoggers.get(iterator.next());
				processLogger.setCommentMessageMapper(messageMapper);
			}
		}
	}

	public Map<String, ProcessLogger> getProcessLoggers() {
		return processLoggers;
	}

	public void setProcessLoggers(Map<String, ProcessLogger> processLoggers) {
		this.processLoggers = processLoggers;
	}

	public CommentMessageMappaer getMessageMapper() {
		return messageMapper;
	}

	public void setMessageMapper(CommentMessageMappaer messageMapper) {
		this.messageMapper = messageMapper;
	}
	
}
