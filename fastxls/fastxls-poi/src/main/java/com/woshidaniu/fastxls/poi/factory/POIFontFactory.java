package com.woshidaniu.fastxls.poi.factory;

import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basicutils.StringUtils;

/**
 * 
 *@类名称	: POIFontFactory.java
 *@类描述	：Workbook字体工厂类
 *@创建人	：kangzhidong
 *@创建时间	：Mar 26, 2016 10:07:40 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class POIFontFactory {
	
	protected static Logger LOG = LoggerFactory.getLogger(POIFontFactory.class);
	private volatile static POIFontFactory singleton;

	public static POIFontFactory getInstance() {
		if (singleton == null) {
			synchronized (POIFontFactory.class) {
				if (singleton == null) {
					singleton = new POIFontFactory();
				}
			}
		}
		return singleton;
	}
	
	private POIFontFactory(){
		
	}
	
	public Font getFont(Workbook wb,String fontName,short fontHeightInPoints) {
		return getFont(wb, fontName, Font.COLOR_NORMAL, fontHeightInPoints);
	}
	
	public Font getFont(Workbook wb,String fontName, short color, short fontHeightInPoints) {
		return getFont(wb, fontName, Font.BOLDWEIGHT_NORMAL, color, fontHeightInPoints);
	}
	
	public Font getFont(Workbook wb,String fontName, short boldweight, short color, short fontHeightInPoints) {
		return getFont(wb, fontName, boldweight, color, fontHeightInPoints , false, false, Font.SS_NONE, Font.U_NONE, Font.DEFAULT_CHARSET);
	}
	
	/**
	 * 
	 *@描述		：
	 *@创建人		: kangzhidong
	 *@创建时间	: Aug 26, 20164:25:33 PM
	 *@param wb
	 *@param fontName	: 字体名称
	 *@param boldweight	: 字体粗细
	 *@param color		: 字体颜色
	 *@param fontHeightInPoints	: 字体大小
	 *@param italic		: 是否斜体
	 *@param strikeout	: 是否有删除线
	 *@param typeOffset	: 设置上标，下标
	 *@param underline	: 是否有下划线
	 *@param charSet	: 字符集
	 *@return
	 *@修改人	: 
	 *@修改时间	: 
	 *@修改描述	:
	 */
	public Font getFont(Workbook wb,String fontName,short boldweight, short color, short fontHeightInPoints,
			boolean italic, boolean strikeout, short typeOffset, byte underline, int charSet) {
		if (wb != null && StringUtils.isNotEmpty(fontName)) {
			Font font = wb.findFont(boldweight, color, fontHeightInPoints ,fontName , italic , strikeout , typeOffset , underline);
			if(font == null){
				//生成一个字体
				font = wb.createFont();
				//字体粗细
				font.setBoldweight(boldweight);
				//字体颜色
				font.setColor(color);
				//字体大小
				font.setFontHeightInPoints(fontHeightInPoints);
				//font.setFontHeight((short)(12 * 20));
				//字体
				font.setFontName(fontName);
				//是否斜体
				font.setItalic(italic);
				//是否有删除线
				font.setStrikeout(strikeout);
				//设置上标，下标
				font.setTypeOffset(typeOffset);
				//下划线
				font.setUnderline(underline);
				//字符集
				font.setCharSet(charSet);
				
				LOG.debug("Create Font [" +  StringUtils.join(new Object[]{ fontName , boldweight , color , fontHeightInPoints
						, italic , strikeout , typeOffset , underline , charSet }, "-") + " ]" );
			}
			return font;
		}
		return null;
	}
 
}

