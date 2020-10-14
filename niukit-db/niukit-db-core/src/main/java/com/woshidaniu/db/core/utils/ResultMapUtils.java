package com.woshidaniu.db.core.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.woshidaniu.db.core.mapper.Map2BeanRowMapper;
import com.woshidaniu.db.core.mapper.MapRowMapper;

public final class ResultMapUtils {
	
	public static <T> List<T> mapToObject(List<Map<String, Object>> list){
		MapRowMapper<T> mapper = new Map2BeanRowMapper<T>();
		List<T> result = new ArrayList<T>();
		for (Iterator<Map<String, Object>> iterator = list.iterator(); iterator.hasNext();) {
			Map<String, Object> temp = (Map<String, Object>) iterator.next();
			result.add(mapper.mapperRow(temp));
		}
		return result;
	}
	
	public static <T> List<T> mapToObject(List<Map<String, Object>> list,MapRowMapper<T> mapper){
		List<T> result = new ArrayList<T>();
		for (Iterator<Map<String, Object>> iterator = list.iterator(); iterator.hasNext();) {
			Map<String, Object> temp = (Map<String, Object>) iterator.next();
			result.add(mapper.mapperRow(temp));
		}
		return result;
	}

}



