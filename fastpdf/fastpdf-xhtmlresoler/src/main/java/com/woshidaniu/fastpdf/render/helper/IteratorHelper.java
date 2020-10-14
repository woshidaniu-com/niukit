package com.woshidaniu.fastpdf.render.helper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.woshidaniu.fastpdf.render.elements.ItextXMLElement;
/**
 * 
 * @package com.woshidaniu.fastpdf.render.helper
 * @className: IteratorHelper
 * @description: 元素迭代处理助手，因为迭代操作较多，采用多线程方式
 * @author : kangzhidong
 * @date : 2014-1-17
 * @time : 下午2:07:43
 */
public class IteratorHelper {

	private static IteratorHelper instance = null;
	private IteratorHelper(){}
	
	private static ThreadLocal<IteratorHelper> threadLocal = new ThreadLocal<IteratorHelper>(){
		
		protected IteratorHelper initialValue() {
			if (instance == null) {
				instance = new IteratorHelper();
			}
			return instance;
		};
		
	};
	
	public static IteratorHelper getInstance(){
		return threadLocal.get();
	}
	
	public <T> List<Object> iterator(ItextXMLElement xmlElement,T data){
		List<Object> elements = new ArrayList<Object>();
		if(xmlElement.isElement("iterator")){
			Iterator<org.jdom2.Element> itr = xmlElement.getChildren().iterator();
			while (itr.hasNext()) {
				ItextXMLElement itrElement = (ItextXMLElement)itr.next();
				List<Object> itrElements = this.iterator(itrElement,JavaBeanUtils.getProperty(data,itrElement.attr("value")));
				elements.addAll(itrElements);
			}
		}else if(xmlElement.isElement("table")){
			//表格
			elements.add(PDFTableHelper.getInstance().getTable(xmlElement,data));
		}else if(xmlElement.isElement("tbody")){
			//表格body内容
			elements.add(PDFTbodyHelper.getInstance().getTbody(xmlElement,data));
		}else if(xmlElement.isElement("tr")){
			//行记录PdfPRow
			elements.add(PDFRowHelper.getInstance().getRow(xmlElement,data));
		}else if(xmlElement.isElementOf(new String[]{"p","h1","h2","h3","h4","h5","h6","h7"})){
  			//渲染一个文字段落
			elements.add(ParagraphHelper.getInstance().getParagraph(xmlElement,data));
		}else if(xmlElement.isElementOf(new String[]{"space","line"})){
  			//渲染一条分割线
			elements.add(LineHelper.getInstance().getLine(xmlElement));
  		}else if(xmlElement.isElementOf(new String[]{"br","a","i","span","font"})){
  			elements.add(ChunkHelper.getInstance().getChunk(xmlElement));
		}else if(xmlElement.isElementOf(new String[]{"th","td"})){
			//单元格PdfPCell
			elements.add(PDFCellHelper.getInstance().getCell(xmlElement, data));
  		}
		return elements;
	}
	
}
