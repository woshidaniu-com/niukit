/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.opensymphony.xwork2.plus.validators;

import org.apache.oro.text.regex.Perl5Compiler;

import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;
import com.woshidaniu.basicutils.ObjectUtils;
import com.woshidaniu.regexp.factory.PatternMatcherFactory;

/**
 * 
 * *******************************************************************
 * @className	： PatternMatcherValidator
 * @description	： 基于fastkit-regexp内置校验规则的正则校验器
 * @author 		： kangzhidong
 * @date		： Mar 23, 2016 9:48:20 PM
 * @version 	V1.0 
 * *******************************************************************
 */
public class PatternMatcherValidator extends FieldValidatorSupport{

	protected PatternMatcherFactory matcherFactory  = PatternMatcherFactory.getInstance();
	//验证规则
	protected String pattern;
	/* 
     * CASE_INSENSITIVE_MASK : 区分大小写 
     * DEFAULT_MASK : 默认(不区分大小写) 
     * EXTENDED_MASK : 支持Perl5 扩展正则表达式 
     * MULTILINE_MASK : 多行匹配，^$匹配每行内容． 
     * SINGLELINE_MASK　：单行匹配  ^$匹配全部内容. 
     * READ_ONLY_MASK : Perl5Pattern 是只读的，提高性能且线程安全． 
     */  
	protected int mask = Perl5Compiler.CASE_INSENSITIVE_MASK;
	
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
		//根据默认配置检测是否匹配成功
		if ( matcherFactory.matches(fieldValue, getPattern()) == false) {
			this.addFieldError(fieldName, object);
			return;
		}
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public int getMask() {
		return mask;
	}

	public void setMask(int mask) {
		this.mask = mask;
	}
	
	
	
}
