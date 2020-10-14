package com.woshidaniu.db.core.builder.impl;

import com.woshidaniu.db.core.builder.AnnotationSQLBuilder;
import com.woshidaniu.db.core.mapper.domain.FieldMapperModel;

public class MybatisDynamicSQLBuilder extends AnnotationSQLBuilder {

	public <T> String getHoldText(FieldMapperModel entity){
		StringBuilder hold = new StringBuilder();
		hold.append("#{").append(entity.getFieldName()).append(",").append("jdbcType=").append(entity.getJdbcType().toString()).append("}");
		return hold.toString();
	}

}
