/**
 * 
 */
package com.woshidaniu.safety.csrf.strategy;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *   <h3>niutal框架<h3>
 *   说明：只验证POST提交的请求
 *   class com.woshidaniu.web.filter.security.csrf.PostCsrfVerifyStrategy
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月9日下午5:24:08
 */
public class PostCsrfVerifyStrategy implements CsrfVerifyStrategy{

	@Override
	public boolean needVerify(HttpServletRequest request) {
		return request.getMethod().equalsIgnoreCase("POST");
	}

}
