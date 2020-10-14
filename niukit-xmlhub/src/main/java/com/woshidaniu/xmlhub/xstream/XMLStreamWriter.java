package com.woshidaniu.xmlhub.xstream;

import java.io.File;
import java.io.OutputStream;

public interface XMLStreamWriter {

	public <T> String writeToXML(T source);
	
	public <T> String writeToXML(T source,String outPath);
	
	public <T> String writeToXML(T source,File outFile);
	
	public <T> String writeToXML(T source,OutputStream outPut);
	
	public <T> T toBean(String xmlStr, Class<T> clazz);
	
	public <T> T toBean(File xmlfile, Class<T> clazz);
	
}
