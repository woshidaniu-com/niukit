package com.woshidaniu.web.servlet.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
/**
 * <p>
 *   <h3>niutal框架<h3>
 *   说明：访问控制过滤器
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年7月20日上午10:49:30
 */
public abstract class AbstractAccessControlFilter extends AbstractPathMatchFilter {
	
	@Override
	protected boolean onPreHandle(ServletRequest request, ServletResponse response) throws Exception {
		if(isAccessAllowed(request, response)){
			return true;
		}
		onAccessDeniad(request, response);
		return false;
	}
	
	/**
	 * 
	 * <p>方法说明：判断是否允许访问<p>
	 * <p>作者：a href="#">Zhangxiaobin[1036]<a><p>
	 * <p>时间：2016年7月20日上午10:54:13<p>
	 */
	protected abstract boolean isAccessAllowed(ServletRequest request,ServletResponse response);
	
	/**
	 * 
	 * <p>方法说明：当访问被禁止是，需要做的操作<p>
	 * <p>作者：a href="#">Zhangxiaobin[1036]<a><p>
	 * <p>时间：2016年7月20日上午10:55:24<p>
	 */
	protected abstract void onAccessDeniad(ServletRequest request,ServletResponse response);
	
}
