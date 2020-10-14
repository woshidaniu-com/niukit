package com.woshidaniu.qa.types;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import com.woshidaniu.qa.tools.DateHelper;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2017年5月12日 下午2:39:58 类说明
 */
public abstract class AbstractTypeMap {
	private static Map<String, Class> types = new HashMap<String, Class>() {
		{
			this.put("java.sql.Timestamp", Timestamp.class);
			this.put("java.sql.Time", Time.class);
			this.put("java.sql.Date", Date.class);

			this.put("java.lang.Integer", Integer.class);
			this.put("java.lang.String", String.class);
			this.put("java.lang.Long", Long.class);
			this.put("java.lang.Short", Short.class);
			this.put("java.lang.Double", Double.class);
			this.put("java.lang.Float", Float.class);
			this.put("java.lang.Boolean", Boolean.class);
			this.put("java.math.BigDecimal", BigDecimal.class);

			this.put("[B", byte[].class);
			this.put("byte[]", byte[].class);
		}
	};

	/**
	 * 将string对象转换为java对象
	 * 
	 * @param input
	 * @param javaType
	 * @return
	 */
	public Object toObjectByType(String input, String typeName) {
		Class javaType = types.get(typeName);
		if (javaType == null) {
			javaType = this.getJavaTypeByName(typeName);
		}
		if (javaType == String.class) {
			return input;
		}
		if (javaType == Long.class) {
			return Long.valueOf(input);
		}
		if (javaType == Integer.class) {
			return Integer.valueOf(input);
		}
		if (javaType == Short.class) {
			return Short.valueOf(input);
		}
		if (javaType == Double.class) {
			return Double.valueOf(input);
		}
		if (javaType == Float.class) {
			return Float.valueOf(input);
		}
		if (javaType == Boolean.class) {
			if (input.matches("\\d+")) {
				return !Integer.valueOf(input).equals(0);
			} else {
				return Boolean.valueOf(input);
			}
		}
		if (javaType == Byte.class) {
			return Byte.valueOf(input);
		}

		if (javaType == BigDecimal.class) {
			return new BigDecimal(input);
		}
		if (javaType == Date.class) {
			long time = DateHelper.parse(input).getTime();
			return new Date(time);
		}
		if (javaType == Time.class) {
			long time = DateHelper.parse(input).getTime();
			return new Time(time);
		}
		if (javaType == Timestamp.class) {
			long time = DateHelper.parse(input).getTime();
			return new Timestamp(time);
		}

		if (javaType == byte[].class) {
			InputStream is = getStream(input);
			return is;
		}
		if (javaType == InputStream.class) {
			InputStream is = new ByteArrayInputStream(input.getBytes());
			return is;
		}
		Object value = this.toObjectByType(input, javaType);
		return value;
	}

	protected abstract Class getJavaTypeByName(String typeName);

	protected abstract Object toObjectByType(String input, Class javaType);


	protected abstract Object getDefaultValue(Class javaType);

	protected InputStream getStream(String input) {
		try {
			byte[] bs = input.getBytes("UTF-8");
			InputStream is = new ByteArrayInputStream(bs);
			return is;
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
}
