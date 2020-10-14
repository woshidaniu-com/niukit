package com.woshidaniu.fastpdf.render.helper;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.woshidaniu.fastpdf.render.elements.ItextXMLElement;

public final class WatermarkHelper {
	
	private static WatermarkHelper instance = null;
	private WatermarkHelper(){}
	
	public static WatermarkHelper getInstance(){
		instance = instance==null?new WatermarkHelper():instance;
		return instance;
	}
	
	//图片水印
	public void setWatermark(PdfReader reader,FileOutputStream out,Image img) throws DocumentException, IOException{
		PdfStamper stamp = new PdfStamper(reader,out);
		img.setAbsolutePosition(200, 400);
		PdfContentByte under = stamp.getUnderContent(1);
		under.addImage(img);
		stamp.close();
		reader.close();
	}
	
	//文字水印
	public void setWatermark(PdfReader reader,FileOutputStream out,ItextXMLElement element) throws DocumentException, IOException{
		PdfStamper stamp = new PdfStamper(reader,out);
		PdfContentByte over = stamp.getOverContent(2);
		over.beginText();
		BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI,BaseFont.EMBEDDED);
		over.setFontAndSize(bf, 18);
		over.setTextMatrix(30, 30);
		over.showTextAligned(Element.ALIGN_LEFT, element.text(), 230, 430, 45);
		over.endText();
		reader.close();
	}
	
	//背景图
	public void setWatermarks(PdfReader reader,FileOutputStream out,Image img) throws DocumentException, IOException{
		PdfStamper stamp = new PdfStamper(reader,out);
		img.setAbsolutePosition(0, 0);
		PdfContentByte under2 = stamp.getUnderContent(3);
		under2.addImage(img);
		stamp.close();
		reader.close();
	}
	
}
