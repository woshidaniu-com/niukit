package com.woshidaniu.fastpdf.render.helper;

import java.util.HashMap;
import java.util.Map;

import com.itextpdf.text.Document;
import com.itextpdf.text.Rectangle;
import com.woshidaniu.fastpdf.render.elements.ItextXMLElement;
import com.woshidaniu.fastpdf.render.style.PDFStyleTransformer;


 /**
 * @package com.woshidaniu.fastpdf.render.helper
 * @className: RectangleHelper
 * @description: TODO
 * @author : kangzhidong
 * @date : 2014-1-14
 * @time : 上午11:08:53 
 */

public final class DocumentHelper {
	
	private static DocumentHelper instance = null;
	private DocumentHelper(){}
	
	public static DocumentHelper getInstance(){
		instance = instance==null?new DocumentHelper():instance;
		return instance;
	}
	
	public Document getDocument(String documentID){
	    //返回渲染后的Document对象
		return this.getDocument(documentID,new HashMap<String, String>()); 
	}
	
	public Document getDocument(String documentID,Map<String,String> attrs){
		//得到二级xml配置元素，即document元素
		ItextXMLElement element = ItextContext.getElement(documentID);
	    //返回渲染后的Document对象
		return this.getDocument(element, attrs); 
	}
	
	public Document getDocument(ItextXMLElement element){
		return this.getDocument(element,new HashMap<String, String>()); 
	}
	
	public Document getDocument(ItextXMLElement element,Map<String,String> attrs){
		//渲染过样式的页面大小 颜色 
		Rectangle rectangle = RectangleHelper.getInstance().getRectangle(element);
		//创建一个Document
		Document doc = new Document(rectangle);
		//Title,Author,Subject,Keywords 
		doc.addTitle(element.childText("title",attrs));
		doc.addAuthor(element.childText("author",attrs));
		doc.addSubject(element.childText("subject",attrs));
		doc.addKeywords(element.childText("keywords",attrs));
		doc.addCreator(element.childText("creator",attrs));
		
		//样式渲染 页面大小,页面背景色,页边空白,
		ElementStyleRender.getInstance(PDFStyleTransformer.getInstance()).render(doc, element);	
	    //返回渲染后的Document对象
		return doc; 
	}
	
}



