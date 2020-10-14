package com.woshidaniu.format.utils;

import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;

import com.woshidaniu.basicutils.BlankUtils;

public abstract class JSONFormatUtils {

	public static <T> String toJSONString(List<T> list,String key,String value) throws Exception {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		if(!BlankUtils.isBlank(list)){
			for(T bean : list){
				builder.append("{");
				builder.append("'").append(key).append("'");
				builder.append(":");
				builder.append("'"+String.valueOf(PropertyUtils.getProperty(bean, key))+"'");
				builder.append(",");
				builder.append("'").append(value).append("'");
				builder.append(":");
				builder.append("'"+String.valueOf(PropertyUtils.getProperty(bean, value))+"'");
				builder.append("}");
				builder.append(",");
			}
			builder.delete(builder.length() - 1, builder.length());
		}
		builder.append("]");
		return builder.toString();
	}
	
}
