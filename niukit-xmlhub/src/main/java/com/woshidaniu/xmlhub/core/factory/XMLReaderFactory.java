package com.woshidaniu.xmlhub.core.factory;

import java.io.IOException;

import javax.xml.stream.EventFilter;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;

import com.woshidaniu.basicutils.BlankUtils;
import com.woshidaniu.xmlhub.utils.XMLInputUtils;

/**
 * 
 *@类名称	: XMLReaderFactory.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 29, 2016 4:28:01 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class XMLReaderFactory {
	
	private volatile static XMLReaderFactory singleton;

	public static XMLReaderFactory getInstance() {
		if (singleton == null) {
			synchronized (XMLReaderFactory.class) {
				if (singleton == null) {
					singleton = new XMLReaderFactory();
				}
			}
		}
		return singleton;
	}
	
	private XMLReaderFactory(){
		
	}
	
	public void readerXML(XMLEventInvocation invocation) throws IOException, XMLStreamException {
		this.readerXML(invocation, null);
	}
	
	public void readerXML(XMLEventInvocation invocation,EventFilter filter) throws IOException, XMLStreamException {
		XMLEventReader reader = null;
		if(invocation instanceof XMLURIInvocation){
			reader = XMLInputUtils.getXMLEventReader(((XMLURIInvocation)invocation).getURI(),filter);
		}else if(invocation instanceof XMLFileInvocation){
			XMLFileInvocation fileInvocation = ((XMLFileInvocation)invocation);
			if(!BlankUtils.isBlank(fileInvocation.getFileName())){
				reader = XMLInputUtils.getXMLEventReader(fileInvocation.getFileName(),filter);
			}else if(!BlankUtils.isBlank(fileInvocation.getFile())){
				reader = XMLInputUtils.getXMLEventReader(fileInvocation.getFile(),filter);
			}else if(!BlankUtils.isBlank(fileInvocation.getFileDescriptor())){
				reader = XMLInputUtils.getXMLEventReader(fileInvocation.getFileDescriptor(),filter);
			}
		}else if(invocation instanceof XMLStreamInvocation){
			reader = XMLInputUtils.getXMLEventReader(((XMLStreamInvocation)invocation).getInputStream(),filter);
		}
		while(reader.hasNext()){
			invocation.doEvent(reader.nextEvent());
		}
	}
	
	
	
}
