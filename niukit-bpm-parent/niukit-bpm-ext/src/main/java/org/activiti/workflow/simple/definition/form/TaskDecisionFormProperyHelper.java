package org.activiti.workflow.simple.definition.form;

/**
 * <p>
 * <h3>niutal框架
 * <h3><br>
 * 说明：TODO <br>
 * class：org.activiti.workflow.simple.definition.form.TaskDecisionFormProperyHelper.java
 * <p>
 * 
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
public class TaskDecisionFormProperyHelper {

	public static String DEFAULT_DECISION_PROPERTY_NAME = "p_decision";
	public static String DEFAULT_DECISION_MESSAGE_PROPERTY_NAME = "p_decision_message";
	public static String DEFAULT_DECISION_PROPERTY_DISPLAY_NAME = "操作";
	public static String DEFAULT_DECISION_MESSAGE_PROPERTY_DISPLAY_NAME = "意见";
	public static String DEFAULT_DECISION_PROPERTY_ENTITY_BACK_DISPLAY_NAME = "退回";
	public static String DEFAULT_DECISION_PROPERTY_ENTITY_PASS_NAME = "通过";
	public static String DEFAULT_DECISION_PROPERTY_ENTITY_NOPASS_NAME = "不通过";
	
	public static FormPropertyDefinition createDefaultDecisionMessagePropertyDefinition() {
		FormPropertyDefinition p = new TextPropertyDefinition();
		p.setDisplayName(DEFAULT_DECISION_MESSAGE_PROPERTY_DISPLAY_NAME);
		p.setName(DEFAULT_DECISION_MESSAGE_PROPERTY_NAME);
		p.setMandatory(false);
		p.setType("textarea");
		p.setWritable(true);
		return p;
	}
	
	public static ListPropertyDefinition createDefaultDecisionPropertyDefinition() {
		ListPropertyDefinition p = new ListPropertyDefinition();
		p.setName(DEFAULT_DECISION_PROPERTY_NAME);
		p.setDisplayName(DEFAULT_DECISION_PROPERTY_DISPLAY_NAME);
		p.setType("enum");
		p.setWritable(true);
		p.setMandatory(true);
		ListPropertyEntry PASS_ENTITY = new ListPropertyEntry("PASS", DEFAULT_DECISION_PROPERTY_ENTITY_PASS_NAME);
		p.addEntry(PASS_ENTITY);
 
		return p;
	}

	public static ListPropertyEntry createBackListPropertyEntry(String value, String displayName) {
		return new ListPropertyEntry(value,
				DEFAULT_DECISION_PROPERTY_ENTITY_BACK_DISPLAY_NAME + "[" + displayName + "]");
	}

	public static ListPropertyEntry createNoPassListPropertyEntry() {
		return new ListPropertyEntry("NOPASS", DEFAULT_DECISION_PROPERTY_ENTITY_NOPASS_NAME);
	}

}
