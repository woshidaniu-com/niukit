package com.woshidaniu.web.servlet3;

import java.util.Set;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * *******************************************************************
 * 
 * @className ： CustomServletContainerInitializer
 * @description ： TODO(描述这个类的作用)
 * @author ： kangzhidong
 * @date ： Feb 2, 2016 3:39:39 PM
 * @version V1.0
 *          *******************************************************************
 */
@HandlesTypes({ AsyncMessagePushServlet.class })
public class CustomServletContainerInitializer implements
		ServletContainerInitializer {

	public static final Log log = LogFactory.getLog(CustomServletContainerInitializer.class);

	public static final String JAR_HELLO_URL = "/jarhello";

	public void onStartup(Set<Class<?>> c, ServletContext servletContext)
			throws ServletException {
		log.info("CustomServletContainerInitializer is loaded here...");

		log.info("now ready to add servlet : "
				+ AsyncMessagePushServlet.class.getName());
		/*
		 * ServletRegistration.Dynamic servlet = servletContext.addServlet(
		 * AsyncMessagePushServlet.class.getSimpleName(),
		 * AsyncMessagePushServlet.class); servlet.addMapping(JAR_HELLO_URL);
		 */

		/*
		 * log.info("now ready to add filter : " +
		 * JarWelcomeFilter.class.getName()); FilterRegistration.Dynamic filter
		 * = servletContext.addFilter( JarWelcomeFilter.class.getSimpleName(),
		 * JarWelcomeFilter.class);
		 * 
		 * EnumSet<DispatcherType> dispatcherTypes = EnumSet
		 * .allOf(DispatcherType.class);
		 * dispatcherTypes.add(DispatcherType.REQUEST);
		 * dispatcherTypes.add(DispatcherType.FORWARD);
		 * 
		 * filter.addMappingForUrlPatterns(dispatcherTypes, true,
		 * JAR_HELLO_URL);
		 * 
		 * log.info("now ready to add listener : " +
		 * JarWelcomeListener.class.getName());
		 * servletContext.addListener(JarWelcomeListener.class);
		 */
	}
}