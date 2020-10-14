package com.woshidaniu.shiro.filter.session;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticationFilter;

import com.woshidaniu.web.core.http.HttpStatus;

/**
 * 
 * <p>
 *   <h3>niutal框架<h3>
 *   说明：会话超时过滤器
 * <p>
 * @author <a href="mailto:337836629@qq.com">Penghui.Qu[445]<a>
 * @version 2017年2月16日下午3:15:33
 */
public  class SessionExpiredFilter extends AuthenticationFilter  {

	@Override
	public boolean onPreHandle(ServletRequest  request, ServletResponse response, Object mappedValue) {
        
		Subject subject =  SecurityUtils.getSubject();
		
         if(subject==null || !subject.isAuthenticated()){
            if ("XMLHttpRequest".equalsIgnoreCase(((HttpServletRequest) request).getHeader("X-Requested-With"))) {
            	  HttpServletResponse httpResponse = (HttpServletResponse) response;
                  httpResponse.setStatus(HttpStatus.SC_SESSION_TIMEOUT);
                return false;
            }
        }
        return true;
    }

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		return isAccessAllowed(request, response, mappedValue);
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) {
		return onAccessDenied(request, response);
	}

}
