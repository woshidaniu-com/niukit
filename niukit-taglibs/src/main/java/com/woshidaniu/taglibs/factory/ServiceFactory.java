package com.woshidaniu.taglibs.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 服务工厂（用于取spring容器中定义的bean）
 * @author Administrator
 * 
 */
public class ServiceFactory implements ApplicationContextAware, BeanFactoryAware{
	
	public ServiceFactory() {

	}

	protected static BeanFactory beanFactory = null;
	protected static ApplicationContext springContext = null;
	protected static Logger LOG = LoggerFactory.getLogger(ServiceFactory.class);
	
	
	/**
	 * 常用于知道配置的服务名取服务
	 * 
	 * @param name
	 * @return
	 */
	public static Object getService(String name) {
		Object result = null;
		try {
			if(springContext != null ){
				result = getSpringContext().getBean(name);
			}
			if ( result == null && beanFactory != null){
				result = getBeanFactory().getBean(name);
			}
		} catch (BeansException e) {
			LOG.error(e.getMessage());
		}
		return result;
	}


	/**
	 * 用于按规范命名的服务：通过类的类型取服务
	 * 
	 * @param name
	 * @return
	 */
	public static <T> T getService(Class<T> requiredType) {
		T result = null;
		try {
			if(springContext != null ){
				result = getSpringContext().getBean(requiredType);
			}
			if ( result == null && beanFactory != null){
				result = getBeanFactory().getBean(requiredType);
			}
		} catch (BeansException e) {
			LOG.error(e.getMessage());
		}
		return result;
	}
	
	
	/**
	 * 用于按规范命名的服务：通过类的类型取服务
	 * 
	 * @param name
	 * @return
	 */
	public static <T> T getService(String name,Class<T> requiredType) {
		T result = null;
		try {
			if(springContext != null ){
				result = getSpringContext().getBean(name,requiredType);
			}
			if ( result == null && beanFactory != null){
				result = getBeanFactory().getBean(name,requiredType);
			}
		} catch (BeansException e) {
			LOG.error(e.getMessage());
		}
		return result;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		ServiceFactory.beanFactory = beanFactory;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)throws BeansException {
		ServiceFactory.springContext = applicationContext;
	}

	public static BeanFactory getBeanFactory() {
		return beanFactory;
	}

	public static ApplicationContext getSpringContext() {
		return springContext;
	}

}
