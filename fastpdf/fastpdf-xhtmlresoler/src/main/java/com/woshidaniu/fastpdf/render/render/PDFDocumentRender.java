package com.woshidaniu.fastpdf.render.render;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.woshidaniu.fastpdf.render.elements.ItextXMLElement;
import com.woshidaniu.fastpdf.render.helper.DocumentHelper;
import com.woshidaniu.fastpdf.render.helper.IteratorHelper;
import com.woshidaniu.fastpdf.render.helper.PDFWriterHelper;
/**
 * 
 * @className: PDFDocumentRender
 * @description: XML配置文件解析渲染器，根据XML渲染PDF页面内容
 * @author : kangzhidong
 * @date : 上午9:39:10 2013-8-14
 * @modify by:
 * @modify date :
 * @modify description :
 */
@SuppressWarnings({"unchecked"})
public class PDFDocumentRender extends DocumentRender{

	private static PDFDocumentRender instance = null;
	private PDFDocumentRender(){}
	private static ThreadLocal<PDFDocumentRender> threadLocal = new ThreadLocal<PDFDocumentRender>(){
		
		protected PDFDocumentRender initialValue() {
			if (instance == null) {
				instance = new PDFDocumentRender();
			}
			return instance;
		};
		
	};
	
	public static PDFDocumentRender getInstance(){
		return threadLocal.get();
	}
	
	
	/**
	 * @description: 根据documentID指定的配置信息和数据渲染PDF文档
	 * @param documentID
	 * @param datas
	 * @return
	 * @throws Exception
	 */
	public <T> ByteArrayInputStream render(String documentID,List<T> datas) throws Exception{
		return this.render(documentID, new HashMap(), datas);
	}
	
	/**
	 * @param <T>
	 * @description: 根据documentID指定的配置信息和数据渲染PDF文档
	 * @param documentID
	 * @param attrs
	 * @param datas
	 * @return
	 * @throws Exception
	 */
	public <T> ByteArrayInputStream render(String documentID,Map<String,String> attrs,List<T> datas) throws Exception{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		//第1步：根据XML配置 得到document元素
		ItextXMLElement rootElement = ItextContext.getElement(documentID);
		//第2步，创建一个 iTextSharp.text.Document对象的实例：
		Document document = DocumentHelper.getInstance().getDocument(rootElement, attrs);
		//第3步：根据属性为该Document创建一个Writer实例：
		PdfWriter writer = PDFWriterHelper.getInstance(attrs).getPDFWriter(document, out, rootElement);
		//第4步：打开当前Document
		document.open();
		if(document.isOpen()){
		    //第5步，循环数据集合，每一个元素代表一个document的文档
			for (T data : datas) {
				//第6步，遍历所有的子元素,为当前Document添加内容：
				List<Element> childrens = rootElement.getChildren();
			    Iterator<Element> itr = childrens.iterator();
				while (itr.hasNext()) {
					//渲染文档内容
					ItextXMLElement element = (ItextXMLElement)itr.next();
		  			List<Object> elements = IteratorHelper.getInstance().iterator(element,data);
		  			for (Object elementx : elements) {
		  				if(elementx instanceof Chunk){
		  					document.add((Chunk)elementx);
		  				}else if(elementx instanceof LineSeparator){
		  					document.add((LineSeparator)elementx);
						}else if(elementx instanceof Paragraph){
							document.add((Paragraph)elementx);
						}else if(elementx instanceof PdfPTable){
							document.add((PdfPTable)elementx);
						}
					}
				}
				document.newPage();
			}
		}
		//第7步，关闭Document
		document.close();
		writer.close();
		//第8步，返回渲染后的对象输入流
		return new ByteArrayInputStream(out.toByteArray());
	}
	
	public <T> ByteArrayInputStream render(String documentID,T data) throws Exception{
		return this.render(documentID, new HashMap(), data);
	}
	
	/**
	 * @param <T>
	 * @description: 根据documentID指定的配置信息和数据渲染PDF文档
	 * @param documentID
	 * @param attrs
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public <T> ByteArrayInputStream render(String documentID,Map<String,String> attrs,T data) throws Exception{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		//第1步：根据XML配置 得到document元素
		ItextXMLElement rootElement = ItextContext.getElement(documentID);
		//第2步，创建一个 iTextSharp.text.Document对象的实例：
		Document document = DocumentHelper.getInstance().getDocument(rootElement, attrs);
		//第3步：根据属性为该Document创建一个Writer实例：
		PdfWriter writer = PDFWriterHelper.getInstance(attrs).getPDFWriter(document, out, rootElement);
		//第4步：打开当前Document
		document.open();
		if(document.isOpen()){
			//第5步，遍历所有的子元素,为当前Document添加内容：
			List<Element> childrens = rootElement.getChildren();
		    Iterator<Element> itr = childrens.iterator();
			while (itr.hasNext()) {
				//渲染文档内容
				ItextXMLElement element = (ItextXMLElement)itr.next();
				List<Object> elements = IteratorHelper.getInstance().iterator(element,data);
	  			for (Object elementx : elements) {
	  				if(elementx instanceof Chunk){
	  					document.add((Chunk)elementx);
	  				}else if(elementx instanceof LineSeparator){
	  					document.add((LineSeparator)elementx);
					}else if(elementx instanceof Paragraph){
						document.add((Paragraph)elementx);
					}else if(elementx instanceof PdfPTable){
						document.add((PdfPTable)elementx);
					}
				}
			}
		}
		//第6步，关闭Document
		document.close();
		writer.close();
		//第7步，返回渲染后的对象输入流
		return new ByteArrayInputStream(out.toByteArray());
	}
	
	
	
}
