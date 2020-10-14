package com.woshidaniu.fastpdf.render.mapping;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @className: StyleResolver
 * @description: 样式解析器
 * @author : kangzhidong
 * @date : 下午5:10:14 2013-8-14
 * @modify by:
 * @modify date :
 * @modify description :
 */
public class CSSMappings {

	protected String cssPattern = "[\\.\\#]([\\w\\-]+){([\\S]*)}";
	
	private static CSSMappings instance = null;
	private Map<String,Integer> aligns = new HashMap<String, Integer>();
	private CSSMappings(){
		aligns.put("left", Element.ALIGN_LEFT);
		aligns.put("center", Element.ALIGN_CENTER);
		aligns.put("right", Element.ALIGN_RIGHT);
		aligns.put("justify", Element.ALIGN_JUSTIFIED);
		aligns.put("top", Element.ALIGN_TOP);
		aligns.put("middle", Element.ALIGN_MIDDLE);
		aligns.put("bottom", Element.ALIGN_BOTTOM);
		aligns.put("baseline", Element.ALIGN_BASELINE);
	}
	
	public static CSSMappings getInstance(){
		instance = instance==null?new CSSMappings():instance;
		return instance;
	}
	
	public Integer align(String align) {
		return (align != null && align.length() > 0) ? aligns.get(align) : Element.ALIGN_JUSTIFIED_ALL;
	}
}



