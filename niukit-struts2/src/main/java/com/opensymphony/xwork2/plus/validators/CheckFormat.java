package com.opensymphony.xwork2.plus.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;
/**
 * 
 *@类名称	: CheckFormat.java
 *@类描述	：自定义格式验证格式。
 * 				默认验证规则：
 * 				1，邮件： "[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+"  x@x.x则可通过
 * 				2，手机："^[1]+\\d{10}" 1+10位数字可通过
 * 				3，电话号码："(\\d{3}-)?\\d{8}|(\\d{4}-)?(\\d{7})|(\\d{4}-)?(\\d{8})" 区号-电话可通过
 * 				4，身份证号码："(^\\d{18}$)|(^\\d{15}$)" 15位或18位身份证号码可通过
 *@创建人	：kangzhidong
 *@创建时间	：Mar 23, 2016 3:10:17 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */

public class CheckFormat extends FieldValidatorSupport {
	private String type; //验证类别， 包含"email"、"cellphone"、"phone"、"idcard"
	private String pattern; //验证规则
	private static final String EMAIL =  "[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+";
	private static final String CELLPHONE = "^[1]+\\d{10}";
	private static final String PHONE = "(\\d{3}-)?\\d{8}|(\\d{4}-)?(\\d{7})|(\\d{4}-)?(\\d{8})";
	private static final String IDCARD = "(^\\d{18}$)|(^\\d{15}$)|(^\\d{17}+[xX]{1}$)";
	
	public String getTypes() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public CheckFormat() {
		
	}
		
	@Override
	public void validate(Object arg0) throws ValidationException {
		//确认用户验证类别
		if (type != null) {
			if ("email".equals(type.toLowerCase())) {
				this.pattern = EMAIL;
			}
			if ("cellphone".equals(type.toLowerCase())) {
				this.pattern = CELLPHONE;
			}
			if ("phone".equals(type.toLowerCase())) {
				this.pattern = PHONE;
			}
			if ("idcard".equals(type.toLowerCase())) {
				this.pattern = IDCARD;
			}
		} else {
			return;
		}
		
		String fieldName = this.getFieldName();
		//得到用户输入字符串
		String fieldValue = (String) this.getFieldValue(fieldName, arg0);
		
		Pattern pattern = Pattern.compile(this.pattern);
		Matcher matcher = pattern.matcher(fieldValue);
		
		//验证邮箱地址
		if (!matcher.matches()) {
			addFieldError(fieldName, arg0);
		}
	}
}

