package com.woshidaniu.db.core.builder;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.PropertyUtils;

import com.woshidaniu.db.core.annotation.TableMapper;
import com.woshidaniu.db.core.mapper.domain.FieldMapperModel;
import com.woshidaniu.db.core.mapper.domain.TableMapperModel;
import com.woshidaniu.db.core.utils.DynamicSQLUtils;


public abstract class AnnotationSQLBuilder {

	/**
	 * 
	 * @description: 构建动态JDBC Insert 语句
	 * @author : kangzhidong
	 * @date 下午2:55:47 2013-10-16 
	 * @param entity 实体bean
	 * @return 动态生成的SQL
	 */
	public <T> String buildInsertSQL(T entity) {
		if (null == entity) {
			throw new NullPointerException(" entity is null !");
		}
		Map<String,Object> fieldMap = describe(entity);
		// 从参数对象里提取注解信息
		TableMapperModel tableMapper = DynamicSQLUtils.buildTableMapper(entity.getClass());
		// 从表注解里获取表名等信息
		TableMapper tma = (TableMapper) tableMapper.getTableAnnotation();
		String tableName = tma.table();
		StringBuffer tableSql = new StringBuffer();
		tableSql.append("INSERT INTO ").append(tableName).append("(");
		boolean allFieldNull = true;
		// 根据字段注解和属性值联合生成sql语句
		for (String columnName : tableMapper.getFieldCache().keySet()) {
			FieldMapperModel fieldMapper = tableMapper.getFieldCache().get(columnName);
			String fieldName = fieldMapper.getFieldName();
			Object value = fieldMap.get(fieldName);
			// 由于要根据字段对象值是否为空来判断是否将字段加入到sql语句中，因此DTO对象的属性不能是简单类型，反而必须是封装类型
			if (value == null) {
				continue;
			}
			allFieldNull = false;
			tableSql.append(columnName).append(",");
		}
		if (allFieldNull) {
			throw new RuntimeException(" Object "+ entity.getClass().getName() + "'s all fields are null, build sql failed !");
		}
		tableSql.delete(tableSql.lastIndexOf(","),tableSql.lastIndexOf(",") + 1);
		return tableSql.append(") ").append(buildValuesSQL(entity)).toString();
		
	}
	
	/**
	 * 
	 * @description: 构建动态JDBC Delete 语句
	 * 				// delete from tableName where primaryKeyName=?
	 * @author : kangzhidong
	 * @date 下午2:55:47 2013-10-16 
	 * @param entity 实体bean
	 * @return 动态生成的SQL
	 */
	public <T> String buildDeleteSQL(T entity) {
		if (null == entity) {
			throw new NullPointerException(" entity is null !");
		}
		TableMapperModel tableMapper = DynamicSQLUtils.buildTableMapper(entity.getClass());
		TableMapper tma = (TableMapper) tableMapper.getTableAnnotation();
		String tableName = tma.table();
		StringBuffer tableSql = new StringBuffer();
		tableSql.append("DELETE FROM ").append(tableName).append(" t ");
		tableSql.append(buildWhereSQL(entity));
		return tableSql.toString();
	}

	/**
	 * 
	 * @description: 构建动态JDBC Update 语句
	 * @author : kangzhidong
	 * @date 下午2:55:47 2013-10-16 
	 * @param entity 实体bean
	 * @return 动态生成的SQL
	 */
	public <T> String buildUpdateSQL(T entity) {
		if (null == entity) {
			throw new NullPointerException(" entity is null !");
		}
		TableMapperModel tableMapper = DynamicSQLUtils.buildTableMapper(entity.getClass());
		TableMapper tma = (TableMapper) tableMapper.getTableAnnotation();
		String tableName = tma.table();
		StringBuffer tableSql = new StringBuffer();
		tableSql.append("UPDATE ").append(tableName).append(" t ");
		tableSql.append(buildSetSQL(entity));
		tableSql.append(buildWhereSQL(entity));
		return tableSql.toString();
	}

	/**
	 * 
	 * @description: 构建动态JDBC Query 语句
	 * @author : kangzhidong
	 * @date 下午2:55:47 2013-10-16 
	 * @param entity 实体bean
	 * @return 动态生成的SQL
	 */
	public <T> String buildQuerySQL(T entity) {
		if (null == entity) {
			throw new NullPointerException(" entity is null !");
		}
		TableMapperModel tableMapper = DynamicSQLUtils.buildTableMapper(entity.getClass());
		StringBuffer selectSql = new StringBuffer();
		selectSql.append("SELECT ");
		for (String columnName : tableMapper.getFieldCache().keySet()) {
			selectSql.append(columnName).append(",");
		}
		selectSql.delete(selectSql.lastIndexOf(","),selectSql.lastIndexOf(",") + 3);
		
		selectSql.append(buildFromSQL(entity));
		selectSql.append(buildWhereSQL(entity));
		return selectSql.toString();
	}

	/**
	 * 
	 * @description: 构建动态DBC Query Count 语句
	 * @author : kangzhidong
	 * @date 下午2:55:47 2013-10-16 
	 * @param entity 实体bean
	 * @return 动态生成的SQL
	 */
	public <T> String buildCountSQL(T entity) {
		StringBuilder buff = new StringBuilder(" SELECT COUNT('1') ");
		buff.append(buildFromSQL(entity));
		buff.append(buildWhereSQL(entity));
		return buff.toString();
	}

