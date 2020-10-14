/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.fastxls.jexcel.utils;

import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.WritableCellFormat;
import jxl.write.WriteException;

import com.woshidaniu.fastxls.jexcel.cache.JXLCellFormatCacheManager;

/**
 *@类名称	: CellFormatUtils.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 25, 2016 1:49:57 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public abstract class JXLCellFormatUtils {

	public static String HT = "黑体";
	public static String ST = "宋体";
	
	protected static JXLCellFormatCacheManager cacheManager = JXLCellFormatCacheManager.getInstance();

	public static WritableCellFormat getCellFormat(String name, int size, boolean bold,
			Alignment alignment, VerticalAlignment valignment,
			Colour background, Border border, BorderLineStyle borderLine) {
		return cacheManager.getCellFormat(name, size, bold, alignment, valignment, background, border, borderLine);
	}
	
	//大标题单元格格式（黑色，黑体，加粗，23磅字）
	public static WritableCellFormat getTitleCellFormat() {
		return getCellFormat(HT, 23, true, Alignment.CENTRE, VerticalAlignment.CENTRE, Colour.GREY_50_PERCENT, Border.ALL, BorderLineStyle.THIN);
	}
	
	//普通字段标题单元格格式（黑色，黑体，不加粗，13磅字）
	public static WritableCellFormat getNormalCellFormat(){
		return getCellFormat(HT, 13, false, Alignment.CENTRE, VerticalAlignment.CENTRE, Colour.GREY_25_PERCENT, Border.ALL, BorderLineStyle.THIN);
	}
	
	//特殊字段[如：必填，唯一]标题单元格格式（红色，黑体，加粗，15磅字）
	public static WritableCellFormat getRequiredCellFormat(){
		return getCellFormat(HT, 15, true, Alignment.CENTRE, VerticalAlignment.CENTRE, Colour.GREY_25_PERCENT, Border.ALL, BorderLineStyle.THIN);
	}
	
	//普通内容字段标题单元格格式（黑色，宋体，13磅字）
	public static WritableCellFormat getTextCellFormat(){
		return getCellFormat(ST, 13, false, Alignment.CENTRE, VerticalAlignment.CENTRE, Colour.WHITE, Border.ALL, BorderLineStyle.THIN);
	}
	
	//大标题单元格格式（黑色，黑体，加粗，23磅字）
	public static WritableCellFormat getNumberCellFormat(String format) throws WriteException{
		WritableCellFormat style = new WritableCellFormat(new jxl.write.NumberFormat(format));
        // 设置居右
		style.setAlignment(Alignment.RIGHT);
		style.setVerticalAlignment(VerticalAlignment.CENTRE);
		// 设置单元格的背景颜色
		style.setBackground(Colour.GREY_25_PERCENT);
		 // 设置边框线
		style.setBorder(Border.ALL, BorderLineStyle.THIN);	
		return style;
	}
	
	public static WritableCellFormat getDateCellFormat(String format) throws WriteException{
		WritableCellFormat style = new WritableCellFormat(new jxl.write.DateFormat(format));
        // 设置居右
		style.setAlignment(Alignment.RIGHT);
		style.setVerticalAlignment(VerticalAlignment.CENTRE);
		// 设置单元格的背景颜色
		style.setBackground(Colour.GREY_25_PERCENT);
		 // 设置边框线
		style.setBorder(Border.ALL, BorderLineStyle.THIN);	
		return style;
	}

}
