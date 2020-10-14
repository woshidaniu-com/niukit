package com.woshidaniu.fastpdf.render.helper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPRow;
import com.itextpdf.text.pdf.PdfPTable;
import com.woshidaniu.fastpdf.render.elements.ItextXMLElement;
import com.woshidaniu.fastpdf.render.style.PDFStyleTransformer;
public class PDFTbodyHelper{

	private static PDFTbodyHelper instance = null;
	private PDFTbodyHelper(){}
	
	public static PDFTbodyHelper getInstance(){
		if (instance == null) {
			instance = new PDFTbodyHelper();
		}
		return instance;
	}
	
	public <T> PdfPTable getTbody(ItextXMLElement element,T data) {
		if(element.isElement("tbody")){
			Iterator<Element> itr = element.getChildren().iterator();
			List<PdfPRow> rows = new ArrayList<PdfPRow>();
			while (itr.hasNext()) {
				ItextXMLElement trElement = (ItextXMLElement)itr.next();
				if(element.isElement("iterator")){
					List<Object> elements = IteratorHelper.getInstance().iterator(trElement, JavaBeanUtils.getProperty(data,element.attr("value")));
					for (Object row : elements) {
						if(row instanceof PdfPRow){
							rows.add((PdfPRow)row);
						}
					}
				}else if(element.isElement("tr")){
					rows.add(PDFRowHelper.getInstance().getRow(trElement,data));
				}
			}
			//迭代行记录
			int columns = 1;
			for (PdfPRow row : rows) {
				columns = Math.max(columns, row.getCells().length);
			}
			//多行columns列 
			PdfPTable tbody = new PdfPTable(columns);
			for (PdfPRow row : rows) {
				for (PdfPCell cell: row.getCells()) {
					tbody.addCell(cell);
				}
			}
			if(tbody!=null){
				ElementStyleRender.getInstance(PDFStyleTransformer.getInstance()).render(tbody, element);
			}
			return tbody;
		}
		return null;
	}
	
}