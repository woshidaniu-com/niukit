/**
 * 
 */
package com.woshidaniu.web.servlet.filter;

import javax.servlet.Filter;

/**
 * <p>
 *   <h3>niutal框架<h3>
 *   说明：给过滤器设置路径
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年7月19日下午7:49:01
 */
public interface PathProcessor {

	
	/**
	 * 
	 * <p>方法说明：TODO<p>
	 * <p>作者：a href="#">Zhangxiaobin[1036]<a><p>
	 * <p>时间：2016年7月19日下午7:49:30<p>
	 */
	Filter processPath(String path);
}
