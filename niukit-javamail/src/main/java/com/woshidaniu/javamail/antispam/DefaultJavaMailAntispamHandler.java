package com.woshidaniu.javamail.antispam;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.mail.Message;
import javax.mail.MessagingException;

import com.woshidaniu.basicutils.StringUtils;
import com.woshidaniu.javamail.JavaMailAntispamHandler;
import com.woshidaniu.javamail.JavaMailClient;
import com.woshidaniu.javamail.conf.EmailBody;

/**
 * @author 1571
 */
public class DefaultJavaMailAntispamHandler implements JavaMailAntispamHandler{
	
	protected SimpleDateFormat format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");

	@Override
	public synchronized void setAntispam(Message message, EmailBody email) throws MessagingException {
		//=============反垃圾邮件处理====================
		// 设置优先级(1:紧急 3:普通 5:低)
		message.setHeader(JavaMailClient.HEADER_PRIORITY, email != null ? StringUtils.getSafeStr(email.getPriority(), "3") : "3");
		message.setHeader(JavaMailClient.HEADER_MSMAIL_PRIORITY, "Normal");
		// 声明邮件地址和头信息披上outlook的马甲;
		message.setHeader(JavaMailClient.HEADER_MAILER, "Microsoft Outlook Express 6.00.2900.2869");
		message.setHeader(JavaMailClient.HEADER_MIMEOLE, "Produced By Microsoft MimeOLE V6.00.2900.2869");
		message.setHeader(JavaMailClient.HEADER_DATE, format.format(new Date()));
	}
}
