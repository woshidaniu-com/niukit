package com.woshidaniu.xmlhub.core.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * 
 *@类名称	: LoggerErrorHandler.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 29, 2016 4:27:19 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class LoggerErrorHandler implements ErrorHandler{

	protected static Logger LOG = LoggerFactory.getLogger(LoggerErrorHandler.class);
	
	@Override
	public void error(SAXParseException exception) throws SAXException {
		LOG.error(exception.getLocalizedMessage(),exception);
	}

	@Override
	public void fatalError(SAXParseException exception) throws SAXException {
		LOG.error(exception.getLocalizedMessage(),exception);
	}

	@Override
	public void warning(SAXParseException exception) throws SAXException {
		LOG.warn(exception.getLocalizedMessage(),exception);
	}

}
