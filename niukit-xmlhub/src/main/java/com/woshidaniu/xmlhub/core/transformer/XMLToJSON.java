/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.xmlhub.core.transformer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.woshidaniu.xmlhub.core.exception.TransformException;
import com.woshidaniu.xmlhub.core.handler.XML2JSONSaxHandler;
  
public class XMLToJSON {  
      
    public static String convert(String xmlStr) throws SAXException,  
            IOException, TransformException {  
        return convert(new InputSource(new StringReader(xmlStr)));  
    }  
  
    public static String convert(File file) throws SAXException,  
            IOException, TransformException {  
        return convert(new InputSource(new FileInputStream(file)));  
    }  
  
    public static String convert(InputSource inputSource) throws SAXException,  
            IOException, TransformException {  
    	XML2JSONSaxHandler handler = new XML2JSONSaxHandler();  
        //创建一个 SAX 解析器 ,并设置这个解析器的内容事件处理器 和 错误事件处理器 为 handler  
        XMLReader reader = XMLReaderFactory.createXMLReader();  
        reader.setContentHandler(handler);  
        reader.setErrorHandler(handler);  
        //用 SAX 解析器解析XML输入源  
        reader.parse(inputSource);  
        //返回 ToJsonSAXHandler 中保存的 json字符串  
        return handler.toJSONString();  
    }  
  
}  
