package com.woshidaniu.fastpdf.render.resolver;

import java.util.HashMap;
import java.util.Map;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.BaseFont;
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
public class ItextFontResolver extends AbstractFontResolver<Font,BaseColor,ItextXMLElement>{

	private static ItextFontResolver instance = null;
	private Map<String,Integer> styles = new HashMap<String, Integer>();
	private ItextFontResolver(){
		styles.put("normal", Font.NORMAL);
		styles.put("bold", Font.BOLD);
		styles.put("bolditalic", Font.BOLDITALIC);
		styles.put("italic", Font.ITALIC);
		styles.put("underline", Font.UNDERLINE);
	}
	
	public static ItextFontResolver getInstance(){
		instance = instance==null?new ItextFontResolver():instance;
		return instance;
	}

	public Font getFont(ItextXMLElement element) {
		 return this.getFont(element.styles());
	}
	
	public Font getFont(Map<String,Object> styles){
		return FontFactory.getFont(this.family(styles.get("font-family")),BaseFont.IDENTITY_H,true,
				this.size(styles.get("font-size")),
				this.style(styles.get("font-style")),
				this.color(styles.get("color")),true);
	}
	
	public Font getFont(String[] css_values){
		return FontFactory.getFont(this.family(css_values[0]),BaseFont.IDENTITY_H,true,this.size(css_values[1]),this.style(css_values[2]),this.color(css_values[3]),true);
	}
	
	public int style(Object style) {
		return (style != null && style.toString().length() > 0) ? styles.get(style) : Font.NORMAL;
	}
	
	public int size(Object size){
		return (size != null && size.toString().length() > 0) ? Double.valueOf(DimensionUtils.unitParse(size.toString())).intValue() : 12;
	}
	
	public BaseColor color(Object color){
		return (color != null && color.toString().length() > 0) ? ItextBaseColorResolver.getInstance().getColor(color.toString()):BaseColor.BLACK;
	}

	
	
}
