package com.opensymphony.xwork2.plus.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;

/**
 * 
 *@类名称	: CheckSpecialChar.java
 *@类描述	：验证特殊字符,配置时的参数为：
 * 				1，cnValue：是否允许中文
 * 				2，numValue：是否允许数字
 * 				3，letterValue：是否允许字母
 * 				为true时启用。false时禁用。
 *@创建人	：kangzhidong
 *@创建时间	：Mar 23, 2016 3:34:36 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class CheckSpecialChar extends FieldValidatorSupport {
	private String specialChar;//特殊字符
	private String cnValue;//是否允许中文
	private String numValue;//是否允许数字
	private String letterValue;//是否允许字母
	
	public String getSpecialChar() {
		return specialChar;
	}

	public void setSpecialChar(String specialChar) {
		this.specialChar = specialChar;
	}

	public String getCnValue() {
		return cnValue;
	}

	public void setCnValue(String cnValue) {
		this.cnValue = cnValue;
	}

	public String getNumValue() {
		return numValue;
	}

	public void setNumValue(String numValue) {
		this.numValue = numValue;
	}

	public String getLetterValue() {
		return letterValue;
	}

	public void setLetterValue(String letterValue) {
		this.letterValue = letterValue;
	}

	public CheckSpecialChar() {
		
	}

	@Override
	public void validate(Object arg0) throws ValidationException {
		
		boolean isHaveNum = false;
		boolean isHaveCN = false;
		boolean isHaveLetter = false;
		
		//确认状态
		if ("true".equals(numValue)) {
			isHaveNum = true;
		}
		if ("true".equals(cnValue)) {
			isHaveCN = true;
		}
		if ("true".equals(letterValue)) {
			isHaveLetter = true;
		}

		String fieldName = this.getFieldName();
		//得到用户输入的字符串
		String fieldValue = (String) this.getFieldValue(fieldName, arg0);
		
		//定义正则表达式
		StringBuffer tempRegex = new StringBuffer();
		tempRegex.append("^[");
		if (isHaveCN == true) {
			tempRegex.append("|\u4e00-\u9fa5");
		}
		if (isHaveNum == true) {
			tempRegex.append("|0-9");
		}
		if (isHaveLetter == true) {
			tempRegex.append("|a-zA-Z");
		}
		if (specialChar != null) {
			tempRegex.append("|" + specialChar);
		}
		tempRegex.append("]+$");

		// 开始比较
		Pattern pattern = Pattern.compile(tempRegex.toString());
		Matcher matcher = pattern.matcher(fieldValue);
		boolean result = matcher.matches();
		if (!result) {
			this.addFieldError(fieldName, arg0);
		}
	}

}

