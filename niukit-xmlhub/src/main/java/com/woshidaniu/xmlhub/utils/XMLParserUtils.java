package com.woshidaniu.xmlhub.utils;

import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.Schema;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.woshidaniu.basicutils.BlankUtils;
import com.woshidaniu.basicutils.StringUtils;

/**
 * *******************************************************************
 * @className	： XMLParserUtils
 * @description	： TODO(描述这个类的作用)
 * @author 		： kangzhidong
 * @date		： Mar 29, 2016 5:21:58 PM
 * @version 	V1.0 
 * *******************************************************************
 */
public abstract class XMLParserUtils {

	private static final Logger LOG = LoggerFactory.getLogger(XMLParserUtils.class);
	//获取基于 SAX 的解析器的实例
	protected static SAXParserFactory XML_PARSER_FACTORY = SAXParserFactory.newInstance();
    
	public static class FactoryCanstant{
		
		public static String FEATURES = "features";
		public static String NAMESPACE_AWARE = "namespaceAware";
		public static String SCHEMA = "schema";
		public static String VALIDATING = "validating";
		public static String XINCLUDE_AWARE = "isXIncludeAware";
		
	}

	// Create a SAXParserFactory
    public static SAXParserFactory getSAXParserFactory() {
        return XML_PARSER_FACTORY;
    }
    
	// Create a SAXParser
    public static SAXParser createSAXParser() throws SAXException, ParserConfigurationException {
    	//使用当前配置的工厂参数创建 SAXParser 的一个新实例。
        return XML_PARSER_FACTORY.newSAXParser();
    }
	
	@SuppressWarnings("unchecked")
	public static SAXParser createSAXParser(Map<String, Object> factorySeting) throws SAXException, ParserConfigurationException{
		SAXParserFactory XML_PARSER_FACTORY = SAXParserFactory.newInstance();
		if( !BlankUtils.isBlank(factorySeting) ){
			
			Map<String, Boolean> features = (Map<String, Boolean>) factorySeting.get(FactoryCanstant.FEATURES);
			if( !BlankUtils.isBlank(features) ){
				for (String name : features.keySet()) {
					try {
						XML_PARSER_FACTORY.setFeature(name, features.get(name));
					} catch (ParserConfigurationException e) {
						LOG.error("setFeature({0}) Error :",new String[]{name},e);
					}
				}
			}
			//指定由此代码生成的解析器是否将提供对 XML名称空间的支持。
			XML_PARSER_FACTORY.setNamespaceAware(Boolean.valueOf(StringUtils.getSafeBoolean(factorySeting.get(FactoryCanstant.NAMESPACE_AWARE), "false")));
			XML_PARSER_FACTORY.setSchema((Schema) factorySeting.get(FactoryCanstant.SCHEMA));
			//指定解析器在解析时是否验证 XML 内容。
			XML_PARSER_FACTORY.setValidating(Boolean.valueOf(StringUtils.getSafeBoolean(factorySeting.get(FactoryCanstant.VALIDATING), "false")));
			XML_PARSER_FACTORY.setXIncludeAware(Boolean.valueOf(StringUtils.getSafeBoolean(factorySeting.get(FactoryCanstant.XINCLUDE_AWARE), "false")));
		}
		//使用当前配置的工厂参数创建 SAXParser 的一个新实例。
		return XML_PARSER_FACTORY.newSAXParser();
	}
	
	
}
