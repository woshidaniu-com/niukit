package com.woshidaniu.xmlhub.utils;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * 
 *@类名称	: XMLOutputUtils.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 29, 2016 4:25:59 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public abstract class XMLOutputUtils {

	protected static XMLOutputFactory XML_OUTPUT_FACTORY = XMLOutputFactory.newFactory();
	
	public static XMLStreamWriter getXMLStreamWriter(String fileName) throws IOException, XMLStreamException {
		return XML_OUTPUT_FACTORY.createXMLStreamWriter(new FileWriter(fileName));
	}
	
	public static XMLStreamWriter getXMLStreamWriter(File file) throws IOException, XMLStreamException {
		return XML_OUTPUT_FACTORY.createXMLStreamWriter(new FileWriter(file));
	}
	
	public static XMLStreamWriter getXMLStreamWriter(FileDescriptor file) throws XMLStreamException {
		return XML_OUTPUT_FACTORY.createXMLStreamWriter(new FileWriter(file));
	}
	
	public static XMLStreamWriter getXMLStreamWriter(OutputStream out) throws XMLStreamException {
		return XML_OUTPUT_FACTORY.createXMLStreamWriter(out);
	}
	
	
	
}
