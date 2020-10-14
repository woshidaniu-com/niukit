package com.woshidaniu.javamail.conf;


public class EmailFrom {

	/**
	 * 名称
	 */
	protected String name;
	/**
	 * 邮箱
	 */
	protected String email;
	/**
	 * 是否要求阅读回执(收件人阅读邮件时会提示回复发件人,表明邮件已收到,并已阅读)
	 */
	protected boolean notification = false;

	public EmailFrom(){}
	
	public EmailFrom(String name, String email, boolean notification) {
		this.name = name;
		this.email = email;
		this.notification = notification;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isNotification() {
		return notification;
	}

	public void setNotification(boolean notification) {
		this.notification = notification;
	}

}

