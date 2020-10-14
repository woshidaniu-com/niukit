package com.woshidaniu.javamail.utils;

import java.util.Properties;

import javax.mail.Session;
import javax.servlet.http.HttpServletRequest;

import com.woshidaniu.javamail.JavaMailKey;
import com.woshidaniu.javamail.auth.PropsAuthenticator;

/**
 * 
 *@类名称		： JavaMailUtils.java
 *@类描述		：
 *@创建人		：kangzhidong
 *@创建时间	：2017年4月14日 上午9:57:04
 *@修改人		：
 *@修改时间	：
 *@版本号		: v1.0
 */
public abstract class JavaMailUtils {

	/**
	 * 获取邮件发送期间上下文环境信息，如服务器的主机名、端口号、协议名称等
	 */
	public static Session getSession(Properties props) {
		Session ret = null;
		if (Boolean.parseBoolean(props.getProperty(JavaMailKey.MAIL_SMTP_AUTH, "false"))) {
			ret = Session.getInstance(props, new PropsAuthenticator(props));
		} else {
			ret = Session.getInstance(props);
		}
		return ret;
	}

	public static String getRootURL(HttpServletRequest request) {
		return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath() + "/";
	}
}
