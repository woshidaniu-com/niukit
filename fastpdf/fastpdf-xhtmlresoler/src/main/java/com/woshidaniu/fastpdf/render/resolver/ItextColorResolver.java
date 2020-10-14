package com.woshidaniu.fastpdf.render.resolver;

import java.awt.Color;
import java.io.IOException;
import java.util.Map;

import com.woshidaniu.fastpdf.render.elements.ItextXMLElement;

/**
 * 
 * @className: FontParser
 * @description: 字体解析器：必须是定义过的字体
 * @author : kangzhidong
 * @date : 上午10:31:53 2013-8-16
 * @modify by:
 * @modify date :
 * @modify description :
 */
public class ItextColorResolver implements ColorResolver<Color,ItextXMLElement>{

	private static ItextColorResolver instance = null;
	
	public static ItextColorResolver getInstance(){
		instance = instance==null?new ItextColorResolver():instance;
		return instance;
	}

	public Color getColor(ItextXMLElement element) throws IOException {
		 return this.getColor(element.styles(), new String[]{"color"});
	}
	
	public Color getColor(Map<String,Object> styles,String[] cssNames) throws IOException {
		for (int i = 0; i < cssNames.length; i++) {
			Object color = styles.get(cssNames[i]);
			if(color!=null&&color instanceof String){
				try {
					int[] rgb = ColorParser.getInstance().rgb(color.toString());
					System.out.println(rgb[0]+","+rgb[1]+","+rgb[2]);
					return new Color(rgb[0],rgb[1], rgb[2]);
				} catch (Exception e) {
					return Color.BLACK;
				}
			}
		}
		return Color.BLACK;
	}

	
	

	
}
