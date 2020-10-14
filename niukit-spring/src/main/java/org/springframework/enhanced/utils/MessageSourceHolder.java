package org.springframework.enhanced.utils;

import java.util.Locale;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.enhanced.context.MultipleMessageSource;
import org.springframework.enhanced.factory.EnhancedBeanFactory;

public class MessageSourceHolder extends EnhancedBeanFactory {

	private static Object[] EMPTY = new Object[0]; 
	private static MultipleMessageSource messageSource = null;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		MessageSourceHolder.messageSource = BeanFactoryUtils.beanOfType(getApplicationContext(), MultipleMessageSource.class);
	}
	
	public static String getMessage(String key) throws NoSuchMessageException{
		return messageSource.getMessage(key, EMPTY, Locale.getDefault());
	}
	
	public static String getMessage(String key, Object[] args) throws NoSuchMessageException{
		return messageSource.getMessage(key, args, Locale.getDefault());
	}
	
	public static String getMessage(String key, Object[] args, String defaultMessage){
		return messageSource.getMessage(key, args, defaultMessage, Locale.getDefault());
	}

	public static String getMessage(String key, Locale locale) throws NoSuchMessageException{
		return messageSource.getMessage(key, EMPTY, locale);
	}
	
	public static String getMessage(String key, Object[] args, Locale locale) throws NoSuchMessageException{
		return messageSource.getMessage(key, args, locale);
	}
	
	public static String getMessage(String key, Object[] args, String defaultMessage, Locale locale){
		return messageSource.getMessage(key, args, defaultMessage, locale);
	}
	
	public static String getMessage(MessageSourceResolvable resolvable) throws NoSuchMessageException{
		return messageSource.getMessage(resolvable, Locale.getDefault());
	}
	
	public static String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException{
		return messageSource.getMessage(resolvable, locale);
	}
	
}
