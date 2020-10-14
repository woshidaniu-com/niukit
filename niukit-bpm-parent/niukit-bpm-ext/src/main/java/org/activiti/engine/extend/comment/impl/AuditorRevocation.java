package org.activiti.engine.extend.comment.impl;

/**
 * <p>
 *   <h3>niutal框架<h3>
 *   <br>说明：TODO
 *	 <br>class：org.activiti.engine.extend.decision.impl.AuditorRevocation.java
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
public class AuditorRevocation extends BACK {

	protected String sourceTaskName;
	
	@Override
	public String getValue() {
		return "BACK_AR";
	}

	@Override
	public String getDisplayName() {
		return "撤销退回上一节点";
	}

	public String getSourceTaskName() {
		return sourceTaskName;
	}

	public void setSourceTaskName(String sourceTaskName) {
		this.sourceTaskName = sourceTaskName;
	}
	
}
