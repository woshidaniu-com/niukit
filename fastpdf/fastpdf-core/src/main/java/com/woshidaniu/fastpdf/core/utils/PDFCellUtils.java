package com.woshidaniu.fastpdf.core.utils;

import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.woshidaniu.fastpdf.core.model.PdfPCellx;
/**
 * 
 *@类名称	: PDFCellUtils.java
 *@类描述	：打印公共类<br/>
 * 原理:使用iText生成pdf,并调用本地pdf解析器进行打印.<br/>
 * 常见问题:由于本地pdf解析器可能存在差异,可能会有不同的效果,也可能出现无法打印的可能.<br/>
 *@创建人	：kangzhidong
 *@创建时间	：Mar 16, 2016 2:01:37 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public abstract class PDFCellUtils {

	/**
	 * 构造一个自定义的cell单元格内容(默认居中对齐,默认字体)
	 * @param content 单元格内容
	 * @return com.lowagie.text.pdf.PdfPCell
	 * 
	 */
	public static PdfPCell getCell(String content) {
		return getCell(content, 1, PdfPCell.ALIGN_CENTER,PdfPCell.ALIGN_MIDDLE, new Font());
	}

	public static PdfPCell getCell(Phrase phrase) {
		return new PdfPCell(phrase);
	}

	public static PdfPCell getCell(Image image) {
		return new PdfPCell(image);
	}

	public static PdfPCell getCell(PdfPCell cell) {
		return new PdfPCell(cell);
	}

	public static PdfPCell getCell(PdfPTable table, PdfPCell style) {
		return new PdfPCell(table, style);
	}

	/**
	 * 构造一个自定义的cell单元格内容(默认居中对齐)
	 * @param content  单元格内容
	 * @param font 单元格字体
	 * @return com.lowagie.text.pdf.PdfPCell
	 */
	public static PdfPCell getCell(String content, Font font) {
		return getCell(content, 1, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_MIDDLE, font);
	}

	/**
	 * 构造一个自定义的cell单元格内容
	 * 
	 * @param content 单元格内容
	 * @param align 横向对齐
	 * @param valign  纵向对齐
	 * @param font 单元格字体
	 * @return com.lowagie.text.pdf.CellElement
	 * 
	 */
	public static PdfPCell getCell(String content, int align, int valign, Font font) {
		return getCell(content, 1, align, valign, font);
	}

	/**
	 * 构造一个自定义的cell单元格内容
	 * 
	 * @param content 单元格内容
	 * @param colspan 合并列
	 * @param font  单元格字体
	 * @return com.lowagie.text.pdf.CellElement
	 * 
	 */
	public static PdfPCell getCell(String content, int colspan, Font font) {
		return getCell(content, colspan, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_MIDDLE, font);
	}

	/**
	 * 构造一个自定义的内嵌表cell单元格内容(默认居中对齐)
	 * 
	 * @param table 单元格内容
	 * @return com.lowagie.text.pdf.CellElement
	 * 
	 */
	public static PdfPCell getCell(PdfPTable table) {
		return getCell(table, 1, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_MIDDLE);
	}

	/**
	 * 构造一个自定义的内嵌表cell单元格内容
	 * 
	 * @param table
	 *            单元格内容
	 * @param align
	 *            横向对齐
	 * @param valign
	 *            纵向对齐
	 * @return com.lowagie.text.pdf.CellElement
	 * 
	 */
	public static PdfPCell getCell(PdfPTable table, int align, int valign) {
		return getCell(table, 1, align, valign);
	}

	/**
	 * 构造一个自定义的内嵌表cell单元格内容
	 * 
	 * @param content
	 *            单元格内容
	 * @param colspan
	 *            合并列
	 * @return com.lowagie.text.pdf.CellElement
	 * 
	 */
	public static PdfPCell getCell(PdfPTable table, int colspan) {
		return getCell(table, colspan, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_MIDDLE);
	}

	/**
	 * 构造一个自定义的cell单元格内容
	 * 
	 * @param content 单元格内容
	 * @param colspan 合并列
	 * @param align 横向对齐
	 * @param valign 纵向对齐
	 * @param font 单元格字体
	 * @return com.lowagie.text.pdf.CellElement
	 * 
	 */
	public static PdfPCell getCell(String content, int colspan, int align,int valign, Font font) {
		PdfPCell cell = null;
		Paragraph paragraph = null;
		// 使用自定义字体
		paragraph = new Paragraph(content == null ? "" : content, font);
		cell = new PdfPCell(paragraph);
		if (colspan > 1) {
			cell.setColspan(colspan);
		}
		// 设置对齐
		cell.setHorizontalAlignment(align);
		cell.setVerticalAlignment(valign);
		cell.setMinimumHeight(15);
		return cell;
	}

	public static PdfPCell getCell(Paragraph content, int colspan, int align, int valign) {
		PdfPCell cell = null;
		Paragraph paragraph = content;
		// 使用自定义字体
		cell = new PdfPCell(paragraph);
		if (colspan > 1) {
			cell.setColspan(colspan);
		}
		// 设置对齐
		cell.setHorizontalAlignment(align);
		cell.setVerticalAlignment(valign);
		cell.setMinimumHeight(15);
		return cell;
	}

	/**
	 * 构造一个自定义的cell<br/>
	 * 由于使用CellElement无法合并行,所以此方法中一些属性无法使用.
	 * 
	 * @param content 单元格内容
	 * @param colspan 合并列
	 * @param rowspan 合并行 
	 * @param align  横向对齐
	 * @param valign 纵向对齐
	 * @param font 单元格字体
	 * @return 生成的单元格
	 * 
	 */
	public static PdfPCell getCell(String content, int colspan, int rowspan, int align, int valign, Font font) {
		PdfPCell cell = null;
		Paragraph paragraph = null;
		// 使用自定义字体
		paragraph = new Paragraph(content == null ? "" : content, font);
		cell = new PdfPCell(paragraph);
		// 设置colspan,同样的方法可以设置rowspan
		if (colspan > 1) {
			cell.setColspan(colspan);
		}
		if (rowspan > 1) {
			cell.setRowspan(rowspan);
		}
		// 设置对齐
		cell.setHorizontalAlignment(align);
		cell.setVerticalAlignment(valign);
		cell.setMinimumHeight(15);
		return cell;
	}

	/**
	 * 构造一个自定义的图片cell单元格
	 * 
	 * @param img 单元格内容
	 * @param colspan 合并列
	 * @param align 横向对齐
	 * @param valign 纵向对齐
	 * @return 生成的单元格
	 * 
	 */
	public static PdfPCell getCell(Image img, int colspan, int rowspan,int align, int valign) {
		PdfPCell cell = null;
		cell = new PdfPCell(img);
		if (rowspan > 1) {
			cell.setRowspan(rowspan);
		}
		if (colspan > 1) {
			cell.setColspan(colspan);
		}
		// 设置对齐
		cell.setHorizontalAlignment(align);
		cell.setVerticalAlignment(valign);
		cell.setMinimumHeight(15);
		return cell;
	}

	/**
	 * 构造一个自定义的图片cell单元格
	 * 
	 * @param img 单元格内容
	 * @param colspan 合并列
	 * @param align 横向对齐
	 * @param valign 纵向对齐
	 * @return 生成的单元格
	 * 
	 */
	public static PdfPCell getCell(Image img, int colspan, int align, int valign) {
		PdfPCell cell = null;
		cell = new PdfPCell(img);
		if (colspan > 1) {
			cell.setColspan(colspan);
		}
		// 设置对齐
		cell.setHorizontalAlignment(align);
		cell.setVerticalAlignment(valign);
		cell.setMinimumHeight(15);
		return cell;
	}

	/**
	 * 构造一个内嵌table的单元格<br/>
	 * 由于CellElement无法实现跨行功能,所以需要内嵌一个table.以实现复杂的跨行功能.
	 * 
	 * @param table 内嵌表格
	 * @param colspan 合并列
	 * @param align 横向对齐
	 * @param valign 纵向对齐
	 * @return
	 */
	public static PdfPCell getCell(PdfPTable table, int colspan, int align, int valign) {
		PdfPCell cell = null;
		cell = new PdfPCell(table);
		if (colspan > 1) {
			cell.setColspan(colspan);
		}
		// 设置对齐
		cell.setHorizontalAlignment(align);
		cell.setVerticalAlignment(valign);
		cell.setMinimumHeight(15);
		return cell;
	}

	/**
	 * 获得空单元格
	 * 
	 * @return
	 */
	public static PdfPCell getEmptyCell() {
		return getEmptyCell(1);
	}

	/**
	 * 获得空单元格
	 * 
	 * @param colspan
	 * @return
	 */
	public static PdfPCell getEmptyCell(int colspan) {
		PdfPCell cell = null;
		Paragraph paragraph = null;
		paragraph = new Paragraph("");
		cell = new PdfPCell(paragraph);
		if (colspan > 1) {
			cell.setColspan(colspan);
		}
		cell.setMinimumHeight(15);
		return cell;
	}

	public static void addEmptyRow(Document document,float percentage,int height) throws DocumentException{
		//空白行
    	PdfPTable emptyRow = new PdfPTable(1);   
    	emptyRow.setWidthPercentage(percentage);   
    	emptyRow.getDefaultCell().setBorderWidthLeft(0);   
    	emptyRow.getDefaultCell().setBorderWidthRight(0);   
    	emptyRow.getDefaultCell().setBorderWidthTop(0);
    	emptyRow.getDefaultCell().setBorderWidthBottom(0);
    	emptyRow.getDefaultCell().setFixedHeight(height);
    	emptyRow.addCell("  ");  
    	document.add(emptyRow);
	}
	
	public static void addLineRow(Document document,float percentage,int lineWidth,int height) throws DocumentException{
		document.add(new Paragraph(""));
		PdfPTable tt = new PdfPTable(2);   
		tt.setWidthPercentage(percentage);   
		tt.getDefaultCell().setBorderWidthLeft(0);   
		tt.getDefaultCell().setBorderWidthRight(0);   
		tt.getDefaultCell().setBorderWidthTop(0);
		tt.getDefaultCell().setBorderWidthBottom(lineWidth);
		tt.getDefaultCell().setFixedHeight(height/2);
		tt.addCell("  ");   
		tt.addCell("  ");  
		document.add(tt);
		PdfPTable tt2 = new PdfPTable(2);   
		tt2.setWidthPercentage(percentage);   
		tt2.getDefaultCell().setBorderWidthLeft(0);   
		tt2.getDefaultCell().setBorderWidthRight(0);   
		tt2.getDefaultCell().setBorderWidthTop(0);  
		tt2.getDefaultCell().setBorderWidthBottom(0);
		tt2.getDefaultCell().setFixedHeight(height/2);
		tt2.addCell("  ");   
		tt2.addCell("  ");  
		document.add(tt2);
	}
	
	/**
	 * 字体:黑体(要获得字体库simhei.ttf)
	 */
	public static final int HT = 1;
	/**
	 * 字体:宋体(要获得itext亚洲字体库)
	 */
	public static final int ST = 2;
	/**
	 * 字体:宋体(要获得标准宋体字体库)
	 */
	public static final int ST2 = 3;
	
	/**
	 * 加粗
	 */
	public static final int BOLD = Font.BOLD;
	/**
	 * 普通
	 */
	public static final int NORMAL = Font.NORMAL;
	
	/**
	 * 获得指定字体库类型(默认库位置,若未加入字体库,可能无法找到),指定大小,指定字体类型的font.
	 * 
	 * @param type <ul><li>PrintUtil.HT:黑体</li><li>PrintUtil.ST:宋体</li></ul>
	 * @param size number
	 * @param style <ul><li>Font.BOLD:加粗</li><li>Font.ITALIC:斜体</li><li>Font.NORMAL:普通</li></ul>
	 * @return com.lowagie.text.Font
	 * @throws DocumentException
	 * @throws IOException
	 */
	public static Font getFont(int type, int size, int style) throws DocumentException, IOException {
		Font font;
		switch(type) {
		case HT:
			font = new Font(BaseFont.createFont(System.getProperty("filepath") + "fonts/simhei.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED), size, style);
			break;
		case ST:
			font = new Font(BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED), size, style);
			break;
		case ST2:
			font = new Font(BaseFont.createFont(System.getProperty("filepath") + "fonts/simsun.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED), size, style);
			break;
		default:
			font = new Font();
			break;
		}
		return font;
	}
	
	/**
	 * 获得指定字体库类型(指定字体库路径),指定大小,指定字体类型的font.
	 * 
	 * @param storePath 字体库路径
	 * @param writingType 统一字符编码编写格式:<ul><li>横向(horizontal):IDENTITY_H</li><li>纵向(vertical):IDENTITY_V</li></ul>
	 * <ul><li>BaseFont.IDENTITY_H:"Identity-H"</li><li>BaseFont.IDENTITY_V:"Identity-V";</li></ul>
	 * @param size number
	 * @param style <ul><li>Font.BOLD:加粗</li><li>Font.ITALIC:斜体</li><li>Font.NORMAL:普通</li></ul>
	 * @return com.lowagie.text.Font
	 * @throws DocumentException
	 * @throws IOException
	 */
	public static Font getFont(String storePath, String writingType, int size, int style) throws  DocumentException, IOException {
		Font font = new Font(BaseFont.createFont(storePath, writingType, BaseFont.NOT_EMBEDDED), size, style);
		return font;
	}
	
	/**
	 * 构造一个自定义的cell单元格内容(默认居中对齐,默认字体)
	 * 
	 * @param content 单元格内容
	 * @return com.lowagie.text.pdf.PdfPCellx
	 * 
	 */
	public static PdfPCellx makeCell(String content) {
		return makeCell(content, 1, PdfPCellx.ALIGN_CENTER, PdfPCellx.ALIGN_MIDDLE, new Font());
    }
	
	/**
	 * 构造一个自定义的cell单元格内容(默认居中对齐)
	 * 
	 * @param content 单元格内容
	 * @param font 单元格字体
	 * @return com.lowagie.text.pdf.PdfPCellx
	 * 
	 */
	public static PdfPCellx makeCell(String content, Font font) {
		return makeCell(content, 1, PdfPCellx.ALIGN_CENTER, PdfPCellx.ALIGN_MIDDLE, font);
    }
	
	/**
	 * 构造一个自定义的cell单元格内容
	 * 
	 * @param content 单元格内容
	 * @param align 横向对齐
	 * @param valign 纵向对齐
	 * @param font 单元格字体
	 * @return com.lowagie.text.pdf.PdfPCellx
	 * 
	 */
	public static PdfPCellx makeCell(String content, int align, int valign, Font font) {
		return makeCell(content, 1, align, valign, font);
    }
	
	/**
	 * 构造一个自定义的cell单元格内容
	 * 
	 * @param content 单元格内容
	 * @param colspan 合并列
	 * @param font 单元格字体
	 * @return com.lowagie.text.pdf.PdfPCellx
	 * 
	 */
	public static PdfPCellx makeCell(String content, int colspan, Font font) {
		return makeCell(content, colspan, PdfPCellx.ALIGN_CENTER, PdfPCellx.ALIGN_MIDDLE, font);
    }
	
	/**
	 * 构造一个自定义的内嵌表cell单元格内容(默认居中对齐)
	 * 
	 * @param table 单元格内容
	 * @return com.lowagie.text.pdf.PdfPCellx
	 * 
	 */
	public static PdfPCellx makeCell(PdfPTable table) {
		return makeCell(table, 1, PdfPCellx.ALIGN_CENTER, PdfPCellx.ALIGN_MIDDLE);
    }
	
	/**
	 * 构造一个自定义的内嵌表cell单元格内容
	 * 
	 * @param table 单元格内容
	 * @param align 横向对齐
	 * @param valign 纵向对齐
	 * @return com.lowagie.text.pdf.PdfPCellx
	 * 
	 */
	public static PdfPCellx makeCell(PdfPTable table, int align, int valign) {
		return makeCell(table, 1, align, valign);
    }
	
	/**
	 * 构造一个自定义的内嵌表cell单元格内容
	 * 
	 * @param content 单元格内容
	 * @param colspan 合并列
	 * @return com.lowagie.text.pdf.PdfPCellx
	 * 
	 */
	public static PdfPCellx makeCell(PdfPTable table, int colspan) {
		return makeCell(table, colspan, PdfPCellx.ALIGN_CENTER, PdfPCellx.ALIGN_MIDDLE);
    }
	
	/**
	 * 构造一个自定义的cell单元格内容
	 * 
	 * @param content 单元格内容
	 * @param colspan 合并列
	 * @param align 横向对齐
	 * @param valign 纵向对齐
	 * @param font 单元格字体
	 * @return com.lowagie.text.pdf.PdfPCellx
	 * 
	 */
	public synchronized static PdfPCellx makeCell(String content, int colspan, int align, int valign, Font font) {
		PdfPCellx cell = null;
		Paragraph paragraph = null;
        //使用自定义字体
        paragraph=new Paragraph(content, font);
        cell= new PdfPCellx(paragraph);
        if(colspan > 1){
            cell.setColspan(colspan);
        }
        //设置对齐
        cell.setHorizontalAlignment(align);
        cell.setVerticalAlignment(valign);
        cell.setMinimumHeight(15);
        return cell;
    }
	
	public synchronized static PdfPCellx makeCell(Paragraph content, int colspan, int align, int valign) {
		PdfPCellx cell = null;
		Paragraph paragraph =content;
        //使用自定义字体
        cell= new PdfPCellx(paragraph);
        if(colspan > 1){
            cell.setColspan(colspan);
        }
        //设置对齐
        cell.setHorizontalAlignment(align);
        cell.setVerticalAlignment(valign);
        cell.setMinimumHeight(15);
        return cell;
    }
	
	/**
	 * 构造一个自定义的cell<br/>
	 * 由于使用PdfPCellx无法合并行,所以此方法中一些属性无法使用.
	 * 
	 * @param content 单元格内容
	 * @param colspan 合并列
	 * @param rowspan 合并行 (deprecated)
	 * @param align 横向对齐
	 * @param valign 纵向对齐
	 * @param font 单元格字体
	 * @return 生成的单元格
	 * 
	 */
	public static PdfPCellx makeCell(String content, int colspan, int rowspan, int align, int valign, Font font) {
		PdfPCellx cell = null;
		Paragraph paragraph =null;
        //使用自定义字体
        paragraph = new Paragraph(content, font);
        cell = new PdfPCellx(paragraph);
        //设置colspan,同样的方法可以设置rowspan
        if(colspan > 1){
            cell.setColspan(colspan);
        }
        if(rowspan > 1) {
        	cell.setRowspan(rowspan);
        }
        //设置对齐
        cell.setHorizontalAlignment(align);
        cell.setVerticalAlignment(valign);
        cell.setMinimumHeight(15);
        return cell;
    }
	
	/**
	 * 构造一个自定义的图片cell单元格
	 * 
	 * @param img 单元格内容
	 * @param colspan 合并列
	 * @param align 横向对齐
	 * @param valign 纵向对齐
	 * @return 生成的单元格
	 * 
	 */
	public synchronized static PdfPCellx makeCell(Image img, int colspan,int rowspan, int align, int valign) {
		PdfPCellx cell = null;
        cell = new PdfPCellx(img);
        if(rowspan > 1) {
        	cell.setRowspan(rowspan);
        }
        if(colspan > 1){
            cell.setColspan(colspan);
        }
        //设置对齐
        cell.setHorizontalAlignment(align);
        cell.setVerticalAlignment(valign);
        cell.setMinimumHeight(15);
        return cell;
    }
	
	/**
	 * 构造一个自定义的图片cell单元格
	 * 
	 * @param img 单元格内容
	 * @param colspan 合并列
	 * @param align 横向对齐
	 * @param valign 纵向对齐
	 * @return 生成的单元格
	 * 
	 */
	public synchronized static PdfPCellx makeCell(Image img, int colspan, int align, int valign) {
		PdfPCellx cell = null;
        cell=new PdfPCellx(img);
        if(colspan > 1){
            cell.setColspan(colspan);
        }
        //设置对齐
        cell.setHorizontalAlignment(align);
        cell.setVerticalAlignment(valign);
        cell.setMinimumHeight(15);
        return cell;
    }
	
	/**
	 * 构造一个内嵌table的单元格<br/>
	 * 由于PdfPCellx无法实现跨行功能,所以需要内嵌一个table.以实现复杂的跨行功能.
	 * 
	 * @param table 内嵌表格
	 * @param colspan 合并列
	 * @param align 横向对齐
	 * @param valign 纵向对齐
	 * @return
	 */
	public synchronized static PdfPCellx makeCell(PdfPTable table, int colspan, int align, int valign) {
		PdfPCellx cell = null;
        cell=new PdfPCellx(table);
        if(colspan > 1){
            cell.setColspan(colspan);
        }
        //设置对齐
        cell.setHorizontalAlignment(align);
        cell.setVerticalAlignment(valign);
        cell.setMinimumHeight(15);
        return cell;
	}
	
	/**
	 * 获得空单元格
	 * 
	 * @return
	 */
	public static PdfPCellx makeEmptyCell() {
		return makeEmptyCell(1);
	}
	
	/**
	 * 获得空单元格
	 * 
	 * @param colspan
	 * @return
	 */
	public static PdfPCellx makeEmptyCell(int colspan) {
		PdfPCellx cell = null;
		Paragraph paragraph =null;
		paragraph = new Paragraph("");
        cell=new PdfPCellx(paragraph);
        if(colspan > 1){
            cell.setColspan(colspan);
        }
        cell.setMinimumHeight(15);
        return cell;
	}
	 
}
