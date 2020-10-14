package com.woshidaniu.xmlhub.core.factory;

import java.io.Serializable;

import javax.xml.stream.events.XMLEvent;


public interface XMLEventInvocation extends Serializable {
	
	void doEvent(XMLEvent event);
	
}
