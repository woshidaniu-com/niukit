/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.fastxls.jexcel.utils;

import jxl.format.Alignment;
import jxl.format.VerticalAlignment;

import com.woshidaniu.fastxls.core.CellStyle;

/**
 *@类名称	: AlignmentUtils.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 25, 2016 1:35:26 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public abstract class AlignmentUtils {

	public Alignment getAlignment(short align) {
		if(align == CellStyle.ALIGN_CENTER){
			return Alignment.CENTRE;
		}else if(align == CellStyle.ALIGN_FILL){
			return Alignment.FILL;
		}else if(align == CellStyle.ALIGN_GENERAL){
			return Alignment.GENERAL;
		}else if(align == CellStyle.ALIGN_JUSTIFY){
			return Alignment.JUSTIFY;
		}else if(align == CellStyle.ALIGN_LEFT){
			return Alignment.LEFT;
		}else if(align == CellStyle.ALIGN_RIGHT){
			return Alignment.RIGHT;
		}else {
			return Alignment.getAlignment(align);
		}
	}
	
	public VerticalAlignment getVerticalAlignment(short vertical) {
		if(vertical == CellStyle.VERTICAL_BOTTOM){
			return VerticalAlignment.BOTTOM;
		}else if(vertical == CellStyle.VERTICAL_CENTER){
			return VerticalAlignment.CENTRE;
		}else if(vertical == CellStyle.VERTICAL_JUSTIFY){
			return VerticalAlignment.JUSTIFY;
		}else if(vertical == CellStyle.VERTICAL_TOP){
			return VerticalAlignment.TOP;
		}else {
			return VerticalAlignment.getAlignment(vertical);
		}
	}
	
}
