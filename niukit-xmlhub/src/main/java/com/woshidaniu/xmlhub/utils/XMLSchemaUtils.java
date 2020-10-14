package com.woshidaniu.xmlhub.utils;

import java.io.File;
import java.net.URL;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

import com.woshidaniu.basicutils.BlankUtils;
import com.woshidaniu.xmlhub.core.handler.LoggerErrorHandler;
import com.woshidaniu.xmlhub.utils.XMLBuilderUtils.FactoryCanstant;

/**
 * 
 *@类名称	: XMLSchemaUtils.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 29, 2016 4:26:14 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public abstract class XMLSchemaUtils {

	public static String PROPERTIES = "properties";
	public static String FEATURES = "features";
	protected static Logger LOG = LoggerFactory.getLogger(XMLSchemaUtils.class);
	// 建立schema工厂
	protected static SchemaFactory SCHEMA_FACTORY = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
	protected static SchemaFactory SCHEMA_INSTANCE_FACTORY = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI);
	protected static LoggerErrorHandler ERROR_HANDLER = new LoggerErrorHandler();
	
	// Create a SchemaFactory
    public static SchemaFactory createSchemaFactory() {
        return createSchemaFactory(SCHEMA_FACTORY , null);
    }
	
	@SuppressWarnings("unchecked")
	public static SchemaFactory createSchemaFactory(SchemaFactory factory,Map<String, Object> factorySeting) {
		factory = factory == null ? SCHEMA_FACTORY : factory;
		if( !BlankUtils.isBlank(factorySeting)){
			
			factory.setErrorHandler(ERROR_HANDLER);
			factory.setErrorHandler(ERROR_HANDLER);
			
			Map<String, Object> properties = (Map<String, Object>) factorySeting.get(FactoryCanstant.ATTRIBUTES);
			if( !BlankUtils.isBlank(properties) ){
				for (String name : properties.keySet()) {
					try {
						factory.setProperty(name, properties.get(name));
					} catch (SAXNotRecognizedException e) {
						LOG.error("setProperty({0}) Error :",new String[]{name},e);
					} catch (SAXNotSupportedException e) {
						LOG.error("setProperty({0}) Error :",new String[]{name},e);
					}
				}
			}
			Map<String, Boolean> features = (Map<String, Boolean>) factorySeting.get(FactoryCanstant.FEATURES);
			if( !BlankUtils.isBlank(features) ){
				for (String name : features.keySet()) {
					try {
						factory.setFeature(name, features.get(name));
					} catch (SAXNotRecognizedException e) {
						LOG.error("setFeature({0}) Error :",new String[]{name},e);
					} catch (SAXNotSupportedException e) {
						LOG.error("setFeature({0}) Error :",new String[]{name},e);
					}
				}
			}
		}
		return factory;
	}
	
	
	public static Schema newSchema(File schemaFile) throws SAXException {
		// 建立验证文档文件对象，利用此文件对象所封装的文件进行schema验证
        return createSchemaFactory().newSchema(schemaFile);
    }
	
	public static Schema newSchema(String schemaPath) throws SAXException {
		return newSchema(new File(schemaPath));
	}
	
    public static Schema newSchema(URL schemaURL) throws SAXException {
        return createSchemaFactory().newSchema(schemaURL);
    }
    
    public static Schema newSchema(Source schema) throws SAXException {
        return createSchemaFactory().newSchema(schema);
    }
    
    public static Schema newSchema(Source[] schemas) throws SAXException {
        return createSchemaFactory().newSchema(schemas);
    }
    
    public static Schema newSchema(File schemaFile,Map<String, Object> factorySeting) throws SAXException {
		// 建立验证文档文件对象，利用此文件对象所封装的文件进行schema验证
        return createSchemaFactory(SCHEMA_FACTORY ,factorySeting).newSchema(schemaFile);
    }
	
	public static Schema newSchema(String schemaPath,Map<String, Object> factorySeting) throws SAXException {
		return newSchema(new File(schemaPath),factorySeting);
	}
	
    public static Schema newSchema(URL schemaURL,Map<String, Object> factorySeting) throws SAXException {
        return createSchemaFactory(SCHEMA_FACTORY ,factorySeting).newSchema(schemaURL);
    }
    
    public static Schema newSchema(Source schema,Map<String, Object> factorySeting) throws SAXException {
        return createSchemaFactory(SCHEMA_FACTORY ,factorySeting).newSchema(schema);
    }
    
    public static Schema newSchema(Source[] schemas,Map<String, Object> factorySeting) throws SAXException {
        return createSchemaFactory(SCHEMA_FACTORY ,factorySeting).newSchema(schemas);
    }
    
    public static Schema newSchemaInstance(String schemaPath) throws SAXException {
		return newSchemaInstance(new File(schemaPath));
	}
    
    public static Schema newSchemaInstance(File schemaFile) throws SAXException {
        return SCHEMA_INSTANCE_FACTORY.newSchema(schemaFile);
    }
    
    public static Schema newSchemaInstance(URL schemaURL) throws SAXException {
        return SCHEMA_INSTANCE_FACTORY.newSchema(schemaURL);
    }
    
    public static Schema newSchemaInstance(Source[] schemas) throws SAXException {
        return SCHEMA_INSTANCE_FACTORY.newSchema(schemas);
    }
    
    public static Schema newSchemaInstance(Source schema) throws SAXException {
        return SCHEMA_INSTANCE_FACTORY.newSchema(schema);
    }
    
   
}
