package com.woshidaniu.javamail;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;

import com.woshidaniu.javamail.antispam.DefaultJavaMailAntispamHandlerFactory;
import com.woshidaniu.javamail.conf.EmailBody;
import com.woshidaniu.javamail.provider.EmailPropertiesProvider;

public abstract class JavaMailClientAdapter implements JavaMailClient {

	protected SimpleDateFormat format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
	protected EmailPropertiesProvider propsProvider;
	
	private JavaMailAntispamHandlerFactory handlerFactory = new DefaultJavaMailAntispamHandlerFactory();
	
	@Override
	public boolean sendText(EmailBody email) throws Exception {
		return false;
	}

	@Override
	public boolean sendMime(EmailBody email) throws Exception {
		return false;
	}

	@Override
	public boolean sendMime(InputStream email) throws Exception {
		return false;
	}

	@Override
	public void receive(String subject, String content, String sendTo) throws Exception {

	}

	/**
	 * 设置反垃圾邮件
	 */
	public void setAntispam(Message message,EmailBody email) throws MessagingException {
		
		EmailPropertiesProvider provider = this.getPropsProvider();
		
		String handlerName = "default";
		
		if(provider == null || provider.props() == null){
			handlerName = "default";
		}else{
			Properties properties = provider.props();
			handlerName = properties.getProperty(JavaMailKey.MAIL_ANTISPAM_HANDLER,"default");
		}
		JavaMailAntispamHandler handler = handlerFactory.getHandler(handlerName);
		handler.setAntispam(message, email);
	}

	@Override
	public EmailPropertiesProvider getPropsProvider() {
		return propsProvider;
	}

	public void setPropsProvider(EmailPropertiesProvider propsProvider) {
		this.propsProvider = propsProvider;
	}
	
}
