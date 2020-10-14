package org.activiti.engine.extend.log.impl;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.extend.comment.CommentMessageMappaer;
import org.activiti.engine.extend.log.ProcessLog;
import org.activiti.engine.extend.log.ProcessLogger;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.CommentEntity;
import org.activiti.engine.impl.persistence.entity.CommentEntityManager;

/**
 * <p>
 * <h3>niutal框架
 * <h3><br>
 * 说明： <br>
 * class：org.activiti.engine.extend.log.impl.AbstractActivitiCommentBasedProcessLogger.java
 * <p>
 * 
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
public abstract class AbstractActivitiCommentBasedProcessLogger implements ProcessLogger {
	
	protected CommentMessageMappaer commentMessageMapper;
	
	@Override
	public void log(ProcessLog processLog) {
		if(getCommentEntityManager() == null)
			throw new ActivitiException("CommentEntityManager没有设置");
		
		if(processLog != null && processLog instanceof DefaultProcessLog){
			
			CommentEntity comment = getCommentEntity(processLog);
			
			if(comment != null){
				getCommentEntityManager().insert(comment);
			}
		}
	}

	protected abstract CommentEntity getCommentEntity(ProcessLog processLog);
	
	
	public CommentEntityManager getCommentEntityManager() {
		return Context.getCommandContext().getCommentEntityManager();
	}

	public CommentMessageMappaer getCommentMessageMapper() {
		return commentMessageMapper;
	}

	public void setCommentMessageMapper(CommentMessageMappaer commentMessageMapper) {
		this.commentMessageMapper = commentMessageMapper;
	}
	
}
