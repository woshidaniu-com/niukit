package com.woshidaniu.fastpdf.render.events;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTransition;
import com.itextpdf.text.pdf.PdfWriter;
import com.woshidaniu.fastpdf.render.elements.ItextXMLElement;

 /**
 * @package com.woshidaniu.fastpdf.render.events
 * @className: PDFPageEvent
 * @description: TODO
 * @author : kangzhidong
 * @date : 2014-1-15
 * @time : 下午1:33:59 
 */

//public class PDFPageEvent extends PdfPageEventHelper {
	
	protected ItextXMLElement element;
	
	public PDFPageEvent(ItextXMLElement element) {
		this.element = element;
	}


	/**
	 * 
	 * @description: 设置幻灯片放映 
	 * @author : kangzhidong
	 * @date : 2014-1-14
	 * @time : 下午1:59:58 
	 * @param writer
	 * @param document
	 */
	 public void onStartPage(PdfWriter writer, Document document) { 
         
		 
		 writer.setTransition(new PdfTransition(PdfTransition.DISSOLVE, 3)); 
         writer.setDuration(5);//间隔时间 
	 } 


	/**
	 * 
	 * @description: 设置Header, Footer 
	 * @author : kangzhidong
	 * @date : 2014-1-14
	 * @time : 下午2:00:32 
	 * @param writer
	 * @param document
	 */
	public void onEndPage(PdfWriter writer, Document document) {
		PdfContentByte cb = writer.getDirectContent();
		cb.saveState();
		cb.beginText();
		BaseFont bf = null;
		try {
			bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED);
		} catch (Exception e) {
			e.printStackTrace();
		}
		cb.setFontAndSize(bf, 10);
		
		// Header
		float x = document.top(-20);
		// 左
		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "H-Left",document.left(),x, 0);

		// 中
		cb.showTextAligned(PdfContentByte.ALIGN_CENTER,
		writer.getPageNumber() + " page",(document.right() + document.left()) / 2,x, 0);

		// 右
		cb.showTextAligned(PdfContentByte.ALIGN_RIGHT,"H-Right",document.right(), x, 0);

		
		// Footer
		float y = document.bottom(-20);
		// 左
		cb.showTextAligned(PdfContentByte.ALIGN_LEFT,"F-Left",document.left(), y, 0);
		// 中
		cb.showTextAligned(PdfContentByte.ALIGN_CENTER,writer.getPageNumber() + " page",(document.right() + document.left()) / 2,y, 0);
		// 右
		cb.showTextAligned(PdfContentByte.ALIGN_RIGHT, "F-Right",document.right(), y, 0);

		cb.endText();
		cb.restoreState();
	}
}



