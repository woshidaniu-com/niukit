package com.woshidaniu.xmlhub.core.validator;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.xml.transform.sax.SAXSource;
import javax.xml.validation.Schema;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

import com.woshidaniu.xmlhub.core.CharEncoding;

/**
 * 
 *@类名称	: XMLSchemaSaxValidator.java
 *@类描述	：Validate the document with the schema via SAX.
 *@创建人	：kangzhidong
 *@创建时间	：Mar 29, 2016 4:31:14 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class XMLSchemaSaxValidator extends AbstractXMLSchemaValidator implements XMLSchemaValidator {

	protected static Logger LOG = LoggerFactory.getLogger(XMLSchemaSaxValidator.class);
	
	public boolean validate(StringBuilder xmlString, Schema schema) throws IOException{
		InputStream inputStream = null;
		Reader reader = null;
		try {
			inputStream = new ByteArrayInputStream(xmlString.toString().getBytes(CharEncoding.UTF_8));
			reader = new InputStreamReader(inputStream);
			SAXSource source = new SAXSource(new InputSource(reader));
			return this.validate(source, schema);
		} finally {
			IOUtils.closeQuietly(inputStream);
			IOUtils.closeQuietly(reader);
		}
	}
	
	public boolean validate(Reader xmlReader, Schema schema) throws IOException{
		try {
			SAXSource source = new SAXSource(new InputSource(xmlReader));
			return this.validate(source, schema);
		} finally {
			IOUtils.closeQuietly(xmlReader);
		}
	}
	
	public boolean validate(InputStream xmlInput, Schema schema) throws IOException{
		try {
			SAXSource source = new SAXSource(new InputSource(xmlInput));
			return this.validate(source, schema);
		} finally {
			IOUtils.closeQuietly(xmlInput);
		}
	}
	
	
}
