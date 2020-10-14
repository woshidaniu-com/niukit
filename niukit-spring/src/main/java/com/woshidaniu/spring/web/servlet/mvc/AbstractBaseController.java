package com.woshidaniu.spring.web.servlet.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractBaseController {

	protected static final transient Logger log = LoggerFactory.getLogger(AbstractBaseController.class);
	
	protected void logException(Exception ex) {
		if (log.isErrorEnabled()){
			log.error(ex.getMessage(), ex);
		}
	}
	
	protected void logException(Object source,Exception ex) {
		logException(ex);
	}
	
}
