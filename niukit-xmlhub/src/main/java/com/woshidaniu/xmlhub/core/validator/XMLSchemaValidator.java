package com.woshidaniu.xmlhub.core.validator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import javax.xml.transform.Source;
import javax.xml.validation.Schema;

/**
 * 
 *@类名称	: XMLSchemaValidator.java
 *@类描述	：使用*.xsd 校验xml
 *@创建人	：kangzhidong
 *@创建时间	：Mar 29, 2016 4:30:34 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public interface XMLSchemaValidator extends XMLStreamValidator{

	public boolean validate(StringBuilder xmlString, Schema schema) throws IOException;
	
	public boolean validate(String xmlpath, Schema schema) throws IOException;
	
	public boolean validate(File xmlfile, Schema schema) throws IOException;
	
	public boolean validate(Reader xmlReader, Schema schema) throws IOException;
	
	public boolean validate(InputStream xmlInput, Schema schema) throws IOException;
	
	public boolean validate(Source source, Schema schema) throws IOException;
	
}
