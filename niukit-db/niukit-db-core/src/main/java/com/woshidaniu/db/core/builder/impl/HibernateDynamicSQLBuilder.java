package com.woshidaniu.db.core.builder.impl;

import com.woshidaniu.db.core.builder.AnnotationSQLBuilder;
import com.woshidaniu.db.core.mapper.domain.FieldMapperModel;

public class HibernateDynamicSQLBuilder extends AnnotationSQLBuilder {

	public <T> String getHoldText(FieldMapperModel entity){
		return " ? ";
	}
	
}



