/**
 * 
 */
package com.woshidaniu.safety.xss;

import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.web.servlet.filter.AbstractPathMatchFilter;
import com.woshidaniu.web.utils.WebUtils;

/**
 * <p>
 *   <h3>niutal框架<h3>
 *   说明：安全设置：http header
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年7月19日下午8:07:15
 */
public class HttpServletRequestHeaderFilter extends AbstractPathMatchFilter{
	
	private static final Logger log = LoggerFactory.getLogger(HttpServletRequestHeaderFilter.class);
	
	protected String x_frame_options = "SAMEORIGIN";
	
	protected String x_xss_protection = "1; mode=block";
	
	protected String x_content_type_options = "nosniff";

	protected void onFilterConfigSet(FilterConfig filterConfig) throws Exception {
		String initParameter_HTTP_HEAD_X_FRAME_OPTIONS = filterConfig.getInitParameter("X_FRAME_OPTIONS");
		if(initParameter_HTTP_HEAD_X_FRAME_OPTIONS != null && initParameter_HTTP_HEAD_X_FRAME_OPTIONS.trim().length() > 0){
			x_frame_options = initParameter_HTTP_HEAD_X_FRAME_OPTIONS.trim();
		}
		
		String initParameter_HTTP_HEAD_X_XSS_PROTECTION = filterConfig.getInitParameter("X_XSS_PROTECTION");
		if(initParameter_HTTP_HEAD_X_XSS_PROTECTION != null && initParameter_HTTP_HEAD_X_XSS_PROTECTION.trim().length() > 0){
			x_xss_protection = initParameter_HTTP_HEAD_X_XSS_PROTECTION.trim();
		}
		
		String initParameter_HTTP_HEAD_X_CONTENT_TYPE_OPTIONS = filterConfig.getInitParameter("X_CONTENT_TYPE_OPTIONS");
		if(initParameter_HTTP_HEAD_X_CONTENT_TYPE_OPTIONS != null && initParameter_HTTP_HEAD_X_CONTENT_TYPE_OPTIONS.trim().length() > 0){
			x_content_type_options = initParameter_HTTP_HEAD_X_CONTENT_TYPE_OPTIONS.trim();
		}
		
	}
	
	@Override
	protected boolean onPreHandle(ServletRequest request,
			ServletResponse response) throws Exception {
		HttpServletResponse httpResponse = WebUtils.toHttp(response);
		//iframe策略
		httpResponse.setHeader("X-Frame-Options", x_frame_options);
		//X-XSS-Protection：主要是用来防止浏览器中的反射性xss，IE，chrome和safari（webkit）支持这个header,有以下两种方式：
		//1; mode=block – 开启xss防护并通知浏览器阻止而不是过滤用户注入的脚本；
		//1; report=http://site.com/report – 这个只有chrome和webkit内核的浏览器支持，这种模式告诉浏览器当
		//发现疑似xss攻击的时候就将这部分数据post到指定地址。
		httpResponse.setHeader("X-XSS-Protection", x_xss_protection);
		//防止在IE9、chrome和safari中的MIME类型混淆攻击
		httpResponse.setHeader("X-Content-Type-Options", x_content_type_options);
		
		if(log.isTraceEnabled()){
			log.trace("Filter:{} Set HTTP HEADER:X-Frame-Options:{};X-XSS-Protection:{};X-Content-Type-Options:{}.", 
					new Object[]{getName(),x_frame_options,x_xss_protection,x_content_type_options});
		}
		
		return true;
	}

	public String getX_frame_options() {
		return x_frame_options;
	}

	public void setX_frame_options(String x_frame_options) {
		this.x_frame_options = x_frame_options;
	}

	public String getX_xss_protection() {
		return x_xss_protection;
	}

	public void setX_xss_protection(String x_xss_protection) {
		this.x_xss_protection = x_xss_protection;
	}

	public String getX_content_type_options() {
		return x_content_type_options;
	}

	public void setX_content_type_options(String x_content_type_options) {
		this.x_content_type_options = x_content_type_options;
	}
	
}
