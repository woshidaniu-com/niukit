/**
 * 
 */
package com.woshidaniu.web.servlet.filter.chain;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * <p>
 *   <h3>niutal框架<h3>
 *   说明：该接口定义如何获取对应请求的过滤器链
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年7月19日上午11:49:13
 */
public interface FilterChainResolver {

	/**
	 * 
	 * <p>方法说明：获取FilterChian<p>
	 * <p>作者：a href="#">Zhangxiaobin[1036]<a><p>
	 * <p>时间：2016年7月19日上午11:50:20<p>
	 */
	FilterChain getChain(ServletRequest request, ServletResponse response, FilterChain originalChain);
	
}
