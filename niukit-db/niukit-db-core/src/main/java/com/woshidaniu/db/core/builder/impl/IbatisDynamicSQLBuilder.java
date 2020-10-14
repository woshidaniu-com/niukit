package com.woshidaniu.db.core.builder.impl;

import com.woshidaniu.db.core.builder.AnnotationSQLBuilder;
import com.woshidaniu.db.core.mapper.domain.FieldMapperModel;

/**
 * 
 * @className: IbatisDynamicSQLBuilder
 * @description: 
 * @author : kangzhidong
 * @date : 下午4:27:21 2013-10-16
 * @modify by:
 * @modify date :
 * @modify description :
 */
public class IbatisDynamicSQLBuilder extends AnnotationSQLBuilder {

	public <T> String getHoldText(FieldMapperModel entity){
		StringBuilder hold = new StringBuilder();
		hold.append("#").append(entity.getFieldName()).append(":").append(entity.getJdbcType().toString()).append("#");
		return hold.toString();
	}
}
