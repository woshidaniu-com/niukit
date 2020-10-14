package org.springframework.enhanced.config;

import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;


public class CustomPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

	/**
	 * 重写父类处理过程
	 */
	protected void processProperties(ConfigurableListableBeanFactory beanFactory,Properties props) throws BeansException {
		super.processProperties(beanFactory, props);
		//load properties to ctxPropertiesMap  
        for (Object key : props.keySet()) {  
            String keyStr = key.toString();  
            String value = props.getProperty(keyStr);  
        }  
	}
	
}
