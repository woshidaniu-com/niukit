package com.opensymphony.xwork2.plus.validators;

import java.util.Date;

import com.opensymphony.xwork2.validator.validators.RangeValidatorSupport;
/**
 * 
 *@类名称	: CheckDate.java
 *@类描述	：日期校验类,可以用于判断输入日期是否在min和max规定的日期范围内,该类重写
 * 				struts2的Date校验规则,增加不配置max值的情况下默认最大日期为当前日期.
 *@创建人	：kangzhidong
 *@创建时间	：Mar 23, 2016 3:10:06 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
@SuppressWarnings("unchecked")
public class CheckDate extends RangeValidatorSupport<Date> {

	private Date max;
    private Date min;
    
	public CheckDate(Class type) {
		super(type); 
		max = new Date(System.currentTimeMillis());
	}
    
	public Date getMax() {
		return max;
	}

	public void setMax(Date max) {
		this.max = max;
	}

	public Date getMin() {
		return min;
	}

	public void setMin(Date min) {
		this.min = min;
	}

	protected Comparable<Date> getMaxComparatorValue() {
		return max;
	}

	protected Comparable<Date> getMinComparatorValue() {
		return min;
	}
	
}
