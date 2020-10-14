/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.ticket.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @className ： ZFTicketAuthShiroFilter
 * @description ： ticket认证的认证Filter，配置在Shiro的Filter链中
 * 
 *              当请求包含必须的登录参数时，尝试登录操作，登录成功后并不重定向，而是访问目标地址
 *              后续Filter链中的Filter将会处理请求
 * @author ：康康（1571）
 * @date ： 2018年5月2日 下午1:40:32
 * @version V1.0
 */
public class ZFTicketAuthcShiroFilter extends ZFTicketLoginShiroFilter {

	protected Logger log = LoggerFactory.getLogger(ZFTicketAuthcShiroFilter.class);

	/**
	 * 
	 * @description ： 当用户未登录时，执行登录逻辑，无论登录是否成功，都继续执行下面的Filter
	 * @author ：康康（1571）
	 * @date ：2018年5月2日 上午10:26:27
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		this.executeLogin(request, response);
		return true;
	}

	/**
	 * 
	 * @description	：登录成功，什么也不做
	 * @author 		：康康（1571）
	 * @date 		：2018年5月2日 下午2:27:19
	 * @param token
	 * @param subject
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
			ServletResponse response) throws Exception {
		return true;
	}

	/**
	 * 
	 * @description	： 登录失败，什么也不做
	 * @author 		：康康（1571）
	 * @date 		：2018年5月2日 下午2:27:22
	 * @param token
	 * @param e
	 * @param request
	 * @param response
	 * @return
	 */
	@Override
	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request,
			ServletResponse response) {
		return true;
	}
}
