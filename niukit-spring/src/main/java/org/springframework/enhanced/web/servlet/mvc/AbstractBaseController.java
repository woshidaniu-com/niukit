package org.springframework.enhanced.web.servlet.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractBaseController {

	protected static final transient Logger LOG = LoggerFactory.getLogger(AbstractBaseController.class);
	
	protected void logException(Exception ex) {
		if (LOG.isErrorEnabled()){
			LOG.error(ex.getMessage(), ex);
		}
	}
	
}

