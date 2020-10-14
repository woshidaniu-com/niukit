package com.woshidaniu.db.core.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.woshidaniu.db.core.annotation.Parameter;
import com.woshidaniu.db.core.annotation.TableMapper;

/**
 * 
 * @description:分页SQL处理类
 * @author kangzhidong
 * @date 2012-9-18
 */
public class DynamicHQLUtils {

	public static String removeOrders(String sql) {
		Matcher m = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", 2).matcher(sql);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		return sb.toString();
	}

	public static String buildCountSQL(String sql) {
		String removedOrdersQueryStr = removeOrders(sql);
		int index = removedOrdersQueryStr.toUpperCase().indexOf("FROM");
		StringBuilder builder = new StringBuilder("SELECT count(*) ");
		if (index != -1) {
			builder.append(removedOrdersQueryStr.substring(index));
		} else {
			builder.append(removedOrdersQueryStr);
		}
		return builder.toString();
	}

	public static <T> String buildQueryHQL(T entity){
		StringBuilder buff = new StringBuilder(" select ");
		Field[] fields = entity.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			Annotation[] annotations = field.getDeclaredAnnotations();
			for (Annotation annotation : annotations) {
				if (annotation.annotationType().equals(Parameter.class)) {
					buff.append(field.getName()).append(",");
					break;
				} else {
					continue;
				}
			}
		}
		buff.deleteCharAt(buff.length() - 1);
		buff.append(" from ");
		String[] names = (entity.getClass().getName() + "").split("\\.");
		buff.append(names[names.length - 1]);
		return buff.toString();
	}
	
	public static <T> Object[] collectParameters(T entity) {
		List<Object> parameters = new ArrayList<Object>();
		Field[] fields = entity.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			Annotation annotation = field.getAnnotation(Parameter.class);
			if (annotation!=null) {
				try {
					parameters.add(field.get(entity)!=null?field.get(entity):"");
				} catch (Exception e) {
					
				}
				break;
			} else {
				continue;
			}
		}
		return (String[]) parameters.toArray();
	}
	
	public static <T> String buildQuerySQL(T entity)  {
		StringBuilder buff = new StringBuilder(" SELECT ");
		Field[] fields = entity.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			Annotation annotation = field.getAnnotation(Parameter.class);
			if (annotation!=null) {
				Parameter select = (Parameter) annotation;
				buff.append((select.key().length() > 0 ? select.key() : field.getName()).toUpperCase()).append(",");
				break;
			} else {
				continue;
			}
		}
		buff.deleteCharAt(buff.length() - 1);
		buff.append(buildFromSQL(entity));
		buff.append(buildWhereSQL(entity));
		return buff.toString();
	}
	
	
	public static <T> String buildCountSQL(T entity) {
		StringBuilder buff = new StringBuilder(" SELECT COUNT(*) ");
		buff.append(buildFromSQL(entity));
		buff.append(buildWhereSQL(entity));
		return buff.toString();
	}
	
	public static <T> String buildFromSQL(T entity) {
		//组装from
		StringBuilder from = new StringBuilder(" FROM ");
		TableMapper tb = entity.getClass().getAnnotation(TableMapper.class);
		if ((tb != null && tb.table().length() > 0)) {
			from.append(tb.table().toUpperCase());
		} else {
			String[] names = (entity.getClass().getName() + "").split("\\.");
			from.append(names[names.length - 1].toUpperCase());
		}
		return from.toString();
	}
	
	public static <T> String buildWhereSQL(T entity) {
		//组装where
		StringBuilder where = new StringBuilder(" WHERE 1=1 ");
		Field[] fields = entity.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			Annotation annotation = field.getAnnotation(Parameter.class);
			if (annotation!=null) {
				Parameter select = (Parameter) annotation;
				where.append(" AND ").append((select.key().length() > 0 ? select.key() : field.getName()).toUpperCase()).append(" = ? ").append(",");
				break;
			} else {
				continue;
			}
		}
		where.deleteCharAt(where.length() - 1);
		return where.toString();
	}

}
