package com.woshidaniu.javamail.impl;

import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.javamail.MailConfig;
import com.woshidaniu.javamail.MailServer;
import com.woshidaniu.security.algorithm.DesBase64Codec;

/**
 * 邮件服务实现（使用javax.mail）
 * @author Penghui.Qu
 * 已统一实现客户端模式，并与系统参数进行了对象；参见 JavaMailClient子类；使用参见消息服务
 */
@Deprecated
public class JavaxMailServer  implements MailServer{

	private Properties props; // 系统属性
	private MailConfig config;// 邮件服务器配置
	private static Logger log = LoggerFactory.getLogger(JavaxMailServer.class);
	private DesBase64Codec p = new DesBase64Codec();
	
	/**
	 * 邮件服务设置
	 * @param config
	 */
	public void setConfig(MailConfig config) {
		this.config = config;
		props = System.getProperties(); // 获得系统属性对象
		props.put("mail.smtp.host", config.getHostName()); // 设置SMTP主机
		props.put("mail.smtp.auth", config.getNeedAuth());
		//props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.starttls.enable", "false");
		props.put("mail.smtp.socketFactory.fallback", "false");
		props.put("mail.smtp.port", config.getSmtpPort());
		props.put("mail.smtp.socketFactory.port", config.getFactoryPort());
		//log.debug(config);
	}

	/**
	 * 创建MIME邮件对象
	 * @param subject
	 * @param content
	 * @param sendTo
	 * @return
	 * @throws MessagingException
	 */
	private MimeMessage getMime(String subject, String content, String sendTo)
			throws MessagingException {

		// 创建邮件会话对象
		Session session = Session.getDefaultInstance(props, null);
		// 创建MIME邮件对象
		MimeMessage mimeMsg = new MimeMessage(session);
		// Multipart对象,邮件内容,标题,附件等内容均添加到其中后再生成MimeMessage对象
		Multipart multipart = new MimeMultipart();
		// 设置邮件主题
		mimeMsg.setSubject(subject);
		// 设置邮件正文
		BodyPart bp = new MimeBodyPart();
		StringBuilder body = new StringBuilder();
		body.append("<meta http-equiv=Content-Type content=text/html;charset=gb2312>")
			.append(content);
		bp.setContent(body.toString(), "text/html;charset=GB2312");
		multipart.addBodyPart(bp);
		mimeMsg.setContent(multipart);
		// 保存邮件
		mimeMsg.saveChanges();
		// 设置发信人
		mimeMsg.setFrom(new InternetAddress(config.getSendFrom()));
		// 收件 人
		mimeMsg.setRecipients(Message.RecipientType.TO,InternetAddress.parse(sendTo));

		return mimeMsg;
	}

	
	
	/*
	 * (non-Javadoc)
	 * @see com.woshidaniu.common.mail.MailServer#sendMail(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void sendMail(String subject, String content, String sendTo)
			throws Exception {

		if ("on".equalsIgnoreCase(config.getOnOff())){
			String username = p.decrypt(config.getUserName().getBytes());
			String password = p.decrypt(config.getPassword().getBytes());
	
			try {
				//创建邮件对象 
				MimeMessage mime = getMime(subject, content, sendTo);
				log.debug("创建邮件对象成功...");
				Session mailSession = Session.getInstance(props, null);
				log.debug("创建邮件会话成功...");
				Transport transport = mailSession.getTransport("smtp");
				//连接SMTP服务
				transport.connect((String) props.get("mail.smtp.host"), username,password);
				log.debug("smtp连接成功...");
				//发送邮件
				transport.sendMessage(mime,mime.getRecipients(Message.RecipientType.TO));
				log.debug("邮件发送成功...");
				//关闭连接
				transport.close();
			} catch (Exception e) {
				log.error("邮件发送失败....",e);
				throw e;
			}
		}
	}
	
	
	
	/*
	 * public void addFileAffix(String filename) throws Exception { try {
	 * BodyPart bp = new MimeBodyPart(); FileDataSource fileds = new
	 * FileDataSource(filename); bp.setDataHandler(new DataHandler(fileds));
	 * bp.setFileName(fileds.getName());
	 * 
	 * multipart.addBodyPart(bp); } catch (Exception e) { throw new
	 * Exception("增加邮件附件时发生错误..."); } }
	 */
}
