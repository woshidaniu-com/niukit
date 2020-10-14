package com.woshidaniu.fastpdf.render.resolver;

import java.util.Map;

import com.itextpdf.text.BaseColor;
import com.woshidaniu.fastpdf.render.elements.ItextXMLElement;

/**
 * 
 * @package com.woshidaniu.fastpdf.render.resolver
 * @className: ItextBaseColorResolver
 * @description: TODO
 * @author : kangzhidong
 * @date : 2014-1-22
 * @time : 下午4:16:28
 */
public class ItextBaseColorResolver implements ColorResolver<BaseColor,ItextXMLElement>{

	private static ItextBaseColorResolver instance = null;
	
	public static ItextBaseColorResolver getInstance(){
		instance = instance==null?new ItextBaseColorResolver():instance;
		return instance;
	}

	public BaseColor getColor(ItextXMLElement element) {
		 return this.getColor(element.styles(), new String[]{
			 "background-color",
			 "border-color",
			 "border-bottom-color",
			 "border-left-color",
			 "border-right-color",
			 "border-top-color",
			 "color"});
	}
	
	public BaseColor getColor(Map<String,Object> styles,String[] cssNames) {
		for (int i = 0; i < cssNames.length; i++) {
			Object color = styles.get(cssNames[i]);
			if(color!=null&&color instanceof String){
				try {
					return this.getColor(color.toString());
				} catch (Exception e) {
					
				}
			}
		}
		return BaseColor.BLACK;
	}

	public BaseColor getColor(String color) {
		if(color!=null&&color instanceof String){
			int[] rgb = ColorParser.getInstance().rgb(color.toString());
			System.out.println(rgb[0]+","+rgb[1]+","+rgb[2]);
			return new BaseColor(rgb[0],rgb[1], rgb[2]);
		}
		return BaseColor.BLACK;
	}

	

	
}
