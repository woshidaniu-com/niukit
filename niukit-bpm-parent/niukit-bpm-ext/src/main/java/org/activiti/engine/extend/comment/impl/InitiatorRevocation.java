package org.activiti.engine.extend.comment.impl;

/**
 * 
 * <p>
 *   <h3>niutal框架<h3>
 *   <br>说明：TODO
 *	 <br>class：org.activiti.engine.extend.decision.impl.InitiatorRevocation.java
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
public class InitiatorRevocation extends BACK {

	@Override
	public String getValue() {
		return "BACK_IR";
	}

	@Override
	public String getDisplayName() {
		return "[申请人]撤销";
	}
	
}
