package com.woshidaniu.fastpdf.render.helper;

import java.util.Iterator;
import java.util.List;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.woshidaniu.fastpdf.render.elements.ItextXMLElement;
import com.woshidaniu.fastpdf.render.resolver.ItextFontResolver;
import com.woshidaniu.fastpdf.render.style.PDFStyleTransformer;
public class PDFCaptionHelper{

	private static PDFCaptionHelper instance = null;
	private PDFCaptionHelper(){}
	
	public static PDFCaptionHelper getInstance(){
		if (instance == null) {
			instance = new PDFCaptionHelper();
		}
		return instance;
	}
	
	public <T> PdfPTable getCaption(ItextXMLElement element,T data) {
		PdfPTable caption = null;
		PdfPCell cell = null;
		if(element.isElement("caption")){
			caption = new PdfPTable(1);
			if(element.hasChild()){
				cell = new PdfPCell();
				//迭代子元素
				Iterator<Element> itr = element.getChildren().iterator();
				while (itr.hasNext()) {
					ItextXMLElement cellElement = (ItextXMLElement)itr.next();
					//迭代渲染后的子元素
					List<Object> elements = IteratorHelper.getInstance().iterator(cellElement, data);
					for (Object elementx : elements) {
						if(elementx instanceof Chunk){
							cell.addElement((Chunk)elementx);
						}else if(elementx instanceof Paragraph){
							cell.addElement((Paragraph)elementx);
						}else if(elementx instanceof LineSeparator){
							cell.addElement((LineSeparator)elementx);
						}
					}
				}
			}else{
				Font font = ItextFontResolver.getInstance().getFont(element);
				cell = new PdfPCell(element.getPhrase(font,data));
			}
			if(caption!=null&&cell!=null){
				caption.addCell(cell);
				ElementStyleRender.getInstance(PDFStyleTransformer.getInstance()).render(caption, element);
			}
		}
		return caption;
	}
}
