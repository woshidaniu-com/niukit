package com.woshidaniu.shiro.filter.impl;

import java.util.Enumeration;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.shiro.filter.LogoutListener;
import com.woshidaniu.web.utils.WebRequestUtils;
import com.woshidaniu.web.utils.WebUtils;

/**
 * 
 * @类名称 ： DefaultLogoutListener.java
 * @类描述 ：默认的注销监听：主要用于记录行为日志
 * @创建人 ：kangzhidong
 * @创建时间 ：2017年8月29日 上午8:33:20
 * @修改人 ：
 * @修改时间 ：
 * @版本号 : v1.0
 */
public class DefaultLogoutListener implements LogoutListener {

	private static final Logger LOG = LoggerFactory.getLogger(DefaultLogoutListener.class);

	@Override
	public void beforeLogout(Subject subject, ServletRequest request, ServletResponse response) {
		
	}

	@Override
	public void onLogoutFail(Subject subject, Exception ex) {
		LOG.debug("Shiro Logout Fail : {0}", ex.getMessage());
	}

	@Override
	public void onLogoutSuccess(ServletRequest request, ServletResponse response) {

		HttpServletRequest httpRequest = WebUtils.toHttp(request);

		String hostIP = WebRequestUtils.getRemoteAddr(httpRequest);
		String sessionId = httpRequest.getRequestedSessionId();

		StringBuilder builder = new StringBuilder("Shiro Logout {");

		builder.append("sessionId : ").append(sessionId);

		Enumeration<String> names = httpRequest.getHeaderNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			builder.append(name).append(" : ").append(httpRequest.getHeader(name));
		}
		builder.append("hostIP : ").append(hostIP);

		builder.append("}");

		LOG.debug(builder.toString());

	}

}
