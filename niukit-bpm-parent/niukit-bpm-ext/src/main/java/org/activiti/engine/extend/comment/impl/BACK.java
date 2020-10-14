package org.activiti.engine.extend.comment.impl;

import org.activiti.engine.extend.comment.CommentMessage;

/**
 * <p>
 *   <h3>niutal框架<h3>
 *   <br>说明：TODO
 *	 <br>class：org.activiti.engine.extend.decision.impl.BACK.java
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
public class BACK implements CommentMessage {

	protected String targetTaskName;
	
	/* (non-Javadoc)
	 * @see org.activiti.engine.extend.decision.Decision#getValue()
	 */
	@Override
	public String getValue() {
		return "BACK";
	}

	/* (non-Javadoc)
	 * @see org.activiti.engine.extend.decision.Decision#getDisplayName()
	 */
	@Override
	public String getDisplayName() {
		if(targetTaskName != null)
			return "退回[" + targetTaskName + "]";
		else
			return "退回";
	}

	public String getTargetTaskName() {
		return targetTaskName;
	}

	public void setTargetTaskName(String targetTaskName) {
		this.targetTaskName = targetTaskName;
	}

}
