package com.woshidaniu.xmlhub.dom4j.validator;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.SAXValidator;
import org.dom4j.io.XMLWriter;
import org.dom4j.util.XMLErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

import com.woshidaniu.xmlhub.core.handler.LoggerErrorHandler;
import com.woshidaniu.xmlhub.core.validator.AbstractXMLValidator;

/**
 * *******************************************************************
 * @className	： Dom4jXMLValidator
 * @description	： 通过XSD（XML Schema）校验XML
 * @author 		： kangzhidong
 * @date		： Feb 23, 2016 4:24:46 PM
 * @version 	V1.0 
 * *******************************************************************
 */
public class Dom4jXMLValidator extends AbstractXMLValidator {
	
	protected static Logger LOG = LoggerFactory.getLogger(Dom4jXMLValidator.class);
	
	private LoggerErrorHandler handler = new LoggerErrorHandler();
	 //创建默认的XML错误处理器
	private XMLErrorHandler errorHandler = new XMLErrorHandler();
	//获取基于 SAX 的解析器的实例
	private SAXParserFactory XML_PARSER_FACTORY = SAXParserFactory.newInstance();
	  //创建一个读取工具
	private SAXReader xmlReader = new SAXReader();
    
	public Dom4jXMLValidator(){
		 //解析器在解析时验证 XML 内容。
        XML_PARSER_FACTORY.setValidating(true);
        //指定由此代码生成的解析器将提供对 XML 名称空间的支持。
        XML_PARSER_FACTORY.setNamespaceAware(true);
	}
	
	@Override
	public boolean validate(File xmlfile, File file) throws IOException {
		try {
			//使用当前配置的工厂参数创建 SAXParser 的一个新实例。
			SAXParser parser = XML_PARSER_FACTORY.newSAXParser();
			//设置 XMLReader 的基础实现中的特定属性。核心功能和属性列表可以在 [url]http://sax.sourceforge.net/?selected=get-set[/url] 中找到。
			parser.setProperty(
			        "http://java.sun.com/xml/jaxp/properties/schemaLanguage",
			        "http://www.w3.org/2001/XMLSchema");
			parser.setProperty(
			        "http://java.sun.com/xml/jaxp/properties/schemaSource",
			        "file:" + file.getAbsolutePath());
			
			//创建一个SAXValidator校验工具，并设置校验工具的属性
			SAXValidator validator = new SAXValidator(parser.getXMLReader());
			validator.setErrorHandler(errorHandler);
			//设置校验工具的错误处理器，当发生错误时，可以从处理器对象中得到错误信息。
			validator.setErrorHandler(errorHandler);
			//获取要校验xml文档实例
			Document xmlDocument = (Document) xmlReader.read(file);
			//校验
			validator.validate(xmlDocument);
			
			XMLWriter writer = new XMLWriter(OutputFormat.createPrettyPrint());
            //如果错误信息不为空，说明校验失败，打印错误信息
            if (errorHandler.getErrors().hasContent()) {
                System.out.println("XML文件通过XSD文件校验失败！");
                writer.write(errorHandler.getErrors());
            } else {
                System.out.println("Good! XML文件通过XSD文件校验成功！");
            }
            
			return true;
		} catch (SAXNotRecognizedException e) {
			LOG.error(e.getLocalizedMessage(),e);
		} catch (SAXNotSupportedException e) {
			LOG.error(e.getLocalizedMessage(),e);
		} catch (ParserConfigurationException e) {
			LOG.error(e.getLocalizedMessage(),e);
		} catch (SAXException e) {
			LOG.error(e.getLocalizedMessage(),e);
		} catch (DocumentException e) {
			LOG.error(e.getLocalizedMessage(),e);
		}
		return false;
	}

}
