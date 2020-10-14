package org.activiti.engine.extend.form;

import org.activiti.engine.form.AbstractFormType;

/**
 * <p>
 *   <h3>niutal框架<h3>
 *   <br>说明：长字符串表单类型
 *	 <br>class：org.activiti.engine.extend.form.TextareaFormType.java
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
public class TextareaFormType extends AbstractFormType {

	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see org.activiti.engine.form.FormType#getName()
	 */
	@Override
	public String getName() {
		return "textarea";
	}

	/* (non-Javadoc)
	 * @see org.activiti.engine.form.AbstractFormType#convertFormValueToModelValue(java.lang.String)
	 */
	@Override
	public Object convertFormValueToModelValue(String propertyValue) {
		if(propertyValue != null && propertyValue.trim().length() > 0){
			return propertyValue.replaceAll("\\s+", " ");
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.activiti.engine.form.AbstractFormType#convertModelValueToFormValue(java.lang.Object)
	 */
	@Override
	public String convertModelValueToFormValue(Object modelValue) {
		if(modelValue != null)
			return (String)modelValue;
		return null;
	}

}
