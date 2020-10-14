/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.zfca.servlet.filter;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.subject.WebSubject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.niuca.tp.cas.client.filter.ZFSSOFilter;


/**
 * 
 * @className	： ZFCasSSOFilter
 * @description	： 门户单点登录过滤器
 * @author 		：康康（1571）
 * @date		： 2018年4月24日 上午8:42:38
 * @version 	V1.0
 */
@Deprecated
public class ZFCasSSOFilter extends ZFSSOFilter {

	static Logger log = LoggerFactory.getLogger(ZFCasSSOFilter.class);
	
	private SecurityManager securityManager;

	private String casAuthorizedProxyParam;
 
	private String casServiceUrlParam;

	private String casRenewParam;

	private String setsessionclassParam;

	private String notCheckURLListParam;

	private boolean wrapRequestParam;

	/**
	 * 重写参数初始化函数，以便可从配置文件读取相关参数
	 */
	@Override
	public void init(FilterConfig config) throws ServletException {
		
		// 代理config,引入本类的配置文件，给父类初始化
		FilterConfig proxyFilterConfig = new ProxyFilterConfig(config,this);

		// 父类初始化
		super.init(proxyFilterConfig);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc)
			throws ServletException, IOException {
		HttpServletRequest httpServletRequest =(HttpServletRequest)request;
		if(log.isDebugEnabled()) {
			log.debug("filter url:"+httpServletRequest.getRequestURI());
		}
		
		//作为当前ApplicationContext的FilterChain中的第一个Filter，必须创建Subject，绑定给当前线程
		WebSubject subject = new WebSubject.Builder(this.securityManager, request, response).buildWebSubject();
		ThreadContext.bind(subject);
		
		if(!subject.isAuthenticated()) {
			if(log.isDebugEnabled()) {
				log.debug("not auth,doFilter");
			}
			super.doFilter(request, response, fc);			
			if(log.isDebugEnabled()) {
				Subject subjectAfterDoFilter = ThreadContext.getSubject();
				boolean isAuth = subjectAfterDoFilter.isAuthenticated();
				log.debug("subject is auth:{}",isAuth);
			}
		}else {
			if(log.isDebugEnabled()) {
				log.debug("authed , skip this doFilter,do next filter");
			}
			fc.doFilter(request, response);
		}
	}



	public String getCasAuthorizedProxyParam() {
		return casAuthorizedProxyParam;
	}

	public void setCasAuthorizedProxyParam(String casAuthorizedProxyParam) {
		this.casAuthorizedProxyParam = casAuthorizedProxyParam;
	}

	public String getCasServiceUrlParam() {
		return casServiceUrlParam;
	}

	public void setCasServiceUrlParam(String casServiceUrlParam) {
		this.casServiceUrlParam = casServiceUrlParam;
	}

	public String getCasRenewParam() {
		return casRenewParam;
	}

	public void setCasRenewParam(String casRenewParam) {
		this.casRenewParam = casRenewParam;
	}

	public String getSetsessionclassParam() {
		return setsessionclassParam;
	}

	public void setSetsessionclassParam(String setsessionclassParam) {
		this.setsessionclassParam = setsessionclassParam;
	}

	public String getNotCheckURLListParam() {
		return notCheckURLListParam;
	}

	public void setNotCheckURLListParam(String notCheckURLListParam) {
		this.notCheckURLListParam = notCheckURLListParam;
	}

	public boolean isWrapRequestParam() {
		return wrapRequestParam;
	}

	public void setWrapRequestParam(boolean wrapRequestParam) {
		this.wrapRequestParam = wrapRequestParam;
	}

	private class ProxyFilterConfig implements FilterConfig {

		private FilterConfig delegate;
		private ZFCasSSOFilter zfCasSSOFilter;

		public ProxyFilterConfig(FilterConfig delegate, ZFCasSSOFilter zfCasSSOFilter) {
			super();
			this.delegate = delegate;
			this.zfCasSSOFilter = zfCasSSOFilter;
		}

		@Override
		public String getFilterName() {
			return this.delegate.getFilterName();
		}

		@Override
		public ServletContext getServletContext() {
			return this.delegate.getServletContext();
		}

		@Override
		public String getInitParameter(String name) {
			String value = null;
			if ("setsessionclass".equals(name)) {
				value = this.zfCasSSOFilter.setsessionclassParam;
			} else if ("notCheckURLList".equals(name)) {
				value = this.zfCasSSOFilter.notCheckURLListParam;
			} else if ("com.woshidaniu.niuca.tp.cas.client.filter.serviceUrl".equals(name)) {
				value = this.zfCasSSOFilter.casServiceUrlParam;
			} else if ("com.woshidaniu.niuca.tp.cas.client.filter.authorizedProxy".equals(name)) {
				value = this.zfCasSSOFilter.casAuthorizedProxyParam;
			} else if ("com.woshidaniu.niuca.tp.cas.client.filter.renew".equals(name)) {
				value = this.zfCasSSOFilter.casRenewParam;
			} else if ("com.woshidaniu.niuca.tp.cas.client.filter.wrapRequest".equals(name)) {
				value = Boolean.valueOf(this.zfCasSSOFilter.wrapRequestParam).toString();
			} else {
				value = this.delegate.getInitParameter(name);
			}
			if (log.isDebugEnabled()) {
				log.debug("do call getInitParameter, {} : {}",name,value);
			}
			return value;
		}

		@Override
		public Enumeration<String> getInitParameterNames() {
			return this.delegate.getInitParameterNames();
		}
	}
	
	public SecurityManager getSecurityManager() {
		return securityManager;
	}

	public void setSecurityManager(SecurityManager securityManager) {
		this.securityManager = securityManager;
	}
}
