package com.woshidaniu.db.core.mapper;

import java.util.Map;

public interface MapRowMapper<T> {
	
	public  T mapperRow(Map<String, Object> row);
	
}
