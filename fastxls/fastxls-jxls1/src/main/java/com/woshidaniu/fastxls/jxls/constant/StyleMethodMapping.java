package com.woshidaniu.fastxls.jxls.constant;

import java.util.HashMap;
import java.util.Map;

public final class StyleMethodMapping {

	private static Map<String, String> mapping = new HashMap<String, String>();

	static{
		
		
		mapping.put("text-align", "setHorizontalAlignment");
		mapping.put("background", "setBackgroundColor");
	}
	
}
