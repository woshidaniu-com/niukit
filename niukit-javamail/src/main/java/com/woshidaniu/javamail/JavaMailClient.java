package com.woshidaniu.javamail;

import java.io.InputStream;

import com.woshidaniu.javamail.conf.EmailBody;
import com.woshidaniu.javamail.provider.EmailPropertiesProvider;

public interface JavaMailClient {

	public static final String HEADER_PRIORITY = "X-Priority";
	public static final String HEADER_MSMAIL_PRIORITY = "X-MSMail-Priority";
	public static final String HEADER_MAILER = "X-Mailer";
	public static final String HEADER_MIMEOLE = "X-MimeOLE";
	public static final String HEADER_DATE = "Date";
	public static final String HEADER_DISPOSITION_NOTIFICATION_TO = "Disposition-Notification-To";
	
	public EmailPropertiesProvider getPropsProvider();
	
	public boolean sendText(EmailBody email) throws Exception;
	
	public boolean sendMime(EmailBody email) throws Exception;
	
	public boolean sendMime(InputStream email) throws Exception;
	
	public void receive(String subject, String content ,String sendTo) throws Exception;
	
}

