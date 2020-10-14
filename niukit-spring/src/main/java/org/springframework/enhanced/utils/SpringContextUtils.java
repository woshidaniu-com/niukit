package org.springframework.enhanced.utils;

import org.springframework.enhanced.context.SpringClassPathXmlInstanceContext;
import org.springframework.enhanced.context.SpringContext;

public class SpringContextUtils {

	private static SpringContext context = new SpringClassPathXmlInstanceContext(new String[0]);

	public SpringContextUtils() {
	}

	public static SpringContext getContext() {
		return SpringContextUtils.context;
	}

	public static void setContext(SpringContext context) {
		SpringContextUtils.context = context;
	}

}
