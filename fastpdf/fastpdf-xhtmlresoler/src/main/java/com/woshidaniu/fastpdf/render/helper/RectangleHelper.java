package com.woshidaniu.fastpdf.render.helper;

import java.util.HashMap;
import java.util.Map;

import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.woshidaniu.fastpdf.render.elements.ItextXMLElement;
import com.woshidaniu.fastpdf.render.style.PDFStyleTransformer;

 /**
  * 
  *@类名称	: RectangleHelper.java
  *@类描述	：
  *@创建人	：kangzhidong
  *@创建时间	：Mar 29, 2016 7:49:28 PM
  *@修改人	：
  *@修改时间	：
  *@版本号	:v1.0
  */
public final class RectangleHelper {
	
	protected static final Map<String,Rectangle> rectangles = new HashMap<String, Rectangle>();
	private static RectangleHelper instance = null;
	private RectangleHelper(){}
	
	static{
		rectangles.put("LETTER", PageSize.LETTER);
    	rectangles.put("NOTE", PageSize.NOTE);
    	rectangles.put("LEGAL", PageSize.LEGAL);
    	rectangles.put("TABLOID", PageSize.TABLOID);
    	rectangles.put("EXECUTIVE", PageSize.EXECUTIVE);
    	rectangles.put("POSTCARD", PageSize.POSTCARD);
    	rectangles.put("A0", PageSize.A0);
    	rectangles.put("A1", PageSize.A1);
    	rectangles.put("A2", PageSize.A2);
    	rectangles.put("A3", PageSize.A0);
    	rectangles.put("A4", PageSize.A4);
    	rectangles.put("A5", PageSize.A5);
    	rectangles.put("A6", PageSize.A6);
    	rectangles.put("A7", PageSize.A7);
    	rectangles.put("A8", PageSize.A0);
    	rectangles.put("A9", PageSize.A9);
    	rectangles.put("A10", PageSize.A10);
    	rectangles.put("B0", PageSize.B0);
    	rectangles.put("B1", PageSize.B1);
    	rectangles.put("B2", PageSize.B2);
    	rectangles.put("B3", PageSize.B3);
    	rectangles.put("B4", PageSize.B4);
    	rectangles.put("B5", PageSize.B5);
    	rectangles.put("B6", PageSize.B6);
    	rectangles.put("B7", PageSize.B7);
    	rectangles.put("B8", PageSize.B8);
    	rectangles.put("B9", PageSize.B9);
    	rectangles.put("B10", PageSize.B10);
    	rectangles.put("ARCH_E", PageSize.ARCH_E);
    	rectangles.put("ARCH_D", PageSize.ARCH_D);
    	rectangles.put("ARCH_C", PageSize.ARCH_C);
    	rectangles.put("ARCH_B", PageSize.ARCH_B);
    	rectangles.put("ARCH_A", PageSize.ARCH_A);
    	rectangles.put("FLSA", PageSize.FLSA);
    	rectangles.put("FLSE", PageSize.FLSE);
    	rectangles.put("HALFLETTER", PageSize.HALFLETTER);
    	rectangles.put("_11X17", PageSize._11X17);
    	rectangles.put("ID_1", PageSize.ID_1);
    	rectangles.put("ID_2", PageSize.ID_2);
    	rectangles.put("ID_3", PageSize.ID_3);
    	rectangles.put("LEDGER", PageSize.LEDGER);
    	rectangles.put("CROWN_QUARTO", PageSize.CROWN_QUARTO);
    	rectangles.put("LARGE_CROWN_QUARTO", PageSize.LARGE_CROWN_QUARTO);
    	rectangles.put("DEMY_QUARTO", PageSize.DEMY_QUARTO);
    	rectangles.put("ROYAL_QUARTO", PageSize.ROYAL_QUARTO);
    	rectangles.put("CROWN_OCTAVO", PageSize.CROWN_OCTAVO);
    	rectangles.put("LARGE_CROWN_OCTAVO", PageSize.LARGE_CROWN_OCTAVO);
    	rectangles.put("DEMY_OCTAVO", PageSize.DEMY_OCTAVO);
    	rectangles.put("ROYAL_OCTAVO", PageSize.ROYAL_OCTAVO);
    	rectangles.put("SMALL_PAPERBACK", PageSize.SMALL_PAPERBACK);
    	rectangles.put("PENGUIN_SMALL_PAPERBACK", PageSize.PENGUIN_SMALL_PAPERBACK);
    	rectangles.put("PENGUIN_LARGE_PAPERBACK", PageSize.PENGUIN_LARGE_PAPERBACK);
    }
	
	public static RectangleHelper getInstance(){
		instance = instance==null?new RectangleHelper():instance;
		return instance;
	}
	
	public Rectangle getRectangle(ItextXMLElement element){
		String rotate = element.attr("rotate"); 
		String pageSize = element.attr("pageSize");
		//页面大小 
		Rectangle rect = rectangles.get(pageSize);
				  rect = BlankUtils.isBlank(rect)?PageSize.A4:rect;
				  rect = "true".equalsIgnoreCase(rotate)?rect.rotate():rect;
		//样式渲染
	    Rectangle rectangle = new Rectangle(rect);
	    ElementStyleRender.getInstance(PDFStyleTransformer.getInstance()).render(rectangle, element);	  
	    //返回渲染后的Rectangle对象
		return rectangle; 
	}
	
}



