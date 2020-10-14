/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.cas321.shiro.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.web.servlet.OncePerRequestFilter;
import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.woshidaniu.web.utils.WebUtils;

/**
 * @author 		：康康（1571）
 */
public class ZFCas321AuthenticationFilter extends OncePerRequestFilter implements InitializingBean{
	
	private static final Logger log = LoggerFactory.getLogger(ZFCas321AuthenticationFilter.class);

	protected AuthenticationFilter delegate =  new AuthenticationFilter();
	
	private String casServerLoginUrl;
	private String serverName;
	private String service;
	private boolean encodeServiceUrl = true;
	protected boolean redirectToSaveRequestUri = false;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		
		this.delegate.setIgnoreInitConfiguration(true);
		
		this.delegate.setServerName(this.serverName);
		this.delegate.setCasServerLoginUrl(this.casServerLoginUrl);
		this.delegate.setService(service);
		this.delegate.setEncodeServiceUrl(encodeServiceUrl);
		log.info("init ZFCas321AuthenticationFilter start");
		
		log.info("serverName[{}]",this.serverName);
		log.info("service[{}]",this.service);
		log.info("casServerLoginUrl[{}]",this.casServerLoginUrl);
		log.info("encodeServiceUrl[{}]",this.encodeServiceUrl);
		
		log.info("init ZFCas321AuthenticationFilter end");
	}

	@Override
	public void destroy() {
		this.delegate.destroy();
	}

	@Override
	protected void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain)throws ServletException, IOException {
		
		if(redirectToSaveRequestUri){
			HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
			String path = WebUtils.getPathWithinApplication(httpServletRequest);
			
			if(!"/".equals(path)){
				String queryStr = httpServletRequest.getQueryString();
				if(queryStr != null && !queryStr.equals("")){
					path = String.format("%s?%s", path,queryStr);
				}
				log.debug("save first access path[" + path + "] to session");
				httpServletRequest.getSession(true).setAttribute(Constants.SAVE_FIRST_REQUEST_URI_SESSION_KEY, path);				
			}
		}
		
		this.delegate.doFilter(request, response, chain);
	}
	
	public void setCasServerLoginUrl(String casServerLoginUrl) {
		this.casServerLoginUrl = casServerLoginUrl;
	}
	
	public String getCasServerLoginUrl() {
		return casServerLoginUrl;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public void setService(String service) {
		this.service = service;
	}

	public boolean isEncodeServiceUrl() {
		return encodeServiceUrl;
	}

	public void setEncodeServiceUrl(boolean encodeServiceUrl) {
		this.encodeServiceUrl = encodeServiceUrl;
	}

	public boolean isRedirectToSaveRequestUri() {
		return redirectToSaveRequestUri;
	}

	public void setRedirectToSaveRequestUri(boolean redirectToSaveRequestUri) {
		this.redirectToSaveRequestUri = redirectToSaveRequestUri;
	}
}
