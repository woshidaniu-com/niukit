 package com.opensymphony.xwork2.plus.validators;

import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;
import com.woshidaniu.basicutils.IDCardUtils;

/**
 * 
 * @className: ChinaIDCardValidator
 * @description: 中国身份证号校验
 * @author : kangzhidong
 * @date : 上午11:41:59 2015-4-1
 * @modify by:
 * @modify date :
 * @modify description :
 */
public class ChinaIDCardValidator extends FieldValidatorSupport {

	@Override
	public void validate(Object object) throws ValidationException {
		// 获得字段名
		String fieldName = this.getFieldName();
		// 获得字段值
		String fieldValue = (String) this.getFieldValue(fieldName, object);
		//校验身份证号码
		if(!IDCardUtils.isIDCard(fieldValue)){
			getValidatorContext().addFieldError(fieldName, IDCardUtils.getCodeError());
		}
		
	}

}

