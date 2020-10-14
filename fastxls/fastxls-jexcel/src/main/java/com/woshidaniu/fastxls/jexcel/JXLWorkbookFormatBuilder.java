package com.woshidaniu.fastxls.jexcel;

import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WriteException;

import com.woshidaniu.fastxls.jexcel.utils.FormatSort;


public class JXLWorkbookFormatBuilder{
	
	private WritableCellFormat getCellFormat(WritableFont font,Alignment align,VerticalAlignment valign,
			Border border,BorderLineStyle bdStyle) throws WriteException {
		WritableCellFormat format = new WritableCellFormat(font);
		format.setAlignment(align);
		format.setVerticalAlignment(valign);
		format.setBorder(border, bdStyle);
		return format;
	}
	
	public WritableCellFormat titleFormat(int fontSize) throws WriteException{
		return cellFormat(FormatSort.BOLD_BORDER_ALL_THIN, Alignment.CENTRE, VerticalAlignment.CENTRE, fontSize);
	}
	
	public WritableCellFormat cellFormat(FormatSort rank,Alignment align,VerticalAlignment valign,int fontSize) throws WriteException {
		WritableFont font = null;
		WritableCellFormat format = null;
		Border border = null;
		BorderLineStyle style = null;
		switch (rank.ordinal()) {
			case 0:
				font = new WritableFont(WritableFont.TIMES, fontSize);
				break;
			case 1:
				font = new WritableFont(WritableFont.TIMES, fontSize);
				format = getCellFormat(font,align, valign, Border.TOP,BorderLineStyle.THIN);
				break;
			case 2:
				font = new WritableFont(WritableFont.TIMES, fontSize);
				format = new WritableCellFormat(font);
				format.setAlignment(Alignment.RIGHT);
				format.setVerticalAlignment(VerticalAlignment.CENTRE);
				break;
			case 3:
				// 11磅字体大小，加粗，水平居中，垂直居中，四边最细黑边框
				format = new WritableCellFormat(font);
				format.setAlignment(Alignment.CENTRE);
				format.setVerticalAlignment(VerticalAlignment.CENTRE);
				break;
			case 4:
				// 11磅字体大小，水平居中，垂直居中，无边框
				font = new WritableFont(WritableFont.TIMES, 11);
				format = new WritableCellFormat(font);
				format.setAlignment(Alignment.CENTRE);
				format.setVerticalAlignment(VerticalAlignment.CENTRE);
				break;
			case 5:
				// 11磅字体大小，水平居中，垂直居中，底部最细黑边框
				font = new WritableFont(WritableFont.TIMES, 11);
				format = new WritableCellFormat(font);
				format.setAlignment(Alignment.CENTRE);
				format.setVerticalAlignment(VerticalAlignment.CENTRE);
				format.setBorder(Border.NONE, BorderLineStyle.THIN);
				break;
			case 6:
				// 11磅字体大小，不加粗，水平居中，垂直居中，四边最细黑边框
				font = new WritableFont(WritableFont.TIMES, 11);
				format = new WritableCellFormat(font);
				format.setAlignment(Alignment.CENTRE);
				format.setVerticalAlignment(VerticalAlignment.CENTRE);
				
				format.setBorder(Border.ALL, BorderLineStyle.THIN);
				break;
			case 7:
				
				font = new WritableFont(WritableFont.TIMES, 11, WritableFont.BOLD);
				// 14磅字体大小，水平居中，垂直居中
				font = new WritableFont(WritableFont.TIMES, 14);
				format = new WritableCellFormat(font);
				format.setAlignment(Alignment.RIGHT);
				format.setVerticalAlignment(VerticalAlignment.CENTRE);
				break;
			}
			format = getCellFormat(font,align, valign, border,BorderLineStyle.THIN);
		return format;
	}


}
