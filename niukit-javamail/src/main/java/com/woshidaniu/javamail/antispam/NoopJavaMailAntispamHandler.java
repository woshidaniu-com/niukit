package com.woshidaniu.javamail.antispam;

import javax.mail.Message;
import javax.mail.MessagingException;

import com.woshidaniu.javamail.JavaMailAntispamHandler;
import com.woshidaniu.javamail.conf.EmailBody;

/**
 * @author 1571
 */
public class NoopJavaMailAntispamHandler implements JavaMailAntispamHandler{

	@Override
	public void setAntispam(Message message, EmailBody email) throws MessagingException {
	}
}
