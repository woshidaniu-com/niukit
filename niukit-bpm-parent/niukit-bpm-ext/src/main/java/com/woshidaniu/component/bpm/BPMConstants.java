package com.woshidaniu.component.bpm;

/**
 * <p>
 *   <h3>niutal框架<h3>
 *   <br>说明：流程工作常量,以'_'开始的变量为流程内部变量，使用前请咨询作者
 *	 <br>class：BpmConstants.java
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
public interface BPMConstants {
		
	//标识流程向前还是向后,小于0表示流程是向后走，即：‘退回’操作或则‘撤销’操作
	static final String _INNER_PORCESS_VARIABLE_DIRECTION = "_direction";
	
	//表示申请人
	static final String _INNER_PROCESS_VARIABLE_REQUESTER = "_requester";
	
	//流程默认类别
	static final String _DEFAULT_PROCESS_DEFINATION_CATEGORY = "默认";
	
	static final String _TASK_SKIP_EXPRESSION_VARIABLE = "_ACTIVITI_SKIP_EXPRESSION_ENABLED";
	
	
}
