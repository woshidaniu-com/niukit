package com.woshidaniu.fastxls.jexcel.utils;

import java.util.List;
import java.util.Map;

public class RowUtil {
	
	public static int getColumnNum(List<Map<String, String>> data) {
		int num = 0;
		for (Map<String, String> map : data) {
			num = Math.max(map.size(), num);
		}
		return num;
	}
	
}
