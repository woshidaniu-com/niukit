package com.woshidaniu.orm.mybatis.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.woshidaniu.db.core.annotation.TableField;
import com.woshidaniu.db.core.annotation.TableId;
import com.woshidaniu.db.core.annotation.TableName;
import com.woshidaniu.db.core.mapper.domain.TableInfo;

/**
 * 
 * @className	： TableInfoUtils
 * @description	：实体类反射表辅助类
 * @author 		： kangzhidong
 * @date		： Jan 26, 2016 3:12:35 PM
 * @version 	V1.0
 */
public class TableInfoUtils {

	/**
	 * 缓存反射类表信息
	 */
	private static Map<String, TableInfo> tableInfoCache = new ConcurrentHashMap<String, TableInfo>();

	/**
	 * <p>
	 * 根据实体类反射获取表信息
	 * <p>
	 * 
	 * @param clazz
	 *            反射实体类
	 * @return
	 */
	public static TableInfo getTableInfo(Class<?> clazz) {
		TableInfo ti = tableInfoCache.get(clazz.getName());
		if (ti != null) {
			return ti;
		}
		List<Field> list = getAllFields(clazz);
		TableInfo tableInfo = new TableInfo();

		/* 表名 */
		TableName table = clazz.getAnnotation(TableName.class);
		if (table != null && table.value() != null && table.value().trim().length() > 0) {
			tableInfo.setTableName(table.value());
		} else {
			tableInfo.setTableName(camelToUnderline(clazz.getSimpleName()));
		}

		List<String> fieldList = new ArrayList<String>();
		for (Field field : list) {
			/* 主键ID */
			TableId tableId = field.getAnnotation(TableId.class);
			if (tableId != null) {
				tableInfo.setAutoIncrement(tableId.auto());
				tableInfo.setTableId(field.getName());
				continue;
			}

			/* 字段 */
			fieldList.add(field.getName());
		}

		/* 字段列表 */
		tableInfo.setFieldList(fieldList);
		tableInfoCache.put(clazz.getName(), tableInfo);
		return tableInfo;
	}

	/**
	 * 去掉下划线转换为大写
	 */
	private static String camelToUnderline(String param) {
		if (param == null || "".equals(param.trim())) {
			return "";
		}
		int len = param.length();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			char c = param.charAt(i);
			if (Character.isUpperCase(c) && i > 0) {
				sb.append("_");
				sb.append(Character.toLowerCase(c));
			} else {
				sb.append(Character.toLowerCase(c));
			}
		}
		return sb.toString();
	}

	/**
	 * 获取该类的所有字符列表
	 * 
	 * @param clazz
	 *            反射类
	 * @return
	 */
	private static List<Field> getAllFields(Class<?> clazz) {
		List<Field> result = new LinkedList<Field>();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {

			/* 过滤 transient关键字修饰则该属性 */
			if (Modifier.isTransient(field.getModifiers())) {
				continue;
			}

			/* 过滤注解非表字段属性 */
			TableField tableField = field.getAnnotation(TableField.class);
			if (tableField == null || tableField.exist()) {
				result.add(field);
			}
		}

		/* 处理父类字段 */
		Class<?> superClass = clazz.getSuperclass();
		if (superClass.equals(Object.class)) {
			return result;
		}
		result.addAll(getAllFields(superClass));
		return result;
	}

}
