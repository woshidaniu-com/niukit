package org.activiti.engine.extend.comment.impl;

import org.activiti.engine.extend.comment.CommentMessage;

/**
 * <p>
 *   <h3>niutal框架<h3>
 *   <br>说明：TODO
 *	 <br>class：org.activiti.engine.extend.decision.impl.PASS.java
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
public class PASS implements CommentMessage {

	/* (non-Javadoc)
	 * @see org.activiti.engine.extend.decision.Decision#getValue()
	 */
	@Override
	public String getValue() {
		return "PASS";
	}

	/* (non-Javadoc)
	 * @see org.activiti.engine.extend.decision.Decision#getDisplayName()
	 */
	@Override
	public String getDisplayName() {
		return "通过";
	}

}
