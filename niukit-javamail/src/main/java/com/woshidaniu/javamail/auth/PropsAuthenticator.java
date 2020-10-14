package com.woshidaniu.javamail.auth;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

import com.woshidaniu.javamail.JavaMailKey;

public class PropsAuthenticator extends Authenticator {

	protected Properties props = new Properties();
	
	public PropsAuthenticator(Properties props) {
		this.props = props;
	}

	public Properties getProps() {
		return props;
	}

	public void setProps(Properties props) {
		this.props = props;
	}

	protected PasswordAuthentication getPasswordAuthentication() {
		// 用户名、密码
        String userName = props.getProperty(JavaMailKey.MAIL_SMTP_USER, props.getProperty(JavaMailKey.MAIL_USER));
        String password = props.getProperty(JavaMailKey.MAIL_SMTP_PASSWORD, props.getProperty(JavaMailKey.MAIL_PASSWORD));
        return new PasswordAuthentication(userName, password);
	}
	
}
