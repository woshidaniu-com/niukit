package com.woshidaniu.fastpdf.render.helper;

import java.util.Iterator;
import java.util.List;

import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPRow;
import com.woshidaniu.fastpdf.render.elements.ItextXMLElement;
import com.woshidaniu.fastpdf.render.style.PDFStyleTransformer;

public class PDFRowHelper {

	private static PDFRowHelper instance = null;
	private PDFRowHelper(){}
	
	private static ThreadLocal<PDFRowHelper> threadLocal = new ThreadLocal<PDFRowHelper>(){
		
		protected PDFRowHelper initialValue() {
			if (instance == null) {
				instance = new PDFRowHelper();
			}
			return instance;
		};
		
	};
	
	public static PDFRowHelper getInstance(){
		return threadLocal.get();
	}
	
	public <T> PdfPRow getRow(ItextXMLElement element,T data){
		PdfPRow row = null;
		if(element.isElement("tr")){
			List<Element> childrens = element.getChildren();
			PdfPCell[] cells = new PdfPCell[childrens.size()];
			Iterator<Element> itr = childrens.iterator();
			int cellIndex = 0;
			while (itr.hasNext()) {
				ItextXMLElement cellElement = (ItextXMLElement)itr.next();
				cells[cellIndex] = PDFCellHelper.getInstance().getCell(cellElement,data);
				cellIndex ++;
			}
			row = new PdfPRow(cells);
		}else{
			row = new PdfPRow(new PdfPCell[]{new PdfPCell()});
		}
		if(row!=null){
			ElementStyleRender.getInstance(PDFStyleTransformer.getInstance()).render(row, element);
		}
		return row;
	}
	
	
	
}
