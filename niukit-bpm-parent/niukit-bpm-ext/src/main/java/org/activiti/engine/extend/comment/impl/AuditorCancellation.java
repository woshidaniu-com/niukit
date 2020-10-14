package org.activiti.engine.extend.comment.impl;

import org.activiti.engine.extend.comment.CommentMessage;

/**
 * 
 * <p>
 *   <h3>niutal框架<h3>
 *   <br>说明：
 *	 <br>class：org.activiti.engine.extend.comment.impl.AuditorCancellation.java
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
public class AuditorCancellation implements CommentMessage{

	@Override
	public String getValue() {
		return "BACK_AC";
	}

	@Override
	public String getDisplayName() {
		return "退回到申请人[流程结束]";
	}

}
