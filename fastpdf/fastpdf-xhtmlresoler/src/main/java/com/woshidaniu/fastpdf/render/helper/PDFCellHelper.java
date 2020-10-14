package com.woshidaniu.fastpdf.render.helper;

import java.util.Iterator;
import java.util.List;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.woshidaniu.fastpdf.render.elements.ItextXMLElement;
import com.woshidaniu.fastpdf.render.resolver.ItextFontResolver;
import com.woshidaniu.fastpdf.render.style.PDFStyleTransformer;
public class PDFCellHelper{

	private static PDFCellHelper instance = null;
	private PDFCellHelper(){}
	
	private static ThreadLocal<PDFCellHelper> threadLocal = new ThreadLocal<PDFCellHelper>(){
		
		protected PDFCellHelper initialValue() {
			if (instance == null) {
				instance = new PDFCellHelper();
			}
			return instance;
		};
		
	};
	
	public static PDFCellHelper getInstance(){
		return threadLocal.get();
	}
	
	public <T> PdfPCell getCell(ItextXMLElement element,T data) {
		PdfPCell cell = null;
		if(element.isElement("th")||element.isElement("td")){
			if(element.hasChild()){
				cell = new PdfPCell();
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
			if(cell!=null){
				ElementStyleRender.getInstance(PDFStyleTransformer.getInstance()).render(cell, element);
			}
		}
		return cell;
	}
}
