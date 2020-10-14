package com.woshidaniu.javamail;

import javax.mail.Message;
import javax.mail.MessagingException;

import com.woshidaniu.javamail.conf.EmailBody;

/**
 * @author 1571
 */
public interface JavaMailAntispamHandler {

	void setAntispam(Message message,EmailBody email) throws MessagingException;
}
