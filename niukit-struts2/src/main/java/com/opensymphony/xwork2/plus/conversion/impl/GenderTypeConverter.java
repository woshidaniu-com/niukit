package com.opensymphony.xwork2.plus.conversion.impl;

/**
 * *******************************************************************
 * @className	： GenderTypeConverter
 * @description	： TODO(描述这个类的作用)
 * @author 		： kangzhidong
 * @date		： Mar 3, 2016 3:10:26 PM
 * @version 	V1.0 
 * *******************************************************************
 */

import java.util.Map;

import com.opensymphony.xwork2.conversion.impl.DefaultTypeConverter;

/**
 * 
 *@类名称	: GenderTypeConverter.java
 *@类描述	：枚举自定义类型转换器
 *@创建人	：kangzhidong
 *@创建时间	：Mar 23, 2016 3:02:30 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class GenderTypeConverter extends DefaultTypeConverter {

	@Override
	public Object convertValue(Map<String, Object> context, Object value, Class toType) {
		if (toType == Enum.class) { // 当字符串向Gender类型转换时
			String[] params = (String[]) value;
			return Enum.valueOf(toType, params[0]);
		} else if (toType == String.class) { // 当Gender转换成字符串时
			Enum gender = (Enum) value;
			return gender.toString();
		}
		return null;
	}
}