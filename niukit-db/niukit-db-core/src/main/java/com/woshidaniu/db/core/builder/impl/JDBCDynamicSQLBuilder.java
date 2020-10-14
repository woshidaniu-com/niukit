package com.woshidaniu.db.core.builder.impl;

import com.woshidaniu.db.core.builder.AnnotationSQLBuilder;
import com.woshidaniu.db.core.mapper.domain.FieldMapperModel;
/**
 * 
 * @className: JDBCDynamicSQLBuilder
 * @description: TODO(描述这个类的作用)
 * @author : kangzhidong
 * @date : 下午2:49:56 2013-10-16
 * @modify by:
 * @modify date :
 * @modify description :
 */
public class JDBCDynamicSQLBuilder extends AnnotationSQLBuilder {

	public <T> String getHoldText(FieldMapperModel entity){
		return " ? ";
	}
	
	
}
