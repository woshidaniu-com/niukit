package com.woshidaniu.xmlhub.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.woshidaniu.basicutils.BlankUtils;
import com.woshidaniu.basicutils.StringUtils;
import com.woshidaniu.xmlhub.core.CharEncoding;

/**
 * 
 *@类名称	: XMLBuilderUtils.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 29, 2016 4:26:56 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class XMLBuilderUtils {

	protected static Logger LOG = LoggerFactory.getLogger(XMLBuilderUtils.class);
	protected static DocumentBuilderFactory XML_BUILDER_FACTORY = DocumentBuilderFactory.newInstance();
	
	public static class FactoryCanstant{
		
		public static String ATTRIBUTES = "attributes";
		public static String COALESCING = "coalescing";
		public static String EXPAND_ENTITYREF = "expandEntityRef";
		public static String FEATURES = "features";
		public static String IGNORE_WHITESPACE = "whitespace";
		public static String IGNORE_COMMENTS = "ignoreComments";
		public static String NAMESPACE_AWARE = "namespaceAware";
		public static String SCHEMA = "schema";
		public static String VALIDATING = "validating";
		public static String XINCLUDE_AWARE = "isXIncludeAware";
		
	}
	
	// Create a Document from a String.
    public static Document getDocument(StringBuilder xmlString) throws SAXException, IOException, ParserConfigurationException {
        return createDocumentBuilder().parse(new ByteArrayInputStream(xmlString.toString().getBytes(CharEncoding.UTF_8)));
    }
	
	public static Document getDocument(String uri) throws SAXException, IOException, ParserConfigurationException {
		return createDocumentBuilder().parse(uri);
	}
	
	public static Document getDocument(File file) throws SAXException, IOException, ParserConfigurationException {
		return createDocumentBuilder().parse(file);
	}
	
	public static Document getDocument(InputSource in) throws SAXException, IOException, ParserConfigurationException {
		return createDocumentBuilder().parse(in);
	}
	
	public static Document getDocument(InputStream in) throws SAXException, IOException, ParserConfigurationException {
		return createDocumentBuilder().parse(in);
	}
	
	// Create a DocumentBuilder
    public static DocumentBuilder createDocumentBuilder() throws ParserConfigurationException {
        return XML_BUILDER_FACTORY.newDocumentBuilder();
    }
	
	@SuppressWarnings("unchecked")
	public static DocumentBuilder createDocumentBuilder(Map<String, Object> factorySeting) throws ParserConfigurationException{
		
		if( !BlankUtils.isBlank(factorySeting) ){
			
			Map<String, Object> attributes = (Map<String, Object>) factorySeting.get(FactoryCanstant.ATTRIBUTES);
			if( !BlankUtils.isBlank(attributes) ){
				for (String name : attributes.keySet()) {
					XML_BUILDER_FACTORY.setAttribute(name, attributes.get(name));
				}
			}
			XML_BUILDER_FACTORY.setCoalescing(Boolean.valueOf(StringUtils.getSafeBoolean(factorySeting.get(FactoryCanstant.COALESCING), "false")));
			XML_BUILDER_FACTORY.setExpandEntityReferences(Boolean.valueOf(StringUtils.getSafeBoolean(factorySeting.get(FactoryCanstant.EXPAND_ENTITYREF), "false")));
			Map<String, Boolean> features = (Map<String, Boolean>) factorySeting.get(FactoryCanstant.FEATURES);
			if( !BlankUtils.isBlank(features) ){
				for (String name : features.keySet()) {
					try {
						XML_BUILDER_FACTORY.setFeature(name, features.get(name));
					} catch (ParserConfigurationException e) {
						LOG.error("setFeature({0}) Error :",new String[]{name},e);
					}
				}
			}
			XML_BUILDER_FACTORY.setIgnoringComments(Boolean.valueOf(StringUtils.getSafeBoolean(factorySeting.get(FactoryCanstant.IGNORE_COMMENTS), "false")));
			XML_BUILDER_FACTORY.setIgnoringElementContentWhitespace(Boolean.valueOf(StringUtils.getSafeBoolean(factorySeting.get(FactoryCanstant.IGNORE_WHITESPACE), "false")));
			XML_BUILDER_FACTORY.setNamespaceAware(Boolean.valueOf(StringUtils.getSafeBoolean(factorySeting.get(FactoryCanstant.NAMESPACE_AWARE), "false")));
			XML_BUILDER_FACTORY.setSchema((Schema) factorySeting.get(FactoryCanstant.SCHEMA));
			XML_BUILDER_FACTORY.setValidating(Boolean.valueOf(StringUtils.getSafeBoolean(factorySeting.get(FactoryCanstant.VALIDATING), "false")));
			XML_BUILDER_FACTORY.setXIncludeAware(Boolean.valueOf(StringUtils.getSafeBoolean(factorySeting.get(FactoryCanstant.XINCLUDE_AWARE), "false")));
		}
		return XML_BUILDER_FACTORY.newDocumentBuilder();
	}
	
	public static Document getDocument(String uri,Map<String, Object> factorySeting) throws SAXException, IOException, ParserConfigurationException {
		return createDocumentBuilder(factorySeting).parse(uri);
	}
	
	public static Document getDocument(File file,Map<String, Object> factorySeting) throws SAXException, IOException, ParserConfigurationException {
		return createDocumentBuilder(factorySeting).parse(file);
	}
	
	public static Document getDocument(InputSource in,Map<String, Object> factorySeting) throws SAXException, IOException, ParserConfigurationException {
		return createDocumentBuilder(factorySeting).parse(in);
	}
	
	public static Document getDocument(InputStream in,Map<String, Object> factorySeting) throws SAXException, IOException, ParserConfigurationException {
		return createDocumentBuilder(factorySeting).parse(in);
	}
	
	
}
