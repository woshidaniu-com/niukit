/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.pac4j.ext.filter;

import javax.servlet.ServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.web.filter.authc.UserFilter;
import org.apache.shiro.web.util.WebUtils;
import org.pac4j.core.context.Pac4jConstants;

public class Pac4jUserFilter extends UserFilter {

	@Override
    protected void saveRequest(ServletRequest request) {
        // 还是先执行着shiro自己的方法
        super.saveRequest(request);
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute(Pac4jConstants.REQUESTED_URL, WebUtils.toHttp(request).getRequestURI());
    }
	
}
