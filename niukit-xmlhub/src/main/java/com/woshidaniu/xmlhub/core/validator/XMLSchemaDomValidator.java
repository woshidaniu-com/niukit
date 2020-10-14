package com.woshidaniu.xmlhub.core.validator;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.woshidaniu.xmlhub.utils.XMLBuilderUtils;

/**
 * 
 *@类名称	: JavaxXMLSchemaDomValidator.java
 *@类描述	：Validate the document with the schema via DOM.
 *@创建人	：kangzhidong
 *@创建时间	：Mar 29, 2016 4:31:25 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class XMLSchemaDomValidator extends AbstractXMLSchemaValidator implements XMLSchemaValidator {

	protected static Logger LOG = LoggerFactory.getLogger(XMLSchemaSaxValidator.class);
	
	public boolean validate(StringBuilder xmlString, Schema schema) throws IOException{
		try {
			Document document = XMLBuilderUtils.getDocument(xmlString);
			DOMSource source = new DOMSource(document);
			return this.validate(source, schema);
		} catch (SAXException e) {
			LOG.error(e.getLocalizedMessage(),e);
		} catch (ParserConfigurationException e) {
			LOG.error(e.getLocalizedMessage(),e);
		}
		return false;
	}
	
	public boolean validate(Reader xmlReader, Schema schema) throws IOException{
		try {
			Document document = XMLBuilderUtils.getDocument(new InputSource(xmlReader));
			DOMSource source = new DOMSource(document);
			return this.validate(source, schema);
		} catch (SAXException e) {
			LOG.error(e.getLocalizedMessage(),e);
		} catch (ParserConfigurationException e) {
			LOG.error(e.getLocalizedMessage(),e);
		} finally {
			IOUtils.closeQuietly(xmlReader);
		}
		return false;
	}
	
	public boolean validate(InputStream xmlInput, Schema schema) throws IOException{
		try {
			Document document = XMLBuilderUtils.getDocument(new InputSource(xmlInput));
			DOMSource source = new DOMSource(document);
			return this.validate(source, schema);
		} catch (SAXException e) {
			LOG.error(e.getLocalizedMessage(),e);
		} catch (ParserConfigurationException e) {
			LOG.error(e.getLocalizedMessage(),e);
		} finally {
			IOUtils.closeQuietly(xmlInput);
		}
		return false;
	}
	
}
