/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.thauthall.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;

/**
 * 
 * @className	： ThauthallAuthcShiroFilter
 * @description	： 华中师范大学单点登录认证Shiro过滤器
 * @author 		：康康（1571）
 * @date		： 2018年5月11日 上午10:07:02
 * @version 	V1.0
 */
public class ThauthallAuthcShiroFilter extends AuthenticatingFilter{

	@Override
	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
		return null;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		return false;
	}

}
