package com.woshidaniu.javamail.antispam;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.javamail.JavaMailAntispamHandler;
import com.woshidaniu.javamail.JavaMailAntispamHandlerFactory;

/**
 * @author 1571
 */
public class DefaultJavaMailAntispamHandlerFactory implements JavaMailAntispamHandlerFactory{
	
	private static final Logger log = LoggerFactory.getLogger(DefaultJavaMailAntispamHandlerFactory.class);
	
	private Map<String,JavaMailAntispamHandler> mapping = new HashMap<String,JavaMailAntispamHandler>();

	public DefaultJavaMailAntispamHandlerFactory() {
		super();
		this.mapping.put("default", new DefaultJavaMailAntispamHandler());
		this.mapping.put("noop", new NoopJavaMailAntispamHandler());
	}

	@Override
	public JavaMailAntispamHandler getHandler(String name) {
		JavaMailAntispamHandler handler = this.mapping.get(name);
		if(handler != null){
			return handler;
		}else{
			Class<?> clazz = null;
			try {
				clazz = Class.forName(name);
			} catch (ClassNotFoundException e) {
				log.warn("can't find class[{}],use noop handler",name);
				return this.mapping.get("noop");
			}
			
			Object instance = null;
			try {
				instance = clazz.newInstance();
			} catch (Exception e) {
				log.warn("create instance of class[{}] error,cuase:{},use noop handler",name,e.getMessage());
				return this.mapping.get("noop");
			}
			
			return (JavaMailAntispamHandler)instance;
		}
	}
}
