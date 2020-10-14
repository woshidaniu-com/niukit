package com.woshidaniu.xmlhub.core.validator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.woshidaniu.xmlhub.utils.XMLSchemaUtils;


/**
 * 
 *@类名称	: JavaxXMLSchemaValidator.java
 *@类描述	：使用*.xsd 校验xml
 *@创建人	：kangzhidong
 *@创建时间	：Mar 29, 2016 4:31:03 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public abstract class AbstractXMLSchemaValidator extends AbstractXMLStreamValidator implements XMLSchemaValidator {
	
	protected static Logger LOG = LoggerFactory.getLogger(AbstractXMLSchemaValidator.class);
	
	public boolean validate(String xmlpath, Schema schema) throws IOException{
		return this.validate(new File(xmlpath), schema);
	}
	
	public boolean validate(File xmlfile, Schema schema) throws IOException{
		return this.validate(new FileInputStream(xmlfile), schema);
	}
	
	public boolean validate(Source source, Schema schema) throws IOException{
		try {
			// 通过Schema产生针对于此Schema的验证器，利用schenaFile进行验证
			Validator validator = schema.newValidator();
			// 开始验证，成功输出success!!!，失败输出fail
			validator.validate(source);
			return true;
		} catch (SAXException e) {
			LOG.error(e.getLocalizedMessage(),e);
		}
		return false;
	}
	
	public boolean validate(InputStream xmlInput, InputStream schemaInput) throws IOException {
		try {
			// 利用schema工厂，接收验证文档文件对象生成Schema对象
			Schema schema = XMLSchemaUtils.createSchemaFactory().newSchema(new StreamSource(schemaInput));
			// 通过Schema产生针对于此Schema的验证器，利用schenaFile进行验证
			Validator validator = schema.newValidator();
			// 得到验证的数据源
			Source source = new StreamSource(xmlInput);
			// 开始验证，成功输出success!!!，失败输出fail
			validator.validate(source);
			return true;
		} catch (SAXException e) {
			LOG.error(e.getLocalizedMessage(),e);
		} finally {
			IOUtils.closeQuietly(xmlInput);
			IOUtils.closeQuietly(schemaInput);
		}
		return false;
	}
	
}
