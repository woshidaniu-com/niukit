package com.opensymphony.xwork2.plus.conversion.impl;

/**
 * *******************************************************************
 * @className	： DateTypeConverter
 * @description	： TODO(描述这个类的作用)
 * @author 		： kangzhidong
 * @date		： Mar 3, 2016 3:11:14 PM
 * @version 	V1.0 
 * *******************************************************************
 */

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.opensymphony.xwork2.conversion.impl.DefaultTypeConverter;

/**
 * 
 *@类名称	: DateTypeConverter.java
 *@类描述	：日期自定义类型转换器
 *@创建人	：kangzhidong
 *@创建时间	：Mar 23, 2016 3:02:14 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class DateTypeConverter extends DefaultTypeConverter {

	@SuppressWarnings("unchecked")
	@Override
	public Object convertValue(Map<String, Object> context, Object value,
			Class toType) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		try {
			if (toType == Date.class) { // 当字符串向Date类型转换时
				String[] params = (String[]) value;
				return sdf.parseObject(params[0]);
			} else if (toType == String.class) { // 当Date转换成字符串时
				Date date = (Date) value;
				return sdf.format(date);
			}
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}