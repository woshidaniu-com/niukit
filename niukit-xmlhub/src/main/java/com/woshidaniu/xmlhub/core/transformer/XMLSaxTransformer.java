/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.xmlhub.core.transformer;

import java.io.File;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Collection;

import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Result;

import org.apache.commons.io.output.StringBuilderWriter;
import org.w3c.dom.Document;
import org.xml.sax.ContentHandler;

/**
 * 
 *@类名称	: XMLSaxTransformer.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 29, 2016 4:54:26 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class XMLSaxTransformer implements XMLTransformer {

	@Override
	public <T> String toXML(T source) {
		StringBuilderWriter writer = new StringBuilderWriter();
		this.toXML(source, writer);
		return writer.toString();
	}

	@Override
	public <T> void toXML(T source, String outPath) {
		this.toXML(source, new File(outPath));
	}

	@Override
	public <T> void toXML(T source, File outFile) {
		
	}

	@Override
	public <T> void toXML(T source, Writer writer) {
		
	}

	@Override
	public <T> void toXML(T source, OutputStream outPut) {
		
	}

	@Override
	public String toXML(Collection<?> root, String rootName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> void toXML(T source, Document output) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void toXML(T source, ContentHandler output) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public <T> void toXML(T source, Result output) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void toXML(T source, XMLStreamWriter output) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void toXML(T source, XMLEventWriter output) {
		// TODO Auto-generated method stub
		
	}
	 
}