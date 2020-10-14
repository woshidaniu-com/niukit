/**
 * 
 */
package com.woshidaniu.safety.csrf;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.safety.csrf.strategy.CsrfVerifyStrategy;
import com.woshidaniu.safety.csrf.strategy.PostCsrfVerifyStrategy;
import com.woshidaniu.web.servlet.filter.AbstractAccessControlFilter;
import com.woshidaniu.web.utils.WebUtils;

/**
 * <p>
 *   <h3>niutal框架<h3>
 *   说明：csrf漏洞拦截器：</br>
 *   
 *   A:校验referer头信息（必须满足）</br>
 *   
 *   B:校验CSRF token（至少满足一个）</br>
 *   	<ul>
 *   	<li>1.	校验提交的表单中是否有csrf token</br>
 *  	<li>2.	校验请求URL中是否有csrf token</br>
 *   	<li>3. 	ajax异步请求头中是否有csrf token</br>
 *   	</ul>
 *   以上A,B条件必须同时满足</br>
 *   
 *   class com.woshidaniu.safety.csrf.CsrfFilter
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月9日下午2:05:00
 */
public class CsrfFilter extends AbstractAccessControlFilter {

	private static final Logger log = LoggerFactory.getLogger(CsrfFilter.class);
	
	private CsrfVerifyStrategy verifyStrategy;
	
	private static final String SKIP_CSRF_TOKEN_VALIDATE_KEY = "SKIP_CSRF_TOKEN_VALIDATE_KEY";
	
	@Override
	protected boolean isAccessAllowed(ServletRequest request,ServletResponse response) {
	
		String skip = (String) request.getAttribute(SKIP_CSRF_TOKEN_VALIDATE_KEY);
		if(Boolean.parseBoolean(skip)) {
			return true;
		}
		/**
		 * 获取请求信息中csrf token信息
		 * 		<ul>
		 *   	<li>1.	校验提交的表单中是否有csrf token</br>
		 *  	<li>2.	校验请求URL中是否有csrf token</br>
		 *   	<li>3. 	ajax异步请求头中是否有csrf token</br>
		 *   	</ul>
		 *   
		 *   验证所有POST请求
		 */
		HttpServletRequest httpRequest = WebUtils.toHttp(request);
		if(log.isDebugEnabled()){
			log.debug("CSRF过滤器拦截请求：URL:{},METHOD:{}.", new Object[]{httpRequest.getRequestURL(),httpRequest.getMethod()});
		}
		
		/**
		 * 策略默认为验证POST提交的请求
		 */
		if(verifyStrategy == null){
			verifyStrategy = new PostCsrfVerifyStrategy();
		}
		
		return CsrfGuard.getInstance().verifyCsrfToken(httpRequest, verifyStrategy);
	}

	@Override
	protected void onAccessDeniad(ServletRequest request,ServletResponse response) {
		HttpServletResponse httpResponse = WebUtils.toHttp(response);
		
		if(log.isDebugEnabled()){
			log.debug("请求被拦截，原因：该请求没有携带CSRF TOKEN相关信息，可能产生威胁。");
		}
		
		try {
			httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Request Denied!");
		} catch (IOException e) {
			if(log.isErrorEnabled()){
				log.error("Send Response Error:{}.",e.getCause());
			}
			e.printStackTrace();
		}
	}

	public CsrfVerifyStrategy getVerifyStrategy() {
		return verifyStrategy;
	}

	public void setVerifyStrategy(CsrfVerifyStrategy verifyStrategy) {
		this.verifyStrategy = verifyStrategy;
	}

	
	
}
