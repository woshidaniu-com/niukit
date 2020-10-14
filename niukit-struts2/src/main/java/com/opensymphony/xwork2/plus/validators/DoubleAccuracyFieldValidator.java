package com.opensymphony.xwork2.plus.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.DoubleRangeFieldValidator;

/**
 * 
 * --------------------------------------------------------------------------------
 * @className	： DoubleAccuracyFieldValidator
 * @description	： 验证Double值，继承自Struts2的double验证规则。在原有基础上增加验证小数位。
 * @author 		： kangzhidong
 * @date		： Jan 29, 2016 1:53:57 PM
 * @version 	V1.0 
 * --------------------------------------------------------------------------------
 */
public class DoubleAccuracyFieldValidator extends DoubleRangeFieldValidator {
	
	private String minRange;//最小小数位数
	private String maxRange;//最大小数位数
	
	public String getMinRange() {
		return minRange;
	}

	public void setMinRange(String minRange) {
		this.minRange = minRange;
	}

	public String getMaxRange() {
		return maxRange;
	}

	public void setMaxRange(String maxRange) {
		this.maxRange = maxRange;
	}

	@Override
	public void validate(Object object) throws ValidationException {
		String fieldName = this.getFieldName();
		double fieldValue = (Double) this.getFieldValue(fieldName, object);
		String fieldValueStr = Double.toString(fieldValue);
		String regex = "^[0-9]*+(.{1}+[0-9]{"+ minRange + ","+ maxRange + "})?$";
		if (match(regex, fieldValueStr) == false) {
			this.addFieldError(fieldName, object);
			return;
		}
		super.validate(object);
	}
	
	/**
     * @param regex
     * @param str
     * @return
     * author  : bless<505629625@qq.com>
     * about version ：1.00
     * create time   : 2011-2-22-下午04:41:57
     * Description ： 正则表达式验证方法
     *         匹配表达式则返回true
     *         不匹配则返回false
     */
    private static boolean match(String regex, String str) { 
        Pattern pattern = Pattern.compile(regex); 
        Matcher matcher = pattern.matcher(str); 
        return matcher.matches(); 
    }
	
}
