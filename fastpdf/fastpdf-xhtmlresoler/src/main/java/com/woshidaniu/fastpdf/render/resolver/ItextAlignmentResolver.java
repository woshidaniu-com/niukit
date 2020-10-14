package com.woshidaniu.fastpdf.render.resolver;

import java.util.HashMap;
import java.util.Map;

import com.woshidaniu.fastpdf.render.elements.ItextXMLElement;

/**
 * 
 * @package com.woshidaniu.fastpdf.render.resolver
 * @className: ItextAlignmentResolver
 * @description: TODO
 * @author : kangzhidong
 * @date : 2014-1-16
 * @time : 下午7:44:39
 */
public class ItextAlignmentResolver implements AlignmentResolver<Integer,ItextXMLElement>{

	private static ItextAlignmentResolver instance = null;
	private Map<String,Integer> alignments = new HashMap<String, Integer>();
	private ItextAlignmentResolver(){
		//纵向
		alignments.put("baseline", Element.ALIGN_BASELINE);
		alignments.put("top", Element.ALIGN_TOP);
		alignments.put("middle", Element.ALIGN_MIDDLE);
		alignments.put("bottom", Element.ALIGN_BOTTOM);
		//横向
		alignments.put("left", Element.ALIGN_LEFT);
		alignments.put("right", Element.ALIGN_RIGHT);
		alignments.put("center", Element.ALIGN_CENTER);
		alignments.put("justify", Element.ALIGN_JUSTIFIED);
		
	}
	
	public static ItextAlignmentResolver getInstance(){
		instance = instance==null?new ItextAlignmentResolver():instance;
		return instance;
	}

	public Integer getTextAlign(ItextXMLElement element) {
		return this.getAlignment(element,"text-align");
	}

	public Integer getVerticalAlign(ItextXMLElement element) {
		return this.getAlignment(element,"vertical-align");
	}
	
	public Integer getAlignment(ItextXMLElement element,String cssName) {
		Map<String,Object> styles = element.styles();
		if(null!=styles&&!styles.isEmpty()){
			Object alignment = styles.get(cssName);
			if(alignment!=null&&alignment instanceof String){
				if("inherit".equalsIgnoreCase(alignment.toString())){
					return this.getAlignment((ItextXMLElement) element.getParentElement(), cssName);
				}else{
					return alignments.get(cssName);
				}
			}
		}
		return Element.ALIGN_JUSTIFIED_ALL;
	}
	
}
