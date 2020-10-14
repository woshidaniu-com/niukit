package com.woshidaniu.fastxls.poi.cahce;


import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basicutils.StringUtils;
import com.woshidaniu.fastxls.poi.factory.POIFontFactory;
import com.woshidaniu.fastxls.poi.utils.POIConfigUtils;

/**
 * 
 *@类名称	: POICellStyleCacheManager.java
 *@类描述	：单元格样式缓存管理
 *@创建人	：kangzhidong
 *@创建时间	：Mar 26, 2016 11:24:59 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class POICellStyleCacheManager {
	
	protected static Logger LOG = LoggerFactory.getLogger(POICellStyleCacheManager.class);
	protected static POIFontFactory fontFactory = POIFontFactory.getInstance();
	protected static Properties config = POIConfigUtils.getDefaultConfig();
	private volatile static POICellStyleCacheManager singleton;
 	protected static ConcurrentMap<Workbook, Map<String,POICellStyleKey>> COMPLIED_FONTKEY = new ConcurrentHashMap<Workbook, Map<String,POICellStyleKey>>();
 	protected static ConcurrentMap<Workbook, Map<POICellStyleKey,CellStyle>> COMPLIED_CELLSTYLE = new ConcurrentHashMap<Workbook, Map<POICellStyleKey,CellStyle>>();
	
	public static POICellStyleCacheManager getInstance() {
		if (singleton == null) {
			synchronized (POICellStyleCacheManager.class) {
				if (singleton == null) {
					singleton = new POICellStyleCacheManager();
				}
			}
		}
		return singleton;
	}
	
	private POICellStyleCacheManager(){
		
	}
	
	public POICellStyleKey getCellStyleKey(Workbook wb,Font font) {
		return getCellStyleKey(wb, font.getFontName(), font.getBoldweight(), font.getColor(), 
				font.getFontHeightInPoints() , font.getItalic(), font.getStrikeout(), 
				font.getTypeOffset(), font.getUnderline(), font.getCharSet());
	}
	
	public POICellStyleKey getCellStyleKey(Workbook wb,String fontName,short fontHeightInPoints) {
		return getCellStyleKey(wb, fontName, Font.COLOR_NORMAL, fontHeightInPoints);
	}
	
	public POICellStyleKey getCellStyleKey(Workbook wb,String fontName, short color, short fontHeightInPoints) {
		return getCellStyleKey(wb, fontName, Font.BOLDWEIGHT_NORMAL, color, fontHeightInPoints);
	}
	
	public POICellStyleKey getCellStyleKey(Workbook wb,String fontName, short boldweight, short color, short fontHeightInPoints) {
		return getCellStyleKey(wb, fontName, boldweight, color, fontHeightInPoints , false, false, Font.SS_NONE, Font.U_NONE, Font.DEFAULT_CHARSET);
	}
	
	public POICellStyleKey getCellStyleKey(Workbook wb,String fontName,short boldweight, short color, short fontHeightInPoints,
			boolean italic, boolean strikeout, short typeOffset, byte underline, int charSet) {
		if (wb != null && StringUtils.isNotEmpty(fontName)) {
			String format =  StringUtils.join(new Object[]{ fontName , boldweight , color , fontHeightInPoints , italic , strikeout , typeOffset , underline , charSet }, "-");
			Map<String, POICellStyleKey> fontMap = COMPLIED_FONTKEY.get(wb);
			if (fontMap == null) {
				fontMap = new HashMap<String, POICellStyleKey>();
				Map<String, POICellStyleKey> existing = COMPLIED_FONTKEY.putIfAbsent(wb, fontMap);
	 			if (existing != null) {
	 				fontMap = existing;
	 			}
			}
			POICellStyleKey fontKey = fontMap.get(format);
			if (fontKey == null) {
				fontKey = new POICellStyleKey(fontName, boldweight, color, fontHeightInPoints, italic, strikeout, typeOffset, underline, charSet);
				fontMap.put(format, fontKey);
				COMPLIED_FONTKEY.replace(wb, fontMap);
			}
			return fontKey;
		}
		return null;
	}

	public Map<POICellStyleKey,CellStyle> getCacheCellStyles(Workbook wb){
		if(wb != null){
			Map<POICellStyleKey,CellStyle> ret = COMPLIED_CELLSTYLE.get(wb);
			if (ret != null) {
				return ret;
			}
			ret = new HashMap<POICellStyleKey, CellStyle>();
			Map<POICellStyleKey,CellStyle> existing = COMPLIED_CELLSTYLE.putIfAbsent(wb, ret);
 			if (existing != null) {
 				ret = existing;
 			}
			return ret;
		}
		return null;
	}
	
	/*public static SimpleDateFormat getDateFormat(String format,Locale locale) {
 		if (StringUtils.isNotEmpty(format)) {
 			String formatKey =  format + locale.toString();
			SimpleDateFormat ret = COMPLIED_FORMAT.get(formatKey);
 			if (ret != null) {
 				return ret;
 			} 
 			ret = new SimpleDateFormat(format, locale );
 			SimpleDateFormat existing = COMPLIED_FORMAT.putIfAbsent(formatKey, ret);
 			if (existing != null) {
 				ret = existing;
 			}
 			return ret;
 		}
 		return null;
 	}*/
	
	public CellStyle getCellStyle(Workbook wb,Font font) {
		return getCellStyle(wb, font.getFontName(), font.getBoldweight(), font.getColor(), 
				font.getFontHeightInPoints() , font.getItalic(), font.getStrikeout(), 
				font.getTypeOffset(), font.getUnderline(), font.getCharSet());
	}
	
	public CellStyle getCellStyle(Workbook wb,String fontName,short fontHeightInPoints) {
		return getCellStyle(wb,fontName, Font.COLOR_NORMAL, fontHeightInPoints);
	}
	
	public CellStyle getCellStyle(Workbook wb,String fontName, short color, short fontHeightInPoints) {
		return getCellStyle(wb,fontName, Font.BOLDWEIGHT_NORMAL, color, fontHeightInPoints);
	}
	
	public CellStyle getCellStyle(Workbook wb,String fontName, short boldweight, short color, short fontHeightInPoints) {
		return getCellStyle(wb,fontName, boldweight, color, fontHeightInPoints , false, false, Font.SS_NONE, Font.U_NONE, Font.DEFAULT_CHARSET);
	}
	
	public CellStyle getCellStyle(Workbook wb,String fontName,short boldweight, short color, short fontHeightInPoints,
			boolean italic, boolean strikeout, short typeOffset, byte underline, int charSet){
		//获取当前Workbook对象的样式缓存
		Map<POICellStyleKey,CellStyle> styleMap = this.getCacheCellStyles(wb);
		//获取对应字体key
		POICellStyleKey cellStyleKey = this.getCellStyleKey( wb, fontName, boldweight, color, fontHeightInPoints, italic, strikeout, typeOffset, underline, charSet);
		//单元格样式
		CellStyle ret = styleMap.get(cellStyleKey);
		if (ret != null) {
			return ret;
		}
		//创建单元格样式
		ret = wb.createCellStyle();
		//设置字体
		ret.setFont(fontFactory.getFont(wb, fontName, boldweight, color, fontHeightInPoints, italic, strikeout, typeOffset, underline, charSet));
		//缓存字体
		styleMap.put(cellStyleKey, ret);
		//替换原有key的数据
		COMPLIED_CELLSTYLE.put(wb, styleMap);
		return ret;
	}
	
	public void destroy(Workbook wb) {
		if(wb != null){
			//清除Workbook对应的字体缓存
			COMPLIED_FONTKEY.remove(wb);
			COMPLIED_CELLSTYLE.remove(wb);
		}
	}
	
}

