package com.woshidaniu.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.subject.Subject;

public interface LogoutListener {

	void beforeLogout(Subject subject, ServletRequest request, ServletResponse response);

	void onLogoutFail(Subject subject, Exception ex);

	void onLogoutSuccess(ServletRequest request, ServletResponse response);

}
