/**
 * 
 */
package com.woshidaniu.safety.csrf.strategy;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *   <h3>niutal框架<h3>
 *   说明：全部请求需要验证
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月9日下午5:25:57
 */
public class AllCsrfVerifyStrategy implements CsrfVerifyStrategy {

	/* (non-Javadoc)
	 * @see com.woshidaniu.web.filter.security.csrf.CsrfVerifyStrategy#needVerify(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public boolean needVerify(HttpServletRequest request) {
		return true;
	}

}
