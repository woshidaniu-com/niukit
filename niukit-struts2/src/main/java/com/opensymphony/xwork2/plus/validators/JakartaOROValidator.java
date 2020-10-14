/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.opensymphony.xwork2.plus.validators;

import org.apache.oro.text.regex.Perl5Compiler;

import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;
import com.woshidaniu.basicutils.ObjectUtils;
import com.woshidaniu.regexp.utils.JakartaOROUtils;

/**
 * 
 * *******************************************************************
 * @className	： JakartaOROValidator
 * @description	： 基于Jakarta-ORO 的正则校验器
 * @author 		： kangzhidong
 * @date		： Mar 23, 2016 9:50:25 PM
 * @version 	V1.0 
 * *******************************************************************
 */
public class JakartaOROValidator extends FieldValidatorSupport{

	/**
	 * 正则表达式
	 */
	protected String regexp;
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
		if ( JakartaOROUtils.matches(getRegexp(), getMask() ,fieldValue) == false) {
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
	
	public int getMask() {
		return mask;
	}

	public void setMask(int mask) {
		this.mask = mask;
	}
	
}
