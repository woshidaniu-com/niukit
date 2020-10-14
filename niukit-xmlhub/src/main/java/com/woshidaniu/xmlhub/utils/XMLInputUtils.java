package com.woshidaniu.xmlhub.utils;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;

import javax.xml.stream.EventFilter;
import javax.xml.stream.StreamFilter;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;

/**
 * 
 *@类名称	: XMLInputUtils.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 29, 2016 4:26:22 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public abstract class XMLInputUtils {

	protected static XMLInputFactory XML_INPUT_FACTORY = XMLInputFactory.newFactory();
	protected static StreamFilter DEFAULT_STEAM_FILTER = new StreamFilter(){

		@Override
		public boolean accept(XMLStreamReader event) {
			return true;
		}
		
	};
	protected static EventFilter DEFAULT_EVENT_FILTER = new EventFilter(){

		@Override
		public boolean accept(XMLEvent event) {
			return true;
		}
		
	};
	
	public static XMLStreamReader getXMLStreamReader(String fileName) throws FileNotFoundException, XMLStreamException {
		return XML_INPUT_FACTORY.createXMLStreamReader(new FileReader(fileName));
	}
	
	public static XMLStreamReader getXMLStreamReader(File file) throws FileNotFoundException, XMLStreamException {
		return XML_INPUT_FACTORY.createXMLStreamReader(new FileReader(file));
	}
	
	public static XMLStreamReader getXMLStreamReader(FileDescriptor file) throws XMLStreamException {
		return XML_INPUT_FACTORY.createXMLStreamReader(new FileReader(file));
	}
	
	public static XMLStreamReader getXMLStreamReader(InputStream in) throws XMLStreamException {
		return XML_INPUT_FACTORY.createXMLStreamReader(in);
	}
	
	public static XMLStreamReader getXMLStreamReader(String fileName,StreamFilter filter) throws FileNotFoundException, XMLStreamException {
		XMLStreamReader streamReader = XML_INPUT_FACTORY.createXMLStreamReader(new FileReader(fileName));
		return XML_INPUT_FACTORY.createFilteredReader(streamReader, filter == null ? DEFAULT_STEAM_FILTER : filter);
	}
	
	public static XMLStreamReader getXMLStreamReader(File file,StreamFilter filter) throws FileNotFoundException, XMLStreamException {
		XMLStreamReader streamReader = XML_INPUT_FACTORY.createXMLStreamReader(new FileReader(file));
		return XML_INPUT_FACTORY.createFilteredReader(streamReader, filter == null ? DEFAULT_STEAM_FILTER : filter);
	}
	
	public static XMLStreamReader getXMLStreamReader(FileDescriptor file,StreamFilter filter) throws XMLStreamException {
		XMLStreamReader streamReader = XML_INPUT_FACTORY.createXMLStreamReader(new FileReader(file));
		return XML_INPUT_FACTORY.createFilteredReader(streamReader, filter == null ? DEFAULT_STEAM_FILTER : filter);
	}
	
	public static XMLStreamReader getXMLStreamReader(InputStream in,StreamFilter filter) throws XMLStreamException {
		XMLStreamReader streamReader = XML_INPUT_FACTORY.createXMLStreamReader(in);
		return XML_INPUT_FACTORY.createFilteredReader(streamReader, filter == null ? DEFAULT_STEAM_FILTER : filter);
	}
	
	public static XMLEventReader getXMLEventReader(String fileName) throws FileNotFoundException, XMLStreamException {
		return XML_INPUT_FACTORY.createXMLEventReader(new FileReader(fileName));
	}
	
	public static XMLEventReader getXMLEventReader(File file) throws FileNotFoundException, XMLStreamException {
		return XML_INPUT_FACTORY.createXMLEventReader(new FileReader(file));
	}
	
	public static XMLEventReader getXMLEventReader(FileDescriptor file) throws XMLStreamException {
		return XML_INPUT_FACTORY.createXMLEventReader(new FileReader(file));
	}
	
	public static XMLEventReader getXMLEventReader(InputStream in) throws XMLStreamException {
		return XML_INPUT_FACTORY.createXMLEventReader(in);
	}
	
	public static XMLEventReader getXMLEventReader(String fileName,EventFilter filter) throws FileNotFoundException, XMLStreamException {
		XMLEventReader eventReader = XMLInputUtils.getXMLEventReader(fileName);
		return XML_INPUT_FACTORY.createFilteredReader(eventReader, filter == null ? DEFAULT_EVENT_FILTER : filter);
	}
	
	public static XMLEventReader getXMLEventReader(File file,EventFilter filter) throws FileNotFoundException, XMLStreamException {
		XMLEventReader eventReader = XMLInputUtils.getXMLEventReader(file);
		return XML_INPUT_FACTORY.createFilteredReader(eventReader, filter == null ? DEFAULT_EVENT_FILTER : filter);
	}
	
	public static XMLEventReader getXMLEventReader(FileDescriptor file,EventFilter filter) throws XMLStreamException {
		XMLEventReader eventReader = XMLInputUtils.getXMLEventReader(file);
		return XML_INPUT_FACTORY.createFilteredReader(eventReader, filter == null ? DEFAULT_EVENT_FILTER : filter);
	}
	
	public static XMLEventReader getXMLEventReader(InputStream in,EventFilter filter) throws XMLStreamException {
		XMLEventReader eventReader = XMLInputUtils.getXMLEventReader(in);
		return XML_INPUT_FACTORY.createFilteredReader(eventReader, filter == null ? DEFAULT_EVENT_FILTER : filter);
	}
	
}
