package com.woshidaniu.finereport.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;

import com.fr.base.Parameter;
import com.woshidaniu.basicutils.BlankUtils;

public class ReportParserUtils {
	
	public static Parameter[] uniqueParameters(Parameter[] paras) {
		Set<String> set = new HashSet<String>();
		List<Parameter> list = new ArrayList<Parameter>(paras.length);
		for (Parameter p : paras) {
			if (!set.contains(p.getName())) {
				set.add(p.getName());
				list.add(p);
			}
		}
		return list.toArray(new Parameter[] {});

	}

	public static List<Map<String,String>> getDataColumnList(final Parameter[] paras,String ...filters ) {
		List<Map<String,String>> dataColumnList = new ArrayList<Map<String,String>>();
		try {
			if(!BlankUtils.isBlank(paras)){
				Map<String,String> row = null;
				int index = 0;
				//循环查询字段
				for (Parameter para : paras) {
					//进行字段过滤
					if( filters != null && ArrayUtils.contains(filters, para.getName())){
						continue;
					}
					row =  new HashMap<String, String>();
					row.put("field_guid", "field" + index);
					row.put("field_name", para.getName());
					row.put("field_index", para.getName());
					row.put("field_comment","" );
					dataColumnList.add(row);
					index ++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return dataColumnList;
	}
	
}
