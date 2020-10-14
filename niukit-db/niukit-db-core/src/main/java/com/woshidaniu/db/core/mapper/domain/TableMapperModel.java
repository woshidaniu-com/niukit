package com.woshidaniu.db.core.mapper.domain;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

/**
 * 描述java对象的数据库映射信息（数据库表的映射、字段的映射）
 */
public class TableMapperModel {

    private Annotation tableAnnotation;

    private Map<String, FieldMapperModel> fieldCache;

    private List<FieldMapperModel> fieldList;

	public Annotation getTableAnnotation() {
		return tableAnnotation;
	}

	public void setTableAnnotation(Annotation tableAnnotation) {
		this.tableAnnotation = tableAnnotation;
	}

	public Map<String, FieldMapperModel> getFieldCache() {
		return fieldCache;
	}

	public void setFieldCache(Map<String, FieldMapperModel> fieldCache) {
	
		this.fieldCache = fieldCache;
	}

	public List<FieldMapperModel> getFieldList() {
		return fieldList;
	}

	public void setFieldList(List<FieldMapperModel> fieldList) {
	
		this.fieldList = fieldList;
	}

   

}