	///-----------------------------------------------------------------------------------
	
	@SuppressWarnings("unchecked")
	public <T> Map<String,Object> describe(T entity){
		try {
			return  PropertyUtils.describe(entity);
		} catch (IllegalAccessException e) {
			return new HashMap<String,Object>();
		} catch (InvocationTargetException e) {
			return new HashMap<String,Object>();
		} catch (NoSuchMethodException e) {
			return new HashMap<String,Object>();
		}
	}
	 
	public String removeOrders(String sql) {
		Matcher m = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", 2).matcher(sql);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		return sb.toString();
	}
	
	public String buildCountSQL(String sql) {
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
	
	private <T> String buildValuesSQL(T entity){
		Map<String,Object> fieldMap = describe(entity);
		// 从参数对象里提取注解信息
		TableMapperModel tableMapper = DynamicSQLUtils.buildTableMapper(entity.getClass());
		StringBuffer valueSql = new StringBuffer(" VALUES(");
		boolean allFieldNull = true;
		// 根据字段注解和属性值联合生成sql语句
		for (String columnName : tableMapper.getFieldCache().keySet()) {
			FieldMapperModel fieldMapper = tableMapper.getFieldCache().get(columnName);
			String fieldName = fieldMapper.getFieldName();
			Object value = fieldMap.get(fieldName);
			// 由于要根据字段对象值是否为空来判断是否将字段加入到sql语句中，因此DTO对象的属性不能是简单类型，反而必须是封装类型
			if (value == null) {
				continue;
			}
			allFieldNull = false;
			valueSql.append(getHoldText(fieldMapper)).append(",");
		}
		if (allFieldNull) {
			throw new RuntimeException(" Object "+ entity.getClass().getName() + "'s all fields are null, build sql failed !");
		}
		valueSql.delete(valueSql.lastIndexOf(","),valueSql.lastIndexOf(",") + 1);
		return valueSql.append(")").toString();
	}
	
	/**
	 * 
	 * @description: 构建动态FROM 语句
	 * @author : kangzhidong
	 * @date 下午2:55:47 2013-10-16 
	 * @param entity 实体bean
	 * @return 动态生成的SQL
	 */
	private <T> String buildFromSQL(T entity) {
		TableMapperModel tableMapper = DynamicSQLUtils.buildTableMapper(entity.getClass());
		TableMapper tb = (TableMapper) tableMapper.getTableAnnotation();
		StringBuilder from = new StringBuilder(" FROM ");
		if ((tb != null && tb.table().length() > 0)) {
			from.append(tb.table().toUpperCase()).append(" t ");
		} else {
			throw new NullPointerException(" TableAnnotation undefined on class '"+entity.getClass()+"'");
		}
		return from.toString();
	}
	

	/**
	 * 
	 * @description: 构建动态Set 语句
	 * @author : kangzhidong
	 * @date 下午2:55:47 2013-10-16 
	 * @param entity 实体bean
	 * @return 动态生成的SQL
	 */
	private <T> String buildSetSQL(T entity) {
		TableMapperModel tableMapper = DynamicSQLUtils.buildTableMapper(entity.getClass());
		Map<String,Object> fieldMap = describe(entity);
		StringBuffer setSql = new StringBuffer(" SET ");
		boolean allFieldNull = true;
		for (String columnName : tableMapper.getFieldCache().keySet()) {
			FieldMapperModel fieldMapper = tableMapper.getFieldCache().get(columnName);
			String fieldName = fieldMapper.getFieldName();
			Object value = fieldMap.get(fieldName);
			if (value == null) {
				continue;
			}
			allFieldNull = false;
			setSql.append(" t.").append(columnName).append(" = ").append(getHoldText(fieldMapper)).append(",");
		}
		if (allFieldNull) {
			throw new RuntimeException(" Object "+ entity.getClass().getName() + "'s all fields are null, build sql failed !");
		}
		setSql.delete(setSql.lastIndexOf(","),setSql.lastIndexOf(",") + 1);
		return setSql.toString();
	}
	
	/**
	 * 
	 * @description: 构建动态Where 语句
	 * @author : kangzhidong
	 * @date 下午2:55:47 2013-10-16 
	 * @param entity 实体bean
	 * @return 动态生成的SQL
	 */
	private <T> String buildWhereSQL(T entity) {
		TableMapperModel tableMapper = DynamicSQLUtils.buildTableMapper(entity.getClass());
		String[] uniqueKeyNames = DynamicSQLUtils.buildUniqueKey(tableMapper);
		Map<String,Object> fieldMap = describe(entity);
		StringBuffer whereSql = new StringBuffer(" WHERE 1=1 ");
		for (int i = 0; i < uniqueKeyNames.length; i++) {
			whereSql.append(" AND t.").append(uniqueKeyNames[i]);
			FieldMapperModel fieldMapper = tableMapper.getFieldCache().get(uniqueKeyNames[i]);
			String fieldName = fieldMapper.getFieldName();
			Object value = fieldMap.get(fieldName);
			if (value == null) {
				throw new RuntimeException("Unique key '" + uniqueKeyNames[i]+ "' can't be null, build query sql failed!");
			}
			whereSql.append(" = ").append(getHoldText(fieldMapper));
		}
		return whereSql.toString();
	}
	
	public abstract <T> String getHoldText(FieldMapperModel entity);
	
}



