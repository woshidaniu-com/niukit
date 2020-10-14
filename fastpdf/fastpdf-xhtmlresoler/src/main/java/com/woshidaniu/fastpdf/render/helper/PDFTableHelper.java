package com.woshidaniu.fastpdf.render.helper;

import java.util.Iterator;
import java.util.List;

import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPRow;
import com.itextpdf.text.pdf.PdfPTable;
import com.woshidaniu.fastpdf.render.elements.ItextXMLElement;
/**
 * 
 * @package com.woshidaniu.fastpdf.render.helper
 * @className: PDFTableHelper
 * @description: table元素节点处理助手，采用多线程方式
 * @author : kangzhidong
 * @date : 2014-1-17
 * @time : 下午2:07:43
 */
public class PDFTableHelper {

	private static PDFTableHelper instance = null;
	private PDFTableHelper(){}
	
	private static ThreadLocal<PDFTableHelper> threadLocal = new ThreadLocal<PDFTableHelper>(){
		
		protected PDFTableHelper initialValue() {
			if (instance == null) {
				instance = new PDFTableHelper();
			}
			return instance;
		};
		
	};
	
	public static PDFTableHelper getInstance(){
		return threadLocal.get();
	}
	
	public <T> PdfPTable getTable(ItextXMLElement element,T data){
		if(element.isElement("table")){
			//多行1列 int columns = 0;
			PdfPTable table = new PdfPTable(1);
			Iterator<Element> itr = element.getChildren().iterator();
			while (itr.hasNext()) {
				ItextXMLElement itrElement = (ItextXMLElement)itr.next();
				if(itrElement.isElement("iterator")){
					List<Object> elements =  IteratorHelper.getInstance().iterator(itrElement,JavaBeanUtils.getProperty(data,element.attr("value")));
					for (Object elementx : elements) {
						if(elementx instanceof PdfPTable){
							table.addCell((PdfPTable)elementx);
						}else if(elementx instanceof PdfPRow){
							PdfPRow row = (PdfPRow)elementx;
							for (PdfPCell cell: row.getCells()) {
								table.addCell(cell);
							}
						}
					}
				}else if(itrElement.isElement("caption")){
					//设置表格标题
					PdfPTable caption = PDFCaptionHelper.getInstance().getCaption(itrElement,data);
					table.addCell(caption);
				}else if(itrElement.isElement("thead")){
					//设置表格表头
					PdfPTable thead = PDFTheadHelper.getInstance().getThead(itrElement,data);
					table.addCell(thead);
				}else if(itrElement.isElement("tbody")){
					//设置表格body内容
					PdfPTable tbody = PDFTbodyHelper.getInstance().getTbody(itrElement,data);
		  			table.addCell(tbody);
				}else if(itrElement.isElement("tfoot")){
					//设置表格表底
					PdfPTable tfoot = PDFTfootHelper.getInstance().getTfoot(itrElement,data);
		  			table.addCell(tfoot);
				}else if(itrElement.isElement("tr")){
					//设置行记录
					PdfPRow row = PDFRowHelper.getInstance().getRow(itrElement,data);
					for (PdfPCell cell: row.getCells()) {
						table.addCell(cell);
					}
				}
			}
			return table;
		}
		return null;
	}
	
	
}
