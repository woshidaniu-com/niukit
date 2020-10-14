/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.xmlhub.core.transformer;

import java.io.File;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Collection;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Result;

import org.w3c.dom.Document;
import org.xml.sax.ContentHandler;

/**
 * *******************************************************************
 * @className	： XMLTransformer
 * @description	：Java 对象转换成XML接口
 * @author 		： kangzhidong
 * @date		： Mar 12, 2016 5:27:11 PM
 * @version 	V1.0 
 * *******************************************************************
 */
public interface XMLTransformer {

	public <T> String toXML(T source);
	
	/**
	 * 
	 * @description	：Java Object->Xml, 特别支持对Root Element是Collection的情形. 
	 * @author 		： kangzhidong
	 * @date 		：Mar 17, 2016 12:04:59 PM
	 * @param root
	 * @param rootName
	 * @return
	 */
	public String toXML(Collection<?> root, String rootName);
	
	public <T> void toXML(T source,String outPath);
	
	public <T> void toXML(T source,File output);
	
	public <T> void toXML(T source,Writer output);
	
	/**
	 * 
	 * @description	：  编组到 java.io.OutputStream 中： m.marshal( element, System.out );
	 * @author 		： kangzhidong
	 * @date 		：Mar 17, 2016 11:46:47 AM
	 * @param <T>
	 * @param source
	 * @param output
	 */
	public <T> void toXML(T source,OutputStream output);
	
	/**
	 * 
	 * @description	：  编组到 DOM Node 中：
	 * <pre>
	 *  DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	 *  dbf.setNamespaceAware(true);
	 *  DocumentBuilder db = dbf.newDocumentBuilder();
	 *  Document doc = db.newDocument();
	 *  m.marshal( element, doc );
	 * </pre>
	 * @author 		： kangzhidong
	 * @date 		：Mar 17, 2016 11:42:08 AM
	 * @param <T>
	 * @param source
	 * @param output
	 */
	public <T> void toXML(T source,Document output);
	
	/**
	 * 
	 * @description	：  编组到 SAX ContentHandler 中：
	 * // assume MyContentHandler instanceof ContentHandler
	 * m.marshal( element, new MyContentHandler() );  
	 * @author 		： kangzhidong
	 * @date 		：Mar 17, 2016 11:43:12 AM
	 * @param <T>
	 * @param source
	 * @param output
	 */
	public <T> void toXML(T source,ContentHandler output);
	
	/**
	 * 
	 * @description	：编组到 javax.xml.transform.Result 中：
	 * <pre> 
	 * SAXResult result = new SAXResult( new MyContentHandler() );
	 * DOMResult result = new DOMResult();
	 * StreamResult result = new StreamResult( System.out );
	 * </pre>
	 * @author 		： kangzhidong
	 * @date 		：Mar 17, 2016 11:41:14 AM
	 * @param <T>
	 * @param source
	 * @param output
	 */
	public <T> void toXML(T source,Result output);
	
	/**
	 * 
	 * @description	：  编组到 javax.xml.stream.XMLStreamWriter 中：
	 * XMLStreamWriter xmlStreamWriter = XMLOutputFactory.newInstance().createXMLStreamWriter( ... );
	 * @author 		： kangzhidong
	 * @date 		：Mar 17, 2016 11:40:04 AM
	 * @param <T>
	 * @param source
	 * @param output
	 */
	public <T> void toXML(T source,XMLStreamWriter output);
	
	/**
	 * 
	 * @description	：  编组到 javax.xml.stream.XMLEventWriter 中：
     * XMLEventWriter xmlEventWriter = XMLOutputFactory.newInstance().createXMLEventWriter( ... );
	 * @author 		： kangzhidong
	 * @date 		：Mar 17, 2016 11:40:36 AM
	 * @param <T>
	 * @param source
	 * @param output
	 */
	public <T> void toXML(T source,XMLEventWriter output);
	
	/** 
     * 封装Root Element 是 Collection的情况. 
     */ 
	@SuppressWarnings("unchecked")  
    public static class CollectionWrapper {  
    	
		public CollectionWrapper(Collection collection) {
			super();
			this.collection = collection;
		}
		
        @XmlAnyElement  
        protected Collection collection;  
        
        
        
    }  
    
}
