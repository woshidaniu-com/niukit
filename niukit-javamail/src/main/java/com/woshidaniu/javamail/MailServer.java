package com.woshidaniu.javamail;

/**
 * 邮件服务
 * @author Penghui.Qu
 * 已统一实现客户端模式，并与系统参数进行了对象；参见 JavaMailClient
 */
@Deprecated
public interface MailServer {

	/**
	 * 单一发送邮件
	 * @param subject 标题
	 * @param content 内容
	 * @param sendTo  收件人邮件
	 * @throws Exception
	 */
	public void sendMail(String subject, String content ,String sendTo) throws Exception;
}
