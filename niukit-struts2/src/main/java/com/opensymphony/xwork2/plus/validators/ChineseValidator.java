package com.opensymphony.xwork2.plus.validators;

import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;

/**
 * @title: ChineseValidator.java
 * @package com.validation.validator.validators
 * @description: TODO(添加描述)
 * @author: kangzhidong
 * @date : 下午01:16:50 2015-3-24
 */
public class ChineseValidator extends FieldValidatorSupport {
	/**
	 * 表示是否包含有中文，有3中模式 none:没有中文 some:含有中文字符,取默认值 all:全是中文字符
	 */
	private String mode = "some";

	public void setMode(String mode) {
		this.mode = mode;
	}

	@Override
	public void validate(Object object) throws ValidationException {

		// 获得字段名
		String fieldName = this.getFieldName();
		// 获得字段值
		String fieldValue = (String) this.getFieldValue(fieldName, object);
		// 获得字节数
		int bytes = fieldValue.getBytes().length;
		// 获得字符数
		int chars = fieldValue.length();

		System.out.println("bytes:" + bytes);
		System.out.println("chars:" + chars);

		if (mode.equals("none")) {
			// 要求全是非中文字符
			// 所以字节数和字符数，两个数字必须相等，不相等则出错
			if (chars != bytes) {
				this.addFieldError(fieldName, object);
			}
		} else if (mode.equals("some")) {
			// 现在要求包含有中文字符
			if (chars == bytes || chars * 2 == bytes) {
				this.addFieldError(fieldName, object);
			}
		} else if (mode.equals("all")) {
			if (chars * 2 != bytes) {
				this.addFieldError(fieldName, object);
			}
		}
	}
}
