/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.fastxls.poi.utils;

import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.fastxls.poi.factory.POIFontFactory;

/**
 *@类名称	: POIFontUtils.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 26, 2016 10:13:43 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public abstract class POIFontUtils {

	protected static Logger LOG = LoggerFactory.getLogger(POIFontUtils.class);
	protected static POIFontFactory fontFactory = POIFontFactory.getInstance();
	public static String HT = "黑体";
	public static String ST = "宋体";
	public static String HWFS = "华文仿宋";
	
	//大标题字体（黑色，宋体，加粗，23磅字）
	public static Font getTitleFont(Workbook wb){
		return fontFactory.getFont(wb, ST, Font.BOLDWEIGHT_BOLD, Font.COLOR_NORMAL, (short)23, false, false, Font.SS_NONE, Font.U_NONE, Font.DEFAULT_CHARSET);
	}
	
	//普通字段标题字体（黑色，宋体，加粗，13磅字）
	public static Font getNormalFont(Workbook wb){
		return fontFactory.getFont(wb, ST, Font.BOLDWEIGHT_BOLD, Font.COLOR_NORMAL, (short)13, false, false, Font.SS_NONE, Font.U_NONE, Font.DEFAULT_CHARSET);
	}
	
	//特殊字段[如：必填，唯一]标题字体（红色，黑体，加粗，15磅字）
	public static Font getRequiredFont(Workbook wb){
		return fontFactory.getFont(wb, HT, Font.BOLDWEIGHT_BOLD, Font.COLOR_RED, (short)15, false, false, Font.SS_NONE, Font.U_NONE, Font.DEFAULT_CHARSET);
	}
	
	//普通内容字段标题字体（黑色，宋体，不加粗，13磅字）
	public static Font getTextFont(Workbook wb){
		return fontFactory.getFont(wb, ST, Font.BOLDWEIGHT_NORMAL, Font.COLOR_NORMAL, (short)13, false, false, Font.SS_NONE, Font.U_NONE, Font.DEFAULT_CHARSET);
	}
	
	//单元格备注内容字体（红色，Arial，12磅字）
	public static Font getCommentFont(Workbook wb){
		return fontFactory.getFont(wb, "Arial", Font.BOLDWEIGHT_NORMAL, Font.COLOR_RED, (short)12, false, false, Font.SS_NONE, Font.U_NONE, Font.DEFAULT_CHARSET);
	}
	
	//单元格备注内容字体（红色，华文仿宋，11磅字）
	public static Font getTipFont(Workbook wb){
		return fontFactory.getFont(wb, "华文仿宋", Font.BOLDWEIGHT_NORMAL, Font.COLOR_RED, (short)11, false, false, Font.SS_NONE, Font.U_NONE, Font.DEFAULT_CHARSET);
	}
	
	public static Font getPOIFont(Workbook wb,String fontName,short boldweight, short color, short fontHeightInPoints,
			boolean italic, boolean strikeout, short typeOffset, byte underline, int charSet){
		return fontFactory.getFont(wb, fontName, boldweight, color, fontHeightInPoints, italic, strikeout, typeOffset, underline, charSet);
	}
	
}
