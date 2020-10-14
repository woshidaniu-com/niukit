package com.woshidaniu.db.core.mapper.domain;

/**
 * 字段映射类，用于描述java对象字段和数据库表字段之间的对应关系
 * 
 */
public class FieldMapperModel {

	/**
	 * Java对象字段名
	 */
	private String fieldName;

	/**
	 * 数据库表字段名
	 */
	private String columnName;

	/**
	 * 数据库字段对应的jdbc类型
	 */
	private JdbcType jdbcType;

	/**
	 * 数据库表字段是否可为空
	 */
	private boolean emptyAble = true;

	/**
	 * 数据库表字段注释
	 */
	private String comment;
	

	public String getFieldName() {

		return fieldName;
	}

	public void setFieldName(String fieldName) {

		this.fieldName = fieldName;
	}

	public String getColumnName() {

		return columnName;
	}

	public void setColumnName(String columnName) {

		this.columnName = columnName;
	}

	public JdbcType getJdbcType() {

		return jdbcType;
	}

	public void setJdbcType(JdbcType jdbcType) {

		this.jdbcType = jdbcType;
	}

	public boolean isEmptyAble() {

		return emptyAble;
	}

	public void setEmptyAble(boolean emptyAble) {

		this.emptyAble = emptyAble;
	}

	public String getComment() {

		return comment;
	}

	public void setComment(String comment) {

		this.comment = comment;
	}

}
