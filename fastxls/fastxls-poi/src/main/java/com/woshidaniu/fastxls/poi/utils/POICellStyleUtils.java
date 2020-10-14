/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.fastxls.poi.utils;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor.GREY_25_PERCENT;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.fastxls.poi.cahce.POICellStyleCacheManager;

/**
 *@类名称	: POICellStyleUtils.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 26, 2016 10:14:44 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */

public abstract class POICellStyleUtils {

	protected static Logger LOG = LoggerFactory.getLogger(POICellStyleUtils.class);
	protected static POICellStyleCacheManager cacheManager = POICellStyleCacheManager.getInstance();
	
	//大标题单元格格式（黑色，宋体，加粗，23磅字）
	public static CellStyle getTitleStyle(Workbook wb){
		//获取提示信息字体
		Font font = POIFontUtils.getTitleFont(wb);
		//优先取缓存的样式取不到则创建新的对象，以此减少内存消耗
		CellStyle style = cacheManager.getCellStyle(wb, font);
		//设置其他样式
		setCenter(style);
		setBorder(style);
		setFillColor(wb, style);
		return style;
	}

	//普通字段标题单元格格式（黑色，宋体，加粗，13磅字）
	public static  CellStyle getNormalStyle(Workbook wb){
		//获取提示信息字体
		Font font = POIFontUtils.getNormalFont(wb);
		//优先取缓存的样式取不到则创建新的对象，以此减少内存消耗
		CellStyle style = cacheManager.getCellStyle(wb, font);
		style.setFillBackgroundColor(new GREY_25_PERCENT().getIndex());
		//设置其他样式
		setCenter(style);
		setBorder(style);
		return style;
	}
	
	//特殊字段[如：必填，唯一]标题单元格格式（红色，黑体，加粗，15磅字）
	public static CellStyle getRequiredStyle(Workbook wb){
		//获取提示信息字体
		Font font = POIFontUtils.getRequiredFont(wb);
		//优先取缓存的样式取不到则创建新的对象，以此减少内存消耗
		CellStyle style = cacheManager.getCellStyle(wb, font);
		//设置其他样式
		setCenter(style);
		setBorder(style);
		setFillColor(wb, style);
		return style;
	}
	
	//普通内容字段标题单元格格式（黑色，宋体，不加粗，13磅字）
	public static CellStyle getTextStyle(Workbook wb){
		//获取提示信息字体
		Font font = POIFontUtils.getTextFont(wb);
		//优先取缓存的样式取不到则创建新的对象，以此减少内存消耗
		CellStyle style = cacheManager.getCellStyle(wb, font);
		//设置其他样式
		setCenter(style);
		setBorder(style);
		return style;
	}
	
	//单元格备注内容提示框格式（红色，Arial，12磅字）
	public static  CellStyle getCommentStyle(Workbook wb){
		//获取提示信息字体
		Font font = POIFontUtils.getCommentFont(wb);
		//优先取缓存的样式取不到则创建新的对象，以此减少内存消耗
		CellStyle style = cacheManager.getCellStyle(wb, font);
		//设置其他样式
		setCenter(style);
		setBorder(style);
		return style;
	}
	
	//单元格备注内容提示框格式（红色，华文仿宋，11磅字）
	public static  CellStyle getTipStyle(Workbook wb){
		//获取提示信息字体
		Font font = POIFontUtils.getTipFont(wb);
		//优先取缓存的样式取不到则创建新的对象，以此减少内存消耗
		CellStyle style = cacheManager.getCellStyle(wb, font);
		//设置其他样式
		setCenter(style);
		setBorder(style);
		return style;
	}
	
	public static  CellStyle getCachedStyle(Workbook wb,String fontName,short boldweight, short color, short fontHeightInPoints,
			boolean italic, boolean strikeout, short typeOffset, byte underline, int charSet){
		//获取字体
		Font font = POIFontUtils.getPOIFont(wb, fontName, boldweight, color, fontHeightInPoints, italic, strikeout, typeOffset, underline, charSet);
		//优先取缓存的样式取不到则创建新的对象，以此减少内存消耗
		CellStyle style = cacheManager.getCellStyle(wb, font);
		//设置其他样式
		setCenter(style);
		setBorder(style);
		return style;
	}
	
	public static  CellStyle getCachedStyle(Workbook wb, Font font){
		//优先取缓存的样式取不到则创建新的对象，以此减少内存消耗
		CellStyle style = cacheManager.getCellStyle(wb, font);
		//设置其他样式
		setCenter(style);
		setBorder(style);
		return style;
	}
	
	public static void setFillColor(Workbook wb,CellStyle style){
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());// 设置背景色
		style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());// 设置前景色
	}
	
	public static void setCenter(CellStyle style){
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	}
	
	/**
	 * 设置边框
	 */
	public static void setBorder(CellStyle style){
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setBorderTop(CellStyle.BORDER_THIN);
	}
	
	public static void destroy(Workbook wb) {
		cacheManager.destroy(wb);
	}
	
}
