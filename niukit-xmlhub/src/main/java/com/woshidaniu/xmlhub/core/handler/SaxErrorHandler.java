package com.woshidaniu.xmlhub.core.handler;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * 
 *@类名称	: SaxErrorHandler.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 29, 2016 4:31:51 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class SaxErrorHandler implements ErrorHandler{

	@Override
	public void error(SAXParseException exception) throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fatalError(SAXParseException exception) throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void warning(SAXParseException exception) throws SAXException {
		// TODO Auto-generated method stub
		
	}

}
