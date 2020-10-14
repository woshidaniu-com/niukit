/**
 * 
 */
package com.woshidaniu.safety.csrf.strategy;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *   <h3>niutal框架<h3>
 *   说明：csrf 验证策略
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月9日下午4:58:43
 */
public interface CsrfVerifyStrategy {

	/**
	 * 
	 * <p>方法说明：判断是否需要验证<p>
	 * <p>作者：a href="#">Zhangxiaobin[1036]<a><p>
	 * <p>时间：2016年8月9日下午5:23:02<p>
	 */
	boolean needVerify(HttpServletRequest request);
	
}
