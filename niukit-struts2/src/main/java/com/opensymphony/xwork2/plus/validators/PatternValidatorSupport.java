package com.opensymphony.xwork2.plus.validators;

import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;
import com.woshidaniu.basicutils.ObjectUtils;
import com.woshidaniu.regexp.utils.PatternMatchUtils;

/**
 * 
 * *******************************************************************
 * @className	： PatternValidatorSupport
 * @description	：基于Java Pattern 的正则校验器
 * @author 		： kangzhidong
 * @date		： Mar 23, 2016 9:49:16 PM
 * @version 	V1.0 
 * *******************************************************************
 */
public class PatternValidatorSupport  extends FieldValidatorSupport{

	/**
	 * 正则表达式
	 */
	protected String regexp;
	
	@Override
	public void validate(Object object) throws ValidationException {
		// 获得字段名
		String fieldName = this.getFieldName();
		// 获得字段值
		String fieldValue = ObjectUtils.toString(this.getFieldValue(fieldName, object));
		//如果值为空则直接不通过
		if (fieldValue == null || fieldValue.length() <= 0) {
			return;
		}
		if (PatternMatchUtils.matches(getRegexp(), fieldValue) == false) {
			this.addFieldError(fieldName, object);
			return;
		}
	}
	
	public String getRegexp() {
		return regexp;
	}

	public void setRegexp(String regexp) {
		this.regexp = regexp;
	}
	

}
